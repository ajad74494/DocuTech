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
public class AdtrakServiec implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.acsupply}")
	private String folderName;

	@Value("${import.adtrak.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.adtrak.type}")
	private String nuxeoinvoiceType;

	@Value("${import.adtrak.prefix}")
	private String prefix;

	@Value("${import.nuxeo.adtrak.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${adtrak.billRgx}")
	private String billRgx;

	@Value("${adtrak.supplierRgx}")
	private String supplierRgx;

	@Value("${adtrak.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${adtrak.itemNdDescRgx}")
	private String itemNdDescRgx;

	@Value("${adtrak.billToRgx}")
	private String billToRgx;

	@Value("${adtrak.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${adtrak.termsRgx}")
	private String termsRgx;

	@Value("${adtrak.dueDateRgx}")
	private String dueDateRgx;

	@Value("${adtrak.totalNetRgx}")
	private String totalNetRgx;

	@Value("${adtrak.totalRgx}")
	private String totalRgx;

	@Value("${adtrak.dueBalanceRgx}")
	private String dueBalanceRgx;

	@Value("${adtrak.vatRgx}")
	private String vatRgx;

	@Value("${adtrak.tableRgx}")
	private String tableRgx;

	@Value("${adtrak.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${adtrak.tableVatRgx}")
	private String tableVatRgx;

	@Value("${adtrak.tableEndRgx}")
	private String tableEndRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		try {
			String data = pdfStr;
			Invoice invoice = new Invoice();
			ArrayList<InvoiceTable> tableList = new ArrayList<>();
			invoice.setSortName(Constants.ADTRAKMEDIALTD);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String subData = null;
			String[] dataArray = null;
			String[] dataArrayCopy = null;
			// =================== Supplier Details ======================//
			subData = data.substring(0, data.indexOf(billRgx));
			subData = subData.replaceAll(supplierRgx, "");
			/*
			 * invoice no
			 */
			int start = 0;
			int end = 0;
			Pattern pattern = Pattern.compile(invoiceNoRgx);
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
			String invoiceNo = subData.substring(start, end);
			invoice.setInvoiceNumber(invoiceNo);
			subData = subData.replaceAll(invoiceNo, "");
			/**
			 * supplier address
			 */
			String supplierAddress = "";
			dataArray = subData.split("\\n");
			for (String value : dataArray) {
				value = value.trim();
				if (!value.equals("")) {
					supplierAddress += "," + value;
				}
			}
			supplierAddress = supplierAddress.replaceAll("^,", "");
			invoice.setSupplierAddress(supplierAddress);
			dataArray = supplierAddress.split(",");
			invoice.setSupplierName(dataArray[0]);

			// ====================== customer details ===============//
			String customerAddress = "", customerName = "", invoiceDate = "", dueDate = "";
			subData = data.substring(data.indexOf(billRgx), data.indexOf(itemNdDescRgx));
			subData = subData.replaceAll(billToRgx, "");
			dataArray = subData.split("\\n");
			for (String value : dataArray) {
				value = value.trim();
				if (!value.equals("")) {
					if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
						invoiceDate = value;
						value = value.replaceAll(invoiceDateRgx, "");
						invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(invoiceDateRgx, "$1");
						invoice.setInvoiceDate(invoiceDate);
					} else if (Pattern.compile(termsRgx).matcher(value).find()) {
						value = value.replaceAll(termsRgx, "");
					} else if (Pattern.compile(dueDateRgx).matcher(value).find()) {
						dueDate = value;
						value = value.replaceAll(dueDateRgx, "");
						dueDate = dueDate.replaceAll(value, "").replaceAll(dueDateRgx, "$1");// not set

					}
					customerAddress += "," + value.trim();
				}
			}
			customerAddress = customerAddress.replaceAll("^,", "");
			dataArray = customerAddress.split("\\n");
			invoice.setCustomerName(dataArray[0]);
			invoice.setCustomerAddress(customerAddress);
			// ================ invoice table total details ==================//
			String total = "", netTotal = "", dueTotal = "", vatReg = "";
			dataArray = data.split("\\n");
			for (String value : dataArray) {
				value = value.trim();
				if (Pattern.compile(totalNetRgx).matcher(value).find()) {
					netTotal = value;
					value = value.replaceAll(totalNetRgx, "");
					netTotal = netTotal.replaceAll(value, "").replaceAll(totalNetRgx, "$1").trim();
					invoice.setNetTotal(netTotal);
				} else if (Pattern.compile(totalRgx).matcher(value).find()) {
					total = value;
					value = value.replaceAll(totalRgx, "");
					total = total.replaceAll(value, "").replaceAll(totalRgx, "$1").trim();
					invoice.setTotal(total);
				} else if (Pattern.compile(dueBalanceRgx).matcher(value).find()) {
					dueTotal = value;
					value = value.replaceAll(dueBalanceRgx, "");
					dueTotal = dueTotal.replaceAll(value, "").replaceAll(dueBalanceRgx, "$1").trim();
					invoice.setDue(dueTotal);
				} else if (Pattern.compile(vatRgx).matcher(value).find()) {
					vatReg = value;
					value = value.replaceAll(vatRgx, "");
					vatReg = vatReg.replaceAll(value, "").replaceAll(vatRgx, "$1").trim();
					invoice.setDue(vatReg);
				}
			}

			// =========================== table ===========================//
			ArrayList<String> listStr = new ArrayList<>();
			String full_row = "";
			String description = "", totalAmount = "", netAmount = "", vat = "", qty = "";
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
						table_last_idx = index;
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
			description = full_row.replaceAll(table, "$1");
			qty = full_row.replaceAll(table, "$2");
			netAmount = full_row.replaceAll(table, "$3");
			vat = full_row.replaceAll(table, "$4");
			totalAmount = full_row.replaceAll(table, "$5");

			/*
			 * last rows extra lines
			 */
			for (int i = start_indx + 1; i < table_last_idx; i++) {
				arr0[i] = arr0[i].trim();
				pattern = Pattern.compile("[A-Za-z]+");
				if (pattern.matcher(arr0[i]).find()) {
					description += " " + arr0[i].trim();
				} else if (Pattern.compile(tableVatRgx).matcher(arr0[i]).find()) {
					vat += " " + arr0[i];
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
					if (pattern.matcher(value).find()) {
						description = value.replaceAll(table, "$1");
						qty = value.replaceAll(table, "$2");
						netAmount = value.replaceAll(table, "$3");
						vat = value.replaceAll(table, "$4");
						totalAmount = value.replaceAll(table, "$5");
					} else {
						pattern = Pattern.compile("[A-Za-z0-9]+");
						if (pattern.matcher(value).find()) {
							description += " " + value.trim();
						} else if (Pattern.compile(tableVatRgx).matcher(value).find()) {
							vat += " " + value.trim();
						}
					}

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

		} catch (Exception e) {
			// TODO: handle exception
		}
		return invoice;
	}
}
