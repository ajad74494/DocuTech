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
 * @since 2020-07-20
 *
 */
@Service
public class InTouchService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(InTouchService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.intouchwithbricks.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.intouchwithbricks.type}")
	private String nuxeoinvoiceType;

	@Value("${import.intouchwithbricks.prefix}")
	private String prefix;

	@Value("${import.nuxeo.intouchwithbricks.description}")
	private String desc;

	@Value("${inTouch.invoiceNo}")
	private String invoiceNo;

	@Value("${inTouch.invoiceDte}")
	private String invoiceDte;

	@Value("${inTouch.invoiceAddress}")
	private String invoiceAddress;

	@Value("${inTouch.referNo}")
	private String referNo;

	@Value("${inTouch.delevaryAddress}")
	private String delevaryAddress;

	@Value("${inTouch.delevaryAddr}")
	private String delevaryAddr;

	@Value("${inTouch.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${inTouch.accountNo}")
	private String accountNo;

	@Value("${inTouch.vatNo}")
	private String vatNo;

	@Value("${inTouch.phone}")
	private String phone;

	@Value("${inTouch.faxNo}")
	private String faxNo;

	@Value("${inTouch.netTotal}")
	private String netTotal;

	@Value("${inTouch.netTotalC1}")
	private String netTotalC1;

	@Value("${inTouch.vatTotal}")
	private String vatTotal;

	@Value("${inTouch.grossTotalC1}")
	private String grossTotalC1;

	@Value("${inTouch.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${inTouch.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${inTouch.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${inTouch.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${inTouch.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${inTouch.delevaryAddr6}")
	private String delevaryAddr6;

	@Value("${inTouch.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			invoice.setSortName(Constants.INTOUCHWITHBRICKS);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = pdfStr;
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceFirst(delevaryAddr1, "")
					.replaceFirst(delevaryAddr2, "").trim().replaceFirst(delevaryAddr3, "").trim()
					.replaceFirst(delevaryAddr4, "").trim().replaceFirst(delevaryAddr5, "").trim()
					.replaceFirst(delevaryAddr6, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
			String invoiceAddress = invoiceAddressA3[0].trim();
			invoice.setCustomerAddress(invoiceAddress);
			String costumernameA1[] = invoiceAddress.split("\r\n");
			invoice.setCustomerName(costumernameA1[0]);
			invoice.setDeliveryAddress(invoiceAddress);
			invoice.setInvoiceAddress(
					delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5 + delevaryAddr6);

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);

			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone = telephoneA3[0].trim();
			invoice.setDeliveryDate(phone);
			// ==============Telephone no ===========
			String faxA1 = json.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("[a-zA-Z]");
			String fax = faxA3[0].trim();
			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = json.split(delevaryAddress);
			String suplierAddrA1[] = suplierAddr[1].split(vatNo);
			String supAdd = suplierAddrA1[0].trim().replaceFirst("\\w*", "").trim().replaceAll(faxNo + fax, "").trim()
					.replaceAll(delevaryAddr + "\\d*-\\d*-\\d*", "");
invoice.setSupplierAddress(supAdd);
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("\\n");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = json.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = json.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4);
			// ============order number==========
			String[] ordernumberA1 = json.split(referNo);
			String ordernumberA2 = ordernumberA1[1].trim();
			String ordernumberA3[] = ordernumberA2.split(netTotalC1);
			String ordernumber = ordernumberA3[0].trim();
			invoice.setOrderNo(ordernumber);
			// ===============Quality Address ================

			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				InvoiceTable invoicetab = new InvoiceTable();

				String totalPrice = "", unitePrice = "", quantity = "", description = "", descount = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					quantity = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(invoiceTableA21[0]);
					description = des[1].replaceAll("%", "");
					String untPrice[] = quentityA4[i].split(description);
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$1");

					descount = untPrice[1].replaceAll(invoiceTableA2, "$2");

					totalPrice = untPrice[1].replaceAll(invoiceTableA2, "$3");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quantity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoicetab.setDiscount(descount);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = "";
				quantity = "";
				description = "";
				descount = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return invoice;
	}

}
