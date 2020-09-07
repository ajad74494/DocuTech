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

public class TaxService implements PdfInvoiceProcessor {
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

	@Value("${tax.descriptionRgx}")
	private String descriptionRgx ;

	@Value("${tax.invoiceDateRgx}")
	private String invoiceDateRgx ;

	@Value("${tax.companyRgx}")
	private String companyRgx;

	@Value("${tax.telFullRgx}")
	private String telFullRgx ;

	@Value("${tax.telRgx}")
	private String telRgx ;

	@Value("${tax.supplierRgx}")
	private String supplierRgx ;

	@Value("${tax.customerRgx}")
	private String customerRgx ;

	@Value("${tax.detailsRgx}")
	private String detailsRgx;

	@Value("${tax.subTotalRgx}")
	private String subTotalRgx ;

	@Value("${tax.totalVatRgx}")
	private String totalVatRgx ;

	@Value("${tax.totalGBPRgx}")
	private String totalGBPRgx;

	@Value("${tax.dueDateRgx}")
	private String dueDateRgx ;

	@Value("${tax.tableHeaderRgx}")
	private String tableHeaderRgx ;

	@Value("${tax.tableRgx}")
	private String tableRgx ;

	@Value("${tax.tableEndRgx}")
	private String tableEndRgx ;

	@Value("${tax.descRgx}")
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
		// ===================== customer details and supplier details
		// ====================== //
		String customerAddress = "", supplierAddress = "";
		int dataIndex = 0;
		int dataIndexExt = 0;
		subData = data.substring(0, data.indexOf(descriptionRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				dataIndex = value.indexOf(invoiceDateRgx);
			}
			try {
				value = value.substring(0, dataIndex);
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					customerAddress += "," + value;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		/*
		 * supplier address
		 */
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			if (Pattern.compile(companyRgx).matcher(value).find()) {
				dataIndexExt = value.indexOf(companyRgx);
			}
			try {
				value = value.substring(dataIndexExt);
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					supplierAddress += "," + value;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		/*
		 * supplier details values
		 */
		String tel = supplierAddress.substring(supplierAddress.indexOf(telRgx)).trim();
		tel = tel.replaceAll(telFullRgx, "$1");
		invoice.setTelephone(tel);
		supplierAddress = supplierAddress.replaceAll(supplierRgx, "");
		invoice.setSupplierAddress(supplierAddress);
		dataArray = supplierAddress.split(",");
		invoice.setSupplierName(dataArray[0]);
		/*
		 * customer details values
		 **/
		customerAddress = customerAddress.replaceAll(customerRgx, "");
		invoice.setCustomerAddress(customerAddress);
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0]);
		/*
		 * date,invoiceNo,ref,vatNo
		 */
		String details = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			try {
				value = value.substring(dataIndex, dataIndexExt);
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					details += " " + value;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		details = details.trim();
		String invoiceDate = details.replaceAll(detailsRgx, "$1");
		String invoiceNo = details.replaceAll(detailsRgx, "$2");
		String RefNo = details.replaceAll(detailsRgx, "$3");
		String vatNo = details.replaceAll(detailsRgx, "$4");
		invoice.setInvoiceDate(invoiceDate);
		invoice.setInvoiceNumber(invoiceNo);
		invoice.setReferenceNumber(RefNo);
		invoice.setVatNo(vatNo);
		// ============== ***************** ============== //

		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(subTotalRgx).matcher(value).find()) {
				String netTotal = value.replaceAll(subTotalRgx, "$1");
				invoice.setNetTotal(netTotal);
			} else if (Pattern.compile(totalVatRgx).matcher(value).find()) {
				String vatTotal = value.replaceAll(totalVatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(totalGBPRgx).matcher(value).find()) {
				String grossTotal = value.replaceAll(totalGBPRgx, "$1");
				invoice.setGrossTotal(grossTotal);
			} else if (Pattern.compile(dueDateRgx).matcher(value).find()) {
				String dueDate = value.replaceAll(dueDateRgx, "$1");
				invoice.setDueDate(dueDate);
				;
			}
		}

		// ================== table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", unitPrice = "", disc = "", qty = "", vat = "", gbp = "";
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
						description = value.replaceAll(table, "$1").trim();
						qty = value.replaceAll(table, "$2").trim();
						unitPrice = value.replaceAll(table, "$3").trim();
						disc = value.replaceAll(table, "$4").trim();
						vat = value.replaceAll(table, "$5").trim();
						gbp = value.replaceAll(table, "$6").trim();
						/*
						 * setting values
						 */
						tableRow.setQuantity(qty);
						tableRow.setUnitPrice(unitPrice);
						tableRow.setDiscount(disc);
						tableRow.setVatPercentage(vat);
						tableRow.setTotal(gbp);
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
