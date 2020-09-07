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
public class KiteService implements PdfInvoiceProcessor{
	private static Logger log = LoggerFactory.getLogger(KiteService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;
	
	private Invoice invoice = new Invoice();

	@Value("${import.kite.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.kite.type}")
	private String nuxeoinvoiceType;

	@Value("${import.kite.prefix}")
	private String prefix;

	@Value("${import.nuxeo.kite.description}")
	private String desc;

	@Value("${kite.supplierName}")
	private String supplierName;

	@Value("${kite.invoiceDte}")
	private String invoiceDte;

	@Value("${kite.orderNo}")
	private String orderNo;

	@Value("${kite.orderNo1}")
	private String orderNo1;

	@Value("${kite.invoiceNo}")
	private String invoiceNo;

	@Value("${kite.delevaryAddress}")
	private String delevaryAddress;

	@Value("${kite.delevaryAddr}")
	private String delevaryAddr;

	@Value("${kite.accountNo}")
	private String accountNo;

	@Value("${kite.netTotal}")
	private String netTotal;

	@Value("${kite.grossTotalC1}")
	private String grossTotalC1;

	@Value("${kite.onDate}")
	private String onDate;

	@Value("${kite.invoiceDes}")
	private String invoiceDes;

	@Value("${kite.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${kite.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${kite.faxNo}")
	private String faxNo; 

	@Value("${kite.delevaryAddr1}")
	private String delevaryAddr1; 

	@Value("${kite.delevaryAddr3}")
	private String delevaryAddr3; 
	
	@Value("${kite.delevaryAddr4}")
	private String delevaryAddr4; 
 
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			invoice.setSortName(Constants.KITEPACKAGING);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = pdfStr;
			String suplierAddredd[] = json.split(supplierName);
			String suplierName[] = suplierAddredd[0].replaceAll(invoiceNo, "").trim().split("\\n");
			String supAddress = suplierAddredd[0].replaceAll(invoiceNo, "").replaceAll("\\w*\\/\\w*", "").trim();
			invoice.setSupplierAddress(supAddress);
			invoice.setSupplierName(suplierName[0].trim());
			String inv[] = json.split("Tel"); 
			// ============ INvoice =====================
			String invoice1 = inv[0].replaceAll("\\w*([\\w\\/\\w*]*)", "$1").trim();
			invoice.setInvoiceNumber("OP" + invoice1);

			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\n");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ============ Order Date =====================
			String[] OrderA1 = json.split(orderNo);
			String OrderA2 = OrderA1[1].trim();
			String OrderA3[] = OrderA2.split("\\n");
			String Order = OrderA3[0].trim();
			invoice.setOrderNo(Order);
			// ============ Order Date =====================
			String[] OrderA11 = json.split(orderNo1);
			String OrderA21 = OrderA11[1].trim();
			String OrderA31[] = OrderA21.split("\\n");
			String Order1 = OrderA31[0].trim();
			// ============ Order Date =====================
			String[] customerA1 = json.split(faxNo);
			String customerA2 = customerA1[1].trim();
			String customerA3[] = customerA2.split("\\n");
			String customer = customerA3[0].trim();

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(delevaryAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceAll(orderNo + Order, "")
					.replaceAll(orderNo1 + Order1, "").replaceAll(faxNo + customer, "").replaceAll(delevaryAddr4, "")
					.replaceAll(delevaryAddr1, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddr);
			String invoiceAddress = invoiceAddressA3[0].trim();

			invoice.setDeliveryAddress(invoiceAddress);
			invoice.setCustomerName(invoiceDes);
			invoice.setCustomerAddress(invoiceAddress);

			/*
			 * 
			 * Telephone
			 * 
			 */
			String phoneA1[] = json.split(supplierName);
			String phone[] = phoneA1[1].trim().split("\\n");
			String telephone[] = phone[0].trim().split("[a-zA-Z]");
			invoice.setTelephone(telephone[0].trim());
			/*
			 * 
			 * Email
			 * 
			 */
			invoice.setEmail(phone[3].trim());

			// =============== VAT No ================
			String[] vatNoA1 = json.split(delevaryAddr3);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{3}[a-zA-Z]");
			String vatNo = vatNoA3[0].trim();
			invoice.setVatNo(vatNo);
			// ===============Ref No ================
			String[] referNoA1 = json.split(delevaryAddr4);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("[a-zA-Z]");
			String refNo = referNoA3[0].trim();
			invoice.setRegNo(refNo.trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("£", "").trim();
			String netTotalA3[] = netTotalA2.split("\\s+");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			invoice.setVatTotal(netTotalA3[1]);
			// =============== Total Address ================
			String[] totalA1 = json.split(grossTotalC1);
			String totalA2 = totalA1[1].replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("£", "").trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			// ===============Quality Address ================

			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(onDate);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				InvoiceTable invoicetab = new InvoiceTable();

				String price = "", quentity = "", parsentince = "", nettTotal = "", vattotal = "", description = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					description = invoiceTableA1[0].replaceAll("[(]", "");
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
					price = untPrice[1].replaceAll(invoiceTableA2, "$1");
					quentity = untPrice[1].replaceAll(invoiceTableA2, "$2");
					parsentince = untPrice[1].replaceAll(invoiceTableA2, "$3");
					nettTotal = untPrice[1].replaceAll(invoiceTableA2, "$4");
					vattotal = untPrice[1].replaceAll(invoiceTableA2, "$5");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {

					String extraA1[] = quentityA4[i].split("                                  ");
					price += extraA1[1].trim();
					description += extraA1[0].trim();
				} 
				invoicetab.setQuantity(quentity);
				invoicetab.setPrice(price);
				invoicetab.setDiscount(parsentince);
				invoicetab.setNetAmount(nettTotal);
				invoicetab.setVatPercentage(vattotal);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

				price = "";
				quentity = "";
				parsentince = "";
				nettTotal = "";
				vattotal = "";
				description = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

			return invoice;
	} 

}
