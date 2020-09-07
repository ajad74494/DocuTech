package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author Abdullah.Kafi
 * @since 2019-09-17
 **/

public class Customer {

	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;
	private Integer customerVer;
	private String customerNumber;
	private String contactNumber;
	private String customerOrderNo;
	private String customerAddress;

	//foreign key (primary key of DOC table)
	private Integer docId;

	private Date modifiedOn;
	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerVer() {
		return customerVer;
	}

	public void setCustomerVer(Integer customerVer) {
		this.customerVer = customerVer;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_customer_key", "customerId");
			sql2BeanMap.put("@id_customer_ver", "customerVer");
			sql2BeanMap.put("@tx_customer_number", "customerNumber");
			sql2BeanMap.put("@tx_contact_number", "contactNumber");
			sql2BeanMap.put("@tx_customer_order_no", "customerOrderNo");
			sql2BeanMap.put("@tx_customer_address", "customerAddress");
			sql2BeanMap.put("@id_doc_key", "docId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_customer_key", "customerId");
			rs2BeanMap.put("id_customer_ver", "customerVer");
			rs2BeanMap.put("tx_customer_number", "customerNumber");
			rs2BeanMap.put("tx_contact_number", "contactNumber");
			rs2BeanMap.put("tx_customer_order_no", "customerOrderNo");
			rs2BeanMap.put("tx_customer_address", "customerAddress");
			rs2BeanMap.put("id_doc_key", "docId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
		}

		return rs2BeanMap;
	}

}
