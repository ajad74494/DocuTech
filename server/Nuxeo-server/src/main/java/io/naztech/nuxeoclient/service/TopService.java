package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.nuxeo.client.NuxeoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
import io.naztech.nuxeoclient.utility.DirectoryCreator;

/**
 * 
 * @author samiul.islam @since2020-04-07
 */
@Service
public class TopService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(TopService.class);

	@Autowired
	private NuxeoClient nuxeoClient;

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Autowired
	private DocCountService docCountService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private DocDetailsService docDetailsService;

	@Autowired
	private DirectoryCreator directoryCreator;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.topservice}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.topservice.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.topservice.type}")
	private String nuxeoinvoiceType;

	@Value("${import.topservice.prefix}")
	private String prefix;

	@Value("${import.nuxeo.topservice.description}")
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

	@Value("${topService.supplierName}")
	private String supplierName;

	@Value("${topService.invoiceNo}")
	private String invoiceNo;

	@Value("${topService.invoiceDte}")
	private String invoiceDte;

	@Value("${topService.invoiceDes}")
	private String invoiceDes;

	@Value("${topService.invoiceAddress}")
	private String invoiceAddress;

	@Value("${topService.referNo}")
	private String referNo;

	@Value("${topService.delevaryAddr}")
	private String delevaryAddr;

	@Value("${topService.accountNo}")
	private String accountNo;

	@Value("${topService.vatNo}")
	private String vatNo;

	@Value("${topService.phone}")
	private String phone;

	@Value("${topService.netTotal}")
	private String netTotal;

	@Value("${topService.vatTotal}")
	private String vatTotal;

	@Value("${topService.grossTotalC1}")
	private String grossTotalC1;

	@Value("${topService.web}")
	private String web;

	@Value("${topService.invoiceTableA1}")
	private String invoiceTableA1;

	/**
	 * Constructs a object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		Boolean sendToNuxeoFlag = true;
		String json = pdfStr;
		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();
		try {


			invoice.setSortName(Constants.TOPSERVICE); 
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			invoice.setCustomerName(supplierName);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(invoiceDes);
			invoice.setInvoiceNumber(invoiceA3[0].trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split(invoiceDes);
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split(referNo);
			invoice.setAccountNo(accountA3[0].trim());
			// ============================Registration No:======================
			String regNoA1[] = json.split("Registration  No:");
			String regNoA2[] = regNoA1[1].split("[a-zAA-Z]");
			invoice.setRegNo(regNoA2[0].trim());
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(web);
			String invoiceAddressA2 = invoiceAddressA1[1].trim().replaceAll(invoiceAddress, "");
			String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
			String invoiceAddress = invoiceAddressA3[0].trim().replaceAll("(\\w*\\s*\\w*\\:\\s*(\\w*))", "");
			String invoiceAddressA4 = invoiceAddress.trim().replaceAll("(\\w*\\s*\\w*\\:\\s*(\\w*\\/\\d*\\/\\d*))", "");
			String invoiceAddressA5 = invoiceAddressA4.trim().replaceAll("(\\w*\\s*\\w*\\:\\s*(\\w*))", "");
			invoice.setInvoiceAddress(invoiceAddressA5);
			invoice.setCustomerAddress(invoiceAddressA5);
			invoice.setSupplierAddress(invoiceAddressA5);
			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			invoice.setVatNo(vatNoA3[0].trim());

			// =============================Telephone no============================
			String phoneA2[] = json.split(phone);
			String phoneA3[] = phoneA2[1].split(invoiceDes);
			String phone = phoneA3[0];
			invoice.setTelephone(phone);
			// ==============Email no ===========
			String emailA1[] = json.split(delevaryAddr);
			String emailA2[] = emailA1[1].split(web);
			String email = emailA2[0].trim();
			invoice.setEmail(email);
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split(invoiceDes);
			String netTotal = netTotalA3[0];
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split(invoiceDes);
			String out = vatTotalA3[0].trim();
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// =============== Total Address ================
			String[] totalA1 = json.split(grossTotalC1);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split(invoiceDes);
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			// ===============Invoice Table ================

			/*
			 * variables for storing data
			 */
			String quantity = "", product_Description = "", unit_Price = "", net_Amount = "", VAT_Amount = "";
			/*
			 * arraylist for storing table string
			 **/
			ArrayList<String> list = new ArrayList<>();

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
			int index = 0;
			for (String value : arr0) {
				value = value.trim();
				/*
				 * finding table start point
				 **/

				pattern = Pattern.compile(invoiceTableA1);

				if (pattern.matcher(value).find()) {
					flag = true;
				}

				/*
				 * finding table full row
				 **/
				if (flag) {
					pattern = Pattern.compile(
							"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*");
					if (pattern.matcher(value).find()) {
						start_indx = index;
						start = true;
					} else {
						pattern = Pattern.compile(
								"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*");
						try {
							if (start) {
								if (Pattern.compile("Net\\s+Amount\\s+\\d+\\.\\d+").matcher(arr0[index + 1]).find()) {
									table_last_idx = index;
									break;

								} else if (pattern.matcher(arr0[index + 1]).find()) {
									end_indx = index;
									end = true;
								}

							}

						} catch (Exception e) {
							// TODO: handle exception
						}
					}

				}

				/*
				 * collecting full row data
				 **/
				if (start && end) {
					if (start_indx < end_indx) {

						for (int i = start_indx; i <= end_indx; i++) {
							arr0[i] = arr0[i];
							full_row += arr0[i] + "=====";
						}
						list.add(full_row);
						full_row = "";
					}
				}

				/*
				 * end point of table
				 */

				index++;

			}
			/*
			 * table end point
			 */

			/*
			 * finding last row of table row full
			 */

			full_row = "";
			quantity = arr0[start_indx].trim().replaceAll(
					"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
					"$1");
			product_Description = arr0[start_indx].trim().replaceAll(
					"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
					"$2");
			unit_Price = arr0[start_indx].trim().replaceAll(
					"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
					"$3");
			net_Amount = arr0[start_indx].trim().replaceAll(
					"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
					"$4");
			VAT_Amount = arr0[start_indx].trim().replaceAll(
					"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
					"$5");

			/*
			 * last rows extra lines
			 */
			for (int i = start_indx + 1; i < table_last_idx; i++) {
				pattern = Pattern.compile("[A-Za-z]+");
				if (pattern.matcher(arr0[i]).find()) {
					desc = " " + arr0[i].trim();
				} else {
				}

			}

			/*
			 * (desc) stored the multi line description. concate it with product_Description
			 * Exp: product_Description.concat(" "+desc)
			 **/
			if (product_Description.isEmpty() && quantity.isEmpty() && unit_Price.isEmpty() && net_Amount.isEmpty()
					&& VAT_Amount.isEmpty()) {
			} else {
				// set value to table object and trim() the string must.because of extra space
				InvoiceTable invoiceTable = new InvoiceTable();
				invoiceTable.setQuantity(quantity);
				invoiceTable.setDescription(product_Description.trim().concat(" " + desc.trim()));
				invoiceTable.setUnitPrice(unit_Price);
				invoiceTable.setNetAmount(net_Amount);
				invoiceTable.setVatPercentage(VAT_Amount);
				invoiceTab.add(invoiceTable);
			}

			/*
			 * processing list of all rows data
			 * 
			 */
			String[] arr1 = null;
			desc = "";

			if (!list.isEmpty()) {
				for (String row : list) {
					/*
					 * spliting row from extra description using ' = '
					 */
					arr1 = row.split("=");

					for (String value : arr1) {

						if (Pattern.compile(
								"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*")
								.matcher(value).find()) {
							quantity = value.trim().replaceAll(
									"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
									"$1");
							product_Description = value.trim().replaceAll(
									"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
									"$2");
							unit_Price = value.trim().replaceAll(
									"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
									"$3");
							net_Amount = value.trim().replaceAll(
									"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
									"$4");
							VAT_Amount = value.trim().replaceAll(
									"(\\d+\\.\\d+)\\s+([\\w\\s\\(\\)\\[\\]\\{\\}\\\\\\\\^\\$\\|\\?\\*\\+\\<\\>\\-\\=\\!\\_]+)\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*\\s+(\\d+\\.\\d+)*",
									"$5");

						} else {
							pattern = Pattern.compile("[A-Za-z]+");
							if (pattern.matcher(value).find()) {
								desc += " " + value;
							}
						}
					}

					if (product_Description.isEmpty() && quantity.isEmpty() && unit_Price.isEmpty()
							&& net_Amount.isEmpty() && VAT_Amount.isEmpty()) {

					} else {
						// set value to table object and trim() the string must.because of extra space
						InvoiceTable invoiceTable = new InvoiceTable();
						invoiceTable.setQuantity(quantity);
						invoiceTable.setDescription(product_Description.trim().concat(" " + desc.trim()));
						invoiceTable.setUnitPrice(unit_Price);
						invoiceTable.setNetAmount(net_Amount);
						invoiceTable.setVatPercentage(VAT_Amount);
						invoiceTab.add(invoiceTable);

					}
					invoice.setInvoiceTable(invoiceTab);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return invoice;
	}

}