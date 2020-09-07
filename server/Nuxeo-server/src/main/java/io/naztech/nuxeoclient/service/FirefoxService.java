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
 * @since 2020-08-12
 *
 */
@Service
public class FirefoxService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(FirefoxService.class);

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

	@Value("${fire.invoiceNo}")
	private String invoiceNo;

	@Value("${fire.currency}")
	private String currency;

	@Value("${fire.vatTotal}")
	private String vatTotal;

	@Value("${fire.email}")
	private String email;

	@Value("${fire.delevaryAddress}")
	private String delevaryAddress;

	@Value("${fire.orderNo}")
	private String orderNo;

	@Value("${fire.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${fire.faxNo}")
	private String faxNo; 

	@Value("${fire.vatNo}")
	private String vatNo; 

	@Value("${fire.phone}")
	private String phone; 

	@Value("${fire.netTotal}")
	private String netTotal; 

	@Value("${fire.invoiceDte}")
	private String invoiceDte; 

	@Value("${fire.grossTotalC1}")
	private String grossTotalC1;   

	@Value("${fire.invoiceTableA2}")
	private String invoiceTableA2;    
//	

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
//			invoice.setSortName(Constants.BIFFA);
//			invoice.setInvoiceTitle(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.trim().split("\\n");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] dateA1 = pdfStr.split(invoiceDte);
			String dateA2 = dateA1[1].trim();
			String dateA3[] = dateA2.split("\\s+");
			invoice.setInvoiceDate(dateA3[0].trim());
			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("\\s+");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			/*
			 * Fax
			 */
			String faxA1[] = pdfStr.split(faxNo);
			String faxNo1[] = faxA1[1].split("\\n");
			invoice.setDueDate(faxNo1[0].trim()); 

			/*
			 * Invoice Address
			 */// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(dateA3[0].trim());
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i]
						.split("                                                         ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
			}
			invoice.setCustomerAddress(invoiceAddressSum);
			invoice.setCustomerName(invoiceAddressA4[0]);
			invoice.setDeliveryAddress(invoiceAddressSum);

			// ==============InvoiceAddress ===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.replaceAll("£\\d*\\.\\d*", "").split(phone);
			String telephoneA3[] = telephoneA2[1].split(email);
			String phone = telephoneA3[0].trim();
			invoice.setInvoiceAddress(phone);
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1];
			String netTotalA3[] = netTotalA2.trim().split("\\n");
			String netTotalA4 = netTotalA3[1].trim();
			invoice.setNetTotal(netTotalA4);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("\\n");
			String invoiceTotalA4 = invoiceTotalA3[3].trim();
			invoice.setGrossTotal(invoiceTotalA4);
			/*
			 * Vat Total
			 */
			String vatTotalA1[] = pdfStr.replaceAll("[(]*", "").replaceAll("[)]*", "").split(vatTotal);
			String vatTotala2[] = vatTotalA1[1].split("\\n");
			invoice.setVatTotal(vatTotala2[0].trim());
			/*
			 * Vat Total
			 */
			String due1TotalA1[] = pdfStr.split(orderNo);
			String dueTotala2[] = due1TotalA1[1].split("\\n");
			invoice.setDue(dueTotala2[0].trim());
			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			String description = "";
			for (int i = 0; i < quentityA4.length; i++) {
				String description1 = "", itemCode = "", quantity = "", price = "" ,lineValue = "";

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des = invoiceTableA1[0].replaceFirst(itemCode, "").trim();
					description = des.replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
					quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					price = untPrice[1].replaceAll(invoiceTableA2, "$2");
					lineValue = untPrice[1].replaceAll(invoiceTableA2, "$3");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description1 = quentityA4[i].trim();

				} 
				invoicetab.setItemNo(itemCode.trim());
				invoicetab.setQuantity(quantity);
				invoicetab.setPrice(price);
				invoicetab.setNetAmount(lineValue);
				invoicetab.setDescription(description + description1);
				invoiceTable.add(invoicetab);
				description = "";

			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage());
		}

	
		return invoice;
	}
 

}
