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
 * @since 2020-07-16
 */
@Service
public class ParkersProductService implements PdfInvoiceProcessor {
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
	
	@Value("${parkers.invoiceRgx}")
	private String invoiceRgx;
	
	@Value("${parkers.telFaxWebRgx}")
	private String telFaxWebRgx;
	
	@Value("${parkers.stockRgx}")
	private String stockRgx ;
	
	@Value("${parkers.unusedDataRgx}")
	private String unusedDataRgx;
	
	@Value("${parkers.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${parkers.invoiceDateRgx}")
	private String invoiceDateRgx;
	
	@Value("${parkers.delNoteNoRgx}")
	private String delNoteNoRgx ;
	
	@Value("${parkers.orderNoRgx}")
	private String orderNoRgx;
	
	@Value("${parkers.accountNoRgx}")
	private String accountNoRgx ;
	
	@Value("${parkers.paymentTermRgx}")
	private String paymentTermRgx ;
	
	@Value("${parkers.netTotalRgx}")
	private String netTotalRgx;
	
	@Value("${parkers.vatTotalRgx}")
	private String vatTotalRgx ;
	
	@Value("${parkers.invoiceTotalRgx}")
	private String invoiceTotalRgx ;
	
	@Value("${parkers.carrageRgx}")
	private String carrageRgx ;
	
	@Value("${parkers.vatNoRgx}")
	private String vatNoRgx;
	
	@Value("${parkers.DelToRgx}")
	private String DelToRgx ;
	
	@Value("${parkers.tableHeaderRgx}")
	private String tableHeaderRgx ;
	
	@Value("${parkers.tableRgx}")
	private String tableRgx ;
	
	@Value("${parkers.descRgx}")
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
		// ================ supplier address ==================//
		subData = data.substring(0, data.indexOf(invoiceRgx)).trim();
		/*
		 * tel
		 */
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(telFaxWebRgx);
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
		String contract = subData.substring(start, end);
		String telephone = contract.replaceAll(telFaxWebRgx, "$1");
		String fax = contract.replaceAll(telFaxWebRgx, "$2");
		String website = contract.replaceAll(telFaxWebRgx, "$3");
		invoice.setTelephone(telephone);
		invoice.setFax(fax);
		invoice.setWebsite(website);
		subData = subData.replaceAll(telFaxWebRgx, "");
		invoice.setSupplierAddress(subData.trim());
		// =============== customer deatails ==============//
		String customerAddress = "";
		subData = data.substring(data.indexOf(invoiceRgx), data.indexOf(stockRgx)).replaceAll(unusedDataRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				String invoiceNo = value;
				value = value.replaceAll(invoiceNoRgx, "");
				invoiceNo = invoiceNo.replaceAll(value, "").replaceAll(invoiceNoRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				String invoiceDate = value;
				value = value.replaceAll(invoiceDateRgx, "");
				invoiceDate = invoiceDate.replaceAll(value, "").replaceAll(invoiceDateRgx, "$1");
				invoice.setInvoiceDate(invoiceDate);
			} else if (Pattern.compile(delNoteNoRgx).matcher(value).find()) {
				String delNoteNo = value;
				value = value.replaceAll(delNoteNoRgx, "");
				delNoteNo = delNoteNo.replaceAll(value, "").replaceAll(delNoteNoRgx, "$1");
				invoice.setDeliveryNoteNo(delNoteNo);
			} else if (Pattern.compile(orderNoRgx).matcher(value).find()) {
				String orderNo = value;
				value = value.replaceAll(orderNoRgx, "");
				orderNo = orderNo.replaceAll(value, "").replaceAll(orderNoRgx, "$1");
				invoice.setOrderNo(orderNo);
			} else if (Pattern.compile(accountNoRgx).matcher(value).find()) {
				String accNo = value;
				value = value.replaceAll(accountNoRgx, "");
				accNo = accNo.replaceAll(value, "").replaceAll(accountNoRgx, "$1");
				invoice.setAccountNo(accNo);
			}
			if (!value.equals("")) {
				customerAddress += "," + value.trim();
			}
		}
		customerAddress = customerAddress.replaceAll("^,", "");
		dataArray = customerAddress.split(",");
		invoice.setCustomerName(dataArray[0].trim());
		invoice.setCustomerAddress(customerAddress);
		// ============= delivery address =================//
		String deliveryAddress = "";
		dataArray = data.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(paymentTermRgx).matcher(value).find()) {
				flag = false;
			}
			if (Pattern.compile(netTotalRgx).matcher(value).find()) {
				String net = value;
				value = value.replaceAll(netTotalRgx, "");
				net = net.replaceAll(value, "").replaceAll(netTotalRgx, "$1");
				invoice.setNetTotal(net);
			} else if (Pattern.compile(vatTotalRgx).matcher(value).find()) {
				String vat = value;
				value = value.replaceAll(vatTotalRgx, "");
				vat = vat.replaceAll(value, "").replaceAll(vatTotalRgx, "$1");
				invoice.setVatTotal(vat);
				;
			} else if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				String invoiceTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "");
				invoiceTotal = invoiceTotal.replaceAll(value, "")
						.replaceAll(invoiceTotalRgx, "$1");
				invoice.setGrossTotal(invoiceTotal);
			} else if (Pattern.compile(carrageRgx).matcher(value).find()) {
				value = value.replaceAll(carrageRgx, "");
			} else if (Pattern.compile(vatNoRgx).matcher(value).find()) {
				String vatNo = value;
				value = value.replaceAll(vatNoRgx, "");
				vatNo = vatNo.replaceAll(value, "").replaceAll(vatNoRgx, "$1");
				invoice.setVatNo(vatNo);
			}
			if (Pattern.compile(DelToRgx).matcher(value).find()) {
				flag = true;
			} else if (flag) {
				deliveryAddress += "," + value.trim();
			}
			if (Pattern.compile(paymentTermRgx).matcher(value).find()) {
				flag = false;
			}

		}
		deliveryAddress = deliveryAddress.replaceAll("^,", "");
		invoice.setDeliveryAddress(deliveryAddress);
		// ================== table ==================//
		// ================== table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String stockCode = "", description = "", qty = "", unitPrice = "", disc = "", netAmount = "", vat = "";
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
		String checkTotal = "";
		int index = 0;
		for (String value : arr0) {
			value = value.trim();
			if (Pattern.compile(DelToRgx).matcher(value).find()) {
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

						stockCode = value.replaceAll(table, "$1");
						description = value.replaceAll(table, "$2");
						qty = value.replaceAll(table, "$3");
						unitPrice = value.replaceAll(table, "$4");
						disc = value.replaceAll(table, "$5");
						netAmount = value.replaceAll(table, "$6");
						vat = value.replaceAll(table, "$7");

						/*
						 * setting values
						 */

						tableRow.setStockCode(stockCode);
						tableRow.setUnitPrice(unitPrice);
						tableRow.setQuantity(qty);
						tableRow.setDiscount(disc);
						tableRow.setNetAmount(netAmount);
						tableRow.setVatAmount(vat);
					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							description += "\n" + value.trim();
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
