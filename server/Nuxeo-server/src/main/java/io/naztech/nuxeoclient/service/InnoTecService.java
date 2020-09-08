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
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * @author masud.ahmed
 */
@Service
public class InnoTecService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.innotec}")
	private String folderName;

	@Value("${import.innotec.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.innotec.type}")
	private String nuxeoinvoiceType;

	@Value("${import.innotec.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${innotech.invoiceToRgx}")
	private String invoiceToRgx;

	@Value("${innotech.telRgx}")
	private String telRgx;

	@Value("${innotech.emailRgx}")
	private String emailRgx;

	@Value("${innotech.invoiceRgx}")
	private String invoiceRgx;

	@Value("${innotech.vatRegRgx}")
	private String vatRegRgx;

	@Value("${innotech.accountprefixRgx}")
	private String accountprefixRgx;

	@Value("${innotech.invToDeliveryToRgx}")
	private String invToDeliveryToRgx;

	@Value("${innotech.invoiceDetailsRgx}")
	private String invoiceDetailsRgx;

	@Value("${innotech.accountNoRgx}")
	private String accountNoRgx;

	@Value("${innotech.RegNoRgx}")
	private String RegNoRgx;

	@Value("${innotech.invoiceTableTotalRgx}")
	private String invoiceTableTotalRgx;

	@Value("${innotech.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${innotech.tableRgx}")
	private String tableRgx;

	@Value("${innotech.descRgx}")
	private String descRgx;

	@Value("${innotech.totalGoodsRgx}")
	private String totalGoodsRgx;


	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		try {

			String data = pdfStr;
			Invoice invoice = new Invoice();
			ArrayList<InvoiceTable> tableList = new ArrayList<>();
			// invoice.setSortName(Constants.VR);
			// invoice.setInvoiceTitel(nuxeoinvoiceName);
			// invoice.setInvoiceDescription(desc);
			// invoice.setPrefix(prefix);
			// invoice.setInvoiceType(nuxeoinvoiceType);
			String subData = null;
			String[] dataArray = null;
			String[] dataArrayCopy = null;
			// ====================== supplier details =======================//
			subData = data.substring(0, data.indexOf(invoiceToRgx));
			/*
			 * phone
			 */
			int start = 0;
			int end = 0;
			Pattern pattern = Pattern.compile(telRgx);
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
			phone = phone.replaceAll(telRgx, "$1");
			invoice.setTelephone(phone);
			subData = subData.replaceAll(telRgx, "");
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
			subData = subData.replaceAll(emailRgx, "");
			subData = subData.replaceAll(invoiceRgx, "");
			/*
			 * supplier details
			 */
			String vatReg = "", supplierAddress = "";
			dataArray = subData.split("\\n");
			for (String value : dataArray) {
				value = value.trim();
				if (Pattern.compile(vatRegRgx).matcher(value).find()) {
					vatReg = value;
					value = value.replaceAll(vatRegRgx, "");
					vatReg = vatReg.replaceAll(value, "").replaceAll(vatRegRgx, "$1");// not // set
					invoice.setVatReg(vatReg);
				}
				if (!value.equals("")) {
					supplierAddress += "," + value.trim();
				}

			}
			supplierAddress = supplierAddress.replaceAll("^,", "");// not set
			dataArray = supplierAddress.split(",");
			String supplierName = dataArray[0];// not set
			invoice.setSupplierAddress(supplierAddress);
			invoice.setSupplierName(supplierName);
			// ================== customer details ==================//
			subData = data.substring(data.indexOf(invoiceToRgx), data.indexOf(accountprefixRgx));
			subData = subData.replaceAll(invToDeliveryToRgx, "");
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
			customer_detail = customer_detail.replaceAll(",=", ",").replaceAll("\\,{2,}", "").trim();
			dataArray = customer_detail.split(",");
			invoice.setCustomerName(dataArray[0]);
			delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
			invoice.setCustomerAddress(customer_detail);
			invoice.setDeliveryAddress(delivery_detail);
			// =================== invoice details ================//
			String orderNo = "", deliveryDate = "", refNo, invoiceDate = "", invoiceNo = "", regNo = "";
			String invoiceDetailRgx = invoiceDetailsRgx;
			dataArray = data.split("\\n");
			for (String value : dataArray) {
				value = value.trim();
				if (Pattern.compile(invoiceDetailRgx).matcher(value).find()) {
					orderNo = value.replaceAll(invoiceDetailRgx, "$3");
					deliveryDate = value.replaceAll(invoiceDetailRgx, "$4");
					refNo = value.replaceAll(invoiceDetailRgx, "$5");
					invoiceDate = value.replaceAll(invoiceDetailRgx, "$6");
					invoiceNo = value.replaceAll(invoiceDetailRgx, "$7");
					invoice.setOrderNo(orderNo);
					invoice.setDeliveryDate(deliveryDate);
					invoice.setReferenceNumber(refNo);
					invoice.setInvoiceDate(invoiceDate);
					invoice.setInvoiceNumber(invoiceNo);
				}
			}
			/*
			 * Account No
			 */
			subData = data.substring(data.length() / 2);
			start = 0;
			end = 0;
			pattern = Pattern.compile(accountNoRgx);
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
			String accNo = subData.substring(start, end);
			accNo = accNo.replaceAll(accountNoRgx, "$1");
			invoice.setAccountNo(accNo);
			/*
			 * Reg No
			 */
			subData = data.substring(data.length() / 2);
			start = 0;
			end = 0;
			pattern = Pattern.compile(RegNoRgx);
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
			regNo = subData.substring(start, end);
			regNo = regNo.replaceAll(RegNoRgx, "$1");
			invoice.setRegNo(regNo);
			/*
			 * invoice table total
			 */
			subData = subData.substring(subData.indexOf(totalGoodsRgx));
			start = 0;
			end = 0;
			pattern = Pattern.compile(invoiceTableTotalRgx);
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
			subData = subData.substring(start, end);
			String netTotal = subData.replaceAll(invoiceTableTotalRgx, "$1");
			String vatTotal = subData.replaceAll(invoiceTableTotalRgx, "$2");
			String invoiceTotal = subData.replaceAll(invoiceTableTotalRgx, "$3");
			invoice.setNetTotal(netTotal);
			invoice.setVatTotal(vatTotal);
			invoice.setGrossTotal(invoiceTotal);
			// ======================== table ========================//
			ArrayList<String> listStr = new ArrayList<>();
			String full_row = "";
			String description = "", stockCode = "", price = "", disc = "", qty = "", unit = "", netPrc = "",
					total = "";
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
			stockCode = full_row.replaceAll(table, "$1");
			description = full_row.replaceAll(table, "$2");
			qty = full_row.replaceAll(table, "$3");
			price = full_row.replaceAll(table, "$4");
			unit = full_row.replaceAll(table, "$5");
			disc = full_row.replaceAll(table, "$6");
			netPrc = full_row.replaceAll(table, "$7");
			total = full_row.replaceAll(table, "$8");
			lastRow.setStockCode(stockCode);
			lastRow.setQuantity(qty);
			lastRow.setPrice(price);
			lastRow.setUnit(unit);
			lastRow.setDiscount(disc);
			lastRow.setNetAmount(netPrc);
			lastRow.setTotal(total);
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
					if (pattern.matcher(value).find()) {
						stockCode = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						qty = value.replaceAll(table, "$3");
						price = value.replaceAll(table, "$4");
						unit = value.replaceAll(table, "$5");
						disc = value.replaceAll(table, "$6");
						netPrc = value.replaceAll(table, "$7");
						total = value.replaceAll(table, "$8");
						lastRow.setStockCode(stockCode);
						lastRow.setQuantity(qty);
						lastRow.setPrice(price);
						lastRow.setUnit(unit);
						lastRow.setDiscount(disc);
						lastRow.setNetAmount(netPrc);
						lastRow.setTotal(total);
					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							description += " " + value.trim();
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
			return invoice;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return invoice;

	}
}
