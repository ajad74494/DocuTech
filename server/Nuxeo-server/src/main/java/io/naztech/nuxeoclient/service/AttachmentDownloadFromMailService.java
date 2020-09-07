package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.ActionType;
/* import src.main.java.io.naztech.nuxeoclient.constants.Constants; */
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocCount;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.utility.EmailSearchTerm;

/**
 * @author Asadullah.galib
 * 
 * @since 2019-09-15
 **/
@Service
public class AttachmentDownloadFromMailService extends AbstractScannedInvoidUploadService {
	private static Logger log = LoggerFactory.getLogger(AttachmentDownloadFromMailService.class);

	@Autowired
	private DownloadFilesFromGoogleDriveUrl serveGoogleDrive;

	@Autowired
	private DownloadFilesFromOneDriveUrl serveOneDrive;

	@Autowired
	private DownloadFilesFromDropbox serveDropbox;

	@Autowired
	private DocCountService docCountService;

	@Value("${mailcredential.host}")
	private String host;

	@Value("${mailcredential.port}")
	private String port;

	@Value("${mailcredential.username}")
	private String userName;

	@Value("${mailcredential.password}")
	private String password;

	@Value("${mailcredential.savedirectory}")
	private String saveDirectory;

	@Value("${import.ssl.trust}")
	private String sslTrust;

	@Value("${import.ssl.socketFactory}")
	private String sslSockFack;

	@Value("${import.imap.fetchsize}")
	private String fetchsize;

	@Value("${import.imap.fetchsize.value}")
	private String fetchvalue;

	@Value("${import.imap.host}")
	private String imapHost;

//	@Value("${import.imap.port}")
//	private String imapPort;
	
	@Value("${mail.imap.port}")
	private Integer imapPort;

	@Value("${import.imap.socketFactoryClass}")
	private String sockFackClass;

	@Value("${import.javax.socketFactory}")
	private String javSockFack;

	@Value("${import.imap.socketFactory.fallback}")
	private String fallback;

	@Value("${import.imap.ssl.enable}")
	private String sslEnable;

	@Value("${import.imap.socketFactoy.port}")
	private String sockFackPort;

	@Value("${import.googledrive.string}")
	private String googleDriveString;

	@Value("${import.onedrive.string1}")
	private String oneDriveString1;

	@Value("${import.onedrive.string2}")
	private String oneDriveString2;

	@Value("${import.dropbox.string}")
	private String dropboxString;

	@Value("${import.contentType.pdf}")
	private String pdf;

	@Value("${import.contentType.binary}")
	private String binary;

	@Value("${import.source.string}")
	private String source;

	@Value("#{'${list.email.sender}'.split(',')}")
	private List<String> senderList;
	
	/**
	 * Download attachment/attachments from email by date & filter by sender list
	 */
	private void downloadEmailAttachments(String host, String port, String userName, String password)
			throws ParseException {

		Date date = new Date();
		Properties properties = new Properties();

		DocCount docCount = new DocCount();

		// For the last data inserted to db
		docCount.setIsMax(1);

		/**
		 * Get the last downloaded time from db
		 */
		List<DocCount> docCountList = docCountService.processAction(ActionType.SELECT.toString(), docCount);

		if (docCountList.parallelStream().findFirst().isPresent()) {
			date = docCountList.parallelStream().findFirst().get().getModifiedOn();
		}

		try {

			// SSL setting
			//properties.setProperty(sslEnable, "true");
			
			properties.setProperty("mail.store.protocol", Constants.IMAP);
			properties.setProperty("mail.imap.ssl.enable", Constants.FALSE);
			properties.setProperty("mail.imap.starttls.enable", Constants.TRUE);
	        Session session = Session.getInstance(properties, null);
	        Store store = null;
			try {
				// connects to the message store
				store = session.getStore();
				store.connect(host, imapPort, userName, password);

				// opens the inbox folder
				Folder folderInbox = store.getFolder(Constants.INBOX);
				folderInbox.open(Folder.READ_WRITE);

				// create parsed folder if not existed
				Folder newFolder = store.getFolder(Constants.PARSED);

				if (!newFolder.exists()) {
					if (newFolder.create(Folder.HOLDS_MESSAGES)) {
						newFolder.setSubscribed(true);

						log.debug("Folder was created successfully");
					} else {
						log.error("Could not create New folder");
					}
				}

				Date pastDate = date;
				List<Message> msgList = new ArrayList<>();

				// Message[] dateTermMsg = folderInbox.search(new
				// ReceivedDateTerm(ComparisonTerm.GT, pastDate));
				// msgList.addAll(Arrays.asList(dateTermMsg));

				for (String sender : senderList) {

					/*
					 * Date dt = null; try { dt = Constants.DTF.parse("2019-12-10 10:19:29"); }
					 * catch (ParseException e) { e.printStackTrace(); }
					 */

					EmailSearchTerm est = new EmailSearchTerm(pastDate, sender);

					Message[] dateTermMsg;
					try {
						dateTermMsg = folderInbox.search(est);

						log.info("Message found by sender, date & count: [{}/{}/{}]", sender, pastDate,
								dateTermMsg.length);
						msgList.addAll(Arrays.asList(dateTermMsg));

						log.debug("Message count on list: [{}]", msgList.size());
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}

				if (msgList.isEmpty()) {
					log.info("*********No new message**********");
				} else {

					log.debug("Total Message found on date: [{}/{}]", pastDate, msgList.size());

					for (Message message : msgList) {

						message.setFlag(Flags.Flag.SEEN, true);

						try {
							saveAttachment(message, getAttachmentCount(message), message);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				// disconnect
				folderInbox.close(false);

				store.close();

			} catch (NoSuchProviderException ex) {
				log.error("No provider for IMAP: {}", ex);
			} catch (MessagingException ex) {
				log.error("Could not connect to the message store: {}", ex);
			}

		} catch (Exception e) {
			log.error("Error downloading file from mail: {}", e.getMessage());
		} /*
			 * catch (GeneralSecurityException e) {
			 * log.error("Error downloading file from mail: {}", e.getMessage()); }
			 */
	}

	private void saveAttachment(Part p, int aCount, Message message) {

		try {

			if (p.isMimeType(Constants.TEXT_HTML_CONTENT_VALUE)) {
				getLinkFromHtml(p);
			} else if (p.isMimeType(Constants.MULTIPLE_PART)) {
				Multipart mp = (Multipart) p.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++)
					saveAttachment(mp.getBodyPart(i), aCount, message);
			}
			// check if the content is a nested message
			else if (p.isMimeType(Constants.MESSAGE_CONTENT)) {
				saveAttachment((Part) p.getContent(), aCount, message);
			}

			// check if content has pdf
			if (p.isMimeType(pdf) || p.isMimeType(binary)) {

				String fileName = MimeUtility.decodeText(p.getFileName());

				/**
				 * Get sequence from database
				 */
				BigInteger seq = null;
				DocCount docCount = new DocCount();
				List<DocCount> docCountList = docCountService.processAction(ActionType.SEQUENCE.toString(), docCount);

				if (docCountList.parallelStream().findFirst().isPresent()) {
					seq = docCountList.parallelStream().findFirst().get().getFileDownloadId();
				}

				String name = source + seq.toString() + Constants.UNDERSCORE + fileName;

				log.debug("File save to directory and name: [{}/{}]", saveDirectory, name);

				((MimeBodyPart) p).saveFile(saveDirectory + File.separator + name);

				log.info("******File downloaded successfully*******");

				File savedFile = new File(saveDirectory + File.separator + name);
				savedFile.setExecutable(true, false);
				savedFile.setReadable(true, false);
				savedFile.setWritable(true, false);

				Address[] adrs = message.getFrom();
				String sub = message.getSubject().isEmpty() ? "No Subject" : message.getSubject();

				docCount.setNoOfFilesDownloaded(1);
				docCount.setSenderAddress(adrs[0].toString());
				docCount.setDownloadSource(Constants.EMAIL);
				docCount.setDocType(Constants.INVOICE);
				docCount.setFileName(name);
				docCount.setSubject(Constants.DTF_SENT.format(message.getSentDate()) + Constants.UNDERSCORE + sub
						+ Constants.UNDERSCORE + aCount);

				docCountService.processAction(ActionType.NEW.toString(), docCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get count of attachments within one email
	 */
	private int getAttachmentCount(Message message) {

		int count = 0;

		try {
			Object object = message.getContent();

			if (object instanceof Multipart) {
				Multipart parts = (Multipart) object;

				for (int i = 0; i < parts.getCount(); ++i) {
					MimeBodyPart part = (MimeBodyPart) parts.getBodyPart(i);

					if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))
						++count;
				}
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * Get the link from emil body and send to service class for further processing
	 */
	private void getLinkFromHtml(Part p) {

		try {
			Pattern linkPattern = Pattern.compile("href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			String desc = p.getContent().toString();

			Matcher pageMatcher = linkPattern.matcher(desc);

			while (pageMatcher.find()) {
				// Search for google drive link
				if (pageMatcher.toString().contains(googleDriveString))
					// googleDriveLinks.add(pageMatcher1.group(1));
					serveGoogleDrive.downloadFileFromGoogledrive(pageMatcher.group(1));
				// Search for one drive link
				if (pageMatcher.toString().contains(oneDriveString1)
						|| pageMatcher.toString().contains(oneDriveString2))
					// oneDriveLinks.add(pageMatcher1.group(1));
					serveOneDrive.downloadFilesFromOneDriveUrl(pageMatcher.group(1));
				// Search for dropbox link
				if (pageMatcher.toString().contains(dropboxString))
					// oneDriveLinks.add(pageMatcher1.group(1));
					serveDropbox.downloadFilesFromDropbox(pageMatcher.group(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author mahbub.hasan
	 * @throws ParseException
	 * @since 2019-12-08 Moved scheduler from Controller to service class and
	 *        deleted unnecessary controller class
	 *        PdfDownloadFromMailController.java
	 */
	public void downloadAttachments() throws ParseException {
		downloadEmailAttachments(host, port, userName, password);
	}

	@Override
	public void uploadDocument(Invoice invoice, File file, Boolean sendToNuxeoFlag) throws IOException {
		// TODO Auto-generated method stub
	}

}