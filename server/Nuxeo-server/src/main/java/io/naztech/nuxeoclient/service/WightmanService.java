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
 * @since 2020-07-28
 *
 */
@Service
public class WightmanService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(WightmanService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.tannas.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.tannas.type}")
	private String nuxeoinvoiceType;

	@Value("${import.tannas.prefix}")
	private String prefix;

	@Value("${import.nuxeo.tannas.description}")
	private String desc;

	@Value("${wightman.supplierName}")
	private String supplierName;

	@Value("${wightman.invoiceNo}")
	private String invoiceNo;

	@Value("${wightman.invoiceDte}")
	private String invoiceDte;

	@Value("${wightman.invoiceAddress}")
	private String invoiceAddress;

	@Value("${wightman.delevaryAddress}")
	private String delevaryAddress;

	@Value("${wightman.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${wightman.accountNo}")
	private String accountNo;

	@Value("${wightman.vatNo}")
	private String vatNo;

	@Value("${wightman.phone}")
	private String phone;

	@Value("${wightman.netTotal}")
	private String netTotal;

	@Value("${wightman.faxNo}")
	private String faxNo;

	@Value("${wightman.vatTotal}")
	private String vatTotal;

	@Value("${wightman.grossTotalC1}")
	private String grossTotalC1;

	@Value("${wightman.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${wightman.invoiceTableA2}")
	private String invoiceTableA2;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			String json = pdfStr;
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+"); 
			invoice.setInvoiceDate(invoiceA3[6].trim());
			invoice.setTelephone(invoiceA3[0].trim() + invoiceA3[1].trim());
			invoice.setEmail(invoiceA3[2].trim());
			invoice.setWebsite(invoiceA3[4].trim());
			invoice.setInvoiceNumber(invoiceA3[5].trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
			String invoiceAddressSum = "", deleveryAddressSum = "";
			String invoiceAddress1[] = invoiceAddressA3[0].trim().split("\\n");
			for (int i = 0; i < invoiceAddress1.length - 1; i++) {
				String sp[] = invoiceAddress1[i].split("                                                     ");
				invoiceAddressSum = invoiceAddressSum + sp[0];
				deleveryAddressSum += " " + sp[1].trim();
			}
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(delevaryAddr1);
			invoice.setDeliveryAddress(deleveryAddressSum);

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ==============Deleveru date===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("\\n");
			String phone = telephoneA3[0].trim();
			invoice.setOrderNo(phone);
			// ==============Telephone no ===========
			String faxA1 = json.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("\\n");
			String fax = faxA3[0].trim();
			invoice.setReferenceNumber(fax);
			// ==============Telephone no ===========
			String tketA1 = json.trim();
			String tketA2[] = tketA1.split(faxNo);
			String tketA3[] = tketA2[1].split("[a-zA-Z]");
			String tket = tketA3[0].trim();
			/*
			 * SupplierAddress
			 */
			String suplierAddr[] = json.split(invoiceAddress);
			String suplierAddrA1[] = suplierAddr[0].replaceAll(invoiceDte, "").trim().split("\\n");
			String supAdd = suplierAddrA1[0].trim();
			invoice.setSupplierAddress(suplierAddr[0].replaceAll(invoiceDte, ""));
			invoice.setSupplierName(supAdd);
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = json.split(vatTotal);
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
			// ===============Quality Address ================

			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();
				String description = "", item = "", quantity = "", unite = "", netTotaltab = "", vat = "",
						parsentince = "", total = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					String des[] = invoiceTableA1[0].split(invoiceTableA21[0]);
					description = des[1].replaceAll("%", "");
					String untPrice[] = quentityA4[i].split(description);

					item = untPrice[1].replaceAll(invoiceTableA2, "$1");
					quantity = untPrice[1].replaceAll(invoiceTableA2, "$2");
					unite = untPrice[1].replaceAll(invoiceTableA2, "$3");
					netTotaltab = untPrice[1].replaceAll(invoiceTableA2, "$4");
					vat = untPrice[1].replaceAll(invoiceTableA2, "$5");
					parsentince = untPrice[1].replaceAll(invoiceTableA2, "$6");
					total = untPrice[1].replaceAll(invoiceTableA2, "$7");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}
				invoicetab.setItemNo(item);
				invoicetab.setQuantity(quantity);
				invoicetab.setUnit(unite);
				invoicetab.setUnitPrice(netTotaltab);
				invoicetab.setVatAmount(vat);
				invoicetab.setDiscount(parsentince);
				invoicetab.setTotalNetPrice(total);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

				description = "";
				item = "";
				quantity = "";
				unite = "";
				netTotaltab = "";
				vat = "";
				parsentince = "";
				total = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
