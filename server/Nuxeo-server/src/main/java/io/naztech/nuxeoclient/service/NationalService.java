package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.FileInputStream;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

import java.io.ByteArrayInputStream;
import java.io.File;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.nuxeo.client.NuxeoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NationalService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(NationalService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.national}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.national.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.national.type}")
	private String nuxeoinvoiceType;

	@Value("${import.national.prefix}")
	private String prefix;

	@Value("${import.nuxeo.national.description}")
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

	@Value("${national.supplierName}")
	private String supplierName;

	@Value("${national.vatNo}")
	private String vatNo;

	@Value("${national.invoiceNo}")
	private String invoiceNo;

	@Value("${national.invoiceDte}")
	private String invoiceDte;

	@Value("${national.invoiceAddress}")
	private String invoiceAddress;

	@Value("${national.referNo}")
	private String referNo;

	@Value("${national.delevaryAddress}")
	private String delevaryAddress;

	@Value("${national.accountNo}")
	private String accountNo;

	@Value("${national.delevaryAddr}")
	private String delevaryAddr;

	@Value("${national.suplierAddress}")
	private String suplierAddress;

	@Value("${national.phone}")
	private String phone;

	@Value("${national.faxNo}")
	private String faxNo;

	@Value("${national.netTotal}")
	private String netTotal;

	@Value("${national.netTotalC1}")
	private String netTotalC1;

	@Value("${national.vatTotal}")
	private String vatTotal;

	@Value("${national.grossTotalC1}")
	private String grossTotalC1;

	@Value("${national.orderNo}")
	private String orderNo;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		String json = pdfStr;
		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();

		try {

			invoice.setSortName(Constants.NATIONAL);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);

			// ============ INvoice Date =====================
			String[] invoiceDateA1 = json.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = json.split(invoiceAddress);
			String invoiceAddressA2 = null;
			for (int kk = 0; kk < invoiceAddressA1.length; kk++) {
				invoiceAddressA2 = invoiceAddressA1[kk].trim();
			}
			String invoiceAddressA3[] = invoiceAddressA2.split(referNo);
			String invoiceAddress = invoiceAddressA3[0].trim();

			invoice.setCustomerAddress(invoiceAddress);
			String costumernameA1[] = invoiceAddress.split("\r\n");
			invoice.setCustomerName(costumernameA1[0]);
			// ===============Delivery Address ================
			String[] deleveryAddressA1 = json.split(delevaryAddress);
			String deleveryAddressA2 = deleveryAddressA1[1].trim();
			String deleveryAddressA3[] = deleveryAddressA2.split(delevaryAddr);
			String deleveryAddress = deleveryAddressA3[0].trim();
			invoice.setDeliveryAddress(deleveryAddress);
			;
			// ===============Supleyer Address ================
			String[] supleyerAddressA1 = json.split(suplierAddress);
			String supleyer = null;
			for (int i = 0; i < supleyerAddressA1.length; i++) {
				supleyer = supleyerAddressA1[i].trim();
			}
			String supleyerAddressA2[] = supleyer.split("Tel:");
			String suplierAddress = supleyerAddressA2[0].trim();
			invoice.setSupplierAddress(suplierAddress);
			// ===============Account No ================
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			// ===============VAT No Address ================
			String[] vatNoA1 = json.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo = vatNoA3[0].trim();
			invoice.setVatNo(vatNo);
			// ==============Telephone no ===========
			String telephoneA1 = json.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-Z]");
			String phone = telephoneA3[0].trim();
			invoice.setTelephone(phone);
			// ==============Telephone no ===========
			String faxA1 = json.trim();
			String faxA2[] = faxA1.split(faxNo);
			String faxA3[] = faxA2[1].split("[a-zA-Z]");
			String fax = faxA3[0].trim();
			invoice.setFax(fax);
			// ===============Net Total Address ================
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = null;
			for (int ff = 0; ff < netTotalA1.length; ff++) {
				netTotalA2 = netTotalA1[ff].trim();
			}
			String netTotalA3[] = netTotalA2.split(netTotalC1);
			String netTotalA4 = netTotalA3[0].replaceFirst("[\\W]Gross[\\W]", "").trim();
			String netTotalA5 = netTotalA4.replaceFirst("\\W", "");
			String netTotal = netTotalA5.trim();
			invoice.setNetTotal(netTotal);
			// =============== Carriage ================
			String[] carriageA1 = json.split(vatTotal);
			String carriageA2 = null;
			for (int ff = 0; ff < carriageA1.length; ff++) {
				carriageA2 = carriageA1[1].trim();
			}
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setCarriageNet(carriage);
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = json.split(grossTotalC1);
			String invoiceTotalA2 = null;
			for (int ff = 0; ff < invoiceTotalA1.length; ff++) {
				invoiceTotalA2 = invoiceTotalA1[ff].trim();
			}
			String invoiceTotalA3[] = invoiceTotalA2.split(netTotalC1);
			String invoiceTotalA4 = invoiceTotalA3[0].trim().replaceFirst("[\\W]", "").trim();
			String grossTotal = invoiceTotalA4.trim();
			invoice.setGrossTotal(grossTotal);
			// ============order number==========
			String[] ordernumberA1 = json.split(orderNo);
			String ordernumberA2 = ordernumberA1[1].trim();
			String ordernumberA3[] = ordernumberA2.split("[a-zA-Z]");
			String ordernumber = ordernumberA3[0].trim();
			invoice.setOrderNo(ordernumber);
			// ===============description Address ================
			String[] invoiceTableA1 = json.split("Price");
			String invoiceTableA2 = null;
			for (int ff = 0; ff < invoiceTableA1.length; ff++) {
				invoiceTableA2 = invoiceTableA1[ff].trim();
			}
			String invoiceTableA3[] = invoiceTableA2.split(delevaryAddress);
			String invoiceTableA4[] = invoiceTableA3[0].trim().split("£");
			String description = null, itemCode = null, quantity = null, shortCode = null, netTotalPrice = null,
					unitPrice = null;
			for (int i = 0; i < invoiceTableA4.length; i++) {
				String invoiceTableA5[] = invoiceTableA4[i].split("[a-zA-Z]");
				String invoiceTableA6[] = invoiceTableA5[0].replaceFirst("[\\d]*.[\\d]*\r\n", "").trim().split(" ");
				String str[] = invoiceTableA4[i].split("\r\n");
				//
				if (i % 2 == 0 && i != invoiceTableA4.length - 1) {

					// ============ Description =================
					String descriptionA1 = invoiceTableA4[i].replaceFirst("[\\d]*.[\\d]*\r\n", "").trim();
					String descriptionA2 = descriptionA1.replaceAll(invoiceTableA6[0], ""),
							descriptionA3 = descriptionA2.replaceAll(invoiceTableA6[1], ""),
							descriptionA4 = descriptionA3.replaceAll(invoiceTableA6[2], "");
					description += descriptionA4 + "; ";
					// ===================Item No===============
					itemCode += invoiceTableA6[0] + ";";
					// ======= Quentity==================
					quantity += invoiceTableA6[1] + "; ";
					// ===================SortCote===============
					shortCode += invoiceTableA6[2] + "; ";

				}
				if (i % 2 != 0) {

					// ===================UnitPrice ===============
					unitPrice += str[0] + "; ";
				}
			}
			for (int i = 1; i < invoiceTableA4.length; i++) {
				String str[] = invoiceTableA4[i].split("\r\n");
				if (i % 2 == 0 && i != invoiceTableA4.length - 1) {

					// ===================Total Net Price===============
					netTotalPrice += str[0] + "; ";

				}
			}
			String descriptionA1[] = description.split(";");
			String itemCodeA1[] = itemCode.split(";");
			String quantityA1[] = quantity.split(";");
			String shortCodeA1[] = shortCode.split(";");
			String netTotalPriceA1[] = netTotalPrice.split(";");
			String unitPriceA1[] = unitPrice.split(";");
			for (int i = 0; i < descriptionA1.length; i++) {

				InvoiceTable table = new InvoiceTable();
				table.setDescription(descriptionA1[i]);
				table.setItemNo(itemCodeA1[i].replaceAll("null", "").trim());
				table.setQuantity(quantityA1[i].replaceAll("null", "").trim());
				table.setStockCode(shortCodeA1[i].replaceAll("null", "").trim());
				table.setTotalNetPrice(netTotalPriceA1[i].replaceAll("null", "").trim());
				table.setUnitPrice(unitPriceA1[i].replaceAll("null", "").trim());
				invoiceTab.add(table);

			}

		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return invoice;
	}

}
