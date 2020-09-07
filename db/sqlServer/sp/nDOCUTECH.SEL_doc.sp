USE [DOCUTECH_DEV]
GO
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_doc]    Script Date: 24/12/2019 10:39:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	ALTER PROC [nDOCUTECH].[SEL_doc]
	  @tx_action_name				varchar(32)				= 'SELECT'

	, @is_active					int						= 1
	, @id_env_key					int						= NULL 	
	, @id_user_mod_key				int						= NULL 	
	, @dtt_mod						datetime				= NULL 
	
	, @id_state_key					int						= NULL 	
	, @id_action_key				int						= NULL 	
	, @dtt_valid_from				datetime				= NULL
	, @dtt_valid_to					datetime				= NULL
	
	, @dtt_as_at					datetime				= NULL
	, @dtt_last_refresh				datetime				= NULL

	, @id_doc_key 			    int 					 	= NULL 	
	, @id_doc_ver 			    int 						= NULL 	
	, @supplier_number 			varchar(32)					= NULL
	, @tx_company_address 		varchar(256)				= NULL
	, @tx_invoice_to 			varchar(256)				= NULL
	, @tx_deliver_to 			varchar(256)				= NULL
	, @tx_currency 				varchar(8)					= NULL
	, @receipt_number 			int 						= NULL
	, @document_number 			int 						= NULL
	, @dt_document				date 						= NULL
	, @tx_association_number 	varchar(256)				= NULL
	, @recipient_of_invoice 	int 						= NULL
	, @vat_number 				varchar(256)				= NULL
	, @tx_invoice_number 		varchar(16) 				= NULL
	, @tx_account_number 		varchar(256)				= NULL
	, @tx_voucher_number 		varchar(256)				= NULL
	, @tx_fax_number 			varchar(128)				= NULL
	, @dt_despatch 				date 						= NULL
	, @delivery_note_no 		int  						= NULL
	, @tx_order_id 				varchar(256)				= NULL
	, @tx_delivery_note 		varchar(256)				= NULL
	, @tx_delivery_details 		varchar(256)				= NULL
	, @flt_net_invoice_total 	float(8)					= NULL
	, @flt_vat 					float(8)					= NULL
	, @flt_vat_rate 			float(8)					= NULL
	, @flt_vat_payable 			float(8)					= NULL
	, @flt_invoice_amount 		float(8)					= NULL
	, @flt_total_before_vat 	float(8)					= NULL
	, @flt_total_amount_due 	float(8)					= NULL
	, @tx_payment_details 		varchar(256)				= NULL
	, @flt_discount 			float(8)					= NULL
	, @is_sage50_processed		bit							= 0
	, @tx_trader_reference_no	 varchar(2048)				= NULL
	, @id_template_key			int 						= NULL
	, @tx_supplier_short_name   varchar(16)					= NULL	


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_DOC'
		, D.*
		, tx_file_name
		FROM	nDOCUTECH.T_DOC D
		LEFT JOIN nDOCUTECH.T_TEMPLATE T ON T.id_template_key = D.id_template_key
		LEFT JOIN nDOCUTECH.T_DOC_COUNT C ON C.id_doc_count_key = T.id_doc_count
		WHERE	id_doc_key			= ISNULL(@id_doc_key, id_doc_key)
		AND		tx_invoice_number	= ISNULL(@tx_invoice_number, tx_invoice_number)
		AND		CAST(D.dtt_mod AS DATE)			>= ISNULL(@dtt_valid_from, CAST(D.dtt_mod AS DATE))
		AND		CAST(D.dtt_mod AS DATE)			<= ISNULL(@dtt_valid_to, CAST(D.dtt_mod AS DATE))
		/*AND		D.dtt_mod			>= ISNULL(@dtt_valid_from, D.dtt_mod)
		AND		D.dtt_mod			<= ISNULL(@dtt_valid_to, D.dtt_mod)*/
	END
END   