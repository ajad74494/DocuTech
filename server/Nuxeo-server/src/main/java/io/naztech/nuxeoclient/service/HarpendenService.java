package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author Masud.ahmed
 * @since 2020-08-16
 *
 */
@Service
public class HarpendenService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HarpendenService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.adafastfix.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.adafastfix.type}")
	private String nuxeoinvoiceType;

	@Value("${import.adafastfix.prefix}")
	private String prefix;

	@Value("${import.nuxeo.adafastfix.description}")
	private String desc;

	@Value("${harpenden.telRgx}")
	private String telRgx;

	@Value("${harpenden.invoiceToRgx}")
	private String invoiceToRgx;

	@Value("${harpenden.patientRgx}")
	private String patientRgx;

	@Value("${harpenden.invoiceNoRgx}")
	private String invoiceNoRgx;

	@Value("${harpenden.invoiceDateRgx}")
	private String invoiceDateRgx;

	@Value("${harpenden.emailRgx}")
	private String emailRgx;

	@Value("${harpenden.practitionerRgx}")
	private String practitionerRgx;

	@Value("${harpenden.wpaRgx}")
	private String wpaRgx;

	@Value("${harpenden.aXA_PPPRgx}")
	private String aXA_PPPRgx;

	@Value("${harpenden.avivaRgx}")
	private String avivaRgx;

	@Value("${harpenden.cignaRgx}")
	private String cignaRgx;

	@Value("${harpenden.taxRgx}")
	private String taxRgx;

	@Value("${harpenden.totalAmountRgx}")
	private String totalAmountRgx;

	@Value("${harpenden.totalRgx}")
	private String totalRgx;

	@Value("${harpenden.itemRgx}")
	private String itemRgx;

	@Value("${harpenden.tableHeaderRgx}")
	private String tableHeaderRgx;

	@Value("${harpenden.tableRgx}")
	private String tableRgx;

	@Value("${harpenden.descRgx}")
	private String descRgx;

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
		// ================== supplier details =========================//
		String supplierDetails = "", customer_details = "";
		dataArray = data.split("\\n");
		boolean flag = true, flag_2 = false;
		for (String value : dataArray) {
			value = value.trim();
			if (Pattern.compile(telRgx).matcher(value).find()) {
				String telephone = value;
				value = value.replaceAll(telRgx, "");
				telephone = telephone.substring(0, value.length()).replaceAll(telRgx, "$1");
				invoice.setTelephone(telephone);
				flag = false;
			} else if (Pattern.compile(invoiceToRgx).matcher(value).find()) {
				value = "";
				flag_2 = true;
			}
			if (Pattern.compile(patientRgx).matcher(value).find()) {
				flag_2 = false;
			}
			if (Pattern.compile(invoiceNoRgx).matcher(value).find()) {
				String invoiceNo = value;
				value = value.replaceAll(invoiceNoRgx, "");
				invoiceNo = invoiceNo.substring(value.length()).replaceAll(invoiceNoRgx, "$1");
				invoice.setInvoiceNumber(invoiceNo);
			} else if (Pattern.compile(invoiceDateRgx).matcher(value).find()) {
				String invoiceDate = value;
				value = value.replaceAll(invoiceDateRgx, "");
				invoiceDate = invoiceDate.substring(value.length()).replaceAll(invoiceDateRgx, "$1");
				invoice.setInvoiceDate(invoiceDate);
			} else if (Pattern.compile(emailRgx).matcher(value).find()) {
				String email = value;
				value = value.replaceAll(emailRgx, "");
				email = email.substring(0, value.length()).replaceAll(emailRgx, "$1");
				invoice.setEmail(email);
			} else if (Pattern.compile(practitionerRgx).matcher(value).find()) {
				value = value.replaceAll(practitionerRgx, "");
			} else if (Pattern.compile(wpaRgx).matcher(value).find()) {
				value = value.replaceAll(wpaRgx, "");
			} else if (Pattern.compile(aXA_PPPRgx).matcher(value).find()) {
				value = value.replaceAll(aXA_PPPRgx, "");
			} else if (Pattern.compile(avivaRgx).matcher(value).find()) {
				value = value.replaceAll(avivaRgx, "");
			} else if (Pattern.compile(cignaRgx).matcher(value).find()) {
				value = value.replaceAll(cignaRgx, "");
			} else if (Pattern.compile(taxRgx).matcher(value).find()) {
				String tax = value;
				value = value.replaceAll(taxRgx, "");
				tax = tax.substring(value.length()).replaceAll(taxRgx, "$1");
				invoice.setVat(tax);
			} else if (Pattern.compile(totalRgx).matcher(value).find()) {
				String gross = value;
				value = value.replaceAll(totalRgx, "");
				gross = gross.substring(value.length()).replaceAll(totalRgx, "$1");
				invoice.setGrossTotal(gross);
			} else if (Pattern.compile(totalAmountRgx).matcher(value).find()) {
				String net = value;
				value = value.replaceAll(totalAmountRgx, "");
				net = net.substring(value.length()).replaceAll(totalAmountRgx, "$1");
				invoice.setNetTotal(net);
			}
			if (flag) {
				if (!value.equals(""))
					supplierDetails += value.trim().concat("\n");
			}
			if (flag_2) {
				if (!value.equals(""))
					customer_details += value.trim().concat("\n");
			}
		}
		/*
		 * 
		 * */
		invoice.setSupplierAddress(supplierDetails);
		/*
		 * 
		 * */
		dataArray = supplierDetails.split("\\n");
		invoice.setSupplierName(dataArray[0]);
		// ================= customer details ===============//
		invoice.setCustomerAddress(customer_details);
		dataArray = customer_details.split("\\n");
		invoice.setCustomerName(dataArray[0]);
		// ================= table ================= //
		ArrayList<String> listStr = new ArrayList<>();
		String full_row = "";
		String code = "", item = "", type = "", unitPrice = "", qty = "", tax = "", totalPrice = "";
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

			if (Pattern.compile(taxRgx).matcher(value).find()) {
				listStr.add(full_row);
				full_row = "";
				break;
			}
			if (value.trim().matches(tableheader)) {
				value = value.replaceAll("\\s", "=");
				index = value.indexOf(itemRgx);
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
						full_row += "========" + value;
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

						value = value.replaceAll("\\s", "=");
						code = value.substring(0, index).replaceAll("=", "");
						value = value.replaceAll("=", " ").trim();
						item = value.replaceAll(table, "$1");
						type = value.replaceAll(table, "$2");
						unitPrice = value.replaceAll(table, "$3");
						qty = value.replaceAll(table, "$4");
						tax = value.replaceAll(table, "$5");
						totalPrice = value.replaceAll(table, "$6");

					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							item += value.trim() + "\n";
						}
					}
				}
				extra_string = value;

			}
			item = item.trim();
			tableRow.setQuantity(qty);
			tableRow.setProductCode(code);
			tableRow.setType(type);
			tableRow.setUnitPrice(unitPrice);
			tableRow.setVatAmount(tax);
			tableRow.setTotal(totalPrice);
			tableRow.setDescription(item);
			tableList.add(tableRow);
			item = "";
		}
		invoice.setInvoiceTable(tableList);
		return invoice;

	}
}
