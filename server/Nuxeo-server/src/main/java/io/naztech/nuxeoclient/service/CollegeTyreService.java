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
 * @author abul.kalam
 * @author muhammad.tarek
 * @since 2020-06-29
 *
 */
@Service
public class CollegeTyreService implements PdfInvoiceProcessor {

	private static Logger log = LoggerFactory.getLogger(CollegeTyreService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.college}")
	private String folderName;

	@Value("${import.college.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.college.type}")
	private String nuxeoinvoiceType;

	@Value("${import.college.prefix}")
	private String prefix;

	@Value("${import.nuxeo.college.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${collegeTyre.supplierName}")
	private String supplierName;

	@Value("${collegeTyre.invoiceNo}")
	private String invoiceNo;

	@Value("${collegeTyre.invoiceDate}")
	private String invoiceDate;

	@Value("${collegeTyre.customerProduct}")
	private String customerProduct;

	@Value("${collegeTyre.customerAddress}")
	private String customerAddress;

	@Value("${collegeTyre.units}")
	private String units;

	@Value("${collegeTyre.delevaryAddress}")
	private String delevaryAddress;

	@Value("${collegeTyre.invoiceNetTotalk}")
	private String invoiceNetTotalk;

	@Value("${collegeTyre.vat}")
	private String vat;

	@Value("${collegeTyre.balenchDeo}")
	private String balenchDeo;

	@Value("${collegeTyre.unitTable}")
	private String unitTable;

	@Value("${collegeTyre.totalt}")
	private String totalt;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		List<InvoiceTable> invoiceTab = new ArrayList<InvoiceTable>();

		try {
			invoice.setSortName(Constants.COLLEGETYRES);
			invoice.setInvoiceTitle(nuxeoinvoiceName);
			invoice.setInvoiceDescription(desc);
			invoice.setPrefix(prefix);
			invoice.setInvoiceType(nuxeoinvoiceType);
			invoice.setSupplierName(supplierName);
			try {
				// ============ INvoice =====================
				String[] invData = pdfStr.split(invoiceNo);
				String inv = null;
				for (int kk = 0; kk < invData.length; kk++) {
					inv = invData[1].trim();
				}
				String invoices[] = inv.split("[a-zA-Z]");
				String invoice1 = invoices[0].trim();
				invoice.setInvoiceNumber(invoice1);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			// ============ INvoice Date=====================
			String[] invoiceDatedata = pdfStr.split(invoiceDate);
			String inv = null;
			for (int kk = 0; kk < invoiceDatedata.length; kk++) {
				inv = invoiceDatedata[1].trim();
			}
			String invDate[] = inv.split("[a-zA-Z]");
			String invoiceDate = invDate[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			// ============ Prodect No=====================
			try {
				String[] costomerpro = pdfStr.split(customerProduct);
				String costomerproA = null;
				for (int kk = 0; kk < costomerpro.length; kk++) {
					costomerproA = costomerpro[1].trim();
				}
				String product[] = costomerproA.split(supplierName);
				String proNo = product[0].replaceAll("No.:", "").trim();
				invoice.setReferenceNumber(proNo);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// ============ costomerAddress No=====================
			String[] costomaddress = pdfStr.split(customerAddress);
			String addressCostomer = null;
			for (int kk = 0; kk < costomaddress.length; kk++) {
				addressCostomer = costomaddress[1].trim();
			}
			String customer[] = addressCostomer.split(units);
			String costomerAddress = customer[0].trim();
			invoice.setCustomerAddress(costomerAddress);
			// ===============Invoice Address ================
			String[] invoiceaddr = pdfStr.split(supplierName);
			String invadd = null;
			for (int kk = 0; kk < invoiceaddr.length; kk++) {
				invadd = invoiceaddr[1].trim();
			}
			String invaddress[] = invadd.split("[\\d] [\\d]");
			String invoiceAddress = invaddress[0].trim();
			invoice.setDeliveryAddress(invoiceAddress);
			// ===============Delivery Address ================
			String[] deleveryadd = pdfStr.split(delevaryAddress);
			String inv_delevery_add = null;
			for (int kk = 0; kk < deleveryadd.length; kk++) {
				inv_delevery_add = deleveryadd[1].trim();
			}
			String deleveryaddress[] = inv_delevery_add.split(customerAddress);
			String deleveryAddress = deleveryaddress[0].trim();
			invoice.setSupplierAddress(deleveryAddress);
			// ===============Delivery Address ================
			String[] costumadd = pdfStr.split(delevaryAddress);
			String addressCostum = null;
			for (int kk = 0; kk < costumadd.length; kk++) {
				addressCostum = costumadd[1].trim();
			}
			String _Coustomeraddress[] = addressCostum.split("\r\n");
			String costum_Name = _Coustomeraddress[0].trim();
			invoice.setCustomerName(costum_Name);

//				===================== Balance Due =====================
			String[] balenceDueA1 = pdfStr.split("Balance Due");
			String[] balanceDueA2 = balenceDueA1[1].split("[a-zA-Z]");
			invoice.setDue(balanceDueA2[0].trim());
			// ===============On Date Address ================
			String[] odate = pdfStr.split(supplierName);
			String invodate = null;
			for (int kk = 0; kk < odate.length; kk++) {
				invodate = odate[1].trim();
			}
			String invoice_odate[] = invodate.split(customerAddress);
			// ==============Telephone no ===========
			String telephone = invoice_odate[0].replaceAll("[a-zA-Z]", "").trim();

			String invoice_telephone = telephone.replaceFirst("[\\d]* [\\d]", "");
			String rep_telephone[] = invoice_telephone.split("@");
			String phone = rep_telephone[0].trim();
			invoice.setTelephone(phone);
			// ==============Email no ===========
			String inv_email = invoice_odate[0].trim();
			String invoiceEmail[] = inv_email.split("[\\d]* [\\d]+");
			String invoice_Email[] = invoiceEmail[2].split(delevaryAddress);
			String email = invoice_Email[0].trim();
			invoice.setEmail(email);
			// ===============Net Total ================
			String[] invNetTotal = pdfStr.split(invoiceNetTotalk);
			String invNet = null;
			for (int ff = 0; ff < invNetTotal.length; ff++) {
				invNet = invNetTotal[1].trim();
			}
			String fff[] = invNet.split("[a-zA-Z]");
			String netTotal = fff[0].trim();
			invoice.setNetTotal(netTotal);
			// ===============Vat Total ================
			String[] vatTotalInvoice = pdfStr.split(vat);
			String invVat = null;
			for (int ff = 0; ff < vatTotalInvoice.length; ff++) {
				invVat = vatTotalInvoice[1].trim();
			}
			String vat_Total_Invoice[] = invVat.split("[a-zA-Z]");
			String invoiceVat = vat_Total_Invoice[0].replaceAll("[0-9]*%", "").trim();
			String vatTotal = invoiceVat.replace("()", "").trim();
			invoice.setVatNo(vatTotal);
			// ===============Net Total Address ================
			String[] netTotalInv = pdfStr.split(totalt);
			String invNetTo = null;
			for (int ff = 0; ff < netTotalInv.length; ff++) {
				invNetTo = netTotalInv[1].trim();
			}
			String invoiceNetTotal[] = invNetTo.split(balenchDeo);
			String grossTotal = invoiceNetTotal[0].trim();
			invoice.setGrossTotal(grossTotal);

			// ===============unit ================
			String[] unitTAble = pdfStr.split(unitTable);
			String tableUnit = null;
			for (int ff = 0; ff < unitTAble.length; ff++) {
				tableUnit = unitTAble[1].trim();
			}
			String invUnit[] = tableUnit.split(invoiceNetTotalk);
			String saparetunit[] = invUnit[0].split("\r\n");
			for (int i = 0; i < saparetunit.length; i++) {
				InvoiceTable invoiceTable = new InvoiceTable();

				String[] unitA1 = saparetunit[i].split(" £");
				String[] unitA2 = unitA1[0].split(" ");
				String unprice = unitA2[0].trim();
				invoiceTable.setUnitPrice(unprice);
				// ==================Description ===============================
				String description = unitA1[0].replaceFirst("[\\d]*", "").trim();
				invoiceTable.setDescription(description);
				// ==================unitprice ===============================
				String unitprice = unitA1[1].trim();
				invoiceTable.setUnitPrice(unitprice);
				// ==================total ===============================
				String total = unitA1[2].trim();
				invoiceTable.setTotal(total);
				invoiceTab.add(invoiceTable);
			}
			invoice.setInvoiceTable(invoiceTab);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return invoice;
	}
}
