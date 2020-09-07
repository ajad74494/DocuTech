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
public class ADAService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(ADAService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;
	
	private Invoice invoice = new Invoice();

	@Value("${import.adafastfix.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.adafastfix.type}")
	private String nuxeoinvoiceType;

	@Value("${import.adafastfix.prefix}")
	private String prefix;

	@Value("${import.nuxeo.adafastfix.description}")
	private String desc;

	@Value("${ada.supplierName}")
	private String supplierName;

	@Value("${ada.phone}")
	private String phone;

	@Value("${ada.invoiceNo}")
	private String invoiceNo;

	@Value("${ada.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${ada.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${ada.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${ada.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${ada.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${ada.grossTotalC1}")
	private String grossTotalC1;
	 
	@Value("${ada.accountNo}")
	private String accountNo; 
	 
	@Value("${ada.invoiceAddress}")
	private String invoiceAddress; 
	 
	@Value("${ada.telephone}")
	private String telephone; 
	 
	@Value("${ada.componyNo}")
	private String componyNo; 
	 
	@Value("${ada.netTotal}")
	private String netTotal; 
	 
	@Value("${ada.vatNo}")
	private String vatNo; 
	 
	@Value("${ada.invoiceTableA1}")
	private String invoiceTableA1; 
	 
	@Value("${ada.invoiceTableA2}")
	private String invoiceTableA2; 
	 
	@Value("${ada.web}")
	private String web; 

	@Value("${ada.faxNo}")
	private String faxNo;  

	@Value("${ada.companyNo}")
	private String companyNo;  
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {

			invoice.setSortName(Constants.ADAFASTFIX);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(invoiceTableA1);
			String invoiceA4[] = invoiceA3[0].split("\\s+");
			String invoice1 = invoiceA4[8].trim();
			invoice.setInvoiceNumber(invoice1);
			invoice.setInvoiceDate(invoiceA4[6].trim() + invoiceA4[7].trim());
			invoice.setReferenceNumber(invoiceA4[5].trim());
			invoice.setOrderNo(invoiceA4[2].trim());
			invoice.setDeliveryDate(invoiceA4[3].trim() + invoiceA4[4].trim());
			// ===============Invoice Address ================
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(componyNo);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.replaceFirst(delevaryAddr1, "").replaceFirst(delevaryAddr2, "")
					.replaceFirst(delevaryAddr3, "").replaceFirst(delevaryAddr4, "").replaceFirst(delevaryAddr5, "")
					.split(web);
			invoice.setSupplierName(invoiceAddressA3[0]);
			invoice.setSupplierAddress(invoiceAddressA2);
			invoice.setCustomerName(delevaryAddr1);
			// =============== VAT No ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ===============Ref No ================
			String[] referNoA1 = pdfStr.split(grossTotalC1);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("[-]");
			String refNo = referNoA3[0].trim();
			invoice.setRegNo(refNo.trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("\\s+");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			invoice.setVatTotal(netTotalA3[1].trim());
			// =============== Total Address ================
			invoice.setGrossTotal(netTotalA3[2].trim());
			String[] due = pdfStr.split(accountNo);
			String due1 = due[1].trim();
			String duetotalA[] = due1.split("[a-zA-Z]");
			String duetotalA1 = duetotalA[0].trim();
			String duetotal = duetotalA1;
			invoice.setAccountNo(duetotal);
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0].replaceAll("[(]", "").replaceAll("[)]", "").trim();
			invoice.setTelephone(phone);
			String emailA1[] = pdfStr.split(telephone);
			String webA1[] = emailA1[1].trim().split(invoiceAddress);
			String webSite = webA1[0].replaceAll("[(]", "").replaceAll("[)]", "").replaceAll(phone, "").trim();
			invoice.setEmail(webSite);
			// ==============Telephone no ===========
			String faxA1 = pdfStr.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("[a-zA-Z]");
			String fax1 = faxA3[0].trim();
			invoice.setFax(fax1);
			invoice.setCustomerAddress(delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5);
			/*
			 * SuplierAddress 
			 * SuplierNAme
			 */
			String sAddress[] = pdfStr.replaceAll(companyNo, "").split(telephone);
			String supAddress = sAddress[0].replaceAll(vatNo + vatNo1, "").replaceAll(phone + phone1, "")
					.replaceAll(faxNo + fax1, "").trim();
			String sName[] = supAddress.split("\\n");
			invoice.setSupplierAddress(supAddress);
			invoice.setSupplierName(sName[0].trim());
			// ===============Quality Address ================
			String[] quentityA1 = pdfStr.replaceAll("[(]*", "").replaceAll("[)]*", "").replaceAll("[']*", "")
					.split(invoiceTableA1);

			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(" The  title  to  the goods");
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length-1; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String totalPrice = "", unit = "", stockCode = "", description = "", quentity = "",value = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					stockCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(invoiceTableA21[0]);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].split(description);
					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1").trim();
					totalPrice = untPrice[1].replaceAll(invoiceTableA2, "$2");
					unit = untPrice[1].replaceAll(invoiceTableA2, "$3");
					value = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}
 
				invoicetab.setStockCode(stockCode);
				invoicetab.setUnit (unit);
				invoicetab.setTotal(value);
				invoicetab.setDescription(description);
				invoicetab.setQuantity(quentity);
				invoicetab.setTotalNetPrice (totalPrice);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unit = "";
				stockCode = "";
				description = "";
				quentity = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

	
		return invoice;
	}
}
