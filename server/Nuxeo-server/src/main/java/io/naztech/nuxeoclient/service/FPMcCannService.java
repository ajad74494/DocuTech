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
public class FPMcCannService implements PdfInvoiceProcessor{
	private static Logger log = LoggerFactory.getLogger(FPMcCannService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;
	
	private Invoice invoice = new Invoice();

	@Value("${import.fpmccann.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.fpmccann.type}")
	private String nuxeoinvoiceType;

	@Value("${import.fpmccann.prefix}")
	private String prefix;

	@Value("${import.nuxeo.fpmccann.description}")
	private String desc;

	@Value("${fm.invoiceNo}")
	private String invoiceNo;

	@Value("${fm.invoiceDte}")
	private String invoiceDte;

	@Value("${fm.invoiceAddress}")
	private String invoiceAddress;

	@Value("${fm.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${fm.referNo}")
	private String referNo;

	@Value("${fm.accountNo}")
	private String accountNo;

	@Value("${fm.vatNo}")
	private String vatNo;

	@Value("${fm.phone}")
	private String phone;

	@Value("${fm.netTotal}")
	private String netTotal;

	@Value("${fm.vatTotal}")
	private String vatTotal;

	@Value("${fm.grossTotalC1}")
	private String grossTotalC1;

	@Value("${fm.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${fm.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${fm.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${fm.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${fm.delevaryAddr5}")
	private String delevaryAddr5; 

	@Value("${fm.delevaryAddr6}")
	private String delevaryAddr6; 

	@Value("${fm.invoiceTableA2}")
	private String invoiceTableA2;  
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {

			invoice.setSortName(Constants.FPMCCANN);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			String suplierAddredd[] = pdfStr.split(phone);
			String suplierName[] = suplierAddredd[0].trim().split("\\n");
			invoice.setSupplierAddress(suplierAddredd[0].trim());
			invoice.setSupplierName(suplierName[0].trim());
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\n");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\n");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[\\D]{3}[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(invoiceAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceFirst(invoiceNo + invoice1, "")
					.replaceFirst(invoiceDte + invoiceDate, "").trim().replaceFirst(accountNo + accountNo1, "").trim()
					.replaceFirst(delevaryAddr4, "").trim().replaceFirst(delevaryAddr5, "").trim().trim();
			String invoiceAddressA3[] = invoiceAddressA2.replaceAll(delevaryAddr3, "").split(referNo);
			String invoiceAddress = invoiceAddressA3[0].trim();
			invoice.setCustomerAddress(invoiceAddress.replaceFirst(delevaryAddr6, ""));
			String costumernameA1[] = invoiceAddress.split("\\r\\n");
			invoice.setCustomerName(costumernameA1[0]);
			invoice.setDeliveryAddress(invoiceAddress.replaceFirst(delevaryAddr6, ""));
			invoice.setInvoiceAddress(
					delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5 + delevaryAddr6);

			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(suplierAddredd[0].trim());
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0].replaceAll("\\|", "").trim();
			invoice.setTelephone(phone1);

			/*
			 * Email
			 */
			String emailA1[] = pdfStr.split(phone1);
			String emailA2[] = emailA1[1].replaceAll("\\|", "").split("\\n");
			invoice.setEmail(emailA2[0].trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("\\n");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = pdfStr.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("\\n");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("\\n");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4);
			// ===============Quality Address ================

			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String costumwerOrderNo = "", deliveryRef = "", unitePrice = "", docketNo = "", totalPrice = "", description = "",
						quentity = "",suplierDate="";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					suplierDate = invoiceTableA21[0];
					docketNo = invoiceTableA21[1];

					String des[] = invoiceTableA1[0].split(docketNo);
					String descriptionA1 = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String desA1[] = descriptionA1.split("    ");
					description = desA1[0].trim();
					deliveryRef = desA1[1].trim();
					String[] cOrderNo = invoiceTableA1[0].split(deliveryRef);
					costumwerOrderNo=cOrderNo[1].trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(invoiceTableA1[0]);
					totalPrice = untPrice[1].replaceAll("", "").replaceAll(invoiceTableA2, "$3");

					System.out.println(unitePrice);
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$2");

					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quentity);
				invoicetab.setTotalNetPrice(totalPrice); 
				invoicetab.setDescription(description);
				invoicetab.setUnit(unitePrice);
				invoicetab.setOrderNo(costumwerOrderNo);
				invoicetab.setDate(suplierDate);
				invoiceTable.add(invoicetab);
 
				unitePrice = "";
				unitePrice = "";
				description = "";
				quentity = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

			return invoice;
	}
}
