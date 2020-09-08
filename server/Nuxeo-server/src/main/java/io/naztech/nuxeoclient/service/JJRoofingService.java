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
public class JJRoofingService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.jjroofing}")
	private String folderName;

	@Value("${import.jjroofing.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.jjroofing.type}")
	private String nuxeoinvoiceType;

	@Value("${import.jjroofing.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${jjRoof.telRgx}")
	private String telRgx;

	@Value("${jjRoof.telFullRgx}")
	private String telFullRgx;

	@Value("${jjRoof.vatNumberRgx}")
	private String vatNumberRgx;

	@Value("${jjRoof.unusedDataRgx}")
	private String unusedDataRgx;

	@Value("${jjRoof.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${jjRoof.accountNoRgx}")
	private String accountNoRgx;

	@Value("${jjRoof.dateRgx}")
	private String dateRgx;

	@Value("${jjRoof.orderByRgx}")
	private String orderByRgx;

	@Value("${jjRoof.deliAddressRgx}")
	private String deliAddressRgx;

	@Value("${jjRoof.deliAddressFullRgx}")
	private String deliAddressFullRgx;

	@Value("${jjRoof.currencyRgx}")
	private String currencyRgx;

	@Value("${jjRoof.goodsTotalRgx}")
	private String goodsTotalRgx;

	@Value("${jjRoof.vatRgx}")
	private String vatRgx;

	@Value("${jjRoof.totalDueRgx}")
	private String totalDueRgx;

	@Value("${jjRoof.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${jjRoof.tableRgx}")
	private String tableRgx;

	@Value("${jjRoof.tableEndRgx}")
	private String tableEndRgx;

	@Value("${jjRoof.descRgx}")
	private String descRgx;

	@Value("${jjRoof.toRgx}")
	private String toRgx;

	@Value("${jjRoof.descriptionRgx}")
	private String descriptionRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = pdfStr, textStriper = pdfBoxStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		invoice.setSortName(Constants.JJROOFING);
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String subDataXtra = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		String[] multiPageData = null;
		/*
		 * supplier address
		 */
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(telRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				supplierAddress += "," + value;
			}
		}
		supplierAddress = supplierAddress.replaceAll("^,", "");
		invoice.setSupplierAddress(supplierAddress);
		dataArray = supplierAddress.split(",");
		invoice.setSupplierName(dataArray[0]);
		/*
		 * Telephone
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(telFullRgx);
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

		String phone = data.substring(start, end);
		phone = phone.replaceAll(telFullRgx, "$1");
		invoice.setTelephone(phone.trim());
		/*
		 * vat no, reg no
		 */
		dataArray = textStriper.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(vatNumberRgx).matcher(value).find()) {
				String vatNumber = value.replaceAll(vatNumberRgx, "$1");
				invoice.setVatNo(vatNumber);
				String companyReg = value.replaceAll(vatNumberRgx, "$2");
				invoice.setRegNo(companyReg);
			}
		}
		/*
		 * customer and delivery address
		 */
		String details = "";
		subData = data.substring(data.indexOf(toRgx), data.indexOf(descriptionRgx));
		subData = subData.replaceAll(unusedDataRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				String invoiceNo = value.trim();
				value = value.replaceAll(invoiceNoRgx, "").trim();
				invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo.trim());
			} else if (Pattern.compile(accountNoRgx).matcher(value).find()) {
				String accountNo = value.trim();
				value = value.replaceAll(accountNoRgx, "").trim();
				accountNo = accountNo.replaceAll(value, "").replaceAll(accountNoRgx, "$1");
				invoice.setAccountNo(accountNo.trim());
			} else if (Pattern.compile(dateRgx).matcher(value).find()) {
				String invoiceDate = value.trim();
				value = value.replaceAll(dateRgx, "").trim();
				invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(dateRgx, "$1");
				invoice.setInvoiceDate(invoiceDate.trim());
			} else if (Pattern.compile(orderByRgx).matcher(value).find()) {
				String ref = value.trim();
				value = value.replaceAll(orderByRgx, "").trim();
				ref = ref.replaceAll(value, "").replaceAll(orderByRgx, "$1");
				invoice.setReferenceNumber(ref.trim());
			}
			if (!value.equals("")) {
				details += value.concat(",");
			}
		}
		/*
		 * separating customer and delivery details from details variable
		 */
		dataArray = details.split(deliAddressRgx);
		String customerAddress = dataArray[0].replaceAll(deliAddressFullRgx, "");
		String deliveryAddress = dataArray[1];
		invoice.setCustomerAddress(customerAddress);
		/* spliting customer address for customer name */
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0]);
		invoice.setDeliveryAddress(deliveryAddress);
		/*
		 * 
		 * */
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(currencyRgx).matcher(value).find()) {
				String currency = value.trim();
				value = value.replaceAll(currencyRgx, "").trim();
				currency = currency.replaceAll(value, "").replaceAll(currencyRgx, "$1");
			} else if (Pattern.compile(goodsTotalRgx).matcher(value).find()) {
				String goodTotal = value.trim();
				value = value.replaceAll(goodsTotalRgx, "").trim();
				goodTotal = goodTotal.replaceAll(value, "").replaceAll(goodsTotalRgx, "$1");

				invoice.setNetTotal(goodTotal.trim());
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vatTotal = value.trim();
				value = value.replaceAll(vatRgx, "").trim();
				vatTotal = vatTotal.replaceAll(value, "").replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vatTotal.trim());

			} else if (Pattern.compile(totalDueRgx).matcher(value).find()) {
				String total = value.trim();
				value = value.replaceAll(totalDueRgx, "").trim();
				total = total.replaceAll(value, "").replaceAll(totalDueRgx, "$1");
				invoice.setTotal(total.trim());

			}
		}
		// ================== table ===================//
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", qty = "", per = "", unitPrice = "", lineTotal = "", vat = "";
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
						description = value.replaceAll(table, "$1").trim();
						qty = value.replaceAll(table, "$2").trim();
						per = value.replaceAll(table, "$3").trim();
						unitPrice = value.replaceAll(table, "$4").trim();
						lineTotal = value.replaceAll(table, "$5").trim();// lineTotal
						vat = value.replaceAll(table, "$5").trim();
						/*
						 * setting values
						 */
						tableRow.setPer(per);
						tableRow.setUnitPrice(unitPrice);
						tableRow.setQuantity(qty);
						tableRow.setNetAmount(lineTotal);
						tableRow.setVatAmount(vat);
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
