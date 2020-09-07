
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
 * @since 2020-07-29
 *
 */
@Service
public class TubewayService implements PdfInvoiceProcessor {
	
	private static Logger log = LoggerFactory.getLogger(TubewayService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.tubeway}")
	private String folderName;

	@Value("${import.tubeway.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.tubeway.type}")
	private String nuxeoinvoiceType;

	@Value("${import.tubeway.prefix}")
	private String prefix;

	@Value("${import.nuxeo.tubeway.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${tubeway.invoiceNo}")
	private String invoiceNo; 

	@Value("${tubeway.currency}")
	private String currency; 

	@Value("${tubeway.vatTotal}")
	private String vatTotal; 

	@Value("${tubeway.email}")
	private String email; 

	@Value("${tubeway.delevaryAddr4}")
	private String delevaryAddr4; 

	@Value("${tubeway.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${tubeway.componyNo}")
	private String componyNo; 

	@Value("${tubeway.vatNo}")
	private String vatNo; 

	@Value("${tubeway.phone}")
	private String phone; 

	@Value("${tubeway.netTotal}")
	private String netTotal; 

	@Value("${tubeway.invoiceDte}")
	private String invoiceDte; 

	@Value("${tubeway.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${tubeway.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${tubeway.orderNo}")
	private String orderNo; 

	@Value("${tubeway.delevaryAddr2}")
	private String delevaryAddr2; 

	@Value("${tubeway.faxNo}")
	private String faxNo;       
 
@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
	 
	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

	try {
//		invoice.setSortName(Constants.);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String invoiceA2 = invoiceA1[1].trim();
		String invoiceA3[] = invoiceA2.trim().split("[a-zA-Z]");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceNumber(invoice1.trim());
		// ============ INvoice Date =====================
		String[] dateA1 = pdfStr.split(invoiceDte);
		String dateA2 = dateA1[1].trim();
		String dateA3[] = dateA2.split("\\s+");
		invoice.setInvoiceDate(dateA3[0].trim());
		/*
		 * Order Number
		 */
		String orderA1[] = pdfStr.split(orderNo);
		String orderA2[] = orderA1[1].split("\\s+");
		invoice.setOrderNo(orderA2[2].trim());
		invoice.setAccountNo(orderA2[0].trim());
		invoice.setReferenceNumber(orderA2[1].trim());

		// ===============VAT No Address ================
		String[] vatNoA1 = pdfStr.split(vatNo);
		String vatNoA2 = vatNoA1[1].trim();
		String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
		String vatNo1 = vatNoA3[0].trim();
		invoice.setVatNo(vatNo1);
		// ===============Company No Address ================
		String[] companyA1 = pdfStr.split(componyNo);
		String companyA2 = companyA1[1].trim();
		String companyA3[] = companyA2.split("[a-zA-Z]");
		String company = companyA3[0].trim();
		invoice.setVatNo(company);
		// ==============Deleveru date===========
		String telephoneA1 = pdfStr.trim();
		String telephoneA2[] = telephoneA1.split(phone);
		String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
		String phone = telephoneA3[0].trim();
		invoice.setTelephone(phone);

		/*
		 * fax
		 */
		String faxA1[] = pdfStr.split(faxNo);
		String faxA2[] = faxA1[1].split("[a-zA-Z]");
		invoice.setFax(faxA2[0].trim());
		/*
		 * Email
		 */
		String emailA1[] = pdfStr.split(email);
		String emailA2[] = emailA1[1].split("\\s+");
		invoice.setFax(emailA2[0].trim());

		// ===============Invoice Address ================
		String[] invoiceAddressA1 = pdfStr.split(phone);
		String invoiceAddressA2 = invoiceAddressA1[0].trim();
		String invoiceAddressA3[] = invoiceAddressA2.trim().split("\\n");
		invoice.setSupplierAddress(invoiceAddressA2);
		invoice.setSupplierName(invoiceAddressA3[0].trim());

		// ===============Net Total ================
		String[] netTotalA1 = pdfStr.split(netTotal);
		String netTotalA2 = netTotalA1[1];
		String netTotalA3[] = netTotalA2.trim().split("[a-zA-Z]");
		String netTotalA4 = netTotalA3[0].trim();
		invoice.setNetTotal(netTotalA4);
		// ===============vat Total ================
		String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
		String invoiceTotalA2 = invoiceTotalA1[1].trim();
		String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
		String invoiceTotalA4 = invoiceTotalA3[3].trim();
		invoice.setGrossTotal(invoiceTotalA4);
		/*
		 * Vat Total
		 */
		String vatTotalA1[] = pdfStr.split(vatTotal);
		String vatTotala2[] = vatTotalA1[1].split("[a-zA-Z]");
		invoice.setVatTotal(vatTotala2[0].trim());

		// ===============Invoice Address ================
		String[] deleveryA1 = pdfStr.split(delevaryAddr4);
		String deleveryA2 = deleveryA1[1];
		String deleveryA3[] = deleveryA2.split(delevaryAddr2);
		String deleveryA4[] = deleveryA3[0].split("\\n");
		String invoiceAddressSum = "", deleveryAddressSum = "";
		for (int i = 0; i < deleveryA4.length - 1; i++) {
			String sepretAddr[] = deleveryA4[i].split("                                       ");
			invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
			deleveryAddressSum += sepretAddr[1];
		}
		System.out.println(invoiceAddressSum);
		invoice.setInvoiceAddress(invoiceAddressSum);
		invoice.setSupplierAddress( deleveryAddressSum);
		invoice.setSupplierName(delevaryAddr4);

		/*
		 * Table Data
		 */
		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String quentityA4[] = invTBLEa1.trim().split("\\n");
		for (int i = 0; i < quentityA4.length; i++) {
			String description1 = "", quantity = "", unitePrice = "",   itemcode = "", nettotal = "", unite = "",discount = "",
					description = "";
			InvoiceTable invoicetab = new InvoiceTable();
			Pattern pattern = Pattern.compile(invoiceTableA2);
			if (pattern.matcher(quentityA4[i].trim()).find()) {
				String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
				String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
				itemcode = invoiceTableA21[0];
				String des[] = invoiceTableA1[0].split(itemcode);
				description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
				String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

				quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				unite = untPrice[1].replaceAll(invoiceTableA2, "$2");
				unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$3");
				discount = untPrice[1].replaceAll(invoiceTableA2, "$4");
				nettotal = untPrice[1].replaceAll(invoiceTableA2, "$5");
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description1 = quentityA4[i].trim();

			} 
			invoicetab.setQuantity(quantity.trim());
			invoicetab.setUnit(unite.trim());
			invoicetab.setUnitPrice(unitePrice.trim()); 
			invoicetab.setVatAmount(discount.trim());
			invoicetab.setTotal(nettotal.trim());
			invoicetab.setDescription(description.trim() + " " + description1.trim());
			invoiceTable.add(invoicetab );

		}
		invoice.setInvoiceTable(invoiceTable);
	} catch (Exception e) {
		log.info(e.getMessage());
	}


	return invoice;
}

}
