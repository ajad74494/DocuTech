package io.naztech.nuxeoclient.service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Asadullah.galib
 * @since 2019-09-17
 */
@Service
public class DownloadFilesFromFtpServerService {

	private static Logger log = LoggerFactory.getLogger(DownloadFilesFromFtpServerService.class);

	@Value("${ftp.port}")
	private int portNo;

	@Value("${ftp.url}")
	private String ftpUrl;

	@Value("${ftp.user}")
	private String userName;

	@Value("${ftp.pass}")
	private String pass;

	@Value("${ftp.downloadPath}")
	private String downloadPath;

	//@Scheduled(fixedRate = 10000, initialDelay = 500)
	public void filesDownloadFromFtp() {
		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.connect(ftpUrl, portNo);
			ftpClient.login(userName, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			FTPFile[] files = ftpClient.listFiles("/upload", new FTPFileFilter() {

				@Override
				public boolean accept(FTPFile file) {

					return (file.isFile() && file.getName().endsWith(".pdf"));
				}
			});

			if (files.length == 0) {
				log.warn("No new files");
			}
			else {
				for (FTPFile file : files) {
					String remoteFile1 = "/upload/";
					String remoteFile2 = "/download/";
					OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadPath + file.getName()));
					boolean success = ftpClient.retrieveFile(remoteFile1 + file.getName(), outputStream1);
					outputStream1.close();
					if (success) {
						log.info("File " + file.getName() + " has been downloaded successfully.");
						boolean removeSuccess = ftpClient.rename(remoteFile1 + file.getName(), remoteFile2 + file.getName());
						if (removeSuccess) {
							log.info("File has been removed to download ");
						}
						else {
							log.error("File is not removed");
						}

					}

				}
			}

		}
		catch (IOException ex) {
			log.warn("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
