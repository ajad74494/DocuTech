package io.naztech.nuxeoclient;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.nuxeo.client.objects.blob.FileBlob;
import org.nuxeo.client.objects.upload.BatchUpload;
import org.nuxeo.client.objects.upload.BatchUploadManager;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.model.DocRef;

import io.naztech.nuxeoclient.model.DocumentWrapper;

public class TestUploadScannedFiles {
	private static final String NUXEO_URL = "http://vntdaclsappu031:8080/nuxeo";
	private static final String NUXEO_USER = "naztech.admin", NUXEO_PASS = "n@zt3ch@dm1n";
	private static NuxeoClient nuxeoClient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		nuxeoClient = new NuxeoClient.Builder().url(NUXEO_URL).authentication(NUXEO_USER, NUXEO_PASS)
				.connect();

	}

	//@Test
	public void testCreateDocument() throws IOException {
		Document document = Document.createWithName("testDocument", "invform");
		document.setPropertyValue("dc:title", "InvDoc7");
		document.setPropertyValue("dc:description", "It's an invoice document.");
		document.setPropertyValue("invform:Description2", "This document contains various data files of a patient.");
		document.setPropertyValue("invform:Invoice_id", "inv0007");

		document = nuxeoClient.repository().createDocumentByPath("/default-domain/workspaces/Sample Content/Invoice",
				document);

		/* Add main file */
		BatchUploadManager batchUploadManager = nuxeoClient.batchUploadManager();
		BatchUpload batchUpload = batchUploadManager.createBatch();

		FileBlob fileBlob = new FileBlob(FileUtils.getFile("C:\\Nuxeo\\sample2.pdf"));
		batchUpload = batchUpload.upload("1", fileBlob);

		document.setPropertyValue("file:content", batchUpload.getBatchBlob());
		document.updateDocument();

		System.out.println(document.getId());
	}

	//@Test
	public void testAddAttachments() throws IOException {
		/* Add attachments */
		HttpAutomationClient client = new HttpAutomationClient("http://localhost:8080/nuxeo/site/automation");
		Session session = client.getSession("Administrator", "Administrator");
		org.nuxeo.ecm.automation.client.model.DocRef docRef = new DocRef("8ac16853-d849-41af-be77-a9a593dba4eb");
		DocumentService rs = session.getAdapter(DocumentService.class);

		// get files from folder
		File dir = new File("C:\\Nuxeo"); // directory
		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				org.nuxeo.ecm.automation.client.model.Blob myblob = new org.nuxeo.ecm.automation.client.model.FileBlob(
						file);
				rs.setBlob(docRef, myblob, "invform:files");
			}
		}
		Document document = nuxeoClient.repository().fetchDocumentById(docRef.toString());
		System.out.println(document.getPath().toString());
		System.out.println(rs.getDocument(docRef));
	}

	//@Test
	public void testDocumentWrapper() {
		DocumentWrapper wrapper = DocumentWrapper.createWithName("testDocument", "invform");
		wrapper.setTitle("");
		wrapper.setDescription("");
		wrapper.setRepoPath("");
		wrapper.addAttribute("Invoice_id", "9302930290");
		//wrapper.addFile("C:\\Nuxeo\\sample2.pdf");

		Document doc = wrapper.buildDocument();
		doc = nuxeoClient.repository().createDocumentByPath(wrapper.getRepoPath(), doc);
	}

	//@Test
	public void testGetDocumentById() {
		Document ob = nuxeoClient.repository().fetchDocumentById("8ac16853-d849-41af-be77-a9a593dba4eb");
		System.out.println(ob.getPath());
		for (Entry<String, Object> en : ob.getProperties().entrySet()) {
			System.out.println(en.getKey() + " :: " + en.getValue());
		}
	}
	
	//@Test
	public void testGetDocumentByPath() {
		Document ob = nuxeoClient.repository().fetchDocumentByPath("/default-domain/workspaces/Sample%20Content/Test/testInv12233.1554706681089");
		Map<String, Object> obj = ob.getProperties();
		System.out.println(obj.size());
		for (Entry<String, Object> en : obj.entrySet()) {
			System.out.println(en.getKey() + " :: " + en.getValue());
		}
	}
}
