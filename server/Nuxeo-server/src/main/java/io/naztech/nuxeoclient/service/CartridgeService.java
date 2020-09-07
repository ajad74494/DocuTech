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
 * @since 2020-07-23
 *
 */
@Service
public class CartridgeService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(LewisService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.cartridge.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.cartridge.type}")
	private String nuxeoinvoiceType;

	@Value("${import.cartridge.prefix}")
	private String prefix;

	@Value("${import.nuxeo.cartridge.description}")
	private String desc;

	@Value("${cartridge.accountcode}")
	private String accountcode;

	@Value("${cartridge.invoiceNo}")
	private String invoiceNo;

	@Value("${cartridge.invoiceDte}")
	private String invoiceDte;

	@Value("${cartridge.vatTotal}")
	private String vatTotal;

	@Value("${cartridge.grossTotalC1}")
	private String grossTotalC1;

	@Value("${cartridge.delevaryAddr}")
	private String delevaryAddr;

	@Value("${cartridge.accountNo}")
	private String accountNo;

	@Value("${cartridge.orderNo}")
	private String orderNo;

	@Value("${cartridge.phone}")
	private String phone;

	@Value("${cartridge.vatNo}")
	private String vatNo;

	@Value("${cartridge.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${cartridge.dueDte}")
	private String dueDte;

	@Value("${cartridge.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${cartridge.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${cartridge.gross}")
	private String gross;

	@Value("${cartridge.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${cartridge.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${cartridge.telephone}")
	private String telephone;

	@Value("${cartridge.componyNo}")
	private String componyNo;

	@Value("${cartridge.referNo}")
	private String referNo;

	@Value("${cartridge.email}")
	private String email;

	@Value("${cartridge.currency}")
	private String currency;

	@Value("${cartridge.onDate}")
	private String onDate;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			invoice.setSortName(Constants.CARTRIDGE);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = pdfStr;
			// ============ INvoice =====================
			String inv[] = json.split(delevaryAddr2);
			String inv1[] = inv[1].split(invoiceDte);
			String[] invoiceA1 = inv1[0].split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\n");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\n");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(invoiceTableA1);
			String cName[] = invoiceAddressA3[0].trim().split("\\n");
			invoice.setDeliveryAddress(invoiceAddressA3[0].trim());
			invoice.setCustomerAddress(invoiceAddressA3[0].trim());
			invoice.setCustomerName(cName[1]);
			// =============== VAT No ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ===============Ref No ================
			String[] referNoA1 = json.split(orderNo);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.trim().split("\\s+");
			String refNo = referNoA3[0].trim();
			invoice.setOrderNo(refNo.trim());

			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(gross);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============Account Code ================
			String[] accountCodeA1 = json.split(accountcode);
			String accountCodeA2 = accountCodeA1[1].trim();
			String accountCodeA3[] = accountCodeA2.split("\\n");
			String accountCodeNo1 = accountCodeA3[0].trim();
			invoice.setReferenceNumber(accountCodeNo1);
			// ==============phone date===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone1 = telephoneA3[0].trim();
			invoice.setTelephone(phone1);

			/*
			 * Email
			 */
			String emailA1[] = json.split(email);
			String emailA2[] = emailA1[1].split("[a-zA-Z]");
			invoice.setFax(emailA2[0].trim());
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
			// =============== Total Address ================
			/*
			 * InvoiceAddress
			 */
			String addressA1[] = json.split(invoice1);
			String addressA2[] = addressA1[1].replaceAll(invoiceDte + invoiceDate, "")
					.replaceAll(accountcode + accountCodeNo1, "").replaceAll(delevaryAddr4, "").replaceAll(dueDte, "")
					.split(orderNo);
			invoice.setInvoiceAddress(addressA2[0].trim());

			/*
			 * SuplierAddress
			 */
			String sAddress[] = json.split(delevaryAddr5);
			String sAddress1 = sAddress[0].replaceAll(invoiceNo, "").replaceAll(referNo, "").replaceAll(componyNo, "")
					.replaceAll(telephone, "");
			String sName[] = sAddress1.trim().split("\\n");
			invoice.setSupplierName(sName[0].trim());
			invoice.setSupplierAddress(sAddress1);
			// ===============Quality Address ================
			String[] quentityA1 = json.split(onDate);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(currency);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String vatRate = "", unitePrice = "", description = "", quentity = "", vat = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					quentity = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(quentity);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();

					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$1");
					netTotal = untPrice[1].replaceAll(invoiceTableA2, "$2");
					vatRate = untPrice[1].replaceAll(invoiceTableA2, "$3");
					vat = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(netTotal);
				invoicetab.setDescription(description);
				invoicetab.setVatPercentage(vatRate);
				invoicetab.setVatAmount(vat);
				invoiceTable.add(invoicetab);

				unitePrice = "";
				description = "";
				quentity = "";
				vat = "";
				vatRate = "";
			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
		return invoice;

	}

}
