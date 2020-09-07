Ext.define('Desktop.model.Login', {
    extend: 'Ext.data.Model',
    
    fields: [
        {
            type: 'string',
            name: 'userId'
        },
        {
            type: 'int',
            name: 'userVer'
        },
        {
            type: 'string',
            name: 'userAlias'
        },
        {
            type: 'string',
            name: 'loginName'
        },
        {
            type: 'string',
            name: 'password'
        }
    ]
});
