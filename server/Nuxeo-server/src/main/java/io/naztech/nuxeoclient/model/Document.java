package io.naztech.nuxeoclient.model;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Abdullah.Kafi
 * @since 2019-09-17
 **/
public class Document {

	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;
	
	private Integer templateId;
	private Integer docId;
	private Integer docVer;
	private String supplierNumber;
	private Integer deliveryNoteNo;
	private Integer receiptNo;
	private Integer documentNumber;
	private Integer recipientOfInvoice;
	private String supplierShortName;
	
	private String vatNumber;	
	private String invoiceNumber;
	private String currency;
	private String accountNumber;
	private String voucherNumber;
	private String faxNumber;
	private String associationNumber;
	private String companyAddress;
	private String invoiceTo;
	private String deliverTo;
	private String orderId;
	private String deliveryNote;
	private String deliveryDetails;
	private String paymentDetails;
	private String traderReferenceNo;

	//extra fields for sage50 store procedure
	private String transactionType;
	private String accountRef;
	private String nominalCode;
	private String deptCode;
	private String taxCode;
	private String invoiceDate;
	
	
	private Double vat;
	private Double vatRate;
	private Double vatPayable;
	private Double invoiceAmount;
	private Double totalBeforeVat;
	private Double totalAmountDue;
	private Double discount;
	private Double netInvoiceTotal;

	private Date modifiedOn;
	private Date documentDate;
	private Date despatchDate;
	private java.sql.Date docDate;
	private java.sql.Date despDate;

	/* Document details information */
	private Integer docDetailsId;
	private Integer docDetailsVer;
	private Integer partNo;
	private Integer itemQty;
	
	private String referenceNo;
	private String itemName;
	private String itemCode;
	private String rent;
	private String pack;
	private String itemDescription;
	private String propertyAddress;

	private Double trade;
	private Double unitPrice;
	private Double totalPrice;
	private Double netValue;
	private Double valueOfGoods;
	private Double insurancePremium;

	/* Customer information */
	private Integer customerId;
	private Integer customerVer;

	private String customerNumber;
	private String contactNumber;
	private String customerOrderNo;
	private String customerAddress;
	private String fileName;
	
	private Date fromDate;
	private Date toDate;
	
	
	/* Getters/Setters */
	
	
	public Integer getTemplateId() {
		return templateId;
	}

	public String getSupplierShortName() {
		return supplierShortName;
	}

	public void setSupplierShortName(String supplierShortName) {
		this.supplierShortName = supplierShortName;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocVer() {
		return docVer;
	}

	public void setDocVer(Integer docVer) {
		this.docVer = docVer;
	}

	public String getSupplierNumber() {
		return supplierNumber;
	}

	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}

	public Integer getDeliveryNoteNo() {
		return deliveryNoteNo;
	}

	public void setDeliveryNoteNo(Integer deliveryNoteNo) {
		this.deliveryNoteNo = deliveryNoteNo;
	}

	public Integer getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(Integer receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(Integer documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Integer getRecipientOfInvoice() {
		return recipientOfInvoice;
	}

	public void setRecipientOfInvoice(Integer recipientOfInvoice) {
		this.recipientOfInvoice = recipientOfInvoice;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getAssociationNumber() {
		return associationNumber;
	}

	public void setAssociationNumber(String associationNumber) {
		this.associationNumber = associationNumber;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getInvoiceTo() {
		return invoiceTo;
	}

	public void setInvoiceTo(String invoiceTo) {
		this.invoiceTo = invoiceTo;
	}

	public String getDeliverTo() {
		return deliverTo;
	}

	public void setDeliverTo(String deliverTo) {
		this.deliverTo = deliverTo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryNote() {
		return deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(String deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}

	public String getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public String getTraderReferenceNo() {
		return traderReferenceNo;
	}

	public void setTraderReferenceNo(String traderReferenceNo) {
		this.traderReferenceNo = traderReferenceNo;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public Double getVatPayable() {
		return vatPayable;
	}

	public void setVatPayable(Double vatPayable) {
		this.vatPayable = vatPayable;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getTotalBeforeVat() {
		return totalBeforeVat;
	}

	public void setTotalBeforeVat(Double totalBeforeVat) {
		this.totalBeforeVat = totalBeforeVat;
	}

	public Double getTotalAmountDue() {
		return totalAmountDue;
	}

	public void setTotalAmountDue(Double totalAmountDue) {
		this.totalAmountDue = totalAmountDue;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getNetInvoiceTotal() {
		return netInvoiceTotal;
	}

	public void setNetInvoiceTotal(Double netInvoiceTotal) {
		this.netInvoiceTotal = netInvoiceTotal;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	
	public java.sql.Date getDocDate() {
		return docDate;
	}

	public void setDocDate(java.sql.Date docDate) {
		this.docDate = docDate;
	}

	public Date getDespatchDate() {
		return despatchDate;
	}

	public void setDespatchDate(Date despatchDate) {
		this.despatchDate = despatchDate;
	}

	public java.sql.Date getDespDate() {
		return despDate;
	}

	public void setDespatchDate(java.sql.Date despDate) {
		this.despDate = despDate;
	}
	
	public Integer getDocDetailsId() {
		return docDetailsId;
	}

	public void setDocDetailsId(Integer docDetailsId) {
		this.docDetailsId = docDetailsId;
	}

	public Integer getDocDetailsVer() {
		return docDetailsVer;
	}

	public void setDocDetailsVer(Integer docDetailsVer) {
		this.docDetailsVer = docDetailsVer;
	}

	public Integer getPartNo() {
		return partNo;
	}

	public void setPartNo(Integer partNo) {
		this.partNo = partNo;
	}

	public Integer getItemQty() {
		return itemQty;
	}

	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public Double getTrade() {
		return trade;
	}

	public void setTrade(Double trade) {
		this.trade = trade;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getNetValue() {
		return netValue;
	}

	public void setNetValue(Double netValue) {
		this.netValue = netValue;
	}

	public Double getValueOfGoods() {
		return valueOfGoods;
	}

	public void setValueOfGoods(Double valueOfGoods) {
		this.valueOfGoods = valueOfGoods;
	}

	public Double getInsurancePremium() {
		return insurancePremium;
	}

	public void setInsurancePremium(Double insurancePremium) {
		this.insurancePremium = insurancePremium;
	}

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

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
	 * @return the nominalCode
	 */
	public String getNominalCode() {
		return nominalCode;
	}

	/**
	 * @param nominalCode the nominalCode to set
	 */
	public void setNominalCode(String nominalCode) {
		this.nominalCode = nominalCode;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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

	
	
	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_doc_key", "docId");
			sql2BeanMap.put("@id_doc_ver", "docVer");
			sql2BeanMap.put("@supplier_number", "supplierNumber");
			sql2BeanMap.put("@delivery_note_no", "deliveryNoteNo");
			sql2BeanMap.put("@receipt_number", "receiptNo");
			sql2BeanMap.put("@document_number", "documentNumber");
			sql2BeanMap.put("@dt_document", "documentDate");
			sql2BeanMap.put("@tx_trader_reference_no", "traderReferenceNo");
			sql2BeanMap.put("@recipient_of_invoice", "recipientOfInvoice");
			sql2BeanMap.put("@vat_number", "vatNumber");
			sql2BeanMap.put("@tx_invoice_number", "invoiceNumber");
			sql2BeanMap.put("@dt_despatch", "despatchDate");
			sql2BeanMap.put("@tx_currency", "currency");
			sql2BeanMap.put("@tx_account_number", "accountNumber");
			sql2BeanMap.put("@tx_voucher_number", "voucherNumber");
			sql2BeanMap.put("@tx_fax_number", "faxNumber");
			sql2BeanMap.put("@tx_association_number", "associationNumber");
			sql2BeanMap.put("@tx_company_address", "companyAddress");
			sql2BeanMap.put("@tx_invoice_to", "invoiceTo");
			sql2BeanMap.put("@tx_deliver_to", "deliverTo");
			sql2BeanMap.put("@tx_order_id", "orderId");
			sql2BeanMap.put("@tx_delivery_note", "deliveryNote");
			sql2BeanMap.put("@tx_delivery_details", "deliveryDetails");
			sql2BeanMap.put("@tx_payment_details", "paymentDetails");
			sql2BeanMap.put("@flt_vat", "vat");
			sql2BeanMap.put("@flt_vat_rate", "vatRate");
			sql2BeanMap.put("@flt_vat_payable", "vatPayable");
			sql2BeanMap.put("@flt_invoice_amount", "invoiceAmount");
			sql2BeanMap.put("@flt_total_before_vat", "totalBeforeVat");
			sql2BeanMap.put("@flt_total_amount_due", "totalAmountDue");
			sql2BeanMap.put("@flt_discount", "discount");
			sql2BeanMap.put("@flt_net_invoice_total", "netInvoiceTotal");
			sql2BeanMap.put("@id_template_key", "templateId");
			sql2BeanMap.put("@tx_supplier_short_name", "supplierShortName");

			/* Document details information */
			sql2BeanMap.put("@id_doc_details_key", "docDetailsId");
			sql2BeanMap.put("@id_doc_details_ver", "docDetailsVer");
			sql2BeanMap.put("@part_no", "partNo");
			sql2BeanMap.put("@item_qty", "itemQty");
			sql2BeanMap.put("@reference_no", "referenceNo");
			sql2BeanMap.put("@tx_item_name", "itemName");
			sql2BeanMap.put("@tx_item_code", "itemCode");
			sql2BeanMap.put("@tx_rent", "rent");
			sql2BeanMap.put("@tx_pack", "pack");
			sql2BeanMap.put("@tx_item_description", "itemDescription");
			sql2BeanMap.put("@tx_property_address", "propertyAddress");
			sql2BeanMap.put("@flt_trade", "trade");
			sql2BeanMap.put("@flt_unit_price", "unitPrice");
			sql2BeanMap.put("@flt_total_price", "totalPrice");
			sql2BeanMap.put("@flt_net_value", "netValue");
			sql2BeanMap.put("@flt_value_of_goods", "valueOfGoods");
			sql2BeanMap.put("@flt_insurance_premium", "insurancePremium");

			/* Customer information */
			sql2BeanMap.put("@id_customer_key", "customerId");
			sql2BeanMap.put("@id_customer_ver", "customerVer");
			sql2BeanMap.put("@tx_customer_number", "customerNumber");
			sql2BeanMap.put("@tx_contact_number", "contactNumber");
			sql2BeanMap.put("@tx_customer_order_no", "customerOrderNo");
			sql2BeanMap.put("@tx_customer_address", "customerAddress");
		}

		return sql2BeanMap;
	}

	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_doc_key", "docId");
			rs2BeanMap.put("id_doc_ver", "docVer");
			rs2BeanMap.put("supplier_number", "supplierNumber");
			rs2BeanMap.put("delivery_note_no", "deliveryNoteNo");
			rs2BeanMap.put("receipt_number", "receiptNo");
			rs2BeanMap.put("document_number", "documentNumber");
			//rs2BeanMap.put("dt_document", "documentDate");
			rs2BeanMap.put("dt_document", "docDate");			
			rs2BeanMap.put("tx_trader_reference_no", "traderReferenceNo");
			rs2BeanMap.put("recipient_of_invoice", "recipientOfInvoice");
			rs2BeanMap.put("vat_number", "vatNumber");
			rs2BeanMap.put("tx_invoice_number", "invoiceNumber");
			//rs2BeanMap.put("dt_despatch", "despatchDate");
			rs2BeanMap.put("dt_document", "despDate");
			rs2BeanMap.put("tx_currency", "currency");
			rs2BeanMap.put("tx_account_number", "accountNumber");
			rs2BeanMap.put("tx_voucher_number", "voucherNumber");
			rs2BeanMap.put("tx_fax_number", "faxNumber");
			rs2BeanMap.put("tx_association_number", "associationNumber");
			rs2BeanMap.put("tx_company_address", "companyAddress");
			rs2BeanMap.put("tx_invoice_to", "invoiceTo");
			rs2BeanMap.put("tx_deliver_to", "deliverTo");
			rs2BeanMap.put("tx_order_id", "orderId");
			rs2BeanMap.put("tx_delivery_note", "deliveryNote");
			rs2BeanMap.put("tx_delivery_details", "deliveryDetails");
			rs2BeanMap.put("tx_payment_details", "paymentDetails");
			rs2BeanMap.put("flt_vat", "vat");
			rs2BeanMap.put("flt_vat_rate", "vatRate");
			rs2BeanMap.put("flt_vat_payable", "vatPayable");
			rs2BeanMap.put("flt_invoice_amount", "invoiceAmount");
			rs2BeanMap.put("flt_total_before_vat", "totalBeforeVat");
			rs2BeanMap.put("flt_total_amount_due", "totalAmountDue");
			rs2BeanMap.put("flt_discount", "discount");
			rs2BeanMap.put("flt_net_invoice_total", "netInvoiceTotal");
			rs2BeanMap.put("dtt_mod", "modifiedOn");
			rs2BeanMap.put("id_template_key", "templateId");
			rs2BeanMap.put("tx_supplier_short_name", "supplierShortName");
			
			/*extra mapping for sage50 store procedure */
			rs2BeanMap.put("tx_transaction_type", "transactionType");
			rs2BeanMap.put("tx_account_ref", "accountRef");
			rs2BeanMap.put("tx_nominal_code", "nominalCode");
			rs2BeanMap.put("tx_dept_code", "deptCode");
			rs2BeanMap.put("tx_tax_code", "taxCode");
			rs2BeanMap.put("dt_mod", "invoiceDate");
			
			/* Document details information */
			rs2BeanMap.put("id_doc_details_key", "docDetailsId");
			rs2BeanMap.put("id_doc_details_ver", "docDetailsVer");
			rs2BeanMap.put("part_no", "partNo");
			rs2BeanMap.put("item_qty", "itemQty");
			rs2BeanMap.put("reference_no", "referenceNo");
			rs2BeanMap.put("tx_item_name", "itemName");
			rs2BeanMap.put("tx_item_code", "itemCode");
			rs2BeanMap.put("tx_rent", "rent");
			rs2BeanMap.put("tx_pack", "pack");
			rs2BeanMap.put("tx_item_description", "itemDescription");
			rs2BeanMap.put("tx_property_address", "propertyAddress");
			rs2BeanMap.put("flt_trade", "trade");
			rs2BeanMap.put("flt_unit_price", "unitPrice");
			rs2BeanMap.put("flt_total_price", "totalPrice");
			rs2BeanMap.put("flt_net_value", "netValue");
			rs2BeanMap.put("flt_value_of_goods", "valueOfGoods");
			rs2BeanMap.put("flt_insurance_premium", "insurancePremium");

			/* Customer information */
			rs2BeanMap.put("id_customer_key", "customerId");
			rs2BeanMap.put("id_customer_ver", "customerVer");
			rs2BeanMap.put("tx_customer_number", "customerNumber");
			rs2BeanMap.put("tx_contact_number", "contactNumber");
			rs2BeanMap.put("tx_customer_order_no", "customerOrderNo");
			rs2BeanMap.put("tx_customer_address", "customerAddress");
			rs2BeanMap.put("tx_file_name", "fileName");			
		}

		return rs2BeanMap;
	}



}
