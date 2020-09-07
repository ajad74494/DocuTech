


/**
*	Author: Abdullah Al Kafi
*	Desc: Docutech Doc Audit Table
*	Object:  Table [nDOCUTECH].[T_DOC_AUDIT]    Script Date: 9/9/2019
**/

DROP TABLE [nDOCUTECH].[T_DOC_AUDIT]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_DOC_AUDIT](
	id_doc_key 			    int 					 	NOT NULL,
	id_doc_ver 			    int 						NOT NULL,
	is_active 				int 						NOT NULL,
	id_env_key 				int 						NOT NULL,
	id_user_mod_key 		int 						NOT NULL,
	dtt_mod 				datetime 					NOT NULL,
	id_event_key 			int 						NOT NULL,
	id_state_key 			int 						NOT NULL,
	id_action_key 			int 						NOT NULL,
	
	supplier_number 		int 						NOT NULL,
	delivery_note_no 		int  						NOT NULL,
	receipt_number 			int 						NOT NULL,
	document_number 		int 						NOT NULL,
	dt_document				date 						NOT NULL,
	recipient_of_invoice 	int 					NOT NULL,
	vat_number 				varchar(256) 				NOT NULL,
	
	dt_despatch 			date 						NOT NULL,

	tx_invoice_number 		varchar(16) 				NOT NULL,
	tx_currency 			varchar(8)					NOT NULL,
	tx_account_number 		varchar(256)				NOT NULL,
	tx_voucher_number 		varchar(256)				NOT NULL,
	tx_fax_number 			varchar(128)				NOT NULL,
	tx_association_number 	varchar(256)				NOT NULL,
	tx_company_address 		varchar(256)				NOT NULL,
	tx_invoice_to 			varchar(256)				NOT NULL,
	tx_deliver_to 			varchar(256)				NOT NULL,
	tx_order_id 			varchar(256)				NOT NULL,
	tx_delivery_note 		varchar(256)				NOT NULL,
	tx_delivery_details 	varchar(256)				NOT NULL,
	tx_payment_details 		varchar(256)				NOT NULL,
	
	flt_vat 				float(8)					NOT NULL,
	flt_vat_rate 			float(8)					NOT NULL,
	flt_vat_payable 		float(8)					NOT NULL,
	flt_invoice_amount 		float(8)					NOT NULL,
	flt_total_before_vat 	float(8)					NOT NULL,
	flt_total_amount_due 	float(8)					NOT NULL,
	flt_discount 			float(8)					NOT NULL,
	flt_net_invoice_total 	float(8)					NOT NULL,
	tx_trader_reference_no 	varchar(2048)				NOT NULL,
	id_template_key 			int 					NOT NULL,
	

	CONSTRAINT [pk_doc_audit_key] PRIMARY KEY NONCLUSTERED  
(
	[id_doc_key] ASC,
	[id_doc_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO




