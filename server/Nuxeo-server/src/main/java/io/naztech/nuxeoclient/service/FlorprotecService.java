package io.naztech.nuxeoclient.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
 * @since 2020-07-19
 *
 */
@Service
public class FlorprotecService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(FlorprotecService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();
 
	@Value("${import.onsite.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.onsite.type}")
	private String nuxeoinvoiceType;

	@Value("${import.onsite.prefix}")
	private String prefix;

	@Value("${import.nuxeo.onsite.description}")
	private String desc;
 

	private String invoiceNo = getProperticesValues("florprotec.invoiceNo");

	private String invoiceDte = getProperticesValues("florprotec.invoiceDte");

	private String invoiceDes = getProperticesValues("florprotec.invoiceDes");

	private String delevaryAddr1 = getProperticesValues("florprotec.delevaryAddr1");

	private String delevaryAddr2 = getProperticesValues("florprotec.delevaryAddr2");
 
//	FLORPROTEC
	private String delevaryAddr = getProperticesValues("florprotec.delevaryAddr");

	private String invoiceTableA1 = getProperticesValues("florprotec.invoiceTableA1");

	private String accountNo = getProperticesValues("florprotec.accountNo");

	private String vatNo = getProperticesValues("florprotec.vatNo");

	private String phone = getProperticesValues("florprotec.phone");
 

	private String netTotal = getProperticesValues("florprotec.netTotal"); 

	private String vatTotal = getProperticesValues("florprotec.vatTotal");

	private String grossTotalC1 = getProperticesValues("florprotec.grossTotalC1");

	private String faxNo = getProperticesValues("florprotec.faxNo");
 

	private String invoiceTableA2 = getProperticesValues("florprotec.invoiceTableA2");

	public String getProperticesValues(String key) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("./src/main/resources/invoice.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";

	}

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			invoice.setSortName("FLORP");
			invoice.setInvoiceTitle("florprotec");
			invoice.setInvoiceDescription("Invoice");
			invoice.setPrefix("florprotec");
			invoice.setInvoiceType("florprotec:");
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);

			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
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
			 * SupplierAddress
			 */
			String suplierAddr[] = pdfStr.split(phone);
			String suplierAddrA1[] = suplierAddr[0].replaceAll(invoiceDes, "").trim().split("\\n");
			String supAdd = suplierAddrA1[0].trim();
			invoice.setSupplierAddress(suplierAddr[0].trim().replaceAll(invoiceDes, "").trim());
			invoice.setSupplierName(supAdd);
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
			invoice.setGrossTotal(netTotalA4);

			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceAll(invoiceNo + invoice1, "")
					.replaceAll(delevaryAddr1, "").replaceAll(accountNo + accountNo1, "")
					.replaceAll(invoiceDte + invoiceDate1, "");
			String invoiceAddressA3[] = invoiceAddressA2.split(vatNo);

			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i].split("                             ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
				deleveryAddressSum += sepretAddr[1];
			} 
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(delevaryAddr2);
			invoice.setDeliveryAddress(deleveryAddressSum);

			// ===============Quality Address ================

			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].trim();
			String customerRef="",order="",quality="",design="",qty="",unit="",price="",goods="",vat="",iND="";
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) { 
					customerRef = quentityA4[i].replaceAll(invoiceTableA2, "$1");
					order = quentityA4[i].replaceAll(invoiceTableA2, "$2");
					quality = quentityA4[i].replaceAll(invoiceTableA2, "$3");
					design = quentityA4[i].replaceAll(invoiceTableA2, "$4");
					qty = quentityA4[i].replaceAll(invoiceTableA2, "$5");
					unit = quentityA4[i].replaceAll(invoiceTableA2, "$6");
					price = quentityA4[i].replaceAll(invoiceTableA2, "$7");
					goods = quentityA4[i].replaceAll(invoiceTableA2, "$8");
					vat = quentityA4[i].replaceAll(invoiceTableA2, "$9");
					iND = quentityA4[i].replaceAll(invoiceTableA2, "$10"); 
				} 
//				invoicetab.setr
				invoicetab.setOrderNo(order); 
//				invoicetab.digine 
				invoicetab.setQuantity(qty);
				invoicetab.setUnit(unit);
				invoicetab.setPrice(price); 
				invoicetab.setNetAmount(goods);
				invoicetab.setVatPercentage(vat);; 
//				invoicetab.idn
				invoiceTable.add(invoicetab);

			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

	return invoice;
	}

}
