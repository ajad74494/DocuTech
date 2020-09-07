package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;

/**
 * @author Asadullah.galib
 * @since 2019-09-17
 */
@Service

public class DownloadFilesFromDropbox {
	private static Logger log = LoggerFactory.getLogger(DownloadFilesFromDropbox.class);

	private static final int BUFFER_SIZE = 4096;

	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;

	@Value("${import.source.string}")
	private String source;

	@Value("${import.source.string.dropbox}")
	private String link;

	private void downloadFile(String fileURL, String saveDir) {
		try {
			URL url = new URL(fileURL.replace("?dl=0", "?dl=1"));
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField(Constants.HEADERFIELD);

				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf(Constants.HEADER_INDEX_DROPBOX);
					if (index > 0) {
						fileName = disposition.substring(index + 17, disposition.length());
					}
				}
				else {
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
				}
				// fileName = "ACN_2.pdf";
				log.info("fileName = " + fileName);

				if (fileName.contains(Constants.PDF)) {
					// opens input stream from the HTTP connection
					InputStream inputStream = httpConn.getInputStream();
					String saveFilePath = saveDir + File.separator + source + link + fileName;

					// opens an output stream to save into file
					FileOutputStream outputStream = new FileOutputStream(saveFilePath);

					int bytesRead = -1;
					byte[] buffer = new byte[BUFFER_SIZE];
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					outputStream.close();
					inputStream.close();

					log.info(fileName + " download Successful");

				}
				else {

					log.warn("This is not a pdf file");
				}
			}
			else {
				log.error("No file to download. Server replied HTTP code: " + responseCode);
			}
			httpConn.disconnect();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFilesFromDropbox(String url) throws IOException {

		downloadFile(url, saveDirectory);

	}

}
