/* __VER_INFO__  :  */
/******************************************************************************
* Author        : Imamul Hossain
* Date          : Dec 12, 2019
* Description   : Stored Procedure to update in nDOCUTECH.T_DOC_COUNT Table
* SP Name       : ACT_doc_count
******************************************************************************/

#include <nSMART_SQL.h>

#define _SP_NAME 			{nDOCUTECH.UPD_doc_count};
#define _TABLE_NAME			{nDOCUTECH.T_DOC_COUNT};
#define _PRIMARY_KEY		{id_doc_count_key};
#define _VERSION			{id_doc_count_ver};

_DROP_PROC

_CREATE_PROC(_SP_NAME)
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
	
	_SP_PARAM_FOOTER
AS
{
	_SP_HEADER

	_BEGIN_TRAN

		UPDATE	_TABLE_NAME
		SET		  _VERSION				= _VERSION + 1  
				, _TABLE_HEADER_UPD

				, ct_downloaded 				= ISNULL(@ct_downloaded 			,ct_downloaded)
				, is_abbyy_processed 			= ISNULL(@is_abbyy_processed 		,is_abbyy_processed)
				, is_abbyy_error 				= ISNULL(@is_abbyy_error 			,is_abbyy_error)
				, is_abbyy_successful			= ISNULL(@is_abbyy_successful		,is_abbyy_successful)
				, tx_sender_address 			= ISNULL(@tx_sender_address 		,tx_sender_address)
				, tx_document_type				= ISNULL(@tx_document_type			,tx_document_type)
				, tx_download_source			= ISNULL(@tx_download_source		,tx_download_source)
				, tx_subject			= ISNULL(@tx_subject		,tx_subject)
				, tx_file_name			= ISNULL(@tx_file_name		,tx_file_name)
				
		WHERE	_PRIMARY_KEY	= @_PRIMARY_KEY
		AND		_VERSION		= @_VERSION

		_STORE_SYS_VARS
		SELECT @g_tx_err_msg = _GEM_UPDATE(_TABLE_NAME, @_PRIMARY_KEY)
		_HANDLE_ZERO_ROW_COUNT(_GEC_SELECT, @g_tx_err_msg)

		_UPDATE_VER(@_VERSION)

		_TOUCHED_TABLE(_TABLE_NAME)

	_COMMIT_TRAN

	_SP_FOOTER
}
go

_GRANT_PERM_SP

