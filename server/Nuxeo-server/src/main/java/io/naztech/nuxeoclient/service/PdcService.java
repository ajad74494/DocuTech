package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * @author masud.ahmed
 */
@Service
public class PdcService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.pdc}")
	private String folderName;

	@Value("${import.pdc.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.pdc.type}")
	private String nuxeoinvoiceType;

	@Value("${import.pdc.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${pdc.telRgx}")
	private String telRgx;

	@Value("${pdc.telFullRgx}")
	private String telFullRgx;

	@Value("${pdc.emailRgx}")
	private String emailRgx;

	@Value("${pdc.vatRegRgx}")
	private String vatRegRgx;

	@Value("${pdc.invoiceRgx}")
	private String invoiceRgx;

	@Value("${pdc.qtyRgx}")
	private String qtyRgx;

	@Value("${pdc.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${pdc.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${pdc.orderNoRgx}")
	private String orderNoRgx;

	@Value("${pdc.accountRefRgx}")
	private String accountRefRgx;

	@Value("${pdc.dueDateRgx}")
	private String dueDateRgx;

	@Value("${pdc.totalNetRgx}")
	private String totalNetRgx;

	@Value("${pdc.totalTaxRgx}")
	private String totalTaxRgx;

	@Value("${pdc.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${pdc.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${pdc.tableRgx}")
	private String tableRgx;

	@Value("${pdc.descRgx}")
	private String descRgx;

	@Value("${pdc.tableEndRgx}")
	private String tableEndRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.PDC);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// =============== supplier detail =======================//
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(telRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				dataArrayCopy = value.split("\\s{7}");
				supplierAddress += "," + dataArrayCopy[0];
			}

		}
		supplierAddress = supplierAddress.replaceAll(",=", ",").replaceAll("^,", "");
		dataArray = supplierAddress.split(",");
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(dataArray[0]);
		/*
		 * phone
		 */
		subData = data.substring(0, (int) data.length() / 2);
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(telFullRgx);
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
		phone = phone.replaceAll(telFullRgx, "$1");
		invoice.setTelephone(phone);
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
		 * vatReg
		 */
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

		String vatReg = subData.substring(start, end);
		vatReg = vatReg.replaceAll(vatRegRgx, "$1");
		invoice.setVatReg(vatReg);
		// ============== customer detail ================//
		subData = data.substring(data.indexOf(invoiceRgx), data.indexOf(qtyRgx));
		subData = subData.replaceAll(invoiceRgx, "");
		/*
		 * invoiceNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceNoRgx);
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
		invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1");
		invoice.setInvoiceNumber(invoiceNo);
		subData = subData.replaceAll(invoiceNoRgx, "");
		/*
		 * invoiceDate
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceDateRgx);
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
		invoiceDate = invoiceDate.replaceAll(invoiceDateRgx, "$1");
		invoice.setInvoiceDate(invoiceDate);
		subData = subData.replaceAll(invoiceDateRgx, "");
		/*
		 * orderNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(orderNoRgx);
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
		String orderNo = subData.substring(start, end);
		orderNo = orderNo.replaceAll(orderNoRgx, "$1");
		invoice.setOrderNo(orderNo);
		subData = subData.replaceAll(orderNoRgx, "");
		/*
		 * RefNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(accountRefRgx);
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
		String refNo = subData.substring(start, end);
		refNo = refNo.replaceAll(accountRefRgx, "$1");
		invoice.setReferenceNumber(refNo);
		subData = subData.replaceAll(accountRefRgx, "");
		/*
		 * due Date
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(dueDateRgx);
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
		String dueDate = subData.substring(start, end);
		dueDate = dueDate.replaceAll(dueDateRgx, "$1");
		invoice.setDueDate(dueDate);
		subData = subData.replaceAll(dueDateRgx, "");
		/*
		 * customer address
		 */
		String customerAddress = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				customerAddress += "," + value;
			}
		}

		customerAddress = customerAddress.replaceAll("^,", "");
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0]);
		invoice.setCustomerAddress(customerAddress);
		// ================== invoice table totals =========================//
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
		invoice.setNetTotal(total);
		/*
		 * vat total
		 */
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
		 * invoice total
		 */
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
		String invoiceTotal = subData.substring(start, end);
		invoiceTotal = invoiceTotal.replaceAll(invoiceTotalRgx, "$1");
		invoice.setGrossTotal(invoiceTotal);
		// ======================== table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String qty = "", code = "", description = "", vat = "", priceUnit = "", netAmount = "";
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
			if (value.matches(tableheader)) {
				flag = true;
			} else if (flag) {
				if (value.equals("")) {
					blank_Counter++;
				} else {
					blank_Counter = 0;
				}

				if (blank_Counter == 5) {
					table_last_idx = index;
					break;
				} else if (Pattern.compile(tableEndRgx).matcher(value).find()) {
					break;
				}

				/*
				 * finding row
				 */
				if (value.matches(table)) {
					start_indx = index;
					startBol = true;
				} else {
					if (startBol) {
						pattern = Pattern.compile("[A-Za-z0-9]");
						matcher = pattern.matcher(arr0[index + 1]);
						if (!arr0[index + 1].equals("")) {
							pattern = Pattern.compile(table);
							matcher = pattern.matcher(arr0[index + 1].trim());
							if (matcher.find()) {
								endBol = true;
								end_indx = index;
							} else {
								endBol = false;
							}
						}

					}
				}
			}

			/*
			 * collection full row data
			 */
			if (startBol && endBol) {
				if (start_indx < end_indx) {
					for (int i = start_indx; i <= end_indx; i++) {

						arr0[i] = arr0[i].trim();
						full_row += arr0[i].trim() + "=====";
					}

				}
				listStr.add(full_row);
				full_row = "";
			} else if (startBol && !endBol) {
				if (start_indx > 0) {
					arr0[start_indx] = arr0[start_indx].trim();
					full_row += arr0[start_indx].trim() + "=====";

				}
				listStr.add(full_row);
				full_row = "";
			}
			index++;
		}
		/*
		 * outsize table operation
		 */
		/*
		 * finding last row of table row full
		 */
		InvoiceTable lastRow = new InvoiceTable();
		dataArray = null;
		full_row = "";
		full_row = arr0[start_indx].trim();
		qty = full_row.replaceAll(table, "$1");
		code = full_row.replaceAll(table, "$2");
		description = full_row.replaceAll(table, "$3");
		priceUnit = full_row.replaceAll(table, "$4");
		netAmount = full_row.replaceAll(table, "$5");
		vat = full_row.replaceAll(table, "$5");
		lastRow.setProductCode(code);
		lastRow.setQuantity(qty);
		lastRow.setUnitPrice(priceUnit);
		lastRow.setTotal(netAmount);
		lastRow.setVatAmount(vat);
		/*
		 * last rows extra lines
		 */
		for (int i = start_indx + 1; i < table_last_idx; i++) {
			arr0[i] = arr0[i].trim();
			pattern = Pattern.compile("[A-Za-z]+");
			if (pattern.matcher(arr0[i]).find()) {
				description += " " + arr0[i].trim();
			}
		}
		/*
		 * value of desc
		 */
		description = description;
		lastRow.setDescription(description);
		description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (!value.equals(extra_string)) {
					if (pattern.matcher(value).find()) {
						qty = value.replaceAll(table, "$1");
						code = value.replaceAll(table, "$2");
						description = value.replaceAll(table, "$3");
						priceUnit = value.replaceAll(table, "$4");
						netAmount = value.replaceAll(table, "$5");
						vat = value.replaceAll(table, "$5");
						table2.setProductCode(code);
						table2.setQuantity(qty);
						table2.setUnitPrice(priceUnit);
						table2.setTotal(netAmount);
						table2.setVatAmount(vat);
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
		tableList.remove(tableList.size() - 1);
		tableList.add(lastRow);
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
