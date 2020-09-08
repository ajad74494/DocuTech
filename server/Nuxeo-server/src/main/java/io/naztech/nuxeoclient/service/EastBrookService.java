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
public class EastBrookService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.eastbrook}")
	private String folderName;

	@Value("${import.eastbrook.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.eastbrook.type}")
	private String nuxeoinvoiceType;

	@Value("${import.eastbrook.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${eastBrook.supplierRgx}")
	private String supplierRgx;

	@Value("${eastBrook.invoiceToRgx}")
	private String invoiceToRgx;

	@Value("${eastBrook.deliveryRgx}")
	private String deliveryRgx;

	@Value("${eastBrook.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${eastBrook.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${eastBrook.ourRefRgx}")
	private String ourRefRgx;

	@Value("${eastBrook.telRgx}")
	private String telRgx;

	@Value("${eastBrook.faxRgx}")
	private String faxRgx;

	@Value("${eastBrook.emailRgx}")
	private String emailRgx;

	@Value("${eastBrook.stockRefRgx}")
	private String stockRefRgx;

	@Value("${eastBrook.custDeliRgx}")
	private String custDeliRgx;

	@Value("${eastBrook.netAmtRgx}")
	private String netAmtRgx;

	@Value("${eastBrook.vatAmtRgx}")
	private String vatAmtRgx;

	@Value("${eastBrook.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${eastBrook.vatRegRgx}")
	private String vatRegRgx;

	@Value("${eastBrook.tableRgx}")
	private String tableRgx;

	@Value("${eastBrook.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${eastBrook.descRgx}")
	private String descRgx;

	@Value("${eastBrook.desc2Rgx}")
	private String desc2Rgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {

		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		InvoiceTable invoiceTable = new InvoiceTable();

		invoice.setSortName(Constants.EASTBROOK);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		subData = data.substring(0, data.indexOf(invoiceToRgx));
		subData = subData.replaceAll(supplierRgx, "");
		/*
		 * delivery No
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(deliveryRgx);
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
		String deliveryNo = subData.substring(start, end);
		deliveryNo = deliveryNo.replaceAll(deliveryRgx, "$1");
		invoice.setDeliveryNoteNo(deliveryNo);
		subData = subData.replaceAll(deliveryRgx, "");
		/*
		 * invoice No
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
		invoice.setDeliveryNoteNo(invoiceDate);
		subData = subData.replaceAll(invoiceDateRgx, "");
		/*
		 * our Ref
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(ourRefRgx);
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
		String ourRef = subData.substring(start, end);
		ourRef = ourRef.replaceAll(ourRefRgx, "$1");
		invoice.setReferenceNumber(ourRef);
		subData = subData.replaceAll(ourRefRgx, "");

		/*
		 * phone
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(telRgx);
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
		phone = phone.replaceAll(telRgx, "$1");
		invoice.setTelephone(phone);
		subData = subData.replaceAll(telRgx, "");
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
		String supplierAddress = subData.trim();
		invoice.setSupplierAddress(supplierAddress);
		/*
		 * customer and delivery detail
		 */
		String customer_detail = "", delivery_detail = "";
		subData = data.substring(data.indexOf(invoiceToRgx), data.indexOf(stockRefRgx));
		subData = subData.replaceAll(custDeliRgx, "");
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
		 * invoice amount details
		 */
		subData = data.substring(data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(netAmtRgx);
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
		String netAmt = subData.substring(start, end);
		netAmt = netAmt.replaceAll(netAmtRgx, "$1");
		invoice.setTotal(netAmt);

		start = 0;
		end = 0;
		pattern = Pattern.compile(vatAmtRgx);
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
		String vatAmt = subData.substring(start, end);
		vatAmt = vatAmt.replaceAll(vatAmtRgx, "$1");
		invoice.setVatTotal(vatAmt);

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
		invoice.setNetTotal(invoiceTotal);

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

		// ======================= table ===========================//
		String regex = tableRgx;
		// ======================= table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String Stock_Ref = "", UnitPrice = "", Description = "", Unit_Price = "", Disc = "", NetAmount = "", vat = "",
				qty = "";
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
			if (Pattern.compile(netAmtRgx).matcher(value).find()) {
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

		Description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(regex);
				if (pattern.matcher(value).find()) {
					Stock_Ref = value.replaceAll(regex, "$1");
					qty = value.replaceAll(regex, "$2");
					Description = value.replaceAll(regex, "$3").trim();
					Unit_Price = value.replaceAll(regex, "$4");
					Disc = value.replaceAll(regex, "$5");
					netAmt = value.replaceAll(regex, "$6");
					vat = value.replaceAll(regex, "$7");
				} else {
					pattern = Pattern.compile("[A-Za-z0-9]+");
					if (pattern.matcher(value).find()) {
						Description += " " + value.trim();
					} else {
					}
				}

			}
			/*
			 * value of full desc
			 */
			Description = Description.trim();
			table2.setDescription(Description);
			tableList.add(table2);
			Description = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
