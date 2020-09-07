/* __VER_INFO__  :  */
/******************************************************************************
* Author        : Imamul Hossain
* Date          : Dec 12, 2019
* Description   : Stored Procedure to Insert in nDOCUTECH.T_DOC_COUNT Table
* SP Name       : nDOCUTECH.INS_doc_count
******************************************************************************/

#include <nSMART_SQL.h>

#define _SP_NAME				{nDOCUTECH.INS_doc_count};
#define _TABLE_NAME 			{nDOCUTECH.T_DOC_COUNT};
#define _PRIMARY_KEY			{id_doc_count_key};
#define _VERSION				{id_doc_count_ver};
 
_DROP_PROC

_CREATE_PROC (_SP_NAME)

		 _SP_PARAM_HEADER		
			
		, @_PRIMARY_KEY						int					OUTPUT
		, @_VERSION							int					OUTPUT

		, @ct_downloaded 					int					= NULL
		, @is_abbyy_processed 				int					= NULL
		, @is_abbyy_error 					int					= NULL
		, @is_abbyy_successful 				int					= NULL
		, @tx_sender_address 				varchar(256)		= NULL
		, @tx_download_source				varchar(16)			= NULL
		, @tx_document_type					varchar(32)			= NULL
		, @tx_subject						varchar(256)		= NULL
		, @tx_file_name						varchar(128)		= NULL

		_SP_PARAM_FOOTER     																
	AS	                       
	{                          	
		
	_SP_HEADER   

    _INIT_VERSION (@id_doc_count_ver)

	_BEGIN_TRAN

		INSERT INTO _TABLE_NAME
		(
						
			  _PRIMARY_KEY				
			, _VERSION					
			, _TABLE_HEADER_INS_FIELD_WITH_STATE
			, ct_downloaded
			, is_abbyy_processed
			, is_abbyy_error
			, is_abbyy_successful
			, tx_sender_address
			, tx_download_source
			, tx_document_type
			, tx_subject
			, tx_file_name
				
		)
		VALUES
		(
			  @id_doc_count_key
			, @id_doc_count_ver
			, _TABLE_HEADER_INS_VAL_WITH_STATE
			, ISNULL(@ct_downloaded			, 0)
			, ISNULL(@is_abbyy_processed			, 0)
			, ISNULL(@is_abbyy_error			, 0)
			, ISNULL(@is_abbyy_successful	, 0)
			, ISNULL(@tx_sender_address				, _DB_NULL_STR)
			, ISNULL(@tx_download_source			, _DB_NULL_STR)
			, ISNULL(@tx_document_type				, _DB_NULL_STR)
			, ISNULL(@tx_subject				, _DB_NULL_STR)
			, ISNULL(@tx_file_name				, _DB_NULL_STR)
		)

		_STORE_SYS_VARS		
		SELECT @g_tx_err_msg = _GEM_INSERT(_TABLE_NAME, @id_doc_count_key)
		_HANDLE_ZERO_ROW_COUNT(_GEC_INSERT, @g_tx_err_msg)

		_TOUCHED_TABLE(_TABLE_NAME)

	_COMMIT_TRAN

	_SP_FOOTER

	RETURN _STATUS_OK
}
go

_GRANT_PERM_SP
