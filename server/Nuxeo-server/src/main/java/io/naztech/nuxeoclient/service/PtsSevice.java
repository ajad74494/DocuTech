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
public class PtsSevice implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.pts}")
	private String folderName;

	@Value("${import.pts.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.pts.type}")
	private String nuxeoinvoiceType;

	@Value("${import.pts.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${pts.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${pts.customerNameRgx}")
	private String customerNameRgx;

	@Value("${pts.additionRgx}")
	private String additionRgx;

	@Value("${pts.phoneFullRgx}")
	private String phoneFullRgx;

	@Value("${pts.telRgx}")
	private String telRgx;

	@Value("${pts.supplierNameRgx}")
	private String supplierNameRgx;

	@Value("${pts.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${pts.accountRefRgx}")
	private String accountRefRgx;

	@Value("${pts.vatNoRgx}")
	private String vatNoRgx;

	@Value("${pts.goodsTotalRgx}")
	private String goodsTotalRgx;

	@Value("${pts.vatTotalRgx}")
	private String vatTotalRgx;

	@Value("${pts.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${pts.orderNoRgx}")
	private String orderNoRgx;

	@Value("${pts.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${pts.tableRgx}")
	private String tableRgx;

	@Value("${pts.tableEndRgx}")
	private String tableEndRgx;

	@Value("${pts.descRgx}")
	private String descRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		 invoice.setSortName(Constants.PTS);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String subDataXtra = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		String[] multiPageData = null;
		/*
		 * invoice no
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(invoiceNoRgx);
		Matcher matcher = pattern.matcher(data);
		boolean found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}

		String invoiceNo = data.substring(start, end);
		invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1");
		invoice.setInvoiceNumber(invoiceNo);
		// ============== customer and supplier ================== //
		String customerDetails = "", supplierDetails = "";
		subData = data.substring(0, data.indexOf(customerNameRgx));
		subData = subData.replaceAll(invoiceNoRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			value = value.replaceAll("^\\s{10}", "=").trim();
			String[] arr1 = value.split("\\s{7,}");
			customerDetails += "," + arr1[0].trim();
			try {
				supplierDetails += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		customerDetails = customerDetails.replaceAll("=", ",").replaceAll("\\,{2,}", ",").trim();
		customerDetails = customerDetails.replaceAll("^,", "");
		dataArray = customerDetails.split(",");
		invoice.setCustomerName(dataArray[0]);
		supplierDetails = supplierDetails.replaceAll(",=", ",").replaceAll("^,", "");
		supplierDetails = supplierDetails.substring(0, supplierDetails.indexOf(additionRgx));
		start = 0;
		end = 0;
		pattern = Pattern.compile(phoneFullRgx);
		matcher = pattern.matcher(supplierDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String phone = supplierDetails.substring(start, end);
		phone = phone.replaceAll(phoneFullRgx, "$1");
		invoice.setTelephone(phone);
		supplierDetails = supplierDetails.substring(0, supplierDetails.indexOf(telRgx));
		invoice.setSupplierAddress(supplierDetails);
		invoice.setSupplierName(supplierNameRgx);
		/*
		 * invoice date
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceDateRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String date = data.substring(start, end);
		date = date.replaceAll(invoiceDateRgx, "$1");
		invoice.setInvoiceDate(date);
		/*
		 * account ref
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(accountRefRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String accRef = data.substring(start, end);
		accRef = accRef.replaceAll(accountRefRgx, "$1");
		invoice.setReferenceNumber(accRef);
		/*
		 * vat no
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatNoRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatNo = data.substring(start, end);
		vatNo = vatNo.replaceAll(vatNoRgx, "$1");
		invoice.setVatNo(vatNo);
		/*
		 * goods total
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(goodsTotalRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String goodsTotal = data.substring(start, end);
		goodsTotal = goodsTotal.replaceAll(goodsTotalRgx, "$1");// not set
		/*
		 * vat total
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatTotalRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatTotal = data.substring(start, end);
		vatTotal = vatTotal.replaceAll(vatTotalRgx, "$1");// not set
		invoice.setVatTotal(vatTotal);
		/*
		 * invoice total
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceTotalRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String invoiceTotal = data.substring(start, end);
		invoiceTotal = invoiceTotal.replaceAll(invoiceTotalRgx, "$1");// not set
		/*
		 * 
		 * */
		start = 0;
		end = 0;
		pattern = Pattern.compile(orderNoRgx);
		matcher = pattern.matcher(data);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String orderNo = data.substring(start, end);
		orderNo = orderNo.replaceAll(orderNoRgx, "$1");// not set
		invoice.setOrderNo(orderNo);
		// ================== table ===================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String qty = "", description = "", goods = "", vat = "", rate = "";
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
		String checkTotal = "";
		int index = 0;
		for (String value : arr0) {
			value = value.trim();
			if (Pattern.compile(orderNoRgx).matcher(value).find()) {
				value = "";
			}
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
		 * out size of loop
		 */
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable tableRow = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (!value.equals(extra_string)) {
					if (pattern.matcher(value).find()) {
						qty = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						goods = value.replaceAll(table, "$3");
						vat = value.replaceAll(table, "$4");
						rate = value.replaceAll(table, "$5");
						/*
						 * setting values
						 */
						tableRow.setQuantity(qty);
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
			tableRow.setDescription(description);
			tableList.add(tableRow);
			description = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
