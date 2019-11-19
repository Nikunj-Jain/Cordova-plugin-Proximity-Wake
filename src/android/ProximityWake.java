package com.thenikunj.cordova.plugins;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

public class ProximityWake extends CordovaPlugin {

    private final static String TAG = "ProximityWake";
    private final static String WAKE_LOCK_TAG = TAG + ":proximityWakeLockTag";

//    private SensorManager mSensorManager;
//    private Sensor mProximitySensor;
//    private boolean mHasProximitySensor = false;

    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    private CallbackContext savedCallbackContext;

    @Override
    protected void pluginInitialize() {
//        mSensorManager = (SensorManager) cordova.getActivity().getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
//        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        mPowerManager = (PowerManager) cordova.getActivity().getApplicationContext().getSystemService(Context.POWER_SERVICE);

//        if (mProximitySensor != null) {
//            mHasProximitySensor = true;
//        }
    }

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) {

        Log.i(TAG, TAG + " called with " + action);

        if (action.equals("activate")) {
            savedCallbackContext = callbackContext;
            activateSensor();
            return true;
        } else if (action.equals("deactivate")) {
            savedCallbackContext = callbackContext;
            deactivateSensor();
            return true;
        }

        return false;
    }

    public void activateSensor() {

        if (mWakeLock == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, WAKE_LOCK_TAG);
            } else {
                mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
            }
        }
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        returnCallback(true, "Activated");
    }

    public void deactivateSensor() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        returnCallback(true, "Deactivated");
    }


    @Override
    public void onDestroy() {
        deactivateSensor();
    }

    private void returnCallback(boolean success, String message) {

        if (savedCallbackContext == null) {
            return;
        }

        if (message == null) {
            message = "";
        }

        if (success) {
            Log.i(TAG, message);
            savedCallbackContext.success(message);
        } else {
            Log.e(TAG, message);
            savedCallbackContext.error(message);
        }
        savedCallbackContext = null;
    }

}