
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
 * @since 2020-08-9
 *
 */
@Service
public class SuperglassService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(SuperglassService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.superglass}")
	private String folderName;

	@Value("${import.superglass.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.biffa.type}")
	private String nuxeoinvoiceType;

	@Value("${import.superglass.prefix}")
	private String prefix;

	@Value("${import.nuxeo.superglass.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${superglass.invoiceNo}")
	private String invoiceNo;

	@Value("${superglass.invoiceDte}")
	private String invoiceDte;

	@Value("${superglass.delevaryAddr}")
	private String delevaryAddr;

	@Value("${superglass.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${superglass.web}")
	private String web;

	@Value("${superglass.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${superglass.componyNo}")
	private String componyNo;

	@Value("${superglass.vatNo}")
	private String vatNo;

	@Value("${superglass.phone}")
	private String phone;

	@Value("${superglass.netTotal}")
	private String netTotal;

	@Value("${superglass.vatTotal}")
	private String vatTotal;

	@Value("${superglass.grossTotalC1}")
	private String grossTotalC1;

	@Value("${superglass.faxNo}")
	private String faxNo;

	@Value("${superglass.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${superglass.email}")
	private String email;

	@Value("${superglass.customerNo}")
	private String customerNo;

	@Value("${superglass.orderNo}")
	private String orderNo;

	@Value("${superglass.dueDte}")
	private String dueDte;

	@Value("${superglass.invoiceAddress1}")
	private String invoiceAddress1;

	@Value("${superglass.invoiceAddress2}")
	private String invoiceAddress2;

	@Value("${superglass.invoiceAddress3}")
	private String invoiceAddress3;

	@Value("${superglass.invoiceAddress4}")
	private String invoiceAddress4;

	@Value("${superglass.invoiceAddress5}")
	private String invoiceAddress5;

	@Value("${superglass.suplierAddress}")
	private String suplierAddress;

	@Value("${superglass.suplierAddress1}")
	private String suplierAddress1;

	@Value("${superglass.suplierAddress2}")
	private String suplierAddress2;

	@Value("${superglass.suplierAddress3}")
	private String suplierAddress3;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			invoice.setSortName(Constants.SUPERGLASS);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);

			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[1].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			invoice.setAccountNo(invoiceA3[0].trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);
			/*
			 * customerNo
			 */
			String cNo[] = pdfStr.split(customerNo);
			String costumerNo1[] = cNo[1].trim().split("\\s+");
			System.out.println(costumerNo1[0]);
//					invoice.setc 
			/*
			 * orderNo
			 */
			String oNo[] = pdfStr.split(orderNo);
			String orderNo[] = oNo[1].trim().split("\\s+");
			invoice.setOrderNo(orderNo[0].trim());
			/*
			 * customerNo
			 */
			String deleveryNoteA1[] = pdfStr.split(dueDte);
			String deleveryNote[] = deleveryNoteA1[1].trim().split("\\s+");
			invoice.setDeliveryNoteNo(deleveryNote[0].trim());
			/*
			 * customerNo
			 */
			String deleveryNoteA11[] = pdfStr.split(web);
			String deleveryNote1[] = deleveryNoteA11[1].trim().split("\\s+");
			invoice.setDespatchDate(deleveryNote1[0].trim());

			/*
			 * Company No
			 */
			String companyNo[] = pdfStr.split(componyNo);
			String companyNoA1[] = companyNo[1].trim().split("[a-zA-z]");
//		invoice.setc

			// ==============phone===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split(vatNo);
			String phone1 = telephoneA3[0].trim();
			invoice.setTelephone(phone1);
			/*
			 * Fax
			 */
			String faxA1[] = pdfStr.split(faxNo);
			String faxA2[] = faxA1[1].split("[a-zA-z]");
			invoice.setFax(faxA2[0].trim());
			/*
			 * Fax
			 */
			String emailA1[] = pdfStr.split(email);
			String email1[] = emailA1[1].split("[a-zA-z]");
			invoice.setEmail(email1[0].trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].replaceAll(vatTotal, "").trim();
			String netTotalA3[] = netTotalA2
					.split("                                                                         ");
			String netTotalA4[] = netTotalA3[1].trim().split("\\s+");
			invoice.setNetTotal(netTotalA4[0].trim());
			invoice.setVatTotal(netTotalA4[1].trim());
			invoice.setGrossTotal(netTotalA4[2].trim());

			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(costumerNo1[0]);
			String invoiceAddressA3[] = invoiceAddressA1[1].replaceFirst(suplierAddress, "").trim()
					.replaceFirst(suplierAddress1, "").trim().replaceFirst(suplierAddress2, "").trim()
					.replaceFirst(suplierAddress3, "").replaceFirst(invoiceAddress1, "").trim()
					.replaceFirst(invoiceAddress2, "").trim().replaceFirst(invoiceAddress3, "").trim()
					.replaceFirst(invoiceAddress4, "").trim().replaceFirst(invoiceAddress5, "")
					.replaceAll(invoiceDte + invoiceDate1, "").replaceAll(customerNo + costumerNo1[0], "")
					.replaceAll(dueDte + deleveryNote[0].trim(), "").trim().split(phone);
			// ===============Invoice Address ================
			String cName[] = invoiceAddressA3[0].trim().split("\\n");
			invoice.setSupplierAddress(suplierAddress + suplierAddress1 + suplierAddress2 + suplierAddress3);
			invoice.setSupplierName(suplierAddress);
			invoice.setInvoiceAddress(
					invoiceAddress1 + invoiceAddress2 + invoiceAddress3 + invoiceAddress4 + invoiceAddress5);
			invoice.setCustomerAddress(invoiceAddressA3[0]);
			invoice.setCustomerName(cName[0].trim());
			invoice.setDeliveryAddress(invoiceAddressA3[0]);

			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(grossTotalC1);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				String description = "", itemCode = "", quantity = "", price = "", grossPrice = "", lineValue = "";

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quantity = untPrice[1].replaceAll(invoiceTableA2, "$2");
					price = untPrice[1].replaceAll(invoiceTableA2, "$3");
					grossPrice = untPrice[1].replaceAll(invoiceTableA2, "$4");
					lineValue = untPrice[1].replaceAll(invoiceTableA2, "$5");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				}
				invoicetab.setItemNo(itemCode);
				invoicetab.setQuantity(quantity);
				invoicetab.setPrice(price);
				invoicetab.setTotalNetPrice(lineValue);
				invoicetab.setNetAmount(grossPrice);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);
			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
