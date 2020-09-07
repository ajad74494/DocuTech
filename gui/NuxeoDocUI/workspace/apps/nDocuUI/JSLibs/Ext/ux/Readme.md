Ext.ux.ActivityMonitor() is a utility class intended for use with ExtJS 4.x.

ActivityMonitor() watches the browser's BODY element for mouse movement and keystrokes - a realistic way to judge if the user is actively viewing your web application.

Usage:

=====

Ext.ux.ActivityMonitor.init({ verbose : true });
Ext.ux.ActivityMonitor.start();

=====

Configs:

  - verbose (Boolean): Whether or not the ActivityMonitor() should output messages to the JavaScript console.
  - interval (Integer): How often (in millseconds) the monitorUI() method is executed after calling start()
  - maxInactive (Integer): The longest amount of time to consider the user "active" without regestering new mouse movement or keystrokes

  - isActive (Function): Called each time monitorUI() detects the user is currently active (defaults to Ext.emptyFn)
  - isInactive (Funtion): Called when monitorUI() detects the user is inactive (defaults to Ext.emptyFn)
