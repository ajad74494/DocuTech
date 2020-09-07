Ext.define('nDocuUI.view.file.FilesController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.file-files',

    /**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* for view doc from file module
	**/
	onViewFile: function(button, e, eOpts){
		
		var me = this, fileName = null, urlReq = null;

		fileName = me.lookupReference('fileName').value;

		console.log(fileName);

		var fileForm = me.lookupReference('fileForm');

		if(fileForm.isValid()){

			var filePanel = me.lookupReference('filePanel');

			urlReq ='<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0" src="'
			+httpDocuments+'?fileName='+fileName+'"></iframe>';

			filePanel.body.update(urlReq);
		}
	}

});
