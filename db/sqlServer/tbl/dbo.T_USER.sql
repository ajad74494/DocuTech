
/****** Object:  Table [dbo].[T_USER]    Script Date: 18/12/2019 12:04:14 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_USER](
	[id_user_key] 					[int] 			NOT NULL,
	[id_user_ver] 					[int] 			NOT NULL,
	[is_active] 					[int] 			NOT NULL,
	[id_env_key] 					[int] 			NOT NULL,
	[id_user_mod_key] 				[int] 			NOT NULL,
	[dtt_mod] 						[datetime] 		NOT NULL,
	[id_event_key] 					[int] 			NOT NULL,
	[id_state_key] 					[int] 			NOT NULL,
	[id_action_key] 				[int] 			NOT NULL,
	[id_region_key] 				[int] 			NOT NULL,
	[id_group_key] 					[int] 			NOT NULL,
	[id_legal_entity_key]	 		[int] 			NOT NULL,
	[tx_first_name] 				[varchar](64) 	NOT NULL,
	[tx_last_name] 					[varchar](64) 	NOT NULL,
	[tx_login_name] 				[varchar](32) 	NOT NULL,
	[tx_password] 					[varchar](8000) NOT NULL,
	[tx_user_alias] 				[varchar](32) 	NOT NULL,
	[is_disabled] 					[int] 			NOT NULL,
	[is_allow_login] 				[int] 			NOT NULL,
	[is_first_login] 				[int] 			NOT NULL,
	[id_created_by_key] 			[int] 			NOT NULL,
	[tx_email] 						[varchar](64) 	NOT NULL,
 CONSTRAINT [pk_user_key] PRIMARY KEY CLUSTERED 
(
	[id_user_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO
