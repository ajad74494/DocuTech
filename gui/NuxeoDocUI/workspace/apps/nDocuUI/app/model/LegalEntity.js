Ext.define('Desktop.model.LegalEntity', {
    extend: 'Ext.data.Model',
    
    fields: [
        {
			type: 'int',
			name: 'legalEntityKey'
		},
		{
			type: 'string',
			name: 'legalEntityName'
		},
		{
			type: 'int',
			name: 'loginKey'
		},
		{
			type: 'int',
			name: 'loginVer'
		},
		{
			type: 'int',
			name: 'userId'
		},
		{
			type: 'int',
			name: 'userVer'
		},
		{
			type: 'string',
			name: 'firstName'
		},
		{
			type: 'string',
			name: 'lastName'
		},
		{
			type: 'string',
			name: 'loginName'
		},
		{
			type: 'string',
			name: 'userAlias'
		},
		{
			type: 'string',
			name: 'isDisabled'
		},
		{
			type: 'string',
			name: 'isAllowLogin'
		},
		{
			type: 'string',
			name: 'isLoggedIn'
		},
		{
			type: 'string',
			name: 'primaryGroupName'
		}

    ]
});
