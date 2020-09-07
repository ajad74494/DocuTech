


/**
*	Author: Abdullah Al Kafi
*	Desc: Docutech Customer Audit Table
*	Object:  Table [nDOCUTECH].[T_CUSTOMER_AUDIT]    Script Date: 12/9/2019
**/

DROP TABLE [nDOCUTECH].[T_CUSTOMER_AUDIT]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_CUSTOMER_AUDIT](
	id_customer_key 		int 					 	NOT NULL,
	id_customer_ver 		int 						NOT NULL,
	is_active 				int 						NOT NULL,
	id_env_key 				int 						NOT NULL,
	id_user_mod_key 		int 						NOT NULL,
	dtt_mod 				datetime 					NOT NULL,
	id_event_key 			int 						NOT NULL,
	id_state_key 			int 						NOT NULL,
	id_action_key 			int 						NOT NULL,
	
	id_doc_key			    int 						NOT NULL,
	tx_customer_number 		varchar(256)				NOT NULL,
	tx_contact_number 		varchar(256)				NOT NULL,
	tx_customer_order_no 	varchar(256)				NOT NULL,
	tx_customer_address 	varchar(256)				NOT NULL,
	
	
	CONSTRAINT [pk_customer_audit_key] PRIMARY KEY NONCLUSTERED 
(
	[id_customer_key] ASC,
	[id_customer_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO