Ext.define('Desktop.view.doc.StatementDocFrom', {
	extend : 'Ext.form.Panel',
	alias: 'widget.StatementDocFrom',
	
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
									title: 'Statement',
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
													fieldLabel: 'Supplier Name',
													name: 'supplierName',
													reference: 'supplierName',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold',
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Doc Type',
													name: 'docType',
													reference: 'docType',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'textfield',
													fieldLabel: 'Customer Address',
													name: 'customerAddress',
													reference: 'customerAddress',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
												{
													xtype: 'numberfield',
													fieldLabel: 'Invoice Number',
													name: 'invoiceNumber',
                                                    reference: 'invoiceNumber',
                                                    emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
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
													xtype: 'numberfield',
													fieldLabel: 'Account Number',													
													name: 'accNumber',
													reference: 'accNumber',													
													emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
                                                },
                                                {
													xtype: 'numberfield',
													fieldLabel: 'Total Account Balance',													
													name: 'totalAccBalence',
													reference: 'totalAccBalence',													
													emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
                                                },                                                
												{
													xtype: 'textfield',
													fieldLabel: 'Currency',
													name: 'currency',
													reference: 'currency',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
                                                {
													xtype: 'datefield',
													fieldLabel: 'Invoice Date',
													name: 'invoiceDate',
													reference: 'invoiceDate',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold',
													value: new Date()
												},												
                                                
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
													fieldLabel: 'Vat Reg Number',													
													name: 'vatRegNumber',
													reference: 'vatRegNumber',													
													emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
                                                },                                                                  
												{
													xtype: 'textfield',
													fieldLabel: 'Supplier Address',
													name: 'supplierAddress',
													reference: 'supplierAddress',
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
												},
                                                {
													xtype: 'numberfield',
													fieldLabel: 'Total Remitance',													
													name: 'totalRemitance',
													reference: 'totalRemitance',													
													emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
                                                },                                                                  
												{
													xtype: 'textfield',
													fieldLabel: 'Customer Name',
													name: 'customerName',
													reference: 'customerName',
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
													xtype: 'numberfield',
													fieldLabel: 'vat',													
													name: 'vat',
													reference: 'vat',													
													emptyText: 'Number',
													hideTrigger: true,
													mouseWheelEnabled: false,
													columnWidth: .25,
													margin: '0 15 0 0',
													labelStyle: 'font-weight:bold'
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