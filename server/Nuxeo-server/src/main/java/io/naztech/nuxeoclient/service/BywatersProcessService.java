
package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nuxeo.client.NuxeoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocCount;
import io.naztech.nuxeoclient.model.DocDetails;
import io.naztech.nuxeoclient.model.Document;
import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
import io.naztech.nuxeoclient.model.Template;
import io.naztech.nuxeoclient.utility.DirectoryCreator;

/**
 * @author masud.ahmed
 * @author abul.kalam
 * @author muhammad.tarek
 * @since 2020-07-19
 *
 */

@Service
public class BywatersProcessService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(BywatersProcessService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.bywaters}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.bywaters.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.bywaters.type}")
	private String nuxeoinvoiceType;

	@Value("${import.bywaters.prefix}")
	private String prefix;

	@Value("${import.nuxeo.bywaters.description}")
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

	@Value("${bywater.supplierRgx}")
	private String supplierRgx;

	@Value("${bywater.invoice}")
	private String invoiceRgx;

	@Value("${bywater.tel}")
	private String telRgx;

	@Value("${bywater.tel1}")
	private String tel1Rgx;

	@Value("${bywater.web}")
	private String webRgx;

	@Value("${bywater.web1}")
	private String web1Rgx;

	@Value("${bywater.web2}")
	private String web2Rgx;

	@Value("${bywater.vatReg}")
	private String vatRegRgx;

	@Value("${bywater.vatReg1}")
	private String vatReg1Rgx;

	@Value("${bywater.vatReg2}")
	private String vatReg2Rgx;

	@Value("${bywater.regIn}")
	private String regInRgx;

	@Value("${bywater.regIn1}")
	private String regIn1Rgx;

	@Value("${bywater.page}")
	private String pageRgx;

	@Value("${bywater.inoviceNo}")
	private String inoviceNoRgx;

	@Value("${bywater.accNo}")
	private String accNoRgx;

	@Value("${bywater.invoiceDate}")
	private String invoiceDateRgx;

	@Value("${bywater.regex}")
	private String regexRgx;

	@Value("${bywater.regexExtra}")
	private String regexExtraRgx;

	@Value("${bywater.regex1}")
	private String regex1Rgx;

	@Value("${bywater.totalGds}")
	private String totalGdsRgx;

	@Value("${bywater.vat}")
	private String vatRgx;

	@Value("${bywater.totalInvoice}")
	private String totalInvoiceRgx;

	@Value("${bywater.table}")
	private String tableRgx;

	@Value("${bywater.desc}")
	private String descRgx;

	@Value("${bywater.date}")
	private String dateRgx;

	@Value("${bywater.ticketNo}")
	private String ticketNoRgx;

	@Value("${bywater.qty}")
	private String qtyRgx;

	@Value("${bywater.unitPrice}")
	private String unitPrice;

	/**
	 * Constructs a premier Invoice Document object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		Invoice invoice = new Invoice(); 
		ArrayList<InvoiceTable> invoiceTable1 = new ArrayList<InvoiceTable>();
		 
		try {

			invoice.setSortName(Constants.BYWATERS);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			String x, y = "", z;
			int index, start_idx = 0, end_idx = 0;
			String[] arr0, arr1,   arr3;
			String data = pdfStr;
			// supplier detail

			/*
			 * sup_regex no use
			 */
			String sup_regex = supplierRgx;
			x = data.substring(0, data.indexOf(invoiceRgx));
			arr0 = x.split("\\n");
			for (String value : arr0) {
				value = value.trim();
				if (value.contains(telRgx)) {
					y = value.replaceAll(tel1Rgx, "");
					value = value.replace(y, "");
					invoice.setTelephone(value.replaceAll(tel1Rgx, "$2"));
					x = x.replaceAll(value, "");

				} else if (value.contains(webRgx)) {
					y = value.replaceAll(web1Rgx, "");
					value = value.replace(y, "");
					invoice.setWebsite(value.replaceAll(web2Rgx, ""));
					x = x.replaceAll(value, "");

				} else if (value.contains(vatRegRgx)) {
					y = value.replaceAll(vatReg1Rgx, "");
					value = value.replace(y, "");
					invoice.setVatNo(value.replaceAll(vatReg2Rgx, ""));
					x = x.replaceAll(value, "");

				} else if (value.contains(regInRgx)) {
					y = value.replaceAll(regIn1Rgx, "");
					value = value.replace(y, "");
					invoice.setRegNo(value.replaceAll(regIn1Rgx, "$2"));
					x = x.replaceAll(value, "");

				}
			}
			x = x.replaceAll("\\s{7,}", ",");
			x = x.replaceAll("^,", "");
			invoice.setSupplierAddress(x);
			arr0 = null;
			arr0 = x.split(",");
			invoice.setSupplierName(arr0[0]);
			// invoice detail
			x = data.substring(data.indexOf(invoiceRgx), data.indexOf(pageRgx));
			x = x.replaceAll(invoiceRgx, "");
			y = x;
			x = x.replaceAll(inoviceNoRgx, "").replaceAll(accNoRgx, "").replaceAll(invoiceDateRgx, "");
			x = x.replaceAll(regexRgx, "").replaceAll(regexExtraRgx, "").replaceAll("\\s{10,}", ",");
			invoice.setCustomerAddress(x.substring(1, x.length() - 1));
			x = x.substring(1, x.length() - 1);
			arr0 = null;
			arr0 = x.split(",");
			for (String value : arr0) {
				y = y.replaceAll(value, "");
			}
			y = y.replaceAll(inoviceNoRgx, "").replaceAll(accNoRgx, "").replaceAll(invoiceDateRgx, "")
					.replaceAll(regexExtraRgx, "").trim();
			invoice.setAccountNo(y.replaceAll(regex1Rgx, "$1"));
			invoice.setInvoiceNumber(y.replaceAll(regex1Rgx, "$2"));// value
			invoice.setInvoiceDate(y.replaceAll(regex1Rgx, "$3"));
			invoice.setCustomerName(arr0[0]);
			// invoice total
			arr0 = null;
			arr0 = data.split("\\n");
			index = 0;
			for (String value : arr0) {
				value = value.trim();
				if (value.matches(totalGdsRgx)) {
					invoice.setNetTotal(value.replaceAll(totalGdsRgx, "$2"));
					end_idx = index;
				} else if (value.matches(vatRgx)) {
					invoice.setVatTotal(value.replaceAll(vatRgx, "$3"));
				} else if (value.matches(totalInvoiceRgx)) {
					invoice.setGrossTotal(value.replaceAll(totalInvoiceRgx, "$2"));// total
																					// invoice
				}
				index++;
			}

			String regex = tableRgx;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher;
			arr0 = null;
			int[] index_arr = new int[100];
			String full_row = "";
			String date = "", desc = "", ticketNo = "", product_code = "", qty = "", unit = "", net = "",
					extra_desc = "";
			ArrayList<String> list = new ArrayList<>();
			/*
			 * split full string using new line
			 **/
			arr0 = data.split("\\n");
			int end_index = 0, start_index = 0, table_last_idx = 0;
			index = 0;
			boolean flag = false, start = false, end = false;
			for (String value : arr0) {

				/*
				 * triming extra spaces
				 ***/
				value = value.trim();
				/*
				 * this pattern is for table row matching
				 ***/
				pattern = Pattern.compile(regex);
				matcher = pattern.matcher(value);

				/*
				 * matching the ending point of table if regex matches then break
				 ***/
				if (value.matches(totalGdsRgx)) {
					break;
				}
				/*
				 * matching the table row using regex
				 ***/
				else if (matcher.find()) {
					start_index = index;
					flag = true;
					start = true;

				} else {
					if (flag) {
						pattern = Pattern.compile(regex);
						matcher = pattern.matcher(arr0[index + 1]);
						if (matcher.find()) {
							end_index = index;
							end = true;
						}
					}

				}
				/*
				 * collection full row data
				 */
				if (start && end) {
					if (start_index < end_index) {
						for (int i = start_index; i <= end_index; i++) {

							arr0[i] = arr0[i].trim();
							full_row += arr0[i].trim() + "=====";
						}
						list.add(full_row);
						full_row = "";
					}

				}

				index++;

			}

			/*
			 * table end point
			 */
			for (int i = 0; i < arr0.length; i++) {
				arr0[i] = arr0[i].trim();
				if (arr0[i].matches(totalGdsRgx)) {
					table_last_idx = i;
				}
			}

			/*
			 * finding last row of table row full
			 */
			InvoiceTable invoiceTab = new InvoiceTable();
			arr1 = null;
			full_row = "";
			;
			arr3 = arr0[start_index].split("\\s{5,}");
			for (String value1 : arr3) {
				pattern = Pattern.compile(descRgx);
				if (value1.matches(date)) {
					invoiceTab.setDate(value1);
				} else if (value1.matches(ticketNo)) {
					invoiceTab.setTicketNo(value1);
				} else if (pattern.matcher(value1).find()) {
					desc = value1;
				} else if (value1.matches(qty)) {
					invoiceTab.setQuantity(value1);
				} else if (value1.matches(unitPrice)) {
					invoiceTab.setUnitPrice(arr3[arr3.length - 2]);
					invoiceTab.setNetAmount(arr3[arr3.length - 1]);
				} else {
					invoiceTab.setOrderNo(value1);
				}

			}
			/*
			 * last rows extra lines
			 */
			for (int i = start_index + 1; i < table_last_idx; i++) {
				arr0[i] = arr0[i].trim();
				pattern = Pattern.compile("descRgx");
				if (pattern.matcher(arr0[i]).find()) {
					desc += " " + arr0[i];
				} else {
				}

			}
			invoiceTab.setDescription(desc);

			/*
			 * processing list of all rows data
			 * 
			 */
			extra_desc = "";
			desc = "";

			arr3 = null;
			for (String row : list) {
				arr1 = row.split("=");
				InvoiceTable invoiceTab1 = new InvoiceTable();
				for (String value : arr1) {
					pattern = Pattern.compile(tableRgx);
					if (pattern.matcher(value).find()) {
						arr3 = value.split("\\s{5,}");
						for (String value1 : arr3) {
							pattern = Pattern.compile(descRgx);
							if (value1.matches(dateRgx)) {
								invoiceTab1.setDate(value1);
							} else if (value1.matches(ticketNoRgx)) {
								invoiceTab1.setTicketNo(value1);
							} else if (pattern.matcher(value1).find()) {
								desc = value1;
							} else if (value1.matches(qtyRgx)) {
								invoiceTab1.setQuantity(value1);
							} else if (value1.matches(unitPrice)) {
								invoiceTab1.setUnitPrice(arr3[arr3.length - 2]);
								invoiceTab1.setNetAmount(arr3[arr3.length - 1]);
							} else {
								invoiceTab1.setOrderNo(value1);
							}
						}
					} else {
						pattern = Pattern.compile(descRgx);
						if (pattern.matcher(value).find()) {
							desc += " " + value;
						} else {
						}
					}
					invoiceTab1.setDescription(desc);
				}
				if (invoiceTab1.getTicketNo() == null) {
					invoiceTab1.setTicketNo("");
				}
				if (invoiceTab1.getOrderNo() == null) {
					invoiceTab1.setOrderNo("");
				}
				invoiceTable1.add(invoiceTab1);

			}
			invoiceTable1.add(invoiceTab);

		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return invoice;
	}

}
