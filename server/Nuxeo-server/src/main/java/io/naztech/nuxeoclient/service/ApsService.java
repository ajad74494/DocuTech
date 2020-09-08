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
public class ApsService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.acsupply}")
	private String folderName;

	@Value("${import.aps.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.aps.type}")
	private String nuxeoinvoiceType;

	@Value("${import.aps.prefix}")
	private String prefix;

	@Value("${import.nuxeo.aps.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${aps.compnyRegRgx}")
	private String compnyRegRgx;

	@Value("${aps.phoneRgx}")
	private String phoneRgx;

	@Value("${aps.emailRgx}")
	private String emailRgx;

	@Value("${aps.vatRegRgx}")
	private String vatRegRgx;

	@Value("${aps.telNdEmailRgx}")
	private String telNdEmailRgx;

	@Value("${aps.vatNdcompnyRegRgx}")
	private String vatNdcompnyRegRgx;

	@Value("${aps.discountRgx}")
	private String discountRgx;

	@Value("${aps.totalNetRgx}")
	private String totalNetRgx;

	@Value("${aps.carrageNetRgx}")
	private String carrageNetRgx;

	@Value("${aps.totalVatRgx}")
	private String totalVatRgx;

	@Value("${aps.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${aps.tableRgx}")
	private String tableRgx;

	@Value("${aps.invoiceRgx}")
	private String invoiceRgx;

	@Value("${aps.stockRgx}")
	private String stockRgx;

	@Value("${aps.invoiceNdPageRgx}")
	private String invoiceNdPageRgx;

	@Value("${aps.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${aps.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${aps.orderNoRgx}")
	private String orderNoRgx;

	@Value("${aps.accRefRgx}")
	private String accRefRgx;

	@Value("${aps.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${aps.deliverToRgx}")
	private String deliverToRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.APS);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// =========== supplier details ============//
		String tel = "", email = "", vatRegNo = "", companyRegNo = "";
		int start = 0, end = 0;
		/**
		 * finding last index
		 */
		Pattern pattern = Pattern.compile(compnyRegRgx);
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
		subData = data.substring(0, end);
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(phoneRgx).matcher(value).find()) {
				tel = value.replaceAll(phoneRgx, "$1");
				invoice.setTelephone(tel);
			} else if (Pattern.compile(emailRgx).matcher(value).find()) {
				email = value.replaceAll(emailRgx, "$1");
				invoice.setEmail(email);
			} else if (Pattern.compile(vatRegRgx).matcher(value).find()) {
				vatRegNo = value.replaceAll(vatRegRgx, "$1");
				invoice.setVatReg(vatRegNo);
			} else if (Pattern.compile(compnyRegRgx).matcher(value).find()) {
				companyRegNo = value.replaceAll(compnyRegRgx, "$1 $2");
				invoice.setRegNo(companyRegNo);
			}
		}

		subData = subData.replaceAll(telNdEmailRgx, "");
		subData = subData.replaceAll(vatNdcompnyRegRgx, "");
		dataArray = subData.trim().split("\\n");
		/*
		 * value
		 */
		String supplierName = dataArray[0].trim();
		invoice.setSupplierName(supplierName);
		String supplierAddress = "";
		for (String value : dataArray) {
			supplierAddress += value.trim() + ",";
		}
		/*
		 * set value for supplier address down there
		 */
		supplierAddress = supplierAddress;
		dataArray = supplierAddress.split(",");
		invoice.setSupplierName(dataArray[0]);
		invoice.setSupplierAddress(supplierAddress);
		// ======================= customer detail =====================//
		subData = data.substring(data.indexOf(invoiceRgx), data.indexOf(stockRgx));
		subData = subData.replaceAll(invoiceNdPageRgx, "");
		/*
		 * invoice no
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
		 * invoice date
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
		invoice.setInvoiceNumber(invoiceDate);
		subData = subData.replaceAll(invoiceDateRgx, "");
		/*
		 * order no
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
		invoice.setInvoiceNumber(orderNo);
		subData = subData.replaceAll(orderNoRgx, "");
		/*
		 * account ref
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(accRefRgx);
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
		accNo = accNo.replaceAll(accRefRgx, "$1");
		invoice.setInvoiceNumber(accNo);
		subData = subData.replaceAll(accRefRgx, "");
		/*
		 * customer details
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

		// ========================= invoice total
		// ====================================//
		String totalDiscount = "", netTotal = "", carrage = "", vatTotal = "", invoiceTotal = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			if (Pattern.compile(discountRgx).matcher(value).find()) {
				totalDiscount = value;
				value = value.replaceAll(discountRgx, "$1");
				totalDiscount = totalDiscount.substring(value.length()).trim();
			} else if (Pattern.compile(totalNetRgx).matcher(value).find()) {
				netTotal = value;
				value = value.replaceAll(totalNetRgx, "$1");
				netTotal = netTotal.substring(value.length()).trim();
				invoice.setNetTotal(netTotal);
			} else if (Pattern.compile(carrageNetRgx).matcher(value).find()) {
				carrage = value;
				value = value.replaceAll(carrageNetRgx, "$1");
				carrage = carrage.substring(value.length()).trim();
			} else if (Pattern.compile(totalVatRgx).matcher(value).find()) {
				vatTotal = value;
				value = value.replaceAll(totalVatRgx, "$1");
				vatTotal = vatTotal.substring(value.length()).trim();
				invoice.setVatTotal(vatTotal);
				;
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				invoiceTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "$1");
				invoiceTotal = invoiceTotal.substring(value.length()).trim();
				invoice.setTotal(invoiceTotal);
			}
		}
		// ======================== table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String stockCode = "", qty = "", desc = "", unitPrice = "", disc = "", amtNet = "", amtVat = "", vat = "";
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
			if (Pattern.compile(deliverToRgx).matcher(value).find()) {
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
						stockCode = value.replaceAll(tableRgx, "$1");
						qty = value.replaceAll(tableRgx, "$2");
						desc = value.replaceAll(tableRgx, "$3");
						unitPrice = value.replaceAll(tableRgx, "$4");
						disc = value.replaceAll(tableRgx, "$5");
						amtNet = value.replaceAll(tableRgx, "$6");
						amtVat = value.replaceAll(tableRgx, "$7");
						vat = value.replaceAll(tableRgx, "$8");

						/*
						 * setting values
						 */

					} else {
						pattern = Pattern.compile("[A-Za-z0-9]+");
						if (pattern.matcher(value).find()) {
							desc += " " + value.trim();
						}
					}
				}
				extra_string = value;

			}
			/*
			 * value of full desc
			 */
			desc = desc.trim();
			tableRow.setDescription(desc);
			tableList.add(tableRow);
			desc = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
