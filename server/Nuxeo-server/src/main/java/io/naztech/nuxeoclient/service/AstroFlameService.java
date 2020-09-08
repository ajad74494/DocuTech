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
 * @author masud.ahmed
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

		invoice.setSortName(Constants.ASTROFLAME);
		invoice.setInvoiceTitle("astroflame");
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix("astroflame");
		invoice.setInvoiceType(nuxeoinvoiceType);

		String x;

		String[] arr0;
		String data = pdfStr;
		// ====================== supplier details ============================
		int start = 0;
		Pattern pattern = null;
		Matcher matcher = null;
		// ====================== supplier details ============================
		pattern = Pattern.compile(supplierRegex2);
		matcher = pattern.matcher(data);
		while (matcher.find()) {
			start = matcher.start();
		}
		String sup_regex = supplierRegex;
		x = data.substring(0, start);
		x = x.replaceAll(supplierRegex1, " ");
		String supplierName = x.replaceAll(sup_regex, "$1").trim();
		String supplierAddress = x.replaceAll(sup_regex, "$2").replaceAll("\\r\\n", "").trim();
		String tel = x.replaceAll(sup_regex, "$4").trim();
		String fax = x.replaceAll(sup_regex, "$6").trim();
		String email = x.replaceAll(sup_regex, "$8").trim();
		invoice.setSupplierName(supplierName);// name value
		invoice.setSupplierAddress(supplierAddress);// address value
		invoice.setTelephone(tel);// phone value
		invoice.setFax(fax);// Fax value
		invoice.setEmail(email);// Email value
		// ====================== customer and delivery detail ==================
		String[] regexArr = new String[10];
		regexArr[0] = customerRegex;
		regexArr[1] = customerRegex1;
		int[] dataIndex = new int[10];
		for (int i = 0; i < regexArr.length; i++) {
				try {
					pattern = Pattern.compile(regexArr[i]);
					matcher = pattern.matcher(data);
					while (matcher.find()) {
						start = matcher.start();
					}
					dataIndex[i] = start;
				} catch (Exception e) {
					// TODO: handle exception
				}
		}
		x = data.substring(dataIndex[0], dataIndex[1]);
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
		customer_detail= customer_detail.replaceAll(",{2,}|^,", "");
		delivery_detail = delivery_detail.replaceAll(",{2,}|^,", "");
		invoice.setCustomerAddress(customer_detail);
		invoice.setDeliveryAddress(delivery_detail);
		arr0 = null;
		arr0 = customer_detail.replaceAll(",{2,}|^,", "").split(",");
		invoice.setCustomerName(arr0[0]);// value
		arr0 = null;

		// ===================== invoice detail ===================
		regexArr[0] = invoiceNo;
		regexArr[1] = customerRegex1;
		for (int i = 0; i < regexArr.length; i++) {
				try {
					pattern = Pattern.compile(regexArr[i]);
					matcher = pattern.matcher(data);
					while (matcher.find()) {
						start = matcher.start();
					}
					dataIndex[i] = start;
				} catch (Exception e) {
					// TODO: handle exception
				}
		}
		x = data.substring(dataIndex[0], dataIndex[1]);
		arr0 = null;
		arr0 = x.split("\\n");
		for (String i : arr0) {
			i = i.trim();
			if (Pattern.compile(invoiceNo1).matcher(i).find()) {
				invoice.setInvoiceNumber(i.replaceAll(invoiceNo1, "$2"));// value
			} else if (Pattern.compile(invoiceDate).matcher(i).find()) {
				invoice.setInvoiceDate(i.replaceAll(invoiceDate1, "$2"));// value
			} else if (Pattern.compile(yourRef).matcher(i).find()) {
				i.replaceAll(yourRef1, "$2");// value
			} else if (Pattern.compile(accountRef).matcher(i).find()) {
				String refNo = i.trim();
				i = i.replaceAll(accountRef1, "").trim();
				refNo = refNo.substring(i.length()).replaceAll(accountRef1, "$2").trim();
				invoice.setReferenceNumber(refNo);// value
			} else if (Pattern.compile(despatchDate).matcher(i).find()) {
				invoice.setDespatchDate(i.replaceAll(despatchDate1, "$2"));// value
			} else if (Pattern.compile(deliveryNoteNo).matcher(i).find()) {
				String deliveryNote = i.trim();
				i = i.replaceAll(deliveryNoteNo1, "").trim();
				deliveryNote = deliveryNote.substring(i.length()).replaceAll(deliveryNoteNo1, "$2").trim();
				invoice.setDeliveryNoteNo(deliveryNote);// value
			}
		}
		
		// ================== invoice total ===================
		arr0 = null;
		arr0 = data.split("\\n");
		for (String i : arr0) {
			i = i.trim();
			Pattern.compile(vatRegNo).matcher(i).find();
			if (Pattern.compile(totalNet).matcher(i).find()) {
				invoice.setNetTotal(i.replaceAll(totalNet1, "$2"));
			} else if (Pattern.compile(totalVat).matcher(i).find()) {
				invoice.setVatTotal(i.replaceAll(totalVat1, "$2"));
			} else if (Pattern.compile(totalGross).matcher(i).find()) {
				invoice.setGrossTotal(i.replaceAll(totalGross1, "$2"));
			} else if (Pattern.compile(vatRegNo).matcher(i).find()) {
				invoice.setVatReg(i.replaceAll(vatRegNo1, "$2"));// value
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
		log.toString();
		invoice.setInvoiceTable(invoiceTable);
		return invoice;
	}

}
