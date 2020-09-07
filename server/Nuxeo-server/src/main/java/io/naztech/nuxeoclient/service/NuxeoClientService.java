
package io.naztech.nuxeoclient.service;

import java.io.File;

import javax.inject.Named;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.nuxeo.client.objects.upload.BatchUpload;
import org.nuxeo.ecm.automation.client.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.naztech.nuxeoclient.model.DocumentWrapper;
/*import lombok.Setter;*/

/**
 * TODO ISSUE can not get custom attributes (properties) using
 * getDocumentById/Path call.
 * 
 * @author armaan.choudhury
 * @since 2019-04-15
 */
@Named
/* @Setter */
public class NuxeoClientService {
	private static Logger log = LoggerFactory.getLogger(NuxeoClientService.class);

	private Session clientSession;
	private NuxeoClient nuxeoClient;

	public Session getClientSession() {
		return clientSession;
	}

	public void setClientSession(Session clientSession) {
		this.clientSession = clientSession;
	}

	public NuxeoClient getNuxeoClient() {
		return nuxeoClient;
	}

	public void setNuxeoClient(NuxeoClient nuxeoClient) {
		this.nuxeoClient = nuxeoClient;
	}

	public Document createDocument(DocumentWrapper req) {
		if (req.getFile() == null) {
			try {
				throw new NuxeoDocumentServiceException("Document request must contain at least one file attachment");
			} catch (NuxeoDocumentServiceException e1) {
				log.info(e1.getMessage());
				return null;
			}
		}
		Document doc = req.buildDocument();
		try {
			/* Create a document in Nuxeo platform repository */
			 
			log.info(doc.getId());
			log.info(doc.getIsCheckedOut());
			log.info(doc.getName());
			log.info(doc.getPath());
			log.info(doc.getLastModified());
			log.info(doc.getLock());
			log.info(doc.getLockCreated());
			log.info(doc.getRepositoryName());
			log.info(doc.getPath());
			log.info(doc.getType());
			log.info(doc.getUid());
			log.info(doc.getChangeToken());
			
			
			doc = nuxeoClient.repository().createDocumentByPath(req.getRepoPath(), req.buildDocument()); 
			log.info(doc.toString()); 
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		// return req.getFiles().size() == 1 ? uploadOneFile(doc, req.getFiles().get(0))
		// : uploadMultipleFiles(doc, req);
		log.info("Hi"+uploadOneFile(doc, req.getFile()));
		return uploadOneFile(doc, req.getFile());
	}

	// TODO what response need to send to service caller
	private Document uploadOneFile(Document doc, File string) {
		/* Create batch */
		BatchUpload batch = nuxeoClient.batchUploadManager().createBatch();

		/* Upload file attachment to batch */
		batch = batch.upload("1", new org.nuxeo.client.objects.blob.FileBlob(string));

		/* Attach file(s) to the document uploaded into batch */
		doc.setPropertyValue("file:content", batch.getBatchBlob());
		return doc.updateDocument();
	}

//	private Document uploadMultipleFiles(Document doc, DocumentWrapper req) throws NuxeoDocumentServiceException {
//		DocRef docRef = new DocRef(doc.getId());
//		DocumentService autoService = clientSession.getAdapter(DocumentService.class);
//		// TODO maybe save first file in "file:content" and rest in <schemaTag>:files
//		for (File file : req.getFiles()) {
//			if (!file.isFile())
//				continue;
//			try {
//				autoService.setBlob(docRef, new FileBlob(file), req.getType() + ":files");
//			} catch (IOException | NuxeoClientRemoteException e) {
//				throw new NuxeoDocumentServiceException(e);
//			}
//		}
//		return doc;
//	}

	public Document getDocument(String pathOrId) {
		Document ob = nuxeoClient.repository().fetchDocumentById(pathOrId);
		if (ob == null)
			ob = nuxeoClient.repository().fetchDocumentByPath(pathOrId);
		return ob;
	}
}
