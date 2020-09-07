/*!
 * Ext JS Library
 * Copyright(c) 2006-2014 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('Desktop.App', {
	extend: 'Ext.ux.desktop.App',

	requires: [
		'Ext.window.MessageBox',

		'Ext.ux.desktop.ShortcutModel',

		'Desktop.DocsWin',
		'Desktop.FilesWin',
		'Desktop.BogusMenuModule',
		'Desktop.BogusModule',
		'Desktop.Settings'
	],

	init: function() {
		// custom logic before getXYZ methods get called...

		this.callParent();

		// now ready...
	},

	getModules : function(){
		return [
			new Desktop.DocsWin(),
			new Desktop.FilesWin()
			//new Desktop.BogusModule(),
			//new Desktop.BogusMenuModule()
		];
	},

	getDesktopConfig: function () {
		var me = this, ret = me.callParent();

		return Ext.apply(ret, {
			//cls: 'ux-desktop-black',

			contextMenuItems: [
				{ text: 'Change Settings', handler: me.onSettings, scope: me }
			],

			shortcuts: Ext.create('Ext.data.Store', {
				model: 'Ext.ux.desktop.ShortcutModel',
				data: [
					{ name: 'Document_Info', iconCls: 'doc_info-shortcut', module: 'docs-win' },
					{ name: 'File_viewer', iconCls: 'files-shortcut', module: 'files-win' }
				]
			}),

			wallpaper: 'resources/images/wallpapers/elsonn.jpg',
			wallpaperStretch: true
		});
	},

	// config for the start menu
	getStartConfig : function() {
		var me = this, ret = me.callParent();

		return Ext.apply(ret, {
			title: 'Docutech',
			iconCls: 'user',
			height: 300,
			toolConfig: {
				width: 150,
				items: [
					{
						text:'Settings',
						iconCls:'setting-button-icon',
						handler: me.onSettings,
						scope: me
					},
					'-',
					{
						text:'Logout',
						iconCls:'logout-button-icon',
						handler: me.onLogout,
						scope: me
					}
				]
			}
		});
	},

	getTaskbarConfig: function () {
		var ret = this.callParent();

		return Ext.apply(ret, {
			quickStart: [
				{ name: 'Document_Info', iconCls: 'doc_info-button-icon', module: 'docs-win' }
			],
			trayItems: [
				{ xtype: 'trayclock', flex: 1 }
			]
		});
	},

	onLogout: function () {
		Ext.MessageBox.confirm('Logout', 'Are you sure you want to logout?', function(btn) {

			if (btn == 'yes') {
				window.location.reload();
			}
		});		
	},

	onSettings: function () {
		var dlg = new Desktop.Settings({
			desktop: this.desktop
		});
		dlg.show();
	}
});
