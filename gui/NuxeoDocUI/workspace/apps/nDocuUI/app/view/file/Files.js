
Ext.define('nDocuUI.view.file.Files',{
    //extend: 'Ext.panel.Panel',
    extend: 'Ext.tab.Panel',
    xtype: 'filesWin',

    requires: [
        'nDocuUI.view.file.FilesController',
        'nDocuUI.view.file.FilesModel'
    ],

    controller: 'file-files',
    viewModel: {
        type: 'file-files'
    },
    
    layout: 'border',
    ui: 'navigation',

    tabBar: {
        flex: 1,
        layout: {
            align: 'stretch',
            overflowHandler: 'none'
        }
    },

    responsiveConfig: {
        tall: {
            headerPosition: 'top'
        },
        wide: {
            headerPosition: 'left'
        }
    },

    defaults: {
        bodyPadding: 0,
        tabConfig: {
            plugins: 'responsive',
            responsiveConfig: {
                wide: {
                    iconAlign: 'left',
                    textAlign: 'left'
                },
                tall: {
                    iconAlign: 'top',
                    textAlign: 'center',
                    width: 120
                }
            }
        }
    },

    items: [
        {
            xtype: 'panel',
            layout: 'fit',
            items: [
                {
                    xtype: 'tabpanel',
                    activeTab: 0,
                    reference: 'fileTabPanel',
                    items: [
                        {
                            xtype :  'panel',
                            border : false,
                            reference : 'fileTab',
                            title : 'Search',
                            layout : 'fit',
                            items :[
                                {
                                    xtype: 'form',
                                    reference : 'fileForm',
                                    region: 'center',
                                    layout: 'fit',
                                    border : false,
                                    dockedItems: [
                                        {
                                            xtype: 'toolbar',
                                            dock: 'top',
                                            items: [
                                                {
                                                    xtype: 'textfield',
                                                    fieldLabel: 'File name',
                                                    labelWidth: 80,
                                                    width: 500,
                                                    margin: '0 0 0 10',
                                                    reference: 'fileName',
                                                    allowBlank : false
                                                },
                                                {
                                                    xtype: 'button',
                                                    text: '',
                                                    reference: 'srcFile',
                                                    iconCls: 'search-button-icon',
                                                    margin: '0 0 0 15',
                                                    listeners: {
                                                        click: 'onViewFile'
                                                    }
                                                }
                                            ]
                                        }
                                    ],
                                    items : [
                                        {
                                            xtype : 'panel',
                                            width : '100%',
                                            reference : 'filePanel',
                                            layout : 'fit',
                                            border : false
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
