package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author masud.ahmed
 * @since 2020-07-19
 */

@Service
public class HandlingTruckService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HandlingTruckService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.hts}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.htshandlingtruck.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.htshandlingtruck.type}")
	private String nuxeoinvoiceType;

	@Value("${import.htshandlingtruck.prefix}")
	private String prefix;

	@Value("${import.nuxeo.htshandlingtruck.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${import.processorType}")
	private String proType;

	@Value("${import.docType}")
	private String docType;

	@Value("${import.source}")
	private String sourceType;

	@Value("${hts.invoiceRgx}")
	private String invoiceRgx;

	@Value("${archive.enabled}")
	private boolean enabled;

	@Value("${hts.pageRgx}")
	private String pageRgx;

	@Value("${hts.invoiceAddssRegex}")
	private String invoiceAddssRegex;

	@Value("${hts.phoneRgx}")
	private String phoneRgx;

	@Value("${hts.faxRgx}")
	private String faxRgx;

	@Value("${hts.emailRgx}")
	private String emailRgx;

	@Value("${hts.emailRgx1}")
	private String emailRgx1;

	@Value("${hts.emailRgx2}")
	private String emailRgx2;

	@Value("${hts.webSiteRgx}")
	private String webSiteRgx;

	@Value("${hts.orderNoRgx}")
	private String orderNoRgx;

	@Value("${hts.deliveryAddressRgx}")
	private String deliveryAddressRgx;

	@Value("${hts.vatNoRgx}")
	private String vatNoRgx;

	@Value("${hts.vatnoRgx1}")
	private String vatnoRgx1;

	@Value("${hts.orderAndDocNoRgx}")
	private String orderAndDocNoRgx;

	@Value("${hts.accNoAndTaxRgx}")
	private String accNoAndTaxRgx;

	@Value("${hts.totalsRgx}")
	private String totalsRgx;

	@Value("${hts.carrageRgx}")
	private String carrageRgx;

	@Value("${hts.totalNetRgx}")
	private String totalNetRgx;

	@Value("${hts.totalNetRgx1}")
	private String totalNetRgx1;

	@Value("${hts.totalTaxRgx}")
	private String totalTaxRgx;

	@Value("${hts.totalTaxRgx1}")
	private String totalTaxRgx1;

	@Value("${hts.invoiceTotalRgx}")
	private String invoiceTotalRgx;

	@Value("${hts.discRgx}")
	private String discRgx;

	@Value("${hts.invoiceTotalRgx1}")
	private String invoiceTotalRgx1;

	@Value("${hts.vatRegRgx}")
	private String vatRegRgx;

	@Value("${hts.vatRegRgx1}")
	private String vatRegRgx1;

	@Value("${hts.registeredRgx}")
	private String registeredRgx;

	@Value("${hts.tableRgx}")
	private String tableRgx;

	@Value("${hts.tableRgx1}")
	private String tableRgx1;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTable1 = new ArrayList<InvoiceTable>();
		Boolean sendToNuxeoFlag = true;
		try {
			String data = pdfStr;

			String x, y = "", z;
			int start_idx, end_idx = 0;
			String[] arr0, arr1;

			// supplier detail
			x = data.substring(0, data.indexOf(invoiceRgx));
			x = x.replaceAll(pageRgx, "");
			x = x.replaceAll("\\s{10,}", ",");
			x = x.substring(1, x.length() - 1);// supplier address
			invoice.setSupplierAddress(x);
			x = data.substring(data.indexOf(invoiceRgx), data.indexOf(invoiceAddssRegex));
			arr0 = x.split("\\n");
			for (String value : arr0) {
				value = value.trim();
				if (value.matches(phoneRgx)) {
					// telephone
					x = x.replaceAll(phoneRgx, "");
					invoice.setTelephone(value.replaceAll(phoneRgx, "$2").trim().replaceAll("\\s{2,}", "")); // telephone
				} else if (value.matches(faxRgx)) {
					// fax
					x = x.replaceAll(faxRgx, "");
					invoice.setFax(value.replaceAll(faxRgx, "$2").trim().replaceAll("\\s{2,}", ""));// Fax
				} else if (value.matches(emailRgx)) {
					// email
					x = x.replaceAll(emailRgx1, "");
					invoice.setEmail(value.replaceAll(emailRgx2, "$2").trim());
					;// email
						// address
				} else if (value.matches(webSiteRgx)) {
					// web site
					x = x.replaceAll(webSiteRgx, "");
					invoice.setWebsite(value.replaceAll(webSiteRgx, "$2").trim());// website
				}
			}
			invoice.setSupplierName(x.replaceAll(invoiceRgx, "").trim().replaceAll("\\s{2,}", " "));// supplier name
			// ===================== customer detail and delivery detail
			// ========================
			x = data.substring(data.indexOf(invoiceAddssRegex), data.indexOf(orderNoRgx));

			x = x.replaceAll(invoiceAddssRegex, "").replaceAll(deliveryAddressRgx, "");
			arr0 = x.split(vatNoRgx);
			invoice.setVatNo(arr0[1].replaceAll("[A-Za-z]", "").trim());// customer vat reg
			x = x.replaceAll(vatnoRgx1, "                               ");

			arr0 = x.split("\\n");
			String customer_detail = "";
			String delivery_detail = "";
			for (String value : arr0) {
				value = value.trim();
				value = value.replaceAll("\\s{20,}", "                              ");

				arr1 = value.split("\\s{30,}");
				customer_detail += "," + arr1[0].trim();
				try {
					delivery_detail += "," + arr1[1].trim();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			invoice.setSortName(Constants.HTSHANDLINGTRUCK);
			invoice.setInvoiceTitle("handling");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("handling");
			invoice.setInvoiceType(nuxeoinvoiceType);

			customer_detail = customer_detail.replaceAll(",{2,}", ",");
			customer_detail = customer_detail.replaceAll("^,", "");
			customer_detail = customer_detail.substring(0, customer_detail.length() - 1);// customer detail
			invoice.setCustomerAddress(customer_detail);
			delivery_detail = delivery_detail.replaceAll(",{2,}", ",");
			delivery_detail = delivery_detail.replaceAll("^,", "");
			delivery_detail = delivery_detail.substring(0, delivery_detail.length());// delivery_detail
			invoice.setDeliveryAddress(delivery_detail);
			// =========

			x = data.substring(data.indexOf(orderNoRgx), data.indexOf(discRgx));
			arr0 = null;
			arr0 = x.split("\\n");
			for (String value : arr0) {
				value = value.trim();
				if (value.matches(orderAndDocNoRgx)) {
					// order no and document no
					invoice.setOrderNo(value.replaceAll(orderAndDocNoRgx, "$2"));
					invoice.setInvoiceNumber(value.replaceAll(orderAndDocNoRgx, "$4"));
				} else if (value.matches(accNoAndTaxRgx)) {
					// acc no and tax point
					invoice.setAccountNo(value.replaceAll(accNoAndTaxRgx, "$2"));
					invoice.setInvoiceDate(value.replaceAll(accNoAndTaxRgx, "$4"));
				}
			}

			// invoice total

			arr0 = null;
			arr0 = data.split("\\n");
			for (String value : arr0) {
				value = value.trim();
				if (value.matches(totalsRgx)) {
					// _totalAmount
					invoice.setTotal(value.replaceAll(totalsRgx, "$1"));
				} else if (value.matches(carrageRgx)) {
					// _carriageNet
					invoice.setCarriageNet(value.replaceAll(carrageRgx, "$1"));
				}
			}
			arr0 = data.split("\\n");
			arr1 = data.split("\\n");
			for (int index = 0; index < arr0.length; index++) {
				if (arr0[index].contains(totalNetRgx)) {
					arr0[index] = arr0[index].replaceAll(totalNetRgx1, "").trim();
					arr1[index] = arr1[index].trim();
					arr1[index] = arr1[index].substring(arr0[index].length(), arr1[index].length());
					arr1[index].trim().replaceAll(totalNetRgx1, "$2");// value
					invoice.setNetTotal(arr1[index].trim().replaceAll(totalNetRgx1, "$2"));
				} else if (arr0[index].contains(totalTaxRgx)) {
					arr0[index] = arr0[index].replaceAll(totalTaxRgx1, "").trim();
					arr1[index] = arr1[index].trim();
					arr1[index] = arr1[index].substring(arr0[index].length(), arr1[index].length());
					invoice.setVatTotal(arr1[index].trim().replaceAll(totalTaxRgx1, "$2"));// value
				} else if (arr0[index].contains(invoiceTotalRgx)) {
					arr0[index] = arr0[index].replaceAll(invoiceTotalRgx1, "").trim();
					arr1[index] = arr1[index].trim();
					arr1[index] = arr1[index].substring(arr0[index].length(), arr1[index].length());
					invoice.setGrossTotal(arr1[index].trim().replaceAll(invoiceTotalRgx1, "$2"));// invoice
																									// total
																									// value
				} else if (arr0[index].contains(vatRegRgx)) {
					arr0[index] = arr0[index].replaceAll(vatRegRgx1, "").trim();
					arr1[index] = arr1[index].trim();
					arr1[index] = arr1[index].substring(arr0[index].length(), arr1[index].length());
					invoice.setVatReg(arr1[index].trim().replaceAll(vatRegRgx1, "$1".trim()));// value

				}
				arr0[index] = arr0[index].trim();
				if (arr0[index].matches(registeredRgx)) {
					// reg no
				}
			}

			// ===================== table ============================
			String extra_desc = "";
			int index = 0;
			int first_indx = 0;
			boolean flag = false, flag2 = false;

			arr0 = data.split("\\n");
			for (String a : arr0) {

				a = a.trim();

				if (a.contains(tableRgx)) {
					extra_desc = a;
				}

				if (a.matches(tableRgx1)) {
					InvoiceTable table = new InvoiceTable();
					table.setDescription(extra_desc + "\n" + a.replaceAll(tableRgx1, "$1").replaceAll("\\s{2,}", " "));

					table.setQuantity(a.replaceAll(tableRgx1, "$2"));

					table.setUnit(a.replaceAll(tableRgx1, "$4"));

					table.setDiscount("" + a.replaceAll(tableRgx1, "$5"));
					table.setTotal(a.replaceAll(tableRgx1, "$6"));
					invoiceTable1.add(table);

				}
			}
			invoice.setInvoiceTable(invoiceTable1);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

	/**
	 * Constructs a FestoolInvoice object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

}
