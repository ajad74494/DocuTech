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
 * @since 2020-07-19
 *
 */
@Service
public class HarrisonService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HarrisonService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${harrison.supplierName}")
	private String supplierName;

	@Value("${harrison.invoiceNo}")
	private String invoiceNo;

	@Value("${harrison.invoiceDte}")
	private String invoiceDte;

	@Value("${harrison.invoiceDte1}")
	private String invoiceDte1;

	@Value("${harrison.dueDte}")
	private String dueDte;

	@Value("${harrison.referNo}")
	private String referNo;

	@Value("${harrison.delevaryAddress}")
	private String delevaryAddress;

	@Value("${harrison.delevaryAddr}")
	private String delevaryAddr;

	@Value("${harrison.componyNo}")
	private String componyNo;   

	@Value("${harrison.accountNo}")
	private String accountNo;   

	@Value("${harrison.vatNo}")
	private String vatNo;   

	@Value("${harrison.netTotal}")
	private String netTotal;   

	@Value("${harrison.vatTotal}")
	private String vatTotal;   
	
	@Value("${harrison.vatTotal}")
	private String onDate;   

	@Value("${harrison.invoiceDes}")
	private String invoiceDes;   

	@Value("${harrison.delevaryAddr1}")
	private String delevaryAddr1;
	@Value("${harrison.gross}")
	private String gross;
	@Value("${harrison.invoiceTableA1}")
	private String invoiceTableA1;     


	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
		Invoice invoice = new Invoice();
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
 
		try {
			String json = pdfStr;
//			
//			invoice.setSortName(Constants.h);
//			invoice.setInvoiceTitel(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);

			/*
			 * 
			 * INvoice
			 * 
			 */
			String[] invoiceA1 = json.split("Invoice\\#");
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split(supplierName);
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);

			String suplierAddredd[] = json.split("Bill  To:");
			String suplierName[] = suplierAddredd[0].replaceAll(invoiceDes + invoiceNo + invoice1, "")
					.replaceAll("Balance Due", "").replaceAll("£\\d*.\\d*", "").trim().split("\\n");
			String suplierAddress = suplierAddredd[0].replaceAll(invoiceDes + invoiceNo + invoice1, "")
					.replaceAll("Balance Due", "").replaceAll("£\\d*.\\d*", "").trim();

			/*
			 * 
			 * SupplierAddress
			 * 
			 */
			invoice.setSupplierAddress(suplierAddress);
			/*
			 * 
			 * SupplierName
			 * 
			 */
			invoice.setSupplierName(suplierName[0].trim());

			/*
			 * 
			 * INvoice Date
			 * 
			 */
			String[] invoiceDateA1 = json.split(invoiceDte1);
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			/*
			 * 
			 * Due Date
			 * 
			 */
			String[] dueDateA1 = json.split(invoiceDte);
			String dueDateA2 = dueDateA1[1].trim();
			String dueDateA3[] = dueDateA2.split("[a-zA-Z]");
			String dueDate = dueDateA3[0].trim();

			/*
			 * 
			 * ReferNo
			 * 
			 */
			String rferNoA1[] = json.split(referNo);
			String rferNoA2[] = rferNoA1[1].trim().split("\\n");
			String terms = rferNoA2[0];

			/*
			 * Delevery Address
			 */
			String deleveryAdd[] = json.split(delevaryAddress);
			String deleveryAdd1[] = deleveryAdd[1].replaceAll(invoiceDte + invoiceDate, "")
					.replaceAll("Terms   :\\s*" + terms, "").replaceAll(dueDte + dueDate, "").trim().split("#");
			String custmerName[] = deleveryAdd1[0].trim().split("\\n");
			invoice.setCustomerName(custmerName[0].trim());
			invoice.setCustomerAddress(deleveryAdd1[0].trim());
			invoice.setDeliveryAddress(deleveryAdd1[0].trim());
			/**
			 * 
			 * Account No
			 * 
			 **/
			String[] accountA1 = json.split(accountNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);

			/*
			 * 
			 * VAT No
			 * 
			 */
			String[] regA1 = json.split(vatNo);
			String regA2 = regA1[1].trim();
			String regA3[] = regA2.split("[a-zA-Z]");
			String vatReg = regA3[0].trim();
			invoice.setDeliveryDate(vatReg);
			/*
			 * 
			 * Ref No
			 * 
			 */
			String[] comanyregA1 = json.split(componyNo);
			String comanyregA2 = comanyregA1[1].trim();
			String comanyregA3[] = comanyregA2.split("[a-zA-Z]");
			String comanyreg = comanyregA3[0].trim();
			/*
			 * 
			 * Net Total
			 * 
			 */
			String[] netTotalA1 = json.split(netTotal);
			String netTotalA2 = netTotalA1[1].trim();
			String netTotalA3[] = netTotalA2.split("[a-zA-Z]");
			String netTotal = netTotalA3[0].trim();
			invoice.setNetTotal(netTotal);
			/*
			 * 
			 * Vat Total
			 * 
			 */
			String[] vatTotalA1 = json.split(vatTotal);
			String vatTotalA2 = vatTotalA1[1].trim();
			String vatTotalA3[] = vatTotalA2.split("[a-zA-Z]");
			String out = vatTotalA3[0].trim();
			String vatTotal = out.replaceAll("\\d*\\%", "").replaceAll("[()]", "").trim();
			invoice.setVatTotal(vatTotal);
			/*
			 * 
			 * Total Address
			 * 
			 */
			String[] totalA1 = json.split(gross);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			/*
			 * Deu Total
			 */
			String deuTotalA1[] = json.split(onDate);
			String deuTotalA2[] = deuTotalA1[1].split("[a-zA-Z]");
			String dueTotal = "£" + deuTotalA2[0].trim();
			/*
			 * 
			 * Quality Address
			 * 
			 */
			String[] quentityA1 = json.split(invoiceTableA1);
			String quentityA2 = quentityA1[1].trim();
			String quentityA3[] = quentityA2.split(delevaryAddr);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(delevaryAddr1);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String[] des = quentityA4[i].split(delevaryAddr1);
					String[] desA2 = des[0].trim().split("\\s*");
					/*
					 * Description
					 */
					invoicetab.setDescription(des[0].replaceAll(desA2[0], "").trim());
					String[] qtyA1 = quentityA4[i].split(des[0]);
					/*
					 * Qty
					 */
					String qty = qtyA1[1].replaceAll(delevaryAddr1, "$1");
					invoicetab.setQuantity(qty.trim());
					/*
					 * Rate
					 */
					String rate = qtyA1[1].replaceAll(delevaryAddr1, "$2");
					/*
					 * Amount
					 */
					String amount = qtyA1[1].replaceAll(delevaryAddr1, "$3");
					invoicetab.setTotal(amount.trim());
					invoiceTable.add(invoicetab);

				}
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
return invoice;
	}

}
