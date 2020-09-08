package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author mazhar.alam
 * @since 2020-08-16
 *
 */
@Service
public class HSSService implements PdfInvoiceProcessorPdfBox {
	private static Logger log = LoggerFactory.getLogger(HSSService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.hssecurity}")
	private String folderName;

	@Value("${import.hssecurity.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.hssecurity.type}")
	private String nuxeoinvoiceType;

	@Value("${import.hssecurity.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${hss.supplierName}")
	private String supplierName;

	@Value("${hss.supplierAddress}")
	private String supplierAddress;

	@Value("${hss.telephone}")
	private String telephone;

	@Value("${hss.fax}")
	private String fax;

	@Value("${hss.website}")
	private String website;

	@Value("${hss.descriptionRgx}")
	private String descriptionRgx;

	@Value("${hss.unUsedDataRgx}")
	private String unUsedDataRgx;

	@Value("${hss.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${hss.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${hss.orderNoRgx}")
	private String orderNoRgx;

	@Value("${hss.orderDateSiteRgx}")
	private String orderDateSiteRgx;

	@Value("${hss.subTotalRgx}")
	private String subTotalRgx;

	@Value("${hss.vatRgx}")
	private String vatRgx;

	@Value("${hss.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${hss.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${hss.tableRgx}")
	private String tableRgx;

	@Value("${hss.tableEndRgx}")
	private String tableEndRgx;

	@Value("${hss.descRgx}")
	private String descRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		System.out.println(data);
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.HSSECURITYSERVICESLTD);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		// ============== supplier details ============== //
		invoice.setSupplierName(supplierName);
		invoice.setSupplierAddress(supplierAddress);
		invoice.setTelephone(telephone);
		invoice.setFax(fax);
		invoice.setWebsite(website);

		subData = data.substring(0, data.indexOf(descriptionRgx));
		subData = subData.replaceAll(unUsedDataRgx, "");
		/*
		 * invoiceNo
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(invoiceNoRgx);
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
		invoice.setInvoiceDate(invoiceDate);
		subData = subData.replaceAll(invoiceDateRgx, "");
		/*
		 * orderNo
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
		invoice.setOrderNo(orderNo);
		subData = subData.replaceAll(orderDateSiteRgx, "");
		/*
		 * customer and delivery details
		 */
		String customerAddress = "", deliveryAddress = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			value = value.replaceAll("^\\s{10}", "=").trim();
			String[] arr1 = value.split("\\s{11,}");
			customerAddress += "," + arr1[0].trim();
			try {
				deliveryAddress += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		customerAddress = customerAddress.replaceAll(",=", ",").replaceAll("\\,{2,}", "").trim();
		customerAddress = customerAddress.replaceAll("^,", "");
		invoice.setCustomerAddress(customerAddress);
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0]);
		deliveryAddress = deliveryAddress.replaceAll(",{2,}", "").replaceAll("^,", "");
		invoice.setDeliveryAddress(deliveryAddress);
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(subTotalRgx).matcher(value).find()) {
				String subTotal = value;
				value = value.replaceAll(subTotalRgx, "");
				subTotal = subTotal.substring(value.length()).replaceAll(subTotalRgx, "$1");
				invoice.setNetTotal(subTotal);
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vatTotal = value;
				value = value.replaceAll(vatRgx, "");
				vatTotal = vatTotal.substring(value.length()).replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				String grossTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "");
				grossTotal = grossTotal.substring(value.length()).replaceAll(invoiceTotalRgx, "$1");
				invoice.setGrossTotal(grossTotal);
			}
		}

		// ================= table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String date = "", description = "", stockNo = "", qty = "", price = "", per = "", preDiscount = "", disc = "",
				total;
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
						full_row += "========" + value;
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
						date = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						stockNo = value.replaceAll(table, "$3");
						qty = value.replaceAll(table, "$4");
						price = value.replaceAll(table, "$5");
						per = value.replaceAll(table, "$6");
						preDiscount = value.replaceAll(table, "$7");
						disc = value.replaceAll(table, "$8");
						total = value.replaceAll(table, "$9");
						/*
						 * setting values
						 */
						tableRow.setDate(date);
						tableRow.setStockCode(stockNo);
						tableRow.setQuantity(qty);
						tableRow.setPrice(price);
						tableRow.setPer(per);
						tableRow.setPreDiscount(preDiscount);
						tableRow.setDiscount(disc);
						tableRow.setTotal(total);

					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							description += value.replaceAll("\\s{1,}", " ").trim() + "\n";
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
