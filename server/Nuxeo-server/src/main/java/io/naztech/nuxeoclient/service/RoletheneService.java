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
 * @since 2020-07-14
 */
@Service
public class RoletheneService implements PdfInvoiceProcessor {
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.rolathene}")
	private String folderName;

	@Value("${import.rolathene.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.rolathene.type}")
	private String nuxeoinvoiceType;

	@Value("${import.rolathene.prefix}")
	private String prefix;

	@Value("${import.nuxeo.rolathene.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;
	
	@Value("${rolathene.supplierAddress}")
	private String supplierAddress;

	@Value("${rolathene.supplierName}")
	private String supplierName;
	
	@Value("${rolathene.fax}")
	private String fax;
	
	@Value("${rolathene.phone}")
	private String phone;
	
	@Value("${rolathene.quantityRgx}")
	private String quantityRgx;
	
	@Value("${rolathene.vatReg}")
	private String vatReg;
	
	@Value("${rolathene.invoiceRgx}")
	private String invoiceRgx;
	
	@Value("${rolathene.invoiceNoRgx}")
	private String invoiceNoRgx;
	
	@Value("${rolathene.invoiceDateRgx}")
	private String invoiceDateRgx;
	
	@Value("${rolathene.orderNoRgx}")
	private String orderNoRgx;
	
	@Value("${rolathene.accRefRgx}")
	private String accRefRgx;
	
	@Value("${rolathene.totalNetRgx}")
	private String totalNetRgx;
	
	@Value("${rolathene.totalTaxRgx}")
	private String totalTaxRgx;
	
	@Value("${rolathene.invoiceTotalRgx}")
	private String invoiceTotalRgx;
	
	@Value("${rolathene.tableRgx}")
	private String tableRgx;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// ========== invoice details =================//
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(supplierName);
		invoice.setFax(fax);
		invoice.setTelephone(phone);
		String invoiceNo = "", invoiceDate = "", orderNo = "", accRef = "", customer_detail = "";
		subData = data.substring(0, data.indexOf(quantityRgx));
		int start = 0, end = 0;
		Pattern pattern = Pattern.compile(vatReg);
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
		/*
		 * value of supplier
		 */
		String vatReg = subData.substring(start, end);
		invoice.setVatReg(vatReg.replaceAll(vatReg, "$1"));
		subData = subData.replaceAll(invoiceRgx, "");
		dataArray = subData.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				invoiceNo = value;
				value = value.replaceAll(invoiceNoRgx, "");
				invoiceNo = invoiceNo.substring(value.length());
				invoiceNo = invoiceNo.replaceAll(invoiceNoRgx, "$1").trim();
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				invoiceDate = value;
				value = value.replaceAll(invoiceDateRgx, "");
				invoiceDate = invoiceDate.substring(value.length());
				invoiceDate = invoiceDate.replaceAll(invoiceDateRgx, "$1").trim();
				invoice.setInvoiceDate(invoiceDate);
			} else if (Pattern.compile(orderNoRgx).matcher(value).find()) {
				orderNo = value;
				value = value.replaceAll(orderNoRgx, "");
				orderNo = orderNo.substring(value.length());
				orderNo = orderNo.replaceAll(orderNoRgx, "$1").trim();
				invoice.setOrderNo(orderNo);
			} else if (Pattern.compile(accRefRgx).matcher(value).find()) {
				accRef = value;
				value = value.replaceAll(accRefRgx, "");
				accRef = accRef.substring(value.length());
				accRef = accRef.replaceAll(accRefRgx, "$1").trim();
				invoice.setReferenceNumber(accRef);
			}

			if (!value.equals("")) {						
				customer_detail += "," + value.trim();
			}
		}
		/*
		 * value of customer detail
		 */
		customer_detail = customer_detail.replaceFirst("^,", "");
		invoice.setCustomerAddress(customer_detail);
		dataArray = customer_detail.split(",");
		invoice.setCustomerName(dataArray[0]);
		
		// =============== invoice Total ========================//
		String netAmt = "",taxAmt = "",invoiceTotal = "";
		dataArray = data.split("\\n");
		for (String value : dataArray) {
			value = value.trim();
			if(Pattern.compile(totalNetRgx).matcher(value).find()) {
				netAmt = value;
				value = value.replaceAll(totalNetRgx, "");
				netAmt = netAmt.substring(value.length());
				netAmt = netAmt.replaceAll(totalNetRgx, "$1").trim();//value
				invoice.setTotal(netAmt);
			}else if(Pattern.compile(totalTaxRgx).matcher(value).find()) {
				taxAmt = value;
				value = value.replaceAll(totalTaxRgx, "");
				taxAmt= taxAmt.substring(value.length());
				taxAmt = taxAmt.replaceAll(totalTaxRgx, "$1").trim();//value
				invoice.setVatTotal(taxAmt);
			}else if(Pattern.compile(invoiceTotalRgx).matcher(value).find()) {
				invoiceTotal = value;
				value = value.replaceAll(invoiceTotalRgx, "");
				invoiceTotal = invoiceTotal.substring(value.length());
				invoiceTotal = invoiceTotal.replaceAll(invoiceTotalRgx, "$1").trim();//value
				invoice.setNetTotal(invoiceTotal);
			}
		}
		
		//========================= table ============================//
		String qty,desc,unitPrice,discAmt,net,vatPercent,vat;
		String regex = tableRgx;
		for(String value:dataArray) {
			InvoiceTable invoiceTable = new InvoiceTable(); 
			value = value.trim();
			if(value.matches(regex)) {
				qty = value.replaceAll(regex, "$1");
				invoiceTable.setQuantity(qty);
				desc = value.replaceAll(regex, "$2");
				invoiceTable.setDescription(desc);
				unitPrice = value.replaceAll(regex, "$3");
				invoiceTable.setUnitPrice(unitPrice);
				discAmt = value.replaceAll(regex, "$4");
				invoiceTable.setDiscount(discAmt);
				net = value.replaceAll(regex, "$5");
				invoiceTable.setNetAmount(net);
				vatPercent = value.replaceAll(regex, "$6");
				invoiceTable.setVatPercentage(vatPercent);
				vat = value.replaceAll(regex, "$7");
				invoice.setVatTotal(vat);
			}
			tableList.add(invoiceTable);
		}
		return null;
	}

}
