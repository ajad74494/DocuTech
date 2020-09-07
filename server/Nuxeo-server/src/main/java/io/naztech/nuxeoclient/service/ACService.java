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
 * @since 2020-08-12
 *
 */
@Service
public class ACService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(ACService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.acsupply}")
	private String folderName;

	@Value("${import.acsupply.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.acsupply.type}")
	private String nuxeoinvoiceType;

	@Value("${import.acsupply.prefix}")
	private String prefix;

	@Value("${import.nuxeo.acsupply.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${ac.invoiceNo}")
	private String invoiceNo;

	@Value("${ac.email}")
	private String email;

	@Value("${ac.invoiceDes}")
	private String invoiceDes;

	@Value("${ac.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${ac.delevaryAddr}")
	private String delevaryAddr;

	@Value("${ac.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${ac.web}")
	private String web;

	@Value("${ac.vatNo}")
	private String vatNo;

	@Value("${ac.phone}")
	private String phone;

	@Value("${ac.netTotal}")
	private String netTotal;

	@Value("${ac.vatTotal}")
	private String vatTotal;

	@Value("${ac.grossTotalC1}")
	private String grossTotalC1; 
	
	@Value("${ac.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${ac.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${ac.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public io.naztech.nuxeoclient.model.Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

			invoice.setSortName(Constants.ACSUPPLY);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].replaceAll("\\/ ", "/").trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[6].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			invoice.setInvoiceDate(invoiceA3[5].trim());
			invoice.setAccountNo(invoiceA3[0].trim());
			invoice.setOrderNo(invoiceA3[2].trim());
			invoice.setDeliveryDate(invoiceA3[3].trim());
			invoice.setReferenceNumber(invoiceA3[4].trim());

			// ==============Phone Number===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-z]");
			String phone = telephoneA3[0].trim();
			invoice.setTelephone(phone);
			/*
			 * Email
			 */
			String emailA1[] = pdfStr.split(email);
			String email1[] = emailA1[1].split("\\n");
			invoice.setEmail(email1[0].trim());

			/*
			 * Website
			 */

			String webA1[] = pdfStr.split(web);
			String web1[] = webA1[1].split("\\n");
			invoice.setWebsite(web1[0].trim());

			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = pdfStr.split(delevaryAddr3);
			String suplierAddrA1[] = suplierAddr[1].trim().split(delevaryAddr4);
			String supAdd[] = suplierAddrA1[0].trim().split("\\n");
			invoice.setDeliveryAddress(suplierAddrA1[0].trim());
			invoice.setCustomerName(supAdd[0].trim());

			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("\\s+");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			invoice.setVatTotal(netTotalA3[2].trim());
			invoice.setCarriageNet(netTotalA3[1].trim());
			invoice.setGrossTotal(netTotalA3[3].trim());

			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr4);
			String invoiceAddressA2 = invoiceAddressA1[1];
			String invoiceAddressA3[] = invoiceAddressA2.split(phone);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i].split("                                             ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
				deleveryAddressSum += sepretAddr[1];
			}
			System.out.println();
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setSupplierAddress(delevaryAddr4 + " " + deleveryAddressSum);
			invoice.setSupplierName(delevaryAddr4);

			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);

			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(vatNo);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").replaceAll("/", "")
					.replaceAll("x", "").trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			// System.out.println(invTBLEa1);
			for (int i = 0; i < quentityA4.length; i++) {

				String description = "", itemCode = "", quantity = "", price = "", unit = "", value = "";
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(invoiceTableA21[0].trim());
					description = des[1].trim();

					String untPrice[] = quentityA4[i].split(des[1]);
					quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					price = untPrice[1].replaceAll(invoiceTableA2, "$2");
					unit = untPrice[1].replaceAll(invoiceTableA2, "$3");
					value = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				}
				invoicetab.setStockCode(itemCode);
				invoicetab.setQuantity(quantity);
				invoicetab.setPrice(price);
				invoicetab.setTotalNetPrice(value);
				invoicetab.setUnit(unit);
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
