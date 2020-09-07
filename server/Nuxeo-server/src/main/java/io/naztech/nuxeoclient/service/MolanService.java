
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
 * @since 2020-07-27
 *
 */
@Service
public class MolanService implements PdfInvoiceProcessor {
	
	private static Logger log = LoggerFactory.getLogger(MolanService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.molan}")
	private String folderName;

	@Value("${import.molan.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.molan.type}")
	private String nuxeoinvoiceType;

	@Value("${import.molan.prefix}")
	private String prefix;

	@Value("${import.nuxeo.molan.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${molan.invoiceNo}")
	private String invoiceNo;

	@Value("${molan.currency}")
	private String currency;

	@Value("${molan.invoiceAddress}")
	private String invoiceAddress;

	@Value("${molan.email}")
	private String email;

	@Value("${molan.delevaryAddress}")
	private String delevaryAddress;

	@Value("${molan.delevaryAddr}")
	private String delevaryAddr;

	@Value("${molan.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${molan.faxNo}")
	private String faxNo;

	@Value("${molan.vatNo}")
	private String vatNo;

	@Value("${molan.phone}")
	private String phone;

	@Value("${molan.netTotal}")
	private String netTotal; 

	@Value("${molan.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${molan.invoiceTableA2}")
	private String invoiceTableA2;    

@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

	try {
		invoice.setSortName(Constants.MOLANLTD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String invoiceA2 = invoiceA1[1].trim();
		String invoiceA3[] = invoiceA2.split("\\s+");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceNumber(invoice1.trim());
		invoice.setInvoiceDate(invoiceA3[1].trim());
		invoice.setReferenceNumber(invoiceA3[5].trim() + invoiceA3[6].trim());
		invoice.setAccountNo(invoiceA3[2].trim());
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
		 * SupplierAddress
		 */// ===============Invoice Address ================
		String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
		String invoiceAddressA2 = invoiceAddressA1[1].trim();
		String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);

		String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
		String invoiceAddressSum = "", deleveryAddressSum = "";
		for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
			String sepretAddr[] = invoiceAddressA4[i].split("                        ");
			invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
			deleveryAddressSum += sepretAddr[1];
		}
		invoice.setInvoiceAddress(invoiceAddressSum);
		invoice.setCustomerAddress(deleveryAddressSum);
		invoice.setCustomerName(invoiceAddress);
		invoice.setDeliveryAddress(deleveryAddressSum);

		// ===============Net Total Address ================
		String[] netTotalA1 = pdfStr.split(netTotal);
		String netTotalA2 = netTotalA1[1].trim();
		String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
		String netTotalA4 = netTotalA3[0].trim();
		invoice.setNetTotal(netTotalA4);
		// ===============NetInvoiceTotal ================
		String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
		String invoiceTotalA2 = invoiceTotalA1[1].trim();
		String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
		String invoiceTotalA4 = invoiceTotalA3[0].trim();
		invoice.setGrossTotal(invoiceTotalA4);

		/*
		 * Table Data
		 */
		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String description = "", itemCode = "", quantity = "", price = "", gross = "", adj = "", net = "", vAT = "";
		String quentityA4[] = invTBLEa1.split("\\n");
		for (int i = 0; i < quentityA4.length; i++) {

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
				price = untPrice[1].replaceAll(invoiceTableA2, "$2");
				gross = untPrice[1].replaceAll(invoiceTableA2, "$3");
				adj = untPrice[1].replaceAll(invoiceTableA2, "$4");
				net = untPrice[1].replaceAll(invoiceTableA2, "$5");
				vAT = untPrice[1].replaceAll(invoiceTableA2, "$6");
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description += quentityA4[i].trim();
			}
			invoicetab.setItemNo(itemCode);
			invoicetab.setQuantity(quantity);
			invoicetab.setPrice(price);
			invoicetab.setTotalNetPrice(gross);
//			invoicetab.adj
			invoicetab.setNetAmount(net);
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
