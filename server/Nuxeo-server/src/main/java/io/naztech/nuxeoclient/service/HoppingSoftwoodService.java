package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * @author Masbha.uddin
 * @since 2020-04-06
 */

@Service
public class HoppingSoftwoodService implements PdfInvoiceProcessor  {

	private static Logger log = LoggerFactory.getLogger(HoppingSoftwoodService.class); 

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.hoppingssoftwood}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.hoppings.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.hoppings.type}")
	private String nuxeoinvoiceType;

	@Value("${import.hoppings.prefix}")
	private String prefix;

	@Value("${import.nuxeo.hoppings.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${import.processorType}")
	private String proType;

	@Value("${import.docType}")
	private String docType;

	@Value("${import.source}")
	private String sourceType;

	@Value("${archive.enabled}")
	private boolean enabled;

	/**
	 * Constructs a OnsiteDocument object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */


	@Value("${hopping.supplierName}")
	private String supplierName;
	
	@Value("${hopping.invoiceNo}")
	private String invoiceNo;
	
	@Value("${hopping.invoiceDte}")
	private String invoiceDte;
	
	@Value("${hopping.invoiceDes}")
	private String invoiceDes;
	
	@Value("${hopping.onDate}")
	private String onDate;
	
	@Value("${hopping.invoiceAddress}")
	private String invoiceAddress;
	
	@Value("${hopping.referNo}")
	private String referNo;

	@Value("${hopping.delevaryAddress}")
	private String delevaryAddress;

	@Value("${prtek.supplierName}")
	private String supplierName1;
	
	@Value("${hopping.delevaryAddr}")
	private String delevaryAddr;
	
	@Value("${hopping.vatNo}")
	private String vatNo;
	
	@Value("${hopping.accountNo}")
	private String accountNo;
	
	@Value("${hopping.faxNo}")
	private String faxNo;
	
	@Value("${hopping.phone}")
	private String phone;
	
	@Value("${hopping.netTotalC1}")
	private String netTotalC1;
	
	@Value("${hopping.vatTotal}")
	private String vatTotal;
	
	@Value("${hopping.grossTotalC1}")
	private String grossTotalC1;
	
	@Value("${hopping.netTotal}")
	private String netTotal; 
	
	@Value("${hopping.orderNo}")
	private String orderNo; 
	
	@Value("${hopping.telephone}")
	private String telephone; 

	@Value("${hopping.web}")
	private String web;
	
	@Value("${hopping.suplierAddress}")
	private String supAddress;      
 
 
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		String json=pdfStr;
		List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();

		Invoice invoice = new Invoice(); 
		try {

			invoice.setSortName(Constants.HOPPINGS);
//			invoice.setInvoiceTitle(nuxeoinvoiceName);
//			invoice.setInvoiceDescription (desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setInvoiceTitle("handling");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("handling");
			invoice.setInvoiceType(nuxeoinvoiceType);

			invoice.setSupplierName(supplierName);
			invoice.setSupplierAddress(supAddress);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(invoiceDes);
			String[] invoiceA4 = invoiceA3[0].split("         ");
			invoice.setInvoiceNumber(invoiceA4[4].trim());
			invoice.setReferenceNumber(invoiceA4[1].trim());
			/*
			 * Web
			 */
						String webA1[]=json.split(web);
						String webA2[]=webA1[1].trim().split(supplierName1);
						invoice.setWebsite(webA2[0].trim());
						// ============ INvoice Date =====================
						String[] odat11 = invoiceA4[3].replaceAll("null", "").split("      ");
						invoice.setInvoiceDate(odat11[2].trim());

						String delevaryAddressA1[] = json.split(delevaryAddress);
						String iv111[] = delevaryAddressA1[1].split(orderNo); 

						String customerName[] = json.split(web);
						String customerName1[] = customerName[1].split(delevaryAddress);
						String cusName=customerName1[0].replaceAll(webA2[0].trim(), "");
						invoice.setDeliveryAddress(iv111[0]);
						invoice.setCustomerAddress(customerName1[0].trim() + iv111[0]);
						invoice.setCustomerName(cusName.trim());
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[\\D]{4}[a-zA-Z]");
			String accountNo = accountA3[0].replaceAll(phone, "");
			invoice.setAccountNo(accountNo);
			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo = vatNoA3[0].trim();
			invoice.setVatNo(vatNo);
			// ===============On Date Address ================
			String[] onDateA1 = json.split(onDate);
			String onDateA2 = onDateA1[1].trim();
			String onDateA3[] = onDateA2.split(telephone);
			// ==============Telephone no ===========
			String phoneA1 = onDateA3[0].trim();

			// =============================Telephone no============================
			String phoneA2[] = phoneA1.split(phone);
			String phoneA3[] = phoneA2[1].split("[a-zA-Z]");
			String phone = phoneA3[0].trim();
			invoice.setTelephone(phone);
			// ==============Fax no ===========
			String faxNoA1[] = phoneA1.split(faxNo);
			String faxNoA2[] = faxNoA1[1].split("[a-zA-Z]");
			String fax = faxNoA2[0].trim();
			invoice.setFax(fax);

			// ==============Email no ===========
			String emailA1[] = json.split(telephone);
			String emailA2[] = emailA1[1].split(web);
			String email = emailA2[0].trim();
			invoice.setEmail(email);

			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0].replaceAll("-", "");
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].replaceAll("[0-9]*.[0-9]* %:", "").trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// =============== Total Address ================
			String[] totalA1 = json.split(grossTotalC1);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			// ===============Invoice Table ================
			/*
			 * var for storing table data
			 */
			String qty = "", price_unit = "", nett_value = "", vat_rate = "";
			/*
			 * arraylist for storing table string
			 **/

			ArrayList<String> list = new ArrayList<>();
			String regex = "(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*";
			String full_row = "", desc = "";
			/*
			 * flag used for indicate that the table ,blank line found
			 **/
			boolean flag = false, start = false, end = false, empty_line = false;
			/*
			 * start and end index for table
			 **/
			int start_indx = 0, end_indx = 0, table_last_idx = 0, blank_Counter = 0;
			/*
			 * storing every line in array
			 **/
			String[] arr0 = json.split("\\n");
			/*
			 * setting the regex for table search
			 **/
			Pattern pattern;
			Matcher matcher;
			int index = 0;
			for (String value : arr0) {
				value = value.trim();
				pattern = Pattern.compile("Description\\s+Qty\\s+Price\\s+\\/\\s+Unit\\s+Nett\\s+Value\\s+VAT\\s+Rate");
				matcher = pattern.matcher(value);
				if (matcher.find()) {
					flag = true;
				} else if (flag) {
					pattern = Pattern.compile(regex);
					matcher = pattern.matcher(value);
					if (Pattern.compile("Goods:\\s+\\d+\\.\\d+").matcher(value).find()) {
						table_last_idx = index;
						break;
					} else if (matcher.find()) {
						start_indx = index;
						start = true;
					} else {
						if (start) {
							pattern = Pattern.compile("[A-Za-z0-9]");
							matcher = pattern.matcher(arr0[index + 1]);
							if (!arr0[index + 1].isEmpty()) {
								pattern = Pattern.compile(regex);
								matcher = pattern.matcher(arr0[index + 1].trim());
								if (matcher.find()) {
									end = true;
									end_indx = index;
								} else {
									end = false;
								}
							}

						}
					}
				}
				/*
				 * collection full row data
				 */
				if (start && end) {
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
			 * processing list of all rows data
			 * 
			 */
			String extra_string = "";
			desc = "";
			arr0 = null;
			for (String row : list) {
				InvoiceTable invoiceTable = new InvoiceTable();
				arr0 = row.split("=");
				for (String value : arr0) {
					pattern = Pattern.compile(regex);
					if (pattern.matcher(value).find()) {
						desc = value.replaceAll(
								"(\\d+\\.\\d+)*\\s{5,}(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*", "")
								.trim();
						value = value.substring(desc.length(), value.length());
						qty = value.trim().replaceAll(
								"(\\d+\\.\\d+)*\\s{5,}(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*",
								"$1");

						price_unit = value.replaceAll(
								"(\\d+\\.\\d+)*\\s{5,}(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*",
								"$2");
						nett_value = value.replaceAll(
								"(\\d+\\.\\d+)*\\s{5,}(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*",
								"$3");
						vat_rate = value.replaceAll(
								"(\\d+\\.\\d+)*\\s{5,}(\\d+\\.\\d+[\\s\\w]+)*\\s{5,}(\\d+\\.\\d+)*\\s{5,}([A-Z])*",
								"$4");
					} else {
						pattern = Pattern.compile("[A-Za-z0-9]+");
						if (pattern.matcher(value).find()) {
							desc += " " + value.trim();
						} else {
						}
					}

				}

				if (desc.isEmpty() && qty.isEmpty() && price_unit.isEmpty() && nett_value.isEmpty()
						&& vat_rate.isEmpty()) {

				} else {
					// set value to table object and trim() the string must.because of extra space
					invoiceTable.setDescription(desc);
					invoiceTable.setQuantity(qty);
					invoiceTable.setUnitPrice(price_unit);
					invoiceTable.setNetAmount(nett_value);
					invoiceTable.setVatPercentage(vat_rate);
					invoiceTab.add(invoiceTable);
				}
				desc = "";
				qty = "";
				price_unit = "";
				nett_value = "";
				vat_rate = "";
			}
			invoice.setInvoiceTable(invoiceTab);
			invoice.toString();
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return invoice;

	}
 
}
