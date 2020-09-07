
package io.naztech.nuxeoclient.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;

/**
 * 
 * @author muhammad.tarek
 * @since 2020-08-12
 *
 */
@Service
public class BiraService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(BiraService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.biffa}")
	private String folderName;

	@Value("${import.biffa.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.biffa.type}")
	private String nuxeoinvoiceType;

	@Value("${import.biffa.prefix}")
	private String prefix;

	@Value("${import.nuxeo.biffa.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${bira.invoiceNo}")
	private String invoiceNo;

	@Value("${bira.invoiceDte}")
	private String invoiceDte;

	@Value("${bira.customerNo}")
	private String customerNo;

	@Value("${bira.referNo}")
	private String referNo;

	@Value("${bira.currency}")
	private String currency;

	@Value("${bira.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${bira.accountNo}")
	private String accountNo;

	@Value("${bira.vatNo}")
	private String vatNo;

	@Value("${bira.phone}")
	private String phone;

	@Value("${bira.netTotal}")
	private String netTotal;

	@Value("${bira.vatTotal}")
	private String vatTotal;

	@Value("${bira.grossTotalC1}")
	private String grossTotalC1;

	@Value("${bira.faxNo}")
	private String faxNo;

	@Value("${bira.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		try {
//
			invoice.setSortName("bira");
			invoice.setInvoiceTitle("bira");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("bira:");
			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\n");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\n");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);
			/*
			 * customerNo
			 */
			String cNo[] = pdfStr.split(customerNo);
			String costumerNo[] = cNo[1].trim().split("\\n");
			invoice.setDeliveryDate(costumerNo[0].trim());

			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = pdfStr.split(referNo);
			String suplierAddrA1[] = suplierAddr[0].trim().split("\\n");
			String supAdd = suplierAddrA1[0].trim();
			invoice.setSupplierAddress(suplierAddr[0].trim());
			invoice.setSupplierName(supAdd);
			invoice.setInvoiceAddress(suplierAddr[0].trim());
			invoice.setCustomerAddress(suplierAddr[0].trim());
			invoice.setCustomerName(supAdd);
			invoice.setDeliveryAddress(suplierAddr[0].trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = pdfStr.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);

			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4); 
		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
