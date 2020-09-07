package io.naztech.nuxeoclient.controller;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.itextpdf.io.IOException;

import io.naztech.nuxeoclient.service.AttachmentDownloadFromMailService;
import io.naztech.nuxeoclient.service.SageFiftyUKPrepareExlService;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @author Asadullahill Galib
 * @since 2020-03-24
 **/
@Controller
@EnableScheduling
@ConditionalOnProperty(name = "spring.enable.scheduling")
public class NuxeoScheduler {
	private static Logger log = LoggerFactory.getLogger(NuxeoScheduler.class);

	@Value("${import.root.folder_path}")
	private String rootPath;

	@Value("${import.root.exception_folder_path}")
	private String exceptionPath;

	@Value("${import.root.processed_folder_path}")
	private String processedPath;

	@Value("${send.http.ftp.url}")
	private String targetUrl;

	@Value("${dir.inPath}")
	private String inPath;

	@Autowired
	private AttachmentDownloadFromMailService atcMail;

	@Autowired
	private SageFiftyUKPrepareExlService sageFiftyUKPrepareExlService;

	@Autowired
	private PdfBoxController pdfBoxController;

	private List<String> result = new ArrayList<String>();

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

	private static boolean addTree(File file, Collection<File> all) {

		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				all.add(child);
				addTree(child, all);
			}
			return true;
		} else
			return false;
	}

	public void insertTransIntoSage50(String today) throws ParseException {
		log.info("Excel data import: {}", sageFiftyUKPrepareExlService.importDataInExcel(true));
		log.info("Excel data import: {}", sageFiftyUKPrepareExlService.importDataInExcel(today, today));
	}

	/**
	 * @author muhammad.tarek
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 * @since 2019-12-10 Scheduler for File download initial delay 0.5 second
	 */
	@Scheduled(fixedRateString = "${import.scheduled.value.attachmentDownload}", initialDelay = 500)
	public void attachmentDownload() throws ParseException, InterruptedException, java.io.IOException {

		// download pdf from mail
		atcMail.downloadAttachments();
	}

	@Bean
	private void doWatch() throws IOException, InterruptedException {

		WatchService watchService;
		try {
			watchService = FileSystems.getDefault().newWatchService();

			Path path = Paths.get(inPath);
			path.register(watchService, ENTRY_MODIFY);

			log.info("Watch service registered dir: " + path.toString());

			for (;;) {

				WatchKey key;

				try {
					log.info("Waiting for key to be signalled...");
					key = watchService.take();
				} catch (InterruptedException ex) {
					log.info("Interrupted Exception");
					return;
				}

				List<WatchEvent<?>> eventList = key.pollEvents();
				log.info("Process the pending events for the key: " + eventList.size());

				for (WatchEvent<?> genericEvent : eventList) {

					WatchEvent.Kind<?> eventKind = genericEvent.kind();
					log.info("Event kind: " + eventKind);

					if (eventKind == OVERFLOW) {
						continue; // pending events for loop
					}

					File file = new File(inPath);
					File[] files = file.listFiles();
					pdfBoxController.pdfDataExtraction(files);

					// remove file
					fileRemover();

					// export data to excel for sage50
					insertTransIntoSage50(SageFiftyUKPrepareExlService.getRightNowDateAndTime().toString());
				}

				boolean validKey = key.reset();
				log.info("Key reset");

				if (!validKey) {
					log.info("Invalid key");
					break; // infinite for loop
				}

			} // end infinite for loop

			watchService.close();
			log.info("Watch service closed.");
		} catch (java.io.IOException | ParseException e) {
			log.error(e.toString());
		}
	}

	private void fileRemover() throws java.io.IOException {
		Collection<File> allProcessedFiles = new ArrayList<>();
		addTree(new File(inPath), allProcessedFiles);
		try {
			for (File file : allProcessedFiles) {
				if (file.isFile()) {
					Files.delete(Paths.get(file.getAbsolutePath()));
				}
			}
		} catch (IOException e) {
		}

	}
}