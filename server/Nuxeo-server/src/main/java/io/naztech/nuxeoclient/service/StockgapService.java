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
 * @since 2020-07-28
 *
 */
@Service
public class StockgapService implements PdfInvoiceProcessor{
	private static Logger log = LoggerFactory.getLogger(StockgapService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.stockgap.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.stockgap.type}")
	private String nuxeoinvoiceType;

	@Value("${import.stockgap.prefix}")
	private String prefix;

	@Value("${import.nuxeo.stockgap.description}")
	private String desc;

	@Value("${stock.supplierName}")
	private String supplierName;

	@Value("${stock.gross}")
	private String gross;

	@Value("${stock.invoiceNo}")
	private String invoiceNo;

	@Value("${stock.invoiceDte}")
	private String invoiceDte;

	@Value("${stock.grossTotalC1}")
	private String grossTotalC1;

	@Value("${stock.netTotalC1}")
	private String netTotalC1;

	@Value("${stock.accountNo}")
	private String accountNo;

	@Value("${stock.delevaryAddr6}")
	private String delevaryAddr6;

	@Value("${stock.vatTotal}")
	private String vatTotal;

	@Value("${stock.vatNo}")
	private String vatNo;

	@Value("${stock.invoiceDes}")
	private String invoiceDes;

	@Value("${stock.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${stock.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${stock.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${stock.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${stock.delevaryAddr5}")
	private String delevaryAddr5;

	@Value("${stock.onDate}")
	private String onDate;

	@Value("${stock.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${stock.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${stock.suplierAddress}")
	private String suplierAddress;   
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			String json=pdfStr;
			invoice.setSortName(Constants.STOCKGAP);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[\\D]{2}[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceDes);
			String invoiceAddressA2 = invoiceAddressA1[0].replaceFirst(delevaryAddr5, "")
					.replaceFirst(delevaryAddr6, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(invoiceTableA1);
			String invoiceAddressA4[] = invoiceAddressA3[0].trim().split("\\n");
			invoice.setCustomerName(invoiceAddressA4[0]);
			invoice.setCustomerAddress(invoiceAddressA3[0].trim());
			invoice.setDeliveryAddress(invoiceAddressA3[0].trim());
			invoice.setInvoiceAddress(delevaryAddr1 + delevaryAddr2 + delevaryAddr3 + delevaryAddr4 + delevaryAddr5);

			// =============== VAT No ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			// ===============Ref No ================
			String[] referNoA1 = json.split(grossTotalC1);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("[a-zA-Z]");
			String refNo = referNoA3[0].trim();
			invoice.setRegNo(refNo.trim());
			/*
			 * SuplierAddress & Name
			 */
			String sAddr[] = json.split(supplierName);
			String sAddress[] = sAddr[1].split(vatNo);
			String sName[] = sAddress[0].trim().split("\\s+");
			invoice.setSupplierAddress(sAddress[0].trim());
			invoice.setSupplierName(sName[0] + " " + sName[1]);
			// ===============Net Total Address ================
			String[] netTotalA122 = json.split(onDate);
			String netTotalA21 = netTotalA122[1].trim();
			String netTotalA31[] = netTotalA21.split("[a-zA-Z]");
			invoice.setNetTotal(netTotalA31[0]);
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
			String duetotalA[] = due1.split("[\\D]{14}[a-zA-Z]");
			String duetotalA1 = duetotalA[0].replaceFirst("[\\w]{14}", "").trim();
			String duetotal = duetotalA1;
			invoice.setAccountNo(duetotal);
			System.out.println("hi");
			// ===============Quality Address ================
			String[] quentityA1 = json.split(gross);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(suplierAddress);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String unite = "", unitePrice = "", itemCode = "", description = "", quentity = "", amount = "",
						desc = "", lineTotal = "";
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);

					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();

					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$3");

					unite = untPrice[1].replaceAll(invoiceTableA2, "$2");

					amount = untPrice[1].replaceAll(invoiceTableA2, "$4");
					desc = untPrice[1].replaceAll(invoiceTableA2, "$5");
					lineTotal = untPrice[1].replaceAll(invoiceTableA2, "$6");
					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setDescription(description);
				invoicetab.setUnit(unite);
				invoicetab.setProductCode(itemCode);
				invoicetab.setNetAmount(amount);
				invoicetab.setDiscount(desc);
				invoicetab.setTotalNetPrice(lineTotal);
				invoiceTable.add(invoicetab);

				quentity = "";
				unitePrice = "";
				description = "";
				unite = "";
				itemCode = "";
				amount = "";
				desc = "";
				lineTotal = "";
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
return invoice;
	}


}
