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
public class TannasService implements PdfInvoiceProcessor{
	private static Logger log = LoggerFactory.getLogger(TannasService.class);
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

	@Value("${tannas.supplierName}")
	private String supplierName;

	@Value("${tannas.gross}")
	private String gross;

	@Value("${tannas.invoiceNo}")
	private String invoiceNo;

	@Value("${tannas.invoiceDte}")
	private String invoiceDte;

	@Value("${tannas.grossTotalC1}")
	private String grossTotalC1;

	@Value("${tannas.netTotalC1}")
	private String netTotalC1;

	@Value("${tannas.accountNo}")
	private String accountNo;

	@Value("${tannas.componyNo}")
	private String componyNo;

	@Value("${tannas.vatTotal}")
	private String vatTotal;

	@Value("${tannas.vatNo}")
	private String vatNo;

	@Value("${tannas.invoiceDes}")
	private String invoiceDes;

	@Value("${tannas.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${tannas.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${tannas.onDate}")
	private String onDate;

	@Value("${tannas.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${tannas.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${tannas.suplierAddress}")
	private String suplierAddress;
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			String json=pdfStr;
			invoice.setSortName(Constants.TANNAS);
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
			String invoiceDateA3[] = invoiceDateA2.split(componyNo);
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);

			// ============ Deposit =====================
			String[] depositA1 = json.split(delevaryAddr2);
			String depositA2 = depositA1[1].trim();
			String depositA3[] = depositA2.split("[a-zA-Z]");
			String deposit = depositA3[0].trim();
			/*
			 * DeleveryAddress
			 */
			String deleveryA1[]=json.split(invoiceDes);
			String deleveryA2[]=deleveryA1[1].replaceAll(deposit, "").trim().split(invoiceTableA1);
			invoice.setDeliveryAddress(delevaryAddr1+" "+deleveryA2[0].trim());
			invoice.setCustomerAddress(delevaryAddr1+" "+deleveryA2[0].trim());
			invoice.setCustomerName(delevaryAddr1);
			 
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
			String duetotalA[] = due1.split("[a-zA-Z]");
			String duetotalA1 = duetotalA[0].trim();
			String duetotal = duetotalA1;
			invoice.setAccountNo(duetotal); 
			// ===============Quality Address ================
			String[] quentityA1 = json.split(gross);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(suplierAddress);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {

				InvoiceTable invoicetab = new InvoiceTable();

				String unite = "", unitePrice = "", itemCode = "", description = "", quentity = "", amount = "",
						desc = "" ;
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
					quentity = untPrice[1].replaceAll(invoiceTableA2, "$1");
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].replaceAll("%", "").trim();
				}

				invoicetab.setProductCode(itemCode);
				invoicetab.setDescription(description);
				invoicetab.setQuantity(quentity);
				invoicetab.setUnitPrice(unitePrice);
				invoicetab.setUnit(unite);
				invoicetab.setNetAmount(amount);
				invoicetab.setDiscount(desc); 
				invoiceTable.add(invoicetab);

				quentity = "";
				unitePrice = "";
				description = "";
				unite = "";
				itemCode = "";
				amount = "";
				desc = ""; 
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

	
		return invoice;
	}
	
}
