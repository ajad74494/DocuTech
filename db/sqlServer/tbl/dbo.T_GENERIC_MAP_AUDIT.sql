
/****** Object:  Table [dbo].[T_GENERIC_MAP_AUDIT]    Script Date: 18/12/2019 11:54:54 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_GENERIC_MAP_AUDIT](
	[id_generic_map_key] 			[int] 			NOT NULL,
	[id_generic_map_ver] 			[int] 			NOT NULL,
	[is_active] 					[int] 			NOT NULL,
	[id_env_key] 					[int] 			NOT NULL,
	[id_user_mod_key] 				[int] 			NOT NULL,
	[dtt_mod] 						[datetime] 		NOT NULL,
	[id_event_key] 					[int] 			NOT NULL,
	[id_state_key] 					[int] 			NOT NULL,
	[id_action_key] 				[int] 			NOT NULL,
	[dtt_valid_from] 				[datetime] 		NOT NULL,
	[dtt_valid_to] 					[datetime] 		NOT NULL,
	[id_from_type_key] 				[int] 			NOT NULL,
	[id_from_key] 					[int] 			NOT NULL,
	[id_from_key_ver] 				[int] 			NOT NULL,
	[id_to_type_key] 				[int] 			NOT NULL,
	[id_to_key] 					[int] 			NOT NULL,
	[id_to_key_ver] 				[int] 			NOT NULL,
	[is_primary] 					[int] 			NOT NULL,
	[ct_sort_order] 				[int] 			NOT NULL,
	[tx_map_desc] 					[varchar](2048) NOT NULL,
 CONSTRAINT [pk_generic_map_audit_key_global] PRIMARY KEY NONCLUSTERED 
(
	[id_generic_map_key] ASC,
	[id_generic_map_ver] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO
