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
public class PremierService implements PdfInvoiceProcessorPdfBox {
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

	@Value("${premier.extraRgx}")
	private String extraRgx;

	@Value("${premier.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${premier.ourRefRgx}")
	private String ourRefRgx;

	@Value("${premier.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${premier.yourRefRgx}")
	private String yourRefRgx;

	@Value("${premier.invoiceAddressRgx}")
	private String invoiceAddressRgx;

	@Value("${premier.itemDescRgx}")
	private String itemDescRgx;

	@Value("${premier.customerNdDeliveryRgx}")
	private String customerNdDeliveryRgx;

	@Value("${premier.supplierRgx}")
	private String supplierRgx;

	@Value("${premier.supplierRgx2}")
	private String supplierRgx2;

	@Value("${premier.supplierRgx3}")
	private String supplierRgx3;

	@Value("${premier.regOfficeRgx}")
	private String regOfficeRgx;

	@Value("${premier.regOfficeRgx2}")
	private String regOfficeRgx2;

	@Value("${premier.vatNoRgx}")
	private String vatNoRgx;

	@Value("${premier.companyRegRgx}")
	private String companyRegRgx;

	@Value("${premier.emailRgx}")
	private String emailRgx;

	@Value("${premier.telRgx}")
	private String telRgx;

	@Value("${premier.faxRgx}")
	private String faxRgx;

	@Value("${premier.totalAmtRgx}")
	private String totalAmtRgx;

	@Value("${premier.totalVatRgx}")
	private String totalVatRgx;

	@Value("${premier.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${premier.tableRgx}")
	private String tableRgx;

	@Value("${premier.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		InvoiceTable invoiceTable = new InvoiceTable();

		invoice.setSortName(Constants.PREMIEOFORESTPRODUCTSLTD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		data = data.replaceAll(extraRgx, "").replaceAll("^\\s+", "");
		/*
		 * extracting invoiceDate
		 */
		int start = 0, end = 0;
		Pattern pattern = Pattern.compile(invoiceDateRgx);
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
		String invoiceDate = data.substring(start, end);
		invoiceDate = invoiceDate.replaceAll(invoiceDateRgx, "$1").trim();
		invoice.setInvoiceDate(invoiceDate);
		/*
		 * extracting ourRef
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(ourRefRgx);
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
		String ourRef = data.substring(start, end);
		ourRef = ourRef.replaceAll(ourRefRgx, "$1").trim();
		invoice.setReferenceNumber(ourRef);
		/*
		 * extracting invoiceNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceNoRgx);
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
		String invoiceNo = data.substring(start, end);
		invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1").trim();
		invoice.setInvoiceNumber(invoiceNo);
		/*
		 * extracting yourRef
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(yourRefRgx);
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
		String yourRef = data.substring(start, end);
		yourRef = yourRef.replaceAll(yourRefRgx, "$1").trim();
		/*
		 * extracting customer and delivery details
		 */
		String customer_detail = "", delivery_detail = "";
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceAddressRgx);
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
		subData = data.substring(start);
		start = 0;
		end = 0;
		pattern = Pattern.compile(itemDescRgx);
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
		subData = subData.substring(0, start);
		subData = subData.replaceAll(customerNdDeliveryRgx, "");
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
		 * Supplier details
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(supplierRgx);
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

		subData = data.substring(start);
		start = 0;
		end = 0;
		pattern = Pattern.compile(supplierRgx2);
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
		String supplierName = subData.substring(start, end);
		supplierName = supplierName.replaceAll(supplierRgx3, "").trim();
		invoice.setSupplierName(supplierName);
		/*
		 * supplier address
		 */
		String supplierAddress = "";
		String vatNo = "";
		String regNo = "";
		int getLength = 0;
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(regOfficeRgx).matcher(value).find()) {
				supplierAddress = value.replaceAll(vatNoRgx, "").replaceAll(regOfficeRgx2, "");
				invoice.setSupplierAddress(supplierAddress);
				vatNo = value;
				value = value.replaceAll(vatNoRgx, "");
				vatNo = vatNo.substring(value.length());
				vatNo = vatNo.replaceAll(vatNoRgx, "$1");
				invoice.setVatNo(vatNo);
			} else if (Pattern.compile(companyRegRgx).matcher(value).find()) {
				regNo = value.replaceAll(companyRegRgx, "$1");
			}
		}

		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.substring(0, 90);
			subData += value + "\\n";

		}

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
		invoice.setEmail(email.trim());

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
		String tel = subData.substring(start, end);
		tel = tel.replaceAll(telRgx, "$1");
		invoice.setTelephone(tel);

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
		invoice.setFax(fax.trim());

		/*
		 * invoice table totals
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(totalAmtRgx);
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
		String total = data.substring(start, end);
		total = total.replaceAll(totalAmtRgx, "$1");
		invoice.setTotal(total);

		start = 0;
		end = 0;
		pattern = Pattern.compile(totalVatRgx);
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
		String vat = data.substring(start, end);
		vat = vat.replaceAll(totalVatRgx, "$1");
		invoice.setVatTotal(vat);

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
		invoiceTotal = invoiceTotal.replaceAll(invoiceTotalRgx, "$1");
		invoice.setNetTotal(invoiceTotal);

		// ======================= table ===========================//
		String regex = tableRgx;
		// ======================= table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String desc = "", Item = "", Struct = "", Cert = "", Qty = "", Weight = "", Price = "", Per = "", Total = "",
				VAT = "", Rate = "";
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
			if (value.matches(tableHeaderRgx)) {
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
				if (value.matches(regex)) {
					start_indx = index;
					startBol = true;
				} else {
					if (startBol) {
						pattern = Pattern.compile("[A-Za-z0-9]");
						matcher = pattern.matcher(arr0[index + 1]);
						if (!arr0[index + 1].equals("")) {
							pattern = Pattern.compile(regex);
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
		InvoiceTable table = new InvoiceTable();
		dataArray = null;
		full_row = "";
		full_row = arr0[start_indx].trim();
		Item = full_row.replaceAll(regex, "$1");
		desc = full_row.replaceAll(regex, "$2");
		Struct = full_row.replaceAll(regex, "$3").trim();
		Cert = full_row.replaceAll(regex, "$4");
		Qty = full_row.replaceAll(regex, "$5");
		Weight = full_row.replaceAll(regex, "$6");
		Price = full_row.replaceAll(regex, "$7");
		Per = full_row.replaceAll(regex, "$8");
		Total = full_row.replaceAll(regex, "$9");
		VAT = full_row.replaceAll(regex, "$10");
		Rate = full_row.replaceAll(regex, "$11");
		table.setItemNo(Item);
		table.setStruct(Struct);
		table.setCert(Cert);
		table.setQuantity(Qty);
		table.setWeight(Weight);
		table.setPrice(Price);
		table.setPer(Per);
		table.setTotal(Total);
		table.setVatAmount(VAT);
		table.setVatRate(Rate);
		/*
		 * last rows extra lines
		 */
		for (int i = start_indx + 1; i < table_last_idx; i++) {
			arr0[i] = arr0[i].trim();
			pattern = Pattern.compile("[A-Za-z]+");
			if (pattern.matcher(arr0[i]).find()) {
				desc += " " + arr0[i].trim();
			} else {
			}

		}
		/*
		 * value of desc
		 */
		desc = desc;
		table.setDescription(desc);

		desc = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(regex);
				if (pattern.matcher(value).find()) {
					Item = full_row.replaceAll(regex, "$1");
					desc = full_row.replaceAll(regex, "$2");
					Struct = full_row.replaceAll(regex, "$3").trim();
					Cert = full_row.replaceAll(regex, "$4");
					Qty = full_row.replaceAll(regex, "$5");
					Weight = full_row.replaceAll(regex, "$6");
					Price = full_row.replaceAll(regex, "$7");
					Per = full_row.replaceAll(regex, "$8");
					Total = full_row.replaceAll(regex, "$9");
					VAT = full_row.replaceAll(regex, "$10");
					Rate = full_row.replaceAll(regex, "$11");
					table2.setItemNo(Item);
					table2.setStruct(Struct);
					table2.setCert(Cert);
					table2.setQuantity(Qty);
					table2.setWeight(Weight);
					table2.setPrice(Price);
					table2.setPer(Per);
					table2.setTotal(Total);
					table2.setVatAmount(VAT);
					table2.setVatRate(Rate);
				} else {
					pattern = Pattern.compile("[A-Za-z0-9]+");
					if (pattern.matcher(value).find()) {
						desc += " " + value.trim();
					} else {
					}
				}

			}
			/*
			 * value of full desc
			 */
			desc = desc.trim();
			table2.setDescription(desc);
			tableList.add(table2);
			desc = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
