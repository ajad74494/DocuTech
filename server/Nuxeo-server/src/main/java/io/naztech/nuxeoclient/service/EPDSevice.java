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
public class EPDSevice implements PdfInvoiceProcessor {
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

	@Value("${epd.invoiceToRgx}")
	private String invoiceToRgx ;
	
	@Value("${epd.pageRgx}")
	private String pageRgx ;
	
	@Value("${epd.invoiceNoRgx}")
	private String invoiceNoRgx ;
	
	@Value("${epd.invoiceDateRgx}")
	private String invoiceDateRgx ;
	
	@Value("${epd.docketNoRgx}")
	private String docketNoRgx ;
	
	@Value("${epd.refNoRgx}")
	private String refNoRgx ;
	
	@Value("${epd.dueDateRgx}")
	private String dueDateRgx ;
	
	@Value("${epd.takenByRgx}")
	private String takenByRgx;
	
	@Value("${epd.telNdFaxRgx}")
	private String telNdFaxRgx ;
	
	@Value("${epd.webNdEmailRgx}")
	private String webNdEmailRgx ;
	
	@Value("${epd.itemCodeRgx}")
	private String itemCodeRgx ;
	
	@Value("${epd.totalGoodsRgx}")
	private String totalGoodsRgx ;
	
	@Value("${epd.vatTotalRgx}")
	private String vatTotalRgx ;
	
	@Value("${epd.totalGBPRgx}")
	private String totalGBPRgx ;
	
	@Value("${epd.regNoRgx}")
	private String regNoRgx ;
	
	@Value("${epd.vatRegRgx}")
	private String vatRegRgx ;
	
	@Value("${epd.tableEndRgx}")
	private String tableEndRgx ;
	
	@Value("${epd.tableStartRgx}")
	private String tableStartRgx ;
	
	@Value("${epd.qtyUnitPriceRgx}")
	private String qtyUnitPriceRgx ;
	
	@Value("${epd.valueVcRgx}")
	private String valueVcRgx ;
	
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String data = "", textStriper = ""; 
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
		// ================== supplier details ======================//
		String supplierAddress = "";
		subData = data.substring(0, data.indexOf(invoiceToRgx));
		subData = subData.replaceAll(pageRgx, "");
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
			} else if (Pattern.compile(docketNoRgx).matcher(value).find()) {
				value = value.replaceAll(docketNoRgx, "");
			} else if (Pattern.compile(refNoRgx).matcher(value).find()) {
				String refNo = value;
				value = value.replaceAll(refNoRgx, "");
				refNo = refNo.replaceAll(value, "").replaceAll(refNoRgx, "$1");
				invoice.setReferenceNumber(refNo);
			} else if (Pattern.compile(dueDateRgx).matcher(value).find()) {
				String dueDate = value;
				value = value.replaceAll(dueDateRgx, "");
				dueDate = dueDate.replaceAll(value, "").replaceAll(dueDateRgx, "$1");
				invoice.setDueDate(dueDate);
				;
			}
			if (Pattern.compile(takenByRgx).matcher(value).find()) {
				value = value.replaceAll(takenByRgx, "");
			}
			if (Pattern.compile(telNdFaxRgx).matcher(value).find()) {
				String contact = value;
				value = value.replaceAll(telNdFaxRgx, "");
				String telephone = contact.replaceAll(value, "").replaceAll(telNdFaxRgx, "$1");
				String fax = contact.replaceAll(value, "").replaceAll(telNdFaxRgx, "$2");
				invoice.setTelephone(telephone);
				invoice.setFax(fax);

			}
			if (Pattern.compile(webNdEmailRgx).matcher(value).find()) {
				String contact = value;
				value = value.replaceAll(webNdEmailRgx, "");
				String web = contact.replaceAll(value, "").replaceAll(webNdEmailRgx, "$1");
				String email = contact.replaceAll(value, "").replaceAll(webNdEmailRgx, "$2");
				invoice.setWebsite(web);
				invoice.setEmail(email);
			}

			if (!value.equals("")) {
				supplierAddress += "," + value.trim();
			}
		}
		supplierAddress = supplierAddress.replaceAll("^,", "");
		dataArray = supplierAddress.split(",");
		invoice.setSupplierAddress(supplierAddress);
		invoice.setSupplierName(dataArray[0]);
		// ===================== customer and delivery ====================//
		String customer_detail = "", delivery_detail = "";
		subData = data.substring(data.indexOf(invoiceToRgx), data.indexOf(itemCodeRgx));
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
		invoice.setCustomerName(dataArray[1].trim());
		delivery_detail = delivery_detail.replaceAll(",=", ",").replaceAll("^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		// ============== others ============== //
		/*
		 * net
		 */
		subData = data.substring((int) data.length() / 2);
		int start = 0;
		int end = 0;
		Pattern pattern = Pattern.compile(totalGoodsRgx);
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
		String totalGoods = subData.substring(start, end).replaceAll(totalGoodsRgx, "$1");
		invoice.setNetTotal(totalGoods);
		/*
		 * net
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatTotalRgx);
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
		String vatTotal = subData.substring(start, end).replaceAll(vatTotalRgx, "$1");
		invoice.setVatTotal(vatTotal);
		/*
		 * total gbp
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(totalGBPRgx);
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
		String Total = subData.substring(start, end).replaceAll(totalGBPRgx, "$1");
		invoice.setGrossTotal(Total);
		/*
		 * Reg no
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
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
		String regNo = subData.substring(start, end).replaceAll(regNoRgx, "$1");
		invoice.setRegNo(regNo);
		/*
		 * Reg no
		 */
		subData = data.substring((int) data.length() / 2);
		start = 0;
		end = 0;
		pattern = Pattern.compile(vatRegRgx);
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
		String vatReg = subData.substring(start, end).replaceAll(vatRegRgx, "$1");
		invoice.setVatReg(vatReg);
		// ================ table ====================//
		boolean flag = false;
		dataArray = textStriper.split("\\n");
		int descStart = 0, descEnd = 0, perStart = 0, perEnd = 0;
		String description = "", per = "";
		for (int startIdx = 0; startIdx < dataArray.length; startIdx++) {
			if (Pattern.compile(tableEndRgx).matcher(dataArray[startIdx]).find()) {
				break;
			}
			if (Pattern.compile(tableStartRgx).matcher(dataArray[startIdx]).find()) {
				flag = true;
				descStart = startIdx;
			} else if (flag) {
				InvoiceTable tableRow = new InvoiceTable();
				String itemCode = dataArray[descStart + 1];
				tableRow.setItemNo(itemCode);
				if (Pattern.compile(qtyUnitPriceRgx).matcher(dataArray[startIdx]).find()) {
					String qty = dataArray[startIdx].replaceAll(qtyUnitPriceRgx, "$1");
					String unit = dataArray[startIdx].replaceAll(qtyUnitPriceRgx, "$2");
					String unitPrice = dataArray[startIdx].replaceAll(qtyUnitPriceRgx, "$3");
					tableRow.setQuantity(qty);
					tableRow.setUnit(unit);
					tableRow.setUnitPrice(unitPrice);
					descEnd = startIdx;
					perStart = startIdx;
					if (descEnd != 0)
						for (int i = descStart + 2; i < descEnd; i++)
							description += dataArray[i] + "\n";

				} else if (Pattern.compile(valueVcRgx).matcher(dataArray[startIdx]).find()) {
					String value = dataArray[startIdx].replaceAll(valueVcRgx, "$1");
					String vc = dataArray[startIdx].replaceAll(valueVcRgx, "$2");
					tableRow.setNetAmount(value);
					tableRow.setVc(vc);
					perEnd = startIdx;
					descStart = startIdx;
					if (perEnd != 0)
						for (int i = perStart + 1; i < perEnd; i++)
							per += dataArray[i] + "\n";

				}
				tableRow.setDescription(description);
				tableRow.setPer(per);
				if (tableRow.getVc() != null | tableRow.getNetAmount() != null) {
					tableList.add(tableRow);
				}
			}
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}