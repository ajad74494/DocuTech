package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum SPName {
	
	DOCUTECH("nDOCUTECH"),
	
	/* Stored procedure name */

	ACT_USER("ACT_user"),
	ACT_REMITTANCE_USER("ACT_remittance_user"),
	ACT_USER_GROUP("ACT_group"),
	ACT_GENERIC_MAP("ACT_generic_map"),
	ACT_TEMPLATE("ACT_template"),
	ACT_TEMPLATE_EXCEPTION("ACT_template_exception"),
	ACT_CUSTOMER("ACT_customer"),
	ACT_DOC_COUNT("ACT_doc_count"),
	ACT_DOC("ACT_doc"),
	ACT_DOC_DETAILS("ACT_doc_details"),
	ACT_SUPPLIER_META("ACT_supplier_meta"),
	ACT_SAGE50_TRANS_EXEL("ACT_sage50_trans_excel"),

	SEL_DOCUMENT("SEL_document"),
	SEL_DOC_DETAILS("SEL_doc_details"),
	SEL_DOC("SEL_doc"),
		
	SP_SEL_LEGAL_ENTITY("SEL_legal_entity"),
	SP_SEL_USER("SEL_user"),
	SP_SEL_GROUP("SEL_group"),
	SP_SEL_ROLE("SEL_role"),
	SP_SEL_LOGIN("SEL_login"),
	SP_SEL_REMIT_USER("SEL_remittance_user"),
	
	ACTION_NEW("NEW"),
	ACTION_LOGIN("LOGIN"),
	ACTION_LOGOUT("LOGOUT"),
	ACTION_SELECT("SELECT"),
	ACTION_CHANGE_PWD("CHANGE_PWD"),
	ACTION_GET_LOGIN_PASSWORD("GET_LOGIN_PASSWORD"),
	ACTION_REMIT_USER_DETAILS("GET_REMIT_USER_DETAILS"),
	ACTION_UPDATE("UPDATE"),
	ACTION_TRANSACTIONS("TRANSACTION"),
	ACTION_SUPPLIER_META("SUPPLIER"),

	ACTION_MAP("MAP"),
	ACTION_UNMAP("UNMAP"),	
	USER_SECONDARY_GROUP("USER_SECONDARY_GROUP"),
	MAP_USER_TO_GROUP("MAP_USER_TO_GROUP"),
	MAP_GROUP_TO_ROLE("MAP_GROUP_TO_ROLE"),
	
	/* Resultset type */
	RS_TYPE_USER("RS_TYPE_USER"),
	RS_TYPE_LEGAL_ENTITY("RS_TYPE_LEGAL_ENTITY"),
	RS_TYPE_USER_GROUP("RS_TYPE_GROUP"),
	RS_TYPE_ROLE("RS_TYPE_ROLE"),
	RS_TYPE_LOGIN("RS_TYPE_LOGIN"),
	RS_TYPE_USER_ID("RS_TYPE_USER_ID"),
	RS_TYPE_SECONDARY_GROUP("RS_TYPE_SECONDARY_GROUP"),
	
	RS_TYPE_DOCUMENT("RS_TYPE_DOCUMENT"),
	RS_TYPE_TEMPLATE_EXCEPTION("RS_TYPE_TEMPLATE_EXCEPTION"),
	RS_TYPE_TEMPLATE("RS_TYPE_TEMPLATE"),
	RS_TYPE_CUSTOMER("RS_TYPE_CUSTOMER"),
	RS_TYPE_DOC_COUNT("RS_TYPE_DOC_COUNT"),
	RS_TYPE_DOC("RS_TYPE_DOC"),
	RS_TYPE_SUPPLIER("RS_TYPE_SUPPLIER"),
	RS_TYPE_DOC_DETAILS("RS_TYPE_DOC_DETAILS"),
	RS_TYPE_SUPPLIER_META("RS_SUPPLIER_META"),
	
	SHEET_TRANSACTIONS("Invoice Transactions"),
	SHEET_SUPPLIERS("Trading Accounts");

	private final String storeProcedureName;

	private SPName(String storeProcedureName) {
		this.storeProcedureName = storeProcedureName;
	}

	@Override
	public String toString() {
		return storeProcedureName;
	}

}
