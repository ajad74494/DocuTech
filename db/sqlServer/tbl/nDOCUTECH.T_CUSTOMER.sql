


/******
	Author: Md. Mahbub Hasan
	Desc: Docutech Customer Table
	Object:  Table [nDOCUTECH].[T_CUSTOMER]    Script Date: 9/9/2019
******/

DROP TABLE [nDOCUTECH].[T_CUSTOMER]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_CUSTOMER](
	id_customer_key 		int IDENTITY(100000, 1) 	NOT NULL,
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
	
	CONSTRAINT [pk_customer_key] PRIMARY KEY CLUSTERED 
(
	[id_customer_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO