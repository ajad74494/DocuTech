
Ext.define('nDocuUI.view.login.Login', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.login',

    requires: [
        'appConstants',
        'Ext.plugin.Viewport',
        'Ext.window.MessageBox',
        "nDocuUI.view.login.LoginModel",
        "nDocuUI.view.login.LoginController",
        'Ext.form.field.plugin.CapsLockDetector'
    ],

    xtype: 'login',
    plugins: 'viewport',

    controller: 'login-login',
    viewModel: {
        type: "login-login"
    },
    closable: false,
    draggable: false,
    resizable: false,
    y: 170,
    bodyStyle: 'background-color: #666666;',
    //bodyStyle: 'background: linear-gradient(45deg, rgba(29,229,226,1) 0%, rgba(181,136,247,1) 100%);',

    layout: {
        type: 'vbox', // Previous value was hbox
        align: 'middle',
        pack: 'center'
    },
    items: [
        {
            xtype: 'panel',
            dock: 'top',
            height: 300,
            width: 350,
            layout: 'center',
            bodyStyle: 'background-color: #FFE257;',
            items: [
                {
                    xtype: 'panel',
                    height: 220,
                    width: 250,
                    flex: 1,
                    bodyStyle: 'background-color: #FFE257;',
                    layout: {
                        type: 'vbox',
                        align: 'stretch',
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'panel',
                            flex: 0.2,
                            bodyStyle: 'background-color: #FFE257;',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [
                                {
                                    xtype: 'label',
                                    html: '<h2>DocuTech UI Login</h2>',
                                    text: ''
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            flex: 0.6,
                            bodyStyle: 'background-color: #FFE257;',
                            layout: {
                                type: 'vbox',
                                align: 'stretch',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'panel',
                                    flex: 0.4,
                                    bodyStyle: 'background-color: #FFE257;',
                                    layout: {
                                        type: 'hbox',
                                        align: 'middle'
                                    },
                                    items: [
                                        {
                                            flex: 1,
                                            xtype: 'textfield',
                                            inputType: 'email',
                                            emptyText: 'User ID'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    bodyStyle: 'background-color: #FFE257;',
                                    flex: 0.4,
                                    layout: {
                                        type: 'hbox',
                                        align: 'middle'
                                    },
                                    items: [
                                        {
                                            flex: 1,
                                            xtype: 'textfield',
                                            inputType: 'password',
                                            allowBlank: false,
                                            emptyText: 'Password',
                                            enforceMaxLength: false
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    flex: 0.3,
                                    bodyStyle: 'background-color: #FFE257;',
                                    layout: {
                                        type: 'vbox',
                                        align: 'stretch'
                                    },
                                    items: [
                                        {
                                            xtype: 'panel',
                                            flex: 0.2,
                                            bodyStyle: 'background-color: #FFE257;',
                                        },
                                        {
                                            xtype: 'button',
                                            flex: 0.8,
                                            html: '<h4>Login</h4>',
                                            //style: 'background-color: gray;',
                                            //style: 'background: linear-gradient(41deg, rgba(191,100,222,1) 0%, rgba(94,40,253,1) 100%);',
                                            //text: 'Login',
                                            //border: 0,
                                            listeners: {
                                                click: 'onLoginButtonClick'
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
});