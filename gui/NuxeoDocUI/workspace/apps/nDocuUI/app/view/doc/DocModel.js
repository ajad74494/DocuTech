Ext.define('Desktop.view.doc.DocModel', {
	extend: 'Ext.app.ViewModel',
	alias: 'viewmodel.doc-doc',
	data: {
		name: 'Desktop'
	},
	
	requires: [
        'Ext.data.BufferedStore',
        'Desktop.model.Doc',
        'Desktop.model.Customer',
        'Desktop.model.DocDetails'
    ],

	stores: {

		/*docStore: {
			groupField: 'documentDate',
			model: 'Desktop.model.Doc'
		},

		customerStore: {
			model: 'Desktop.model.Customer'
		},

		docDetailsStore: {
			model: 'Desktop.model.DocDetails'
		}*/

		docStore: 'gDocStore',
		tmpltStore: 'gTmpltStore',
		docCtStore: 'gDocCtStore',
		customerStore: 'customerStore',
		docDetailsStore: 'docDetailsStore'
	}

});
