package de.kolbasa.screenstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

public class ScreenStatus extends CordovaPlugin {

    private CallbackContext cbc;
    private BroadcastReceiver broadcastReceiver;

    private void registerScreenStatusListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                broadcastScreenStatus(intent.getAction().equals(Intent.ACTION_SCREEN_ON));
            }
        };
        this.webView.getContext().registerReceiver(this.broadcastReceiver, intentFilter);
    }

    private void unregisterScreenStatusListener() {
        if (this.broadcastReceiver != null) {
            this.webView.getContext().unregisterReceiver(this.broadcastReceiver);
            this.broadcastReceiver = null;
        }
    }

    private void broadcastScreenStatus(boolean screenOn) {
        if (this.cbc == null) {
            return;
        }
        PluginResult response = new PluginResult(PluginResult.Status.OK, screenOn);
        response.setKeepCallback(true);
        this.cbc.sendPluginResult(response);
    }

    private void registerCallback(CallbackContext callbackContext) {
        this.cbc = callbackContext;
        PluginResult response = new PluginResult(PluginResult.Status.OK, "subscribed");
        response.setKeepCallback(true);
        callbackContext.sendPluginResult(response);
    }

    private void unregisterCallback() {
        if (this.cbc != null) {
            this.cbc.success();
            this.cbc = null;
        }
    }

    private void subscribe(CallbackContext callbackContext) {
        try {
            cleanup();
            registerScreenStatusListener();
            registerCallback(callbackContext);
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void unsubscribe(CallbackContext callbackContext) {
        try {
            cleanup();
            callbackContext.success("unsubscribed");
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void cleanup() {
        unregisterScreenStatusListener();
        unregisterCallback();
    }

    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {
        switch (action) {
            case "subscribe":
                cordova.getThreadPool().execute(() -> subscribe(callbackContext));
                break;
            case "unsubscribe":
                cordova.getThreadPool().execute(() -> unsubscribe(callbackContext));
                break;
            default:
                return false;
        }
        return true;
    }

}
