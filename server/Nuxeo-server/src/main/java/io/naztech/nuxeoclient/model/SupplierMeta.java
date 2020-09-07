package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Md. Asadullahill Galib
 * @desc: Model Class for SupplierMeta Table
 * @since 2019-12-29
 **/
public class SupplierMeta {
	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;
	
	
	private Integer accountId;
	private Integer accountVer;
	private Integer isActive;
	private Integer environmentId;
	private Integer stateId;
	private Integer actionId;
	private Integer eventId;
	private Integer UserModId;
	private Date modifiedOn;
	private String supplierFullName;
	private String supplierShortName;
	private String accountRef;
	private String nominalCode;
	private String deptCode;
	private String transactionType;
	
	//Getter & Setter
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getAccountVer() {
		return accountVer;
	}
	public void setAccountVer(Integer accountVer) {
		this.accountVer = accountVer;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Integer getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}
	public Integer getStateId() {
		return stateId;
	}
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getUserModId() {
		return UserModId;
	}
	public void setUserModId(Integer userModId) {
		UserModId = userModId;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getSupplierFullName() {
		return supplierFullName;
	}
	public void setSupplierFullName(String supplierFullName) {
		this.supplierFullName = supplierFullName;
	}
	public String getSupplierShortName() {
		return supplierShortName;
	}
	public void setSupplierShortName(String supplierShortName) {
		this.supplierShortName = supplierShortName;
	}
	
	public String getAccountRef() {
		return accountRef;
	}
	public void setAccountRef(String accountRef) {
		this.accountRef = accountRef;
	}
	public String getNominalCode() {
		return nominalCode;
	}
	public void setNominalCode(String nominalCode) {
		this.nominalCode = nominalCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	
	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_account_key", "accountId");
			sql2BeanMap.put("@id_account_ver", "accountVer");
			sql2BeanMap.put("@is_active", "isActive");
			sql2BeanMap.put("@id_evn_key", "environmentId");
			sql2BeanMap.put("@id_state_key", "stateId");
			sql2BeanMap.put("@id_action_key", "actionId");
			sql2BeanMap.put("@id_event_key", "eventId");
			sql2BeanMap.put("@id_user_mod_key", "UserModId");
			sql2BeanMap.put("@dtt_mod", "modifiedOn");
			sql2BeanMap.put("@tx_supplier_full_name", "supplierFullName");
			sql2BeanMap.put("@tx_supplier_short_name", "supplierShortName");
			sql2BeanMap.put("@tx_account_ref", "accountRef");
			sql2BeanMap.put("@tx_nominal_code", "nominalCode");
			sql2BeanMap.put("@tx_dept_code", "deptCode");
			sql2BeanMap.put("@tx_transaction_type", "transactionType");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_account_key", "accountId");
			rs2BeanMap.put("id_account_ver", "accountVer");
			rs2BeanMap.put("is_active", "isActive");
			rs2BeanMap.put("id_evn_key", "environmentId");
			rs2BeanMap.put("id_state_key", "stateId");
			rs2BeanMap.put("id_action_key", "actionId");
			rs2BeanMap.put("id_event_key", "eventId");
			rs2BeanMap.put("id_user_mod_key", "UserModId");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
			rs2BeanMap.put("tx_supplier_full_name", "supplierFullName");
			rs2BeanMap.put("tx_supplier_short_name", "supplierShortName");
			rs2BeanMap.put("tx_account_ref", "accountRef");
			rs2BeanMap.put("tx_nominal_code", "nominalCode");
			rs2BeanMap.put("tx_dept_code", "deptCode");
			rs2BeanMap.put("tx_transaction_type", "transactionType");

		}

		return rs2BeanMap;
	}
	
	
}
