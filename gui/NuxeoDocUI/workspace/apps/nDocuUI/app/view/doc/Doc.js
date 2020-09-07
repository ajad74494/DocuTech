
Ext.define('Desktop.view.doc.Doc',{

	//extend: 'Ext.panel.Panel',
	extend: 'Ext.tab.Panel',
	xtype: 'docs',

	requires: [
		'Desktop.view.doc.DocController',
		'Desktop.view.doc.DocModel',
		'Ext.menu.Menu'
	],

	controller: 'doc-doc',
	viewModel: {
		type: 'doc-doc'
	},
	layout: 'border',
	ui: 'navigation',

	tabBar: {
		flex: 1,
		layout: {
			align: 'stretch',
			overflowHandler: 'none'
		}
	},

	responsiveConfig: {
		tall: {
			headerPosition: 'top'
		},
		wide: {
			headerPosition: 'left'
		}
	},

	defaults: {
		bodyPadding: 0,
		tabConfig: {
			plugins: 'responsive',
			responsiveConfig: {
				wide: {
					iconAlign: 'left',
					textAlign: 'left'
				},
				tall: {
					iconAlign: 'top',
					textAlign: 'center',
					width: 120
				}
			}
		}
	},

	items: [
		{
			xtype: 'panel',
			layout: 'fit',
			items: [
				{
					xtype: 'tabpanel',
					activeTab: 0,
					reference: 'docsTabPanel',
					items: [
						,
						{
							xtype: 'panel',
							layout: 'fit',
							title: 'Documents',
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											xtype: 'textfield',
											reference: 'docGridFilter',
											fieldLabel: 'Filter',
											width: 200,
											margin: '0 0 0 10',
											labelWidth: 30,
											listeners: {
												change: 'onDocGridFilter'
											}
										},
										{
											xtype: 'datefield',
											reference: 'docFromDate',
											format: 'd/m/Y',
											fieldLabel: 'From Date',
											labelWidth: 80,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false
										},
										{
											xtype: 'datefield',
											reference: 'docToDate',
											format: 'd/m/Y',
											fieldLabel: 'To Date',
											labelWidth: 65,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false,
											maxValue: new Date()
										},
										{
											xtype: 'button',
											text: '',
											reference: 'srcDocBtn',
											iconCls: 'search-button-icon',
											margin: '0 0 0 15',
											tooltip: 'Search with Date Range',
											listeners: {
												click: 'onDocSrc'
											}
										},
										{
											xtype:'button',
											iconCls: 'add-button-icon',
											text: 'New Doc',
											margin: '0 0 0 15',
											menu: {
												xtype: 'menu',
												items: [
													{
														xtype: 'menuitem',
														text: 'Invoice',
														listeners:{
															click: 'onAddNewInvoiceDoc'
														}
													},
													{
														xtype: 'menuitem',
														text: 'Credit Note',
														listeners:{
															click: 'onAddNewCreditNoteDoc'
														}
														
													},
													{
														xtype: 'menuitem',
														text: 'Statement',
														listeners:{
															click: 'onAddNewStatementDoc'
														}
													}
												]
											}											
										}
									]
								}
							],
							items: [
								// Grid items panel start
								{
									xtype: 'gridpanel',
									reference: 'docsGrid',
									title: '',
									bind: {
										store: '{docStore}'
									},
									// showing on UI
									scrollable: true,	
									stripeRows : true,
									columnLines: true,
									columns: [
										{
											xtype: 'rownumberer',
											text: 'Sl No.',
											width : 100
										},
										{
											dataIndex: 'fileName',
											text: 'File Name',
											sortable: true,
											width : 250,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'docId',
											text: 'Doc ID',
											hidden: true,
											sortable: true,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'docVer',
											text: 'Doc Version',
											hidden: true,
											sortable: true,
											width : 110,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'supplierNumber',
											text: 'Supplier Number',
											sortable: true,
											width : 150,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'deliveryNoteNo',
											text: 'Delivery Note No.',
											sortable: true,
											width : 160,
											align: 'center',
											filter: {
												type: 'number'
											}
										}, 
										{
											dataIndex: 'receiptNo',
											text: 'Receipt No.',
											sortable: true,
											width : 100,
											align: 'center',
											filter: {
												type: 'number'
											}
										}, 
										{
											dataIndex: 'documentNumber',
											text: 'Document Number',
											sortable: true,
											width : 150,
											align: 'center',
											filter: {
												type: 'number'
											}
										}, 
										{
											xtype: 'datecolumn',
											dataIndex: 'documentDate',
											text: 'Document Date',
											sortable: true,
											format: 'Y-m-d',
											width : 125,
											align: 'center',
											filter: {
												type: 'date'
											}
										}, 
										{
											dataIndex: 'recipientOfInvoice',
											text: 'Recipient of Invoice',
											sortable: true,
											width : 155,
											align: 'center',
											filter: {
												type: 'number'
											}
										}, 
										{
											dataIndex: 'vatNumber',
											text: 'VAT Number',
											sortable: true,
											width : 100,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'invoiceNumber',
											text: 'Invoice Number',
											sortable: true,
											width : 135,
											align: 'center' ,
											filter: {
												type: 'string'
											}
										}, 
										{
											xtype: 'datecolumn',
											dataIndex: 'despatchDate',
											text: 'Despatch Date',
											format: 'Y-m-d',
											sortable: true,
											width : 130,
											align: 'center',
											filter: {
												type: 'date'
											}
										}, 
										{
											dataIndex: 'currency',
											text: 'Currency',
											sortable: true,
											width : 80,
											align: 'center',
											filter: {
												type: 'list'
											}
										}, 
										{
											dataIndex: 'accountNumber',
											text: 'Account Number',
											sortable: true,
											width : 140,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'voucherNumber',
											text: 'Voucher Number',
											sortable: true,
											width : 130,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'faxNumber',
											text: 'Fax Number', 
											sortable: true,
											width : 110,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'associationNumber',
											text: 'Association Number',
											sortable: true,
											width : 170,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'companyAddress',
											text: 'Company Address',
											sortable: true,
											width : 150,
											align: 'center',
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'invoiceTo',
											text: 'Invoice To',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 100,
											align: 'center'
										}, 
										{
											dataIndex: 'deliverTo',
											text: 'Deliver To',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 100,
											align: 'center'
										}, 
										{
											dataIndex: 'orderId',
											text: 'Order ID',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 90,
											align: 'center'
										}, 
										{
											dataIndex: 'deliveryNote',
											text: 'Delivery Note', 
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 130,
											align: 'center'
										}, 
										{
											dataIndex: 'deliveryDetails',
											text: 'Delivery Details',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 160,
											align: 'center'
										}, 
										{
											dataIndex: 'paymentDetails',
											text: 'Payment Details',
											sortable: true,
											filter: {
												type: 'string'
											},
											width : 150,
											align: 'center'
										}, 
										{
											dataIndex: 'vat',
											text: 'VAT',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 150,
											align: 'right'
										}, 
										{
											dataIndex: 'vatRate',
											text: 'VAT Rate',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 105,
											align: 'right'
										}, 
										{
											dataIndex: 'vatPayable',
											text: 'VAT Payable',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 130,
											align: 'right'
										}, 
										{
											dataIndex: 'invoiceAmount',
											text: 'Invoice Amount',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 140,
											align: 'right'
										}, 
										{
											dataIndex: 'totalBeforeVat',
											text: 'Total Before VAT',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 160,
											align: 'right'
										}, 
										{
											dataIndex: 'totalAmountDue',
											text: 'Total Amount Due',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 150,
											align: 'right'
										},
										{
											dataIndex: 'link',
											text: 'Link',
											sortable: true,
											filter: {
												type: 'string'
											},
											hidden :true,
											width : 400,
											align: 'right'
										},  
										{
											dataIndex: 'discount',
											text: 'Discount',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 100,
											align: 'right'
										}, 
										{
											dataIndex: 'netInvoiceTotal',
											text: 'Net Invoice Total',
											sortable: true,
											filter: {
												type: 'number'
											},
											width : 160,
											align: 'right'
										}, 
										{
											xtype: 'datecolumn',
											dataIndex: 'modifiedOn',
											text: 'Modified On',
											format: 'Y-m-d H:i:s A',
											sortable: true,
											hidden: true,
											filter: {
												type: 'date'
											},
											width : 150
										}		
									],
									/*features: [
										{
											ftype: 'grouping',
											groupHeaderTpl: 'Document Date: {name} ({children.length})',
											startCollapsed: true
										}
									],*/
									plugins: [
										{
											ptype: 'gridfilters'
										}
									],
									/*selModel: {
										selType: 'checkboxmodel'
									},*/
									viewConfig : {
										enableTextSelection : true
									},
									listeners: {
										itemdblclick:  'onDoubleClick'
									}
								}
							]
						},
						{
							xtype: 'panel',
							layout: 'fit',
							title: 'Download Info',
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											xtype: 'datefield',
											reference: 'ctFromDate',
											format: 'd/m/Y',
											fieldLabel: 'From Date',
											labelWidth: 80,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false
										},
										{
											xtype: 'datefield',
											reference: 'ctToDate',
											format: 'd/m/Y',
											fieldLabel: 'To Date',
											labelWidth: 65,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false,
											maxValue: new Date()
										},
										{
											xtype: 'button',
											text: 'Filter',
											reference: 'srcDocCtBtn',
											margin: '0 0 0 15',
											tooltip: 'Search with Date Range',
											listeners: {
												click: 'onDocCtSrc'
											}
										}
									]
								}
							],
							items: [
								// Grid items panel start
								{
									xtype: 'gridpanel',
									reference: 'docCtGrid',
									title: '',
									bind: {
										store: '{docCtStore}'
									},
									// showing on UI
									scrollable: true,	
									stripeRows : true,
									columnLines: true,
									columns: [
										{
											xtype: 'rownumberer',
											text: 'Sl No.',
											width : 100
										}, 
										{
											xtype: 'datecolumn',
											dataIndex: 'modifiedOn',
											text: 'Date',
											sortable: true,
											format: 'Y-m-d',
											width : 100,
											align: 'center',
											filter: {
												type: 'date'
											}
										},
										{
											dataIndex: 'ttlAtchmnt',
											text: 'Attachments',
											sortable: true,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'isAbbyProcessed',
											text: 'Processed',
											sortable: true,
											width : 70,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										}, 
										{
											dataIndex: 'isAbbyError',
											text: 'Error',
											sortable: true,
											width : 70,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										}, 
										{
											dataIndex: 'isProcessedSuccessfully',
											text: 'Successful',
											sortable: true,
											width : 80,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										},
										{
											dataIndex: 'fileName',
											text: 'File Name',
											sortable: true,
											width : 250,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'sender',
											text: 'Sender',
											flex: 1,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'downloadSource',
											text: 'Source',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'docType',
											text: 'Type',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'subject',
											text: 'Subject',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'onlySubject',
											text: 'Subject',
											flex: 1,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'docCountId',
											text: 'Doc Ct. Id',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'docCountVer',
											text: 'Template Id',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										}	
									],
									plugins: [
										{
											ptype: 'gridfilters'
										}
									],
									/*selModel: {
										selType: 'checkboxmodel',
										listeners: {
											selectionchange:'onTmpltDataSlct'
										}
									},*/
									viewConfig : {
										enableTextSelection : true
									},
									listeners: {
										itemdblclick:  'onDocCtItmDblClick'
									}
								}
							]
						},
						{
							xtype: 'panel',
							layout: 'fit',
							title: 'Templates',
							dockedItems: [
								{
									xtype: 'toolbar',
									dock: 'top',
									items: [
										{
											xtype: 'datefield',
											reference: 'fromDate',
											format: 'd/m/Y',
											fieldLabel: 'From Date',
											labelWidth: 80,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false
										},
										{
											xtype: 'datefield',
											reference: 'toDate',
											format: 'd/m/Y',
											fieldLabel: 'To Date',
											labelWidth: 65,
											size: 17,
											margin: '0 0 0 10',
											allowBlank: false,
											maxValue: new Date()
										},
										{
											xtype: 'button',
											text: 'Filter',
											reference: 'srcTmpltBtn',											
											margin: '0 0 0 15',
											tooltip: 'Search with Date Range',
											listeners: {
												click: 'onTmpltSrc'
											}
										}
									]
								},
								{
									xtype: 'toolbar',
									dock: 'bottom',
									items: [
										{
											xtype: 'button',
											text: 'View Doc',
											hidden: true,
											reference: 'viewBtn',
											iconCls: 'invoice-icon',
											margin: '0 0 0 15',
											style: 'border: groove',
											listeners: {
												click: 'onViewDoc'
											}
										}
									]
								}
							],
							items: [
								// Grid items panel start
								{
									xtype: 'gridpanel',
									reference: 'tmpltGrid',
									title: '',
									bind: {
										store: '{tmpltStore}'
									},
									// showing on UI
									scrollable: true,	
									stripeRows : true,
									columnLines: true,
									columns: [
										{
											xtype: 'rownumberer',
											text: 'Sl No.',
											width : 100,
										}, 
										{
											xtype: 'datecolumn',
											dataIndex: 'modifiedOn',
											text: 'Date',
											sortable: true,
											format: 'Y-m-d',
											width : 100,
											align: 'center',
											filter: {
												type: 'date'
											}
										},
										{
											dataIndex: 'isSentToNuxeo',
											text: 'Nuxeo Sent',
											sortable: true,
											width : 70,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										}, 
										{
											dataIndex: 'isTemplateParsed',
											text: 'Parsed',
											sortable: true,
											width : 80,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										}, 
										{
											dataIndex: 'isParsedSuccessful',
											text: 'Successful',
											sortable: true,
											width : 80,
											align: 'center',
											filter: {
												type: 'string'
											},
											renderer : function(value){
												if(value == 1){
													return 'Yes';
												}
												else{
													return 'No';
												}
											}
										},
										{
											dataIndex: 'fileName',
											text: 'File Name',
											sortable: true,
											width : 250,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'errorIssue',
											text: 'Error',
											flex: 1,
											sortable: true,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'docCountId',
											text: 'Doc Ct. Id',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'templateId',
											text: 'Template Id',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'templateVer',
											text: 'Template Ver',
											hidden: true,
											sortable: true,
											filter: {
												type: 'number'
											}
										}, 
										{
											dataIndex: 'clientName',
											text: 'Client Name',
											sortable: true,
											hidden: true,
											width : 135,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'supplierName',
											text: 'Supplier Name',
											hidden: true,
											sortable: true,
											width : 140,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'pdfType',
											text: 'Type',
											hidden: true,
											sortable: true,
											width : 80,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'processorType',
											text: 'Processor',
											hidden: true,
											sortable: true,
											width : 90,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'docSource',
											text: 'Source',
											hidden: true,
											sortable: true,
											width : 90,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'receivedDocId',
											text: 'Received Doc Id',
											hidden: true,
											sortable: true,
											hidden: true,
											filter: {
												type: 'string'
											}
										}, 
										{
											dataIndex: 'docType',
											text: 'Doc Type',
											hidden: true,
											sortable: true,
											width : 90,
											filter: {
												type: 'string'
											}
										},
										{
											dataIndex: 'errorIssueSource',
											text: 'Issue Source',
											hidden: true,
											sortable: true,
											filter: {
												type: 'string'
											}
										}		
									],
									plugins: [
										{
											ptype: 'gridfilters'
										}
									],
									selModel: {
										selType: 'checkboxmodel',
										listeners: {
											selectionchange:'onTmpltDataSlct'
										}
									},
									viewConfig : {
										enableTextSelection : true
									}
								}
							]
						}
					]
				}
			]
		}
	],
	listeners: {
		afterrender: 'onPanelShow'
	}	
});
