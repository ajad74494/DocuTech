Ext.define('Desktop.model.Template', {
	extend: 'Ext.data.Model',
	fields: [ 
		  'docCountId'
		, 'templateId'
		, 'templateVer'
		, 'isTmpltExist'
		, 'isSentToNuxeo'
		, 'isTemplateParsed'
		, 'isParsedSuccessful'
		, 'abbyUnprocessed'
		, 'pdfType'
		, 'processorType'
		, 'clientName'
		, 'supplierName'
		, 'docSource'
		, 'receivedDocId'
		, 'docType'
		, 'errorIssue'
		, 'errorIssueSource'
		, 'fileName'
		, 'modifiedOn'
	]
});
