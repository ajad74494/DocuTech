package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Asadullah.galib
 * @since 2019-09-16
 **/
public class Template {

	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;

	private Integer docCountId;
	private Integer templateId;
	private Integer templateVer;

	private Integer isTmpltExist;
	private Integer isSentToNuxeo;
	private Integer isTemplateParsed;
	private Integer isParsedSuccessful;

	private String pdfType;
	private String processorType;
	private String clientName;
	private String supplierName;
	private String docSource;
	private String receivedDocId;
	private String docType;

	/* Template Exception */
	private String errorIssue;
	private String errorIssueSource;
	private String fileName;

	private Date modifiedOn;
	private Date fromDate;
	private Date toDate;

	/* Start getters and setters */

	public Integer getTemplateId() {
		return templateId;
	}

	public Integer getDocCountId() {
		return docCountId;
	}

	public void setDocCountId(Integer docCountId) {
		this.docCountId = docCountId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getTemplateVer() {
		return templateVer;
	}

	public void setTemplateVer(Integer templateVer) {
		this.templateVer = templateVer;
	}

	public Integer getIsTmpltExist() {
		return isTmpltExist;
	}

	public void setIsTmpltExist(Integer isTmpltExist) {
		this.isTmpltExist = isTmpltExist;
	}

	public Integer getIsSentToNuxeo() {
		return isSentToNuxeo;
	}

	public void setIsSentToNuxeo(Integer isSentToNuxeo) {
		this.isSentToNuxeo = isSentToNuxeo;
	}

	public Integer getIsTemplateParsed() {
		return isTemplateParsed;
	}

	public void setIsTemplateParsed(Integer isTemplateParsed) {
		this.isTemplateParsed = isTemplateParsed;
	}

	public Integer getIsParsedSuccessful() {
		return isParsedSuccessful;
	}

	public void setIsParsedSuccessful(Integer isParsedSuccessful) {
		this.isParsedSuccessful = isParsedSuccessful;
	}

	public String getPdfType() {
		return pdfType;
	}

	public void setPdfType(String pdfType) {
		this.pdfType = pdfType;
	}

	public String getProcessorType() {
		return processorType;
	}

	public void setProcessorType(String processorType) {
		this.processorType = processorType;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDocSource() {
		return docSource;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

	public String getReceivedDocId() {
		return receivedDocId;
	}

	public void setReceivedDocId(String receivedDocId) {
		this.receivedDocId = receivedDocId;
	}

	public String getErrorIssue() {
		return errorIssue;
	}

	public void setErrorIssue(String errorIssue) {
		this.errorIssue = errorIssue;
	}

	public String getErrorIssueSource() {
		return errorIssueSource;
	}

	public void setErrorIssueSource(String errorIssueSource) {
		this.errorIssueSource = errorIssueSource;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	/* End getters and setters */

	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_template_key", "templateId");
			sql2BeanMap.put("@id_template_ver", "templateVer");
			sql2BeanMap.put("@is_send_to_nuxeo", "isSentToNuxeo");
			sql2BeanMap.put("@is_template_prepared", "isTemplateParsed");
			sql2BeanMap.put("@is_parsed_successfully", "isParsedSuccessful");
			sql2BeanMap.put("@tx_pdf_type", "pdfType");
			sql2BeanMap.put("@tx_processor_type", "processorType");
			sql2BeanMap.put("@tx_client_name", "clientName");
			sql2BeanMap.put("@tx_supplier_name", "supplierName");
			sql2BeanMap.put("@tx_doc_source", "docSource");
			sql2BeanMap.put("@tx_received_doc_id", "receivedDocId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
			sql2BeanMap.put("@tx_doc_type", "docType");

			/* Template Exception */
			sql2BeanMap.put("@tx_error_issue", "errorIssue");
			sql2BeanMap.put("@tx_error_issue_source", "errorIssueSource");
			sql2BeanMap.put("@tx_file_name", "fileName");
			sql2BeanMap.put("@id_doc_count", "docCountId");
			
			/*
			 * sql2BeanMap.put("@dtt_from", "fromDate"); sql2BeanMap.put("@dtt_to",
			 * "toDate");
			 */
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_template_key", "templateId");
			rs2BeanMap.put("id_template_ver", "templateVer");
			
			rs2BeanMap.put("is_abbyy_error", "isTmpltExist");
			rs2BeanMap.put("is_send_to_nuxeo", "isSentToNuxeo");
			rs2BeanMap.put("is_template_prepared", "isTemplateParsed");
			rs2BeanMap.put("is_parsed_successfully", "isParsedSuccessful");
			rs2BeanMap.put("tx_pdf_type", "pdfType");
			rs2BeanMap.put("tx_processor_type", "processorType");
			rs2BeanMap.put("tx_client_name", "clientName");
			rs2BeanMap.put("tx_supplier_name", "supplierName");
			rs2BeanMap.put("tx_doc_source", "docSource");
			rs2BeanMap.put("tx_received_doc_id", "receivedDocId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
			rs2BeanMap.put("tx_doc_type", "docType");

			/* Template Exception */
			rs2BeanMap.put("tx_error_issue", "errorIssue");
			rs2BeanMap.put("tx_error_issue_source", "errorIssueSource");
			rs2BeanMap.put("tx_file_name", "fileName");
			rs2BeanMap.put("id_doc_count", "docCountId");
		}

		return rs2BeanMap;
	}

	@Override
	public String toString() {
		return "Template [docCountId=" + docCountId + ", templateId=" + templateId + ", templateVer=" + templateVer
				+ ", isTmpltExist=" + isTmpltExist + ", isSentToNuxeo=" + isSentToNuxeo + ", isTemplateParsed="
				+ isTemplateParsed + ", isParsedSuccessful=" + isParsedSuccessful + ", pdfType=" + pdfType
				+ ", processorType=" + processorType + ", clientName=" + clientName + ", supplierName=" + supplierName
				+ ", docSource=" + docSource + ", receivedDocId=" + receivedDocId + ", docType=" + docType
				+ ", errorIssue=" + errorIssue + ", errorIssueSource=" + errorIssueSource + ", fileName=" + fileName
				+ ", modifiedOn=" + modifiedOn + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}
	

}