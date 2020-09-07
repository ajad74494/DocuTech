/*Ext.define('nDocuUI.view.login.LoginModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.login-login',
    data: {
        name: 'nDocuUI'
    }

});*/
Ext.define('nDocuUI.view.login.LoginModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.login-login',
    data: {
        name: 'nDocuUI'
    },

    requires: [
        'Ext.data.Store',
        'Desktop.model.Login'
        //'nDocuUI.model.Login'
    ],

    stores: {
        loginStore: {
            //model: 'nDocuUI.model.Login'
            model: 'Desktop.model.Login'
        }
    }

});
