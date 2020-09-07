/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('Desktop.DocsWin', {
	extend: 'Ext.ux.desktop.Module',

	requires: [
		'Ext.data.ArrayStore',
		'Ext.util.Format',
		'Ext.grid.Panel',
		'Ext.grid.RowNumberer'
	],

	id:'docs-win',

	init : function(){
		this.launcher = {
			text: 'Document Info',
			iconCls:'doc_info-button-icon'
		};

		this.createGlobalStores();
	},

	createWindow : function(){
		var desktop = this.app.getDesktop();
		var win = desktop.getWindow('docs-win');
		if(!win){
			win = desktop.createWindow({
				id: 'docs-win',
				title:'Document Info',
				width: desktop.getWidth() -150,
				height: desktop.getHeight() - 60,
				iconCls:'doc_info-button-icon',
				animCollapse:false,
				constrainHeader:true,
				layout: 'fit',
				items: [
					{
						xtype: 'docs'
					}
				]
			});
		}
		return win;
	},

	createGlobalStores : function(){

		Ext.create('Ext.data.Store', {
			model: 'Desktop.model.Doc',
			storeId: 'gDocStore',
			groupField: 'documentDate'
		});

		Ext.create('Ext.data.Store', {
			model: 'Desktop.model.DocDetails',
			storeId: 'docDetailsStore',
		});

		Ext.create('Ext.data.Store', {
			model: 'Desktop.model.Customer',
			storeId: 'customerStore',
		});

		Ext.create('Ext.data.Store', {
			model: 'Desktop.model.Template',
			storeId: 'gTmpltStore',
		});

		Ext.create('Ext.data.Store', {
			model: 'Desktop.model.DocCount',
			storeId: 'gDocCtStore',
		});
	},

	statics: {
	}
});

