package io.naztech.nuxeoclient;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.nuxeo.ecm.automation.client.Session;

import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.service.NuxeoClientService;
import io.naztech.nuxeoclient.service.NuxeoDocumentServiceException;

public class NuxeoDocumentServiceTest {
	private static final String NUXEO_URL = "http://vntdaclsappu031:8080/nuxeo";
	private static final String NUXEO_USER = "naztech.admin", NUXEO_PASS = "n@zt3ch@dm1n";

	private static NuxeoClientService service;
	private static NuxeoClient nuxeoClient;
	private static Session clientSession;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		nuxeoClient = new NuxeoClient.Builder().url(NUXEO_URL).authentication(NUXEO_USER, NUXEO_PASS).connect();
		service = new NuxeoClientService();
		service.setNuxeoClient(nuxeoClient);
//		HttpAutomationClient client = new HttpAutomationClient(NUXEO_URL + "/site/automation");
//		clientSession = client.getSession(NUXEO_USER, NUXEO_PASS);
	}

	@Before
	public void setUp() throws Exception {
//		service.setClientSession(clientSession);
	}

	@Test
	public void testCreateDocumentSingleAttachment() throws NuxeoDocumentServiceException {
//		DocumentWrapper req = DocumentWrapper.createWithName("doc-junit", "invoicetest")
//				.setDescription("sample invoice from junit")
//				.setRepoPath("/default-domain/workspaces/Sample Content/Invoice")
//				.addFile(getClassLoader().getResource("Festool_Invoice11.pdf").getFile());
//	
//		Document res = service.createDocument(req);
//		assertNotNull(res.getId());
//		System.out.println(res);
		// TODO check more what is expected ?
	}

	//@Test
	public void testCreateDocumentmultipleAttachments() throws NuxeoDocumentServiceException {
		DocumentWrapper req = DocumentWrapper.createWithName("", "");
		Document res = service.createDocument(req);
		assertNotNull(res.getId());
		// TODO check more what is expected ?
	}

	//@Test
	public void tesScanAndUploadFiles() throws NuxeoDocumentServiceException {
		File dir = new File("C:\\Nuxeo"); // directory
		File[] files = dir.listFiles();

//		for (File file : files) {
//			if (file.isFile()) {
//				DocumentWrapper req = DocumentWrapper.createWithName(file.getName().substring(0, file.getName().indexOf(".")), "invform")
//						.setDescription(file.getName().substring(0, file.getName().indexOf(".")))
//						.setRepoPath("/default-domain/workspaces/Sample Content/Test")
//						.addFile(file);
//				Document res = service.createDocument(req);
//				assertNotNull(res.getId());
//				System.out.println(res);
//			}
//		}
	}
	@Test
	public void getCurrentDate() {
		LocalDate date = LocalDate.now();
		String str = date.toString();
		System.out.println("Current date of the day: " + str);
		File theDir = new File("C:\\Users\\naym.hossain\\hotfolder" + File.separator + str);
		if (!theDir.exists()) theDir.mkdir();
	}
	@Test
	public void testCopyFiles() throws IOException {
		File source = new File("C:\\Users\\naym.hossain\\hotfolder\\New folder\\Festool_Invoice1.pdf");
        File dest = new File("C:\\Users\\naym.hossain\\hotfolder\\New folder (2)\\Festool_Invoice1.pdf");
        Files.copy(source.toPath(), dest.toPath());
	}
	
	private ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {
		}

		if (cl == null) cl = ClassLoader.getSystemClassLoader();
		return cl;
	}
}
