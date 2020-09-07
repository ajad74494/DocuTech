
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
public class ThePlasticsGroupService implements PdfInvoiceProcessor {
	
	private static Logger log = LoggerFactory.getLogger(ThePlasticsGroupService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.theplasticgroup}")
	private String folderName;

	@Value("${import.plasticsgroup.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.plasticsgroup.type}")
	private String nuxeoinvoiceType;

	@Value("${import.plasticsgroup.prefix}")
	private String prefix;

	@Value("${import.nuxeo.plasticsgroup.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${plastic.invoiceNo}")
	private String invoiceNo;

	@Value("${plastic.currency}")
	private String currency;

	@Value("${plastic.vatTotal}")
	private String vatTotal;

	@Value("${plastic.email}")
	private String email;

	@Value("${plastic.delevaryAddress}")
	private String delevaryAddress;

	@Value("${plastic.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${plastic.dueDte}")
	private String dueDte;

	@Value("${plastic.vatNo}")
	private String vatNo;

	@Value("${plastic.phone}")
	private String phone;

	@Value("${plastic.netTotal}")
	private String netTotal;

	@Value("${plastic.invoiceDte}")
	private String invoiceDte;

	@Value("${plastic.grossTotalC1}")
	private String grossTotalC1;

	@Value("${plastic.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${plastic.orderNo}")
	private String orderNo;

	@Value("${plastic.referNo}")
	private String referNo; 
 


@Override
public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
	 
	List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

	try {

		invoice.setSortName(Constants.THEPLASTICSGROUPLTD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);
		// ============ INvoice =====================
		String[] invoiceA1 = pdfStr.split(invoiceNo);
		String invoiceA2 = invoiceA1[1].trim();
		String invoiceA3[] = invoiceA2.trim().split("[a-zA-Z]");
		String invoice1 = invoiceA3[0].trim();
		invoice.setInvoiceNumber(invoice1.trim());
		// ============ INvoice Date =====================
		String[] dateA1 = pdfStr.split(invoiceDte);
		String dateA2 = dateA1[1].trim();
		String dateA3[] = dateA2.split("\\s+");
		invoice.setInvoiceDate(dateA3[0].trim());
		/*
		 * Order Number
		 */
		String orderA1[] = pdfStr.split(orderNo);
		String orderA2[] = orderA1[1].split("\\n");
		invoice.setOrderNo(orderA2[0].trim());

		/*
		 * Del Note
		 */
		String delNoteA1[] = pdfStr.split(dueDte);
		String delNoteA2[] = delNoteA1[1].split("\\n");
		invoice.setDeliveryNoteNo(delNoteA2[0].trim());

		// ===============VAT No Address ================
		String[] vatNoA1 = pdfStr.split(vatNo);
		String vatNoA2 = vatNoA1[1].trim();
		String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
		String vatNo1 = vatNoA3[0].trim();
		invoice.setVatNo(vatNo1);
		// ==============Deleveru date===========
		String telephoneA1 = pdfStr.trim();
		String telephoneA2[] = telephoneA1.split(phone);
		String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
		String phone = telephoneA3[0].trim();
		invoice.setTelephone(phone);

		/*
		 * Email
		 */
		String emailA1[] = pdfStr.split(email);
		String emailA2[] = emailA1[1].split("\\s+");
		invoice.setFax(emailA2[0].trim());
		/*
		 * Account Ref
		 */
		String refNoA1[] = pdfStr.split(referNo);
		String refA2[] = refNoA1[1].split("\\n");
		invoice.setReferenceNumber(refA2[0].trim());

		// ===============Invoice Address ================
		String[] invoiceAddressA1 = pdfStr.split(invoice1);
		String invoiceAddressA2 = invoiceAddressA1[1].replaceAll(invoiceDte + dateA3[0].trim(), "")
				.replaceAll(orderNo + orderA2[0].trim(), "").replaceAll(dueDte + delNoteA2[0].trim(), "").trim();
		String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
		invoice.setInvoiceAddress(invoiceAddressA3[0].trim());

		// ===============Net Total ================
		String[] netTotalA1 = pdfStr.split(netTotal);
		String netTotalA2 = netTotalA1[1];
		String netTotalA3[] = netTotalA2.trim().split("[a-zA-Z]");
		String netTotalA4 = netTotalA3[0].trim();
		invoice.setNetTotal(netTotalA4);
		// ===============vat Total ================
		String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
		String invoiceTotalA2 = invoiceTotalA1[1].trim();
		String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
		String invoiceTotalA4 = invoiceTotalA3[3].trim();
		invoice.setGrossTotal(invoiceTotalA4);
		/*
		 * Vat Total
		 */
		String vatTotalA1[] = pdfStr.split(vatTotal);
		String vatTotala2[] = vatTotalA1[1].split("[a-zA-Z]");
		invoice.setVatTotal(vatTotala2[0].trim());
		/*
		 * Carriage Net
		 */

		String carriageA1[] = pdfStr.split(currency);
		String carriage[] = carriageA1[1].split("[a-zA-Z]");
		invoice.setCarriageNet(carriage[0].trim());
		/*
		 * Delivery Address
		 */
		String deleveryAddressA1[] = pdfStr.split(delevaryAddress);
		String deleveryAddressA2 = deleveryAddressA1[1].replaceAll(netTotal + netTotalA4, "")
				.replaceAll(currency + carriage[0].trim(), "").replaceAll(vatTotal + vatTotala2[0].trim(), "")
				.trim();
		String deleveryAddressA3[] = deleveryAddressA2.split(grossTotalC1);
		String customerName[] = deleveryAddressA3[0].trim().split("\\n");
		invoice.setDeliveryAddress(deleveryAddressA3[0].trim());
		invoice.setCustomerAddress(deleveryAddressA3[0].trim());
		invoice.setCustomerName(customerName[0].trim());
		
		/*
		 * Supplaire Address
		 */
		String suplierAddress[]=pdfStr.split(phone);
		String suplierAddressA1[]=suplierAddress[0].trim().split("\\n");
		invoice.setSupplierName(suplierAddressA1[0].trim());
		invoice.setSupplierAddress(suplierAddress[0].trim());
		/*
		 * Table Data
		 */
		String[] quentityA1 = pdfStr.split(invoiceTableA1);
		String quentityA2 = quentityA1[1].trim();
		String quentityA3[] = quentityA2.split(currency);
		String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
		String quentityA4[] = invTBLEa1.trim().split("\\n");
		for (int i = 0; i < quentityA4.length; i++) {
			String description1 = "", quantity = "", unitePrice = "", netAmount = "", vat = "", totalAmount = "",
					description = ""; 
			InvoiceTable invoicetab = new InvoiceTable();
			Pattern pattern = Pattern.compile(invoiceTableA2);
			if (pattern.matcher(quentityA4[i].trim()).find()) {
				String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
				String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
				quantity = invoiceTableA21[0];
				String des[] = invoiceTableA1[0].split(quantity);
				description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
				String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

				unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$1");
				netAmount = untPrice[1].replaceAll(invoiceTableA2, "$2");
				vat = untPrice[1].replaceAll(invoiceTableA2, "$3");
				totalAmount = untPrice[1].replaceAll(invoiceTableA2, "$4");
			}
			if (!pattern.matcher(quentityA4[i].trim()).find()) {
				description1 = quentityA4[i].trim();

			} 
			invoicetab.setQuantity(quantity);
			invoicetab.setUnitPrice(unitePrice);
			invoicetab.setNetAmount(netAmount);
			invoicetab.setVatAmount(vat);
			invoicetab.setTotal(totalAmount);
			invoicetab.setDescription(description+" "+description1);
			invoiceTable.add(invoicetab); 
			
		}
		invoice.setInvoiceTable(invoiceTable);
	} catch (Exception e) {
		log.info(e.getMessage());
	}


	return invoice;
}

}
