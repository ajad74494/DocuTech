


/******
	Author: Md. Asadullahill Galib
	Desc: Docutech Doc Count Audit Table
	Object:  Table [nDOCUTECH].[T_DOC_COUNT_AUDIT]    Script Date: 08/12/2019
******/

DROP TABLE [nDOCUTECH].[T_DOC_COUNT_AUDIT]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_DOC_COUNT_AUDIT](
	  id_doc_count_key 		int 						NOT NULL
	, id_doc_count_ver 		int 						NOT NULL
	, is_active 			int 						NOT NULL
	, id_env_key 			int 						NOT NULL
	, id_user_mod_key 		int 						NOT NULL
	, dtt_mod 				datetime 					NOT NULL
	, id_event_key 			int 						NOT NULL
	, id_state_key 			int 						NOT NULL
	, id_action_key 		int 						NOT NULL
	
	, ct_downloaded 		int							NOT NULL
	, is_abbyy_processed 	int							NOT NULL
	, is_abbyy_error 		int							NOT NULL
	, is_abbyy_successful 	int							NOT NULL
	, tx_sender_address 	varchar(256)				NOT NULL
	, tx_download_source	varchar(16)					NOT NULL
	, tx_document_type		varchar(32)					NOT NULL
	, tx_subject			varchar(256)				NOT NULL
	, tx_file_name			varchar(128)				NOT NULL	
	
	CONSTRAINT [pk_doc_count_audit_key] PRIMARY KEY NONCLUSTERED 
(
	[id_doc_count_key] ASC,
	[id_doc_count_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO