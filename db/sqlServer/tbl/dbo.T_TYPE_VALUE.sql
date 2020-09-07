/****** Object:  Table [dbo].[T_TYPE_VALUE]    Script Date: 18/12/2019 11:46:47 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[T_TYPE_VALUE](
	[id_type_value_key] 		[int] 			NOT NULL,
	[id_type_value_ver] 		[int] 			NOT NULL,
	[is_active] 				[int] 			NOT NULL,
	[id_env_key] 				[int] 			NOT NULL,
	[id_user_mod_key] 			[int] 			NOT NULL,
	[dtt_mod] 					[datetime] 		NOT NULL,
	[id_event_key] 				[int] 			NOT NULL,
	[id_i8ln_key] 				[int] 			NOT NULL,
	[id_type_key] 				[int] 			NOT NULL,
	[tx_type_value] 			[varchar](512) 	NOT NULL,
	[int_sort_order] 			[int] 			NOT NULL,
	[tx_desc] 					[varchar](1024) NOT NULL,
 CONSTRAINT [pk_type_value_key] PRIMARY KEY CLUSTERED 
(
	[id_type_value_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO