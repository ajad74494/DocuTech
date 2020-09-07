Ext.define('Desktop.view.doc.DocForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.docForm',
	
	layout : 'border',
	border : true,
	closable : true,
	scrollable: true,

	requires: [
	],

	controller: 'doc-doc',
	viewModel: {
		type: 'doc-doc'
	},

	layout: 
	{
		type: 'fit',
		align: 'middle',
		pack: 'center'
	},

	initComponent: function() {

		var me = this;

		Ext.applyIf(me, {
		  
			items: [
				{
					xtype: 'form',
					bodyPadding: 5,
					reference: 'docForm',
					border: 'true',
					bind: {
						scrollable: 'true'
					},
					items: [
						{
							xtype: 'panel',
							autoScroll: true,
							bodyPadding: 10,
							dockedItems:[
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										'->',
										{
											xtype: 'button',
											text: 'Update',
											reference: 'documentUpdateBtn',
											iconCls: 'approve_icon',
											style: 'border: groove',
											listeners: {
												click :  'docUpdate',
											}
										},
										{
											xtype: 'button',
											text: 'Reset',
											reference: 'formReset',
											handler: 'onFormReset',
											hidden: true
										},
										{
											xtype: 'button',
											text: 'Save',
											hidden: true,
											iconCls: 'save-button-icon',
											reference: 'documentSaveBtn',
											listeners: {
												click :  'docUpdate',
											}
										}
									]
								}
							],
							items: [
								{
									xtype: 'fieldset',
									collapsible: 'true',
									title: 'Numbers',
									defaults: {
										anchor: '100%'
									},
									items: [
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'numberfield',
													fieldLabel: 'Doc ID',
													name: 'docId',
													reference: 'docId',
													labelStyle: 'font-weight:bold',
													columnWidth: .25,
													margin: '0 5 10 0',
													hidden: true
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Doc Version',
													name: 'docVer',
													reference: 'docVer',
													labelStyle: 'font-weight:bold',
													columnWidth: .25,
													hidden: true
												},
												{
													xtype: 'datefield',
													fieldLabel: 'Modified On',
													name: 'modifiedOn',
													reference: 'modifiedOn',
													labelStyle: 'font-weight:bold',
													columnWidth: .25,
													hidden: true
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Supplier Number',
													name: 'supplierNumber',
													reference: 'supplierNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold',
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Receipt Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',	
													name: 'receiptNo',
													reference: 'receiptNo',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Document Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'documentNumber',
													reference: 'documentNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Association Number',
													name: 'associationNumber',
													reference: 'associationNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												}
											]
										},
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'textfield',
													fieldLabel: 'Account Number',
													name: 'accountNumber',
													reference: 'accountNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Voucher Number',
													name: 'voucherNumber',
													reference: 'voucherNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Fax Number',
													name: 'faxNumber',
													reference: 'faxNumber',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Order Reference',
													name: 'orderId',
													reference: 'orderId',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												}
											]
										}
									]
								},
								{
									xtype: 'fieldset',
									collapsible: 'true',
									title: 'Invoice',
									defaults: {
										anchor: '100%'
									},
									items: [
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'textfield',
													fieldLabel: 'Invoice Number',
													name: 'invoiceNumber',
													reference: 'invoiceNumber',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Recipient Of Invoice',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'recipientOfInvoice',
													reference: 'recipientOfInvoice',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Invoice Amount',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'invoiceAmount',
													reference: 'invoiceAmount',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Net Invoice Total',
													name: 'netInvoiceTotal',
													reference: 'netInvoiceTotal',
													columnWidth: .25,
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												}
											]
										}
									]
								},
								{
									xtype: 'fieldset',
									collapsible: 'true',
									title: 'VAT & Others',
									defaults: {
										anchor: '100%'
									},
									items: [
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'textfield',
													fieldLabel: 'Currency',
													name: 'currency',
													reference: 'currency',
													columnWidth: .25,
													margin: '5 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Discount',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'discount',
													reference: 'discount',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Total Before VAT',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'totalBeforeVat',
													reference: 'totalBeforeVat',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'VAT Number',
													name: 'vatNumber',
													reference: 'vatNumber',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												}
											]
										},
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'numberfield',
													fieldLabel: 'VAT',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'vat',
													reference: 'vat',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'VAT Rate',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'vatRate',
													reference: 'vatRate',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'VAT Payable',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'vatPayable',
													reference: 'vatPayable',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Total Amount Due',
													hideTrigger: true,
													mouseWheelEnabled: false,
													emptyText: 'Number',
													name: 'totalAmountDue',
													reference: 'totalAmountDue',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												}
											]
										},
										{
											xtype: 'fieldcontainer',
											columnWidth: 1,
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'datefield',
													fieldLabel: 'Document Date',
													name: 'documentDate',
													reference: 'documentDate',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold',
													value: new Date()
												},
												{
													xtype: 'datefield',
													fieldLabel: 'Despatch Date',
													name: 'despatchDate',
													reference: 'despatchDate',
													columnWidth: .25,
													margin: '4 15 0 0',
													labelStyle: 'font-weight:bold',
													value: new Date()
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Delivery Note No',
													hideTrigger: true,
													mouseWheelEnabled: false,
													name: 'deliveryNoteNo',
													reference: 'deliveryNoteNo',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold',
									                msgTarget: 'side',
									                emptyText: 'Number'	
												}
											]
										}
									]
								},
								{
									xtype: 'fieldset',
									collapsible: 'true',
									title: 'Notes & Details',
									defaults: {
										anchor: '100%'
									},
									items: [
										{
											xtype: 'fieldcontainer',
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'textarea',
													fieldLabel: 'Delivery Note',
													name: 'deliveryNote',
													reference: 'deliveryNote',
													width: '30%',
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textarea',
													fieldLabel: 'Delivery Details',
													name: 'deliveryDetails',
													reference: 'deliveryDetails',
													width: '30%',
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textarea',
													fieldLabel: 'Deliver To',
													name: 'deliverTo',
													reference: 'deliverTo',
													labelStyle: 'font-weight:bold',
													width: '30%',
													margin: '0 15 0 0'
												}
											]
										},
										{
											xtype: 'fieldcontainer',
											layout: {
												type: 'column',
												align: 'stretch'
											},
											padding: '0 0 0 10',
											items:[
												{
													xtype: 'textarea',
													fieldLabel: 'Company Address',
													name: 'companyAddress',
													reference: 'companyAddress',
													labelStyle: 'font-weight:bold',
													width: '30%',
													margin: '0 15 0 0'
												},
												{
													xtype: 'textarea',
													fieldLabel: 'Payment Details',
													name: 'paymentDetails',
													reference: 'paymentDetails',
													width: '30%',
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textarea',
													fieldLabel: 'Invoice To',
													name: 'invoiceTo',
													reference: 'invoiceTo',
													labelStyle: 'font-weight:bold',
													width: '30%',
													margin: '0 15 0 0'
												}
											]
										}
									]
								},
								{
									xtype: 'fieldset',
									collapsible: 'true',
									collapsed: 'true',
									title: 'Doc Details',
									defaults: {
										anchor: '100%'
									},
									items:[
										{
											xtype: 'panel',
											layout: 'fit',
											bodyPadding: 5,
											items:[
												{
													reference:'docDetailsGridPanel',
													xtype: 'gridpanel',
													stripeRows : true,
													columnLines: true,
													scrollable: true,
													title: 'Doc Details',
													header:{
														titlePosition: 0,
														items:[
															{
																xtype: 'textfield',
																reference: 'docDetailsGridFilter',
																fieldLabel: 'Filter',
																padding: 3,
																left: '6px',
																width: 200,
																labelWidth: 35,
																fieldStyle: 'min-height: 18px;',
																listeners: {
																	change: 'onDocDetailsGridFilter'
																},
															},
															{
																xtype:'button',
																iconCls: 'add-button-icon',
																text: 'Add Doc Details',
																//margin: '0 0 0 10',
																listeners:{
																	click: 'onAddNewDocDetails'
																}
															}
														]
													},
													//margin: '15 0 0 0',
													bind: 'docDetailsStore',
													features: [
														{
															ftype: 'grouping',
															groupHeaderTpl: '{name} ({children.length})',
															startCollapsed: true
														}
													],
													columns: [
														{
															dataIndex: 'docDetailsId',
															text: 'Doc Details ID',
															sortable: true,
															hidden: true,
															filter: {
																type: 'number'
															},
															width : 150,
															align: 'center'
														},
														{
															dataIndex: 'docDetailsVer',
															text: 'Doc Details Version',
															sortable: true,
															hidden: true,
															filter: {
																type: 'number'
															},
															width : 150,
															align: 'center'
														},
														{
															dataIndex: 'docId',
															text: 'Doc ID', 
															sortable: true,
															hidden: true,
															filter: {
																type: 'number'
															},
															width : 150,
															align: 'center'
														},
														{
															dataIndex: 'itemName',
															text: 'Item Name',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 108,
															align: 'center'
														},
														{
															dataIndex: 'itemCode',
															text: 'Item Code',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 105,
															align: 'center'
														},
														{
															dataIndex: 'partNo',
															text: 'Part No',
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 80,
															align: 'center'
														},
														{
															dataIndex: 'itemQty',
															text: 'Item Quantity',
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 120,
															align: 'center'
														},
														{
															dataIndex: 'referenceNo',
															text: 'Reference No',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 120,
															align: 'center'
														},
														{
															dataIndex: 'rent',
															text: 'Rent',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 65,
															align: 'center'
														},
														{
															dataIndex: 'pack',
															text: 'Pack',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 65,
															align: 'center'
														},
														{
															dataIndex: 'itemDescription',
															text: 'Item Description',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 140,
															align: 'center'
														},
														{
															dataIndex: 'propertyAddress',
															text: 'Property Address',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 140,
															align: 'center' 
														},
														{
															dataIndex: 'trade',
															text: 'Trade',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 73,
															align: 'center'
														},
														{
															dataIndex: 'unitPrice',
															text: 'Unit Price',
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 110,
															align: 'right'
														},
														{
															dataIndex: 'totalPrice',
															text: 'Total Price',
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 110,
															align: 'right'
														},
														{
															dataIndex: 'netValue',
															text: 'Net Value', 
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 110,
															align: 'right'
														},
														{
															dataIndex: 'valueOfGoods',
															text: 'Value of Goods',
															sortable: true,
															filter: {
																type: 'number'
															},
															width : 140,
															align: 'right'
														},
														{
															dataIndex: 'insurancePremium',
															text: 'Insurance Premium',
															sortable: true,
															filter: {
																type: 'string'
															},
															width : 150,
															align: 'center'
														},
														{
															xtype: 'datecolumn',
															format: 'Y-m-d',
															dataIndex: 'datefield',
															text: 'Modified On',
															sortable: true,
															hidden: true,
															filter: {
																type: 'date'
															},
															width : 120,
															align: 'center'
														}
													],
													listeners: {
														itemdblclick: 'onDocDetailsDblClk',
													},
													plugins: [
														{
															ptype: 'gridfilters'
														}
													],
													viewConfig : {
														enableTextSelection : true
													},
													selModel: {
														selType: 'checkboxmodel'
													}
												}
											]
										}
									]
								}
							]
						}/*,
						{
							xtype: 'panel',
							layout: 'fit',
							bodyPadding: 5,
							items:[
								{
									reference:'docDetailsGridPanel',
									xtype: 'gridpanel',
									stripeRows : true,
									columnLines: true,
									scrollable: true,
									title: 'Doc Details',
									header:{
										titlePosition: 0,
										items:[
											{
												xtype: 'textfield',
												reference: 'docDetailsGridFilter',
												fieldLabel: 'Filter',
												padding: 3,
												left: '6px',
												width: 200,
												labelWidth: 30,
												fieldStyle: 'min-height: 18px;',
												listeners: {
													change: 'onDocDetailsGridFilter'
												},
											},
											{
												xtype:'button',
												iconCls: 'add',
												text: 'Add Doc Details',
												style: 'background-color: grey;',
												margin: '0 0 0 10',
												listeners:{
													click: 'onAddNewDocDetails'
												}
											}
										]
									},
									//margin: '15 0 0 0',
									bind: 'docDetailsStore',
									features: [
										{
											ftype: 'grouping',
											groupHeaderTpl: '{name} ({children.length})',
											startCollapsed: true
										}
									],
									columns: [
										{
											dataIndex: 'docDetailsId',
											text: 'Doc Details ID',
											sortable: true,
											hidden: true,
											filter: {
												type: 'number'
											},
											width : 150,
											align: 'center'
										},
										{
											dataIndex: 'docDetailsVer',
											text: 'Doc Details Version',
											sortable: true,
											hidden: true,
											filter: {
												type: 'number'
											},
											width : 150,
											align: 'center'
										},
										{
											dataIndex: 'docId',
											text: 'Doc ID', 
											sortable: true,
											hidden: true,
											filter: {
												type: 'number'
											},
											width : 150,
											align: 'center'
										},
										{
											dataIndex: 'itemName',
											text: 'Item Name',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 108,
											align: 'center'
										},
										{
											dataIndex: 'itemCode',
											text: 'Item Code',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 105,
											align: 'center'
										},
										{
											dataIndex: 'partNo',
											text: 'Part No',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 80,
											align: 'center'
										},
										{
											dataIndex: 'itemQty',
											text: 'Item Quantity',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 120,
											align: 'center'
										},
										{
											dataIndex: 'referenceNo',
											text: 'Reference No',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 120,
											align: 'center'
										},
										{
											dataIndex: 'rent',
											text: 'Rent',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 65,
											align: 'center'
										},
										{
											dataIndex: 'pack',
											text: 'Pack',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 65,
											align: 'center'
										},
										{
											dataIndex: 'itemDescription',
											text: 'Item Description',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 140,
											align: 'center'
										},
										{
											dataIndex: 'propertyAddress',
											text: 'Property Address',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 140,
											align: 'center' 
										},
										{
											dataIndex: 'trade',
											text: 'Trade',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 73,
											align: 'center'
										},
										{
											dataIndex: 'unitPrice',
											text: 'Unit Price',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 110,
											align: 'right'
										},
										{
											dataIndex: 'totalPrice',
											text: 'Total Price',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 110,
											align: 'right'
										},
										{
											dataIndex: 'netValue',
											text: 'Net Value', 
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 110,
											align: 'right'
										},
										{
											dataIndex: 'valueOfGoods',
											text: 'Value of Goods',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 140,
											align: 'right'
										},
										{
											dataIndex: 'insurancePremium',
											text: 'Insurance Premium',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 150,
											align: 'center'
										},
										{
											xtype: 'datecolumn',
											format: 'Y-m-d',
											dataIndex: 'datefield',
											text: 'Modified On',
											sortable: true,
											hidden: true,
											filter: {
												type: 'date'
											},
											width : 120,
											align: 'center'
										}
									],
									listeners: {
										itemdblclick: 'onDocDetailsDblClk',
									},
									plugins: [
										{
											ptype: 'gridfilters'
										}
									],
									viewConfig : {
										enableTextSelection : true
									},
									selModel: {
										selType: 'checkboxmodel'
									}
								}
							]
						}*/
					]
				}
			]
		});
		me.callParent(arguments);
	}
});