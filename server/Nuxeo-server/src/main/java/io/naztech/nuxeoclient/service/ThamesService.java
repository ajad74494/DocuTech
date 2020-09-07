
package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author abul.kalam
 * @since 2020-08-8
 *
 */
public class ThamesService implements PdfInvoiceProcessor { 
	private static Logger log = LoggerFactory.getLogger(ThamesService.class);

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

	@Value("${thames.invoiceNo}")
	private String invoiceNo; 

	@Value("${thames.supplierName}")
	private String supplierName; 

	@Value("${thames.invoiceNo1}")
	private String invoiceNo1; 

	@Value("${thames.invoiceDte}")
	private String invoiceDte; 

	@Value("${thames.email}")
	private String email; 

	@Value("${thames.accountNo}")
	private String accountNop; 

	@Value("${thames.dueDte}")
	private String dueDte; 

	@Value("${thames.vatNo}")
	private String vatNo; 

	@Value("${thames.netTotal}")
	private String netTotal; 

	@Value("${thames.vatTotal}")
	private String vatTotal; 

	@Value("${thames.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${thames.orderNo}")
	private String orderNo; 

	@Value("${thames.onDate}")
	private String onDate; 

	@Value("${thames.invoiceDte1}")
	private String invoiceDte1; 
	
	@Value("${thames.dueDte}")
	private String dueDtep; 

	@Value("${thames.web}")
	private String web;

	@Value("${thames.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${thames.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${thames.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${thames.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${thames.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${thames.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${thames.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		String string = pdfStr; 
		try {
			List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
//			invoice.setSortName(Constants.THAMESVALLY);
//			invoice.setInvoiceTitle(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = string;
			String suplierAddredd[] = json.split(supplierName);
			String suplierName[] = suplierAddredd[0].replaceAll(invoiceNo, "").trim().split("\\s+");
			invoice.setSupplierAddress(suplierAddredd[0].replaceAll(invoiceNo, "").trim());

			invoice.setSupplierName(suplierName[0].trim() + suplierName[1].trim());
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
			String refNo = rferNoA2[0];
			// ===============Account No ================
			String[] accountA1 = json.split(accountNop);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("\\n");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============DeleveryDate No ================
			String[] delevaryDateA1 = json.split(dueDte);
			String delevaryDateA2 = delevaryDateA1[1].trim();
			String delevaryDateA3[] = delevaryDateA2.split("\\n");
			String delevaryDate = delevaryDateA3[0].trim();
			invoice.setDeliveryDate(delevaryDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceDte1);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(invoiceTableA1);
			String invoiceAddress = invoiceAddressA3[0].trim().replaceAll(invoiceNo + invoice1, "");
			String invoiceAddressA4 = invoiceAddress.trim().replaceAll(invoiceDte + invoiceDate, "")
					.replaceAll(orderNo + orderNoA2[0].trim(), "").replaceAll(accountNop + accountA3[0].trim(), "");
			String invoiceAddressA5 = invoiceAddressA4.trim().replaceAll(invoiceNo1 + refNo, "").replaceAll(dueDtep, "")
					.replaceAll(delevaryDateA3[0].trim(), "");
			String consomerAddress[] = invoiceAddressA5.trim().split("\\n");
			invoice.setCustomerAddress(invoiceAddressA5);
			invoice.setCustomerName(consomerAddress[0].trim());
			invoice.setDeliveryAddress(delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5);

			// =============== VAT No ================
			String[] vatA1 = json.split(vatNo);
			String vatA2 = vatA1[1].trim();
			String vatA3[] = vatA2.split("[a-zA-Z]");
			String vatReg = vatA3[0].trim();
			invoice.setVatReg(vatReg);
			// ===============Ref No ================

			/*
			 * 
			 * Telephone
			 * 
			 */
			String phoneA1[] = json.split(supplierName);
			String phoneA2[] = phoneA1[1].split("[a-zA-Z]");
			invoice.setTelephone(phoneA2[0].trim());
			/*
			 * 
			 * Fax
			 * 
			 */
			String faxA1[] = json.split(supplierName);
			String faxA2[] = faxA1[1].split("[a-zA-Z]");
			invoice.setFax(faxA2[0].trim());

			/*
			 * 
			 * Email
			 * 
			 */

			String emailA1[] = json.split(email);
			String emailA2[] = emailA1[1].split("\\n");
			invoice.setEmail(emailA2[0].trim());
			/*
			 * Website
			 */
			String webA1[] = json.split(web);
			String webA2[] = webA1[1].split("\\n");
			invoice.setWebsite (webA2[0].trim()); 
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

			invoice.setDeliveryAddress(invoiceAddressA5);

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

				String totalPrice = "", descountAmount = "", unitePrice = "", quantity = "", description = "",
						partNo = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					/*
					 * lne
					 */
					quantity = invoiceTableA21[0].trim();
					/*
					 * Description
					 */
					String descrtptionA1[] = invoiceTableA1[0].trim().split(quantity);
					description = descrtptionA1[1].trim();

					/*
					 * Quantity
					 */
					String quantityA1[] = quentityA4[i].split(description);
					partNo = quantityA1[1].replaceAll(invoiceTableA2, "$1").trim();

					/*
					 * unitePrice
					 */
					unitePrice = quantityA1[1].replaceAll(invoiceTableA2, "$2").trim();
					/*
					 * descountAmount
					 */
					descountAmount = quantityA1[1].replaceAll(invoiceTableA2, "$3").trim();
					/*
					 * totalPrice
					 */
					totalPrice = quantityA1[1].replaceAll(invoiceTableA2, "$4").trim();
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim() + " ;";
				}
				invoicetab.setQuantity(quantity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoicetab.setDiscount(descountAmount);
				invoicetab.setStockCode(partNo);
				invoiceTable.add(invoicetab);
				partNo = "";
				totalPrice = "";
				unitePrice = "";
				quantity = "";
				description = "";
				descountAmount = "";

				System.out.println(invoicetab.toString());
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

	
		return null;
	}
  

}
