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
 * @author mazhar.alam
 * since: 04 Aug 2020
 */
@Service
public class EnconInsulationService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.encon}")
	private String folderName;

	@Value("${import.encon.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.encon.type}")
	private String nuxeoinvoiceType;

	@Value("${import.encon.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${encon.ledgerNoRgx}")
	private String ledgerNoRgx;

	@Value("${encon.invoiceToRgx}")
	private String invoiceToRgx;

	@Value("${encon.invoiceRgxRgx}")
	private String invoiceRgxRgx;

	@Value("${encon.totalGoodsRgx}")
	private String totalGoodsRgx;

	@Value("${encon.totalVatRgx}")
	private String totalVatRgx;

	@Value("${encon.dueAmtRgx}")
	private String dueAmtRgx;

	@Value("${encon.companyNameRgx}")
	private String companyNameRgx;

	@Value("${encon.companyName}")
	private String companyName;

	@Value("${encon.vatNoRgx}")
	private String vatNoRgx;

	@Value("${encon.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${encon.tableRgx}")
	private String tableRgx;

	@Value("${encon.descRgx}")
	private String descRgx;

	@Value("${encon.desc2Rgx}")
	private String desc2Rgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.ENCON);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// ================== customer and delivery details =====================//
		String customer_detail = "", delivery_detail = "";
		dataArray = data.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			if (Pattern.compile(ledgerNoRgx).matcher(value).find()) {
				break;
			}
			if (Pattern.compile(invoiceToRgx).matcher(value).find()) {
				flag = true;
			} else if (flag) {
				value = value.replaceAll("^\\s{10}", "=").trim();
				String[] arr1 = value.split("\\s{7,}");
				customer_detail += "," + arr1[0].trim();
				try {
					delivery_detail += "," + arr1[1].trim();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
		customer_detail = customer_detail.replaceAll(",=", ",").replaceAll("\\,{2,}", "").trim();
		customer_detail = customer_detail.replaceAll("^,", "");
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		/*
		 * invoiceNo, invoiceDate, accNo, orderNo,
		 * despatchNo,total,netTotal,vatTotal,dueTotal
		 */
		String invoiceRgx = invoiceRgxRgx;
		String invoiceNo = "", invoiceDate = "", accNo = "", orderNo = "", despatchNo = "", total = "", netTotal = "",
				vatTotal = "", dueTotal = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceRgx).matcher(value).find()) {
				invoiceNo = value.replaceAll(invoiceRgx, "$7");
				invoice.setInvoiceNumber(invoiceNo);
				invoiceDate = value.replaceAll(invoiceRgx, "$6");
				invoice.setInvoiceDate(invoiceDate);
				despatchNo = value.replaceAll(invoiceRgx, "$5");// not set
				orderNo = value.replaceAll(invoiceRgx, "$3");
				invoice.setOrderNo(orderNo);
				accNo = value.replaceAll(invoiceRgx, "$2");
				invoice.setAccountNo(accNo);
			} else if (Pattern.compile(totalGoodsRgx).matcher(value).find()) {
				total = value;
				value = value.replaceAll(totalGoodsRgx, "");
				total = total.replaceAll(value.trim(), "").replaceAll(totalGoodsRgx, "$1").trim();
				invoice.setTotal(total);
			} else if (Pattern.compile(totalVatRgx).matcher(value).find()) {
				vatTotal = value;
				value = value.replaceAll(totalVatRgx, "");
				vatTotal = total.replaceAll(value.trim(), "").replaceAll(totalVatRgx, "$1").trim();
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(dueAmtRgx).matcher(value).find()) {
				dueTotal = value;
				value = value.replaceAll(dueAmtRgx, "");
				dueTotal = dueTotal.replaceAll(value.trim(), "").replaceAll(dueAmtRgx, "$1").trim();
				invoice.setDue(dueTotal);
			}
		}
		// ================== supplier detail =====================//
		subData = data.substring((int) data.length() / 2);
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(companyNameRgx);
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
		subData = subData.substring(start);
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatNoRgx);
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
		String supplierAddress = subData.substring(0, start);
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(companyName);
		String vatRegNo = subData.substring(start, end);
		vatRegNo = vatRegNo.replaceAll(vatNoRgx, "$1");
		invoice.setVatReg(vatRegNo);
		// ======================== table ========================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", prodCode = "", disc = "", qty = "", priceUnit = "", goodsTotal = "";
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

			if (Pattern.compile(totalGoodsRgx).matcher(value).find()) {
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
		 * outsize table operation
		 */
		description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (!value.equals(extra_string)) {
					if (pattern.matcher(value).find()) {
						prodCode = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						qty = value.replaceAll(table, "$3");
						priceUnit = value.replaceAll(table, "$4");
						disc = value.replaceAll(table, "$5");
						goodsTotal = value.replaceAll(table, "$5");
						table2.setProductCode(prodCode);
						table2.setQuantity(qty);
						table2.setUnitPrice(priceUnit);
						table2.setDiscount(disc);
						table2.setTotal(goodsTotal);
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
			table2.setDescription(description);
			tableList.add(table2);
			description = "";
		}

		invoice.setInvoiceTable(tableList);
		return invoice;

	}
}
