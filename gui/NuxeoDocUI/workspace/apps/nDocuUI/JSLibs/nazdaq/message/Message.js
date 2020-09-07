Ext.define(
	'nazdaq.message.Message', {
	alias: 'nMessage',
	alternateClassName: 'nMessage',
	requires: [
		// 'nazdaq.message.MessageHeader',
		// 'nazdaq.message.MessageBuilder',
		// 'nazdaq.message.MessageUtil',
		// 'nazdaq.message.MessageProcessor',
		// 'nazdaq.message.MessageDispatcher'
	],

	config: {
		header: null,
		payload: null
	},

	constructor: function (config) {
		 this.initConfig(config);
	},

	applyHeader: function (header) {

		// TODO_H : Check for existing header

		var h = null;

		if (header == null) {
			h = Ext.create('nMessageHeader', {});
		}
		else if (header.isMixedCollection) {
			h = Ext.create('nMessageHeader', {headers: header});

		}
		else if (Ext.getClassName(header) != nMessageHeader.CLASS_NAME) {
			Ext.Error.raise('Invlaid class object, class object must be type of nMessageHeader or Ext.util.MixedCollection');
		}

		return h;
	},

	toString: function() {
		return Ext.JSON.encode({header: this.getHeader().getHeaders().map, payload: this.getPayload()});
	}

	/*,
	applyPayload: function (payload) {
		// do encryption
	}*/
});