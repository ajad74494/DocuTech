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

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.InvoiceTable;

/**
 * 
 * @author abul.kalam
 * @since 2020-07-19
 *
 */
@Service
public class ApcService implements PdfInvoiceProcessor {
	private static Logger log = LoggerFactory.getLogger(HarrisonService.class);
	@Autowired
	NuxeoClientService nuxeoClientService;

	private Invoice invoice = new Invoice();

	@Value("${import.pdfType}")
	private String pdfType;

	@Value("${apc.costumerNo}")
	private String costumerNo; 

	@Value("${apc.phone}")
	private String phone;

	@Value("${apc.invoiceTableA2}")
	private String invoiceTableA2; 

	@Value("${apc.invoiceNo}")
	private String invoiceNo; 

	@Value("${apc.companyNo}")
	private String companyNo; 

	@Value("${apc.invoiceDte}")
	private String invoiceDte; 

	@Value("${apc.invoiceAddress}")
	private String invoiceAddress; 

	@Value("${apc.delevaryAddress}")
	private String delevaryAddress; 

	@Value("${apc.delevaryAddr}")
	private String delevaryAddr; 

	@Value("${apc.vatNo}")
	private String vatNo; 

	@Value("${apc.netTotal}")
	private String netTotal; 

	@Value("${apc.netTotalC1}")
	private String netTotalC1;

	@Value("${apc.vatTotal}")
	private String vatTotal;

	@Value("${apc.grossTotalC1}")
	private String grossTotalC1;

	@Value("${apc.delevaryAddr1}")
	private String delevaryAddr1;

	@Value("${apc.delevaryAddr2}")
	private String delevaryAddr2;

	@Value("${apc.delevaryAddr3}")
	private String delevaryAddr3;

	@Value("${apc.invoiceTableA1}")
	private String invoiceTableA1;

	@Value("${apc.invoiceTableA2}")
	private String netToStringtalC1; 

	@Override
	public Invoice processPdfInvoice(String pdfStr, File pdfInvoice) { 
		List<InvoiceTable> invoiceTable = new ArrayList<InvoiceTable>();
 
		try {
			invoice.setSortName(Constants.APC);
//			invoice.setInvoiceTitel(nuxeoinvoiceName);
//			invoice.setInvoiceDescription(desc);
//			invoice.setPrefix(prefix);
//			invoice.setInvoiceType(nuxeoinvoiceType);
			String json = pdfStr;
			String suplierAddredd[] = json.split(phone);
			String suplierName[] = suplierAddredd[0].trim().split("\\n");
			String suplierAddress = suplierAddredd[0].trim();
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
			 * INvoice
			 * 
			 */
			String[] invoiceA1 = json.split(invoiceNo);
			String invoiceA2 = invoiceA1[1].trim();
			String invoiceA3[] = invoiceA2.split("[a-zA-Z]");
			String invoice1 = invoiceA3[0].trim();
			invoice.setInvoiceNumber(invoice1);

			/*
			 * 
			 * INvoice Date
			 * 
			 */
			String[] invoiceDateA1 = json.split(invoiceDte );
			String invoiceDateA2 = invoiceDateA1[1].trim();
			String invoiceDateA3[] = invoiceDateA2.split("[a-zA-Z]");
			String invoiceDate = invoiceDateA3[0].trim();
			invoice.setInvoiceDate(invoiceDate);
			/*
			 * 
			 * ReferNo
			 * 
			 */
			String rferNoA1[] = json.split(costumerNo);
			String rferNoA2[] = rferNoA1[1].trim().split("[a-zA-Z]");
			invoice.setReferenceNumber(rferNoA2[0].trim());
			/**
			 * 
			 * Account No
			 * 
			 **/
			String[] accountA1 = json.split(vatNo);
			String accountA2 = accountA1[1].trim();
			String accountA3[] = accountA2.split("[a-zA-Z]");
			String accountNo = accountA3[0].trim();
			invoice.setAccountNo(accountNo);
			/*
			 * 
			 * Invoice Address
			 * 
			 */
			String[] invoiceAddressA1 = json.split(vatNo);
			String invoiceAddressA2 = invoiceAddressA1[1].trim().replaceAll(accountNo, "")
					.replaceAll("Invoice\\s*" + invoice1, "").replaceAll(costumerNo+"\\s*" + rferNoA2[0].trim(), "")
					.replaceAll("Invoice Date:\\s*" + invoiceDate, "");
			String invoiceAddressA3[] = invoiceAddressA2.replaceAll("\\w*\\s*\\w*\\:\\s*\\w*\\s*\\d*", "")
					.split("Overnight      Parcels");
			String consomerAddress[] = invoiceAddressA3[0].trim().split("\\n");
			invoice.setCustomerAddress(invoiceAddressA3[0]);
			invoice.setCustomerName(consomerAddress[0].trim());
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
			String[] comanyregA1 = json.split(companyNo);
			String comanyregA2 = comanyregA1[1].trim();
			String comanyregA3[] = comanyregA2.split("[a-zA-Z]");
			String comanyreg = comanyregA3[0].trim();
			/*
			 * 
			 * Telephone
			 * 
			 */
			String phoneA1[] = json.split(phone);
			String phoneA2[] = phoneA1[1].split("[a-zA-Z]");
			invoice.setTelephone(phoneA2[0].trim());
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
			String vatTotal = out;
			invoice.setVatTotal(vatTotal);

			/*
			 * 
			 * Total Address
			 * 
			 */
			String[] totalA1 = json.split(grossTotalC1);
			String totalA2 = totalA1[1].trim();
			String totalA3[] = totalA2.split("[a-zA-Z]");
			String out1 = totalA3[0].trim();
			String grossTotal = out1;
			invoice.setGrossTotal(grossTotal);
			/*
			 * 
			 * Quality Address
			 * 
			 */
			String[] quentityA1 = json.split(netTotalC1); 
			String quentityA2 = quentityA1[1].trim().replaceAll(invoiceTableA1 + invoice1 + "\\s*", "")
					.replaceAll(invoiceAddress, "").replaceAll(delevaryAddr+ phoneA2[0].trim() + "\\s*", "")
					.replaceAll(suplierAddress + "\\s*", "")
					.replaceAll(delevaryAddr1 + comanyreg + "\\s*", "")
					.replaceAll(delevaryAddr2+ vatReg + "\\s*", "");
			String quentityA3[] = quentityA2.split(invoiceTableA2);
			String invTBLEa1 = quentityA3[0].trim();
			String quentityA4[] = invTBLEa1.split("\\n");
			for (int i = 0; i < quentityA4.length; i++) {
				InvoiceTable invoicetab = new InvoiceTable();
				Pattern pattern = Pattern.compile(delevaryAddress);
				if (pattern.matcher(quentityA4[i].trim()).find()) {
					String invoiceTableA1[] = quentityA4[i].split(delevaryAddress);
					String invoiceTableA2[] = invoiceTableA1[0].split("          ");
					/*
					 * SenderRefer
					 */
					String senderRef = invoiceTableA2[1];
					invoicetab.setBarCode(senderRef);
					String invTabA1[] = invoiceTableA2[0].trim().split("\\s+");
					/*
					 * Job No
					 */
					String jobNo = invTabA1[1].trim();
					invoicetab.setStockCode(jobNo);
					/*
					 * Date
					 */
					String date = invTabA1[0];
					invoicetab.setDate(date);
					String desc[] = invoiceTableA1[0].split(jobNo);
					/*
					 * Description
					 */
					String descriptionA1 = desc[1].replaceAll(senderRef, "").trim();
					invoicetab.setDescription(descriptionA1);
					String invTabA2[] = quentityA4[i].split(descriptionA1);
					/*
					 * service
					 */
					String service = invTabA2[1].replaceAll(delevaryAddr3, "$1")
							.trim();
					invoicetab.setProductCode(service);
					/*
					 * packs
					 */
					String packs = invTabA2[1].replaceAll(delevaryAddr3, "$2")
							.trim();
					invoicetab.setItemNo(packs);
					/*
					 * Weight
					 */
					String weight = invTabA2[1].replaceAll(delevaryAddr3, "$3")
							.trim();
					invoicetab.setWeight(weight);
					/*
					 * Net Charge
					 */
					String netCharge = invTabA2[1]
							.replaceAll(delevaryAddr3, "$4").trim();
					invoicetab.setNetAmount(netCharge);
					invoiceTable.add(invoicetab);
					System.out.println("hi");
				}
			}
			invoice.setInvoiceTable(invoiceTable);

		} catch (Exception e) {
			log.info(e.getMessage() + " HI I am a kalam");
		}
return invoice;
	}

}
