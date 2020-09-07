package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
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
 * @author muhammad.tarek
 * @author abul.kalam
 * @since 2020-07-06
 */
@Service
public class AstroFlameService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(AstroFlameService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.astroflame}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.astroflame.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.astroflame.type}")
	private String nuxeoinvoiceType;

	@Value("${import.astroflame.prefix}")
	private String prefix;

	@Value("${import.nuxeo.astroflame.description}")
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

	@Value("${astro.supplierRegex}")
	private String supplierRegex;

	@Value("${astro.supplierRegex1}")
	private String supplierRegex1;

	@Value("${astro.supplierRegex2}")
	private String supplierRegex2;

	@Value("${astro.customerRegex}")
	private String customerRegex;

	@Value("${astro.customerRegex1}")
	private String string;

	@Value("${astro.customerRegx2}")
	private String customerRegx2;

	@Value("${astro.customerRegx3}")
	private String customerRegx3;

	@Value("${astro.customerRegex4}")
	private String customerRegex4;

	@Value("${astro.customerRegex5}")
	private String customerRegex5;

	@Value("${astro.customerRegex6}")
	private String customerRegex6;

	@Value("${astro.customerRegex7}")
	private String customerRegex7;

	@Value("${astro.customerRegex8}")
	private String customerRegex8;

	@Value("${astro.customerRegex9}")
	private String customerRegex9;

	@Value("${astro.invoiceNo}")
	private String invoiceNo;

	@Value("${astro.invoiceNo1}")
	private String invoiceNo1;

	@Value("${astro.invoiceDate}")
	private String invoiceDate;

	@Value("${astro.invoiceDate1}")
	private String invoiceDate1;

	@Value("${astro.yourRef}")
	private String yourRef;

	@Value("${astro.yourRef1}")
	private String yourRef1;

	@Value("${astro.accountRef}")
	private String accountRef;

	@Value("${astro.accountRef1}")
	private String accountRef1;

	@Value("${astro.despatchDate}")
	private String despatchDate;

	@Value("${astro.despatchDate1}")
	private String despatchDate1;

	@Value("${astro.deliveryNoteNo}")
	private String deliveryNoteNo;

	@Value("${astro.deliveryNoteNo1}")
	private String deliveryNoteNo1;

	@Value("${astro.totalNet}")
	private String totalNet;

	@Value("${astro.totalNet1}")
	private String totalNet1;

	@Value("${astro.totalVat}")
	private String totalVat;

	@Value("${astro.totalVat1}")
	private String totalVat1;

	@Value("${astro.totalGross}")
	private String totalGross;

	@Value("${astro.totalGross1}")
	private String totalGross1;

	@Value("${astro.vatRegNo}")
	private String vatRegNo;

	@Value("${astro.vatRegNo1}")
	private String vatRegNo1;

	@Value("${astro.invoiceTable}")
	private String invoiceTableRgx;

	@Value("${astro.customerRegex1}")
	private String customerRegex1;

	/**
	 * Constructs a FestoolInvoice object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
		try {
			invoice.setSortName(Constants.ASTROFLAME);
			invoice.setInvoiceTitle("astroflame");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("astroflame");
			invoice.setInvoiceType(nuxeoinvoiceType);

			String x;

			String[] arr0;
			String data = pdfStr;
			// ====================== supplier details ============================
			String sup_regex = supplierRegex;
			x = data.substring(0, data.indexOf(supplierRegex2));
			x = x.replaceAll(supplierRegex1, " ");
			invoice.setSupplierName(x.replaceAll(sup_regex, "$1").trim());// name value
			invoice.setSupplierAddress(x.replaceAll(sup_regex, "$2").replaceAll("\\r\\n", "").trim());// address value
			invoice.setTelephone(x.replaceAll(sup_regex, "$4").trim());// phone value
			invoice.setFax(x.replaceAll(sup_regex, "$6").trim());// Fax value
			invoice.setEmail(x.replaceAll(sup_regex, "$8").trim());// Email value
			// ====================== customer and delivery detail ==================
			x = data.substring(data.indexOf(customerRegex), data.indexOf(customerRegex1));
			x = x.replaceAll(customerRegx2, "");
			x = x.replaceAll(customerRegx3, "");
			x = x.replaceAll(customerRegex4, "");
			x = x.replaceAll(customerRegex5, "");
			x = x.replaceAll(customerRegex6, "");
			x = x.replaceAll(customerRegex7, "");
			x = x.replaceAll(customerRegex8, "");
			x = x.replaceAll(customerRegex, "").replaceAll(customerRegex9, "");
			// ======================= customer and delivery detail
			// ==================================
			arr0 = x.split("\\n");
			String customer_detail = "";
			String delivery_detail = "";
			for (String value : arr0) {
				value = value.trim();
				String[] arr1 = value.split("\\s{50,}");
				customer_detail += "," + arr1[0].trim();
				try {
					delivery_detail += "," + arr1[1].trim();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			invoice.setCustomerAddress(customer_detail.replaceAll(",{2,}|^,", ""));
			invoice.setDeliveryAddress(delivery_detail.replaceAll(",{2,}|^,", ""));
			arr0 = null;
			arr0 = customer_detail.replaceAll(",{2,}|^,", "").split(",");
			invoice.setCustomerName(arr0[0]);// value
			arr0 = null;

			// ===================== invoice detail ====================
			x = data.substring(data.indexOf(invoiceNo), data.indexOf(customerRegex1));
			arr0 = null;
			arr0 = x.split("\\n");
			for (String i : arr0) {
				i = i.trim();
				if (i.contains(invoiceNo)) {
					invoice.setInvoiceNumber(i.replaceAll(invoiceNo1, "$2"));// value
				} else if (i.contains(invoiceDate)) {
					invoice.setInvoiceDate(i.replaceAll(invoiceDate1, "$2"));// value
				} else if (i.contains(yourRef)) {
					i.replaceAll(yourRef1, "$2");// value
				} else if (i.contains(accountRef)) {
					invoice.setReferenceNumber(i.replaceAll(accountRef1, "$2"));// value
				} else if (i.contains(despatchDate)) {
					invoice.setDespatchDate(i.replaceAll(despatchDate1, "$2"));// value
				} else if (i.contains(deliveryNoteNo)) {
					invoice.setDeliveryNoteNo(i.replaceAll(deliveryNoteNo1, "$2"));// value
				}
			}
			// ================== invoice total ===================
			arr0 = null;
			arr0 = data.split("\\n");
			for (String i : arr0) {
				i = i.trim();
				if (i.contains(totalNet)) {
					invoice.setNetTotal(i.replaceAll(totalNet1, "$2"));
				} else if (i.contains(totalVat)) {
					invoice.setVatTotal(i.replaceAll(totalVat1, "$2"));
				} else if (i.contains(totalGross)) {
					invoice.setGrossTotal(i.replaceAll(totalGross1, "$2"));
				} else if (i.contains(vatRegNo)) {
					invoice.setVatNo(i.replaceAll(vatRegNo1, "$2"));// value
				}
			}

			// ================= table ==============================
			arr0 = null;
			arr0 = data.split("\\n");
			String table_regex = invoiceTableRgx;
			for (String value : arr0) {
				value = value.trim();
				if (value.matches(table_regex)) {
					InvoiceTable table = new InvoiceTable();
					table.setProductCode(value.replaceAll(table_regex, "$1").trim());
					table.setDescription(value.replaceAll(table_regex, "$2").trim());
					table.setBarCode(value.replaceAll(table_regex, "$3").trim());
					table.setIntrastatComm(value.replaceAll(table_regex, "$4").trim());
					table.setUnit(value.replaceAll(table_regex, "$5").trim());
					table.setQuantity(value.replaceAll(table_regex, "$6").trim());
					table.setUnitPrice(value.replaceAll(table_regex, "$7").trim());
					table.setDiscount(value.replaceAll(table_regex, "$8").trim());
					table.setNetAmount(value.replaceAll(table_regex, "$9").trim());

					invoiceTable.add(table);
				}
			}

			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return invoice;
	}

}
