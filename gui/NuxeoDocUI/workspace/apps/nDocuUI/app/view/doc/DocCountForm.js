Ext.define('Desktop.view.doc.DocCountForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.docCountForm',

	requires: [
	],
	
	frame: true,
	bodyPadding: 40,
	border : true,
	closable : true,
	scrollable: true,
	width: 355,

	controller: 'doc-doc',
	viewModel: {
		type: 'doc-doc'
	},

	fieldDefaults: {
		labelAlign: 'right',
		labelWidth: 115,
		msgTarget: 'side'
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
					reference: 'templateForm',
					border: 'true',
					bind: {
						scrollable: 'true'
					},
					dockedItems:[
						{
							xtype: 'toolbar',
							dock: 'bottom',
							items: [
								{
									xtype: 'button',
									text: 'Update',
									style: 'border: groove',
									reference: 'updDocCount',
									iconCls: 'approve_icon',
									hidden: false,
									listeners: {
										click: 'docCountUpdate'
									}
								},
								'->'
							]
						}
					],
					items: [
						{
							xtype: 'fieldset',
							collapsible: 'true',
							title: 'File',
							defaults: {
								anchor: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'File name',
									name: 'fileName',
									reference: 'fileName',
									width: '100%',
									readonly: true,
									editable: false
								},
								{
									xtype: 'textfield',
									name: 'docCountId',
									reference: 'docCountId',
									hidden: true
								},
								{
									xtype: 'textfield',
									name: 'docCountVer',
									reference: 'docCountVer',
									hidden: true
								}
							]
						},
						{
							xtype: 'fieldset',
							collapsible: 'true',
							title: 'Status',
							defaults: {
								anchor: '100%'
							},
							items: [
								{
									xtype: 'combobox',
									labelWidth: 100,
									width: '100%',
									reference: 'isAbbyError',
									name: 'isAbbyError',
									fieldLabel: 'Tmplt Exist',
									store: ["YES","NO"],
									queryMode: 'local',
									displayField: 'isAbbyError',
									valueField: 'isAbbyError',
									queryMode: 'local',
									triggerAction: 'all'
								},
								{
									xtype: 'combobox',
									labelWidth: 100,
									width: '100%',
									reference: 'isAbbyProcessed',
									name: 'isAbbyProcessed',
									fieldLabel: 'Processed',
									store: ["YES","NO"],
									queryMode: 'local',
									displayField: 'isAbbyProcessed',
									valueField: 'isAbbyProcessed',
									queryMode: 'local',
									triggerAction: 'all'
								},
								{
									xtype: 'combobox',
									labelWidth: 100,
									width: '100%',
									reference: 'isProcessedSuccessfully',
									name: 'isProcessedSuccessfully',
									fieldLabel: 'Successful',
									store: ["YES","NO"],
									queryMode: 'local',
									displayField: 'isProcessedSuccessfully',
									valueField: 'isProcessedSuccessfully',
									queryMode: 'local',
									triggerAction: 'all'
								}
							]
						},
						{
							xtype: 'fieldset',
							collapsible: 'true',
							title: 'Others',
							defaults: {
								anchor: '100%'
							},
							items: [
								{
									xtype: 'textfield',
									fieldLabel: 'Sender',
									name: 'senderAddress',
									reference: 'senderAddress',
									width: '100%',
									readonly: true,
									editable: false
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Source',
									name: 'downloadSource',
									reference: 'downloadSource',
									width: '100%',
									readonly: true,
									editable: false
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Type',
									name: 'docType',
									reference: 'docType',
									width: '100%'
								},
								{
									xtype: 'textfield',
									fieldLabel: 'Subject',
									name: 'subject',
									reference: 'subject',
									width: '100%',
									readonly: true,
									editable: false
								}
							]
						}
					]
				}
			]
		});
		me.callParent(arguments);
	}
});