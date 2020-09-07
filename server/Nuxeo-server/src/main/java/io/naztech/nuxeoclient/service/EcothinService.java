
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
 * @since 2020-08-8
 *
 */
@Service
public class EcothinService implements PdfInvoiceProcessor {
	 

	private static Logger log = LoggerFactory.getLogger(EcothinService.class);
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

	@Value("${echothin.invoiceNo}")
	private String invoiceNo;

	@Value("${echothin.invoiceDte}")
	private String invoiceDte;

	@Value("${echothin.web}")
	private String web;

	@Value("${echothin.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${echothin.delevaryAddr}")
	private String delevaryAddr;

	@Value("${echothin.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${echothin.accountNo}")
	private String accountNo;

	@Value("${echothin.vatNo}")
	private String vatNo;

	@Value("${echothin.phone}")
	private String phone;

	@Value("${echothin.netTotal}")
	private String netTotal;

	@Value("${echothin.vatTotal}")
	private String vatTotal; 

	@Value("${echothin.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${echothin.faxNo}")
	private String faxNo; 

	@Value("${echothin.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${echothin.referNo}")
	private String referNo; 

	@Value("${echothin.customerNo}")
	private String customerNo; 

	@Value("${echothin.orderNo}")
	private String orderNo; 

	@Value("${echothin.dueDte}")
	private String dueDte; 

	@Value("${echothin.invoiceAddress}")
	private String invoiceAddress; 

	@Value("${echothin.componyNo}")
	private String componyNo;      
	 


	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {

		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {

			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\s+");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
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
			String refNo[] = rNo[1].trim().split("\\s+");
			invoice.setReferenceNumber(refNo[0].trim());
			/*
			 * orderNo
			 */
			String oNo[] = pdfStr.split(orderNo);
			String orderNo[] = oNo[1].trim().split("\\s+");
			invoice.setOrderNo(orderNo[0].trim());
			/*
			 * customerNo
			 */
			String deleveryNoteA1[] = pdfStr.split(dueDte);
			String deleveryNote[] = deleveryNoteA1[1].trim().split("\\s+");
			invoice.setDeliveryNoteNo(deleveryNote[0].trim());

			// ===============Account No ================
			String[] accountA1 = pdfStr.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo1 = accountA3[0].trim();
			invoice.setAccountNo(accountNo1);
			// ===============VAT No Address ================
			String[] vatNoA1 = pdfStr.split(vatNo);
			String vatNoA2 = vatNoA1[1].trim();
			String vatNoA3[] = vatNoA2.split("[\\D]{2}[a-zA-Z]");
			String vatNo1 = vatNoA3[0].trim();
			invoice.setVatNo(vatNo1);
			/*
			 * Company No
			 */
			String companyNo[] = pdfStr.split(componyNo);
			String companyNoA1[] = companyNo[1].trim().split("\\s+");
//			invoice.setcoum

			// ==============Deleveru date===========
			String telephoneA1 = pdfStr.trim();
			String telephoneA2[] = telephoneA1.split(phone);
			String telephoneA3[] = telephoneA2[1].split(vatNo);
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
			String sAdd[] = pdfStr.split(costumerNo[0].trim());
			String suplierAddr[] = sAdd[1].split(web);
			String suplierAddrA1[] = suplierAddr[0].split("\\n");
			String supplierAddSum = "";
			for (int i = 0; i < suplierAddrA1.length; i++) {
				String ss[] = suplierAddrA1[i].split("                                     ");
				supplierAddSum += " " + ss[1].trim();
			}
			invoice.setSupplierAddress(supplierAddSum.trim());
			invoice.setSupplierName(suplierAddrA1[1]);
			// ===============Net Total Address ================
			String[] netTotalA1 = pdfStr.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotalA4 = netTotalA3[0].trim();
			invoice.setNetTotal(netTotalA4);
			// =============== Carriage ================
			String[] carriageA1 = pdfStr.split(vatTotal);
			String carriageA2 = carriageA1[1].trim();
			String carriageA3[] = carriageA2.split("[a-zA-Z]");
			String carriageA4 = carriageA3[0];
			String carriage = carriageA4.trim();
			invoice.setVatTotal(carriage);
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
			String invoiceAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length; i++) {
				String ss[] = invoiceAddressA4[i].split("                                                 ");
				invoiceAddressSum += " " + ss[0].trim();
			}
			// ===============Invoice Address ================
			String[] deleveryAddressA1 = pdfStr.split(delevaryAddr);
			String deleveryAddress[] = deleveryAddressA1[1].split(delevaryAddr1);
			String cName[] = deleveryAddress[0].trim().split("\\n");
			System.out.println(invoiceAddressSum.trim());
			invoice.setInvoiceAddress(invoiceAddressSum.trim());
			invoice.setCustomerAddress(deleveryAddress[0].trim());
			invoice.setCustomerName(cName[0].trim());
			invoice.setDeliveryAddress(deleveryAddress[0].trim());

			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(netTotal);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				String quantity = "", unitePrice = "", nettotal = "", description = "";
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2);
					description = invoiceTableA1[0].trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);

					quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					unitePrice = untPrice[1].replaceAll(invoiceTableA2, "$3");
					nettotal = untPrice[1].replaceAll(invoiceTableA2, "$4");
				}
				invoicetab.setUnitPrice(unitePrice.trim());
				invoicetab.setQuantity(quantity.trim());
				invoicetab.setTotalNetPrice(nettotal.trim());
				invoicetab.setDescription(description.trim());
				invoiceTable.add(invoicetab);

			}
			invoice.setInvoiceTable(invoiceTable);
		} catch (Exception e) {
			log.info(e.getMessage());
		}

	
		return invoice;
	}

}
