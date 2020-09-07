Ext.define('nazdaq.message.MessageDispatcher', {
    alias: 'nMessageDispatcher',
    alternateClassName: 'nMessageDispatcher',
    statics: {

        dispatch: function(request, dispatchType) {

            if (Ext.isEmpty(dispatchType)) {
                dispatchType = appConstants.DISPATCH_TYPE_AJAX;
            }

            if (dispatchType === appConstants.DISPATCH_TYPE_AJAX) {
                this.dispatchAjaxRequest(request);
            } else {
                Ext.MessageBox.alert('Warn', 'Unknown dispatch type ' + dispatchType);
            }
        },

        dispatchAjaxRequest: function(appRequest) {

            var serverUrl = httpServer;

            if (appRequest.contentType == 'User' && appRequest.actionName == 'LOGIN') {
                // Put the object into storage
                localStorage.setItem('logoutObject', appRequest.message.toString());
            }
            
            /*if(appRequest.contentType == 'GoAML' || appRequest.contentType == 'GoAMLNr'){
                serverUrl = goamlServer;
            }
            else{
                serverUrl = httpServer;
            }*/

            if (!Ext.isEmpty(appRequest.message)) {

                Ext.Ajax.request({
                    
                    /*url: (appRequest.contentType == 'GoAML' || appRequest.contentType == 'GoAMLNr') ? goamlServer : httpServer,*/

                    url: serverUrl,
                    method: appConstants.REQUEST_METHOD_POST,
                    jsonData: appRequest.message.toString(),                    
                    xhrFields: { withCredentials: true },

                    success: function(result, request) {
                        var response = Ext.JSON.decode(result.responseText);
                        nMessageProcessor.processResponse(appRequest, response);
                    },

                    failure: function(result, request) {
                        Ext.MessageBox.alert('Server Error', 'An unexpected error occured. Please try again later.');
                    }
                });
            }
        }
    }
});