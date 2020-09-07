
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
 * @since 2020-07-29
 *
 */
@Service
public class UniversalService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(UniversalService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.universal}")
	private String folderName;

	@Value("${import.universal.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.biffa.type}")
	private String nuxeoinvoiceType;

	@Value("${import.universal.prefix}")
	private String prefix;

	@Value("${import.nuxeo.universal.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${universal.invoiceNo}")
	private String invoiceNo;

	@Value("${universal.currency}")
	private String currency;

	@Value("${universal.vatTotal}")
	private String vatTotal;

	@Value("${universal.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${universal.delevaryAddress}")
	private String delevaryAddress;

	@Value("${universal.delevaryAddr}")
	private String delevaryAddr;

	@Value("${universal.faxNo}")
	private String faxNo;

	@Value("${universal.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${universal.vatNo}")
	private String vatNo;

	@Value("${universal.phone}")
	private String phone;

	@Value("${universal.netTotal}")
	private String netTotal;

	@Value("${universal.invoiceDte}")
	private String invoiceDte;

	@Value("${universal.grossTotalC1}")
	private String grossTotalC1;

	@Value("${universal.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${universal.supplierName}")
	private String supplierName; 
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			invoice.setSortName(Constants.UNIVERSAL);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.trim().split("\\s+");
			String invoice1 = invoiceA3[6].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			invoice.setAccountNo(invoiceA3[8].trim());
			// ============ INvoice Date =====================
			invoice.setInvoiceDate(invoiceA3[7].trim());
			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("\\s+");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			/*
			 * Supplier Name
			 */
			String sAddress[] = pdfStr.split("\\d*\\-\\d*\\-\\d*");
			String supplierAddr = sAddress[0].trim();
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(supplierAddr);
			String telephoneA3[] = telephoneA2[1].trim().split("\\s+");
			String phone = telephoneA3[0].trim();
			invoice.setTelephone(phone);

			/*
			 * Invoice Address
			 */// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceAll("[(]*", "").replaceAll("[)]*", "")
					.replaceAll(delevaryAddr1, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i].split("                                                 ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
				deleveryAddressSum += sepretAddr[1];
			}
			String cName[] = invoiceAddressSum.split("         ");
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(cName[0]);
			invoice.setDeliveryAddress(deleveryAddressSum);

			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1];
			String netTotalA3[] = netTotalA2.trim().split("\\n");
			String netTotalA4[] = netTotalA3[2].trim().split("\\s+");
			invoice.setNetTotal(netTotalA4[0]);
			// ===============NetInvoiceTotal ================
			invoice.setGrossTotal(netTotalA4[2]);
			/*
			 * Vat Total
			 */
			invoice.setVatTotal(netTotalA4[1]);
			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(currency);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			String description = "";
			for (int i = 0; i < quentityA4.length; i++) {
				String description1 = "", itemCode = "", quantity = "", price = "", discount = "", lineValue = "";

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					discount = untPrice[1].replaceAll(invoiceTableA2, "$1");
					quantity = untPrice[1].replaceAll(invoiceTableA2, "$2");
					price = untPrice[1].replaceAll(invoiceTableA2, "$3");
					lineValue = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description1 = quentityA4[i].trim();

				}
				invoicetab.setItemNo(itemCode.trim());
				invoicetab.setQuantity(quantity);
				invoicetab.setDiscount(discount);
				invoicetab.setPrice(price);
				invoicetab.setNetAmount(lineValue);
				invoicetab.setDescription(description + description1);
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
