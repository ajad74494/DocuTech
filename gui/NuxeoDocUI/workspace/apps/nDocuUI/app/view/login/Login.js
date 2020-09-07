
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

    layout: {
        type: 'vbox', // Previous value was hbox
        align: 'middle',
        pack: 'center'
    },
    items: [
        {
            xtype: 'panel',
            layout: 'form',
            items: [
                {
                    xtype: 'form',
                    reference: 'loginForm',
                    flex: 1,
                    width: 380,                    
                    title: 'Login',
                    titleAlign: 'center',
                    items: [
                        {
                            xtype: 'panel',
                            region: 'center',
                            layout: 'form',
                            bodyStyle: "background-image:url('resources/images/desktop.gif')",
                            items: [
                                {
                                    xtype: 'tbspacer',
                                    height: 5
                                },
                                {
                                    reference: 'uid',
                                    xtype: 'textfield',
                                    fieldLabel: 'User Name',
                                    dataIndex: 'userId',
                                    labelStyle: 'color: #fff;',
                                    labelWidth: 80,
                                    align: 'right',
                                    allowBlank: false,
                                    validator: function(text) {
                                        return (text.length == 0 || Ext.util.Format.trim(text).length != 0);
                                    },
                                    listeners: {
                                        specialkey: 'onKeyPress'
                                    }
                                },
                                {
                                    xtype: 'tbspacer',
                                    height: 5
                                },
                                {
                                    reference: 'pass',
                                    xtype: 'textfield',
                                    fieldLabel: 'Password',
                                    dataIndex: 'password',
                                    labelStyle: 'color: #fff;',
                                    labelWidth: 80,
                                    align: 'right',
                                    inputType: 'password',
                                    listeners: {
                                        specialkey: 'onKeyPress'
                                    },
                                    allowBlank: false,
                                    validator: function(text) {
                                        return (text.length == 0 || Ext.util.Format.trim(text).length != 0);
                                    },
                                    plugins: [
                                        {
                                            ptype: 'capslockdetector',
                                            title: '<span style="color : red">Caps lock is on</span>',
                                            message: 'Having caps lock on may cause you to enter your password incorrectly.'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'tbspacer',
                                    height: 5
                                }
                            ],

                            dockedItems: [
                                {
                                    xtype: 'toolbar',
                                    dock: 'bottom',
                                    padding: 10,
                                    items: [
                                        '->', // Allow the button to center
                                        {
                                            xtype: 'button',
                                            reference: 'loginBtn',
                                            width: '80px',
                                            style: 'border: groove',
                                            icon: 'resources/images/admin.png',
                                            text: '<div style="margin-left:0px">Login</div>',
                                            listeners: {
                                                click: 'onLoginButtonClick'
                                            }
                                        },
                                        {
                                            xtype: 'button',
                                            width: '80px',
                                            style: 'border: groove',
                                            margin: '0 0 0 15',
                                            icon: 'resources/images/cleaning_brush.png',
                                            text: '<div style="margin-left:0px">Clear</div>',
                                            listeners: {
                                                click: 'onCancelButtonClick'
                                            }
                                        },
                                        '->'
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