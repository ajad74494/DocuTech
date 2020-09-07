
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
public class MirkaService implements PdfInvoiceProcessor {
	 
	private static Logger log = LoggerFactory.getLogger(MirkaService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.mirka}")
	private String folderName;

	@Value("${import.mirka.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.mirka.type}")
	private String nuxeoinvoiceType;

	@Value("${import.mirka.prefix}")
	private String prefix;

	@Value("${import.nuxeo.mirka.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${mirka.invoiceNo}")
	private String invoiceNo;

	@Value("${mirka.supplierName}")
	private String supplierName;

	@Value("${mirka.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${mirka.componyNo}")
	private String componyNo;

	@Value("${mirka.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${mirka.delevaryAddr}")
	private String delevaryAddr;

	@Value("${mirka.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${mirka.email}")
	private String email;

	@Value("${mirka.vatNo}")
	private String vatNo;

	@Value("${mirka.phone}")
	private String phone;

	@Value("${mirka.currency}")
	private String currency; 

	@Value("${mirka.vatTotal}")
	private String vatTotal; 

	@Value("${mirka.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${mirka.faxNo}")
	private String faxNo; 

	@Value("${mirka.invoiceTableA2}")
	private String invoiceTableA2;  
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			invoice.setSortName(Constants.MIRKA);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);

			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[1].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			invoice.setInvoiceDate(invoiceA3[0].trim());
// invoice.setCustomerNo( invoiceA3[2].trim()); 
			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split(vatNo);
			String phone = telephoneA3[0].trim();
			invoice.setTelephone(phone);
			/*
			 * Fax
			 */
			String faxA1[] = pdfStr.split(faxNo);
			String faxA2[] = faxA1[1].split("[a-zA-z]");
			invoice.setFax(faxA2[0].trim());
			/*
			 * Fax
			 */
			String emailA1[] = pdfStr.split(email);
			String emailA2[] = emailA1[1].split("[a-zA-z]");
//			invoice.setEmail(emailA2[0].trim());
//			invoice.setCompantNo(emailA2[0].trim())
 
			/*
			 * SupplierAddress //
			 */
			 invoice.setSupplierAddress(supplierName);
			invoice.setSupplierName(componyNo); 
			// =============== Vat Total ================
			String[] carriageA1 = pdfStr.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.replaceAll(",", "").replaceAll("-", "").trim();
			invoice.setVatTotal(carriage);
			int vt = Integer.parseInt(carriage.trim());
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
			String invoiceTotalA4 = invoiceTotalA3[0].replaceAll(",", "").trim();
			invoice.setGrossTotal(invoiceTotalA4);
			int gtotal = Integer.parseInt(invoiceTotalA4.trim());
			int inv=   gtotal-vt; 
			invoice.setNetTotal(Integer.toString(inv));
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1];
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddr1);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i].split("                                    ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
				deleveryAddressSum += sepretAddr[1];
			}
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(delevaryAddr2);
			invoice.setDeliveryAddress(deleveryAddressSum); 
			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(currency);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				String description = "", itemCode = "", quantity = "", price = "", gross = "" ;
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) { 
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[1];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quantity = untPrice[1].replaceAll(invoiceTableA2, "$3");
					price = untPrice[1].replaceAll(invoiceTableA2, "$1");
					gross = untPrice[1].replaceAll(invoiceTableA2, "$4"); 
				} 
				System.out.println(quantity);
				invoicetab.setItemNo(itemCode);
				invoicetab.setQuantity(quantity);
				invoicetab.setPrice(price);
				invoicetab.setTotalNetPrice(gross); 
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

			}
			invoice.setInvoiceTable(invoiceTable);
 
		} catch (Exception e) {
			log.info(e.getMessage());
		}

	
		return invoice;
	}  

  

}
