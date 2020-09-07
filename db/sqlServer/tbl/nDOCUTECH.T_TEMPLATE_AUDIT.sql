


/**
*	Author: Abdullah Al Kafi
*	Desc: Docutech Template Audit Table
*	Object:  Table [nDOCUTECH].[T_TEMPLATE_AUDIT]    Script Date: 12/9/2019
**/

DROP TABLE [nDOCUTECH].[T_TEMPLATE_AUDIT]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_TEMPLATE_AUDIT](
	id_template_key 			int 					 	NOT NULL,
	id_template_ver 			int 						NOT NULL,
	is_active 					int 						NOT NULL,
	id_env_key 					int 						NOT NULL,
	id_user_mod_key 			int 						NOT NULL,
	dtt_mod 					datetime 					NOT NULL,
	id_event_key 				int 						NOT NULL,
	id_state_key 				int 						NOT NULL,
	id_action_key 				int 						NOT NULL,
	
	is_send_to_nuxeo			int 						NOT NULL,
	is_template_prepared 		int 						NOT NULL,
	is_parsed_successfully		int 						NOT NULL,
	tx_pdf_type 				varchar(8) 					NOT NULL,
	tx_processor_type			varchar(8)					NOT NULL,
	tx_client_name 				varchar(64)					NOT NULL,
	tx_supplier_name 			varchar(64)					NOT NULL,
	tx_doc_source 				varchar(64)					NOT NULL,
	tx_received_doc_id 			varchar(64)					NOT NULL,
	tx_doc_type					varchar(256)				NOT NULL,
	id_doc_count				int 						NOT NULL,

	CONSTRAINT [pk_template_audit_key] PRIMARY KEY NONCLUSTERED 
(
	[id_template_key] ASC,
	[id_template_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO