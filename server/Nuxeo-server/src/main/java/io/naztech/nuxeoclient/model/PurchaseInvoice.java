package io.naztech.nuxeoclient.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@code PurchaseInvoice} model class is used to set/get purchase details
 * @author bm.alamin
 *
 */
public class PurchaseInvoice {
	
	private static Map<String, String> rs2BeanMap = null;
	private String accountRef;
	private int nominalAccountRef;
	private int deptCode;
	private String invoiceDate;
	private double netAmount;
	private double taxAmount;
	private String taxCode;
	
	/**
	 * @return the accountRef
	 */
	public String getAccountRef() {
		return accountRef;
	}
	/**
	 * @param accountRef the accountRef to set
	 */
	public void setAccountRef(String accountRef) {
		this.accountRef = accountRef;
	}
	/**
	 * @return the nominalAccountRef
	 */
	public int getNominalAccountRef() {
		return nominalAccountRef;
	}
	/**
	 * @param nominalAccountRef the nominalAccountRef to set
	 */
	public void setNominalAccountRef(int nominalAccountRef) {
		this.nominalAccountRef = nominalAccountRef;
	}
	/**
	 * @return the deptCode
	 */
	public int getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(int deptCode) {
		this.deptCode = deptCode;
	}
	/**
	 * @return the invoiceDate
	 */
	public String getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}
	/**
	 * @param netAmount the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	/**
	 * @return the taxAmount
	 */
	public double getTaxAmount() {
		return taxAmount;
	}
	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	/**
	 * @return the taxCode
	 */
	public String getTaxCode() {
		return taxCode;
	}
	/**
	 * @param taxCode the taxCode to set
	 */
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PurchaseInvoice [accountRef=" + accountRef + ", nominalAccountRef=" + nominalAccountRef + ", deptCode=" + deptCode + ", invoiceDate="
		        + invoiceDate + ", netAmount=" + netAmount + ", taxAmount=" + taxAmount + ", taxCode=" + taxCode + "]";
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
