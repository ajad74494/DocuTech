
/**
 * @class Ext.ux.ActivityMonitor
 * @author Arthur Kay (http://www.akawebdesign.com)
 * @singleton
 * @version 1.0
 *
 * GitHub Project: https://github.com/arthurakay/ExtJS-Activity-Monitor
 *
 * The MIT License (MIT)
 * 
 * Copyright (c) <2011> Arthur Kay
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
Ext.define('Ext.ux.ActivityMonitor', {
	alias: 'activityMonitor',

	singleton   : true,

	ui          : null,
	runner      : null,
	task        : null,
	lastActive  : null,
	
	ready       : false,
	verbose     : false,
	interval    : (1000 * 60 * 1), //1 minute
	maxInactive : (1000 * 60 * 20), //30 minutes
	
	init : function(config) {
		if (!config) { config = {}; }
		
		Ext.apply(this, config, {
			runner     : new Ext.util.TaskRunner(),
			ui         : Ext.getBody(),
			task       : {
				run      : this.monitorUI,
				interval : config.interval || this.interval,
				scope    : this
			}
		});
		
		this.ready = true;
	},
	
	isReady : function() {
		return this.ready;
	},
	
	isActive   : Ext.emptyFn,
	isInactive : Ext.emptyFn,
	
	start : function() {
		if (!this.isReady()) {
			this.log('Please run ActivityMonitor.init()');
			return false;
		}
		
		this.ui.on('mousemove', this.captureActivity, this);
		this.ui.on('keydown', this.captureActivity, this);
		
		this.lastActive = new Date();
		this.log('ActivityMonitor has been started.');
		
		this.runner.start(this.task);
	},
	
	stop : function() {
		if (!this.isReady()) {
			this.log('Please run ActivityMonitor.init()');
			return false;
		}
		
		this.runner.stop(this.task);
		this.lastActive = null;
		
		this.ui.un('mousemove', this.captureActivity);
		this.ui.un('keydown', this.captureActivity);
		
		this.log('ActivityMonitor has been stopped.');

		window.location.reload();
	},
	
	captureActivity : function(eventObj, el, eventOptions) {
		this.lastActive = new Date();
	},
	
	monitorUI : function() {
		var now      = new Date(),
		
		inactive = (now - this.lastActive);
		
		var minute;
		
		if(this.isInt(inactive / 60000)){
			minute = inactive / 60000;
		}
		else if(this.isFloat(inactive / 60000)){
			minute = parseFloat(Math.round((inactive / 60000) * 100) / 100).toFixed(2);
		}

		if (inactive >= this.maxInactive) {
			this.log('MAXIMUM INACTIVE TIME HAS BEEN REACHED');
			this.stop(); //remove event listeners
			
			this.isInactive();
		}
		else if (inactive >= (this.maxInactive - 1000 * 60)) {

			this.warn('YOU ARE CURRENTLY INACTIVE FOR ' + minute + ' (MINUTE)');
		}
		else if (inactive >= (this.maxInactive - 1000 * 30)) {
			this.warn('YOUR SESSION WILL END IN 30 SECONDS');
		}
		else {
			this.log('CURRENTLY INACTIVE FOR ' + minute + ' (MINUTE)');
			//this.warn('CURRENTLY INACTIVE FOR ' + minute + ' (MINUTE)');
			this.isActive();
		}
	},
	
	log : function(msg) {
		if (this.verbose) {
			window.console.log(msg);
		}
	},
	
	warn : function(msg) {
		Ext.toast({
			html: '<span style="color:red;font-weight:bold">'+ msg +'</span>',
			title: 'Status',
			width: 200,
			align: 't'
		});
	},
	
	isInt : function(n) {
		
		return Number(n) === n && n % 1 === 0;
	},
	
	isFloat : function(n) {
		
		return Number(n) === n && n % 1 !== 0;
	}
	
});