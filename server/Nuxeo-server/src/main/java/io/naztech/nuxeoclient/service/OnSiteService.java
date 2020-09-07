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
import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author abul.kalam
 * @since 2020-07-19
 *
 */
@Service
public class OnSiteService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HarrisonService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.onsite.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.onsite.type}")
	private String nuxeoinvoiceType;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.onsite.prefix}")
	private String prefix;

	@Value("${import.nuxeo.onsite.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${import.processorType}")
	private String proType;

	@Value("${oneSite.supplierName}")
	private String supplierName;

	@Value("${oneSite.invoiceNo}")
	private String invoiceNo;

	@Value("${oneSite.invoiceDte}")
	private String invoiceDte;

	@Value("${oneSite.referNo}")
	private String referNo;

	@Value("${oneSite.delevaryAddr}")
	private String delevaryAddr;

	@Value("${oneSite.suplierAddress}")
	private String suplierAddress;

	@Value("${oneSite.accountNo}")
	private String accountNo;

	@Value("${oneSite.netTotalC1}")
	private String netTotalC1;
	@Value("${oneSite.netTotal}")
	private String netTotalp;

	@Value("${oneSite.vatTotal}")
	private String vatTotal;

	@Value("${oneSite.orderNo}")
	private String orderNo;

	@Value("${oneSite.onDate}")
	private String onDate;

	@Value("${oneSite.invoiceDes}")
	private String invoiceDes;

	@Value("${oneSite.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${oneSite.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${oneSite.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${oneSite.currency}")
	private String currency;

	@Value("${oneSite.gross}")
	private String gross;

	@Value("${oneSite.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${oneSite.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${oneSite.refNop}")
	private String refNop;

	@Value("${oneSite.costumerName}")
	private String costumerName;

	@Value("${oneSite.supplierAddress}")
	private String supplierAddress;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
			String json = pdfStr;

			invoice.setSortName(Constants.ONSITE);
			invoice.setInvoiceTitle("onsite");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("onsite");
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ============ Due Date: =====================
			String[] dueA1 = json.split(onDate);
			String dueA2 = dueA1[1].trim();
			String dueA3[] = dueA2.split("[a-zA-Z]");
			String dueDate = dueA3[0].trim();
			invoice.setDueDate(dueDate);
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceDes);
			String invoiceAddressA2 = invoiceAddressA1[1].trim().replaceAll(suplierAddress, "");
			String invoiceAddressA3[] = invoiceAddressA2.split(orderNo);
			String invoiceAddress = invoiceAddressA3[0].trim().replaceAll(referNo, "");
			String invoiceAddressA4 = invoiceAddress.trim().replaceAll("(\\w*\\s*\\w*:\\s*\\d*\\/\\d*\\/\\d*)", "");
			String invoiceAddressA5 = invoiceAddressA4.trim().replaceAll("(\\w*\\s*\\w*:\\s*\\d*)", "");
			invoice.setInvoiceAddress(invoiceAddressA5);
			// =============== VAT No ================
			String[] vatNoA1 = json.split(orderNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split(netTotalC1);
			String vatNo = vatNoA3[0].trim();
			invoice.setVatNo(vatNo);
			// ===============Ref No ================
			String[] referNoA1 = json.split(netTotalC1);
			String referNoA2 = referNoA1[1].trim();
			String referNoA3[] = referNoA2.split("Order Date:");
			String refNo = referNoA3[0].trim();
			invoice.setReferenceNumber(refNo);
			// ===============Order Date================
			String[] orderDteA1 = json.split(delevaryAddr);
			String orderDteA2 = orderDteA1[1].trim();
			String orderDteA3[] = orderDteA2.split(delevaryAddr1);
			String orderDate = orderDteA3[0].trim();
			invoice.setOrderDate(orderDate);
			// ===============Delevery No================
			String[] delevaryNoA1 = json.split(delevaryAddr);
			String delevaryNoA2 = delevaryNoA1[1].trim();
			String delevaryNoA3[] = delevaryNoA2.split(delevaryAddr2);
			String deleveryNo = delevaryNoA3[0].trim();
			invoice.setDeliveryNoteNo(deleveryNo);
			// ==========Delevary Address =================

			String[] delevaryAddressA1 = json.split(delevaryAddr3);
			String delevaryAddressA2 = delevaryAddressA1[1].trim()
					.replaceAll("(VAT\\s*\\w*\\s*\\(\\w*\\)\\s*\\w*\\s*\\w*\\:\\s*\\d*\\.\\d*\\s*)", "").trim();
			String delevaryAddressA3[] = delevaryAddressA2.split(currency);
			String delevaryAddressA4 = delevaryAddressA3[0].trim().replaceAll(
					"\\w*\\s*\\w*\\%\\s*\\w*\\.\\w*\\s*\\w*\\.\\w*\\s*\\w*\\.\\w*\\s*\\w*\\s*\\w*\\:\\s*\\w*\\.\\w*",
					"").trim();
			String delevery = delevaryAddressA4.trim()
					.replaceAll("Rate\\s*\\w*\\%\\s*\\w*\\s*\\w*\\s*\\w*\\s*\\w*\\:\\s*\\d*\\.\\d*\\s*", "").trim();
			String deleveryAddress[] = delevery.split("\\n");
			String addressSum = "";
			for (int i = 0; i < deleveryAddress.length; i++) {
				addressSum += " " + deleveryAddress[i].trim();
			}
			invoice.setDeliveryAddress(addressSum.trim());
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(gross);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal.trim());

			String[] orderNoA1 = json.split(orderNo);
			String orderNoA2 = orderNoA1[1].trim();
			String orderNo[] = orderNoA2.split(refNop);
			System.out.println(orderNo);
			invoice.setOrderNo(orderNo[0].trim());
			invoice.setCustomerAddress(addressSum.trim());
			invoice.setCustomerName(costumerName);
			invoice.setSupplierAddress(supplierAddress);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal.trim());
			// =============== Total Address ================
			String[] totalA1 = json.split(netTotalp);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;

			invoice.setGrossTotal(grossTotal.trim());
			// ===============Quality Address ================
			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(invoiceTableA2);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				InvoiceTable invoicetab = new InvoiceTable();
				String sum = "", descrip = "";
				Pattern pattern = Pattern.compile("(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+([\\d\\,]*\\.\\d+)");
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String quentityA5[] = quentityA4[i].split("(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+([\\d\\,]*\\.\\d+)");
					String invoiceTableA1[] = quentityA5[0].trim().split("               ");
					String invoiceTableA2[] = invoiceTableA1[0].split("\\s+");
					descrip += invoiceTableA1[2].trim();
					String price[] = quentityA4[i].split(descrip);
					String priceEst = price[1].replaceAll("(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+([\\d\\,]*\\.\\d+)", "$1");
					invoicetab.setUnitPrice(priceEst.trim());
					String vatEst = price[1].replaceAll("(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+([\\d\\,]*\\.\\d+)", "$1");
					invoicetab.setVatPercentage(vatEst.trim());
					String lineEst = price[1].replaceAll("(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\s+([\\d\\,]*\\.\\d+)", "$1");
					invoicetab.setTotal(lineEst.trim());
					String quentity = invoiceTableA2[0].trim();
					String unite = invoiceTableA2[1].trim();
					String code = invoiceTableA2[2].trim();
					String size = invoiceTableA1[1].trim();
					invoicetab.setSize(size);
					invoicetab.setStockCode(code);
					invoicetab.setQuantity(quentity);
					invoicetab.setUnit(unite);

				} else {
					sum += quentityA4[i].trim();
				}
				String description = descrip.trim() + " " + sum.trim();
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab);
				sum = "";
				descrip = "";
			}
			invoice.setInvoiceTable(invoiceTable);

			DocumentWrapper ob = DocumentWrapper.createWithName(invoice.getInvoiceTitle(), invoice.getInvoiceType());
			ob.setTitle(invoice.getInvoiceTitle());
			ob.setDescription(invoice.getInvoiceDescription());
			ob.setPrefix(invoice.getPrefix());
			ob.setRepoPath(repoPath);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}

		return invoice;
	}

}
