package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author masud.ahmed
 * @since 
 */
@Service
public class RoyalMailService  implements PdfInvoiceProcessor {
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

	@Value("${royal.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${royal.pageRgx}")
	private String pageRgx ;
	
	@Value("${royal.unusedDataX1Rgx}")
	private String unusedDataX1Rgx ;
	
	@Value("${royal.vatRegRgx}")
	private String vatRegRgx ;
	
	@Value("${royal.accountNoRgx}")
	private String accountNoRgx ;
	
	@Value("${royal.accountNoX2Rgx}")
	private String accountNoX2Rgx;
	
	@Value("${royal.unusedDataX2Rgx}")
	private String unusedDataX2Rgx ;
	
	@Value("${royal.unusedDataX3Rgx}")
	private String unusedDataX3Rgx ;
	
	@Value("${royal.termsRgx}")
	private String termsRgx ;
	
	@Value("${royal.totalNetRgx}")
	private String totalNetRgx ;
	
	@Value("${royal.vatRgx}")
	private String vatRgx ;
	
	@Value("${royal.invoiceTotalRgx}")
	private String invoiceTotalRgx ;
	
	@Value("${royal.unusedDataX4Rgx}")
	private String unusedDataX4Rgx ;
	
	@Value("${royal.tableHeaderRgx}")
	private String tableHeaderRgx ;
	
	@Value("${royal.tableRgx}")
	private String tableRgx ;
	
	@Value("${royal.trackNextPageRgx}")
	private String trackNextPageRgx ;
	
	@Value("${royal.tableEndRgx}")
	private String tableEndRgx ;
	
	@Value("${royal.refQtyWeigtRgx}")
	private String refQtyWeigtRgx ;
	
	@Value("${royal.subTotalRgx}")
	private String subTotalRgx ;

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

		/*
		 * invoice no,date, vat Reg no
		 */

		subData = textStriper.substring(textStriper.indexOf(invoiceNoRgx), textStriper.indexOf(pageRgx));
		subData = subData.replaceAll(unusedDataX1Rgx, "").trim();
		dataArray = subData.split("\\n");
		invoice.setInvoiceNumber(dataArray[0]);
		invoice.setInvoiceDate(dataArray[2]);
		/*
		 * vat Reg
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(vatRegRgx);
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
		String vatReg = textStriper.substring(start, end).replaceAll(vatRegRgx, "$1");
		invoice.setVatReg(vatReg);
		// ================== customer and delivery ==================== //
		String customer_detail = "", delivery_detail = "";
		subData = data.substring(data.indexOf("Page"), data.indexOf(accountNoRgx));
		subData = subData.replaceAll(unusedDataX2Rgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.replaceAll("^\\s{10}", "=").trim();
			String[] arr1 = value.split("\\s{15,}");
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
		/*
		 * spliting customer details for customer name
		 * */
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);
		invoice.setDeliveryAddress(delivery_detail);
		/*
		 * customer account number
		 */
		subData = textStriper.substring(textStriper.indexOf(accountNoX2Rgx), textStriper.indexOf(termsRgx));
		subData = subData.replaceAll(unusedDataX3Rgx, "").trim();
		dataArray = subData.split("\\n");
		invoice.setAccountNo(dataArray[0]);
		/*
		 * * total net, vat, invoice total
		 */
		dataArray = textStriper.split("\\n");
		for (String value : dataArray) {
			if (Pattern.compile(totalNetRgx).matcher(value).find()) {
				String net = value.replaceAll(totalNetRgx, "$1");
				invoice.setNetTotal(net);
			} else if (Pattern.compile(vatRgx).matcher(value).find()) {
				String vat = value.replaceAll(vatRgx, "$3");
				invoice.setVatTotal(vat);
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				String gross = value.replaceAll(invoiceTotalRgx, "$1");
				invoice.setGrossTotal(gross);
			}
		}
		// ==================== table ========================//
		data = data.replaceAll(unusedDataX4Rgx, "");
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String docketNo = "", sendersDate = "", ref = "", desc = "", qty = "", weight = "", net = "", vatCode = "";
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
			if (Pattern.compile(trackNextPageRgx).matcher(value).find()) {
				full_row += "";
				flag = false;
			}
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
//						docketNo = "", sendersDate = "", ref = "", service = "", qty = "", weight = "", net = "",vatCode = "";
						docketNo = value.replaceAll(table, "$1");
						sendersDate = value.replaceAll(table, "$2");
						desc = value.replaceAll(table, "$3");
						net = value.replaceAll(table, "$4");
						vatCode = value.replaceAll(table, "$5");

						/*
						 * setting values
						 */

					} else {
						if (Pattern.compile(refQtyWeigtRgx).matcher(value).find()) {
							ref = value.replaceAll(refQtyWeigtRgx, "$1");
							qty = value.replaceAll(refQtyWeigtRgx, "$2");
							weight = value.replaceAll(refQtyWeigtRgx, "$13");
						} else if (Pattern.compile(subTotalRgx).matcher(value).find()) {
							value = "";
						} else {
							sendersDate = sendersDate.concat(" " + value);
						}
					}
				}
				extra_string = value;

			}
			tableRow.setDocketNumber(docketNo);
			tableRow.setReference(ref);
			tableRow.setVatCode(vatCode);
			tableRow.setDate(sendersDate);
			tableRow.setNetAmount(net);
			tableRow.setQuantity(qty);
			tableRow.setWeight(weight);
			tableRow.setDescription(desc);
			tableList.add(tableRow);
		}
		invoice.setInvoiceTable(tableList);
		 return invoice;
	}
}
