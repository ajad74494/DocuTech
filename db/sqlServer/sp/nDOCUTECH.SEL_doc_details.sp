
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_doc_details]    Script Date: 9/11/2019 6:11:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	CREATE PROC [nDOCUTECH].[SEL_doc_details]
	  @tx_action_name				varchar(32)				= NULL

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

	, @id_doc_details_key 		int 					 		= NULL 	
	, @id_doc_details_ver 		int 							= NULL 	
	, @tx_item_name 			varchar(256)					= NULL
	, @tx_item_code 			varchar(256)					= NULL
	, @tx_item_description 		varchar(256)					= NULL
	, @ct_part_no 				int								= NULL
	, @ct_qty 					int								= NULL
	, @flt_unit_price 			float(8)						= NULL
	, @flt_total_price 			float(8)						= NULL
	, @flt_value_of_goods 		float(8)						= NULL
	, @tx_property_address 		varchar(256)					= NULL
	, @tx_rent 					varchar(256)					= NULL
	, @flt_insurance_premium 	float(8)						= NULL
	, @flt_net_value 			float(8)						= NULL
	, @ct_reference 			int								= NULL
	, @tx_pack 					varchar(256)					= NULL
	, @flt_trade 				float(8)						= NULL
	, @id_doc_key				int 							= NULL


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
				, *	
		FROM	nDOCUTECH.T_DOC_DETAILS
		WHERE id_doc_key = ISNULL(@id_doc_key, id_doc_key)		
		
	END

END                    
