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
 * @author mazhar.alam
 * @since 2020-07-28
 *
 */
@Service
public class RobertlrService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(JslService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.robertlee.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.robertlee.type}")
	private String nuxeoinvoiceType;

	@Value("${import.robertlee.prefix}")
	private String prefix;

	@Value("${import.nuxeo.robertlee.description}")
	private String desc;

	@Value("$robertlr.invoiceNo}")
	private String invoiceNo;

	@Value("$robertlr.invoiceDte}")
	private String invoiceDte;

	@Value("$robertlr.invoiceAddress}")
	private String invoiceAddress;

	@Value("$robertlr.referNo}")
	private String referNo;

	@Value("$robertlr.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("$robertlr.accountNo}")
	private String accountNo;

	@Value("$robertlr.vatNo}")
	private String vatNo;

	@Value("$robertlr.phone}")
	private String phone;

	@Value("$robertlr.faxNo}")
	private String faxNo;

	@Value("$robertlr.netTotal}")
	private String netTotal;

	@Value("$robertlr.vatTotal}")
	private String vatTotal;

	@Value("$robertlr.grossTotalC1}")
	private String grossTotalC1;

	@Value("$robertlr.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("$robertlr.email}")
	private String email;

	@Value("$robertlr.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("$robertlr.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("$robertlr.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("$robertlr.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("$robertlr.suplierAddress}")
	private String suplierAddress;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

			invoice.setSortName(Constants.ROBERTLEE);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\s");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);

			/*
			 * saDDRESS
			 */
			String sAdd[] = pdfStr.split(delevaryAddr3);
			String suplierNameA212 = sAdd[0].replaceAll(delevaryAddr4, "").replaceAll(invoiceNo + invoice1, "")
					.replaceAll(invoiceDte + invoiceDate, "").trim();
			invoice.setSupplierAddress(suplierNameA212.trim());
			invoice.setSupplierName(delevaryAddr5);
			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("\\s+");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			invoice.setOrderNo(accountA3[1].trim());
			invoice.setDeliveryDate(accountA3[2].trim());
			invoice.setDeliveryNoteNo(accountA3[3].trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(invoiceAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
				String sepretAddr[] = invoiceAddressA4[i].split("                                ");
				invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
				deleveryAddressSum += sepretAddr[1];
			}
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(delevaryAddr1);
			invoice.setDeliveryAddress(deleveryAddressSum);

			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0].replaceAll("\\|", "").trim();
			invoice.setTelephone(phone1);

			/*
			 * Email
			 */
			String emailA1[] = pdfStr.split(email);
			String emailA2[] = emailA1[1].replaceAll("\\|", "").split("\\n");
			invoice.setEmail(emailA2[0].trim());
			/*
			 * Fax
			 */
			String faxA1[] = pdfStr.split(faxNo);
			String faxA2[] = faxA1[1].replaceAll("\\|", "").split("\\n");
			invoice.setFax(faxA2[0].trim());
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
			// ===============Quality Address ================

			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(suplierAddress);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String code = "", vatP = "", discount = "", vat = "", description = "", quentity = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					code = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(code);
					description = des[1].trim();

					String untPrice[] = quentityA4[i].split(description);

					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					netTotal = untPrice[1].replaceAll(invoiceTableA2, "$2");
					discount = untPrice[1].replaceAll(invoiceTableA2, "$3");
					vatP = untPrice[1].replaceAll(invoiceTableA2, "$4");
					vat = untPrice[1].replaceAll(invoiceTableA2, "$5");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}
				invoicetab.setQuantity(quentity);
				invoicetab.setTotalNetPrice(netTotal);
				invoicetab.setDescription(description);
				invoicetab.setDiscount(discount);
				invoicetab.setNetAmount(vatP);
				invoicetab.setVatPercentage(vat);
				invoicetab.setStockCode(code);
				invoiceTable.add(invoicetab);

				netTotal = "";
				discount = "";
				description = "";
				vatP = "";
				vat = "";
				quentity = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}
}
