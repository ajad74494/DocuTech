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

import io.github.jonathanlink.PDFLayoutTextStripper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

public class AdescsService  implements PdfInvoiceProcessor {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.bristan}")
	private String folderName;

	@Value("${import.bristan.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.bristan.type}")
	private String nuxeoinvoiceType;

	@Value("${import.bristan.prefix}")
	private String prefix;

	@Value("${import.nuxeo.bristan.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${adescs.vatRgx}")
	private String vatRgx ;
	
	@Value("${adescs.pageRgx}")
	private String pageRgx ;
	
	@Value("${adescs.qtyRgx}")
	private String qtyRgx;
	
	@Value("${adescs.vatRegRgx}")
	private String vatRegRgx ;
	
	@Value("${adescs.telRgx}")
	private String telRgx ;
	
	@Value("${adescs.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${adescs.invoiceDateRgx}")
	private String invoiceDateRgx;
	
	@Value("${import.orderNoRgx}")
	private String orderNoRgx ;
	
	@Value("${import.accountNoRgx}")
	private String accountNoRgx ;
	
	@Value("${import.totalNetRgx}")
	private String totalNetRgx ;
	
	@Value("${import.totalVatRgx}")
	private String totalVatRgx ;
	
	@Value("${import.invoiceTotalRgx}")
	private String invoiceTotalRgx;
	
	@Value("${import.tableHeaderRgx}")
	private String tableHeaderRgx ;
	
	@Value("${import.tableRgx}")
	private String tableRgx ;
	
	@Value("${import.tableEndRgx}")
	private String tableEndRgx ;
	
	@Value("${import.descRgx}")
	private String descRgx ;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String data = "", textStriper = "";
		
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		// invoice.setSortName(Constants.VR);
		// invoice.setInvoiceTitel(nuxeoinvoiceName);
		// invoice.setInvoiceDescription(desc);
		// invoice.setPrefix(prefix);
		// invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String subDataXtra = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		String[] multiPageData = null;
		// =============== supplier details =================//
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(vatRgx));
		subData = subData.replaceAll(pageRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			if (!value.trim().equals(""))
				supplierAddress += value.trim() + "\n";
		}
		invoice.setSupplierAddress(supplierAddress);
		dataArray = supplierAddress.split("\\n");
		invoice.setSupplierAddress(dataArray[0]);
		// ========== customer details and others ================//
		String customerAddress = "";
		dataArray = data.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(qtyRgx).matcher(value).find()) {
				break;
			}
			if (Pattern.compile(vatRegRgx).matcher(value).find()) {
				String vatReg = value;
				value = value.replaceAll(vatRegRgx, "");
				vatReg = vatReg.replaceAll(value, "").replaceAll(vatRegRgx, "$1");
				invoice.setVatReg(vatReg);
				flag = true;
			} else if (flag) {
				if (Pattern.compile(telRgx).matcher(value).find()) {
					String telNo = value;
					value = value.replaceAll(telRgx, "");
					telNo = telNo.replaceAll(value, "").replaceAll(telRgx, "$1");
					invoice.setTelephone(telNo);
				} else if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
					String invoiceNo = value;
					value = value.replaceAll(invoiceNoRgx, "");
					invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");
					invoice.setInvoiceNumber(invoiceNo);
				} else if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
					String invoiceDate = value;
					value = value.replaceAll(invoiceDateRgx, "");
					invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(invoiceDateRgx, "$1");
					invoice.setInvoiceDate(invoiceDate);
				} else if (Pattern.compile(orderNoRgx).matcher(value).find()) {
					String orderNo = value;
					value = value.replaceAll(orderNoRgx, "");
					orderNo = orderNo.replaceAll(value, "").replaceAll(orderNoRgx, "$1");
					invoice.setOrderNo(orderNo);
				} else if (Pattern.compile(accountNoRgx).matcher(value).find()) {
					String accountNo = value;
					value = value.replaceAll(accountNoRgx, "");
					accountNo = accountNo.replaceAll(value, "").replaceAll(accountNoRgx, "$1");
					invoice.setAccountNo(accountNo);
				}
				if (!value.equals("")) {
					customerAddress += value.trim() + "\n";
				}
			}

		}
		invoice.setCustomerAddress(customerAddress);
		dataArray = customerAddress.split("\\n");
		invoice.setCustomerName(dataArray[0]);

		// ================= invoice table total =========================//
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(totalNetRgx).matcher(value).find()) {
				String netTotal = value;
				value = value.replaceAll(totalNetRgx, "");
				netTotal = netTotal.replaceAll(value, "").replaceAll(totalNetRgx, "$1");
				invoice.setNetTotal(netTotal);
			} else if (Pattern.compile(totalVatRgx).matcher(value).find()) {
				String totalVat = value;
				value = value.replaceAll(totalVatRgx, "");
				totalVat = totalVat.replaceAll(value, "").replaceAll(totalVatRgx, "$1");
				invoice.setVatTotal(totalVat);
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				String invoiceTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "");
				invoiceTotal = invoiceTotal.replaceAll(value, "").replaceAll(invoiceTotalRgx, "$1");
				invoice.setGrossTotal(invoiceTotal);
			}
		}
		// ================= table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String qty = "", description = "", unitPrice = "", netAmount = "", vatRate = "", vat = "";
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

		flag = false;

		int index = 0;
		for (String value : arr0) {
			value = value.trim();

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
		Pattern pattern;
		Matcher matcher;
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
						netAmount = value.replaceAll(table, "$4");
						vatRate = value.replaceAll(table, "$5");
						vat = value.replaceAll(table, "$6");

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
			/*
			 * setting values
			 */
			tableRow.setQuantity(qty);
			tableRow.setUnitPrice(unitPrice);
			tableRow.setNetAmount(netAmount);
			tableRow.setVatPercentage(vatRate);
			tableRow.setVatAmount(vat);
			tableRow.setDescription(description);
			tableList.add(tableRow);
			description = "";
		}
		invoice.setInvoiceTable(tableList);
		 return invoice;
	}
}
