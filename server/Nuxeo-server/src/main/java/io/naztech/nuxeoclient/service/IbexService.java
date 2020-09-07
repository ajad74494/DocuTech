
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
 * @since 2020-07-31
 *
 */
@Service
public class IbexService implements PdfInvoiceProcessor {
	
	private static Logger log = LoggerFactory.getLogger(IbexService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.ibexmarina}")
	private String folderName;

	@Value("${import.ibexmarina.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.ibexmarina.type}")
	private String nuxeoinvoiceType;

	@Value("${import.ibexmarina.prefix}")
	private String prefix;

	@Value("${import.nuxeo.ibexmarina.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${ibex.invoiceNo}")
	private String invoiceNo; 

	@Value("${ibex.currency}")
	private String currency; 

	@Value("${ibex.vatTotal}")
	private String vatTotal; 

	@Value("${ibex.email}")
	private String email; 

	@Value("${ibex.delevaryAddress}")
	private String delevaryAddress; 

	@Value("${ibex.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${ibex.faxNo}")
	private String faxNo; 

	@Value("${ibex.vatNo}")
	private String vatNo; 

	@Value("${ibex.phone}")
	private String phone; 

	@Value("${ibex.invoiceDte}")
	private String invoiceDte; 

	@Value("${ibex.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${ibex.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${ibex.supplierName}")
	private String supplierName;   
 public void getXml(String pdfStr, File file) throws Exception {}
@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

	try {
		invoice.setSortName(Constants.IBEXMARINROPESLTD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String invoiceA2 = invoiceA1[1].replaceAll(supplierName, "").trim();
		String invoiceA3[] = invoiceA2.trim().split("\\s+");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceNumber(invoice1.trim());
		// ============ INvoice Date =====================
		String[] dateA1 = pdfStr.split(invoiceDte);
		String dateA2 = dateA1[1].trim();
		String dateA3[] = dateA2.split("\\s+");
		invoice.setInvoiceDate(dateA3[5].trim());
		invoice.setReferenceNumber(dateA3[0].trim() + dateA3[1].trim());
		invoice.setAccountNo(dateA3[2].trim());
		// ===============VAT No Address ================
		String[] vatNoA1 = pdfStr.split(vatNo);
		String vatNoA2 = vatNoA1[1].trim();
		String vatNoA3[] = vatNoA2.split("           ");
		String vatNo1 = vatNoA3[0].trim();
		invoice.setVatNo(vatNo1);
		// ==============Deleveru date===========
		String telephoneA1 = pdfStr.trim();
		String telephoneA2[] = telephoneA1.split(phone);
		String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
		String phone = telephoneA3[0].trim();
		invoice.setTelephone(phone);
		/*
		 * Fax
		 */
		String faxA1[] = pdfStr.split(faxNo);
		String faxNo1[] = faxA1[1].split("[a-zA-Z]");
		invoice.setFax(faxNo1[0].trim());
		/*
		 * Email
		 */
		String emailA1[] = pdfStr.split(email);
		String emailA2[] = emailA1[1].split("\\n");
		invoice.setEmail(emailA2[0].trim());
		/*
		 * Invoice Address
		 */
		// ===============Invoice Address ================
		String[] invoiceAddressA1 = pdfStr.split(emailA2[0].trim());
		String invoiceAddressA2 = invoiceAddressA1[1].trim();
		String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);

		String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
		String invoiceAddressSum = "", deleveryAddressSum = "";
		for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
			String sepretAddr[] = invoiceAddressA4[i].split("                                         ");
			invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
			deleveryAddressSum += sepretAddr[1];
		}
		String cName[] = invoiceAddressSum.split("         ");
		invoice.setInvoiceAddress(invoiceAddressSum);
		invoice.setCustomerAddress(deleveryAddressSum);
		invoice.setCustomerName(cName[0]);
		invoice.setDeliveryAddress(deleveryAddressSum);

		// ===============Net Total Address ================
		String[] netTotalA1 = pdfStr.split(vatNo1);
		String netTotalA2 = netTotalA1[1];
		String netTotalA3[] = netTotalA2.trim().split("\\s+");
		String netTotalA4 = netTotalA3[1].trim();
		invoice.setNetTotal(netTotalA4);
		// ===============NetInvoiceTotal ================
		String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
		String invoiceTotalA2 = invoiceTotalA1[1].trim();
		String invoiceTotalA3[] = invoiceTotalA2.split("\\s+");
		String invoiceTotalA4 = invoiceTotalA3[3].trim();
		invoice.setGrossTotal(invoiceTotalA4);
		/*
		 * Vat Total
		 */
		String vatTotalA1[] = pdfStr.split(vatTotal);
		String vatTotala2[] = vatTotalA1[1].split("\\n");
		invoice.setVatTotal(vatTotala2[0].trim());
		/*
		 * Table Data
		 */
		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String quentityA4[] = invTBLEa1.split("\\n");
		String description = "";
		for (int i = 0; i < quentityA4.length; i++) {
			String itemCode = "", quantity = "", price = "", unit = "", orderNo = "", lineValue = "";

			InvoiceTable invoicetab = new InvoiceTable();
			Pattern pattern = Pattern.compile(invoiceTableA2);
			if (pattern.matcher(quentityA4[i].trim()).find()) {
				String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
				String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
				itemCode = invoiceTableA21[0];
				String des[] = invoiceTableA1[0].split(itemCode);
				description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
				String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

				quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				unit = untPrice[1].replaceAll(invoiceTableA2, "$2");
				price = untPrice[1].replaceAll(invoiceTableA2, "$3");
				orderNo = untPrice[1].replaceAll(invoiceTableA2, "$4");
				lineValue = untPrice[1].replaceAll(invoiceTableA2, "$5");
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description += quentityA4[i].trim();
			}
			invoicetab.setItemNo(itemCode);
			invoicetab.setQuantity(quantity);
			invoicetab.setUnit(unit);
			invoicetab.setPrice(price);
			invoicetab.setOrderNo(orderNo);
			invoicetab.setNetAmount(lineValue);
			invoicetab.setDescription(description);
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
