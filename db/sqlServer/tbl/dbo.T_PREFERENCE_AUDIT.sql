/****** Object:  Table [dbo].[T_PREFERENCE_AUDIT]    Script Date: 18/12/2019 10:56:07 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_PREFERENCE_AUDIT](
	[id_pref_key] 					[int] 				NOT NULL,
	[id_pref_ver] 					[int] 				NOT NULL,
	[is_active] 					[int] 				NOT NULL,
	[id_env_key] 					[int] 				NOT NULL,
	[id_user_mod_key] 				[int] 				NOT NULL,
	[dtt_mod] 						[datetime] 			NOT NULL,
	[id_event_key] 					[int] 				NOT NULL,
	[id_pref_type_value_key] 		[int] 				NOT NULL,
	[id_pref_type_value_key_value] 	[int] 				NOT NULL,
	[tx_pref_group] 				[varchar](64) 		NOT NULL,
	[tx_pref_name] 					[varchar](128) 		NOT NULL,
	[tx_pref_value] 				[varchar](512) 		NOT NULL,
	[is_allow_override] 			[int] 				NOT NULL,
	[int_pref_order] 				[int] 				NOT NULL,
	[tx_pref_desc] 					[varchar](2048) 	NOT NULL,
 CONSTRAINT [pk_preference_key_audit] PRIMARY KEY CLUSTERED 
(
	[id_pref_key] ASC,
	[id_pref_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO


