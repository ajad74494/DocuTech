package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * @author abul.kalam
 * @author muhammad.tarek
 * @since 2020-07-14
 *
 */

@Service
public class PirtekService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(PirtekService.class);

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

	@Value("${folder.name.pirtek}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.pirtek.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.pirtek.type}")
	private String nuxeoinvoiceType;

	@Value("${import.pirtek.prefix}")
	private String prefix;

	@Value("${import.nuxeo.pirtek.description}")
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

	@Value("${prtek.vatNo}")
	private String vatNo;

	@Value("${prtek.netTotal}")
	private String netTotal;

	@Value("${prtek.supplierName}")
	private String supplierName;

	@Value("${prtek.invoiceNo}")
	private String invoiceNo;

	@Value("${prtek.invoiceDte}")
	private String invoiceDte;
 
 
	@Value("${prtek.invoiceAddress}")
	private String invoiceAddress;

	@Value("${prtek.delevaryAddress}")
	private String delevaryAddress;

	@Value("${prtek.suplierAddress}")
	private String suplierAddress;

	@Value("${prtek.delevaryAddr}")
	private String delevaryAddr;

	@Value("${prtek.accountNo}")
	private String accountNo;

	@Value("${prtek.phone}")
	private String phonepro;

	@Value("${prtek.faxNo}")
	private String faxNo;

	/**
	 * Constructs a FestoolInvoice object from the xml file
	 * 
	 * @param file
	 * @return FestoolInvoice Object
	 * @throws IOException
	 */

	 
	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		Boolean sendToNuxeoFlag = true;
		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();


		invoice.setSortName(Constants.PIRTEK); 
		invoice.setInvoiceTitle(nuxeoinvoiceName);
		invoice.setInvoiceDescription(desc);
		invoice.setPrefix(prefix);
		invoice.setInvoiceType(nuxeoinvoiceType);

		try {
			String json=pdfStr;
			// ===============Delivery Address ================
			String[] dalevaryAddressA1 = json.split("[A-Z]{3}[\\d]+");
			String deleveryAddress = dalevaryAddressA1[0].trim();
			invoice.setDeliveryAddress(deleveryAddress);

			// ===============Supplier Address ================
			String[] supplierAddressA1 = json.split(suplierAddress);
			String supplierAddressA2 = supplierAddressA1[1].trim();
			String supplierAddress1[] = supplierAddressA2.split(phonepro);
			invoice.setSupplierAddress(supplierAddress1[0].trim());
			// ===============Phone Address ================
			String[] phone = json.split(phonepro);
			String phoneA1 = phone[1].trim();
			String phoneA2[] = phoneA1.split("[a-zA-Z]");
			invoice.setTelephone(phoneA2[0].trim());
			// ===============Fax Address ================
			String[] faxA1 = json.split(faxNo);
			String faxA2 = faxA1[1].trim();
			String faxA3[] = faxA2.split("[a-zA-Z]");
			invoice.setFax(faxA3[0].trim());
			// ===============Email Address ================
			String[] emailA1 = json.split(netTotal);
			String emailA2 = emailA1[1].trim();
			String emailA3[] = emailA2.split(invoiceNo);
			invoice.setEmail(emailA3[0].trim());
			// ============ INvoice =====================
			String[] invoiceA1 = json.split(deleveryAddress);
			String invoiceA2 = invoiceA1[1].trim();
			String iv[] = invoiceA2.split("\r\n");
			String invoice1 = iv[0].trim();
			invoice.setInvoiceNumber(invoice1);
			// ============ Invoice Order =====================
			String order = iv[2].trim();
			invoice.setOrderNo(order);
//			// ============ INvoice Date ===================== 
			String invoiceDate = iv[4].trim();
			invoice.setInvoiceDate(invoiceDate);

			// ===============On Date Address ================
			String[] costumerAddressA1 = json.split(invoiceDate);
			String costumerAddressA2 = costumerAddressA1[1].trim();
			String customerAddress[] = costumerAddressA2.split(vatNo);
			invoice.setCustomerAddress(customerAddress[0].trim());
			invoice.setCustomerName(supplierName);
			// ===============VAT No ================
			String[] vatA1 = json.split(vatNo);
			String vatA2 = vatA1[1].trim();
			String vatA3[] = vatA2.split("[a-zA-Z]");
			String vatNo = vatA3[0].trim();
			invoice.setVatNo(vatNo);
			// ===============Net Total ================
			String[] netTotalB1 = json.split(delevaryAddr);
			String netTotalB2 = netTotalB1[1].trim();
			String netTotalB5[] = netTotalB2.split("[a-zA-Z]");
			String netTotalB3[] = netTotalB5[0].split("£");

			String netTotalB4 = netTotalB3[3].replaceAll("-", "");
			String netTotal = netTotalB4.trim();
			invoice.setNetTotal(netTotal);
			// ===============Vat Total ================
			String vatTotalA5 = netTotalB3[1];
			String vatTotal = vatTotalA5.trim();
			invoice.setVatTotal(vatTotal);
			// =============== Net Total Address ================
			String netTotalA1 = netTotalB3[2];
			String grossTotal = netTotalA1.trim();
			invoice.setGrossTotal(grossTotal);

			String itemNumber = null, itemDescription = null, invoiceQuentity = null, unitePrice = null, amount = null;
			// ===============Discription Address ================
			String[] invoiceTableA1 = json.split(netTotal);
			String invoiceTableA2 = invoiceTableA1[1].replaceAll(netTotal, "").trim();
			String invoiceTableA3 = invoiceTableA2.replaceAll(vatTotal, "").trim();
			String invoiceTableA4 = invoiceTableA3.replaceAll(grossTotal, "").trim();
			String invoiceTableA5[] = invoiceTableA4.split("\r\n");
			for (int i = 0; i < invoiceTableA5.length; i++) {
				int count = (invoiceTableA5.length / 6) + 1;
				if (i <= count) {
					itemNumber += invoiceTableA5[i];
				}
				if (i > count && i <= count + count + 1) {
					itemDescription += invoiceTableA5[i];
				}
				if (i > count * 2 + 1 && i <= (count * 3) + 2) {
					invoiceQuentity += invoiceTableA5[i];
				}
				if (i > (count * 3) + 2 && i <= (count * 4) + 3) {
					unitePrice += invoiceTableA5[i];
				}
				if (i > (count * 4) + 3 && i <= (count * 5) + 4) {
					amount += invoiceTableA5[i];
				}

			}

			String itemNumberA1[] = itemNumber.split(";");
			String itemDescriptionA1[] = itemDescription.split(";");
			String invoiceQuentityA1[] = invoiceQuentity.split(";");
			String unitePriceA1[] = unitePrice.split(";");
			String amountA5[] = amount.split(";");
			for (int i = 0; i < amountA5.length; i++) {
				InvoiceTable table = new InvoiceTable();
				table.setItemNo(itemNumberA1[i].replaceFirst("null", ""));
				table.setDescription(itemDescriptionA1[i].replaceFirst("null", ""));
				// table.setInvoiceQuantity (invoiceQuentityA1[i].replaceFirst("null", ""));
				table.setUnitPrice(unitePriceA1[i].replaceFirst("null", ""));
				table.setTotal(amountA5[i].replaceFirst("null", ""));
				invoiceTab.add(table);
			}
			invoice.setInvoiceTable(invoiceTab);
		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
//		uploadDocument(invoice, file, sendToNuxeoFlag);
		return invoice;
	}
}
