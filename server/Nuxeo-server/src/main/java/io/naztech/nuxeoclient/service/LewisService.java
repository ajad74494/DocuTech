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
 * @since 2020-07-22
 *
 */
@Service
public class LewisService implements PdfInvoiceProcessor{ 
	private static Logger log = LoggerFactory.getLogger(LewisService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.uniteight.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.uniteight.type}")
	private String nuxeoinvoiceType;

	@Value("${import.uniteight.prefix}")
	private String prefix;

	@Value("${import.nuxeo.uniteight.description}")
	private String desc;

	@Value("${lewis.phone}")
	private String phone;

	@Value("${lewis.invoiceNo}")
	private String invoiceNo;

	@Value("${lewis.invoiceDte}")
	private String invoiceDte;

	@Value("${lewis.grossTotalC1}")
	private String grossTotalC1;

	@Value("${lewis.accountNo}")
	private String accountNo;

	@Value("${lewis.vatTotal}")
	private String vatTotal;

	@Value("${lewis.netTotalC1}")
	private String netTotalC1;

	@Value("${lewis.invoiceDes}")
	private String invoiceDes;

	@Value("${lewis.netTotal}")
	private String netTotal;

	@Value("${lewis.vatNo}")
	private String vatNo;

	@Value("${lewis.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${lewis.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${lewis.faxNo}")
	private String faxNo;  


	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			String json=pdfStr;

//			invoice.setSortName(Constants.l);
//			invoice.setInvoiceTitel(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(invoiceTableA1);
			String invoiceA4[] = invoiceA3[0].split("\\s+");
			String invoice1 = invoiceA4[4].trim();
			invoice.setInvoiceNumber(invoice1);
			invoice.setInvoiceDate(invoiceA4[0].trim());
			invoice.setReferenceNumber(invoiceA4[2].trim());
			invoice.setOrderNo(invoiceA4[1].trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceDes);
			String invoiceAddressA2 = invoiceAddressA1[0].trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(invoiceDte);
			String customerName[] = invoiceAddressA3[0].trim().split("\\n");
			invoice.setInvoiceAddress(invoiceAddressA3[0].trim());
			invoice.setCustomerName(customerName[0]);
			invoice.setCustomerAddress(invoiceAddressA3[0].trim());

			// =============== VAT No ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ===============Ref No ================
			String[] referNoA1 = json.split(grossTotalC1);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("[-]");
			String refNo = referNoA3[0].trim();
			invoice.setRegNo(refNo.trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// =============== Total Address ================
			String[] totalA1 = json.split(netTotalC1);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);

			String[] due = json.split(accountNo);
			String due1 = due[1].trim();
			String duetotalA[] = due1.split("[a-zA-Z]");
			String duetotalA1 = duetotalA[0].trim();
			String duetotal = duetotalA1;
			invoice.setAccountNo(duetotal);
			/*
			 * Carriage
			 */
			String carriageA1[] = json.split("Carriage");
			String carriageA2[] = carriageA1[1].replaceAll("-", "").split("[a-zA-Z]");
			invoice.setCarriageNet(carriageA2[0].trim());
			/*
			 * SupplierAddress SupplierName
			 */
			String sAddress[] = json.split(phone);
			String sName[] = sAddress[0].trim().split("\\n");
			invoice.setSupplierAddress(sAddress[0].trim());
			invoice.setSupplierName(sName[0].trim());
			// ==============Deleveru date===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone = telephoneA3[0].replaceAll("[(]", "").replaceAll("[)]", "").trim();
			invoice.setTelephone(phone);
			String webA1[] = telephoneA2[1].trim().split(faxNo);
			String webSite = webA1[0].replaceAll("[(]", "").replaceAll("[)]", "").replaceAll(phone, "").trim();
			invoice.setWebsite(webSite);
			// ==============Telephone no ===========
			String faxA1 = json.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("[a-zA-Z]");
			String fax = faxA3[0].trim();
			invoice.setEmail(fax);
			// ===============Quality Address ================
			String[] quentityA1 = json.replaceAll("[(]*", "").replaceAll("[)]*", "").replaceAll("[']*", "")
					.split(invoiceTableA1);

			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split("Bank  Details:");
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String totalPrice = "", unitePrice = "",   description = "", quentity = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					quentity = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(invoiceTableA21[0]);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].split(description);
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$1").trim();
					totalPrice = untPrice[1].replaceAll(invoiceTableA2, "$2");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotal(totalPrice);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = ""; 
				description = "";
				quentity = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
return invoice;
	}

}
