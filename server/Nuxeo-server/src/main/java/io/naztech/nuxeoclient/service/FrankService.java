
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
 * @since 2020-08-8
 *
 */
@Service
public class FrankService implements PdfInvoiceProcessor{
	
	private static Logger log = LoggerFactory.getLogger(FrankService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.frankhowerd}")
	private String folderName;

	@Value("${import.frankhowerd.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.frankhowerd.type}")
	private String nuxeoinvoiceType;

	@Value("${import.frankhowerd.prefix}")
	private String prefix;

	@Value("${import.nuxeo.frankhowerd.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${frank.invoiceNo}")
	private String invoiceNo;

	@Value("${frank.delevaryAddress}")
	private String delevaryAddress;

	@Value("${frank.currency}")
	private String currency;

	@Value("${frank.vatTotal}")
	private String vatTotal;

	@Value("${frank.email}")
	private String email;

	@Value("${frank.delevaryAddr}")
	private String delevaryAddr;

	@Value("${frank.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${frank.faxNo}")
	private String faxNo;

	@Value("${frank.vatNo}")
	private String vatNo;

	@Value("${frank.phone}")
	private String phone;

	@Value("${frank.netTotal}")
	private String netTotal;

	@Value("${frank.invoiceDte}")
	private String invoiceDte;

	@Value("${frank.grossTotalC1}")
	private String grossTotalC1;

	@Value("${frank.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${frank.supplierName}")
	private String supplierName;  
//	 
  
@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
	 
	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
String json=pdfStr;
	try {

		invoice.setSortName(Constants.FRANKHOWARD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = json.split(invoiceNo);
		String invoiceA2 = invoiceA1[1] .trim();
		String invoiceA3[] = invoiceA2.trim().split("[\\D]{3}[a-zA-Z]");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceNumber(invoice1.trim());
		// ============ INvoice Date =====================
		String[] dateA1 = json.split(invoiceDte);
		String dateA2 = dateA1[1].trim();
		String dateA3[] = dateA2.split("\\s+");
		invoice.setInvoiceDate(dateA3[0].trim()); 
		// ===============VAT No Address ================
		String[] vatNoA1 = json.split(vatNo);
		String vatNoA2 = vatNoA1[1].trim();
		String vatNoA3[] = vatNoA2.split("\\s+");
		String vatNo1 = vatNoA3[0].trim();
		invoice.setVatNo(vatNo1);
		// ==============Deleveru date===========
		String telephoneA1 = json.trim();
		String telephoneA2[] = telephoneA1.split(phone);
		String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
		String phone = telephoneA3[0].trim();
		invoice.setTelephone(phone);

		/*
		 * Email
		 */
		String emailA1[] = json.split(email);
		String emailA2[] = emailA1[1].split("\\s+");
		invoice.setEmail(emailA2[0].trim()); 
		/*
		 * Invoice Address
		 */// ===============Invoice Address ================
		String[] invoiceAddressA1 = json.split(delevaryAddr);
		String invoiceAddressA2 = invoiceAddressA1[1].trim();
		String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress); 
		String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
		String invoiceAddressSum = "", deleveryAddressSum = "";
		for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
			String sepretAddr[] = invoiceAddressA4[i].split("                                                 ");
			invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
			deleveryAddressSum += sepretAddr[1];
		} 
		String cName[] = invoiceAddressSum.split("         ");
		invoice.setInvoiceAddress(invoiceAddressSum);
		invoice.setCustomerAddress(deleveryAddressSum);
		invoice.setCustomerName(cName[0]);
		invoice.setDeliveryAddress(deleveryAddressSum);

		// ===============Net Total Address ================
		String[] netTotalA1 = json.split(netTotal);
		String netTotalA2 = netTotalA1[1];
		String netTotalA3[] = netTotalA2.trim().split("[a-zA-Z]");
		String netTotalA4 = netTotalA3[1].trim();
		invoice.setNetTotal(netTotalA4);
		// ===============NetInvoiceTotal ================
		String[] invoiceTotalA1 = json.split(grossTotalC1);
		String invoiceTotalA2 = invoiceTotalA1[1].trim();
		String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
		String invoiceTotalA4 = invoiceTotalA3[3].trim();
		invoice.setGrossTotal(invoiceTotalA4);
		/*
		 * Vat Total
		 */
		String vatTotalA1[] = json.split(vatTotal);
		String vatTotala2[] = vatTotalA1[1].split("[a-zA-Z]");
		invoice.setVatTotal(vatTotala2[0].trim());
		/*
		 * Table Data
		 */
		String[] quentityA1 = json.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String quentityA4[] = invTBLEa1.trim().split("\\n");
		String description = "";
		for (int i = 0; i < quentityA4.length; i++) {
			String  description1 = "",itemCode = "", quantity = "", price = "", unit = "", orderNo = "", lineValue = "";

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
				description1  = quentityA4[i].trim();

			} 

			invoicetab.setItemNo(itemCode.trim());
			invoicetab.setQuantity(quantity);
			invoicetab.setUnitPrice(unit);
			invoicetab.setUnit(price);
			invoicetab.setDiscount (orderNo);
			invoicetab.setNetAmount(lineValue);
			invoicetab.setDescription(description+ description1);
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
