/*Ext.define('nDocuUI.view.login.LoginController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.login-login'

});*/
Ext.define('nDocuUI.view.login.LoginController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.login-login',

	onKeyPress: function (field, e) {
		if (e.getKey() == e.ENTER) {
			this.onLoginButtonClick(field, e);
		}
	},

	/*
	  Application access gateway. It sends user information to confirm them, 
	  whether they have an authentication.If then so, user can do their task 
	  as they should.
	*/
	onLoginButtonClickbk: function (button, e, eOpts) {

		var me = this;
		/*Login config for dev and prod+uat*/
		var isDevLogin = true;
		//var isDevLogin = true;

		loginCntrl = me;

		var loginForm = me.lookupReference('loginForm');
		//this.sender.getView().destroy();
		this.getView().destroy();

		/*show the Desktop view*/
		app = new Desktop.App();

		/*call function for checking idle user*/
		Ext.ux.ActivityMonitor.init({ verbose: true });
		Ext.ux.ActivityMonitor.start();

		if (isDevLogin) {

			/*for testing purpose*/
			var userId = 'docutech_user@naztech.us.com';
			var password = '123456';

			var payload = {
				loginName: userId,
				password: password,
				reference: 'onLoginButtonClick'
			};

			//me.sendRequest(appActionType.ACTION_TYPE_LOGIN, appContentType.CONTENT_TYPE_USER, payload);
		}
		else {

			/*prod and uat*/

				//loginForm.isValid()
			if (true) {

				if (true) {

					var userId = me.lookupReference('uid').value;

					var password = me.lookupReference('pass').value;

					var payload = {
						loginName: userId,
						password: password,
						reference: 'onLoginButtonClick'
					};

					me.sendRequest(appActionType.ACTION_TYPE_LOGIN, appContentType.CONTENT_TYPE_USER, payload);

					myMask = new Ext.LoadMask({
						msg: 'Please wait...',
						target: me.lookupReference('loginForm')
					});

					myMask.show();
				}

			}
		}
	},



	onLoginButtonClick: function (button, e, eOpts) {

		var me = this;
		/*Login config for dev and prod+uat*/
		var isDevLogin = false;
		//var isDevLogin = true;

		loginCntrl = me;

		var loginForm = me.lookupReference('loginForm');

		if (isDevLogin) {

			/*for testing purpose*/
			var userId = 'docutech_user@naztech.us.com';
			var password = '123456';

			var payload = {
				loginName: userId,
				password: password,
				reference: 'onLoginButtonClick'
			};

			me.sendRequest(appActionType.ACTION_TYPE_LOGIN, appContentType.CONTENT_TYPE_USER, payload);
		}
		else {

			/*prod and uat*/

			if (loginForm.isValid()) {
				var userId = me.lookupReference('uid').value;

				var password = me.lookupReference('pass').value;

				var payload = {
					loginName: userId,
					password: password,
					reference: 'onLoginButtonClick'
				};

				me.sendRequest(appActionType.ACTION_TYPE_LOGIN, appContentType.CONTENT_TYPE_USER, payload);

				myMask = new Ext.LoadMask({
					msg: 'Please wait...',
					target: me.lookupReference('loginForm')
				});

				myMask.show();

			}
		}
	},


	selectLogin: function (button, e, eOpts) {

		loginCntrl = this;
		userId = gUserId;

		var payload = {
			userId: userId,
			reference: 'selectLogin'
		};

		this.sendRequest(appActionType.ACTION_TYPE_SELECT_LOGIN, appContentType.CONTENT_TYPE_USER, payload);
	},

	onLogoutButtonClick: function (button, e, eOpts) {

		var me = this, userModKey = gUserId, userId = gUserId, loginVer = loginVer;

		var payload = {
			userModKey: userModKey,
			userId: userId,
			loginVer: loginVer
		};

		me.sendRequest(appActionType.ACTION_TYPE_LOGOUT, appContentType.CONTENT_TYPE_USER, payload);
	},

	onCancelButtonClick: function (button, e, eOpts) {

		this.lookupReference('loginForm').reset();
	},

	sendRequest: function (actionName, contentType, payload, header) {

		var component = null;

		if (Ext.isEmpty(payload)) {
			payload = new Array();
		} else if (!Ext.isEmpty(payload.reference)) {
			component = payload.reference;
		} else if (!Ext.isEmpty(payload[0]) && payload[0].reference != 'undefined') {
			component = payload[0].reference;
		}

		var request = {
			actionName: actionName,
			contentType: contentType,
			requestId: null,
			requestType: null,
			header: header,
			body: payload,
			message: null,
			dispatchType: null,
			sender: this,
			component: component,
			onSuccess: this.onSuccess,
			onError: this.onError
		};

		var requestId = nMessageProcessor.sendRequest(request);
	},

	onSuccess: function (request, response) {

		var items = response.payload[1].payload;

		if (request.component == 'onLoginButtonClick') {

			if (myMask) {
				myMask.destroy();
			}

			if (items.errMsg == null) {

				gUserId = items.userId;
				gUserVer = items.userVer;
				gLoginName = items.loginName;
				gUserAlias = items.userAlias;
				/*gPrimaryGroupName 	= items.primaryGroupName;
				gLegalEntityKey 	= items.legalEntityKey;

				for (i = 0; i < items.roleList.length; i++) {
					userRoles.add(items.roleList[i].name, items.roleList[i].name);
				}

				request.sender.selectLogin();
				request.sender.onLoginPanelShow();*/

				/*Remove Login Window*/
				this.sender.getView().destroy();

				/*show the Desktop view*/
				app = new Desktop.App();

				/*call function for checking idle user*/
				Ext.ux.ActivityMonitor.init({ verbose: true });
				Ext.ux.ActivityMonitor.start();

				isLoggedIn = true;
			}
			else {
				icon = Ext.MessageBox['error'.toUpperCase()];

				Ext.MessageBox.show({
					title: 'Error',
					msg: items.errMsg,
					//msg: "Please Provide Appropriate Credentials",
					buttons: Ext.MessageBox.OK,
					animateTarget: this.sender.lookupReference('loginBtn'),
					scope: this,
					fn: this.showResult,
					icon: icon
				});
			}
		}
		else if (request.component == 'selectLogin') {

			var data = response.payload[1].payload;
			loginVer = data.loginVer;
		}
		else if (request.component == 'feesNdCharges') {

			var data = response.payload[1].payload;

			gMinCharge = data[0].localAmount;

			for (var i = 0; i < data.length; i++) {
				feesMap.add(data[i].foreignAmount, data[i].feePercent);
			}
		}
		else if (request.component == 'sarbException') {

			var data = response.payload[1].payload;
			gSarbNotification = data;

			if (userRoles.containsKey('SA') || userRoles.containsKey('TECH_ADMIN')) {

				icon = Ext.MessageBox['info'.toUpperCase()];

				Ext.MessageBox.show({
					title: 'Information',
					msg: data,
					buttons: Ext.MessageBox.OK,
					animateTarget: this.sender.lookupReference('loginBtn'),
					scope: this,
					fn: this.showResult,
					icon: icon
				});
			}
		}
	},

	onLoginFailed: function (request, response, statusText) {
		Ext.MessageBox.alert('LOGIN FAILED', "Please Provide Appropriate Credentials");
	},

	onLoginPanelShow: function () {

		var me = this;

		var payload = {
			userModKey: gUserId,
			legalEntityKey: gLegalEntityKey,
			reference: 'feesNdCharges'
		};

		me.sendRequest(appActionType.ACTION_TYPE_SELECT, appContentType.CONTENT_TYPE_FEES_CHARGES, payload);

		var payload = {
			reference: 'sarbException',
			legalEntityKey: gLegalEntityKey
		};

		me.sendRequest(appActionType.ACTION_TYPE_SARB_EXCEPTION, appContentType.CONTENT_TYPE_SARBDEX, payload);
	}
});