/****** Object:  Table [dbo].[T_TYPE]    Script Date: 18/12/2019 11:45:00 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_TYPE](
	[id_type_key] 			[int] 			NOT NULL,
	[id_type_ver] 			[int] 			NOT NULL,
	[is_active] 			[int] 			NOT NULL,
	[id_env_key] 			[int] 			NOT NULL,
	[id_user_mod_key] 		[int] 			NOT NULL,
	[dtt_mod] 				[datetime] 		NOT NULL,
	[id_event_key] 			[int] 			NOT NULL,
	[id_system_key] 		[int] 			NOT NULL,
	[tx_type_category] 		[varchar](64) 	NOT NULL,
	[tx_type_name] 			[varchar](64) 	NOT NULL,
	[ct_sort_order] 		[int] 			NOT NULL,
	[tx_table_name] 		[varchar](32) 	NOT NULL,
	[tx_desc] 				[varchar](2048) NOT NULL,
 CONSTRAINT [pk_type_key] PRIMARY KEY CLUSTERED 
(
	[id_type_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO