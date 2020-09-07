


/**
*	Author: Abdullah Al Kafi
*	Desc: Docutech Template Exception Table
*	Object:  Table [nDOCUTECH].[T_TEMPLATE_EXCEPTION]    Script Date: 12/9/2019
**/

DROP TABLE [nDOCUTECH].[T_TEMPLATE_EXCEPTION]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_TEMPLATE_EXCEPTION](
	id_template_exception_key 	int IDENTITY(100000, 1) 	NOT NULL,
	id_template_exception_ver 	int 						NOT NULL,
	is_active 					int 						NOT NULL,
	id_env_key 					int 						NOT NULL,
	id_user_mod_key 			int 						NOT NULL,
	dtt_mod 					datetime 					NOT NULL,
	id_event_key 				int 						NOT NULL,
	id_state_key 				int 						NOT NULL,
	id_action_key 				int 						NOT NULL,
	
	id_template_key 			int 						NOT NULL,
	tx_error_issue 				varchar(256)				NOT NULL,
	tx_error_issue_source 		varchar(256)				NOT NULL,
	
	CONSTRAINT [pk_template_exception_key] PRIMARY KEY CLUSTERED 
(
	[id_template_exception_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO