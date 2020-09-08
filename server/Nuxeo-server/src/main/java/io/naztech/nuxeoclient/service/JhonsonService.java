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
public class JhonsonService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.jhonson}")
	private String folderName;

	@Value("${import.jhonson.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.jhonson.type}")
	private String nuxeoinvoiceType;

	@Value("${import.forktruck.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${jhonson.telRgx}")
	private String telRgx;

	@Value("${jhonson.invPageRgx}")
	private String invPageRgx;

	@Value("${jhonson.telFullRgx}")
	private String telFullRgx;

	@Value("${jhonson.vatNoRgx}")
	private String vatNoRgx;

	@Value("${jhonson.refRgx}")
	private String refRgx;

	@Value("${jhonson.vatRegRgx}")
	private String vatRegRgx;

	@Value("${jhonson.orderRgx}")
	private String orderRgx;

	@Value("${jhonson.totalNetRgx}")
	private String totalNetRgx;

	@Value("${jhonson.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${jhonson.totalNetFullRgx}")
	private String totalNetFullRgx;

	@Value("${jhonson.carrageRgx}")
	private String carrageRgx;

	@Value("${jhonson.totalVatRgx}")
	private String totalVatRgx;

	@Value("${jhonson.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${jhonson.tableRgx}")
	private String tableRgx;

	@Value("${jhonson.descRgx}")
	private String descRgx;

	@Value("${jhonson.desc2Rgx}")
	private String desc2Rgx;

	public void process() {

	}

	public static void main(String[] args) {
		new JhonsonService().process();
	}

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		 invoice.setSortName(Constants.JHONSON);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// ================= supplier details ====================//
		String supplierDetails = "";
		subData = data.substring(0, data.indexOf(telRgx));
		subData = subData.replaceAll(invPageRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				supplierDetails += "," + value;
			}
		}
		supplierDetails = supplierDetails.replaceAll("^,", "");
		dataArray = supplierDetails.split(",");
		invoice.setSupplierAddress(supplierDetails);
		invoice.setSupplierName(dataArray[0]);
		/*
		 * phone
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(telFullRgx);
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
		String phone = data.substring(start, end);
		phone = phone.replaceAll(telFullRgx, "$1");
		/*
		 * vatNo
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
		// ============= customer details ===============//
		String customer_detail = "";
		String invoice_detail = "";
		subData = data.substring(data.indexOf(vatNo), data.indexOf(refRgx));

		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			value = value.replaceAll("^\\s{10}", "=").trim();
			String[] arr1 = value.split("\\s{11,}");
			customer_detail += "," + arr1[0].trim();
			try {
				invoice_detail += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		customer_detail = customer_detail.replaceAll(",=", ",").replaceAll("\\,{2,}", "").trim();
		customer_detail = customer_detail.replaceAll("^,", "").replaceAll(vatNo + vatRegRgx, "");
		invoice_detail = invoice_detail.replaceAll(",=", ",").replaceAll("^,", "");
		dataArray = invoice_detail.split(",");
		invoice.setInvoiceNumber(dataArray[0]);
		invoice.setInvoiceDate(dataArray[1]);
		invoice.setOrderNo(dataArray[4].replaceAll(orderRgx, "").trim());
		/*
		 * delivery address
		 **/
		String deliveryAddress = "";
		dataArray = data.split("\\n");
		int index = 0;
		int indexStore = 0;
		for (String value : dataArray) {
			if (Pattern.compile(totalNetRgx).matcher(value).find()) {
				indexStore = index;
			}
			index++;
		}
		for (int i = indexStore - 3; i < dataArray.length; i++) {

			if (Pattern.compile(invoiceTotalRgx).matcher(dataArray[i]).find()) {
				String invoiceTotal = dataArray[i].replaceAll(invoiceTotalRgx, "$1");
				invoice.setTotal(invoiceTotal.trim());
				deliveryAddress += "," + dataArray[i].replaceAll(invoiceTotalRgx, "");
				break;
			} else if (Pattern.compile(totalNetFullRgx).matcher(dataArray[i]).find()) {
				String subTotal = dataArray[i].trim();
				dataArray[i] = dataArray[i].replaceAll(totalNetFullRgx, "").trim();
				deliveryAddress += "," + dataArray[i].replaceAll(totalNetFullRgx, "");
				subTotal = subTotal.replaceAll(dataArray[i], "").replaceAll(totalNetFullRgx, "$1");// not
				// set
				invoice.setNetTotal(subTotal.trim());
			} else if (Pattern.compile(carrageRgx).matcher(dataArray[i]).find()) {
				dataArray[i] = dataArray[i].replaceAll(carrageRgx, "").trim();
				deliveryAddress += "," + dataArray[i].replaceAll(carrageRgx, "");
			} else if (Pattern.compile(totalVatRgx).matcher(dataArray[i]).find()) {
				String vatTotal = dataArray[i].trim();
				dataArray[i] = dataArray[i].replaceAll(totalVatRgx, "").trim();
				deliveryAddress += "," + dataArray[i].replaceAll(totalVatRgx, "");
				vatTotal = vatTotal.replaceAll(dataArray[i], "").replaceAll(totalVatRgx, "$1");// not
				// set
				invoice.setVatTotal(vatTotal.trim());
			} else {
				deliveryAddress += "," + dataArray[i].trim();
			}

		}
		deliveryAddress = deliveryAddress.replaceAll(",{2,}", "").replaceAll("^,", "");
		invoice.setDeliveryAddress(deliveryAddress);
		dataArray = deliveryAddress.split(",");
		// ============= table =============== //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", qty = "", vatRate = "", unitPrice = "", disc = "", netTotal = "", vat;
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
		for (String value : arr0) {
			value = value.trim();
			if (Pattern.compile(dataArray[0]).matcher(value).find()) {
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
						unitPrice = value.replaceAll(table, "$3");
						netTotal = value.replaceAll(table, "$4");
						vatRate = value.replaceAll(table, "$5");
						vat = value.replaceAll(table, "$6");

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
