Ext.define('Desktop.model.DocCount', {
	extend: 'Ext.data.Model',
	fields: [ 
		  'docCountId'
		, 'docCountVer'
		, 'isAbbyProcessed'
		, 'isAbbyError'
		, 'isProcessedSuccessfully'
		, 'idUserModKey'
		, 'senderAddress'
		, 'downloadSource'
		, 'docType'
		, 'subject'
		, 'fileName'
		, 'modifiedOn'
		, 'senderEmail',
		{
			name: 'onlySubject',
			type: 'string',
			convert: function( v, record ) {
				return record.get( 'subject' ).slice(18,-2)
			}
		},
		{
			name: 'ttlAtchmnt',
			type: 'string',
			convert: function( v, record ) {
				return record.get( 'subject' ).slice(record.get( 'subject' ).length - 1)
			}
		},
		{
			name: 'sender',
			type: 'string',
			convert: function( v, record ) {
				return record.get( 'senderAddress' ) + ' (' + record.get( 'senderEmail' ) + ')'
			}
		}
	]
});
