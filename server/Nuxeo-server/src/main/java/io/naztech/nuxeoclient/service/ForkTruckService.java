package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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
public class ForkTruckService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.forktruck}")
	private String folderName;

	@Value("${import.forktruck.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.forktruck.type}")
	private String nuxeoinvoiceType;

	@Value("${import.forktruck.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${fork.invoiceRgx}")
	private String invoiceRgx;

	@Value("${fork.serviceRgx}")
	private String serviceRgx;

	@Value("${fork.faxRgx}")
	private String faxRgx;

	@Value("${fork.emailRgx}")
	private String emailRgx;

	@Value("${fork.customerRgx}")
	private String customerRgx;

	@Value("${fork.descriptionRgx}")
	private String descriptionRgx;

	@Value("${fork.docNoRgx}")
	private String docNoRgx;

	@Value("${fork.taxPointRgx}")
	private String taxPointRgx;

	@Value("${fork.custNdDeliRgx}")
	private String custNdDeliRgx;

	@Value("${fork.vatRegRgx}")
	private String vatRegRgx;

	@Value("${fork.totalNetRgx}")
	private String totalNetRgx;

	@Value("${fork.totalTaxRgx}")
	private String totalTaxRgx;

	@Value("${fork.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${fork.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${fork.tableRgx}")
	private String tableRgx;

	@Value("${fork.tableEndRgx}")
	private String tableEndRgx;

	@Value("${fork.descRgx}")
	private String descRgx;

	@Value("${fork.desc2Rgx}")
	private String desc2Rgx;

	@Value("${fork.wrongRowRgx}")
	private String wrongRowRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;

		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.FORKTRUCK);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// ================== supplier details =======================//
		subData = data.substring(0, data.indexOf(invoiceRgx));
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(serviceRgx);
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

		String phone = subData.substring(start, end);
		phone = phone.replaceAll(serviceRgx, "$1");
		invoice.setTelephone(phone);
		subData = subData.replaceAll(serviceRgx, "");
		/*
		 * fax
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
		fax = fax.replaceAll(faxRgx, "$1");
		invoice.setFax(fax);
		subData = subData.replaceAll(faxRgx, "");
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

		String email = subData.substring(start, end);
		email = email.replaceAll(emailRgx, "$1");
		invoice.setEmail(email);
		/*
		 * supplier address
		 */
		subData = subData.replaceAll(emailRgx, "");
		invoice.setSupplierAddress(subData.trim());
		dataArray = subData.split(",");
		invoice.setSupplierName(dataArray[0].trim());
		// =============== invoice No , invoice date ==================//
		subData = data.substring(data.indexOf(customerRgx), data.indexOf(descriptionRgx));
		/*
		 * invoiceNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(docNoRgx);
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

		String invoiceNo = subData.substring(start, end);
		invoiceNo = invoiceNo.replaceAll(docNoRgx, "$1");
		invoice.setInvoiceNumber(invoiceNo);
		subData = subData.replaceAll(docNoRgx, "");
		/*
		 * invoiceDate
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(taxPointRgx);
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

		String invoiceDate = subData.substring(start, end);
		invoiceDate = invoiceDate.replaceAll(taxPointRgx, "$1");
		invoice.setInvoiceDate(invoiceDate);
		subData = subData.replaceAll(custNdDeliRgx, "");
		// =================== customer and delivery ======================//
		String customer_detail = "", delivery_detail = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("^\\s{10}", "=").trim();
			String[] arr1 = value.split("\\s{7,}");
			customer_detail += "," + arr1[0].trim();
			try {
				delivery_detail += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		customer_detail = customer_detail.replaceAll(",=", ",").replaceAll("\\,{2,}", ",").trim();
		customer_detail = customer_detail.replaceFirst("^,", "");
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0].trim());
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		/*
		 * vatRegNo
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatRegRgx);
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

		String vatRegNo = subData.substring(start, end);
		vatRegNo = vatRegNo.replaceAll(vatRegRgx, "$1");
		invoice.setVatReg(vatRegNo);
		/*
		 * total
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(totalNetRgx);
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

		String total = subData.substring(start, end);
		total = total.replaceAll(totalNetRgx, "$1");
		invoice.setTotal(total);
		/*
		 * vat total
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(totalTaxRgx);
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
		vatTotal = vatTotal.replaceAll(totalTaxRgx, "$1");
		invoice.setVatTotal(vatTotal);
		/*
		 * invoice Total
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceTotalRgx);
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

		String netTotal = subData.substring(start, end);
		netTotal = netTotal.replaceAll(invoiceTotalRgx, "$1");
		invoice.setNetTotal(netTotal);
		// ============================ table ==============================//
		// ======================== table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", disc = "", qty = "", unit = "", totals = "";
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
		boolean flag = false;
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
			InvoiceTable table2 = new InvoiceTable();
			if (Pattern.compile(wrongRowRgx).matcher(row).find()) {
				row = "";
			}
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (!value.equals(extra_string)) {
					if (pattern.matcher(value).find()) {
						description = value.replaceAll(table, "$1");
						qty = value.replaceAll(table, "$2");
						unit = value.replaceAll(table, "$3");
						disc = value.replaceAll(table, "$4");
						totals = value.replaceAll(table, "$5");
						table2.setQuantity(qty);
						table2.setUnit(unit);
						table2.setDiscount(disc);
						table2.setTotal(totals);
					} else {
						pattern = Pattern.compile(descRgx);
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
			description = description.trim();
			table2.setDescription(description);
			tableList.add(table2);
			description = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;

	}
}
