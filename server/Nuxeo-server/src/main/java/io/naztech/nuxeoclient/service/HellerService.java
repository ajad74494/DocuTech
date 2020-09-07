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
 * @author abul.kalam
 * @since 2020-07-19
 *
 */
@Service
public class HellerService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(CollegeTyreService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${heller.supplierName}")
	private String supplierName;

	@Value("${heller.netTotal}")
	private String netTotal;

	@Value("${heller.invoiceNo}")
	private String invoiceNo;

	@Value("${heller.invoiceNo1}")
	private String v;

	@Value("${heller.invoiceDte}")
	private String invoiceDte;

	@Value("${heller.email}")
	private String email;

	@Value("${heller.referNo}")
	private String referNo;

	@Value("${heller.delevaryAddress}")
	private String delevaryAddress;

	@Value("${heller.delevaryAddr}")
	private String delevaryAddr;

	@Value("${heller.accountNo}")
	private String accountNo;

	@Value("${heller.phone}")
	private String phone;

	@Value("${heller.invoiceNo1}")
	private String invoiceNo1;

	@Value("${heller.vatTotal}")
	private String vatTotal;

	@Value("${heller.grossTotalC1}")
	private String grossTotalC1;

	@Value("${heller.orderNo}")
	private String orderNo;

	@Value("${heller.onDate}")
	private String onDate;

	@Value("${heller.invoiceDes}")
	private String invoiceDes;

	@Value("${heller.gross}")
	private String gross;

	@Value("${heller.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${heller.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

//
//			invoice.setSortName(Constants.HELLERTOOLSGMBH);
//			invoice.setInvoiceTitel(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			String json = pdfStr;
			String suplierAddredd[] = json.split(supplierName);
			String suplierName[] = suplierAddredd[0].replaceAll(invoiceNo, "").trim().split("\\s+");
			invoice.setSupplierAddress(suplierAddredd[0].replaceAll(invoiceNo, "").trim());

			invoice.setSupplierName(suplierName[0].trim() + suplierName[1].trim());
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(supplierName);
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

			/*
			 * 
			 * order No
			 * 
			 */
			String orderNoA1[] = json.split(orderNo);
			String orderNoA2[] = orderNoA1[1].trim().split("[a-zA-Z]");
			invoice.setOrderNo(orderNoA2[0].trim());
			/*
			 * 
			 * ReferNo
			 * 
			 */
			String rferNoA1[] = json.split(invoiceNo1);
			String rferNoA2[] = rferNoA1[1].trim().split("\\n");
			invoice.setReferenceNumber(rferNoA2[0].trim());
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(supplierName);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
			String invoiceAddress = invoiceAddressA3[0].trim().replaceAll(invoice1, "");
			String invoiceAddressA4 = invoiceAddress.trim().replaceAll(invoiceDes, "");
			String invoiceAddressA5 = invoiceAddressA4.trim();
			String consomerAddress[] = invoiceAddressA5.trim().split("\\n");
			invoice.setCustomerAddress(invoiceAddressA5);
			invoice.setCustomerName(consomerAddress[0].trim());
			// =============== VAT No ================
			String[] deleveryDateA1 = json.split(delevaryAddr);
			String deleveryDateA2 = deleveryDateA1[1].trim();
			String deleveryDateA3[] = deleveryDateA2.split("[a-zA-Z]");
			String deleveryDate = deleveryDateA3[0].trim();
			invoice.setDeliveryDate(deleveryDate);
			// ===============Ref No ================
			String[] deleveryNoteA1 = json.split(delevaryAddr);
			String deleveryNoteA2 = deleveryNoteA1[1].trim();
			String deleveryNoteA3[] = deleveryNoteA2.split("[a-zA-Z]");
			String deleveryNote = deleveryNoteA3[0].trim();
			invoice.setDeliveryNoteNo(deleveryNote);

			/*
			 * 
			 * Telephone
			 * 
			 */
			String phoneA1[] = json.split(phone);
			String phoneA2[] = phoneA1[1].split("[a-zA-Z]");
			invoice.setTelephone(phoneA2[0].trim());

			/*
			 * 
			 * Email
			 * 
			 */

			String emailA1[] = json.split(email);
			String emailA2[] = emailA1[1].split("\\n");
			invoice.setEmail(emailA2[0].trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);

			// ==========Delevary Address =================

			String[] delevaryAddressA1 = json.split(delevaryAddress);
			String delevaryAddressA2 = delevaryAddressA1[1].trim();
			String delevaryAddressA3[] = delevaryAddressA2.split(referNo);
			String delevaryAddressA4 = delevaryAddressA3[0].trim();
			invoice.setDeliveryAddress(delevaryAddressA4);
			// =============== Total Address ================
			String[] totalA1 = json.split(grossTotalC1);
			String totalA2 = totalA1[1].trim();
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

				String totalPrice = "", unitePrice = "", quantity = "", description = "", lne = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA2[] = invoiceTableA1[0].trim().split("\\s+");
					/*
					 * lne
					 */
					lne = invoiceTableA2[0].trim();
					/*
					 * Description
					 */
					String descrtptionA1[] = invoiceTableA1[0].trim().split(lne);
					description = descrtptionA1[1].trim();
					/*
					 * Quantity
					 */
					String quantityA1[] = quentityA4[i].split(description);
					quantity = quantityA1[1].replaceAll(gross, "$1").trim();
					/*
					 * unitePrice
					 */
					unitePrice = quantityA1[1].replaceAll(gross, "$2").trim();
					/*
					 * totalPrice
					 */
					totalPrice = quantityA1[1].replaceAll(gross, "$3").trim();
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim() + " ;";
				}
				invoicetab.setStockCode(lne);
				invoicetab.setQuantity(quantity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = "";
				quantity = "";
				description = "";
				lne = "";
			}
			invoice.setInvoiceTable(invoiceTable);
			// invoice.toString();

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
		return invoice;
	}

}
