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
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * @author masud.ahmed
 */
@Service
public class UkTruckPertService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.uktruck}")
	private String folderName;

	@Value("${import.uktruck.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.uktruck.type}")
	private String nuxeoinvoiceType;

	@Value("${import.uktruck.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${ukTruck.invoiceToRgx}")
	private String invoiceToRgx;

	@Value("${ukTruck.deliMethodRgx}")
	private String deliMethodRgx;

	@Value("${ukTruck.invoiceRgx}")
	private String invoiceRgx;

	@Value("${ukTruck.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${ukTruck.dateRgx}")
	private String dateRgx;

	@Value("${ukTruck.orderRefRgx}")
	private String orderRefRgx;

	@Value("${ukTruck.accountNoRgx}")
	private String accountNoRgx;

	@Value("${ukTruck.telAndFaxRgx}")
	private String telAndFaxRgx;

	@Value("${ukTruck.operatorRgx}")
	private String operatorRgx;

	@Value("${ukTruck.unusedDataRgx}")
	private String unusedDataRgx;

	@Value("${ukTruck.goodsRgx}")
	private String goodsRgx;

	@Value("${ukTruck.vatRgx}")
	private String vatRgx;

	@Value("${ukTruck.totalRgx}")
	private String totalRgx;

	@Value("${ukTruck.email_Vat_RegNoRgx}")
	private String email_Vat_RegNoRgx;

	@Value("${ukTruck.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${ukTruck.tableRgx}")
	private String tableRgx;

	@Value("${ukTruck.tableEndRgx}")
	private String tableEndRgx;

	@Value("${ukTruck.descRgx}")
	private String descRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = "", textStriper = "";
		try {
			PDDocument document = PDDocument.load(new File("./data/UK_Truckparts.PDF"));
			PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
			data = pdfTextStripper.getText(document);
			PDFTextStripper tStripper = new PDFTextStripper();
			textStriper = tStripper.getText(document);
			document.close();
		} catch (Exception e) {
		}
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
		// =============== supplier details ================//
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(invoiceToRgx));
		subData = subData.replaceAll(invoiceRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				String invoiceNo = value;
				value = value.replaceAll(invoiceNoRgx, "");
				invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo.trim());
			} else if (Pattern.compile(dateRgx).matcher(value).find()) {
				String invoiceDate = value;
				value = value.replaceAll(dateRgx, "");
				invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(dateRgx, "$1");
				invoice.setInvoiceDate(invoiceDate.trim());
			} else if (Pattern.compile(orderRefRgx).matcher(value).find()) {
				String orderRef = value;
				value = value.replaceAll(orderRefRgx, "");
				orderRef = orderRef.replaceAll(value, "").replaceAll(orderRefRgx, "$1");
				invoice.setReferenceNumber(orderRef.trim());
			} else if (Pattern.compile(accountNoRgx).matcher(value).find()) {
				String accountNo = value;
				value = value.replaceAll(accountNoRgx, "");
				accountNo = accountNo.replaceAll(value, "").replaceAll(accountNoRgx, "$1");
				invoice.setAccountNo(accountNo.trim());
			} else if (Pattern.compile(telAndFaxRgx).matcher(value).find()) {
				String phone = value;
				value = value.replaceAll(telAndFaxRgx, "");
				phone = phone.replaceAll(value, "").replaceAll(telAndFaxRgx, "$1");
				invoice.setTelephone(phone.trim());
				String fax = phone.replaceAll(value, "").replaceAll(telAndFaxRgx, "$2");
				invoice.setFax(fax.trim());
			} else if (Pattern.compile(operatorRgx).matcher(value).find()) {
				value = value.replaceAll(operatorRgx, "");
			}
			if (!value.equals("")) {
				supplierAddress += "," + value.trim();
			}
		}
		supplierAddress = supplierAddress.replaceAll("^,", "");
		/*
		 * spliting into dataArray using ',' for supplier name index 0 -> supplier name
		 */
		dataArray = supplierAddress.split(",");
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(dataArray[0]);
		// ================== customer and delivery details ================= //
		String customer_detail = "", delivery_detail = "";
		subData = data.substring(data.indexOf(invoiceToRgx), data.indexOf(deliMethodRgx));
		subData = subData.replaceAll(unusedDataRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("^\\s{15}", "=").trim();
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
		// =============== invoice totals and others ================= //
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(goodsRgx).matcher(value).find()) {
				String goods = value;
				value = value.replaceAll(goodsRgx, "");
				goods = goods.replaceAll(value, "").replaceAll(goodsRgx, "$1");
				invoice.setNetTotal(goods.trim());
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vatTotal = value;
				value = value.replaceAll(vatRgx, "");
				vatTotal = vatTotal.substring(value.length()).replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(totalRgx).matcher(value).find()) {
				String total = value;
				value = value.replaceAll(totalRgx, "");
				total = total.replaceAll(value, "").replaceAll(totalRgx, "$1");
				invoice.setGrossTotal(total);
			} else if (Pattern.compile(email_Vat_RegNoRgx).matcher(value).find()) {
				String details = value;
				value = value.replaceAll(email_Vat_RegNoRgx, "");
				String email = details.replaceAll(value, "").replaceAll(email_Vat_RegNoRgx, "$1");
				String vatNo = details.replaceAll(value, "").replaceAll(email_Vat_RegNoRgx, "$2");
				String regNo = details.replaceAll(value, "").replaceAll(email_Vat_RegNoRgx, "$3");
				invoice.setEmail(email);
				invoice.setVatNo(vatNo);
				invoice.setRegNo(regNo);
			}
		}
		// ================== table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String partNo = "", description = "", unitPrice = "", locn = "", qty = "", price = "", total = "";
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
						partNo = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						locn = value.replaceAll(table, "$3");
						unitPrice = value.replaceAll(table, "$4");
						qty = value.replaceAll(table, "$5");
						price = value.replaceAll(table, "$6");
						total = value.replaceAll(table, "$7");
						/*
						 * setting values
						 */
						tableRow.setUnitPrice(unitPrice);
						tableRow.setPrice(price);
						tableRow.setTotal(total);
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
