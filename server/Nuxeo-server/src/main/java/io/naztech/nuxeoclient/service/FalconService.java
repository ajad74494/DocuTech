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
 * @author samiul.islam
 * @author asadullah.galib
 * @since 2020-04-04
 *
 */

@Service
public class FalconService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(FalconService.class);
 

	@Autowired
	NuxeoClientService nuxeoClientService; 

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.falcon}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.falcon.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.falcon.type}")
	private String nuxeoinvoiceType;

	@Value("${import.falcon.prefix}")
	private String prefix;

	@Value("${import.nuxeo.college.description}")
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

	@Value("${falcon.supplierName}")
	private String supplierName;

	@Value("${falcon.invoiceNo}")
	private String invoiceNo;

	@Value("${falcon.referNo}")
	private String referNo;

	@Value("${falcon.telephone}")
	private String telephone;

	@Value("${falcon.invoiceAddress}")
	private String invoiceAddress;

	@Value("${falcon.delevaryAddress}")
	private String delevaryAddress;

	@Value("${falcon.delevaryAddr}")
	private String delevaryAddr;

	@Value("${falcon.accountNo}")
	private String accountNo;

	@Value("${falcon.vatNo}")
	private String vatNo;

	@Value("${falcon.faxNo}")
	private String faxNo;

	@Value("${falcon.email}")
	private String email;

	@Value("${falcon.netTotal}")
	private String netTotal;

	@Value("${falcon.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${falcon.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${falcon.vatTotal}")
	private String vatTotalp;

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
		try {
			List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
			invoice.setSupplierName(supplierName);

			invoice.setSortName(Constants.FALCON);
			invoice.setInvoiceTitle("falcon");
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix("falcon");
			invoice.setInvoiceType("falcon"); 
			String json = pdfStr;
			try {
				// ============ INvoice =====================
				String[] invoiceA1 = json.split(invoiceNo);
				String invoiceA2 = invoiceA1[1].trim();
				String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
				String invoiceNo1 = invoiceA3[0].trim();
				invoice.setInvoiceNumber(invoiceNo1);

				String[] ourreferA1 = json.split(referNo);
				String ourreferA2 = ourreferA1[1].trim();
				String ourreferA3[] = ourreferA2.split("\r\n");
				String ourreferA4[] = ourreferA3[1].trim().split(" ");
				String ourReferA1 = ourreferA4[0];
				invoice.setReferenceNumber(ourReferA1);
				// ==========Delevary Address =================
				String delevaryAddress = ourreferA4[1];

				invoice.setDeliveryDate(delevaryAddress);

				String[] supliearAddressA1 = json.split(invoiceNo);
				String[] supliearAddressA2 = supliearAddressA1[1].split(telephone);
				String supliearAddressA = supliearAddressA2[0].replaceAll(invoiceNo1, "");
				invoice.setSupplierAddress(supliearAddressA);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ===============Invoice Address ================
			try {
				String[] invoiceAddressA1 = json.split(invoiceAddress);
				String invoiceAddressA2 = invoiceAddressA1[1].trim();

				String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);
				String invoiceAddress = invoiceAddressA3[0].trim();
				invoice.setCustomerAddress(invoiceAddress);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ===============Delivery Address ================
			try {
				String[] deleveryAddressA1 = json.split(delevaryAddress);
				String deleveryAddressA2 = deleveryAddressA1[1].trim();

				String deleveryAddressA3[] = deleveryAddressA2.split(delevaryAddr);

				String deleveryAddress = deleveryAddressA3[0].trim();
				invoice.setDeliveryAddress(deleveryAddress);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ===============Acc Address ================
			try {
				String[] accountA1 = json.split(referNo);
				String accountA2 = accountA1[1].trim();
				String accountA3[] = accountA2.split("[\\d]*-[\\w]*-[\\d]{4}");
				String accountA4[] = accountA3[1].split(accountNo);
				String accountNo = accountA4[0].trim();
				invoice.setAccountNo(accountNo);
				String[] invoiceDateA1 = json.split("[\\d]*/[\\d]*/[\\d]*");
				String invoiceDateA2 = invoiceDateA1[1].trim();
				String invoiceDateA3[] = invoiceDateA2.split(accountNo);
				String invoiceDate = invoiceDateA3[0].replaceAll(accountNo, "").trim();
				invoice.setInvoiceDate(invoiceDate);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ===============VAT No Address ================
			try {
				String[] vatnoA1 = json.split(vatNo);
				String vatnoA2 = vatnoA1[1].trim();

				String vatnoA3[] = vatnoA2.split("[\\D]{2}[a-zA-Z]");
				String vatNo = vatnoA3[0].trim();
				invoice.setVatNo(vatNo);

			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ===============On Date Address ================
			String telephoneA1[] = json.split(telephone);
			String telephoneA2[] = telephoneA1[1].split(faxNo);
			String phone = telephoneA2[0].trim();
			invoice.setTelephone(phone);
			// ==============Telephone no ===========
			String emailA1[] = json.split(email);
			String emailA2[] = emailA1[1].split("Sold to:");
			String email = emailA2[0].trim();
			invoice.setEmail(email);

			// ===============Net Total Address ================
			String[] nettottalA1 = json.split(netTotal);
			String nettottalA2 = nettottalA1[1].trim();
			String nettottalA3[] = nettottalA2.split("£");
			String netTotal = nettottalA3[1].trim();
			invoice.setNetTotal(netTotal);
			// ===============Vat Total Address ================

			String vattot[] = nettottalA2.split(vatTotalp);
			String out = vattot[2].trim();

			String vatTotal = out;
			invoice.setVatTotal(vatTotal);
			// ===============Net Total Address ================

			String grosstotA1[] = nettottalA2.split("£");
			String grosstotA2 = grosstotA1[3].trim();
			String grosstotA3[] = grosstotA2.split("[a-zA-Z]");
			String grossTotal = grosstotA3[0].trim();
			invoice.setGrossTotal(grossTotal);

			// ===============Description ================
			String price = null, totalpis = null, totaltk = null, description = null;

			try {
				String[] descripttationA1 = json.split(invoiceTableA1);
				String descripttationA2 = descripttationA1[1].trim();
				String descripttationA3[] = descripttationA2.split(invoiceTableA2);
				String descripttationA4[] = descripttationA3[0].split(vatTotalp);
				for (int i = 0; i < descripttationA4.length; i += 1) {

					String descripttationA5[] = descripttationA4[i].split(" ");
					if (i % 2 != 0) {
						// =================Price ================
						price += descripttationA5[0].trim() + "; ";
						// =================totalPis =======================
						totalpis += descripttationA5[1].trim() + "; ";

					} else if (i % 2 == 0) {
						String sk = descripttationA5[0].replaceAll("[\\d]{2}mm", "");
						totaltk += sk.trim() + "; ";
						String descrip = descripttationA4[i].replaceAll("[\\d]*,", "");
						description += descrip.replaceFirst("[\\d]*.[\\d]{2}", "").trim() + "; ";

					}

				}

				String invTableA1[] = price.split(";");
				String invTableA2[] = totalpis.split(";");
				String invTableA3[] = totaltk.split(";");
				String invTableA4[] = description.split(";");
				for (int i = 0; i <= invTableA4.length; i++) {

					InvoiceTable table = new InvoiceTable();

					table.setPrice(invTableA1[i].replaceAll("null", ""));
					table.setDescription(invTableA4[i].replaceAll("null", ""));
					table.setTotal(invTableA3[i].replaceAll("null", ""));
					String pice1 = invTableA2[i].replaceAll("null", "");
					table.setTotalNetPrice(pice1);
					invoiceTable.add(table);
				}
				invoice.setInvoiceTable(invoiceTable);

			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return invoice;

	}
}
