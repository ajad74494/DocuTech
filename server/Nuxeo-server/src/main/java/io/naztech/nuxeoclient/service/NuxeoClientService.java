
package io.naztech.nuxeoclient.service;

import java.io.File;

import javax.inject.Named;

import org.nuxeo.client.NuxeoClient;
import org.nuxeo.client.objects.Document;
import org.nuxeo.client.objects.upload.BatchUpload;
import org.nuxeo.ecm.automation.client.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.Invoice;

/**
 * @author muhammad.tarek
 * @since 2020-09-06
 */
@Named
/* @Setter */
public class NuxeoClientService {
	private static Logger log = LoggerFactory.getLogger(NuxeoClientService.class);

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

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
			doc = nuxeoClient.repository().createDocumentByPath(req.getRepoPath(), req.buildDocument());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
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

	public Document getDocument(String pathOrId) {
		Document ob = nuxeoClient.repository().fetchDocumentById(pathOrId);
		if (ob == null)
			ob = nuxeoClient.repository().fetchDocumentByPath(pathOrId);
		return ob;
	}

	public DocumentWrapper convertInvoiceToDocumentReq(Invoice inv) {

		if (inv == null)
			return null;

		try {
			DocumentWrapper ob = DocumentWrapper.createWithName(inv.getInvoiceTitle(), inv.getInvoiceType());

			// Setting attributes of the document wrapper object
			ob.setTitle(inv.getInvoiceTitle());
			ob.setDescription(inv.getInvoiceDescription());
			ob.setPrefix(inv.getPrefix());
			ob.setRepoPath(repoPath);

			ob.addAttribute("product_number", inv.getReferenceNumber());
			ob.addAttribute("customer_address", inv.getCustomerAddress());
			ob.addAttribute("customer_name", inv.getCustomerName());
			ob.addAttribute("delivery_date", inv.getDeliveryDate());
			ob.addAttribute("delivery_note", inv.getDeliveryNoteNo());
			ob.addAttribute("despatch_date", inv.getDespatchDate());
			ob.addAttribute("discoun", inv.getDiscount());
			ob.addAttribute("due_date", inv.getDueDate());
			ob.addAttribute("invoice_date", inv.getInvoiceDate());
			ob.addAttribute("invoice_number", inv.getInvoiceNumber());
			ob.addAttribute("email", inv.getEmail());
			ob.addAttribute("fax_number", inv.getFax());
			ob.addAttribute("net_invoice_total", inv.getGrossTotal());
			ob.addAttribute("reference_number", inv.getReferenceNumber());
			ob.addAttribute("supplier_address", inv.getSupplierAddress());
			ob.addAttribute("supplier_name", inv.getSupplierName());
			ob.addAttribute("telephone_number", inv.getTelephone());
			ob.addAttribute("total_amount", inv.getNetTotal());
			ob.addAttribute("total_due ", inv.getDue());
			ob.addAttribute("vat_reg_number", inv.getVatReg());
			ob.addAttribute("reg_number", inv.getVatReg());
			ob.addAttribute("vat_total", inv.getVatTotal());
			ob.addAttribute("despatch_note", inv.getDeliveryNoteNo());
			ob.addAttribute("account_number", inv.getAccountNo());
			ob.addAttribute("order_number", inv.getOrderNo());
			ob.addAttribute("customer_number", "inv.getGrossTotal()");
			ob.addAttribute("website", inv.getWebsite());
			ob.addAttribute("delivery_address", inv.getDeliveryAddress());
			ob.addAttribute("carriage_net", inv.getCarriageNet());
			ob.addAttribute("currency", inv.getCurrency());
			return ob;
		} catch (Exception e) {
			log.error("Failed to convert to Document Wrapper Object", e);
			return null;
		}
	}

	public Document getDocumentByPathOrId(String pathOrId) {
		Document ob = nuxeoClient.repository().fetchDocumentById(pathOrId);
		if (ob == null)
			ob = nuxeoClient.repository().fetchDocumentByPath(pathOrId);
		return ob;
	}
}
