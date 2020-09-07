
package io.naztech.nuxeoclient.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;
/**
 * 
 * @author abul.kalam
 * @since 2020-08-10
 *
 */
@Service
public class PlasmorService implements PdfInvoiceProcessor { 
	private static Logger log = LoggerFactory.getLogger(PlasmorService.class);

	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${folder.name.biffa}")
	private String folderName;

	@Value("${import.biffa.Name}")
	private String nuxeoinvoiceName;

	@Value("${import.biffa.type}")
	private String nuxeoinvoiceType;

	@Value("${import.biffa.prefix}")
	private String prefix;

	@Value("${import.nuxeo.biffa.description}")
	private String desc;

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${plasmor.invoiceNo}")
	private String invoiceNo;

	@Value("${plasmor.invoiceDte}")
	private String invoiceDte;

	@Value("${plasmor.suplierAddress}")
	private String suplierAddress;

	@Value("${plasmor.delevaryAddr}")
	private String delevaryAddr;

	@Value("${plasmor.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${plasmor.accountNo}")
	private String accountNo;

	@Value("${plasmor.vatNo}")
	private String vatNo;

	@Value("${plasmor.phone}")
	private String phone;

	@Value("${plasmor.netTotal}")
	private String netTotal;

	@Value("${plasmor.vatTotal}")
	private String vatTotal;

	@Value("${plasmor.grossTotalC1}")
	private String grossTotalC1;

	@Value("${plasmor.faxNo}")
	private String faxNo;

	@Value("${plasmor.invoiceTableA2}")
	private String invoiceTableA2;

	@Value("${plasmor.referNo}")
	private String referNo;

	@Value("${plasmor.customerNo}")
	private String customerNo;

	@Value("${plasmor.orderNo}")
	private String orderNo;

	@Value("${plasmor.dueDte}")
	private String dueDte;

	@Value("${plasmor.invoiceAddress}")
	private String invoiceAddress;

	@Value("${plasmor.componyNo}")
	private String componyNo;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

//			invoice.setSortName(Constants.SWP);
//			invoice.setInvoiceTitle(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			try {
				// ============ INvoice =====================
				String[] invoiceA1 = pdfStr.split(invoiceNo);
				String invoiceA2 = invoiceA1[1].trim();
				String invoiceA3[] = invoiceA2.split("[\\D]{2}[a-zA-Z]");
				String invoice1 = invoiceA3[0].trim();
				invoice.setInvoiceNumber(invoice1.trim());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);
			/*
			 * customerNo
			 */
			String cNo[] = pdfStr.split(customerNo);
			String costumerNo[] = cNo[1].trim().split("\\s+");
//						invoice.setc
			/*
			 * referNo
			 */
			String rNo[] = pdfStr.split(referNo);
			String refNo[] = rNo[1].trim().split("       ");
			invoice.setReferenceNumber(refNo[0].trim());
			/*
			 * orderNo
			 */
			String oNo[] = pdfStr.split(orderNo);
			String orderNo[] = oNo[1].trim().split("         ");
			invoice.setOrderNo(orderNo[0].trim());
			/*
			 * customerNo
			 */
			String deleveryNoteA1[] = pdfStr.split(dueDte);
			String deleveryNote[] = deleveryNoteA1[1].trim().split("\\s+");
			invoice.setDeliveryDate(deleveryNote[0].trim());
    
			// ===============VAT No Address ================
			try {
				String[] vatNoA1 = pdfStr.split(vatNo);
				String vatNoA2 = vatNoA1[1].trim();
				String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
				String vatNo1 = vatNoA3[0].trim();
				invoice.setVatNo(vatNo1);
			} catch (Exception e) {
				// TODO: handle exception
			} 

			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split("[a-zA-z]");
			String phone1 = telephoneA3[0].trim();
			invoice.setTelephone(phone1);
			/*
			 * Fax
			 */
			String faxA1[] = pdfStr.split(faxNo);
			String faxA2[] = faxA1[1].split("[a-zA-z]");
			invoice.setFax(faxA2[0].trim());
			/*
			 * SupplierAddress
			 */
			invoice.setSupplierAddress(accountNo);
			invoice.setSupplierName(suplierAddress);
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			try {
				// =============== Carriage ================
				String[] carriageA1 = pdfStr.split(vatTotal);
				String carriageA2 = carriageA1[1].trim();
				String carriageA3[] = carriageA2.split("[a-zA-Z]");
				String carriageA4 = carriageA3[0];
				String carriage = carriageA4.trim();
				invoice.setVatTotal(carriage);
			} catch (Exception e) {
				// TODO: handle exception
			}
			// ===============NetInvoiceTotal ================
			String[] invoiceTotalA1 = pdfStr.split(grossTotalC1);
			String invoiceTotalA2 = invoiceTotalA1[1].trim();
			String invoiceTotalA3[] = invoiceTotalA2.split("[a-zA-Z]");
			String invoiceTotalA4 = invoiceTotalA3[0].trim();
			invoice.setGrossTotal(invoiceTotalA4);

			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(invoiceAddress);
			String invoiceAddressA3[] = invoiceAddressA1[1].split(delevaryAddr);
			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryaddr = "";
			try {
				for (int i = 0; i < invoiceAddressA4.length - 1; i++) {
					String ss[] = invoiceAddressA4[i].split("                                       ");
					invoiceAddressSum += " " + ss[0].trim();
					deleveryaddr += " " + ss[1].trim();
				}
			} catch (Exception e) {
				// TODO: handle exception
			} 
			// ===============Invoice Address ================
			String cName[] = deleveryaddr.trim().split("\\n");
			invoice.setInvoiceAddress(invoiceAddressSum.trim());
			invoice.setCustomerAddress(deleveryaddr);
			invoice.setCustomerName(cName[0].trim());
			invoice.setDeliveryAddress(deleveryaddr);

			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				String itemCode = "",amount="", quantity = "", price = "", unit = "",   discount = "",description="";

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					String invoiceTableA21[] = invoiceTableA1[0].trim().split("\\s+");
					itemCode = invoiceTableA21[0];
					String des[] = invoiceTableA1[0].split(itemCode);
					description = des[1].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					unit = untPrice[1].replaceAll(invoiceTableA2, "$2");
					price = untPrice[1].replaceAll(invoiceTableA2, "$3"); 
					discount = untPrice[1].replaceAll(invoiceTableA2, "$4"); 
					amount = untPrice[1].replaceAll(invoiceTableA2, "$5"); 
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				} 
				invoicetab.setItemNo(itemCode);
				invoicetab.setQuantity(quantity);
				invoicetab.setUnit(unit);
				invoicetab.setPrice(price); 
				invoicetab.setNetAmount(amount);
				invoicetab.setDiscount(discount);
				invoicetab.setDescription(description);
				invoiceTable.add(invoicetab); 
			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage());
		}

			return invoice;
	} 

	  

}
