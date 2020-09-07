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
 * @since 2020-07-19
 *
 */
@Service
public class AllBeesService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HarrisonService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${allBees.invoiceNo1}")
	private String invoiceNo1;

	@Value("${allBees.vatNo}")
	private String vatNo;

	@Value("${allBees.invoiceNo}")
	private String invoiceNo;

	@Value("${allBees.invoiceDte}")
	private String invoiceDte;

	@Value("${allBees.web}")
	private String web;

	@Value("${allBees.email}")
	private String email;

	@Value("${allBees.invoiceAddress}")
	private String invoiceAddress;

	@Value("${allBees.phone}")
	private String phone;

	@Value("${allBees.netTotalC1}")
	private String netTotalC1;

	@Value("${allBees.faxNo}")
	private String faxNo;

	@Value("${allBees.accountNo}")
	private String accountNo;

	@Value("${allBees.delevaryAddress}")
	private String delevaryAddress;

	@Value("${allBees.netTotal}")
	private String netTotal;

	@Value("${allBees.vatTotal}")
	private String vatTotal;

	@Value("${allBees.grossTotalC1}")
	private String grossTotalC1;

	@Value("${allBees.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${allBees.delevaryAddr}")
	private String delevaryAddr;

	@Value("${allBees.delevaryAddr4}")
	private String delevaryAddr4;

	@Value("${allBees.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${allBees.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${allBees.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${allBees.delevaryAddr4}")
	private String delevaryAdd4r;

	@Value("${allBees.gross}")
	private String gross;

	@Value("${allBees.invoiceTableA1}")
	private String invoiceTableA1;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		try {

			List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();
			String json = pdfStr;
//			invoice.setSortName(Constants.b);
//			invoice.setInvoiceTitel(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			for (int i = 1; i < json.length(); i++) {
				// ============ INvoice =====================
				String[] invData = json.split(invoiceNo);
				String inv = null;
				for (int kk = 0; kk < invData.length; kk++) {
					inv = invData[i].trim();
				}
				String invoices[] = inv.split("[a-zA-Z]");
				String invoice1 = invoices[0].trim();
				invoice.setInvoiceNumber(invoice1);
				// ============ INvoice Date=====================
				String[] invoiceDatedata = json.split(invoiceDte);
				String invvoiceDate = null;
				for (int kk = 0; kk < invoiceDatedata.length; kk++) {
					invvoiceDate = invoiceDatedata[i].trim();
				}
				String invDate[] = invvoiceDate.split("[\\D]{8}[a-zA-Z]");
				String invoiceDate1 = invDate[0].trim();
				invoice.setInvoiceDate(invoiceDate1);
				// ==================Account Number =======================

				String[] accountA1 = json.split(accountNo);
				String accountA2 = null;
				for (int kk = 0; kk < accountA1.length; kk++) {
					accountA2 = accountA1[i].trim();
				}
				String accountA3[] = accountA2.split("[\\D]{3}[a-zA-Z]");
				String account = accountA3[0].trim();
				invoice.setAccountNo(account);
				// ========== Website ==================

				String webSiteA1[] = json.split(web);
				String webSiteA2[] = webSiteA1[i].split(email);
				String webSite = webSiteA2[0].trim();
				invoice.setWebsite(webSite);
				// ===============costomerAddress ================

				try {

					String[] invoiceaddr = json.split(invoiceAddress);
					String invadd = null;
					for (int kk = 0; kk < invoiceaddr.length; kk++) {
						invadd = invoiceaddr[i].trim();
					}
					String invaddress[] = invadd.split(webSite);
					String invoiceAddress = invaddress[0].trim();
					invoice.setSupplierAddress(invoiceAddress);
				} catch (Exception e) {
					log.error(e.toString());
				}
				// ===============Delivery Address ================
				String[] deleveryadd = json.split(delevaryAddress);
				String inv_delevery_add = deleveryadd[i].trim();

				String deleveryaddress[] = inv_delevery_add.split(vatNo);
				String deleveryAddress = deleveryaddress[0].replaceAll(invoiceNo1, "").replaceAll(delevaryAddr4, "")
						.replaceAll(delevaryAddr, "")

						.replaceAll(delevaryAddr1, "").replaceAll(delevaryAddr2, "").replaceAll(delevaryAddr3, "")
						.trim();
				String delevery = deleveryAddress.replaceAll(invoiceNo + invoice1, "")
						.replaceAll(accountNo + account, "").replaceAll(invoiceDte + invoiceDate1, "");
				invoice.setDeliveryAddress(delevery);

				invoice.setCustomerAddress(
						delevaryAddr4 + delevaryAddr + delevaryAddr1 + delevaryAddr2 + delevaryAddr3);
				invoice.setCustomerName(delevaryAddr4);
				// ==============Telephone no ===========

				String invoice_telephone[] = json.split(phone);
				String rep_telephone[] = invoice_telephone[i].split("[a-zA-Z]");
				String phone = rep_telephone[0].trim();
				invoice.setTelephone(phone);
				// ==============Email no ===========
				String invoiceEmail[] = json.split(email);
				String invoice_Email[] = invoiceEmail[i].split(netTotalC1);
				String email = invoice_Email[0].trim();
				invoice.setEmail(email);

				// ==============Fax no ===========
				String faxNoA1[] = json.split(faxNo);
				String faxNoA2[] = faxNoA1[i].split("[a-zA-Z]");
				String fax = faxNoA2[0].trim();
				invoice.setFax(fax);

				// ===============Net Total ================
				String[] invNetTotal = json.split(netTotal);
				String invNet = invNetTotal[i].trim();
				String fff[] = invNet.split("[a-zA-Z]");
				String netTotal = fff[0].trim();
				invoice.setNetTotal(netTotal);
				// ===============Vat Total ================
				String[] vatTotalInvoice = json.split(vatTotal);
				String invVat = vatTotalInvoice[i].trim();
				String vat_Total_Invoice[] = invVat.split("[a-zA-Z]");
				String invoiceVat = vat_Total_Invoice[0].replaceAll("[0-9]*%", "").trim();
				String vatTotal = invoiceVat.replace("()", "").trim();
				invoice.setVatNo(vatTotal);
				// ===============Net Total Address ================
				String[] netTotalInv = json.split(grossTotalC1);
				String invNetTo = netTotalInv[i].trim();
				String invoiceNetTotal[] = invNetTo.split("[a-zA-Z]");
				String grossTotal = invoiceNetTotal[0].replaceAll("£", "").trim().replace("()", "").trim();
				invoice.setGrossTotal(grossTotal);
				Pattern.compile("((\\d*\\.\\d*)\\s*(\\d*\\.\\d*)\\s*(\\d*\\.\\d*)\\s*(\\d*\\.\\d*))");
				// ============== Invoice Table ===================
				String invtableA1[] = json.split(invoiceTableA1);
				String invtableA2[] = invtableA1[i].split(invoiceTableA2);

				String invtableA4[] = invtableA2[0].trim().split("\\n");

				String description = "", dis = "", netAmount = "", total = "";
				Pattern patternreg = Pattern.compile(gross);
				for (int j = 0; j < invtableA4.length - 1; j++) {
					try {
						if (patternreg.matcher(invtableA4[j].trim()).find()) {
							try {
								String invTab[] = invtableA4[j].trim().split("   ");
								invTab[0].trim();
								String code = invTab[1].trim();
								String invTabA1[] = invtableA4[j].trim().split(code);
								String invTabDescription[] = invTabA1[1].trim().split(gross);
								description = invTabDescription[0];
								String[] listA1 = invtableA4[j].split(description);
								String[] listA2 = listA1[1].split("    ");

								listA2[1].replaceAll(gross, "$1");
								dis = listA1[1].replaceAll(gross, "$2");
								netAmount = listA1[1].replaceAll(gross, "$3");
								total = listA1[1].replaceAll(gross, "$4");

							} catch (Exception e) {
								log.error(e.toString());
							}
						} else {
							description = description + " " + invtableA4[j].trim();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					InvoiceTable invoiceTable = new InvoiceTable();
					invoiceTable.setDescription(description);
					invoiceTable.setDiscount(dis);
					invoiceTable.setNetAmount(netAmount);
					invoiceTable.setTotal(total);
					invoiceTab.add(invoiceTable);
				}
				invoice.setInvoiceTable(invoiceTab);
			}

		} catch (Exception e) {
			log.info(e.getLocalizedMessage().toString());
		}
		return invoice;
	}

}
