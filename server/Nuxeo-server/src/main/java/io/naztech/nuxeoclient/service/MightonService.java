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
public class MightonService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(MightonService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.mighton.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.mighton.type}")
	private String nuxeoinvoiceType;

	@Value("${import.mighton.prefix}")
	private String prefix;

	@Value("${import.nuxeo.mighton.description}")
	private String desc;

	@Value("${mighton.supplierName}")
	private String supplierName;

	@Value("${mighton.invoiceNo}")
	private String invoiceNo;

	@Value("${mighton.delevaryAddress}")
	private String delevaryAddress;

	@Value("${mighton.delevaryAddr}")
	private String delevaryAddr;

	@Value("${mighton.accountNo}")
	private String accountNo;

	@Value("${mighton.netTotal}")
	private String netTotal;

	@Value("${mighton.vatTotal}")
	private String vatTotal;

	@Value("${mighton.grossTotalC1}")
	private String grossTotalC1;

	@Value("${mighton.onDate}")
	private String onDate;

	@Value("${mighton.invoiceDes}")
	private String invoiceDes;

	@Value("${mighton.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${mighton.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${mighton.delevaryAddr1}")
	private String delevaryAddr1; 

	@Value("${mighton.delevaryAddr2}")
	private String delevaryAddr2; 

	@Value("${mighton.delevaryAddr3}")
	private String delevaryAddr3; 

	@Value("${mighton.delevaryAddr4}")
	private String delevaryAddr4; 

	@Value("${mighton.delevaryAddr5}")
	private String delevaryAddr5;  
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {  
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		invoice.setSortName(Constants.MIGHTON);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		try {
			String json = pdfStr;
			String suplierAddredd[] = json.split(supplierName);
			String suplierName[] = suplierAddredd[0].trim().split("\\n");
			String supAddress = suplierAddredd[0].replaceAll("01223 497097", "").replaceAll("01223 839896", "")
					.replaceAll("sales@mighton.co.uk", "").trim();
			invoice.setSupplierAddress(supAddress);
			invoice.setSupplierName(suplierName[0].trim());
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[3].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String invoiceDate = invoiceA3[4].trim();
			invoice.setInvoiceDate(invoiceDate);

			/*
			 * 
			 * order No
			 * 
			 */
			invoice.setOrderNo(invoiceA3[5].trim());
			/*
			 * 
			 * ReferNo
			 * 
			 */
			invoice.setReferenceNumber(invoiceA3[2].trim());
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(delevaryAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceAll(delevaryAddr1, "").replaceAll(delevaryAddr2, "")
					.replaceAll(delevaryAddr3, "").replaceAll(delevaryAddr4, "").replaceAll(delevaryAddr5, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddr);
			String invoiceAddress = invoiceAddressA3[0].trim();
			String invoiceAddressA4 = invoiceAddress.trim().replaceAll(invoiceDes, "");
			String invoiceAddressA5 = invoiceAddressA4.trim();
			invoice.setDeliveryAddress(invoiceAddressA5);
			invoice.setCustomerName(delevaryAddr1);
			invoice.setCustomerAddress(delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5);

			/*
			 * 
			 * Telephone
			 * 
			 */
			String phoneA1[] = json.split(supAddress);
			String phoneA2[] = phoneA1[1].split(supplierName);
			String phone[] = phoneA2[0].trim().split("\\n");
			invoice.setTelephone(phone[0].trim());
			/*
			 * Fax Number
			 */
			invoice.setFax(phone[1].trim());
			/*
			 * 
			 * Email
			 * 
			 */
			invoice.setEmail(phone[2].trim());

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

				String totalPrice = "", unitePrice = "", quantity = "", description = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA2[] = invoiceTableA1[0].trim().split("\\s+");
					invoicetab.setStockCode(invoiceTableA2[0]);
					String des[] = invoiceTableA1[0].split(invoiceTableA2[0]);
					description = des[1].replaceAll("[(]", "");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				} 
				invoicetab.setQuantity(quantity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = "";
				quantity = "";
				description = ""; 
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
return invoice;
	}


}
