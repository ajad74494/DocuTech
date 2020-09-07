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
 * 
 * @author masud.ahmed
 * @since 2020-07-16
 */
@Service
public class VrService implements PdfInvoiceProcessor {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.vr}")
	private String folderName;

	@Value("${import.vr.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.vr.type}")
	private String nuxeoinvoiceType;

	@Value("${import.vr.prefix}")
	private String prefix;

	@Value("${import.nuxeo.vr.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${vr.supplierName}")
	private String supplierName;

	@Value("${vr.taxRgx}")
	private String taxRgx;
	
	@Value("${vr.vatRegRgx}")
	private String vatRegRgx;
	
	@Value("${vr.companyRegRgx}")
	private String companyRegRgx;
	
	@Value("${vr.supplierRgx}")
	private String supplierRgx;
	
	@Value("${vr.invoicetoRgx}")
	private String invoicetoRgx;
	
	@Value("${vr.shipviaRgx}")
	private String shipviaRgx;
	
	@Value("${vr.dueDate}")
	private String dueDate;
	
	@Value("${vr.invoiceNoRgx}")
	private String invoiceNoRgx;
	
	@Value("${vr.dateRgx}")
	private String dateRgx;
	
	@Value("${vr.customerRgx}")
	private String customerRgx;
	
	@Value("${vr.activityRgx}")
	private String activityRgx;
	
	@Value("${vr.orderNoRgx}")
	private String orderNoRgx;
	
	@Value("${vr.orderNoRgx2}")
	private String orderNoRgx2;
	
	@Value("${vr.subTotalRgx}")
	private String subTotalRgx;
	
	@Value("${vr.vatTotalRgx}")
	private String vatTotalRgx;
	
	@Value("${vr.totalRgx}")
	private String totalRgx;
	
	@Value("${vr.dueRgx}")
	private String dueRgx;
	
	@Value("${vr.tableRgx}")
	private String tableRgx;
	
	@Value("${vr.tableHeaderRgx}")
	private String tableHeaderRgx;
	
	@Value("${vr.descRgx}")
	private String descRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.VR);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String data = pdfStr;
		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// =============== supplier detail =================//
		subData = data.substring(0, data.indexOf(taxRgx));
		int start = 0, end = 0;
		Pattern pattern = Pattern.compile(vatRegRgx);
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
		String vatRegNo = subData.substring(start, end);
		vatRegNo = vatRegNo.replaceAll(vatRegRgx, "$1");// value
		invoice.setVatReg(vatRegNo);
		pattern = Pattern.compile(companyRegRgx);
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
		String companyRegNo = subData.substring(start, end);
		companyRegNo = companyRegNo.replaceAll(companyRegRgx, "$1");// value
		invoice.setRegNo(companyRegNo);
		subData = subData.replaceAll(supplierRgx, "");
		dataArray = subData.split("\\n");
		String supplierAddress = "";
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				supplierAddress += "," + value;
			}

		}
		supplierAddress = supplierAddress.replaceFirst("^,", "");// value
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(supplierName);
		// ========================= supplier & customer address ==================//

		pattern = Pattern.compile(invoicetoRgx);
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
		subData = data.substring(end);
		pattern = Pattern.compile(shipviaRgx);
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
		subData = subData.replaceAll(dueDate, "");
		// ================ NEXT ================//
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
		invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1");// value
		invoice.setInvoiceNumber(invoiceNo);
		// ================ NEXT ================//
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
		String invoiceDate = subData.substring(start, end);
		invoiceDate = invoiceDate.replaceAll(dateRgx, "$1");// value
		invoice.setInvoiceDate(invoiceDate);
		// ================= details =============//
		String customer_detail = "", delivery_detail = "";
		subData = subData.replaceAll(customerRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			value = value.replaceAll("^\\s{10}", "=").trim();
			if (!value.equals("")) {
				String[] arr1 = value.split("\\s{7,}");

				customer_detail += "," + arr1[0].trim();
				try {
					delivery_detail += "," + arr1[1].trim();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
		customer_detail = customer_detail.replaceAll(",=", ",").replaceAll("\\,{2,}", "").trim();// value
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");// value
		dataArray = customer_detail.split(",");
		String customerName = dataArray[0];// value
		invoice.setCustomerName(customerName);
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		// ================ order NO =============//
		dataArray = data.split("\\n");
		String orderNo = "";
		int index = 0;
		boolean flag = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(activityRgx).matcher(value).find()) {
				break;
			}
			if (Pattern.compile(orderNoRgx).matcher(value).find()) {
				flag = true;
			} else if (flag) {
				if (!value.equals("")) {
					value = value.replaceAll(orderNoRgx2, "$3");
					orderNo = value.trim();
					invoice.setOrderNo(orderNo);
				}

			}

		}
		// ================ invoice total =======================//
		String subTotal = "", vatTotal = "", total = "", balaceDue = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(subTotalRgx).matcher(value).find()) {
				subTotal = value.replaceAll(subTotalRgx, "$1");// value
				invoice.setTotal(subTotal);
			} else if (Pattern.compile(vatTotalRgx).matcher(value).find()) {
				vatTotal = value.replaceAll(vatTotalRgx, "$1");// value
			} else if (Pattern.compile(totalRgx).matcher(value).find()) {
				total = value.replaceAll(totalRgx, "$1");// value
				invoice.setNetTotal(total);
			} else if (Pattern.compile(dueRgx).matcher(value).find()) {
				balaceDue = value;// value
				invoice.setDue(balaceDue);
			}

		}
		// ======================= table ========================//
		String regex = tableRgx;
		ArrayList<String> list = new ArrayList<>();
		String full_row = "";
		String ACTIVITY = "", QTY = "", RATE = "", VAT = "", AMOUNT = "";
		int start_indx = 0, end_indx = 0, table_last_idx = 0, blank_Counter = 0;
		boolean startBol = false, endBol = false;
		/*
		 * storing every line in array
		 **/
		String[] arr0 = data.split("\\n");
		/*
		 * setting the regex for table search
		 **/
		flag = false;
		index = 0;
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
		ACTIVITY = full_row.replaceAll(regex, "$1");
		QTY = full_row.replaceAll(regex, "$2");
		table.setQuantity(QTY);
		RATE = full_row.replaceAll(regex, "$3").trim();
		table.setPrice(RATE);
		VAT = full_row.replaceAll(regex, "$4");
		table.setVatPercentage(VAT);
		AMOUNT = full_row.replaceAll(regex, "$5");
		table.setTotal(AMOUNT);

		tableList.add(table);
		/*
		 * last rows extra lines
		 */
		for (int i = start_indx + 1; i < table_last_idx; i++) {
			arr0[i] = arr0[i].trim();
			pattern = Pattern.compile("[A-Za-z]+");
			if (pattern.matcher(arr0[i]).find()) {
				dataArray = arr0[i].split("\\s{7,}");
				ACTIVITY += " " + dataArray[0].trim();
				try {
					VAT += " " + dataArray[1].trim();
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else {
			}

		}
		table.setDescription(ACTIVITY);
		tableList.add(table);
		ACTIVITY = "";
		arr0 = null;
		for (String row : list) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {

				pattern = Pattern.compile(regex);
				if (pattern.matcher(value).find()) {
					ACTIVITY = full_row.replaceAll(regex, "$1");
					QTY = full_row.replaceAll(regex, "$2");
					table2.setQuantity(QTY);
					RATE = full_row.replaceAll(regex, "$3").trim();
					table2.setPrice(RATE);
					VAT = full_row.replaceAll(regex, "$4");
					table2.setVatPercentage(VAT);
					AMOUNT = full_row.replaceAll(regex, "$5");
					table2.setTotal(AMOUNT);
				} else {
					pattern = Pattern.compile("[A-Za-z]+");
					if (pattern.matcher(value).find()) {
						dataArray = value.split("\\s{7,}");
						ACTIVITY += " " + dataArray[0].trim();
						try {
							VAT += " " + dataArray[1].trim();
						} catch (Exception e) {
							// TODO: handle exception
						}

					} else {
					}
				}

			}
			/*
			 * value of full desc
			 */
			ACTIVITY = ACTIVITY.trim();
			table2.setDescription(ACTIVITY);
			tableList.add(table2);
			ACTIVITY = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}

}
