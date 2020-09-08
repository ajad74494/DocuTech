package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
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
public class InfoLogicService implements PdfInvoiceProcessorPdfBox {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.forktruck}")
	private String folderName;

	@Value("${import.forktruck.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.forktruck.type}")
	private String nuxeoinvoiceType;

	@Value("${import.forktruck.prefix}")
	private String prefix;

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${infologic.descriptionRgx}")
	private String descriptionRgx;

	@Value("${infologic.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${infologic.unusedDataRgx}")
	private String unusedDataRgx;

	@Value("${infologic.companyNameRgx}")
	private String companyNameRgx;

	@Value("${infologic.invoiceDetailsRgx}")
	private String invoiceDetailsRgx;

	@Value("${infologic.invoiceDetailsDataRgx}")
	private String invoiceDetailsDataRgx;

	@Value("${infologic.subTotalRgx}")
	private String subTotalRgx;

	@Value("${infologic.subTotalFullRgx}")
	private String subTotalFullRgx;

	@Value("${infologic.totalGBPRgx}")
	private String totalGBPRgx;

	@Value("${infologic.totalVatRgx}")
	private String totalVatRgx;

	@Value("${infologic.totalVatFullRgx}")
	private String totalVatFullRgx;

	@Value("${infologic.DueAmountRgx}")
	private String DueAmountRgx;

	@Value("${infologic.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${infologic.tableRgx}")
	private String tableRgx;

	@Value("${infologic.tableVatRgx}")
	private String tableVatRgx;

	@Value("${infologic.descRgx}")
	private String descRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
		String data = "";
		try {
			PDFParser pdfParser = new PDFParser(
					new RandomAccessFile(new File("./data/EMAIL_1000292_Invoice INV-126467(InfoLogic).pdf"), "r"));
			pdfParser.parse();
			PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
			PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
			data = pdfTextStripper.getText(pdDocument);

		} catch (Exception e) {
			// TODO: handle exception
		}
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();

		// invoice.setSortName(Constants.VR);
		// invoice.setInvoiceTitel(nuxeoinvoiceName);
		// invoice.setInvoiceDescription(desc);
		// invoice.setPrefix(prefix);
		// invoice.setInvoiceType(nuxeoinvoiceType);

		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;

		// =============== invoice detail =============== //
		/*
		 * customer address
		 */
		String customerAddress = "";
		int getIndex = 0, getIndexExtra = 0;
		subData = data.substring(0, data.indexOf(descriptionRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				getIndex = value.indexOf(invoiceDateRgx);
			}

			if (getIndex > 0) {
				try {
					value = value.substring(0, getIndex);
					getIndexExtra = getIndex;
				} catch (Exception e) {
					// TODO: handle exception
				}
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					customerAddress += "," + value;
				}
			}

		}
		customerAddress = customerAddress.replaceAll(unusedDataRgx, "").trim().replaceFirst("^,", "");// not set
		dataArrayCopy = customerAddress.split(",");
		invoice.setCustomerName(dataArrayCopy[0]);
		invoice.setCustomerAddress(customerAddress);
		/*
		 * delivery address
		 */
		String deliveryAddress = "";
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			if (Pattern.compile(companyNameRgx).matcher(value).find()) {
				getIndex = value.indexOf(companyNameRgx);
			}
			if (getIndex > 0) {
				try {
					value = value.substring(getIndex);
				} catch (Exception e) {
					// TODO: handle exception
				}
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					deliveryAddress += "," + value;
				}
			}

		}
		deliveryAddress = deliveryAddress.trim().replaceFirst("^,", "");// not set
		invoice.setDeliveryAddress(deliveryAddress);
		/*
		 * invoiceNo,date,accNo,vatNo
		 */
		String invoiceDetail = "";
		for (String value : dataArray) {
			value = value.replaceAll("\\s", "=");
			if (Pattern.compile(companyNameRgx).matcher(value).find()) {
				getIndex = value.indexOf(companyNameRgx);
			}
			if (getIndex > 0) {
				try {
					value = value.substring(getIndexExtra, getIndex);
				} catch (Exception e) {
					// TODO: handle exception
				}
				value = value.replaceAll("=", " ").trim();
				if (!value.equals("")) {
					invoiceDetail += "," + value;
				}
			}

		}
		invoiceDetail = invoiceDetail.trim().replaceFirst("^,", "");// not set
		invoiceDetail = invoiceDetail.replaceAll(invoiceDetailsRgx, "").replaceAll(",{2}", ",");
		String invoiceDate = invoiceDetail.replaceAll(invoiceDetailsDataRgx, "$1");
		invoice.setInvoiceDate(invoiceDate);
		String accNo = invoiceDetail.replaceAll(invoiceDetailsDataRgx, "$2");
		invoice.setAccountNo(accNo);
		String invoiceNo = invoiceDetail.replaceAll(invoiceDetailsDataRgx, "$3");
		invoice.setInvoiceNumber(invoiceNo);
		String vatNo = invoiceDetail.replaceAll(invoiceDetailsDataRgx, "$4");
		invoice.setVatNo(vatNo);
		// ================== invoice table total ====================//
		subData = data.substring(data.indexOf(subTotalRgx));
		String total = "", netTotal = "", vatTotal = "", dueTotal = "";
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(subTotalFullRgx).matcher(value).find()) {
				total = value.replaceAll(subTotalFullRgx, "$1").trim();
				invoice.setTotal(total);
			} else if (Pattern.compile(totalGBPRgx).matcher(value).find()) {
				netTotal = value.replaceAll(totalGBPRgx, "$1");
				invoice.setNetTotal(netTotal);
			} else if (value.contains(totalVatRgx)) {
				vatTotal = value.replaceAll(totalVatFullRgx, "$1").trim();
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(DueAmountRgx).matcher(value).find()) {
				dueTotal = value.replaceAll(DueAmountRgx, "$1");
				invoice.setDue(dueTotal);
			}

		}
		// ================= table ==================//
		Pattern pattern;
		Matcher matcher;
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String description = "", totalAmount = "", unitPrice = "", vat = "", qty = "";
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
			if (value.matches(tableheader)) {
				flag = true;
			} else if (flag) {
				if (value.equals("")) {
					blank_Counter++;
				} else {
					blank_Counter = 0;
				}

				if (blank_Counter == 5) {
					table_last_idx = index;
					break;
				} else if (Pattern.compile("Subtotal").matcher(value).find()) {
					break;
				}

				/*
				 * finding row
				 */
				if (value.matches(table)) {
					start_indx = index;
					startBol = true;
				} else {
					if (startBol) {
						pattern = Pattern.compile("[A-Za-z0-9]");
						matcher = pattern.matcher(arr0[index + 1]);
						if (!arr0[index + 1].equals("")) {
							pattern = Pattern.compile(table);
							matcher = pattern.matcher(arr0[index + 1].trim());
							if (matcher.find()) {
								endBol = true;
								end_indx = index;
							} else {
								endBol = false;
							}
						}

					}
				}
			}
			/*
			 * collection full row data
			 */
			if (startBol && endBol) {
				if (start_indx < end_indx) {
					for (int i = start_indx; i <= end_indx; i++) {

						arr0[i] = arr0[i].trim();
						full_row += arr0[i].trim() + "=====";
					}

				}
				listStr.add(full_row);
				full_row = "";
			}
			index++;
		}
		/*
		 * outsize table operation
		 */
		/*
		 * finding last row of table row full
		 */
		InvoiceTable lastRow = new InvoiceTable();
		dataArray = null;
		full_row = "";
		full_row = arr0[start_indx].trim();
		description = full_row.replaceAll(table, "$1");
		qty = full_row.replaceAll(table, "$2");
		lastRow.setQuantity(qty);
		unitPrice = full_row.replaceAll(table, "$3");
		lastRow.setQuantity(unitPrice);
		vat = full_row.replaceAll(table, "$4");
		lastRow.setQuantity(vat);
		totalAmount = full_row.replaceAll(table, "$5");
		lastRow.setQuantity(totalAmount);
		/*
		 * last rows extra lines
		 */
		for (int i = start_indx + 1; i < table_last_idx; i++) {
			arr0[i] = arr0[i].trim();
			pattern = Pattern.compile("[A-Za-z]+");
			if (pattern.matcher(arr0[i]).find()) {
				description += " " + arr0[i].trim();
			} else if (Pattern.compile(tableVatRgx).matcher(arr0[i]).find()) {
				vat += " " + arr0[i];
			}

		}
		/*
		 * value of desc
		 */
		description = description;
		lastRow.setDescription(description);

		description = "";
		String extra_string = "";
		arr0 = null;
		for (String row : listStr) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(table);
				if (pattern.matcher(value).find()) {
					description = value.replaceAll(table, "$1");

					qty = value.replaceAll(table, "$2");
					lastRow.setQuantity(qty);
					unitPrice = value.replaceAll(table, "$3");
					lastRow.setQuantity(unitPrice);
					vat = value.replaceAll(table, "$4");
					lastRow.setQuantity(vat);
					totalAmount = value.replaceAll(table, "$5");
					lastRow.setQuantity(totalAmount);
				} else {
					pattern = Pattern.compile(descRgx);
					if (pattern.matcher(value).find()) {
						description += " " + value.trim();
					}
				}

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
