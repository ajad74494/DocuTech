
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
 * @since 2020-08-9
 *
 */
@Service
public class SWPService implements PdfInvoiceProcessor { 
	private static Logger log = LoggerFactory.getLogger(SWPService.class);
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

	@Value("${swp.invoiceNo}")
	private String invoiceNo; 

	@Value("${swp.invoiceDte}")
	private String invoiceDte; 

	@Value("${swp.customerNo}")
	private String customerNo; 

	@Value("${swp.referNo}")
	private String referNo; 

	@Value("${swp.delevaryAddr}")
	private String delevaryAddr; 

	@Value("${swp.invoiceTableA1}")
	private String invoiceTableA1; 

	@Value("${swp.accountNo}")
	private String accountNo; 

	@Value("${swp.vatNo}")
	private String vatNo; 

	@Value("${swp.phone}")
	private String phone; 

	@Value("${swp.netTotal}")
	private String netTotal; 

	@Value("${swp.vatTotal}")
	private String vatTotal; 

	@Value("${swp.grossTotalC1}")
	private String grossTotalC1; 

	@Value("${swp.delevaryAddress}")
	private String delevaryAddress; 

	@Value("${swp.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${swp.delevaryAddr1}")
	private String delevaryAddr1; 

	@Value("${swp.gross}")
	private String gross;

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) {
		 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();

		try {
//			invoice.setSortName(Constants.SWP);
//			invoice.setInvoiceTitle(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);

			// ============ INvoice =====================
			String[] invoiceA1 = pdfStr.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("\\n");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1.trim());
			// ============ INvoice Date =====================
			String[] invoiceDateA1 = pdfStr.split(invoiceDte);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("\\n");
			String invoiceDate1 = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate1);
			/*
			 * customerNo
			 */
			String cNo[] = pdfStr.split(customerNo);
			String costumerNo[] = cNo[1].trim().split("\\n");
			invoice.setDeliveryNoteNo(costumerNo[0].trim());
			/*
			 * referNo
			 */
			String rNo[] = pdfStr.split(referNo);
			String refNo[] = rNo[1].trim().split("\\n");
			invoice.setOrderNo(refNo[0].trim());

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
			 * SupplierAddress
			 */
			String suplierAddr[] = pdfStr.split(accountNo1);
			String suplierAddrA1[] = suplierAddr[1].trim().split(phone);
			String supAdd = suplierAddrA1[0].trim();
			invoice.setSupplierAddress(supAdd);
			invoice.setSupplierName(supAdd);
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
			System.out.println(invoiceTotalA4);
			// ===============Invoice Address ================
			String[] invoiceAddressA1 = pdfStr.split(delevaryAddr);
			String invoiceAddressA2 = invoiceAddressA1[1].replaceAll(delevaryAddr1, "").trim();
			String invoiceAddressA3[] = invoiceAddressA2.split(delevaryAddress);

			String invoiceAddressA4[] = invoiceAddressA3[0].split("\\n");
			String invoiceAddressSum = "", deleveryAddressSum = "";
			for (int i = 0; i < invoiceAddressA4.length; i++) {
				try {
					String sepretAddr[] = invoiceAddressA4[i].split("                                   ");
					invoiceAddressSum = invoiceAddressSum + sepretAddr[0];
					deleveryAddressSum += sepretAddr[1];
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			String cName[] = invoiceAddressSum.split("         ");
			invoice.setInvoiceAddress(invoiceAddressSum);
			invoice.setCustomerAddress(deleveryAddressSum);
			invoice.setCustomerName(cName[0]);
			invoice.setDeliveryAddress(deleveryAddressSum);

			/*
			 * Table Data
			 */
			String[] quentityA1 = pdfStr.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(gross);
			String invTBLEa1 = quentityA3[0].replaceAll("[(]*", "").replaceAll("[)]*", "").trim();
			String quentityA4[] = invTBLEa1.trim().split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				String amount="", quantity = "", price = "", unit = "",   discount = "",description="",vat="";

				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(invoiceTableA2);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].trim().split(invoiceTableA2); 
					description = invoiceTableA1[0].trim();
					String untPrice[] = quentityA4[i].replaceAll("[(]*", "").replaceAll("[)]*", "").split(description);
 
					quantity = untPrice[1].replaceAll(invoiceTableA2, "$1");
					unit = untPrice[1].replaceAll(invoiceTableA2, "$2");
					price = untPrice[1].replaceAll(invoiceTableA2, "$3"); 
					vat = untPrice[1].replaceAll(invoiceTableA2, "$4"); 
					discount = untPrice[1].replaceAll(invoiceTableA2, "$5"); 
					amount = untPrice[1].replaceAll(invoiceTableA2, "$6"); 
				}
				if (!pattern.matcher(quentityA4[i].trim()).find()) {
					description += quentityA4[i].trim();
				}  
				invoicetab.setQuantity(quantity);
				invoicetab.setUnit(unit);
				invoicetab.setPrice(price); 
				invoicetab.setVatPercentage(vat);
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

//	Product\\s*Qty\\s*Unit\\s*Price\\s*VAT\\s*Disc %\\s*Value     
 

}
