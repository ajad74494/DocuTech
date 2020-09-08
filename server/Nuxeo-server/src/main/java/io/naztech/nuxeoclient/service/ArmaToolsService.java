package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.mozilla.universalchardet.prober.SBCSGroupProber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.jonathanlink.PDFLayoutTextStripper;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * @author masud.ahmed
 */
@Service
public class ArmaToolsService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.armatool}")
	private String folderName;

	@Value("${import.armatool.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.armatool.type}")
	private String nuxeoinvoiceType;

	@Value("${import.armatool.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${armaTools.delByRgx}")
	private String delByRgx;

	@Value("${armaTools.acCodeRgx}")
	private String acCodeRgx;
	@Value("${armaTools.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${armaTools.yourRefRgx}")
	private String yourRefRgx;

	@Value("${armaTools.dateRgx}")
	private String dateRgx;

	@Value("${armaTools.invoiceRgx}")
	private String invoiceRgx;

	@Value("${armaTools.qtyRgx}")
	private String qtyRgx;

	@Value("${armaTools.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${armaTools.tableRgx}")
	private String tableRgx;

	@Value("${armaTools.deliveryRgx}")
	private String deliveryRgx;

	@Value("${armaTools.subTotalRgx}")
	private String subTotalRgx;

	@Value("${armaTools.vatTotalRgx}")
	private String vatTotalRgx;

	@Value("${armaTools.dueTotalRgx}")
	private String dueTotalRgx;

	@Value("${armaTools.pageRgx}")
	private String pageRgx;

	@Value("${armaTools.phoneRgx}")
	private String phoneRgx;

	@Value("${armaTools.faxRgx}")
	private String faxRgx;

	@Value("${armaTools.websiteRgx}")
	private String websiteRgx;

	@Value("${armaTools.emailRgx}")
	private String emailRgx;

	@Value("${armaTools.vatNoRgx}")
	private String vatNoRgx;

	@Value("${armaTools.supplierRgx}")
	private String supplierRgx;

	@Value("${armaTools.tableEndRgx}")
	private String tableEndRgx;

	@Value("${armaTools.descRgx}")
	private String descRgx;

	@Value("${armaTools.desc2Rgx}")
	private String desc2Rgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {

		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		 invoice.setSortName(Constants.ARMATOOL);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;

		// ================= invoice detail =================//
		String accNo = "", invoiceNo = "", refNo = "", invoiceDate = "";
		subData = data.substring(0, data.indexOf(delByRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(acCodeRgx).matcher(value).find()) {
				accNo = value;
				value = value.replaceAll(acCodeRgx, "");
				accNo = accNo.replaceAll(value, "").replaceAll(acCodeRgx, "$1");// not set
				invoice.setAccountNo(accNo);
			} else if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				invoiceNo = value;
				value = value.replaceAll(invoiceNoRgx, "");
				invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");// not set
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(yourRefRgx).matcher(value).find()) {
				refNo = value;
				value = value.replaceAll(yourRefRgx, "");
				refNo = refNo.replaceAll(value, "").replaceAll(yourRefRgx, "$1");// not set
				invoice.setReferenceNumber(refNo);
			} else if (Pattern.compile(dateRgx).matcher(value).find()) {
				invoiceDate = value;
				value = value.replaceAll(dateRgx, "").trim();
				invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(dateRgx, "$1");// not
				invoice.setInvoiceDate(invoiceDate); // set
			}
		}

		// ============== customer_details ==================//
		String customerAddress = "";
		subData = data.substring(data.indexOf(invoiceRgx), data.indexOf(qtyRgx));
		subData = subData.replaceAll(invoiceRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				customerAddress += "," + value;
			}

		}
		customerAddress = customerAddress.replaceAll("^,", "");// not set
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0]);
		invoice.setCustomerAddress(customerAddress);
		// =============== delivery details ================//
		String deliveryAddress = "";
		dataArray = data.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(tableHeaderRgx).matcher(value).find()) {
				flag = true;
			} else if (flag) {
				if (Pattern.compile(tableRgx).matcher(value).find()) {
					break;
				} else {
					if (!value.equals("")) {
						deliveryAddress += "," + value.trim();
					}
				}
			}
		}
		deliveryAddress = deliveryAddress.replaceAll("^,", "").replaceAll(deliveryRgx, "").trim();// not set
		invoice.setDeliveryAddress(deliveryAddress);
		// =============== invoice table amount details ======================//
		/*
		 * netTotal
		 */
		subData = data.substring((int) data.length() / 2);
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(subTotalRgx);
		Matcher matcher = pattern.matcher(subData);
		boolean found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String subTotal = subData.substring(start, end);
		subTotal = subTotal.replaceAll(subTotalRgx, "$1");
		invoice.setNetTotal(subTotal);
		subTotal = subTotal.replaceAll(subTotalRgx, "$1").trim();
		/*
		 * vat total
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatTotalRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatTotal = subData.substring(start, end);
		vatTotal = vatTotal.replaceAll(vatTotalRgx, "$1").trim();
		invoice.setVatTotal(vatTotal);
		/*
		 * due total
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(dueTotalRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String dueTotal = subData.substring(start, end);
		dueTotal = dueTotal.replaceAll(dueTotalRgx, "$1").trim();
		invoice.setDue(dueTotal);
		subData = subData.substring(end);
		subData = subData.replaceAll(pageRgx, "");
		/*
		 * phone
		 */

		start = 0;
		end = 0;
		pattern = Pattern.compile(phoneRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String phone = subData.substring(start, end);
		phone = phone.replaceAll(phoneRgx, "$1").trim();
		invoice.setTelephone(phone);
		subData = subData.replaceAll(phoneRgx, "");
		/*
		 * Fax
		 */

		start = 0;
		end = 0;
		pattern = Pattern.compile(faxRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String fax = subData.substring(start, end);
		fax = fax.replaceAll(faxRgx, "$1").trim();
		invoice.setFax(fax);
		subData = subData.replaceAll(faxRgx, "");
		/*
		 * website
		 */

		start = 0;
		end = 0;
		pattern = Pattern.compile(websiteRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String website = subData.substring(start, end);// not set
		invoice.setWebsite(website);
		subData = subData.replaceAll(websiteRgx, "");
		/*
		 * email
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(emailRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String email = subData.substring(start, end);// not set
		email = email.replaceAll(emailRgx, "$1");
		invoice.setEmail(email);
		subData = subData.replaceAll(emailRgx, "");
		/*
		 * vatReg
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatNoRgx);
		matcher = pattern.matcher(subData);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatReg = subData.substring(start, end);// not set
		vatReg = vatReg.replaceAll(vatNoRgx, "$1");
		invoice.setVatReg(vatReg);
		subData = subData.replaceAll(vatNoRgx, "");
		String supplierAddress = subData;// not set
		dataArray = subData.split(",");
		String supplierName = dataArray[0];
		dataArray[0] = dataArray[0].replaceAll(supplierRgx, "");
		supplierName = supplierName.replaceAll(dataArray[0], "").trim();// not set
		invoice.setSupplierName(supplierName);
		invoice.setSupplierAddress(supplierAddress.trim());
		// =========================== table ===========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", ref = "", cost = "", disc = "", qty = "", net = "", total = "", vat = "";
		int start_indx = 0, end_indx = 0, table_last_idx = 0, blank_Counter = 0;
		boolean startBol = false, endBol = false;
		String tableheader = tableHeaderRgx;
		String table = tableRgx;
		/*
		 * storing every line in array
		 **/
		String[] arr0 = data.split("\\n");
		/*
		 * setting the regex for table search
		 **/
		flag = false;
		int index = 0;
		for (String value : arr0) {
			value = value.trim();
			if (Pattern.compile(tableEndRgx).matcher(value).find()) {
				listStr.add(full_row);
				full_row = "";
				break;
			}

			if (value.matches(tableheader)) {
				flag = true;
			} else if (flag) {
				/*
				 * finding row
				 */
				if (value.matches(table)) {
					if (!full_row.equals("")) {
						listStr.add(full_row);
						full_row = "";
						full_row = value;
					} else {
						full_row = value;
					}
				} else {
					if (!value.equals("")) {
						full_row += "========" + value.trim();
					}
				}
			}
		}

		/*
		 * outsize table operation
		 */

		description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable tableRow = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (!value.equals(extra_string)) {
					if (pattern.matcher(value).find()) {
						qty = value.replaceAll(table, "$1").trim();
						ref = value.replaceAll(table, "$2").trim();
						description = value.replaceAll(table, "$3").trim();
						cost = value.replaceAll(table, "$4").trim();
						disc = value.replaceAll(table, "$5").trim();
						net = value.replaceAll(table, "$6").trim();
						total = value.replaceAll(table, "$7").trim();
						vat = value.replaceAll(table, "$8").trim();
					} else {
						pattern = Pattern.compile("[A-Za-z0-9]+");
						if (pattern.matcher(value).find()) {
							description += " " + value.trim();
						}
					}
				}
				extra_string = value;

			}
			/*
			 * value of full desc
			 */
			/*
			 * value of full desc
			 */
			description = description.trim();
			tableRow.setDescription(description);
			tableList.add(tableRow);
			description = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
