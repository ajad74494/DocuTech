
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

import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author mazhar.alam
 * @since 2020-08-16
 *
 */
@Service
public class EssexService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(EssexService.class);

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

	@Value("${essea.invoiceNo}")
	private String invoiceNo;

	@Value("${essea.currency}")
	private String currency;

	@Value("${essea.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${essea.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${essea.delevaryAddress}")
	private String delevaryAddress;

	@Value("${essea.delevaryAddr}")
	private String delevaryAddr;

	@Value("${essea.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${essea.invoiceAddress}")
	private String invoiceAddress;

	@Value("${essea.faxNo}")
	private String faxNo;

	@Value("${essea.vatNo}")
	private String vatNo; 

	@Value("${essea.phone}")
	private String phone; 

	@Value("${essea.netTotal}")
	private String netTotal; 

	@Value("${essea.invoiceDte}")
	private String invoiceDte; 

	@Value("${essea.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${essea.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${essea.email}")
	private String email; 
	  
//		 
	

@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
	try {

		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String inv[]=invoiceA1[1].trim().split("                                                  ");
		String invoiceA3[] = inv[1].split("\\s+");
		invoice.setInvoiceNumber(invoiceA3[1].trim());
		invoice.setInvoiceDate(invoiceA3[2].trim()); 
		// ===============VAT No Address ================
		String[] vatNoA1 = pdfStr.split(vatNo);
		String vatNoA2 = vatNoA1[1].trim();
		String vatNoA3[] = vatNoA2.split("\\n");
		String vatNo1 = vatNoA3[0].trim();
		invoice.setVatNo(vatNo1);
		// ==============Deleveru date===========
		String telephoneA1 = pdfStr.trim();
		String telephoneA2[] = telephoneA1.split(phone);
		String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
		String phone1 = telephoneA3[0].trim();
		invoice.setTelephone(phone1);
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
		 * SupplierAddress
		 */// ===============Invoice Address ================
		String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
		String invoiceAddressA2 = invoiceAddressA1[1].trim();
		String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);

		String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
		String invoiceAddressSum = "";
		for (int i = 0; i < invoiceAddressA4.length ; i++) {
			String sepretAddr[] = invoiceAddressA4[i].split("                                   ");
			invoiceAddressSum = invoiceAddressSum + sepretAddr[0]; 
		}
		 String strA1[]=invoiceAddressSum.split(delevaryAddr2);
		invoice.setInvoiceAddress(strA1[0]);
		invoice.setCustomerAddress(strA1[1]);
		invoice.setCustomerName(invoiceAddress);
		invoice.setDeliveryAddress(strA1[1]);

		// ===============Net Total Address ================
		String[] netTotalA1 = pdfStr.split(netTotal);
		String netTotalA2 = netTotalA1[1].trim();
		String netTotalA3[] = netTotalA2.split("\\n");
		String netTotalA4 = netTotalA3[0].replaceAll(grossTotalC1, "$5").trim();
		invoice.setNetTotal(netTotalA4);
		// ===============NetInvoiceTotal ================
		invoice.setGrossTotal(netTotalA3[0].replaceAll(grossTotalC1, "$7").trim());
		invoice.setVatTotal(netTotalA3[0].replaceAll(grossTotalC1, "$6").trim());
//
		/*
		 * SuplierAddress
		 */
		invoice.setSupplierName(delevaryAddr1);
		invoice.setSupplierAddress(delevaryAddr1+" "+invoiceDte);
		/*
		 * Table Data
		 */
		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String description = "",  price = "", gross = "",  vAT = "";
		String quentityA4[] = invTBLEa1.split("\\n");
		for (int i = 0; i < quentityA4.length; i++) {

			InvoiceTable invoicetab = new InvoiceTable();
			Pattern pattern = Pattern.compile(invoiceTableA2);
			if (pattern.matcher(quentityA4[i].trim()).find()) {
				String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
				String des = invoiceTableA1[0].trim();
				description = des.trim();
				String untPrice[] = quentityA4[i].split(description);
				price = untPrice[1].replaceAll(invoiceTableA2, "$1");
				gross = untPrice[1].replaceAll(invoiceTableA2, "$2");
				 vAT = untPrice[1].replaceAll(invoiceTableA2, "$3");
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description += quentityA4[i].trim();
			}
			invoicetab.setPrice(price);
			invoicetab.setNetAmount(gross);
			invoicetab.setVatAmount(vAT);
			invoicetab.setDescription(description);

		}
		invoice.setInvoiceTable(invoiceTable);

	} catch (Exception e) {
		log.info(e.getMessage());
	}


	return invoice;
}

}
