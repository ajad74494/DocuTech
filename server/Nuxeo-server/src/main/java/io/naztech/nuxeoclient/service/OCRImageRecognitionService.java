package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/*import com.qoppa.pdf.PDFException;
import com.qoppa.pdfText.PDFText;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Asadullah.galib
 * @since 2019-09-17
 */
@Service
public class OCRImageRecognitionService {
	private static Logger log = LoggerFactory.getLogger(OCRImageRecognitionService.class);

	@Value("${import.imageRecognition.abbyyInboxFolder-path}")
	private String abbyInboxPath;

	@Value("${import.imageRecognition.itextInboxFolder-path}")
	private String iTextInboxPath;

	@Value("${import.imageRecognition.mailInboxFolder-path}")
	private String mailInboxPath;

	/*
	 * @Scheduled(fixedRate = 10000, initialDelay = 500)
	 * public void imageRecognition() throws PDFException, IOException { List<File>
	 * itextattachments = new ArrayList<File>(); List<File> abbytattachments = new
	 * ArrayList<File>(); File[] fileList = new File(mailInboxPath).listFiles(); if
	 * (fileList == null || fileList.length < 1) return; for (File pdf : fileList) {
	 * InputStream inputStream = new FileInputStream(pdf.getAbsolutePath()); PDFText
	 * pdfText = new PDFText(inputStream, null); int pageCount =
	 * pdfText.getPageCount(); for (int i = 0; i < pageCount; i++) {
	 * 
	 * String pageText = pdfText.getText(i);
	 * 
	 * if (pageText != null && pageText.trim().length() > 0) {
	 * itextattachments.add(pdf); break; } else { abbytattachments.add(pdf); } }
	 * inputStream.close(); } moveToItextInbox(itextattachments);
	 * moveToABBYInbox(abbytattachments); }
	 */
	@SuppressWarnings("unused")
	private void moveToABBYInbox(List<File> attachments) {
		String abbyInbox = abbyInboxPath + File.separator;
		File abbDir = new File(abbyInbox);
		if (!abbDir.exists()) abbDir.mkdir();

		for (File doc : attachments) {
			if (!new File(abbyInbox + File.separator + doc.getName()).exists()) try {
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(abbyInbox + File.separator + doc.getName()),
				        StandardCopyOption.REPLACE_EXISTING);
				log.info("Files moved to Abbyy Inbox");
			}
			catch (IOException e) {
				log.info("Files couldn't move to Abbyy Inbox", e);
			}
		}
	}

	@SuppressWarnings("unused")
	private void moveToItextInbox(List<File> attachments) {
		String itextInbox = iTextInboxPath + File.separator;
		File itextDir = new File(itextInbox);
		if (!itextDir.exists()) itextDir.mkdir();

		for (File doc : attachments) {
			if (!new File(itextInbox + File.separator + doc.getName()).exists()) try {
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(itextInbox + File.separator + doc.getName()),
				        StandardCopyOption.REPLACE_EXISTING);
				log.info("Files moved to Itext Inbox");
			}
			catch (IOException e) {
				log.info("Files couldn't move to Itext Inbox", e);
			}
		}
	}
}
