Ext.define(
	'nazdaq.message.MessageHeader', {
		alias: 'nMessageHeader',
		alternateClassName: 'nMessageHeader',
		statics: {

			ID					: 'id',
			CLIENT_UID			: 'clientUid',
			CORRELATION_ID		: 'correlationId',
			TIMESTAMP			: 'timestamp',
			SEQUENCE_NUMBER		: 'sequenceNumber',
			SEQUENCE_SIZE		: 'sequenceSize',

			SOURCE				: 'source',
			CLIENT_SOURCE		: 'clientSource',
			DESTINATION			: 'destination',
			SERVER_DESTINATION	: 'serverDestination',

			ACTION_TYPE			: 'actionType',
			CONTENT_TYPE		: 'contentType',
			PRIORITY			: 'priority',
			TTL					: 'ttl',
			EXPIRATION_DATE		: 'expirationDate',

			LOCATION_ID			: 'locationId',

			USER_ID				: 'userId',
			USER_NAME			: 'userName',
			ENC_TYPE			: 'encType',

			CLIENT_DATA			: 'clientData',

			nMESSAGE_NAME		: 'nazdaq.message.MessageHeader',
			nMESSAGE_VER		: 'nMessageVer',

			UUID 				: Ext.create('Ext.data.identifier.Uuid'),

			sequence			: 0,
			version				: '1.0.0',

			localClientId		: null,
			CLIENT_UNIQUE_ID	: 'secretId',

			init : function() {
				nMessageHeader.generateClientId();
			},

			generateClientId : function() {
				if (nMessageHeader.localClientId == null && Ext.util.LocalStorage.supported)
				{
					var localStore = new Ext.state.LocalStorageProvider(Desktop.app.getName());
					nMessageHeader.localClientId = localStore.get('local.client.id');

					if (nMessageHeader.localClientId == null)
					{
						nMessageHeader.localClientId = nMessageHeader.UUID.generate();
						localStore.set('local.client.id', nMessageHeader.UUID.generate());
					}
				}

				console.log('local.client.id :' + this.localClientId);
			}

		},

		config: {
			headers			: Ext.create('Ext.util.MixedCollection'),
			subSource		: null,
			subDestination	: null,
			clientData		: null
		},

		constructor: function (config) {
			this.initConfig(config);

			nMessageHeader.sequence++;

			if (nMessageHeader.localClientId == null) {
				nMessageHeader.generateClientId();
			}

			this.getHeaders().add(nMessageHeader.ID, nMessageHeader.UUID.generate());
			this.getHeaders().add(nMessageHeader.TIMESTAMP, Date.now());
			this.getHeaders().add(nMessageHeader.nMESSAGE_VER, nMessageHeader.version);
			this.getHeaders().add(nMessageHeader.SEQUENCE_NUMBER, nMessageHeader.sequence);
			this.getHeaders().add(nMessageHeader.CLIENT_UID, nMessageHeader.localClientId);

			//console.log('ctor - nMessageHeader');
		},


		applyHeaders: function (headers) {

			// Return an Array or just add to the existing object

			if (headers)
			{
				if(!this.headers) {
					//ctor call
					return headers;
				}
				else
				{

					if (headers.isMixedCollection && headers.getCount() > 0)
					{
						// passed in header object, avoid any overrides to orig values
						if (headers.containsKey(this.ID)) {
							headers.removeAtKey(this.ID);
						}

						if (headers.containsKey(this.TIMESTAMP)) {
							headers.removeAtKey(this.TIMESTAMP);
						}

						if (headers.containsKey(this.nMESSAGE_VER)) {
							headers.removeAtKey(this.nMESSAGE_VER);
						}

						this.headers.addAll(headers.map);
					}
				}
			}
		},
		get: function(key) {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(key);
			}

			return null;
		},

		set: function (key, value) {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				this.getHeaders().add(key, value);

				return;
			}
		},

		getId: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.ID);
			}

			return null;
		},
		getCorrelationId: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.CORRELATION_ID);
			}

			return null;
		},
		getSource: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.SOURCE);
			}

			return null;
		},
		getDestination: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.DESTINATION);
			}

			return null;
		},
		getActionType: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.ACTION_TYPE);
			}

			return null;
		},
		getContentType: function() {
			if (this.getHeaders() != null && this.getHeaders().isMixedCollection) {
				return this.getHeaders().getByKey(nMessageHeader.CONTENT_TYPE);
			}

			return null;
		},

		printHeaders: function() {
			console.log('Headers:\n' +
						nMessageHeader.TIMESTAMP + ': ' + this.get(nMessageHeader.TIMESTAMP) + '\n' +
						nMessageHeader.ID + ': ' + this.get(nMessageHeader.ID) + '\n' +
						nMessageHeader.CORRELATION_ID + ': ' + this.get(nMessageHeader.CORRELATION_ID) + '\n' +
						nMessageHeader.SOURCE + ': ' + this.get(nMessageHeader.SOURCE) + '\n' +
						nMessageHeader.DESTINATION + ': ' + this.get(nMessageHeader.DESTINATION) + '\n' +
						nMessageHeader.ACTION_TYPE + ': ' + this.get(nMessageHeader.ACTION_TYPE) + '\n' +
						nMessageHeader.CONTENT_TYPE + ': ' + this.get(nMessageHeader.CONTENT_TYPE) + '\n' +
						nMessageHeader.PRIORITY + ': ' + this.get(nMessageHeader.PRIORITY) + '\n' +
						nMessageHeader.SEQUENCE_NUMBER + ': ' + this.get(nMessageHeader.SEQUENCE_NUMBER) + '\n' +
						nMessageHeader.SEQUENCE_SIZE + ': ' + this.get(nMessageHeader.SEQUENCE_SIZE) + '\n' +
						nMessageHeader.EXPIRATION_DATE + ': ' + this.get(nMessageHeader.EXPIRATION_DATE) + '\n' +
						nMessageHeader.LOCATION_ID + ': ' + this.get(nMessageHeader.LOCATION_ID) + '\n' +
						nMessageHeader.USER_ID + ': ' + this.get(nMessageHeader.USER_ID) + '\n' +
						nMessageHeader.USER_NAME + ': ' + this.get(nMessageHeader.USER_NAME) + '\n' +
						nMessageHeader.ENC_TYPE + ': ' + this.get(nMessageHeader.ENC_TYPE));
		}

	});