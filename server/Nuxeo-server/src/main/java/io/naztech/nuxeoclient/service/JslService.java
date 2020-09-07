package io.naztech.nuxeoclient.service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
 * @author abul.kalam
 * @since 2020-07-28
 *
 */
@Service
public class JslService  implements PdfInvoiceProcessor{
	private static Logger log = LoggerFactory.getLogger(JslService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;
	
	private Invoice invoice = new Invoice();

	@Value("${import.jsl.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.jsl.type}")
	private String nuxeoinvoiceType;

	@Value("${import.jsl.prefix}")
	private String prefix;

	@Value("${import.nuxeo.jsl.description}")
	private String desc;

	@Value("${jsl.invoiceNo}")
	private String invoiceNo;

	@Value("${jsl.supplierName}")
	private String supplierName;

	@Value("${jsl.invoiceDte}")
	private String invoiceDte;

	@Value("${jsl.grossTotalC1}")
	private String grossTotalC1;

	@Value("${jsl.suplierAddress}")
	private String suplierAddress;

	@Value("${jsl.accountNo}")
	private String accountNo;

	@Value("${jsl.vatTotal}")
	private String vatTotal;

	@Value("${jsl.orderNo}")
	private String orderNo;

	@Value("${jsl.invoiceDes}")
	private String invoiceDes;

	@Value("${jsl.vatNo}")
	private String vatNo;

	@Value("${jsl.gross}")
	private String gross;

	@Value("${jsl.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${jsl.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${jsl.email}" )
	private String email;
 
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try { 

			invoice.setSortName(Constants.JSL);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(invoiceDes);
			String invoiceAddressA2 = invoiceAddressA1[0].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split("\\n");
			invoice.setSupplierName(invoiceAddressA3[0]);
			invoice.setSupplierAddress(invoiceAddressA2);
			// =============== VAT No ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ===============Ref No ================
			String[] referNoA1 = pdfStr.split(orderNo);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.trim().split("\\s+");
			String refNo = referNoA3[0].trim();
			invoice.setOrderNo(refNo.trim());
 
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(gross);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);

			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============Account Code ================
			String[] accountCodeA1 = pdfStr.split(accountNo);
			String accountCodeA2 = accountCodeA1[1].trim();
			String accountCodeA3[] = accountCodeA2.split("[a-zA-Z]");
			String accountCodeNo1 = accountCodeA3[0].trim();
			invoice.setAccountCode(accountCodeNo1);
			// ==============phone date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(supplierName);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0] .trim();
			invoice.setTelephone(phone1);

			/*
			 * Email
			 */
			String emailA1[] = pdfStr.split(email);
			String emailA2[] = emailA1[1] .split("\\n");
			invoice.setEmail(emailA2[1].replaceAll(suplierAddress, "").trim()); 
			invoice.setFax(emailA2[0].trim());
			// ===============Vat Total Address ================
			String[] vatTotalA1 = pdfStr.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// =============== Total Address ================
			String[] totalA1 = pdfStr.split(grossTotalC1);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			// ===============Quality Address ================

			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(vatNo);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String totalPrice = "", unitePrice = "", itemCode = "", description = "", quentity = "", unite = "",
						vat = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();

					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					unite = untPrice[1].replaceAll(invoiceTableA2, "$2");
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$3");
					vat = untPrice[1].replaceAll(invoiceTableA2, "$4");
					totalPrice = untPrice[1].replaceAll(invoiceTableA2, "$5");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoicetab.setUnit(unite);
				invoicetab.setStockCode(itemCode);
				invoicetab.setVatPercentage(vat);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = "";
				itemCode = "";
				description = "";
				quentity = "";
				unite = "";
				vat = "";
			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

	
		return null;
	}
}
