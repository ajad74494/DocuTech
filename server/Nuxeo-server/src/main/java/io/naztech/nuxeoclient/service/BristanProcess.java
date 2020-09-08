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
 * @author masud.ahmed
 */
@Service
public class BristanProcess implements PdfInvoiceProcessorPdfBox {
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

	@Value("${import.nuxeo.invoice.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${bristan.invoiceAddressRgx}")
	private String invoiceAddressRgx ;

	@Value("${bristan.invoiceAddressRgx2}")
	private String invoiceAddressRgx2 ;

	@Value("${bristan.invoiceNoRgx}")
	private String invoiceNoRgx ;

	@Value("${bristan.taxDateRgx}")
	private String taxDateRgx ;

	@Value("${bristan.ourRefRgx}")
	private String ourRefRgx ;

	@Value("${bristan.accNoRgx}")
	private String accNoRgx ;

	@Value("${bristan.orderNoRgx}")
	private String orderNoRgx ;

	@Value("${bristan.despatchNoRgx}")
	private String despatchNoRgx ;

	@Value("${bristan.invoiceAddrssRgx}")
	private String invoiceAddrssRgx ;

	@Value("${bristan.partNoRgx}")
	private String partNoRgx ;

	@Value("${bristan.customerRgx}")
	private String customerRgx ;

	@Value("${bristan.invoiceRgx}")
	private String invoiceRgx ;

	@Value("${bristan.invoiceRgx2}")
	private String invoiceRgx2 ;

	@Value("${bristan.invoiceRgx3}")
	private String invoiceRgx3 ;

	@Value("${bristan.invoiceRgx4}")
	private String invoiceRgx4 ;

	@Value("${bristan.webRgx}")
	private String webRgx;

	@Value("${bristan.telRgx}")
	private String telRgx ;

	@Value("${bristan.faxRgx}")
	private String faxRgx ;

	@Value("${bristan.tableHeaderRgx}")
	private String tableHeaderRgx ;

	@Value("${bristan.tableRgx}")
	private String tableRgx ;

	@Override
	public Invoice processPdfInvoice(String pdfStr, String pdfBoxStr, File pdfInvoice) {
	
		String data = pdfStr;
		Invoice invoice = new Invoice();
		ArrayList<InvoiceTable> tableList = new ArrayList<>();
		String subData = null;
		String[] dataArray = null;
		String[] dataArrayCopy = null;
		// ========== invoice detail ==========//
		String invoiceNo = "", taxDate = "", ourRef = "", AccountNo = "", orderNo = "", despatchNo = "";
		subData = data.substring(0, data.indexOf(invoiceAddressRgx)).replaceAll(invoiceAddressRgx2, "");
		dataArray = subData.split("\\n");
		boolean flag = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				invoiceNo = value.replaceAll(invoiceNoRgx, "").trim();
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(taxDateRgx).matcher(value).find()) {
				taxDate = value.replaceAll(taxDateRgx, "").trim();
				invoice.setInvoiceDate(taxDate);
			} else if (Pattern.compile(ourRefRgx).matcher(value).find()) {
				ourRef = value.replaceAll(ourRefRgx, "").trim();
				invoice.setReferenceNumber(ourRef);
			} else if (Pattern.compile(accNoRgx).matcher(value).find()) {
				AccountNo = value.replaceAll(accNoRgx, "").trim();
				invoice.setAccountNo(AccountNo);
			} else if (Pattern.compile(orderNoRgx).matcher(value).find()) {
				orderNo = value.replaceAll(orderNoRgx, "").trim();
				invoice.setOrderNo(orderNo);
			} else if (Pattern.compile(despatchNoRgx).matcher(value).find()) {
				despatchNo = value.replaceAll(despatchNoRgx, "").trim();
			}

		}

		// ======= customer detail & Delivery detail=======//
		String customer_detail = "", delivery_detail = "", customerName = "";
		subData = data.substring(data.indexOf(invoiceAddrssRgx), data.indexOf(partNoRgx));
		subData = subData.replaceAll(customerRgx, "").trim();
		dataArray = subData.split("\\n");

		for (String value : dataArray) {
			value = value.trim();
			String[] arr1 = value.split("\\s{7,}");
			customer_detail += "," + arr1[0].trim();
			try {
				delivery_detail += "," + arr1[1].trim();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		dataArray = null;
		customer_detail = customer_detail.replaceAll(",{2,}|^,", "");
		delivery_detail = delivery_detail.replaceAll(",{2,}|^,", "");
		dataArray = customer_detail.replaceAll(",{2,}|^,", "").split(",");
		customerName = dataArray[0];// value
		invoice.setCustomerName(customerName);
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		// ============= invoice total ===============//
		String extraData = "", vatRegNo = "";
		String CARRIAGE = "", GOODS_VALUE = "", VAT_AMOUNT = "", TOTAL_AMOUNT = "";
		subData = data.substring(data.indexOf(invoiceRgx));
		dataArray = subData.split("\\n");
		flag = true;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(invoiceRgx2).matcher(value).find()) {
				CARRIAGE = value.replaceAll(invoiceRgx2, "$1");
				GOODS_VALUE = value.replaceAll(invoiceRgx2, "$2");
				invoice.setTotal(GOODS_VALUE);
				VAT_AMOUNT = value.replaceAll(invoiceRgx2, "$3");
				invoice.setVatTotal(VAT_AMOUNT);
				TOTAL_AMOUNT = value.replaceAll(invoiceRgx2, "$4");
				invoice.setNetTotal(TOTAL_AMOUNT);
			} else if (Pattern.compile(invoiceRgx3).matcher(value).find()) {
				extraData = value;
			} else if (Pattern.compile(invoiceRgx4).matcher(value).find()) {
				vatRegNo = value;
				value = value.replaceAll(invoiceRgx4, "");
				/*
				 * value of vatRegNo
				 */
				vatRegNo = vatRegNo.substring(value.length()).replaceAll(invoiceRgx4, "$1");
				invoice.setVatReg(vatRegNo);
			}

		}
		// =================== supplier detail ========================//
		subData = subData.substring(subData.indexOf(extraData));
		int start = 0, end = 0;
		Pattern pattern = Pattern.compile(invoiceRgx3);
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
		String supplierName = subData.substring(start, end);
		invoice.setSupplierName(supplierName);
		pattern = Pattern.compile(webRgx);
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
		/*
		 * value of address
		 */
		String supplierAddress = subData.substring(0, start);
		invoice.setSupplierAddress(supplierAddress);
		String website = subData.substring(start, end);
		invoice.setWebsite(website);
		pattern = Pattern.compile(telRgx);
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
		String tel = subData.substring(start, end).replaceAll(telRgx, "$2");
		invoice.setTelephone(tel);
		pattern = Pattern.compile(faxRgx);
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
		String fax = subData.substring(start, end).replaceAll(faxRgx, "$2");
		invoice.setFax(fax);
		// ======================= table ========================//
		ArrayList<String> list = new ArrayList<>();
		String full_row = "";
		String desc = "", Quantity = "", Unit = "", Trade_Price = "", Discount = "", Value = "", VAT = "";
		;
		int start_indx = 0, end_indx = 0, table_last_idx = 0, blank_Counter = 0;
		boolean startBol = false, endBol = false;
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
			if (value.matches(tableHeaderRgx)) {
				flag = true;
			} else if (flag) {
				if (value.equals("")) {

					blank_Counter++;
				} else {
					blank_Counter = 0;
				}

				if (blank_Counter == 3) {
					table_last_idx = index;
					break;
				}

				/*
				 * finding row
				 */
				if (value.matches(tableRgx)) {
					start_indx = index;
					startBol = true;
				} else {
					if (startBol) {
						pattern = Pattern.compile("[A-Za-z0-9]");
						matcher = pattern.matcher(arr0[index + 1]);
						if (!arr0[index + 1].equals("")) {
							pattern = Pattern.compile(tableRgx);
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
				list.add(full_row);
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
		InvoiceTable table = new InvoiceTable();
		dataArray = null;
		full_row = "";
		full_row = arr0[start_indx].trim();
		desc = full_row.replaceAll(tableRgx, "$1").trim();
		Quantity = full_row.replaceAll(tableRgx, "$2").trim();
		table.setQuantity(Quantity);
		Unit = full_row.replaceAll(tableRgx, "$3").trim();
		table.setUnit(Unit);
		/*
		 * value not set
		 */
		Trade_Price = full_row.replaceAll(tableRgx, "$4").trim();
		Discount = full_row.replaceAll(tableRgx, "$5").trim();
		table.setDiscount(Discount);
		Value = full_row.replaceAll(tableRgx, "$6").trim();
		table.setPrice(Value);
		VAT = full_row.replaceAll(tableRgx, "$7").trim();
		table.setVatPercentage(VAT);
		/*
		 * last rows extra lines
		 */
		for (int i = start_indx + 1; i < table_last_idx; i++) {
			arr0[i] = arr0[i].trim();
			pattern = Pattern.compile("[A-Za-z]+");
			if (pattern.matcher(arr0[i]).find()) {
				desc += " " + arr0[i];
			} else {
			}

		}
		table.setDescription(desc);
		tableList.add(table);
		/*
		 * processing list of all rows data
		 * 
		 */
		desc = "";
		String extra_string = "";
		arr0 = null;
		for (String row : list) {
			InvoiceTable table2 = new InvoiceTable();
			arr0 = row.split("=");
			for (String value : arr0) {
				pattern = Pattern.compile(tableRgx);
				if (pattern.matcher(value).find()) {
					desc = value.replaceAll(tableRgx, "$1").trim();
					Quantity = value.replaceAll(tableRgx, "$2").trim();
					table2.setQuantity(Quantity);
					Unit = value.replaceAll(tableRgx, "$3").trim();
					table2.setUnit(Unit);
					/*
					 * value not set
					 */
					Trade_Price = value.replaceAll(tableRgx, "$4").trim();
					Discount = value.replaceAll(tableRgx, "$5").trim();
					table2.setDiscount(Discount);
					Value = value.replaceAll(tableRgx, "$6").trim();
					table2.setPrice(Value);
					VAT = value.replaceAll(tableRgx, "$7").trim();
					table2.setVatPercentage(VAT);
				} else {
					pattern = Pattern.compile("[A-Za-z0-9]+");
					if (pattern.matcher(value).find()) {
						desc += " " + value.trim();
					} else {
					}
				}

			}
			table2.setDescription(desc);
			tableList.add(table2);
			desc = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;
	}
}
