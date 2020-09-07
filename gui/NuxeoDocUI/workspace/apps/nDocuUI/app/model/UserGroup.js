Ext.define('Desktop.model.UserGroup', {
    extend: 'Ext.data.Model',
    
    fields: [
        { name: 'id',
          type: 'int'
        },
        { name: 'groupVersion',
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
