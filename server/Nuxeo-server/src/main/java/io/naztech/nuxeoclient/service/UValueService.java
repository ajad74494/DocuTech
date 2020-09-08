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
 * @since 2020-07-22
 *
 */
@Service
public class UValueService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(UValueService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.uvalue.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.uvalue.type}")
	private String nuxeoinvoiceType;

	@Value("${import.uvalue.prefix}")
	private String prefix;

	@Value("${import.nuxeo.uvalue.description}")
	private String desc;

	@Value("${uValue.invoiceNo}")
	private String invoiceNo;

	@Value("${uValue.invoiceDte}")
	private String invoiceDte;

	@Value("${uValue.invoiceAddress}")
	private String invoiceAddress;

	@Value("${uValue.referNo}")
	private String referNo;

	@Value("${uValue.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${uValue.accountNo}")
	private String accountNo;

	@Value("${uValue.vatNo}")
	private String vatNo;

	@Value("${uValue.phone}")
	private String phone;

	@Value("${uValue.faxNo}")
	private String faxNo;

	@Value("${uValue.netTotal}")
	private String netTotal;

	@Value("${uValue.vatTotal}")
	private String vatTotal;

	@Value("${uValue.grossTotalC1}")
	private String grossTotalC1;

	@Value("${uValue.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${uValue.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${uValue.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${uValue.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${uValue.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${uValue.delevaryAddr6}")
	private String delevaryAddr6;

	@Value("${uValue.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${uValue.suplierAddress}")
	private String suplierAddress;   
   

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
String json=pdfStr;
		try {

			invoice.setSortName(Constants.UVALUE);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String suplierAddredd[] = json.split(phone);
			String suplierName[] = suplierAddredd[0].trim().split("\\n");
			invoice.setSupplierAddress(suplierAddredd[0].trim());

			invoice.setSupplierName(suplierName[0].trim());

			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(invoiceTableA1);
			String invoiceA4[] = invoiceA3[0].split("\\s+");
			String invoice1 = invoiceA4[0].trim();
			invoice.setInvoiceNumber(invoice1);
			invoice.setInvoiceDate(invoiceA4[4].trim());
			invoice.setReferenceNumber(invoiceA4[3].trim());
			invoice.setOrderNo(invoiceA4[2].trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceAddress);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceFirst(delevaryAddr1, "")
					.replaceFirst(delevaryAddr2, "").trim().replaceFirst(delevaryAddr3, "").trim()
					.replaceFirst(delevaryAddr4, "").trim().replaceFirst(delevaryAddr5, "").trim().trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
			String invoiceAddress = invoiceAddressA3[0].trim();
			invoice.setCustomerAddress(invoiceAddress.replaceFirst(delevaryAddr6, ""));
			String costumernameA1[] = invoiceAddress.split("\r\n");
			invoice.setCustomerName(costumernameA1[0]);
			invoice.setDeliveryAddress(invoiceAddress.replaceFirst(delevaryAddr6, ""));
			invoice.setInvoiceAddress(
					delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5 + delevaryAddr6);

			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();

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
			String phone = telephoneA3[0].trim();
			invoice.setTelephone(phone);
			;
			// ==============Telephone no ===========
			String faxA1 = json.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("[a-zA-Z]");
			String fax = faxA3[0].trim();
			// ==============Telephone no ===========
			String tketA1 = json.trim();
			String tketA2[] = tketA1.split(invoiceDte);
			String tketA3[] = tketA2[1].replaceAll("Reprint\\s*Vat", "").split(vatNo);
			String tket = tketA3[0].trim();
			invoice.setEmail(tket);
			 
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("\\n");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = json.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("\\n");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = json.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("\\n");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4); 
			// ===============Quality Address ================

			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(suplierAddress);  
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String totalPrice = "", unitePrice = "", itemCode = "", description = "", quentity = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();

					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
					 unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$3");

					System.out.println(unitePrice);

					itemCode = untPrice[1].replaceAll(invoiceTableA2, "$2");

					totalPrice = untPrice[1].replaceAll(invoiceTableA2, "$4");
					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}
				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setTotalNetPrice(totalPrice);
				invoicetab.setDescription(description);
				invoicetab.setUnit(itemCode);
				invoiceTable.add(invoicetab);

				totalPrice = "";
				unitePrice = "";
				itemCode = "";
				description = "";
				quentity = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

	
		return invoice;
	}

}
