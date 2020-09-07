
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
 * @since 2020-08-12
 *
 */
@Service
public class DebenhamsService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(DebenhamsService.class);
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

	@Value("${debenhams.invoiceNo}")
	private String invoiceNo;

	@Value("${debenhams.invoiceDte}")
	private String invoiceDte;

	@Value("${debenhams.customerNo}")
	private String customerNo;

	@Value("${debenhams.referNo}")
	private String referNo;

	@Value("${debenhams.currency}")
	private String currency;

	@Value("${debenhams.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${debenhams.accountNo}")
	private String accountNo;

	@Value("${debenhams.vatNo}")
	private String vatNo;

	@Value("${debenhams.phone}")
	private String phone;

	@Value("${debenhams.vatTotal}")
	private String vatTotal;

	@Value("${debenhams.grossTotalC1}")
	private String grossTotalC1;

	@Value("${debenhams.faxNo}")
	private String faxNo; 

	@Value("${debenhams.invoiceTableA2}")
	private String invoiceTableA2; 
	
	@Value("${debenhams.netTotal}")
	private String netTotal;   
//	 
 
	 
	public void getXml(String pdfStr, File file) throws Exception {}
@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>(); 
	try {

//		invoice.setSortName(Constants.BIFFA);
//		invoice.setInvoiceTitle(nuxeoinvoiceName);
//		invoice.setInvoiceDescription(desc);
//		invoice.setPrefix(prefix);
//		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String invoiceA2 = invoiceA1[1].trim();
		String invoiceA3[] = invoiceA2.split("\\n");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceAddress(invoice1.trim());
		// ============ INvoice Date =====================
		String[] invoiceDateA1 = pdfStr.split(invoiceDte);
		String invoiceDateA2 = invoiceDateA1[1].trim();
		String invoiceDateA3[] = invoiceDateA2.split("\\n");
		String invoiceDate1 = invoiceDateA3[0].trim();
		invoice.setInvoiceDate(invoiceDate1);
		/*
		 * customerNo
		 */
		String cNo[] = pdfStr.split(customerNo);
		String costumerNo[] = cNo[1].trim().split("\\n");
		invoice.setDeliveryDate(costumerNo[0].trim());
		/*
		 * referNo
		 */
		String rNo[] = pdfStr.split(referNo);
		String refNo[] = rNo[1].trim().split("\\n");
		invoice.setReferenceNumber(refNo[0].trim());

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
		/*
		 * SupplierAddress
		 */
		String suplierAddr[] = pdfStr.split(referNo);
		String suplierAddrA1[] = suplierAddr[0].trim().split("\\n");
		String supAdd = suplierAddrA1[0].trim();
		invoice.setSupplierAddress(suplierAddr[0].trim());
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
		invoice.setGrossTotal(invoiceTotalA4);
		// ===============Invoice Address ================
		invoice.setInvoiceAddress(suplierAddr[0].trim());
		invoice.setCustomerAddress(suplierAddr[0].trim());
		invoice.setCustomerName(supAdd);
		invoice.setDeliveryAddress(suplierAddr[0].trim());

		// ===============Quality Address ================

		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split("62.50");
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String quentityA4[] = invTBLEa1.split("\\n");
		for (int i = 0; i < quentityA4.length; i++) {
			String itemCode = "", quantity = "", price = "", unit = "", orderNo = "", lineValue = "",
					description = "";
			System.out.println(quentityA4[i].trim());
			InvoiceTable invoicetab = new InvoiceTable();
			Pattern pattern = Pattern.compile(invoiceTableA2);
			if (pattern.matcher(quentityA4[i].trim()).find()) {
				String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
				String invoiceTableA21[] = invoiceTableA1[0].trim().split("        ");
				itemCode = invoiceTableA21[0];
				String des[] = invoiceTableA1[0].split(itemCode);
				description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
				String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
				System.out.println(itemCode);
				quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				unit = untPrice[1].replaceAll(invoiceTableA2, "$2");
				;
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description += quentityA4[i].trim();
			}

			invoicetab.setItemNo(itemCode);
			invoicetab.setQuantity(quantity);
			invoicetab.setUnit(unit);
			invoicetab.setPrice(price);
			invoicetab.setOrderNo(orderNo);
			invoicetab.setNetAmount(lineValue);
			invoicetab.setDescription(description);
			invoiceTable.add(invoicetab);
			description = "";
		}
		invoice.setInvoiceTable(invoiceTable);
	} catch (Exception e) {
		log.info(e.getMessage());
	}


	return invoice;
}

}
