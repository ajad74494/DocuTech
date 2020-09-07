


/**
*	Author: Abdullah Al Kafi
*	Desc: Docutech Doc Details Table
*	Object:  Table [nDOCUTECH].[T_DOC_DETAILS]    Script Date: 12/9/2019
**/

DROP TABLE [nDOCUTECH].[T_DOC_DETAILS]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [nDOCUTECH].[T_DOC_DETAILS](
	id_doc_details_key 		int IDENTITY(128000, 1) 	NOT NULL,
	id_doc_details_ver 		int 						NOT NULL,
	is_active 				int 						NOT NULL,
	id_env_key 				int 						NOT NULL,
	id_user_mod_key 		int 						NOT NULL,
	dtt_mod 				datetime 					NOT NULL,
	id_event_key 			int 						NOT NULL,
	id_state_key 			int 						NOT NULL,
	id_action_key 			int 						NOT NULL,
	
	id_doc_key				int 						NOT NULL,
	part_no 				int							NOT NULL,
	item_qty				int							NOT NULL,
	reference_no 			int							NOT NULL,
	tx_item_name 			varchar(256)				NOT NULL,
	tx_item_code 			varchar(256)				NOT NULL,
	tx_rent 				varchar(256)				NOT NULL,
	tx_pack 				varchar(256)				NOT NULL,
	tx_item_description 	varchar(256)				NOT NULL,
	tx_property_address 	varchar(256)				NOT NULL,
	
	flt_trade 				float(8)					NOT NULL,
	flt_unit_price 			float(8)					NOT NULL,
	flt_total_price 		float(8)					NOT NULL,
	flt_net_value 			float(8)					NOT NULL,
	flt_value_of_goods 		float(8)					NOT NULL,
	flt_insurance_premium 	float(8)					NOT NULL,
	
	
	CONSTRAINT [pk_doc_details_key] PRIMARY KEY CLUSTERED 
(
	[id_doc_details_key] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO



