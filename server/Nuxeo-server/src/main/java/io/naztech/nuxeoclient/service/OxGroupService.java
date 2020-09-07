package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author masud.ahmed
 * @since 2020-07-14
 */
@Service
public class OxGroupService implements PdfInvoiceProcessor {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.ox}")
	private String folderName;

	@Value("${import.ox.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.ox.type}")
	private String nuxeoinvoiceType;

	@Value("${import.ox.prefix}")
	private String prefix;

	@Value("${import.nuxeo.ox.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${ox.vatRegNo}")
	private String vatRegNo;
	
	@Value("${ox.supplierName}")
	private String supplierName;
	
	@Value("${ox.qty}")
	private String qty;
	
	@Value("${ox.invoiceDateRgx}")
	private String invoiceDateRgx;
	
	@Value("${ox.invoiceNoRgx}")
	private String invoiceNoRgx;
	
	@Value("${ox.accNoRgx}")
	private String accNoRgx;
	
	@Value("${ox.customerRgx}")
	private String customerRgx;
	
	@Value("${ox.customerRgx2}")
	private String customerRgx2;
	
	@Value("${ox.customerRgx3}")
	private String customerRgx3;
	
	@Value("${ox.orderRefRgx}")
	private String orderRefRgx;
	
	@Value("${ox.orderNoRgx}")
	private String orderNoRgx;
	
	@Value("${ox.orderDateRgx}")
	private String orderDateRgx;
	
	@Value("${ox.deliverynoRgx}")
	private String deliverynoRgx;
	
	@Value("${ox.goodNetRgx}")
	private String goodNetRgx;
	
	@Value("${ox.deliveryRgx}")
	private String deliveryRgx;
	
	@Value("${ox.surchargeRgx}")
	private String surchargeRgx;
	
	@Value("${ox.orderNetRgx}")
	private String orderNetRgx;
	
	@Value("${ox.vatRgx}")
	private String vatRgx;
	
	@Value("${ox.totalRgx}")
	private String totalRgx;
	
	@Value("${ox.regOfficeRgx}")
	private String regOfficeRgx;
	
	@Value("${ox.tableRgx}")
	private String tableRgx;
	
	@Value("${ox.tableHeaderRgx}")
	private String tableHeaderRgx;
	
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<InvoiceTable>();
		invoice.setSupplierName(supplierName);
		invoice.setVatReg(vatRegNo);
		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		subData = data.substring(0, data.indexOf(qty));
		int start = 0, end = 0;
		Pattern pattern = Pattern.compile(invoiceDateRgx);
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
		String invoiceDate = subData.substring(start, end);
		invoiceDate = invoiceDate.replaceAll(invoiceDateRgx, "$1").trim();// value
		invoice.setInvoiceDate(invoiceDate);

		pattern = Pattern.compile("Ordered\\s+By:\\s+(\\w+)");
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
		String orderedBy = subData.substring(start, end);
		orderedBy = orderedBy.replaceAll("Ordered\\s+By:\\s+(\\w+)", "$1").trim();// value

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
		invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1").trim();// value
		invoice.setInvoiceNumber(invoiceNo);
		pattern = Pattern.compile(accNoRgx);
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
		accNo = accNo.replaceAll(accNoRgx, "$1").trim();// value
		invoice.setAccountNo(accNo);
		// ======================= customer & delivery detail =====================//
		String customer_detail = "", delivery_detail = "";

		subData = subData.replaceAll(customerRgx, "");
		subData = subData.replaceAll(customerRgx2, "");
		subData = subData.replaceAll(customerRgx3, "");
		subData = subData.replaceAll(invoiceNoRgx, "");
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
		// ================ order details ==============//
		subData = data.substring(data.indexOf(qty));
		pattern = Pattern.compile(orderRefRgx);
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
		String orderRef = subData.substring(start, end);
		orderRef = orderRef.replaceAll(orderRefRgx, "$1");// value
		invoice.setReferenceNumber(orderRef);
		// ============= next ===========//
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
		orderNo = orderNo.replaceAll(orderNoRgx, "$1");// value
		invoice.setOrderNo(orderNo);
		// ============= next ===========//
		pattern = Pattern.compile(orderDateRgx);
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
		String orderDate = subData.substring(start, end);
		orderDate = orderDate.replaceAll(orderDateRgx, "$1");// value
		// ============= next ===========//
		pattern = Pattern.compile(deliverynoRgx);
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
		String deliveryNo = subData.substring(start, end);
		deliveryNo = deliveryNo.replaceAll(deliverynoRgx, "$1");// value
		// ================ invoice total ==============//
		pattern = Pattern.compile(goodNetRgx);
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
		String goodsNet = subData.substring(start, end);
		goodsNet = goodsNet.replaceAll(goodNetRgx, "$1");// value
		invoice.setTotal(goodsNet);
		// ================= next =================//
		pattern = Pattern.compile(deliveryRgx);
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
		String delivery = subData.substring(start, end);
		delivery = delivery.replaceAll(deliveryRgx, "$1");// value
		// ================= next =================//
		pattern = Pattern.compile(surchargeRgx);
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
		String Surcharge = subData.substring(start, end);
		Surcharge = Surcharge.replaceAll(surchargeRgx, "$1");// value
		// ================= next =================//
		pattern = Pattern.compile(orderNetRgx);
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
		String orderNet = subData.substring(start, end);
		orderNet = orderNet.replaceAll(orderNetRgx, "$1");// value
		// ================= next =================//
		pattern = Pattern.compile(vatRgx);
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
		vatTotal = vatTotal.replaceAll(vatRgx, "$1");// value
		invoice.setVatTotal(vatTotal);
		// ================= next =================//
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
		String netTotal = subData.substring(start, end);
		netTotal = netTotal.replaceAll(totalRgx, "$1");// value
		invoice.setNetTotal(netTotal);
		// ================= next =================//

		pattern = Pattern.compile(regOfficeRgx);
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
		String supplierAddress = subData.substring(start);
		supplierAddress = supplierAddress.substring(0, supplierAddress.indexOf("VAT")).replaceAll(regOfficeRgx, "")
				.trim();
		// ======================= table ===========================//
		String regex = tableRgx;
		// ======================= table ========================//
		ArrayList<String> list = new ArrayList<>();
		String full_row = "";
		String desc = "", Quantity = "", code = "", rsp = "", disc = "", priceEach = "", lineTotal = "", vat = "",
				lineVat = "";
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
				list.add(full_row);
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
		Quantity = full_row.replaceAll(regex, "$1");
		table.setQuantity(Quantity);
		code = full_row.replaceAll(regex, "$2");
		table.setProductCode(code);
		desc = full_row.replaceAll(regex, "$3").trim();
		rsp = full_row.replaceAll(regex, "$4");
		disc = full_row.replaceAll(regex, "$5");
		table.setDiscount(disc);
		priceEach = full_row.replaceAll(regex, "$6");
		table.setPrice(priceEach);
		lineTotal = full_row.replaceAll(regex, "$7");
		table.setTotal(lineTotal);
		vat = full_row.replaceAll(regex, "$8");
		table.setVatPercentage(vat);
		lineVat = full_row.replaceAll(regex, "$9");
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
		tableList.add(table);
		desc = "";
		String extra_string = "";
		arr0 = null;
		for (String row : list) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(regex);
				if (pattern.matcher(value).find()) {
					Quantity = value.replaceAll(regex, "$1");
					table2.setQuantity(Quantity);
					code = value.replaceAll(regex, "$2");
					table2.setProductCode(code);
					desc = value.replaceAll(regex, "$3").trim();
					rsp = value.replaceAll(regex, "$4");
					disc = value.replaceAll(regex, "$5");
					table2.setDiscount(disc);
					priceEach = value.replaceAll(regex, "$6");
					table2.setPrice(priceEach);
					lineTotal = value.replaceAll(regex, "$7");
					table2.setTotal(lineTotal);
					vat = value.replaceAll(regex, "$8");
					table2.setVatPercentage(vat);
					lineVat = value.replaceAll(regex, "$9");
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
