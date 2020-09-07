Ext.define('Desktop.model.User', {
    extend: 'Ext.data.Model',
    
    fields: [
        /*{ name: 'id', type: 'int' },*/
        {name : 'id'},
		{name : 'userVersion'},
		{name : 'active'},
		{name : 'dateModified', type: 'date', dateFormat: 'Ymd H:i:s'}, // new server must be remove 'type: 'date', dateFormat: 'Ymd H:i:s''
		{name : 'userIdModified'},
		{name : 'userNameModified'},
		{name : 'stateType'},
		{name : 'actionType'},
		{name : 'unId'},
		{name : 'firstName'},
		{name : 'lastName'},
		{name : 'userAlias'},
		{name : 'userId'},
		{name : 'legalEntityTypeId'},
		{name : 'primaryGroupName'},
		{name : 'delete', defaultValue: 'delete'}

    ]
});
