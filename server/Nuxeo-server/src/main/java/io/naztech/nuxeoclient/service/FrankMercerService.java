package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.jonathanlink.PDFLayoutTextStripper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author masud.ahmed
 * @since 2020-07-16
 */
@Service
public class FrankMercerService implements PdfInvoiceProcessor {
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
	
	@Value("${frankMercer.qtyRgx}")
	private String qtyRgx ;
	
	@Value("${frankMercer.deliAddressRgx}")
	private String deliAddressRgx ;
	
	@Value("${frankMercer.invoiceNddeliveryRgx}")
	private String invoiceNddeliveryRgx ;
	
	@Value("${frankMercer.telRgx}")
	private String telRgx ;
	
	@Value("${frankMercer.supplierNameRgx}")
	private String supplierNameRgx ;
	
	@Value("${frankMercer.telphoneRgx}")
	private String telphoneRgx ;
	
	@Value("${frankMercer.faxRgx}")
	private String faxRgx ;
	
	@Value("${frankMercer.emailRgx}")
	private String emailRgx ;
	
	@Value("${frankMercer.webRgx}")
	private String webRgx ;
	
	@Value("${frankMercer.vatRegRgx}")
	private String vatRegRgx ;
	
	@Value("${frankMercer.RegNoRgx}")
	private String RegNoRgx ;
	
	@Value("${frankMercer.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${frankMercer.invoiceDateRgx}")
	private String invoiceDateRgx ;
	
	@Value("${frankMercer.orderNoRgx}")
	private String orderNoRgx ;
	
	@Value("${frankMercer.accountNoRgx}")
	private String accountNoRgx ;
	
	@Value("${frankMercer.netRgx}")
	private String netRgx ;
	
	@Value("${frankMercer.vatRgx}")
	private String vatRgx ;
	
	@Value("${frankMercer.totalRgx}")
	private String totalRgx ;
	
	@Value("${frankMercer.tableHeaderRgx}")
	private String tableHeaderRgx ;
	
	@Value("${frankMercer.tableRgx}")
	private String tableRgx ;
	
	@Value("${frankMercer.tableEndRgx}")
	private String tableEndRgx ;
	
	@Value("${frankMercer.descRgx}")
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

		// ============= details ================ //
		String details = "", invoiceDetails = "";
		subData = data.substring(0, data.indexOf(qtyRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			value = value.replaceAll("^\\s{4}", "=").trim();
			String[] arr1 = value.split("\\s{10,}");
			details += "," + arr1[0].trim();
			try {
				invoiceDetails += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		details = details.replaceAll(",=", ",").replaceAll("\\,{2,}", ",").trim();
		details = details.replaceAll("^,", "");
		dataArray = details.split(deliAddressRgx);
		String customerAddress = dataArray[0].replaceAll(invoiceNddeliveryRgx, "");
		invoice.setCustomerAddress(customerAddress.trim());
		String deliveryAddress = dataArray[1].replaceAll("^,", "");
		invoice.setDeliveryAddress(deliveryAddress);
		/*
		 * spliting customer address for customer name
		 */
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0].trim());
		/*
		 * supplier address
		 */
		String supplierAddress = invoiceDetails.substring(0, invoiceDetails.indexOf(telRgx)).replaceAll("^,", "");
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(supplierNameRgx);
		/*
		 * tel
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(telphoneRgx);
		Matcher matcher = pattern.matcher(invoiceDetails);
		boolean found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String telePhone = invoiceDetails.substring(start, end).replaceAll(telphoneRgx, "$1");
		invoice.setTelephone(telePhone);
		/*
		 * fax
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(faxRgx);
		matcher = pattern.matcher(invoiceDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String fax = invoiceDetails.substring(start, end).replaceAll(faxRgx, "$1");
		invoice.setFax(fax);
		/*
		 * email
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(emailRgx);
		matcher = pattern.matcher(invoiceDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String email = invoiceDetails.substring(start, end).replaceAll(emailRgx, "$1");
		invoice.setEmail(email);
		/*
		 * web
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(webRgx);
		matcher = pattern.matcher(invoiceDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String website = invoiceDetails.substring(start, end);
		invoice.setWebsite(website);
		/*
		 * tel
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatRegRgx);
		matcher = pattern.matcher(invoiceDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatReg = invoiceDetails.substring(start, end).replaceAll(vatRegRgx, "$1");
		;
		invoice.setVatReg(vatReg);
		/*
		 * regNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(RegNoRgx);
		matcher = pattern.matcher(invoiceDetails);
		found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String regNo = invoiceDetails.substring(start, end).replaceAll(RegNoRgx, "$1");
		invoice.setRegNo(regNo);
		/*
		 * invoice no
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
		String invoiceNo = data.substring(start, end).replaceAll(invoiceNoRgx, "$1");
		invoice.setInvoiceNumber(invoiceNo);
		/*
		 * invoice date
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(invoiceDateRgx);
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
		String invoiceDate = data.substring(start, end).replaceAll(invoiceDateRgx, "$1");
		invoice.setInvoiceDate(invoiceDate);
		/*
		 * orderNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(orderNoRgx);
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
		String orderNo = data.substring(start, end).replaceAll(orderNoRgx, "$1");
		invoice.setOrderNo(orderNo);
		/*
		 * accNo
		 */
		start = 0;
		end = 0;
		pattern = Pattern.compile(accountNoRgx);
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
		String accNo = data.substring(start, end).replaceAll(accountNoRgx, "$1");
		invoice.setAccountNo(accNo);
		// =========== invoice totals =============//
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(netRgx).matcher(value).find()) {
				String net = value;
				value = value.replaceAll(netRgx, "");
				net = net.replaceAll(value, "").replaceAll(netRgx, "$1");
				invoice.setNetTotal(net.trim());
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vat = value;
				value = value.replaceAll(vatRgx, "");
				vat = vat.replaceAll(value, "").replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vat);
			} else if (Pattern.compile(totalRgx).matcher(value).find()) {
				String total = value;
				value = value.replaceAll(totalRgx, "");
				total = total.replaceAll(value, "").replaceAll(totalRgx, "$1");
				invoice.setGrossTotal(total);
			}
		}

		// ================== table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String qty = "", description = "", unitPrice = "", netAmount = "";
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

						/*
						 * setting values
						 */
						tableRow.setNetAmount(netAmount);
						;
						tableRow.setUnitPrice(unitPrice);
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
