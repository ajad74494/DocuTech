/****** Object:  Table [dbo].[T_GROUP]    Script Date: 18/12/2019 12:18:12 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_GROUP](
	[id_group_key] 				[int] 			NOT NULL,
	[id_group_ver] 				[int] 			NOT NULL,
	[is_active] 				[int] 			NOT NULL,
	[id_env_key] 				[int] 			NOT NULL,
	[id_user_mod_key] 			[int] 			NOT NULL,
	[dtt_mod] 					[datetime] 		NOT NULL,
	[id_event_key] 				[int] 			NOT NULL,
	[id_state_key] 				[int] 			NOT NULL,
	[id_action_key] 			[int] 			NOT NULL,
	[tx_group_name] 			[varchar](64) 	NOT NULL,
	[id_group_type_value_key] 	[int] 			NOT NULL,
	[tx_desc] 					[varchar](2048) NOT NULL,
 CONSTRAINT [pk_group_key] PRIMARY KEY CLUSTERED 
(
	[id_group_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO