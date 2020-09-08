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
public class AngloNordenService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.anglonorden}")
	private String folderName;

	@Value("${import.anglonorden.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.anglonorden.type}")
	private String nuxeoinvoiceType;

	@Value("${import.anglonorden.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${angloNorden.pageRgx}")
	private String pageRgx;

	@Value("${angloNorden.customerRgx}")
	private String customerRgx;

	@Value("${angloNorden.custEndRgx}")
	private String custEndRgx;

	@Value("${angloNorden.invoiceAddressRgx}")
	private String invoiceAddressRgx;

	@Value("${angloNorden.deliveryAddressRgx}")
	private String deliveryAddressRgx;

	@Value("${angloNorden.invoiceNdSaleRgx}")
	private String invoiceNdSaleRgx;

	@Value("${angloNorden.phoneRgx}")
	private String phoneRgx;

	@Value("${angloNorden.faxRgx}")
	private String faxRgx;

	@Value("${angloNorden.faoRgx}")
	private String faoRgx;

	@Value("${angloNorden.limitedRgx}")
	private String limitedRgx;

	@Value("${angloNorden.limited2Rgx}")
	private String limited2Rgx;

	@Value("${angloNorden.vatRegRgx}")
	private String vatRegRgx;

	@Value("${angloNorden.accNoRgx}")
	private String accNoRgx;

	@Value("${angloNorden.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${angloNorden.tableRgx}")
	private String tableRgx;

	@Value("${angloNorden.totalAmtRgx}")
	private String totalAmtRgx;

	@Value("${angloNorden.totalVatRgx}")
	private String totalVatRgx;

	@Value("${angloNorden.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		 invoice.setSortName(Constants.ANGLONORDEN);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String subDataXtra = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		String[] multiPageData = null;
		// ============= invoice details =============//
		subData = textStriper.substring(0, textStriper.indexOf(pageRgx));
		subDataXtra = subData.substring(subData.indexOf(customerRgx), subData.indexOf(custEndRgx));
		dataArray = subDataXtra.split("\\n");
		invoice.setInvoiceNumber(dataArray[2]);
		invoice.setInvoiceDate(dataArray[1]);
		invoice.setReferenceNumber(dataArray[4]);
		// ============= supplier details =============//
		subDataXtra = subData.substring(subData.indexOf(invoiceAddressRgx), subData.indexOf(deliveryAddressRgx));
		subDataXtra = subDataXtra.replaceAll(invoiceNdSaleRgx, "");
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(phoneRgx);
		Matcher matcher = pattern.matcher(subDataXtra);
		boolean found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String phone = subDataXtra.substring(start, end);
		phone = phone.replaceAll(phoneRgx, "$1");
		invoice.setTelephone(phone);
		subDataXtra = subDataXtra.replaceAll(phoneRgx, "");
		/*
		 * fax
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(faxRgx);
		matcher = pattern.matcher(subDataXtra);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String fax = subDataXtra.substring(start, end);
		fax = fax.replaceAll(faxRgx, "$1");
		invoice.setFax(fax);
		subDataXtra = subDataXtra.replaceAll(faxRgx, "").trim();
		/*
		 * supplier address
		 **/
		String supplierAddress = "";
		dataArray = subDataXtra.split("\\r\\n");
		for (String value : dataArray) {
			supplierAddress += "," + value;
		}
		invoice.setSupplierAddress(supplierAddress.replaceAll("^,", ""));
		invoice.setSupplierName(dataArray[dataArray.length - 1]);
		// ============= customer and delivery details =============//
		subDataXtra = subData.substring(subData.indexOf(deliveryAddressRgx), subData.indexOf(faoRgx));
		dataArray = subDataXtra.split(limitedRgx);
		invoice.setCustomerName(dataArray[0].replaceAll(deliveryAddressRgx, "") + limited2Rgx);
		invoice.setCustomerName(dataArray[1]);
		invoice.setDeliveryAddress(dataArray[1] + "," + dataArray[2]);
		/*
		 * vat reg no
		 */
		String vatReg = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(vatRegRgx).matcher(value).find()) {
				vatReg = value;
				value = value.replaceAll(vatRegRgx, "");
				vatReg = vatReg.substring(value.length());
				vatReg = vatReg.replaceAll(vatRegRgx, "$1");
				invoice.setVatReg(vatReg);
				break;
			}
		}
		/*
		 * accNo
		 */
		String accNo = "";
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(accNoRgx).matcher(value).find()) {
				accNo = value;
				value = value.replaceAll(accNoRgx, "");
				accNo = accNo.substring(value.length());
				accNo = accNo.replaceAll(accNoRgx, "$1");
				invoice.setAccountNo(accNo);
				break;
			}
		}

		// ======================== table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", item = "", qty = "", per_1 = "", price = "", per_2 = "", total = "", vat = "",
				rate = "";
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
			if (Pattern.compile(totalAmtRgx).matcher(value).find()) {
				start = 0;
				end = 0;
				pattern = Pattern.compile(totalAmtRgx);
				matcher = pattern.matcher(value);
				found = false;
				while (matcher.find()) {
					start = matcher.start();
					end = matcher.end();
					found = true;
				}
				if (!found) {
					// not found
				}
				checkTotal = value.substring(start, end);
				checkTotal = checkTotal.replaceAll(totalAmtRgx, "$1").trim();
				if (checkTotal.equals("")) {
					full_row += "";
					flag = false;
				} else {

					listStr.add(full_row);
					full_row = "";
					break;
				}

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
						description = value.replaceAll(table, "$1");
						item = value.replaceAll(table, "$2");
						description = value.replaceAll(table, "$3");
						qty = value.replaceAll(table, "$4");
						per_1 = value.replaceAll(table, "$5");
						price = value.replaceAll(table, "$1");
						per_2 = value.replaceAll(table, "$2");
						total = value.replaceAll(table, "$3");
						vat = value.replaceAll(table, "$4");
						rate = value.replaceAll(table, "$4");

						/*
						 * setting values
						 */
						tableRow.setQuantity(qty);
						tableRow.setTotal(total);
						tableRow.setVatPercentage(rate);
					} else {
						pattern = Pattern.compile("[A-Za-z0-9]+");
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
		// ================== invoice table total details ==================//
		String netTotal = "", vatTotal = "", invoiceTotal = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(totalAmtRgx).matcher(value).find()) {
				netTotal = value;
				value = value.replaceAll(totalAmtRgx, "");
				netTotal = netTotal.replaceAll(value, "").replaceAll(totalAmtRgx, "$1");
				invoice.setTotal(netTotal);
			} else if (Pattern.compile(totalVatRgx).matcher(value).find()) {
				vatTotal = value;
				value = value.replaceAll(totalVatRgx, "");
				vatTotal = vatTotal.replaceAll(value, "").replaceAll(totalVatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				invoiceTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "");
				invoiceTotal = invoiceTotal.replaceAll(value, "").replaceAll(invoiceTotalRgx, "$1");
				invoice.setNetTotal(invoiceTotal);
			}
		}
		return invoice;
	}
}
