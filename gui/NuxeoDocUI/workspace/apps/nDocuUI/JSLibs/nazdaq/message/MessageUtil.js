Ext.define(
	'nazdaq.message.MessageUtil', {
	alias: 'nMessageUtil',
	alternateClassName: 'nMessageUtil',
	statics: {
		createRequestMessage: function (message) {
			var jsonMessage = {
				header: message.getMessageHeader().getHeaders().map,
				payload: message.getPayload()
			};

			var jsonMsgString = Ext.JSON.encode(jsonMessage);

			return jsonMsgString;
		},

		getResponseMessage: function (result) {
			return Ext.JSON.decode(result.responseText);
		}
	}
});