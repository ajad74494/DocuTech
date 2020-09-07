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
 * @since 2020-08-6
 *
 */
@Service
public class BiffaService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(BiffaService.class);

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

	@Value("${biffa.invoiceNo}")
	private String invoiceNo;

	@Value("${biffa.invoiceDte}")
	private String invoiceDte;

	@Value("${biffa.invoiceDes}")
	private String invoiceDes;

	@Value("${biffa.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${biffa.web}")
	private String web;

	@Value("${biffa.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${biffa.accountNo}")
	private String accountNo;

	@Value("${biffa.vatNo}")
	private String vatNo;

	@Value("${biffa.phone}")
	private String phone;

	@Value("${biffa.netTotal}")
	private String netTotal;

	@Value("${biffa.vatTotal}")
	private String vatTotal;
//	FBiffa  Waste  Services  Ltd  

	@Value("${biffa.grossTotalC1}")
	private String grossTotalC1;

	@Value("${biffa.faxNo}")
	private String faxNo;

	@Value("${biffa.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

			invoice.setSortName(Constants.BIFFA);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = pdfStr;
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("            ");
			String invoice1 = invoiceA3[1].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[\\D]{3}[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0].trim();
			invoice.setTelephone(phone);
			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = json.split(invoiceDes);
			String suplierAddrA1[] = suplierAddr[1].trim().split(invoiceNo);
			String supAdd = suplierAddrA1[0].trim();
			invoice.setSupplierAddress(invoiceDes + supAdd);
			invoice.setSupplierName(invoiceDes);
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = json.replaceAll("[(]*", "").replaceAll("[)]*", "").split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = json.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4);

			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.replaceAll("[*]*", "").split(accountNo1.replaceAll("[*]*", ""));
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(web);
			String invoiceAddressA4[] = invoiceAddressA3[0].trim().split("\\n");
			String invoiceAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length; i++) {
				invoiceAddressSum += " " + invoiceAddressA4[i].trim();
			}
			invoice.setInvoiceAddress(invoiceAddressSum.trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA11 = json.split(phone1);
			String invoiceAddressA21 = invoiceAddressA11[1].trim();
			String invoiceAddressA31[] = invoiceAddressA21.split(delevaryAddr1);
			String invoiceAddressA41[] = invoiceAddressA31[0].trim().split("\\n");
			String invoiceAddressSum1 = "";
			for (int i = 0; i < invoiceAddressA41.length; i++) {
				invoiceAddressSum1 += " " + invoiceAddressA41[i].trim();
			}
			invoice.setDeliveryAddress(invoiceAddressSum1.trim());
			invoice.setCustomerAddress(invoiceAddressSum1.trim());
			invoice.setCustomerName(invoiceAddressA41[0].trim());
			System.out.println(invoiceAddressSum.trim());
			System.out.println(invoiceAddressSum1.trim());
			System.out.println(invoiceAddressA41[0].trim());
			/*
			 * Table Data
			 */
			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				String description1 = "", itemcode = "", nettotal = "", discount = "", description = "";
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemcode = invoiceTableA21[0].trim();
					description = invoiceTableA1[0].replaceFirst(itemcode, "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					discount = untPrice[1].replaceAll(invoiceTableA2, "$5");
					nettotal = untPrice[1].replaceAll(invoiceTableA2, "$6");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description1 = quentityA4[i].trim();
				}
				invoicetab.setUnitPrice(discount.trim());
				invoicetab.setTotal(nettotal.trim());
				invoicetab.setDescription(description.trim() + " " + description1.trim());
				invoiceTable.add(invoicetab);

			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
