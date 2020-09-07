package io.naztech.nuxeoclient.service;

import java.io.File;

import io.naztech.nuxeoclient.model.Invoice;

public interface PdfInvoiceProcessorPdfBox {
	
	public Invoice processPdfInvoice(String pdfStr,String pdfBoxStr, File pdfInvoice);
}
