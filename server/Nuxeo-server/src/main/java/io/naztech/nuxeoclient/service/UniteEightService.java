package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author mazhar.alam
 * @since 2020-07-20
 *
 */
@Service
public class UniteEightService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(UniteEightService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.uniteight.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.uniteight.type}")
	private String nuxeoinvoiceType;

	@Value("${import.uniteight.prefix}")
	private String prefix;

	@Value("${import.nuxeo.uniteight.description}")
	private String desc;

	@Value("${unit.supplierName}")
	private String supplierName;

	@Value("${unit.invoiceNo}")
	private String invoiceNo;

	@Value("${unit.invoiceDte}")
	private String invoiceDte;

	@Value("${unit.grossTotalC1}")
	private String grossTotalC1;

	@Value("${unit.suplierAddress}")
	private String suplierAddress;

	@Value("${unit.accountNo}")
	private String accountNo;

	@Value("${unit.web}")
	private String web;

	@Value("${unit.vatTotal}")
	private String vatTotal;

	@Value("${unit.orderNo}")
	private String orderNo;

	@Value("${unit.invoiceDes}")
	private String invoiceDes;

	@Value("${unit.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${unit.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${unit.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${unit.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${unit.gross}")
	private String gross;

	@Value("${unit.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${unit.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${unit.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${unit.invoiceTableA3}")
	private String invoiceTableA3;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			String json = pdfStr;

			invoice.setSortName(Constants.UNIT8LVORYSLTD);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceDes);
			String invoiceAddressA2 = invoiceAddressA1[0].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(web);
			invoice.setSupplierName(invoiceAddressA3[0]);
			invoice.setSupplierAddress(invoiceAddressA2);
			invoice.setCustomerName(delevaryAddr1);
			invoice.setCustomerAddress(delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5);

			// =============== VAT No ================
			String[] vatNoA1 = json.split(orderNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
			String vatNo = vatNoA3[0].trim();
			invoice.setVatNo(vatNo.trim());
			// ===============Ref No ================
			String[] referNoA1 = json.split(grossTotalC1);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("[---]");
			String refNo = referNoA3[0].trim();
			invoice.setRegNo(refNo.trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(gross);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// =============== Total Address ================
			String[] totalA1 = json.split(suplierAddress);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);

			String[] due = json.split(accountNo);
			String due1 = due[1].trim();
			String duetotalA[] = due1.split("[a-zA-Z]");
			String duetotalA1 = duetotalA[0].trim();
			String duetotal = duetotalA1;
			invoice.setDue(duetotal);
			// ===============Quality Address ================
			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(invoiceTableA2);
			String quentityA4[] = quentityA2.split(invoiceTableA3);
			for (int i = 0; i < quentityA3.length - 1; i++) {
				InvoiceTable invoicetab = new InvoiceTable();
				String des = quentityA4[0].trim();
				String des1 = quentityA3[0].trim().replaceAll(des, "").replaceAll("ADVISORIES", "");
				invoicetab.setDescription(des);
				invoicetab.setTotal(des1);
				invoiceTable.add(invoicetab);
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
		return invoice;
	}

}
