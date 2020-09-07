/* __VER_INFO__  :  */
/******************************************************************************
* Author        : Imamul Hossain
* Date          : Dec 12, 2019
* Description   : Stored Procedure creation to Action in nDOCUTECH.T_DOC_COUNT Table
* SP Name       : ACT_doc_count
******************************************************************************/

#include <nSMART_SQL.h>

#define _SP_NAME 			{nDOCUTECH.ACT_doc_count};
#define _TABLE_NAME			{nDOCUTECH.T_DOC_COUNT};
#define _PRIMARY_KEY		{id_doc_count_key};
#define _VERSION			{id_doc_count_ver};

_DROP_PROC

_CREATE_PROC (_SP_NAME)
	 
	 _SP_PARAM_HEADER

	, @_PRIMARY_KEY						int					= NULL		OUTPUT
	, @_VERSION							int					= NULL		OUTPUT

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
	
	_SP_PARAM_FOOTER
AS
{
	_SP_HEADER

	SELECT @g_tx_err_msg = 'ACTION : ' + @tx_action_name
	_LOG_INFO(@g_tx_err_msg)

	IF @tx_action_name NOT IN (_ACTION_NEW, _ACTION_UPDATE, _ACTION_DELETE)
	{
		SELECT @g_tx_err_msg = _GEM_ACTION + ISNULL(@tx_action_name, _DB_NULL_STR)
		_HANDLE_ERROR(_GEC_ACTION, @g_tx_err_msg)
	}

	_GENERATE_EVENT_KEY

	IF (_ACTION(_ACTION_NEW))
	{
		_GENERATE_KEY(id_doc_count_key)

		EXEC @g_id_return_status = nDOCUTECH.INS_doc_count
							_SP_ARGS_HEADER

							, @_PRIMARY_KEY						= @_PRIMARY_KEY
							, @_VERSION 						= @_VERSION

							, @ct_downloaded 					= @ct_downloaded 
 							, @is_abbyy_processed 				= @is_abbyy_processed
 							, @is_abbyy_error 					= @is_abbyy_error
 							, @is_abbyy_successful 				= @is_abbyy_successful
 							, @tx_sender_address 				= @tx_sender_address 
 							, @tx_document_type					= @tx_document_type	
 							, @tx_download_source				= @tx_download_source
 							, @tx_subject						= @tx_subject	
 							, @tx_file_name						= @tx_file_name

		_RETURN_IF_SP_ERROR(nDOCUTECH.INS_doc_count)

	}

	IF (_ACTION(_ACTION_DELETE))
	{
		_SET_ACTION(_ACTION_UPDATE), @is_active = 0
	}

	IF @tx_action_name IN (_ACTION_UPDATE)	
	{

		_CHECK_NULL_INT(@id_doc_count_key)
		_CHECK_NULL_VERSION(@id_doc_count_key)

		EXEC @g_id_return_status = nDOCUTECH.UPD_doc_count
							 _SP_ARGS_HEADER
							, @_PRIMARY_KEY						= @_PRIMARY_KEY
							, @_VERSION 						= @_VERSION
							, @ct_downloaded 					= @ct_downloaded 
 							, @is_abbyy_processed 				= @is_abbyy_processed
 							, @is_abbyy_error 					= @is_abbyy_error
 							, @is_abbyy_successful 				= @is_abbyy_successful
 							, @tx_sender_address 				= @tx_sender_address 
 							, @tx_document_type					= @tx_document_type	
 							, @tx_download_source				= @tx_download_source
 							, @tx_subject						= @tx_subject	
 							, @tx_file_name						= @tx_file_name

		_RETURN_IF_SP_ERROR(nDOCUTECH.UPD_doc_count)

	}

	IF @tx_action_name IN (_ACTION_SELECT)	
	{


		EXEC @g_id_return_status = nDOCUTECH.SEL_doc_count
							 _SP_ARGS_HEADER
							, @_PRIMARY_KEY						= @_PRIMARY_KEY
							, @_VERSION 						= @_VERSION
							, @ct_downloaded 					= @ct_downloaded 
 							, @is_abbyy_processed 				= @is_abbyy_processed
 							, @is_abbyy_error 					= @is_abbyy_error
 							, @is_abbyy_successful 				= @is_abbyy_successful
 							, @tx_sender_address 				= @tx_sender_address 
 							, @tx_document_type					= @tx_document_type	
 							, @tx_download_source				= @tx_download_source
 							, @tx_subject						= @tx_subject	
 							, @tx_file_name						= @tx_file_name

		_RETURN_IF_SP_ERROR(nDOCUTECH.SEL_doc_count)

	}

	_SP_FOOTER

	RETURN _STATUS_OK

}
go

_GRANT_PERM_SP
