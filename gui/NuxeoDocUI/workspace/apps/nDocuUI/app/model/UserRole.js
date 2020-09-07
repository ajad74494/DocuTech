Ext.define('Desktop.model.UserRole', {
    extend: 'Ext.data.Model',
    
    fields: [
    	{ name: 'id',
          type: 'int'
        },
        { name: 'version',
          type: 'int'
        },
        { name: 'name',
          type: 'string'
        },
        { name: 'description',
          type: 'string'
        },
        { name: 'userIdModified',
          type: 'int'
        },
        { name: 'active',
          type: 'int'
        },
        { name: 'envId',
          type: 'int'
        },
        { name: 'eventKey',
          type: 'int'
        }
    ]
});
