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
public class NavigatorService implements PdfInvoiceProcessor {
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
	
	@Value("${navigator.invoiceToRgx}")
	private String invoiceToRgx ;
	
	@Value("${navigator.invoiceRgx}")
	private String invoiceRgx ;
	
	@Value("${navigator.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${navigator.invoiceToExtRgx}")
	private String invoiceToExtRgx ;
	
	@Value("${navigator.invoiceInfoRgx}")
	private String invoiceInfoRgx ;
	
	@Value("${navigator.totalGBPExRgx}")
	private String totalGBPExRgx ;
	
	@Value("${navigator.vatRgx}")
	private String vatRgx ;
	
	@Value("${navigator.totalGBPInRgx}")
	private String totalGBPInRgx ;
	
	@Value("${navigator.vatRegRgx}")
	private String vatRegRgx ;
	
	@Value("${navigator.contractRgx}")
	private String contractRgx ;
	
	@Value("${navigator.tableHeaderRgx}")
	private String tableHeaderRgx ;
	
	@Value("${navigator.tableEndRgx}")
	private String tableEndRgx ;
	
	@Value("${navigator.unusedDataRgx}")
	private String unusedDataRgx ;
	
	@Value("${navigator.tableRgx}")
	private String tableRgx ;
	
	@Value("${navigator.descRgx}")
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
		// ============== supplier details ============= //
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(invoiceToRgx));
		subData = subData.replaceAll(invoiceRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (!value.equals("")) {
				supplierAddress += "," + value.trim();
			}
		}
		supplierAddress = supplierAddress.replaceAll("^,", "");
		invoice.setSupplierAddress(supplierAddress);
		dataArray = supplierAddress.split(",");
		invoice.setSupplierName(dataArray[0]);
		// =========== customer and delivery ============ //
		String customer_detail = "", delivery_detail = "";
		dataArray = data.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				break;
			}
			if (Pattern.compile(invoiceToExtRgx).matcher(value).find()) {
				flag = true;
			} else if (flag) {
				value = value.replaceAll("^\\s{9}", "=").trim();
				String[] arr1 = value.split("\\s{5,}");
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
		 * invoiceNo,Date,orderNo,accountNo,refNo
		 */
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceInfoRgx).matcher(value).find()) {
				String invoiceNo = value.replaceAll(
						"(\\w+)\\s+(\\d+\\/\\d+\\/\\d+)\\s+(\\w+)\\s+(\\w+)\\s+(\\d+\\.\\d+\\.\\d+)\\s+([\\-\\w]+)",
						"$1");
				String invoiceDate = value.replaceAll(invoiceInfoRgx, "$2");
				String accountNo = value.replaceAll(invoiceInfoRgx, "$4");
				String orderNo = value.replaceAll(invoiceInfoRgx, "$5");
				String refNo = value.replaceAll(invoiceInfoRgx, "$6");
				invoice.setInvoiceNumber(invoiceNo);
				invoice.setInvoiceDate(invoiceDate);
				invoice.setAccountNo(accountNo);
				invoice.setOrderNo(orderNo);
				invoice.setReferenceNumber(refNo);
			} else if (Pattern.compile(totalGBPExRgx).matcher(value).find()) {
				String totalGbp = value.replaceAll(totalGBPExRgx, "$1");
				invoice.setNetTotal(totalGbp.trim());
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vatTotal = value.replaceAll(vatRgx, "$1");
				invoice.setVatTotal(vatTotal);
			} else if (Pattern.compile(totalGBPInRgx).matcher(value).find()) {
				String totalGbp = value.replaceAll(totalGBPInRgx, "$1");
				invoice.setGrossTotal(totalGbp);
			}
		}

		/*
		 * telephone,email,fax,vatRegno
		 */
		dataArray = textStriper.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(vatRegRgx).matcher(value).find()) {
				String vatReg = value;
				value = value.replaceAll(vatRegRgx, "");
				vatReg = vatReg.substring(value.length()).replaceAll(vatRegRgx, "$1");
			} else if (Pattern.compile(contractRgx).matcher(value).find()) {
				String telephone = value.replaceAll(contractRgx, "$1");
				String fax = value.replaceAll(contractRgx, "$2");
				String email = value.replaceAll(contractRgx, "$3");
				String website = value.replaceAll(contractRgx, "$4");
				invoice.setTelephone(telephone);
				invoice.setFax(fax);
				invoice.setEmail(email);
				invoice.setWebsite(website);
			}
		}
		// ================== table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String itemNo = "", description = "", unitPrice = "", qty = "", amount = "";
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
			} else if (Pattern.compile(unusedDataRgx).matcher(value).find()) {
				value = "";
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
						itemNo = value.replaceAll(table, "$1").trim();
						description = value.replaceAll(table, "$2").trim();
						qty = value.replaceAll(table, "$3").trim();
						unitPrice = value.replaceAll(table, "$4").trim();
						amount = value.replaceAll(table, "$5").trim();
						/*
						 * setting values
						 */
						tableRow.setItemNo(itemNo);
						tableRow.setQuantity(qty);
						tableRow.setUnitPrice(unitPrice);
						tableRow.setNetAmount(amount);

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