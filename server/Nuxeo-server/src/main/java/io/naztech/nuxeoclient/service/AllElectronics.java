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
public class AllElectronics implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.allelectrics}")
	private String folderName;

	@Value("${import.allelectrics.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.allelectrics.type}")
	private String nuxeoinvoiceType;

	@Value("${import.allelectrics.prefix}")
	private String prefix;

	@Value("${import.nuxeo.allelectrics.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${allElectro.deliveryAddressRgx}")
	private String deliveryAddressRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String invoiceRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String companyRegRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String vatRegRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String phoneRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String faxRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String emailRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String webRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String customerRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String cusOrdNoRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String invoiceNoRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String dateRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String accountNoRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String totalNetRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String vatTotalRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String tableRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String tableHeaderRgx;

	@Value("${allElectro.deliveryAddressRgx}")
	private String totalRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		InvoiceTable invoiceTable = new InvoiceTable();

		 invoice.setSortName(Constants.ALLELECTRICS);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		/*
		 * supplier detail
		 */
		subData = data.substring(0, data.indexOf(deliveryAddressRgx));
		subData = subData.replaceAll(invoiceRgx, "");
		/*
		 * regNo
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(companyRegRgx);
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
		String regNo = subData.substring(start, end);
		regNo = regNo.replaceAll(companyRegRgx, "$1");
		invoice.setRegNo(regNo);
		subData = subData.replaceAll(companyRegRgx, "");
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
		subData = subData.replaceAll(vatRegRgx, "");
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
		phone = phone.replaceAll(phoneRgx, "$1");
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
		subData = subData.replaceAll(emailRgx, "");
		/*
		 * web
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(webRgx);
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
		String web = subData.substring(start, end);
		web = web.replaceAll(webRgx, "$1");
		invoice.setWebsite(web);
		subData = subData.replaceAll(webRgx, "");
		/*
		 * supplierAddress
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

		/*
		 * customer and deivery detail
		 */
		subData = data.substring(data.indexOf(deliveryAddressRgx), data.indexOf(cusOrdNoRgx));
		subData = subData.replaceAll(customerRgx, "");
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
		 * date
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(dateRgx);
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
		String date = subData.substring(start, end);
		date = date.replaceAll(dateRgx, "$1");
		invoice.setInvoiceDate(date);
		subData = subData.replaceAll(dateRgx, "");
		/*
		 * accNo
		 */
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
		subData = subData.replaceAll(accountNoRgx, "");
		/*
		 * customer and delivery detail
		 */
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
		/*
		 * invoice table total details
		 */

		subData = data.substring(data.length() / 2);
		/*
		 * totalNet
		 */
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
		String totalNet = subData.substring(start, end);
		totalNet = totalNet.replaceAll(totalNetRgx, "$1");
		invoice.setTotal(totalNet);
		/*
		 * vat
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
		vatTotal = vatTotal.replaceAll(vatTotalRgx, "$1");
		invoice.setVatTotal(vatTotal);
		/*
		 * invoiceTotal
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(totalRgx);
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
		invoiceTotal = invoiceTotal.replaceAll(totalRgx, "$1");
		invoice.setNetTotal(invoiceTotal);
		// ======================= table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String productCode = "", qty = "", description = "", price = "", disc = "", netAmount = "";
		int start_indx = 0, end_indx = 0, table_last_idx = 0, blank_Counter = 0;
		boolean startBol = false, endBol = false;
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
			if (pattern.compile(totalNetRgx).matcher(value).find()) {
				listStr.add(full_row);
				full_row = "";
				break;
			}
			if (value.matches(tableHeaderRgx)) {
				flag = true;
			} else if (flag) {
				/*
				 * finding row
				 */
				if (value.matches(tableRgx)) {
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
		/*
		 * outsize table operation
		 */

		description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(tableRgx);
				if (pattern.matcher(value).find()) {
					productCode = value.replaceAll(tableRgx, "$1");
					description = value.replaceAll(tableRgx, "$2");
					qty = value.replaceAll(tableRgx, "$3");
					price = value.replaceAll(tableRgx, "$4");
					disc = value.replaceAll(tableRgx, "$5");
					netAmount = value.replaceAll(tableRgx, "$6");
				} else {
					pattern = Pattern.compile("[A-Za-z0-9]+");
					if (pattern.matcher(value).find()) {
						description += " " + value.trim();
					} else {
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

			extra_string = row;
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
