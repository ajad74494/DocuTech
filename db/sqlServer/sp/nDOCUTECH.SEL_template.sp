USE [DOCUTECH_DEV]
GO
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_template]    Script Date: 17/12/2019 16:54:14 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	ALTER PROC [nDOCUTECH].[SEL_template]
	  @tx_action_name				varchar(32)					= NULL
	, @id_template_key 				int 					 	= NULL
	, @id_template_ver 				int 						= NULL
	, @is_active 					int 						= NULL
	, @id_env_key 					int 						= NULL
	, @id_user_mod_key 				int 						= NULL
	, @dtt_mod 						datetime 					= NULL
	, @id_event_key 				int 						= NULL
	, @id_state_key 				int 						= NULL
	, @id_action_key 				int 						= NULL
	, @is_send_to_nuxeo				int 						= NULL
	, @is_template_prepared 		int 						= NULL
	, @is_parsed_successfully		int 						= NULL
	, @tx_pdf_type 					varchar(8) 					= NULL
	, @tx_processor_type			varchar(8)					= NULL
	, @tx_client_name 				varchar(64)					= NULL
	, @tx_supplier_name 			varchar(64)					= NULL
	, @tx_doc_source 				varchar(64)					= NULL
	, @tx_received_doc_id 			varchar(64)					= NULL
	, @tx_doc_type 					varchar(256)				= NULL
	, @id_doc_count					int							= NULL
	, @tx_file_name					varchar(256)				= NULL
	, @dtt_valid_from				datetime					= NULL
	, @dtt_valid_to					datetime					= NULL
	, @dtt_from						datetime					= NULL
	, @dtt_to						datetime					= NULL


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
		, *
		FROM nDOCUTECH.T_TEMPLATE T
		JOIN nDOCUTECH.T_DOC_COUNT C ON C.id_doc_count_key = T.id_doc_count
		LEFT JOIN nDOCUTECH.T_TEMPLATE_EXCEPTION E ON E.id_template_key = T.id_template_key
		WHERE	T.is_active			= 1
		AND		C.is_active			= 1		
		AND		id_doc_count		= ISNULL(@id_doc_count, id_doc_count)
		AND		C.tx_file_name		= ISNULL(@tx_file_name, C.tx_file_name)
		/*AND		T.dtt_mod			>= ISNULL(@dtt_valid_from, T.dtt_mod)
		AND		T.dtt_mod			<= ISNULL(@dtt_valid_to, T.dtt_mod)*/
		AND		CAST(T.dtt_mod AS DATE)			>= ISNULL(@dtt_from, CAST(T.dtt_mod AS DATE))
		AND		CAST(T.dtt_mod AS DATE)			<= ISNULL(@dtt_to, CAST(T.dtt_mod AS DATE))
	END
	
	IF (@tx_action_name = 'SELECT_ID')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
		, T.*		
		FROM nDOCUTECH.T_TEMPLATE T
		JOIN nDOCUTECH.T_DOC_COUNT C ON C.id_doc_count_key = T.id_doc_count
		WHERE	T.is_active			= 1
		AND		C.is_active			= 1		
		AND		id_doc_count		= ISNULL(@id_doc_count, id_doc_count)
		AND		C.tx_file_name		= ISNULL(@tx_file_name, C.tx_file_name)
		AND		is_send_to_nuxeo	= ISNULL(@is_send_to_nuxeo, is_send_to_nuxeo)
	END

END
