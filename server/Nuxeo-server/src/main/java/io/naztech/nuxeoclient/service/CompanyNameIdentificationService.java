//package io.naztech.nuxeoclient.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
///**
// * @author Asadullah.galib
// * @since 2019-09-17
// */
//
//@Service
//public class CompanyNameIdentificationService {
//	private static Logger log = LoggerFactory.getLogger(CompanyNameIdentificationService.class);
//
//	@Value("${import.festool.festoolFolder-path}")
//	private  String festoolInboxPath;
//
//	@Value("${import.nmbs.nmbsFolder-path}")
//	private  String nmbsInboxPath;
//
//	@Value("${import.ox.oxFolder-path}")
//	private  String oxgroupInboxpath;
//
//	@Value("${import.itext.mailInbox}")
//	private  String mailInbox;
//
//	@Value("${import.aps.apsFolder-path}")
//	private  String apsInboxPath;
//
//	@Value("${import.bristan.bristanFolder-path}")
//	private  String bristanInboxPath;
//
//	@Value("${import.dpd.dpdFolder-path}")
//	private  String dpdInboxPath;
//
//	@Value("${import.ctech.ctechFolder-path}")
//	private  String ctechInboxPath;
//
//	@Value("${import.blaklader.blakladerFolder-path}")
//	private  String blakladerInboxPath;
//
////	public void logoDetection() throws Exception {
////		List<File> festoolAttachments = new ArrayList<File>();
////		List<File> nmbsAttachments = new ArrayList<File>();
////		List<File> oxAttachments = new ArrayList<File>();
////		List<File> apsAttachments = new ArrayList<File>();
////		List<File> bristanAttachments = new ArrayList<File>();
////		List<File> dpdAttachments = new ArrayList<File>();
////		List<File> ctechAttachments = new ArrayList<File>();
////		List<File> blakladerAttachments = new ArrayList<File>();
////
////		File[] fileList = new File(mailInbox).listFiles();
////		log.info("Scanning for files in Itext Inbox folder");
////		if (fileList == null || fileList.length < 1) {
////			log.info("No New File in Itext inbox folder");
////			return;
////		}
////		for (File pdf : fileList) {
////
////			PDFReader reader = new PDFReader((new File(mailInbox + pdf.getName())));
////			reader.open();
////			int pages = reader.getNumberOfPages();
////			for (int i = 0; i < pages; i++) {
////				Ocr.setUp();
////				Ocr ocr = new Ocr();
////				ocr.startEngine("eng", Ocr.SPEED_FASTEST);
////				String text = ocr.recognize(new File[] { new File(mailInbox + pdf.getName()) }, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT,
////				        0, null);
////				if (text.contains("FEST-J].")) {
////					festoolAttachments.add(pdf);
////				}
////				if (text.contains("INDEPENDENTS")) {
////					nmbsAttachments.add(pdf);
////				}
////
////				if (text.contains("sales@oxgroup.co.uk")) {
////					oxAttachments.add(pdf);
////				}
////
////				if (text.contains("BristanGroup")) {
////					bristanAttachments.add(pdf);
////				}
////
////				if (text.contains("APS")) {
////					apsAttachments.add(pdf);
////				}
////
////				if (reader.extractTextFromPage(i).contains("C-Tec London Ltd")) {
////					ctechAttachments.add(pdf);
////				}
////
////				if (reader.extractTextFromPage(i).contains("Blaklader Workwear Ltd")) {
////					blakladerAttachments.add(pdf);
////				}
////
////				if (reader.extractTextFromPage(i).contains("dpdlocal.co.uk")) {
////					dpdAttachments.add(pdf);
////				}
////
////				ocr.stopEngine();
////
////			}
////			reader.close();
////		}
////		moveToFestoolFolderPath(festoolAttachments);
////		moveToNMBSFolderPath(nmbsAttachments);
////		moveToOXGroupFolderPath(oxAttachments);
////		moveToAPSGroupFolderPath(apsAttachments);
////		moveToDpdGroupFolderPath(dpdAttachments);
////		moveToBristanGroupFolderPath(bristanAttachments);
////		moveToBlakladerGroupFolderPath(blakladerAttachments);
////		moveToCtechGroupFolderPath(ctechAttachments);
////
////	}
//
//	private void moveToFestoolFolderPath(List<File> attachments) {
//		String festoolInbox = festoolInboxPath + File.separator;
//		File abbDir = new File(festoolInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(festoolInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(festoolInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to festool Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to festool Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToNMBSFolderPath(List<File> attachments) {
//		String nmbsInbox = nmbsInboxPath + File.separator;
//		File itextDir = new File(nmbsInbox);
//		if (!itextDir.exists()) itextDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(nmbsInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(nmbsInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//
//				log.info(doc.getName() + " File moved to nmbs Inbox");
//
//			}
//			catch (IOException e) {
//
//				log.error(doc.getName() + " File couldn't move to nmbs Inbox" + e);
//
//			}
//		}
//	}
//
//	private void moveToAPSGroupFolderPath(List<File> attachments) {
//		String apsInbox = apsInboxPath + File.separator;
//		File abbDir = new File(apsInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(apsInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(apsInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to APS Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to APS Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToDpdGroupFolderPath(List<File> attachments) {
//		String dpdInbox = dpdInboxPath + File.separator;
//		File abbDir = new File(dpdInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(dpdInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(dpdInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to DPD Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to DPD Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToBristanGroupFolderPath(List<File> attachments) {
//		String bristanInbox = bristanInboxPath + File.separator;
//		File abbDir = new File(bristanInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(bristanInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(bristanInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to Bristan Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to Bristan Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToBlakladerGroupFolderPath(List<File> attachments) {
//		String blakladerInbox = blakladerInboxPath + File.separator;
//		File abbDir = new File(blakladerInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(blakladerInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(blakladerInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to Blaklader Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to Blaklader Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToCtechGroupFolderPath(List<File> attachments) {
//		String ctechInbox = ctechInboxPath + File.separator;
//		File abbDir = new File(ctechInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(ctechInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(ctechInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to C-Tech Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to C-Tech Inbox" + e);
//			}
//		}
//	}
//
//	private void moveToOXGroupFolderPath(List<File> attachments) {
//		String oxInbox = oxgroupInboxpath + File.separator;
//		File abbDir = new File(oxInbox);
//		if (!abbDir.exists()) abbDir.mkdir();
//
//		for (File doc : attachments) {
//			if (!new File(oxInbox + File.separator + doc.getName()).exists()) try {
//				java.nio.file.Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(oxInbox + File.separator + doc.getName()),
//				        StandardCopyOption.REPLACE_EXISTING);
//				log.info(doc.getName() + " File moved to OX Inbox");
//			}
//			catch (IOException e) {
//				log.error(doc.getName() + " File couldn't move to OX Inbox" + e);
//			}
//		}
//	}
//
//}
