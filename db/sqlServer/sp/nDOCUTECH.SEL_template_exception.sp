
/****** Object:  StoredProcedure [nDOCUTECH].[SEL_template_exception]    Script Date: 9/11/2019 6:11:44 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	CREATE PROC [nDOCUTECH].[SEL_template_exception]
	  @tx_action_name				varchar(32)				= NULL

	, @is_active					int						= 1
	, @id_user_mod_key				int						= NULL 	OUTPUT
	, @dtt_mod						datetime				= NULL 	

	, @id_template_exception_key 	int 					 	= NULL 	OUTPUT
	, @id_template_exception_ver 	int 						= NULL 	OUTPUT
	, @id_template_key 				int 						= NULL 	OUTPUT
	, @tx_error_issue 				varchar(256)				= NULL
	, @tx_error_issue_source 		varchar(256)				= NULL


AS

BEGIN
	
	IF (@tx_action_name = 'SELECT')
	BEGIN
		SELECT tx_rs_type = 'RS_TYPE_TEMPLATE_EXCEPTION'
				, *	
		FROM	nDOCUTECH.T_TEMPLATE_EXCEPTION		
		
	END

END                    
