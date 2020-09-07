package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.nuxeo.client.NuxeoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.model.DocCount;
import io.naztech.nuxeoclient.model.DocDetails;
import io.naztech.nuxeoclient.model.Document;
import io.naztech.nuxeoclient.model.DocumentWrapper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.model.Template;
import io.naztech.nuxeoclient.utility.DirectoryCreator;

@Service
public class InvoiceDataProcessService {
	private static Logger log = LoggerFactory.getLogger(InvoiceDataProcessService.class);

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	@Value("${folder.name.onsite}")
	private String folderName;

	@Value("${import.nuxeo.RepoPath}")
	private String repoPath;

	@Value("${import.falcon.type}")
	private String falconType;

	@Value("${import.hoppings.type")
	private String hoppingType;

	@Value("${import.htshandlingtruck.type")
	private String handlingype;

	@Value("${import.national.type")
	private String nationalType;

	@Value("${import.astroflame.type}")
	private String astroFlameType;

	@Value("${import.pirtek.type}")
	private String pirtekType;

	@Value("${import.bywaters.type}")
	private String byWaterType;

	@Value("${import.onsite.type}")
	private String onSiteType;

	@Value("${import.vr.type}")
	private String vrType;

	@Value("${import.topservice.type}")
	private String topServicType;

	@Value("${import.htshandlingtruck.type}")
	private String htsType;

	@Value("${import.mighton.type")
	private String mightonType;

	@Value("${import.valleySaw.type}")
	private String valleyType;

	@Value("${import.intouchwithbricks.type}")
	private String inTouchType;

	@Value("${import.bristan.type}")
	private String bristanType;

	@Value("${import.ox.type}")
	private String oxType;

	@Value("${import.rolathene.type}")
	private String rolatheneType;

	@Value("${import.trend.type}")
	private String trendType;

	@Value("${import.aps.type}")
	private String apsType;

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

	/**
	 * Converts Invoice to DocumentRequest
	 * 
	 * @param inv
	 * @return DocumentWrapper object
	 */

	private DocumentWrapper convertInvoiceToDocumentReq(Invoice inv) {

		if (inv == null)
			return null;

		try {
			DocumentWrapper ob = DocumentWrapper.createWithName(inv.getInvoiceTitle(), inv.getInvoiceType());

			// Setting attributes of the document wrapper object
			ob.setTitle(inv.getInvoiceTitle());
			ob.setDescription(inv.getInvoiceDescription());
			ob.setPrefix(inv.getPrefix());
			ob.setRepoPath(repoPath);

//			if (inv.getCarriageNet() != null) {
//				ob.addAttribute("carriage_net", inv.getCarriageNet());
//
//			}
//			if (inv.getAccountNo() != null) {
//				ob.addAttribute("account_number", "inv.getAccountNo()");
//			}
//			if (inv.getCustomerAddress() != null) {
//				if (inv.getInvoiceType() == bristanType || inv.getInvoiceType() == oxType) {
//					ob.addAttribute("deliver_to", inv.getCustomerAddress());
//				} else {
//					ob.addAttribute("customer_address", inv.getCustomerAddress());
//				}
//			}
//			if (inv.getCustomerName() != null) {
//				if (inv.getInvoiceType() == bristanType || inv.getInvoiceType() == oxType
//						|| inv.getInvoiceType() == apsType) {
//					ob.addAttribute("client_name", inv.getCustomerName());
//				} else {
//					ob.addAttribute("customer_name", inv.getCustomerName());
//				}
//			}
//
//			if (inv.getDeliveryAddress() != null) {
//				ob.addAttribute("delivery_address", inv.getDeliveryAddress());
//			}
//			if (inv.getDeliveryDate() != null) {
//
//				ob.addAttribute("delivery_date", inv.getDeliveryDate());
//			}
//			if (inv.getDeliveryNoteNo() != null) {
//				if (inv.getInvoiceType() == onSiteType) {
//					ob.addAttribute("delivery_no", inv.getDeliveryNoteNo());
//				} else {
//					ob.addAttribute("delivery_note", inv.getDeliveryNoteNo());
//				}
//			}
//
//			if (inv.getDespatchDate() != null) {
//				ob.addAttribute("despatch_date", inv.getDespatchDate());
//			}
//
//			if (inv.getEmail() != null) {
//				ob.addAttribute("email", inv.getEmail());
//			}
//			if (inv.getInvoiceDate() != null) {
//				ob.addAttribute("invoice_date", inv.getInvoiceDate());
//			}
//			if (inv.getInvoiceNumber() != null) {
//				ob.addAttribute("invoice_number", inv.getInvoiceNumber());
//			}
//			if (inv.getGrossTotal() != null) {
//				if (inv.getInvoiceType() == pirtekType || inv.getInvoiceType() == byWaterType
//						|| inv.getInvoiceType() == topServicType) {
//
//					ob.addAttribute("nat_invoice_total", inv.getGrossTotal());
//				} else if (inv.getInvoiceType() == hoppingType || inv.getInvoiceType() == handlingype
//						|| inv.getInvoiceType() == nationalType || inv.getInvoiceType() == onSiteType
//						|| inv.getInvoiceType() == vrType || inv.getInvoiceType() == bristanType
//						|| inv.getInvoiceType() == oxType || inv.getInvoiceType() == rolatheneType
//						|| inv.getInvoiceType() == trendType || inv.getInvoiceType() == apsType) {
//					ob.addAttribute("net_invoice_total", inv.getGrossTotal());
//				} else if (inv.getInvoiceType() == valleyType) {
//					ob.addAttribute("net_amount", inv.getGrossTotal());
//				} else if (inv.getInvoiceType() == mightonType) {
//					ob.addAttribute("net_price", inv.getGrossTotal());
//				} else {
//					ob.addAttribute("net_total", inv.getGrossTotal());
//				}
//
//			}
//			if (inv.getSupplierAddress() != null) {
//				if (inv.getInvoiceType() == bristanType || inv.getInvoiceType() == oxType) {
//					ob.addAttribute("company_address", inv.getSupplierAddress());
//				} else {
//					ob.addAttribute("supplier_address", inv.getSupplierAddress());
//				}
//			}
//
//			if (inv.getReferenceNumber() != null) {
//				if (inv.getInvoiceType() == onSiteType) {
//					ob.addAttribute("reference_number", inv.getReferenceNumber());
//				} else if (inv.getInvoiceType() == rolatheneType) {
//					ob.addAttribute("referance", inv.getReferenceNumber());
//				} else if (inv.getInvoiceType() == mightonType) {
//					ob.addAttribute("regerence_number", inv.getReferenceNumber());
//				} else if (inv.getInvoiceType() == bristanType || inv.getInvoiceType() == oxType
//						|| inv.getInvoiceType() == apsType) {
//					ob.addAttribute("reference_no", inv.getReferenceNumber());
//				} else {
//					ob.addAttribute("customer_no", inv.getReferenceNumber());
//				}
//			}
//			if (inv.getSupplierName() != null) {
//				ob.addAttribute("supplier_name", inv.getSupplierName());
//			}
//			if (inv.getTelephone() != null) {
//				if (inv.getInvoiceType() == apsType) {
//					ob.addAttribute("supplier_number", inv.getTelephone());
//				} else {
//					ob.addAttribute("telephone_number", inv.getTelephone());
//				}
//			}
//			if (inv.getNetTotal() != null) {
//				if (inv.getInvoiceType() == hoppingType || inv.getInvoiceType() == bristanType
//						|| inv.getInvoiceType() == oxType || inv.getInvoiceType() == trendType) {
//					ob.addAttribute("value_of_goods", inv.getNetTotal());
//				} else if (inv.getNetTotal() == inTouchType) {
//					ob.addAttribute("total_taxable_value", inv.getNetTotal());
//				} else if (inv.getNetTotal() == apsType) {
//					ob.addAttribute("net_value", inv.getNetTotal());
//				} else {
//					ob.addAttribute("total_amount", inv.getNetTotal());
//				}
//			}
//
//			if (inv.getVatNo() != null) {
//				if (inv.getInvoiceType() == pirtekType || inv.getInvoiceType() == trendType) {
//					ob.addAttribute("vat_no", inv.getVatNo());
//				} else if (inv.getInvoiceType() == htsType || inv.getInvoiceType() == apsType) {
//					ob.addAttribute("vat_number", inv.getVatNo());
//				} else {
//					ob.addAttribute("vat_reg_no", inv.getVatNo());
//				}
//			}
//			if (inv.getFax() != null) {
//				ob.addAttribute("fax_number", inv.getFax());
//			}
//			if (inv.getRegNo() != null) {
//				if (inv.getInvoiceType() == topServicType || inv.getInvoiceType() == vrType
//						|| inv.getInvoiceType() == trendType) {
//					ob.addAttribute("registration_no", inv.getRegNo());
//				}
//				{
//					ob.addAttribute("registration_number", inv.getRegNo());
//				}
//			}
//			if (inv.getOrderNo() != null) {
//				if (inv.getInvoiceType() == handlingype || inv.getInvoiceType() == onSiteType
//						|| inv.getInvoiceType() == rolatheneType) {
//					ob.addAttribute("order_no", inv.getOrderNo());
//				} else if (inv.getInvoiceType() == mightonType) {
//					ob.addAttribute("order_disc", inv.getOrderNo());
//				} else if (inv.getInvoiceType() == apsType) {
//					ob.addAttribute("customer_order_no", inv.getOrderNo());
//				} else if (inv.getInvoiceType() == inTouchType) {
//					ob.addAttribute("cust_order_no", inv.getOrderNo());
//				} else if (inv.getInvoiceType() == bristanType || inv.getInvoiceType() == oxType) {
//					ob.addAttribute("order_id", inv.getOrderNo());
//				} else {
//					ob.addAttribute("order_number", inv.getOrderNo());
//				}
//			}
//			if (inv.getDeliveryTicketNo() != null) {
//				ob.addAttribute("delivery_ticket_no", inv.getDeliveryTicketNo());
//			}
//			if (inv.getSupplierName() != null) {
//				ob.addAttribute("supplier_name", inv.getSupplierName());
//			}
//			if (inv.getWebsite() != null) {
//				ob.addAttribute("website", inv.getWebsite());
//			}
//			if (inv.getOrderDate() != null) {
//				ob.addAttribute("order_date", inv.getOrderDate());
//			}
//			if (inv.getDueDate() != null) {
//				ob.addAttribute("due_date", inv.getDue());
//			}
//			if (inv.getDue() != null) {
//				ob.addAttribute("total_due", inv.getDue());
//			}
//			if (inv.getVat() != null) {
//				ob.addAttribute("vat", inv.getVat());
//			}
//			if (inv.getVatTotal() != null) {
//				if (inv.getInvoiceType() == apsType) {
//					ob.addAttribute("vat_number", inv.getVatTotal());
//				} else {
//					ob.addAttribute("total_tax_amount", inv.getVatTotal());
//				}
//			}
//			if (inv.getDiscount() != null) {
//				ob.addAttribute("discount", inv.getDiscount());
//			}
//			ob.addAttribute("product_number", inv.getReferenceNumber());

//			  ob.addAttribute("company_reg_number", "inv.getRegNo()");
			ob.addAttribute("customer_address", inv.getCustomerAddress());
			ob.addAttribute("customer_name", inv.getCustomerName());
			ob.addAttribute("delivery_address", inv.getDeliveryAddress());
			ob.addAttribute("delivery_note", inv.getDeliveryNoteNo());
			ob.addAttribute("despatch_date", inv.getDespatchDate());
			ob.addAttribute("invoice_date", inv.getInvoiceDate());
			ob.addAttribute("invoice_number", inv.getInvoiceNumber());
//			ob.addAttribute("email", inv.getEmail());
//			ob.addAttribute("fax_number", inv.getFax());
//			ob.addAttribute("net_invoice_total", inv.getNetTotal());
//			ob.addAttribute("reference_number", inv.getReferenceNumber());
//			ob.addAttribute("supplier_address", inv.getSupplierAddress());
//			ob.addAttribute("supplier_name", inv.getSupplierName());
//			ob.addAttribute("telephone_number", "inv.getTelephone()");
//			ob.addAttribute("total_amount", inv.getNetTotal());
//			ob.addAttribute("vat_reg_number", "inv.getVatReg()");
//			ob.addAttribute("vat_total", "inv.getVatTotal()"); 
////			
//
////////			ob.addAttribute("title", "onsite");
//			ob.addAttribute("customer_name", inv.getCustomerName());
//			ob.addAttribute("customer_address", inv.getCustomerAddress());
//			ob.addAttribute("supplier_name", inv.getSupplierName());
//			ob.addAttribute("supplier_address", inv.getSupplierAddress());
			ob.addAttribute("account_number", inv.getAccountNo());
			ob.addAttribute("account_code", inv.getAccountNo());
//			ob.addAttribute("invoice_number", inv.getInvoiceNumber());
//			ob.addAttribute("invoice_date", inv.getInvoiceDate());
//			ob.addAttribute("procduct_number", "inv.getDueDate()");
////////			ob.addAttribute("order_number", inv.getOrderNo());
////////			ob.addAttribute("order_date", inv.getOrderDate()); 
////////			ob.addAttribute("reference_number", inv.getReferenceNumber()); 
////////			ob.addAttribute("delivery_note", inv.getDeliveryNoteNo()); 
//			ob.addAttribute("total_amount", inv.getNetTotal()); 
//			ob.addAttribute("vat_total", inv.getVatTotal());
//			ob.addAttribute("vat_number", "inv.getVatNo()");// 
//			ob.addAttribute("net_invoice_total", inv.getGrossTotal()); 
//			ob.addAttribute("email", "email");//
//			ob.addAttribute("telephone_number", "inv.getTelephone()"); //
//			ob.addAttribute("total_due", "inv.getWebsite()"); 
//			ob.addAttribute("delivery_address", inv.getDeliveryAddress());
//			
log.info(ob.toString());
			return ob;
		} catch (Exception e) {
			log.error("Failed to convert to Document Wrapper Object", e);
			return null;
		}
	}

	/**
	 * Uploads the file to the Nuxeo server and moves the file from hotfolder to
	 * archive
	 * 
	 * @param invoiceInfoXml
	 * @param attachments
	 * @return request
	 * @throws IOException
	 */
	public void uploadDocument(Invoice invoice, File file, Boolean sendToNuxeoFlag) throws IOException {

		String archiveFolderPath = rootOut + Constants.FWD_SL + invoice.getInvoiceType() + Constants.FWD_SL
				+ Constants.Archive;
		String badfolderPath = rootOut + Constants.FWD_SL + folderName + Constants.FWD_SL + Constants.Bad;
		LocalDate date = LocalDate.now();
		String strDate = date.toString();

		DocumentWrapper documentWrapper = new DocumentWrapper();
		Template tmplt = new Template();
		DocCount docCount = new DocCount();
		Optional<Template> tmpListNew = Optional.empty();
		List<DocCount> docCountList = null;

		if (file.getName().contains(sourceType)) {
			tmplt.setDocSource(sourceType);
		}

		tmplt.setPdfType(pdfType);
		tmplt.setProcessorType(proType);
		tmplt.setDocType(docType);

		directoryCreator.outDirectorycreatorForSuppliers(invoice.getInvoiceType());

		if (sendToNuxeoFlag) {

			// Move files to Archive Folder
//			moveToArchive(archiveFolderPath, file, strDate);
			String archive = archiveFolderPath + File.separator + strDate;
			File arcDir = new File(archive);

			if (!arcDir.exists()) {
				arcDir.mkdir();
			}

			// if file not exit then copy from main file to current date file
			if (!new File(archive + File.separator + file.getName()).exists()) {
				Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(archive + File.separator + file.getName()),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}

		// Setup Nuxeo Connection
		nuxeoClientService.setNuxeoClient(nuxeoClient);

		try {

			docCount.setFileName(file.getName());
			docCount.setSearchByFile(Constants.YES);
			log.info(docCount.toString());
			docCountList = docCountService.processAction(ActionType.SELECT.toString(), docCount);

			if (docCountList.parallelStream().findFirst().isPresent()) {
				tmplt.setDocCountId(docCountList.get(0).getDocCountId());
			}

			if (((null == invoice.getInvoiceNumber() || invoice.getInvoiceNumber().isEmpty())
					|| (null == invoice.getInvoiceDate() || invoice.getInvoiceDate().isEmpty())
					|| (null == invoice.getNetTotal() || invoice.getGrossTotal().isEmpty()))) {

//				move to bad folder
//				moveToBadFolder(badfolderPath, file, strDate);
				String badFolderPath = badfolderPath + File.separator + strDate;
				File badDir = new File(badFolderPath);
				if (!badDir.exists())
					badDir.mkdir();

				// copy file main folder to bad folder
				if (!new File(badFolderPath + File.separator + file.getName()).exists())
					Files.move(Paths.get(file.getAbsolutePath()),
							Paths.get(badFolderPath + File.separator + file.getName()),
							StandardCopyOption.REPLACE_EXISTING);

				tmplt.setErrorIssue("Service class name is " + invoice.getClass().getName() + ", Invoice number is "
						+ invoice.getInvoiceNumber() + ", Invoice Date is " + invoice.getInvoiceDate()
						+ ", Net Invoice Total is " + invoice.getNetTotal());

				tmplt.setErrorIssueSource("Data Not available");
				tmplt.setIsSentToNuxeo(0);
				tmplt.setIsTemplateParsed(0);
				tmplt.setIsParsedSuccessful(0);

				templateService.processAction(ActionType.NEW.toString(), tmplt, Constants.TEMPLATE).stream()
						.findFirst();

//				templateService.processAction(ActionType.NEW.toString(), tmplt, Constants.EXCEPTION).stream()
//						.findFirst();

				log.info("Moved to Bad Folder", file.getName());
			} else {

				if (null != file) {

					tmplt.setClientName(invoice.getCustomerName());
					tmplt.setReceivedDocId(tmplt.getClientName() + "_" + invoice.getInvoiceNumber());
					tmplt.setIsTemplateParsed(1);
					tmplt.setIsParsedSuccessful(1);

					try {
						// Uploading the file to Nuxeo Server
						documentWrapper = convertInvoiceToDocumentReq(invoice);
						documentWrapper.setFile(file);
						tmplt.setSupplierName(documentWrapper.getTitle());

						if (nuxeoClientService.createDocument(documentWrapper).equals(null) && sendToNuxeoFlag) {
							saveNuxeoUploadUnsuccessful(file);
							log.error(file.getName() + " Failed to upload file on Nuxeo");
						} else {
							tmplt.setIsSentToNuxeo(1);
							log.info("Successfully Uploaded Document Into Nuxeo: " + file.getName() + " & "
									+ "Invoice No:" + invoice.getInvoiceNumber());

							tmpListNew = templateService
									.processAction(ActionType.NEW.toString(), tmplt, Constants.TEMPLATE).stream()
									.findFirst();

							if (tmpListNew.isPresent()) {
								insertDocuments(tmpListNew, invoice, file);
								Files.delete(Paths.get(file.getAbsolutePath()));

								Files.delete(Paths.get(file.getAbsolutePath()));
							}
						}
					} catch (Exception ex) {

						tmplt.setErrorIssue(ex.getLocalizedMessage());

						tmplt.setErrorIssueSource("Failed to Create object from xml");

						templateService.processAction(ActionType.NEW.toString(), tmplt, Constants.TEMPLATE).stream()
								.findFirst();

						templateService.processAction(ActionType.NEW.toString(), tmplt, Constants.EXCEPTION).stream()
								.findFirst();

						Files.delete(Paths.get(file.getAbsolutePath()));

						Files.delete(Paths.get(file.getAbsolutePath()));

						log.error("Error parsing attachment: {}", ex.getMessage() + " for file: " + file.getName());
					}
				}
			}
		} catch (Exception ex) {
			tmplt.setErrorIssue(ex.getLocalizedMessage());
			log.error("Error parsing attachment: {}", ex.getMessage() + " for file: " + file.getName());
		}

	}

	// Method for reUploading failed Files
	private void saveNuxeoUploadUnsuccessful(File invoiceInfoXml) {
		Template template = new Template();
		template.setFileName(invoiceInfoXml.getName()
				);

		Optional<Template> listDocCountTemp = templateService
				.processAction(ActionType.SELECT_ID.toString(), template, Constants.TEMPLATE).stream().findFirst();

		if (listDocCountTemp.isPresent()) {

			template = listDocCountTemp.get();
			template.setIsSentToNuxeo(1);

			templateService.processAction(ActionType.UPDATE.toString(), template, Constants.TEMPLATE).stream()
					.findFirst();
		}
	}

	// Method for inserting data into DOC,CUSTOMER & DOC_Details Table
	private void insertDocuments(Optional<Template> tmpListNew, Invoice invoice, File file) {
		Document document = new io.naztech.nuxeoclient.model.Document();

		if (null != tmpListNew.get().getTemplateId())
			document.setTemplateId(tmpListNew.get().getTemplateId());

		Optional<io.naztech.nuxeoclient.model.Document> documentTemp = documentService
				.processAction(ActionType.NEW.toString(), getDocument(invoice, document), Constants.DOC).stream()
				.findFirst();

		if (documentTemp.isPresent() && null != invoice.getClass()) {

			io.naztech.nuxeoclient.model.Document documentData = documentTemp.get();
			documentData.setDocId(documentTemp.get().getDocId());

			documentService.processAction(ActionType.NEW.toString(), documentData, Constants.CUSTOMER);

			for (int i = 0; i < invoice.getInvoiceTable().size(); i++) {

				docDetailsService.processAction(ActionType.NEW.toString(), getDocDetails(invoice, documentTemp, i),
						Constants.DOC_DETAILS);
			}
		} else {
			log.info("Document Data is not available");
		}
	}

	// Setting values into Doc table object
	private Document getDocument(Invoice invoice, Document document) {

		document.setCurrency("GBP");
		document.setAccountNumber(invoice.getAccountNo());
		document.setSupplierNumber(invoice.getSupplierName());
		document.setVat(nullHandlerDouble(invoice.getVatNo()));
		document.setVatRate(nullHandlerDouble(invoice.getVatTotal()));

		document.setContactNumber(invoice.getTelephone());
		document.setDeliverTo(invoice.getDeliveryAddress());
		document.setDeliveryDetails(invoice.getDeliveryAddress());
		document.setCompanyAddress(invoice.getSupplierAddress());
		document.setCustomerAddress(invoice.getCustomerAddress());
		document.setInvoiceAmount(nullHandlerDouble(invoice.getNetTotal()));
		document.setInvoiceNumber(invoice.getInvoiceNumber());
		document.setInvoiceTo(invoice.getCustomerAddress());
		document.setNetInvoiceTotal(nullHandlerDouble(invoice.getGrossTotal()));
		document.setSupplierShortName(invoice.getSortName());
		document.setTotalBeforeVat(nullHandlerDouble(invoice.getNetTotal()));
		document.setValueOfGoods(nullHandlerDouble(invoice.getGrossTotal()));
		document.setContactNumber(invoice.getTelephone());
		document.setOrderId(invoice.getOrderNo());
		document.setVatNumber(invoice.getVatNo());

		document.setTotalAmountDue(nullHandlerDouble(invoice.getDue()));
		document.setTotalPrice(nullHandlerDouble(invoice.getGrossTotal()));
		document.setTraderReferenceNo(invoice.getReferenceNumber());
		document.setCustomerOrderNo(invoice.getOrderNo());
		document.setCustomerNumber(invoice.getOrderNo());
		document.setDeliveryNoteNo(nullHandlerInteger(invoice.getOrderNo()));
		document.setFaxNumber(invoice.getFax().toString());
		document.setCustomerNumber(invoice.getCustomerName());
		document.setReferenceNo(invoice.getReferenceNumber().toString());
		document.setCustomerOrderNo(invoice.getOrderNo());
		document.setDiscount(nullHandlerDouble(invoice.getDiscount()));

		try {
			document.setDespatchDate(new SimpleDateFormat("yyyy/MM/dd").parse(dateFormatter(invoice.getInvoiceDate())));

			document.setDocumentDate(new SimpleDateFormat("yyyy/MM/dd").parse(dateFormatter(invoice.getInvoiceDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return document;
	}

	// Setting values into Doc Details table object
	private DocDetails getDocDetails(Invoice invoice, Optional<Document> documentTemp, int i) {

		DocDetails docdetails = new DocDetails();

		docdetails.setDocId(documentTemp.get().getDocId());

		docdetails.setItemDescription(invoice.getInvoiceTable().get(i).getDescription());
		docdetails.setNetValue(nullHandlerDouble(invoice.getInvoiceTable().get(i).getTotal()));
		docdetails.setUnitPrice(nullHandlerDouble(invoice.getInvoiceTable().get(i).getUnitPrice()));
		docdetails.setValueOfGoods(nullHandlerDouble(invoice.getInvoiceTable().get(i).getTotal()));

		docdetails.setItemQty(nullHandlerInteger(invoice.getInvoiceTable().get(i).getQuantity()));
		docdetails.setItemCode(invoice.getInvoiceTable().get(i).getStockCode());
		docdetails.setTotalPrice(nullHandlerDouble(invoice.getInvoiceTable().get(i).getTotal()));

		return docdetails;
	}

	private Integer nullHandlerInteger(String quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Double nullHandlerDouble(String flt) {
		DecimalFormat df = new DecimalFormat("#.0");
		if (null == flt || flt.equals("")) {
			return (Double) 0.0;
		} else if (flt.equals("Each"))
			return 1.0;

		else {
			flt = getCleanData(flt);
			return Double.valueOf(df.format(Double.parseDouble(flt)));
		}

	}

	protected String getCleanData(String value) {
		if (null != value) {

			value = value.trim();

			if (value.contains("$") || value.contains("£") || value.contains(",") || value.contains("*")
					|| value.contains("GBP")) {

				value = value.replaceAll("[$£*a-zA-Z,]", "").trim();
			}

			return value;
		} else
			return null;

	}

	protected String dateFormatter(String date) {
		String[] parts = null;
		if (date.length() < 10 && (date.contains("/") || date.contains(".") || date.contains(" "))) {

			if (date.contains("/"))
				parts = date.split("/");
			else if (date.contains("."))
				parts = date.split(".");
			else if (date.contains(" "))
				parts = date.split(" ");

			if (parts.length > 2 && parts[2].trim().length() == 2) {
				date = parts[0] + "/" + parts[1] + "/20" + parts[2];
			}
		} else if (date.contains("st") || date.contains("nd") || date.contains("rd") || date.contains("th")) {
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th'] MMMM yyyy",
					Locale.ENGLISH);
			LocalDate localDate = LocalDate.parse(date, formatter1);
			date = localDate.toString();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd.MM.yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("d/MMMM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MM yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddMMMyy"));
		knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd-MM-yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd-MMMM-yyyy"));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));

		Exception ex = null;
		for (SimpleDateFormat df : knownPatterns) {
			try {
				if (!formatter.format(df.parse(date)).startsWith("0")) {
					return formatter.format(df.parse(date));
				}
			} catch (ParseException e) {
				ex = e;
			}
		}
		if (ex != null)
			log.warn("Failed to parse date " + date, ex);
		return "1970/01/01";
	}
}
