/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 */

/*GLOBAL VAR*/

var CurrentDocId = null, gUserId, gUserVer, gLoginName, gUserAlias, myMask;
var isLoggedIn = false;

/*Disable _dc parameter*/

Ext.Loader.setConfig({
	disableCaching: false
});

Ext.application({
	name: 'Desktop',

	//-------------------------------------------------------------------------
	// Most customizations should be made to Desktop.Application. If you need to
	// customize this file, doing so below this section reduces the likelihood
	// of merge conflicts when upgrading to new versions of Sencha Cmd.
	//-------------------------------------------------------------------------

	requires: [
		'Desktop.App',
		'Desktop.view.doc.Doc',
		'Ext.layout.container.boxOverflow.Menu'
	],
	
	mainView : 'nDocuUI.view.login.Login',

	init: function() {
		//var app = new Desktop.App();
		Ext.ariaWarn = Ext.emptyFn;
		Ext.Ajax.setTimeout(1800000);

		Ext.merge(appConstants, nConstants);
		Ext.merge(appActionType, nActionType);
		Ext.merge(appContentType, nContentType);
		Ext.merge(appStatusType, nStatusType);
	}
});
