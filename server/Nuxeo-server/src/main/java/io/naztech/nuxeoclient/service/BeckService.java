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
public class BeckService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.beckgroup}")
	private String folderName;

	@Value("${import.beckgroup.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.beckgroup.type}")
	private String nuxeoinvoiceType;

	@Value("${import.beckgroup.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${beck.telRgx}")
	private String telRgx = getProperticesValues("beck.telRgx");

	@Value("${beck.supplierNameRgx}")
	private String supplierNameRgx = getProperticesValues("beck.supplierNameRgx");

	@Value("${beck.invoiceRgx}")
	private String invoiceRgx = getProperticesValues("beck.invoiceRgx");

	@Value("${beck.contractRgx}")
	private String contractRgx = getProperticesValues("beck.contractRgx");

	@Value("${beck.invoiceAddressRgx}")
	private String invoiceAddressRgx = getProperticesValues("beck.invoiceAddressRgx");

	@Value("${beck.invNoRgx}")
	private String invNoRgx = getProperticesValues("beck.invNoRgx");

	@Value("${beck.extraStrRgx}")
	private String extraStrRgx = getProperticesValues("beck.extraStrRgx");

	@Value("${beck.invoiceNoRgx}")
	private String invoiceNoRgx = getProperticesValues("beck.invoiceNoRgx");

	@Value("${beck.invoiceDateRgx}")
	private String invoiceDateRgx = getProperticesValues("beck.invoiceDateRgx");

	@Value("${beck.refNoRgx}")
	private String refNoRgx = getProperticesValues("beck.refNoRgx");

	@Value("${beck.accountNoRgx}")
	private String accountNoRgx = getProperticesValues("beck.accountNoRgx");

	@Value("${beck.dueDateRgx}")
	private String dueDateRgx = getProperticesValues("beck.dueDateRgx");

	@Value("${beck.subTotalRgx}")
	private String subTotalRgx = getProperticesValues("beck.subTotalRgx");

	@Value("${beck.vatTotalRgx}")
	private String vatTotalRgx = getProperticesValues("beck.vatTotalRgx");

	@Value("${beck.totalRgx}")
	private String totalRgx = getProperticesValues("beck.totalRgx");

	@Value("${beck.vatRegNoRgx}")
	private String vatRegNoRgx = getProperticesValues("beck.vatRegNoRgx");

	@Value("${beck.tableHeaderRgx}")
	private String tableHeaderRgx = getProperticesValues("beck.tableHeaderRgx");

	@Value("${beck.tableRgx}")
	private String tableRgx = getProperticesValues("beck.tableRgx");

	@Value("${beck.tableEndRgx}")
	private String tableEndRgx = getProperticesValues("beck.tableEndRgx");

	@Value("${beck.descRgx}")
	private String descRgx = getProperticesValues("beck.descRgx");

	@Value("${beck.desc2Rgx}")
	private String desc2Rgx = getProperticesValues("beck.desc2Rgx");

	public String getProperticesValues(String key) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("./src/main/resources/invoice.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";

	}

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		 invoice.setSortName(Constants.BECK);
		 invoice.setInvoiceTitle(nuxeoinvoiceName);
		 invoice.setInvoiceDescription(desc);
		 invoice.setPrefix(prefix);
		 invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// =============== supplier details ================//
		subData = data.substring(0, data.indexOf(telRgx));
		String supplierAddress = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				supplierAddress += "," + value;
			}

		}
		supplierAddress = supplierAddress.replaceAll("^,", "");
		invoice.setSupplierName(supplierNameRgx);
		invoice.setSupplierAddress(supplierAddress);
		/*
		 * telephone
		 */
		subData = data.substring(data.indexOf(telRgx), data.indexOf(invoiceRgx)).trim();

		String tel = subData.replaceAll(contractRgx, "$1");
		invoice.setTelephone(tel);
		String email = subData.replaceAll(contractRgx, "$2");
		invoice.setEmail(email);
		String web = subData.replaceAll(contractRgx, "$3");
		invoice.setWebsite(web);
		String fax = subData.replaceAll(contractRgx, "$4");
		invoice.setFax(fax);
		// ================= customer and delivery details =================//
		String customer_detail = "", delivery_detail = "";
		subData = data.substring(data.indexOf(invoiceAddressRgx), data.indexOf(invNoRgx));
		subData = subData.replaceAll(extraStrRgx, "");
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
		customer_detail = customer_detail.replaceAll("^,", "");
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		// ================ invoice details =================//
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				String invoiceNo = value.trim();
				value = value.replaceAll(invoiceNoRgx, "").trim();
				invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				String invoiceDate = value.trim();
				value = value.replaceAll(invoiceDateRgx, "").trim();
				invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(invoiceDateRgx, "$1");
				invoice.setInvoiceDate(invoiceDate);
			} else if (Pattern.compile(refNoRgx).matcher(value).find()) {
				String orderNo = value.trim();
				value = value.replaceAll(refNoRgx, "").trim();
				orderNo = orderNo.replaceAll(value, "").replaceAll(refNoRgx, "$1");
				invoice.setOrderNo(orderNo);
			} else if (Pattern.compile(accountNoRgx).matcher(value).find()) {
				String accNo = value.trim();
				value = value.replaceAll(accountNoRgx, "").trim();
				accNo = accNo.replaceAll(value, "").replaceAll(accountNoRgx, "$1");
				invoice.setAccountNo(accNo);
			} else if (Pattern.compile(dueDateRgx).matcher(value).find()) {
				String dueDate = value.trim();
				value = value.replaceAll(dueDateRgx, "").trim();
				dueDate = dueDate.replaceAll(value, "").replaceAll(dueDateRgx, "$1");// not set
			} else if (Pattern.compile(subTotalRgx).matcher(value).find()) {
				String subTotal = value.trim();
				value = value.replaceAll(subTotalRgx, "").trim();
				subTotal = subTotal.replaceAll(value, "").replaceAll(subTotalRgx, "$1");// not
																						// set
				invoice.setNetTotal(subTotal.trim());
			} else if (Pattern.compile(vatTotalRgx).matcher(value).find()) {
				String vatTotal = value.trim();
				value = value.replaceAll(vatTotalRgx, "").trim();
				vatTotal = vatTotal.replaceAll(value, "").replaceAll(vatTotalRgx, "$1");// not set
				invoice.setVatTotal(vatTotal.trim());
			} else if (Pattern.compile(totalRgx).matcher(value).find()) {
				String total = value.trim();
				value = value.replaceAll(totalRgx, "").trim();
				total = total.replaceAll(value, "").replaceAll(totalRgx, "$1");// not set
				invoice.setTotal(total.trim());
			}

		}

		/*
		 * Vat Reg No
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(vatRegNoRgx);
		Matcher matcher = pattern.matcher(textStriper);
		boolean found = false;
		while (matcher.find()) {

			start = matcher.start();
			end = matcher.end();
			found = true;
		}
		if (!found) {
			// not found

		}
		String vatReg = textStriper.substring(start, end);
		vatReg = vatReg.replaceAll(vatRegNoRgx, "$1");
		invoice.setVatReg(vatReg);
		// ================== table ===================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String qty = "", unit = "", code = "", description = "", unitPrice = "", disc = "", subTotal;
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
						unit = value.replaceAll(table, "$2");
						code = value.replaceAll(table, "$3");
						description = value.replaceAll(table, "$4");
						unitPrice = value.replaceAll(table, "$5");
						disc = value.replaceAll(table, "$6");
						subTotal = value.replaceAll(table, "$7");
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
