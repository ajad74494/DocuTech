package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @since 2020-07-13
 */
@Service
public class TrendService implements PdfInvoiceProcessor {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.ox}")
	private String folderName;

	@Value("${import.ox.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.ox.type}")
	private String nuxeoinvoiceType;

	@Value("${import.ox.prefix}")
	private String prefix;

	@Value("${import.nuxeo.ox.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;
	
	@Value("${trend.invoiceNoRgx}")
	private String invoiceNoRgx;
	
	@Value("${trend.invoiceRgx}")
	private String invoiceRgx;
	
	@Value("${trend.salesRgx}")
	private String salesRgx;
	
	@Value("${trend.faxRgx}")
	private String faxRgx;
	
	@Value("${trend.emailRgx}")
	private String emailRgx;
	
	@Value("${trend.webRgx}")
	private String webRgx;
	
	@Value("${trend.invoiceAddressRgx}")
	private String invoiceAddressRgx;
	
	@Value("${trend.despatchRgx}")
	private String despatchRgx;
	
	@Value("${trend.invooiceNoDateRgx}")
	private String invooiceNoDateRgx;
	
	@Value("${trend.invoiceAddressRgx2}")
	private String invoiceAddressRgx2;
	
	@Value("${trend.refRgx}")
	private String refRgx;
	
	@Value("${trend.bankRgx}")
	private String bankRgx;
	
	@Value("${trend.bankRgx2}")
	private String bankRgx2;
	
	@Value("${trend.customerRgx}")
	private String customerRgx;
	
	@Value("${trend.invoiceTotalRgx}")
	private String invoiceTotalRgx;
	
	@Value("${trend.goodsRgx}")
	private String goodsRgx;
	
	@Value("${trend.vatTotalRgx}")
	private String vatTotalRgx;
	
	@Value("${trend.netTotalRgx}")
	private String netTotalRgx;
	
	@Value("${trend.tableRgx}")
	private String tableRgx;
	
	@Value("${trend.tableRgx2}")
	private String tableRgx2;
	
	@Value("${trend.vatNoRgx}")
	private String vatNoRgx;
	
	@Value("${trend.regNoRgx}")
	private String regNoRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		// =========== contract detail ============//
		String sales, fax, email, web;
		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		subData = data.substring(0, data.indexOf(invoiceNoRgx)).replaceAll(invoiceRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(salesRgx).matcher(value).find()) {
				sales = value.replaceAll(salesRgx, "$1");
				invoice.setTelephone(sales);
			} else if (Pattern.compile(faxRgx).matcher(value).find()) {
				fax = value.replaceAll(faxRgx, "$1");
				invoice.setFax(fax);
			} else if (Pattern.compile(emailRgx).matcher(value).find()) {
				email = value.replaceAll(emailRgx, "$1");
			} else if (Pattern.compile(webRgx).matcher(value).find()) {
				web = value.replaceAll(webRgx, "$1");
			}
		}
		subData = data.substring(data.indexOf(invoiceNoRgx), data.indexOf(invoiceAddressRgx));
		dataArray = subData.split("\\n");
		dataArrayCopy = dataArray;
		int despatchIndex = 0;
		int i = 0;
		for (String value : dataArray) {
			if (Pattern.compile(despatchRgx).matcher(value).find()) {
				despatchIndex = value.indexOf(despatchRgx);
			}
			try {
				dataArray[i] = dataArray[i].substring(0, despatchIndex);
			} catch (Exception e) {
				// TODO: handle exception
			}

			i++;
		}
		// =================== despatchNoteNo==================//
		subData = data.substring(data.indexOf(invoiceNoRgx), data.indexOf(invoiceAddressRgx));
		dataArrayCopy = subData.split("\\n");
		String desppatchNo = "";
		for (String value : dataArrayCopy) {
			if (Pattern.compile(despatchRgx).matcher(value).find()) {

				value = value.replaceAll(despatchRgx, "");
			}
			if (Pattern.compile(invooiceNoDateRgx).matcher(value).find()) {
				value = value.replaceAll(invooiceNoDateRgx, "");
			}

			try {
				desppatchNo += value.substring(despatchIndex);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		desppatchNo = desppatchNo.trim();
		// ============ invoice detail ==============//
		String invoiceDate = "", invoiceNo = "";
		i = 0;

		for (String value : dataArray) {
			value = value.trim();
			if (value.matches(invooiceNoDateRgx)) {
				invoiceNo = value.replaceAll(invooiceNoDateRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo);
				invoiceDate = value.replaceAll(invooiceNoDateRgx, "$2");
				invoice.setInvoiceDate(invoiceDate);
				dataArray[i] = "";

			}
			dataArray[i] = dataArray[i].trim();
			i++;
		}
		// ====================== customer detail ========================//
		String customer_detail = "", delivery_detail = "", vatNo = "", regNo = "";
		int indexStore = 0;
		String supplierAddress = Arrays.toString(dataArray);
		subData = data.substring(data.indexOf(invoiceAddressRgx2), data.indexOf(refRgx));
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			if (Pattern.compile(bankRgx).matcher(value).find()) {
				indexStore = subData.indexOf(bankRgx2);
			}
		}

		subData = subData.replaceAll(customerRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {

			try {
				value = value.substring(0, (indexStore + 14));
			} catch (Exception e) {
				// TODO: handle exception
			}

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
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);

		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);
		invoice.setDeliveryAddress(delivery_detail);
		String goodTotal = "", vatTotal = "", totalGBP = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				String ACCOUNTNUMBER = value.replaceAll(invoiceTotalRgx, "$2");
				String ref = value.replaceAll(invoiceTotalRgx, "$1");
				/*
				 * value of full row
				 */
				invoice.setReferenceNumber(ref);
				invoice.setAccountNo(ACCOUNTNUMBER);
			} else if (Pattern.compile(goodsRgx).matcher(value).find()) {
				goodTotal = value.replaceAll(goodsRgx, "$1");
				invoice.setTotal(goodTotal);
			} else if (Pattern.compile(vatTotalRgx).matcher(value).find()) {
				totalGBP = value;
				vatTotal = value;
				value = value.replaceAll(vatTotalRgx, "");
				vatTotal = vatTotal.replaceAll(value, "").replaceAll(vatTotalRgx, "$1");
				invoice.setVatTotal(vatTotal);
				if (Pattern.compile(netTotalRgx).matcher(totalGBP).find()) {
					totalGBP = totalGBP.replaceAll(vatTotalRgx, "");
					totalGBP = totalGBP.replaceAll(netTotalRgx, "$1").trim();
					invoice.setNetTotal(totalGBP);
				}
			}
		}

		// ===================== table ======================//
		String regex = tableRgx;
		String regex2 =tableRgx;
		String qty = "", productRef = "", desc = "", listPrice = "", disc = "", unitCost = "", lineCost = "",
				vatPersent = "", vatAmt = "";
		for (String value : dataArray) {
			InvoiceTable table = new InvoiceTable();
			value = value.trim();
			if (Pattern.compile(regex).matcher(value).find()) {
				qty = value.replaceAll(regex, "$1");
				desc = value.replaceAll(qty, "").replaceAll(regex2, "").trim();// value
				table.setDescription(desc);
				productRef = qty.replaceAll("^\\d+", "");// value
				table.setProductCode(productRef);
				qty = qty.replaceAll(productRef, "");// value
				table.setQuantity(qty);
				listPrice = value.replaceAll(regex2, "");

				value = value.substring(listPrice.length());
				listPrice = value.replaceAll(regex2, "$1");// value
				table.setPrice(listPrice);
				disc = value.replaceAll(regex2, "$2");// value
				table.setDiscount(disc);
				unitCost = value.replaceAll(regex2, "$3");// value
				table.setUnitPrice(unitCost);
				lineCost = value.replaceAll(regex2, "$4");// value
				vatPersent = value.replaceAll(regex2, "$5");// value
				table.setVatPercentage(vatPersent);
				vatAmt = value.replaceAll(regex2, "$6");// value

			}
			tableList.add(table);
		}
		invoice.setInvoiceTable(tableList);
		// ===============================
		subData = data.substring(data.indexOf("INVOICE ADDRESS"), data.indexOf("CUSTOMERORDERREFERENCE"));

		int start = 0, end = 0;
		Pattern pattern = Pattern.compile("VAT\\s*No:\\s+([\\w\\s]+)");
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
		vatNo = subData.substring(start, end);
		vatNo = vatNo.replaceAll(vatNoRgx, "$1").trim();
		invoice.setVatNo(vatNo);
		pattern = Pattern.compile(regNoRgx);
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
		regNo = subData.substring(start, end);
		regNo = regNo.replaceAll(regNoRgx, "$1").trim();
		invoice.setRegNo(regNo);
		return invoice;
	}

}
