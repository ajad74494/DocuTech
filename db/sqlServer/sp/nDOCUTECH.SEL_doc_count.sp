/* __VER_INFO__  :  */
/******************************************************************************
* Author        : Imamul Hossain
* Date          : Dec 12, 2019
* Description   : Stored Procedure to select from nDOCUTECH.T_DOC_COUNT Table
* SP Name       : ACT_doc_count
******************************************************************************/

#include <nSMART_SQL.h>

#define _SP_NAME 			{nDOCUTECH.SEL_doc_count};
#define _PRIMARY_KEY		{id_doc_count_key};
#define _VERSION			{id_doc_count_ver};

_DROP_PROC

_CREATE_PROC (_SP_NAME)
(
		 _SP_PARAM_HEADER

		, @_PRIMARY_KEY			int								OUTPUT
		, @_VERSION				int								OUTPUT
		
		, @ct_downloaded 					int					= NULL
		, @is_abbyy_processed 				int					= NULL
		, @is_abbyy_error 					int					= NULL
		, @is_abbyy_successful 				int					= NULL
		, @tx_sender_address 				varchar(256)		= NULL
		, @tx_download_source				varchar(16)			= NULL
		, @tx_document_type					varchar(32)			= NULL
		, @tx_subject						varchar(256)		= NULL
		, @tx_file_name						varchar(128)		= NULL
		, @is_max							int					= NULL
)
AS
BEGIN
	IF((@is_max = 0 OR @is_max IS NULL) AND @tx_action_name = 'SELECT') --@tx_action_name IN ('SELECT')	
	BEGIN
	
		SELECT tx_rs_type = 'RS_TYPE_DOC_COUNT'
		, *
		FROM nDOCUTECH.T_DOC_COUNT
		WHERE is_active = 1
		AND id_doc_count_key		= ISNULL(@id_doc_count_key, id_doc_count_key)
		AND tx_sender_address		= ISNULL(@tx_sender_address, tx_sender_address)
		AND is_abbyy_processed		= ISNULL(@is_abbyy_processed, is_abbyy_processed)
		AND is_abbyy_error			= ISNULL(@is_abbyy_error, is_abbyy_error)
		AND is_abbyy_successful		= ISNULL(@is_abbyy_successful, is_abbyy_successful)
		AND tx_file_name			= ISNULL(@tx_file_name, tx_file_name)
		AND dtt_mod					>= ISNULL(@dtt_mod, dtt_mod)
		
		--AND CONVERT(DATE, dtt_mod)	= ISNULL(@dtt_mod , CONVERT(DATE, dtt_mod))
	END
	IF(@is_max = 1 AND @tx_action_name = 'SELECT')
	BEGIN
		SELECT TOP 1 tx_rs_type = 'RS_TYPE_DOC_COUNT'
		, dtt_mod
		--, *
		FROM nDOCUTECH.T_DOC_COUNT
		WHERE is_active = 1
		ORDER BY dtt_mod DESC
	END
	
	IF(@tx_action_name = 'SEQUENCE')
	BEGIN
		DECLARE @l_seq bigint
		SELECT @l_seq		= NEXT VALUE FOR nDOCUTECH.id_file_download
		   
		SELECT tx_rs_type	= 'RS_TYPE_DOC_COUNT'
		, id_file_download	= @l_seq
	END

	IF(@tx_action_name = 'SENT_TIME')
	BEGIN
		SELECT tx_rs_type	= 'RS_TYPE_DOC_COUNT'
		, dtt_file_sent = CONVERT(datetime, REPLACE(SUBSTRING(tx_subject, 1, 17), '_', ' '), 120)
		FROM nDOCUTECH.T_DOC_COUNT
	END
	
END
go

_GRANT_PERM_SP