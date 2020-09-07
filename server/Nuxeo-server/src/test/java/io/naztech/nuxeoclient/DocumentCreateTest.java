package io.naztech.nuxeoclient;

import java.io.File;

import org.junit.BeforeClass;
import org.nuxeo.client.NuxeoClient;

public class DocumentCreateTest {
	private static final String SITE_URL = "http://vntdaclsappu031:8080/nuxeo";
	private static NuxeoClient nuxeoClient;
	public File file = new File("C:\\Nuxeo\\sample.pdf");
	// Batch Upload Manager

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		nuxeoClient = new NuxeoClient.Builder().url(SITE_URL).authentication("naztech.admin", "n@zt3ch@dm1n")
				.connect();

	}

//	@Test
//	public void testCreateEmptyDocument() {
//		Document document = Document.createWithName("file", "File");
//		document.setPropertyValue("dc:title", "new title");
//		document = nuxeoClient.repository().createDocumentByPath("/default-domain/workspaces/Sample Content", document);
//		System.out.println(document);
//	}

	/*
	 * @Test public void testCreateEmptyInv() {
	 * 
	 * try { // Batch Upload Manager BatchUploadManager batchUploadManager =
	 * nuxeoClient.batchUploadManager(); BatchUpload batchUpload =
	 * batchUploadManager.createBatch();
	 * 
	 * // Upload File FileBlob fileBlob2 = new
	 * FileBlob(FileUtils.getFile("C:\\Nuxeo\\sample2.pdf")); batchUpload =
	 * batchUpload.upload("2", fileBlob2);
	 * 
	 * Document document = Document.createWithName("testInv12233", "invform");
	 * document.setPropertyValue("dc:title", "Test14");
	 * document.setPropertyValue("dc:description", "test description");
	 * document.setPropertyValue("invform:Description2", "desc 2");
	 * document.setPropertyValue("invform:Invoice_id", "invid:123456789");
	 * 
	 * document = nuxeoClient.repository().
	 * createDocumentByPath("/default-domain/workspaces/Sample Content/Test",
	 * document);
	 * 
	 * document.setPropertyValue("file:content", batchUpload.getBatchBlob());
	 * 
	 * document.updateDocument();
	 * 
	 * HttpAutomationClient client = new
	 * HttpAutomationClient("http://localhost:8080/nuxeo/site/automation");
	 * 
	 * // Open a session as Administrator with password Administrator Session
	 * session = client.getSession("Administrator", "Administrator");
	 * 
	 * String docId = document.getId(); File file =
	 * FileUtils.getFile("C:\\Nuxeo\\sample2.pdf");
	 * 
	 * // The DocumentService will give you some nice shortcut to the most // common
	 * operations DocumentService rs = session.getAdapter(DocumentService.class);
	 * 
	 * // Create a document Ref object with the id of the document
	 * org.nuxeo.ecm.automation.client.model.DocRef docRef = new DocRef(docId);
	 * 
	 * // Create a blob object from the file to upload
	 * org.nuxeo.ecm.automation.client.model.Blob myblob = new
	 * org.nuxeo.ecm.automation.client.model.FileBlob( file);
	 * 
	 * // Use DocumentService to attach the blob. Giving files:files as // argument
	 * will add the blob to the existing attachment rs.setBlob(docRef, myblob,
	 * "invform:files");
	 * 
	 * System.out.println(docId);
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } }
	 */
}
