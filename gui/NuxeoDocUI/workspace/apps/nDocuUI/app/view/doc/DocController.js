var ctFd = null, ctTd = null;

Ext.define('Desktop.view.doc.DocController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.doc-doc',

    requires: [],

	/**
	* Reordered
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Send all request to server
	**/
	sendRequest: function(actionName, contentType, payload, header) {

		var component = null;

		if (Ext.isEmpty(payload)) {
			payload = new Array();
		}
		else if(!Ext.isEmpty(payload.reference)){
			component = payload.reference;
		}
		else if(!Ext.isEmpty(payload[0]) && payload[0].reference != 'undefined'){
			component = payload[0].reference;
		} 

		var request = {
			actionName      : actionName,
			contentType     : contentType,
			requestId       : null,
			requestType     : null,
			header          : header,
			body            : payload,
			message         : null,
			dispatchType    : null,
			sender          : this,
			component       : component,
			onSuccess       : this.onSuccess,
			onError         : this.onError
		};

		var requestId = nMessageProcessor.sendRequest(request); 
	},

	/**
	* Added comment
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Handls all responses from server
	**/
	onSuccess: function(request, response) {
		var me = this;

		//if(request.component == 'onPanelShow'){
		if(request.component == 'onDocSrc'){
			var items = response.payload[1].payload;
			var docStore = Ext.data.StoreManager.lookup('gDocStore');

			docStore.removeAll();
			docStore.add(items);
		}
		else if(request.component == 'onTmpltSrc'){
			var items = response.payload[1].payload;
			var tmpltStore = Ext.data.StoreManager.lookup('gTmpltStore');

			tmpltStore.removeAll();
			tmpltStore.add(items);
		}
		else if(request.component == 'onDocCtSrc'){
			var items = response.payload[1].payload;
			var docCtStore = Ext.data.StoreManager.lookup('gDocCtStore');

			for (var i = items.length - 1; i >= 0; i--) {
				items[i].senderEmail = items[i].senderAddress.match(/[^@<\s]+@[^@\s>]+/g)[0];//.replace(/</g, '=>>').replace(/>/g, '')//.match(/[^@<\s]+@[^@\s>]+/g)[0];
			}

			docCtStore.removeAll();
			docCtStore.add(items);
		}
		else if(request.component == 'docCountUpdate'){
			request.sender.onDocCtSrc();
		}
		else if(request.component == 'getDocDetailsGrid'){
			var items = response.payload[1].payload;
			var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');

			docDetailsStore.removeAll();
			docDetailsStore.add(items);
		}
		else if(request.component == 'docUpdate'){
			var body = request.body;

			var msg = null;

			if(Ext.isEmpty(body.docId)){
				msg = 'Doc added successfully';
			}
			else{
				msg = 'Doc updated successfully';
			}

			var alertBox = Ext.Msg.alert("Success", msg);

			setTimeout(function(){
				alertBox.hide();
			}, 3000);

			request.sender.getView().destroy();
		}
	 },

	/*
	* Gets all data on Panel show
	**/
	onPanelShow : function(){
		var me = this;

		me.onDocCtSrc();
		me.onTmpltSrc();
		me.onDocSrc();
	},

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Gets all data by template search button click on date range
	**/
	onDocCtSrc: function(){

		var me = this;

		var isfd = Ext.isEmpty(me.lookupReference('ctFromDate'));
		
		if(!isfd){
			isfd = Ext.isEmpty(me.lookupReference('ctFromDate').value);
		}

		var istd = Ext.isEmpty(me.lookupReference('ctToDate'));
		if(!istd){
			istd = Ext.isEmpty(me.lookupReference('ctToDate').value);
		}

		if(!isfd){
			ctFd = me.lookupReference('ctFromDate').value;
		}
		else if(!Ext.isEmpty(ctFd)){
			isfd = false;
		}

		if(!istd){
			ctTd = me.lookupReference('ctToDate').value;
		}
		else if(!Ext.isEmpty(ctTd)){
			istd = false;
		}

		var header = {
			userModKey  : gUserId,
			userId      : gUserId,
			userName    : gUserAlias
		};

		var payload = {
			userModKey 	: gUserId,
			fromDate 	: Ext.Date.format(isfd ? last_n_days(): ctFd, 'Y-m-d'),
			toDate 		: Ext.Date.format(istd ? new Date(): ctTd, 'Y-m-d'),
			reference 	: 'onDocCtSrc'
		};

		me.sendRequest(appActionType.ACTION_TYPE_SELECT,  appContentType.CONTENT_TYPE_DOC_COUNT, payload, header);
	},

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Gets all data by template search button click on date range
	**/
	onTmpltSrc: function(){

		var me = this;

		var header = {
			userModKey  : gUserId,
			userId      : gUserId,
			userName    : gUserAlias
		};

		var payload = {
			userModKey 	: gUserId,
			fromDate 	: Ext.Date.format(Ext.isEmpty(me.lookupReference('fromDate').value) ? last_n_days(): me.lookupReference('fromDate').value, 'Y-m-d'),
			toDate 		: Ext.Date.format(Ext.isEmpty(me.lookupReference('toDate').value) ? new Date(): me.lookupReference('toDate').value, 'Y-m-d'),			
			reference 	: 'onTmpltSrc'
		};

		me.sendRequest(appActionType.ACTION_TYPE_SELECT,  appContentType.CONTENT_TYPE_TEMPLATE, payload, header);
	},

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Gets all data by doc search button click on date range
	**/
	onDocSrc: function(){

		var me = this;
		var header = {
			userModKey  : gUserId,
			userId      : gUserId,
			userName    : gUserAlias
		};

		var payload = {
			userModKey 	: gUserId,
			fromDate 	: Ext.Date.format(Ext.isEmpty(me.lookupReference('docFromDate').value) ? last_n_days(): me.lookupReference('docFromDate').value, 'Y-m-d'),
			toDate 		: Ext.Date.format(Ext.isEmpty(me.lookupReference('docToDate').value) ? new Date(): me.lookupReference('docToDate').value, 'Y-m-d'),
			reference 	: 'onDocSrc'
		};

		me.sendRequest(appActionType.ACTION_TYPE_SELECT,  appContentType.CONTENT_TYPE_DOC, payload, header);
	},
	
	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* On Template grid check box selection listener
	**/
	onTmpltDataSlct:function(grid, selection){
		
		var me = this;

		if(grid.getSelection().length == 1){
			me.lookupReference('viewBtn').setHidden(false);
		}
		else{
			me.lookupReference('viewBtn').setHidden(true);
		}
	}, 

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* for documents request
	**/
	onViewDoc: function(button, e, eOpts){
		
		var me = this, fileName = null, urlReq = null;

		if(me.lookupReference('tmpltGrid').getView().getSelection().length == 1){
			fileName = me.lookupReference('tmpltGrid').getView().getSelection()[0].data.fileName;
		}

		var pdfPanel = Ext.create('Ext.panel.Panel', {
			border 		: true,
			closable 	: true,
			floatable 	: true,
			floating 	: true,
			draggable 	: true,          
			title 		: "Document",
			width 		: (window.innerWidth) * 0.8,
			height 		: (window.innerHeight) * 0.9
		});

		pdfPanel.show();

		urlReq ='<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0" src="'
		+httpDocuments+'?fileName='+fileName+'"></iframe>';
		
		try{
			pdfPanel.body.update(urlReq);	
		}
		catch(err){
			Ext.Error.raise(err);
		}
	},

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Template grid item double click function
	**/
	onDocCtItmDblClick: function (dataview, record, item, index, e) {
		var me = this;
		var data = record.data;
		
		var docsTabPanel = this.lookupReference("docsTabPanel");

		var win = Ext.create('Desktop.view.doc.DocCountForm');
		
		win.setTitle('Doc Count: ' + data.docCountId + '_' + data.docCountVer);

		/*load record in form*/
		var form = win.getForm();
		form.loadRecord(record);

		if(data.isAbbyError == 1){
			win.lookupReference('isAbbyError').setValue("NO");
		}
		else{
			win.lookupReference('isAbbyError').setValue("YES");
		}

		if(data.isAbbyProcessed == 1){
			win.lookupReference('isAbbyProcessed').setValue("YES");
		}
		else{
			win.lookupReference('isAbbyProcessed').setValue("NO");
		}

		if(data.isProcessedSuccessfully == 1){
			win.lookupReference('isProcessedSuccessfully').setValue("YES");
		}
		else{
			win.lookupReference('isProcessedSuccessfully').setValue("NO");
		}
		
		win.lookupReference('updDocCount').setHidden(false);

		docsTabPanel.add(win).show();
	},

	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-22
	* Doc grid item double click function
	**/
	onDoubleClick: function (dataview, record, item, index, e) {
		var me = this;
		var data = record.data;
		
		var docsTabPanel = this.lookupReference("docsTabPanel");

		var win = Ext.create('Desktop.view.doc.DocForm');
		
		win.setTitle('Doc: ' + data.docId + '_' + data.docVer);
		
		/*load record in form*/
		var form = win.getForm();
		form.loadRecord(record);
		
		win.lookupReference('documentDate').setValue(this.formatDate(data.documentDate));
		win.lookupReference('despatchDate').setValue(this.formatDate(data.despatchDate));

		docsTabPanel.add(win).show();

		me.getDocDetailsGrid(data.docId);
	},
	
	/**
	* @author: Md. Mahbub Hasan Mohiuddin
	* @since: 2019-12-23
	* Doc count value update functin
	**/
	docCountUpdate: function(button, e, eOpts){
		var me  = this;

		me.lookupReference('updDocCount').setHidden(true);

		var docCountId 	    		= me.lookupReference('docCountId').getValue();
		var docCountVer 	    	= me.lookupReference('docCountVer').getValue();
		var isAbbyError         	= me.lookupReference('isAbbyError').getValue() == 'YES' ? 0:1;
		var isAbbyProcessed     	= me.lookupReference('isAbbyProcessed').getValue() == 'YES' ? 1:0;
		var isProcessedSuccessfully = me.lookupReference('isProcessedSuccessfully').getValue() == 'YES' ? 1:0;
		var docType           		= me.lookupReference('docType').getValue();
		var fileName           		= me.lookupReference('fileName').getValue();

		var header = {
			userModKey 	: gUserId,
			userId 		: gUserId,
			userName 	: gUserAlias
		};
		var payload = {
			docCountId 				: docCountId,
			docCountVer 			: docCountVer,
			docType 				: docType,
			fileName 				: fileName,
			isAbbyError 			: isAbbyError,
			isAbbyProcessed 		: isAbbyProcessed,
			isProcessedSuccessfully : isProcessedSuccessfully,
			reference 				: 'docCountUpdate'
		};

		Ext.MessageBox.confirm('Confirm', 'Are you sure?', function(btn) {

			if (btn == 'yes') {
				me.sendRequest(appActionType.ACTION_TYPE_UPDATE,  appContentType.CONTENT_TYPE_DOC_COUNT, payload, header);
			}
		});

		me.getView().destroy();
	},

	getDocDetailsGrid: function(docId){
		var me = this;
		var header = {
			userModKey  : gUserId,
			userId      : gUserId,
			userName    : gUserAlias
		};

		var payload = {
			docId 		: docId,
			userModKey 	: gUserId,
			reference 	: 'getDocDetailsGrid'
		};

		me.sendRequest(appActionType.ACTION_TYPE_SELECT_DOC_DETAILS,  appContentType.CONTENT_TYPE_DOC_DETAILS, payload, null);
	},

	docUpdate: function(button, eOpts) {
		var me = this, btnRef = button.reference;

		var docId 	            = me.lookupReference('docId').getValue();
		var docVer 	           	= me.lookupReference('docVer').getValue();
		var supplierNumber     	= me.lookupReference('supplierNumber').getValue();
		var deliveryNoteNo 	   	= me.lookupReference('deliveryNoteNo').getValue();
		var receiptNo          	= me.lookupReference('receiptNo').getValue();
		var documentNumber     	= me.lookupReference('documentNumber').getValue();
		var documentDate 	   	= me.lookupReference('documentDate').getValue();
		var recipientOfInvoice 	= me.lookupReference('recipientOfInvoice').getValue();
		var vatNumber          	= me.lookupReference('vatNumber').getValue();
		var despatchDate 	   	= me.lookupReference('despatchDate').getValue();
		var invoiceNumber      	= me.lookupReference('invoiceNumber').getValue();
		var currency           	= me.lookupReference('currency').getValue();
		var accountNumber      	= me.lookupReference('accountNumber').getValue();
		var voucherNumber      	= me.lookupReference('voucherNumber').getValue();
		var faxNumber          	= me.lookupReference('faxNumber').getValue();
		var associationNumber 	= me.lookupReference('associationNumber').getValue();
		var netInvoiceTotal    	= me.lookupReference('netInvoiceTotal').getValue();
		var orderId            	= me.lookupReference('orderId').getValue();
		var vat 	           	= me.lookupReference('vat').getValue();
		var vatRate 			= me.lookupReference('vatRate').getValue();
		var vatPayable 		   	= me.lookupReference('vatPayable').getValue();
		var invoiceAmount 	   	= me.lookupReference('invoiceAmount').getValue();
		var paymentDetails     	= me.lookupReference('paymentDetails').getValue();
		var totalBeforeVat     	= me.lookupReference('totalBeforeVat').getValue();
		var totalAmountDue     	= me.lookupReference('totalAmountDue').getValue();
		var discount 	       	= me.lookupReference('discount').getValue();
		var deliveryNote 	   	= me.lookupReference('deliveryNote').getValue();
		var deliveryDetails    	= me.lookupReference('deliveryDetails').getValue();
		var paymentDetails     	= me.lookupReference('paymentDetails').getValue();
		var companyAddress      = me.lookupReference('companyAddress').getValue();
		var invoiceTo 	        = me.lookupReference('invoiceTo').getValue();
		var deliverTo           = me.lookupReference('deliverTo').getValue();

		if(!Ext.isEmpty(documentDate)){
			documentDate = Ext.Date.format(documentDate, "Ymd")
		}
		else{
			documentDate = null;
		}

		if(!Ext.isEmpty(despatchDate)){
			despatchDate = Ext.Date.format(despatchDate, "Ymd")
		}
		else{
			despatchDate = null;
		}

		var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');
		var items = docDetailsStore.data.items;

		var docListArr = [];
		
		for(var i = 0; i < items.length; i++){

			var docDetailsId = null, docDetailsVer = null;

			data = items[i].data;

			if(!Ext.isEmpty(data.docDetailsId)){
				docDetailsId = data.docDetailsId;
			}

			if(!Ext.isEmpty(data.docDetailsVer)){
				docDetailsVer = data.docDetailsVer;
			}
			else{
				docDetailsId = null;
			}

			var obj = {
				userModKey		: gUserId,
				docDetailsId	: docDetailsId,
				docDetailsVer	: docDetailsVer,
				docId           : data.docId,
				partNo          : data.partNo,
				itemQty         : data.itemQty,
				referenceNo     : data.referenceNo,
				itemName        : data.itemName,
				itemCode        : data.itemCode,
				rent            : data.rent,
				pack            : data.pack,
				itemDescription : data.itemDescription,
				propertyAddress : data.propertyAddress,
				trade           : data.trade,
				unitPrice       : data.unitPrice,
				totalPrice 		: data.totalPrice,
				netValue        : data.netValue,
				valueOfGoods    : data.valueOfGoods,
				insurancePremium: data.insurancePremium,
			};

			docListArr.push(obj);
		}

		var header = {
			userModKey 	: gUserId,
			userId 		: gUserId,
			userName 	: gUserAlias
		};

		var payload = {
			userModKey			: gUserId,
			supplierNumber      : supplierNumber,
			deliveryNoteNo      : deliveryNoteNo,
			receiptNo           : receiptNo,
			documentNumber      : documentNumber,
			documentDate        : documentDate,
			recipientOfInvoice  : recipientOfInvoice,
			vatNumber           : vatNumber,
			despatchDate        : despatchDate,
			invoiceNumber       : invoiceNumber,
			currency            : currency,
			accountNumber       : accountNumber,
			voucherNumber       : voucherNumber,
			faxNumber           : faxNumber,
			associationNumber   : associationNumber,
			netInvoiceTotal     : netInvoiceTotal,
			orderId             : orderId,
			vat                 : vat,
			vatRate             : vatRate,
			vatPayable          : vatPayable,
			invoiceAmount       : invoiceAmount,
			paymentDetails      : paymentDetails,
			totalBeforeVat      : totalBeforeVat,
			totalAmountDue      : totalAmountDue,
			discount            : discount,
			invoiceAmount       : invoiceAmount,
			deliveryNote        : deliveryNote,
			deliveryDetails     : deliveryDetails,
			paymentDetails      : paymentDetails,
			companyAddress      : companyAddress,
			invoiceTo           : invoiceTo,
			deliverTo           : deliverTo,
			docDetailsList 		: docListArr,
			reference 	        : 'docUpdate',
		};
			
		Ext.MessageBox.confirm('Confirm', 'Are you sure?', function(btn) {

			if (btn == 'yes') {
				if(btnRef == 'documentUpdateBtn'){
					payload.docId = docId;
					payload.docVer = docVer;

					me.sendRequest(appActionType.ACTION_TYPE_UPDATE, appContentType.CONTENT_TYPE_DOC, payload, null);
				}
				else if (btnRef == 'documentSaveBtn') {
					me.sendRequest(appActionType.ACTION_TYPE_NEW, appContentType.CONTENT_TYPE_DOC, payload, null);
				}
			}
		});
	},

	onWindowClose: function () {
		this.view.destroy();
	},

	onDocDetailsDblClk: function (dataview, record, item, index, e) {
		var me = this;
		var data = record.data;

		var win = Ext.create('Desktop.view.doc.DocDetailsForm');

		win.setTitle('Doc Details (ID: ' + data.docDetailsId + ')');
		win.lookupReference('docDetailsId').setValue(data.docDetailsId);
		win.lookupReference('docDetailsVer').setValue(data.docDetailsVer);
		win.lookupReference('docId').setValue(data.docId);
		win.lookupReference('partNo').setValue(data.partNo);
		win.lookupReference('itemQty').setValue(data.itemQty);
		win.lookupReference('referenceNo').setValue(data.referenceNo);
		win.lookupReference('itemName').setValue(data.itemName);
		win.lookupReference('itemCode').setValue(data.itemCode);
		win.lookupReference('rent').setValue(data.rent);
		win.lookupReference('pack').setValue(data.pack);
		win.lookupReference('itemDescription').setValue(data.itemDescription);
		win.lookupReference('propertyAddress').setValue(data.propertyAddress);
		win.lookupReference('trade').setValue(data.trade);
		win.lookupReference('unitPrice').setValue(data.unitPrice);
		win.lookupReference('totalPrice').setValue(data.totalPrice);
		win.lookupReference('netValue').setValue(data.netValue);
		win.lookupReference('valueOfGoods').setValue(data.valueOfGoods);
		win.lookupReference('insurancePremium').setValue(data.insurancePremium);
		win.lookupReference('modifiedOn').setValue(data.modifiedOn);

		win.show();
	},

	onFormReset: function(button, e, eOpts){
		var me = this;
		Ext.MessageBox.confirm('Confirm', 'Are you sure?', function(btn) {

		if (btn == 'yes') {
			me.lookupReference('docId').reset();
			me.lookupReference('docVer').reset();
			me.lookupReference('modifiedOn').reset();
			me.lookupReference('supplierNumber').reset();
			me.lookupReference('deliveryNoteNo').reset();
			me.lookupReference('receiptNo').reset();
			me.lookupReference('documentNumber').reset();
			me.lookupReference('documentDate').reset();
			me.lookupReference('recipientOfInvoice').reset();
			me.lookupReference('vatNumber').reset();
			me.lookupReference('despatchDate').reset();
			me.lookupReference('invoiceNumber').reset();
			me.lookupReference('currency').reset();
			me.lookupReference('accountNumber').reset();
			me.lookupReference('voucherNumber').reset();
			me.lookupReference('faxNumber').reset();
			me.lookupReference('associationNumber').reset();
			me.lookupReference('netInvoiceTotal').reset();
			me.lookupReference('orderId').reset();
			me.lookupReference('vat').reset();
			me.lookupReference('vatRate').reset();
			me.lookupReference('vatPayable').reset();
			me.lookupReference('invoiceAmount').reset();
			me.lookupReference('paymentDetails').reset();
			me.lookupReference('totalBeforeVat').reset();
			me.lookupReference('discount').reset();
			me.lookupReference('totalAmountDue').reset();
			me.lookupReference('modifiedOn').reset();
			me.lookupReference('deliveryNote').reset();
			me.lookupReference('deliveryDetails').reset();
			me.lookupReference('companyAddress').reset();
			me.lookupReference('invoiceTo').reset();
			me.lookupReference('deliverTo').reset();
			}
		});
	},

	docDetailsUpdate: function(button, e, eOpts){
		var btnRef = button.reference, me  = this;

		var docDetailsId 	    = me.lookupReference('docDetailsId').getValue();
		var docDetailsVer 	    = me.lookupReference('docDetailsVer').getValue();
		var docId         	    = me.lookupReference('docId').getValue();
		var partNo     		    = me.lookupReference('partNo').getValue();
		var itemQty 	   	    = me.lookupReference('itemQty').getValue();
		var unitPrice           = me.lookupReference('unitPrice').getValue();
		var referenceNo         = me.lookupReference('referenceNo').getValue();
		var itemName 	        = me.lookupReference('itemName').getValue();
		var itemCode 	        = me.lookupReference('itemCode').getValue();
		var rent          	    = me.lookupReference('rent').getValue();
		var pack 	     	    = me.lookupReference('pack').getValue();
		var itemDescription     = me.lookupReference('itemDescription').getValue();
		var propertyAddress     = me.lookupReference('propertyAddress').getValue();
		var trade      			= me.lookupReference('trade').getValue();
		var totalPrice      	= me.lookupReference('totalPrice').getValue();
		var netValue          	= me.lookupReference('netValue').getValue();
		var valueOfGoods 	    = me.lookupReference('valueOfGoods').getValue();
		var insurancePremium    = me.lookupReference('insurancePremium').getValue();

		if(Ext.isEmpty(partNo)){
			partNo = null;
		}
		if(Ext.isEmpty(itemQty)){
			itemQty = null;
		}
		if(Ext.isEmpty(unitPrice)){
			unitPrice = null;
		}
		if(Ext.isEmpty(totalPrice)){
			totalPrice = null;
		}
		if(Ext.isEmpty(netValue)){
			netValue = null;
		}
		if(Ext.isEmpty(valueOfGoods)){
			valueOfGoods = null;
		}
		if(Ext.isEmpty(insurancePremium)){
			insurancePremium = null;
		}

		if(Ext.isEmpty(referenceNo)){
			referenceNo = null;
		}
		if(Ext.isEmpty(itemName)){
			itemName = null;
		}
		if(Ext.isEmpty(itemCode)){
			itemCode = null;
		}
		if(Ext.isEmpty(rent)){
			rent = null;
		}
		if(Ext.isEmpty(pack)){
			pack = null;
		}
		if(Ext.isEmpty(itemDescription)){
			itemDescription = null;
		}
		if(Ext.isEmpty(propertyAddress)){
			propertyAddress = null;
		}
		if(Ext.isEmpty(trade)){
			trade = null;
		}

		var header = {
			userModKey 	: gUserId,
			userId 		: gUserId,
			userName 	: gUserAlias
		};
		var payload = {
			docId           : docId,
			partNo          : partNo,
			itemQty         : itemQty,
			referenceNo     : referenceNo,
			itemName        : itemName,
			itemCode        : itemCode,
			rent            : rent,
			pack            : pack,
			itemDescription : itemDescription,
			propertyAddress : propertyAddress,
			trade           : trade,
			unitPrice       : unitPrice,
			totalPrice 		: totalPrice,
			netValue        : netValue,
			valueOfGoods    : valueOfGoods,
			insurancePremium: insurancePremium,
			reference: 'docDetailsUpdate'
		};

		var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');
		if(btnRef == 'docDetailsUpdtBtn'){
			payload.docDetailsId = docDetailsId;
			payload.docDetailsVer = docDetailsVer;

			Ext.MessageBox.confirm('Confirm', 'Are you sure?', function(btn) {

				if (btn == 'yes') {
					var record = docDetailsStore.findRecord('docDetailsId', docDetailsId);

					record.set('docId'				, docId);
					record.set('partNo'				, partNo);
					record.set('itemQty'			, itemQty);
					record.set('referenceNo'		, referenceNo);
					record.set('itemName'			, itemName);
					record.set('itemCode'			, itemCode);
					record.set('rent'				, rent);
					record.set('pack'				, pack);
					record.set('itemDescription'	, itemDescription);
					record.set('propertyAddress'	, propertyAddress);
					record.set('trade'				, trade);
					record.set('unitPrice'			, unitPrice);
					record.set('totalPrice'			, totalPrice);
					record.set('netValue'			, netValue);
					record.set('valueOfGoods'		, valueOfGoods);
					record.set('insurancePremium'	, insurancePremium);
				}
			});
		}
		else if(btnRef == 'docDetailsSaveBtn'){

			var maxId = null;

			if (docDetailsStore.getCount() > 0){
			  	maxId = docDetailsStore.getAt(0).get('docDetailsId'); // initialise to the first record's id value.
			  	
			  	docDetailsStore.each(function(rec){// go through all the records
			  	
					maxId = Math.max(maxId, rec.get('docDetailsId'));
			  	});
			}

			if(!Ext.isEmpty(maxId)){
				payload.docDetailsId = maxId + 1;
			}
			else{
				payload.docDetailsId = gUserId;
			}
		Ext.MessageBox.confirm('Confirm', 'Are you sure?', function(btn) {

			if (btn == 'yes') {
				docDetailsStore.add(payload);
			}
		});
			
		}

		me.getView().destroy();
	},

	onAddNewDocDetails: function(button, e, eOpts){
		var win = Ext.create('Desktop.view.doc.DocDetailsForm');
		
		var docId =  this.lookupReference('docId').getValue();
		win.lookupReference('docId').setValue(docId);
		win.lookupReference('docDetailsUpdtBtn').setHidden(true);
		win.lookupReference('docDetailsSaveBtn').setHidden(false);

		win.show();
	},

	onAddNewCreditNoteDetails: function(button, e, eOpts){
		var win = Ext.create('Desktop.view.doc.CreditNoteDetailsForm');
		
		var docId =  this.lookupReference('docId').getValue();
		win.lookupReference('docId').setValue(docId);
		win.lookupReference('docDetailsUpdtBtn').setHidden(true);
		win.lookupReference('docDetailsSaveBtn').setHidden(false);

		win.show();
	},

	onAddNewInvoiceDoc: function(button, e, eOpts){
		var me = this;
		
		var docsTabPanel = me.lookupReference("docsTabPanel");

		var win = Ext.create('Desktop.view.doc.DocForm');
		
		var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');
		docDetailsStore.removeAll();
		docDetailsStore.add();

		win.setTitle('New Invoice');
		win.lookupReference('documentUpdateBtn').setHidden(true);
		win.lookupReference('documentSaveBtn').setHidden(false);
		
		docsTabPanel.add(win).show();
	},


// >>>>>>>>>>>>>>>>> Ridoy edit <<<<<<<<<<<<<<<<<<<<<<<<<<
	onAddNewCreditNoteDoc: function(button, e, eOpts){
		var me = this;
		
		var docsTabPanel = me.lookupReference("docsTabPanel");

		var win = Ext.create('Desktop.view.doc.CreditNoteDocForm');
		
		/* var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');
		docDetailsStore.removeAll();
		docDetailsStore.add(); */

		win.setTitle('New Credit Note');
		win.lookupReference('documentUpdateBtn').setHidden(true);
		win.lookupReference('documentSaveBtn').setHidden(false);
		
		docsTabPanel.add(win).show();
	},

	onAddNewStatementDoc: function(button, e, eOpts){
		var me = this;
		
		var docsTabPanel = me.lookupReference("docsTabPanel");

		var win = Ext.create('Desktop.view.doc.StatementDocFrom');
		
		/* var docDetailsStore = Ext.data.StoreManager.lookup('docDetailsStore');
		docDetailsStore.removeAll();
		docDetailsStore.add(); */

		win.setTitle('New Statement');
		win.lookupReference('documentUpdateBtn').setHidden(true);
		win.lookupReference('documentSaveBtn').setHidden(false);
		
		docsTabPanel.add(win).show();
	},

	//________________________________________________________________________________________________

	onDocGridFilter : function (component, newValue, oldValue, eOpts) {
		var store = Ext.data.StoreManager.lookup('gDocStore');
		store.clearFilter();

		if (newValue) {

			var matcher = new RegExp(Ext.String.escapeRegex(newValue), "i");

			store.filter({
				filterFn: function(record) {
					return 	matcher.test(record.get('docId')) ||
							matcher.test(record.get('docVer')) ||
							matcher.test(record.get('modifiedOn')) ||
							matcher.test(record.get('supplierNumber')) ||
							matcher.test(record.get('deliveryNoteNo')) ||
							matcher.test(record.get('receiptNo')) ||	
							matcher.test(record.get('documentNumber')) ||
							matcher.test(record.get('documentDate')) ||
							matcher.test(record.get('recipientOfInvoice')) ||
							matcher.test(record.get('vatNumber')) ||
							matcher.test(record.get('despatchDate')) ||
							matcher.test(record.get('invoiceNumber')) ||
							matcher.test(record.get('currency'))||
							matcher.test(record.get('accountNumber')) ||
							matcher.test(record.get('voucherNumber')) ||
							matcher.test(record.get('faxNumber')) ||
							matcher.test(record.get('associationNumber')) ||
							matcher.test(record.get('netInvoiceTotal')) ||
							matcher.test(record.get('orderId')) ||
							matcher.test(record.get('vat')) ||
							matcher.test(record.get('vatRate')) ||
							matcher.test(record.get('vatPayable')) ||
							matcher.test(record.get('invoiceAmount')) ||
							matcher.test(record.get('paymentDetails'))||
							matcher.test(record.get('totalBeforeVat')) ||
							matcher.test(record.get('totalAmountDue')) ||
							matcher.test(record.get('discount')) ||
							matcher.test(record.get('deliveryNote'))||
							matcher.test(record.get('deliveryDetails')) ||
							matcher.test(record.get('paymentDetails')) ||
							matcher.test(record.get('companyAddress')) ||
							matcher.test(record.get('invoiceTo')) ||
							matcher.test(record.get('deliverTo')) ||
							matcher.test(record.get('fileName')); 
				}
			});
		}
		
		component.focus();
	},

	onDocDetailsGridFilter : function (component, newValue, oldValue, eOpts) {
		var store = Ext.data.StoreManager.lookup('docDetailsStore');
		store.clearFilter();

		if (newValue) {

			var matcher = new RegExp(Ext.String.escapeRegex(newValue), "i");

			store.filter({
				filterFn: function(record) {
					return	matcher.test(record.get('docDetailsId')) ||
							matcher.test(record.get('docDetailsVer')) ||
							matcher.test(record.get('docId')) ||
							matcher.test(record.get('partNo')) ||
							matcher.test(record.get('itemQty')) ||
							matcher.test(record.get('referenceNo')) ||
							matcher.test(record.get('itemName')) ||
							matcher.test(record.get('itemCode')) ||	
							matcher.test(record.get('rent')) ||
							matcher.test(record.get('pack')) ||
							matcher.test(record.get('itemDescription')) ||
							matcher.test(record.get('propertyAddress')) ||
							matcher.test(record.get('trade')) ||
							matcher.test(record.get('unitPrice')) ||
							matcher.test(record.get('totalPrice'))||
							matcher.test(record.get('netValue')) ||
							matcher.test(record.get('valueOfGoods')) ||
							matcher.test(record.get('insurancePremium'))||
							matcher.test(record.get('modifiedOn'));
				}
			});
		}
		component.focus();
	},

	formatDate: function(date){

		if(!Ext.isEmpty(date)){
			return new Date(date);
		}
		else{
			return null;
		}
	}

});
