package io.naztech.nuxeoclient.service;

import java.io.File;

import io.naztech.nuxeoclient.model.Invoice;

public interface PdfInvoiceProcessor {

	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice);
}
