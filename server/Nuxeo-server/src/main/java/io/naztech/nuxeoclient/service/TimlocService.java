
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
public class TimlocService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(TimlocService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.timloc}")
	private String folderName;

	@Value("${import.timloc.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.timloc.type}")
	private String nuxeoinvoiceType;

	@Value("${import.timloc.prefix}")
	private String prefix;

	@Value("${import.nuxeo.timloc.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${timloc.invoiceNo}")
	private String invoiceNo;

	@Value("${timloc.invoiceDte}")
	private String invoiceDte;

	@Value("${timloc.invoiceAddress}")
	private String invoiceAddress;

	@Value("${timloc.referNo}")
	private String referNo;

	@Value("${timloc.delevaryAddress}")
	private String delevaryAddress;

	@Value("${timloc.delevaryAddr}")
	private String delevaryAddr;

	@Value("${timloc.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${timloc.accountNo}")
	private String accountNo;

	@Value("${timloc.vatNo}")
	private String vatNo;

	@Value("${timloc.phone}")
	private String phone;

	@Value("${timloc.orderNo}")
	private String orderNo;

	@Value("${timloc.netTotal}")
	private String netTotal;

	@Value("${timloc.dueDte}")
	private String dueDte;

	@Value("${timloc.vatTotal}")
	private String vatTotal;

	@Value("${timloc.grossTotalC1}")
	private String grossTotalC1;

	@Value("${timloc.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			invoice.setSortName(Constants.TIMLOC);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			/*
			 * Order No
			 */
			String orderA1[] = pdfStr.split(orderNo);
			String orderA2[] = orderA1[1].split("[a-zA-Z]");
			invoice.setOrderNo(orderA2[0].trim());
			/*
			 * due Date
			 */
			String dueA1[] = pdfStr.split(dueDte);
			String dueA2[] = dueA1[1].split("[a-zA-Z]");
			invoice.setDueDate(dueA2[0].trim());
			/*
			 * Ref No
			 */
			String refNoA1[] = pdfStr.split(referNo);
			String refNoA2[] = refNoA1[1].trim().split("\\s+");
			invoice.setReferenceNumber(refNoA2[0].trim());

			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split(vatNo);
			String phone = telephoneA3[0].trim();
//invoice.setCustomerNO(customerName); 
			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = pdfStr.split(invoiceAddress);
			String suplierAddrA1[] = suplierAddr[1].replaceAll(invoiceDte, "").trim().split(phone);
			String supAdd = suplierAddrA1[0].trim();
			invoice.setInvoiceAddress(supAdd);
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
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
			String invoiceAddress1[] = invoiceAddressA3[0].trim().split("\\n");

			invoice.setCustomerAddress(invoiceAddressA3[0].trim());
			invoice.setCustomerName(invoiceAddress1[0].trim());
			invoice.setDeliveryAddress(invoiceAddressA3[0].trim());
			// ===============Quality Address ================

			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].trim();
			String description = "", itemCode = "", quentity = "", unite = "", amount = "", descount = "";
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					unite = untPrice[1].replaceAll(invoiceTableA2, "$2");
					descount = untPrice[1].replaceAll(invoiceTableA2, "$3");
					amount = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				}
				invoicetab.setItemNo(itemCode);
				invoicetab.setQuantity(quentity);
				invoicetab.setUnit(unite);
				invoicetab.setDiscount(descount);
				invoicetab.setNetAmount(amount);
				invoicetab.setDescription(description);

			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
