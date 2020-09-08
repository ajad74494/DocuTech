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
public class CiretService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.ciret}")
	private String folderName;

	@Value("${import.ciret.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.ciret.type}")
	private String nuxeoinvoiceType;

	@Value("${import.ciret.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${ciret.noUseDataRgx}")
	private String noUseDataRgx;

	@Value("${ciret.invoiceRgx}")
	private String invoiceRgx;

	@Value("${ciret.invNoDateRgx}")
	private String invNoDateRgx;

	@Value("${ciret.customerNoRgx}")
	private String customerNoRgx;

	@Value("${ciret.delAdrressRgx}")
	private String delAdrressRgx;

	@Value("${ciret.deliNoteNoRgx}")
	private String deliNoteNoRgx;

	@Value("${ciret.delAddressXtRgx}")
	private String delAddressXtRgx;

	@Value("${ciret.regNoRgx}")
	private String regNoRgx;

	@Value("${ciret.vatNoRgx}")
	private String vatNoRgx;

	@Value("${ciret.netTotalRgx}")
	private String netTotalRgx;

	@Value("${ciret.vatRgx}")
	private String vatRgx;

	@Value("${ciret.totalAmountRgx}")
	private String totalAmountRgx;

	@Value("${ciret.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${ciret.tableRgx}")
	private String tableRgx;

	@Value("${ciret.subTotalRgx}")
	private String subTotalRgx;

	@Value("${ciret.descRgx}")
	private String descRgx;

	@Value("${ciret.unitNdDiscRgx}")
	private String unitNdDiscRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;

		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.CIRET);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		data = data.replaceAll(noUseDataRgx, "").trim();
		// ============ supplier details ============//

		dataArray = data.split("\\n");
		String supplierAddress = dataArray[0];
		invoice.setSupplierAddress(supplierAddress);
		dataArray[0] = "";
		/*
		 * customer details
		 */
		String customerAddress = "";
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceRgx).matcher(value).find()) {
				break;
			}
			if (!value.equals("")) {
				customerAddress += "," + value;
			}
		}
		customerAddress = customerAddress.replaceAll("^,", "");
		dataArray = customerAddress.split("\\n");
		invoice.setCustomerName(dataArray[0]);
		invoice.setCustomerAddress(customerAddress);
		dataArray = supplierAddress.split(",");
		invoice.setSupplierName(dataArray[0]);
		// ============ invoice details ==========//
		String invoiceDetails = "";
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(invNoDateRgx);
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
		invoiceDetails = data.substring(start, end);
		String invoiceNo = invoiceDetails.replaceAll(invNoDateRgx, "$1");
		String invoiceDate = invoiceDetails.replaceAll(invNoDateRgx, "$2");
		String orderNo = invoiceDetails.replaceAll(invNoDateRgx, "$3");
		invoice.setInvoiceNumber(invoiceNo);
		invoice.setInvoiceDate(invoiceDate);
		invoice.setOrderNo(orderNo);
		/*
		 * customerNo
		 */

		start = 0;
		end = 0;
		pattern = Pattern.compile(customerNoRgx);
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
		String customerNo = data.substring(start, end);
		customerNo = customerNo.replaceAll(customerNoRgx, "$1");
		/*
		 * delivery address
		 */
		String deliveryAddress = data.substring(data.indexOf(delAdrressRgx), data.indexOf(deliNoteNoRgx));
		deliveryAddress = deliveryAddress.replaceAll(delAddressXtRgx, " ");
		invoice.setDeliveryAddress(deliveryAddress);
		/*
		 * company registration number
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(regNoRgx);
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
		String regNo = data.substring(start, end);
		regNo = regNo.replaceAll(regNoRgx, "$1");
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

		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(netTotalRgx).matcher(value).find()) {
				String netTotal = value.trim();
				value = value.replaceAll(netTotalRgx, "").trim();
				netTotal = netTotal.replaceAll(value, "").replaceAll(netTotalRgx, "$1");
				invoice.setNetTotal(netTotal);
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vatTotal = value.trim();
				value = value.replaceAll(vatRgx, "").trim();
				vatTotal = vatTotal.replaceAll(value, "").replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(totalAmountRgx).matcher(value).find()) {
				String total = value.trim();
				value = value.replaceAll(totalAmountRgx, "").trim();
				total = total.replaceAll(value, "").replaceAll(totalAmountRgx, "$1");
				invoice.setTotal(total.trim());
			}

		}
		// ============== table =================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String pos = "", itemNo = "", description = "", qtyUnit = "", unitPrice = "", total = "";
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
			if (Pattern.compile(subTotalRgx).matcher(value).find()) {
				full_row += "";
				flag = false;
			} else if (Pattern.compile(netTotalRgx).matcher(value).find()) {
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
						pos = value.replaceAll(table, "$1");
						itemNo = value.replaceAll(table, "$2");
						description = value.replaceAll(table, "$3");
						qtyUnit = value.replaceAll(table, "$4");
						unitPrice = value.replaceAll(table, "$5");
						total = value.replaceAll(table, "$6");
						/*
						 * setting values
						 */

					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							String unitNdDisc = value;
							value = value.replaceAll(unitNdDiscRgx, "").trim();
							unitNdDisc = unitNdDisc.replaceAll(value, "");
							qtyUnit += " " + unitNdDisc.replaceAll(unitNdDiscRgx, "$1");
							unitPrice += " " + unitNdDisc.replaceAll(unitNdDiscRgx, "$2");
							description += " " + value.trim();
						}
					}
				}
				extra_string = value;

			}
			/*
			 * value of full desc
			 */
			tableRow.setQuantity(qtyUnit);
			tableRow.setUnitPrice(unitPrice);
			;
			description = description.trim();
			tableRow.setDescription(description);
			tableList.add(tableRow);
			description = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
