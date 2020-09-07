
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_customer]    Script Date: 9/11/2019 6:11:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	CREATE PROC [nDOCUTECH].[SEL_customer]
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

	, @id_customer_key 			int 				= NULL 	
	, @id_customer_ver 			int 				= NULL 	
	, @id_doc_key			    int 				= NULL 	
	, @tx_customer_number 		varchar(256)		= NULL
	, @tx_contact_number 		varchar(256)		= NULL
	, @tx_customer_order_no 	varchar(256)		= NULL
	, @tx_customer_address 		varchar(256)		= NULL	


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE'
				, *	
		FROM	nDOCUTECH.T_CUSTOMER		
		
	END

END                    
