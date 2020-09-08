package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
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
public class InvoiceDataProcessService extends AbstractScannedInvoidUploadService {
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

	public void uploadDocument(Invoice invoice, File file, Boolean sendToNuxeoFlag) throws IOException {
		DocumentWrapper documentWrapper = new DocumentWrapper();
		Template template = new Template();
		DocCount docCount = new DocCount();
		Optional<Template> tmpListNew = Optional.empty();
		List<DocCount> docCountList = null;
		String archiveFolderPath = rootOut + Constants.FWD_SL + invoice.getInvoiceType() + Constants.FWD_SL
				+ Constants.Archive;
		String badfolderPath = rootOut + Constants.FWD_SL + folderName + Constants.FWD_SL + Constants.Bad;
		LocalDate date = LocalDate.now();
		String strDate = date.toString();
		try {
			if (file.getName().contains(sourceType)) {
				template.setDocSource(sourceType);
			}
			template.setPdfType(pdfType);
			template.setProcessorType(proType);
			template.setDocType(docType);

			// folder structure creation
			directoryCreator.outDirectorycreatorForSuppliers(invoice.getInvoiceType());

			// move to archive folder
			moveToArchive(archiveFolderPath, file, strDate);

			docCount.setFileName(file.getName());
			docCount.setSearchByFile(Constants.YES);
			log.info(docCount.toString());
			docCountList = docCountService.processAction(ActionType.SELECT.toString(), docCount);

			if (docCountList.parallelStream().findFirst().isPresent()) {
				template.setDocCountId(docCountList.get(0).getDocCountId());
			}

			if (((null == invoice.getInvoiceNumber() || invoice.getInvoiceNumber().isEmpty())
					|| (null == invoice.getInvoiceDate() || invoice.getInvoiceDate().isEmpty())
					|| (null == invoice.getNetTotal() || invoice.getGrossTotal().isEmpty()))) {

//				move to bad folder
				moveToBadFolder(badfolderPath, file, strDate);

				template.setErrorIssue("Service class name is " + invoice.getClass().getName() + ", Invoice number is "
						+ invoice.getInvoiceNumber() + ", Invoice Date is " + invoice.getInvoiceDate()
						+ ", Net Invoice Total is " + invoice.getNetTotal());
				template.setErrorIssueSource("Data Not available");
				template.setIsSentToNuxeo(0);
				template.setIsTemplateParsed(0);
				template.setIsParsedSuccessful(0);

				templateService.processAction(ActionType.NEW.toString(), template, Constants.TEMPLATE).stream()
						.findFirst();

				log.info("Moved to Bad Folder", file.getName());
			} else {

				if (null != file) {

					template.setClientName(invoice.getCustomerName());
					template.setReceivedDocId(template.getClientName() + "_" + invoice.getInvoiceNumber());
					template.setIsTemplateParsed(1);
					template.setIsParsedSuccessful(1);
					template.setSupplierName(invoice.getInvoiceTitle());

					try {
						// Setup Nuxeo Connection
						nuxeoClientService.setNuxeoClient(nuxeoClient);

						documentWrapper = nuxeoClientService.convertInvoiceToDocumentReq(invoice);
						documentWrapper.setFile(file);
						template.setSupplierName(documentWrapper.getTitle());

						if (nuxeoClientService.createDocument(documentWrapper).equals(null)) {
							saveNuxeoUploadUnsuccessful(file);
							log.error(file.getName() + " Failed to upload file on Nuxeo");
						} else {
							template.setIsSentToNuxeo(1);
							log.info("Successfully Uploaded Document Into Nuxeo: " + file.getName() + " & "
									+ "Invoice No:" + invoice.getInvoiceNumber());

							tmpListNew = templateService
									.processAction(ActionType.NEW.toString(), template, Constants.TEMPLATE).stream()
									.findFirst();

							if (tmpListNew.isPresent()) {
								try {

									insertDocuments(tmpListNew, invoice, file);
									Files.delete(Paths.get(file.getAbsolutePath()));
								} catch (Exception e) {
									log.info(e.getMessage());
								}
							}
						}
					} catch (Exception ex) {

						template.setErrorIssue(ex.getLocalizedMessage());

						template.setErrorIssueSource("Failed to Create object from xml");

						templateService.processAction(ActionType.NEW.toString(), template, Constants.TEMPLATE).stream()
								.findFirst();

						templateService.processAction(ActionType.NEW.toString(), template, Constants.EXCEPTION).stream()
								.findFirst();

						Files.delete(Paths.get(file.getAbsolutePath()));

						log.error("Error parsing attachment: {}", ex.getMessage() + " for file: " + file.getName());
					}
				}
			}
		} catch (Exception ex) {
			template.setErrorIssue(ex.getLocalizedMessage());
			log.error("Error parsing attachment: {}", ex.getMessage() + " for file: " + file.getName());
		}

	}

	// Method for reUploading failed Files
	private void saveNuxeoUploadUnsuccessful(File invoiceInfoXml) {
		Template template = new Template();
		template.setFileName(invoiceInfoXml.getName());

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
		try {

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

		} catch (Exception e) {

			log.info(e.getMessage());
		}
	}

	// Setting values into Doc table object
	private Document getDocument(Invoice invoice, Document document) {

		try {
			document.setCurrency("GBP");
			document.setAccountNumber(invoice.getAccountNo());
			document.setSupplierNumber(invoice.getSupplierName());
			document.setVat(nullHandlerDouble(invoice.getVatTotal()));
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
			document.setFaxNumber(invoice.getFax());
			document.setCustomerNumber(invoice.getCustomerName());
			document.setReferenceNo(invoice.getReferenceNumber());
			document.setCustomerOrderNo(invoice.getOrderNo());
			document.setDiscount(nullHandlerDouble(invoice.getDiscount()));

		} catch (Exception e) {
			log.info(e.getMessage());
		}
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
}