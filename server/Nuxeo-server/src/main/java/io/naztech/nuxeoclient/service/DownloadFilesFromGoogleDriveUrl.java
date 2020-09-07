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

public class DownloadFilesFromGoogleDriveUrl {
	private static Logger log = LoggerFactory.getLogger(DownloadFilesFromGoogleDriveUrl.class);
	private static final int BUFFER_SIZE = 4096;

	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;

	@Value("${import.source.string}")
	private String source;

	@Value("${import.source.string.googledrive}")
	private String link;

	private void downloadFile(String fileURL, String saveDir) {
		try {
			String newUrl = urlFormatter(fileURL);
			URL url = new URL(newUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField(Constants.HEADERFIELD);

				fileName = getFileNameFromUrl(disposition, newUrl);
				log.info("fileName = " + fileName);

				//downloads pdf file only
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

	private String getFileNameFromUrl(String disposition, String newUrl) {
		String fileName = "";

		int index1 = disposition.indexOf(Constants.INDEX1);
		index1 += 10;
		int index2 = disposition.indexOf(Constants.INDEX2);
		fileName = disposition.substring(index1, index2);
		return fileName;
	}

	/*
	 * Handles 3 kinds of links (they can be preceeded by https://): -
	 * drive.google.com/open?id=FILEID - (Still in progress)
	 * drive.google.com/file/d/FILEID/view?usp=sharing -
	 * drive.google.com/uc?id=FILEID&export=download
	 */
	private String urlFormatter(String url) {
		int index1 = 0;
		int index2 = 0;
		String contain_export_download = "export=download";
		String contain_file_d = "file/d/";
		String contain_view = "/view";
		String contain_open_Id = "open?id=";

		if (url.contains(contain_export_download)) {
			return url;
		}
		else if (url.contains(contain_file_d)) {

			index1 = url.indexOf(contain_file_d);
			index1 += 7;
			index2 = url.indexOf(contain_view);

		}
		else if (url.contains(contain_open_Id)) {
			index1 = url.indexOf(contain_open_Id);
			index1 += 8;
			index2 = url.length();
		}

		String fileId = url.substring(index1, index2);
		String finalUrl = Constants.GOOGLEDRIVE_FINAL_URL1 + fileId + Constants.GOOGLEDRIVE_FINAL_URL2;
		return finalUrl;
	}

	public void downloadFileFromGoogledrive(String url) {
		downloadFile(url, saveDirectory);

	}
}
