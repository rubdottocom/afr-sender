package com.basetis.afrsender.afrsender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by rubdottocom on 11/03/14.
 */
public class MainBroadcastReceiver extends BroadcastReceiver {
    public final String TAG = MainBroadcastReceiver.class.getCanonicalName();

    public static final String BROADCAST_AFR_CALLBACK_ACTION = "COM.BASETIS.BLINKING.BROADCAST.CALLBACK.AFR";
    public static final String AFR_REQUIRED_EXTRA_PACKAGE = "AFR_CALLER_PACKAGE";
    public static final String BROADCAST_AFR_CALLBACK_EXTRA = "AFR_RESULT";
    public static final String BROADCAST_AFR_CALLBACK_SUCCESS_RESULT_DESC = "SUCCESS";

    public static final String API_RESULT_EXTRA = "AFR_API_RESULT";
    public static final String API_CALL_SUCCEEDED = "API_CALL_SUCCEEDED";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Callback Received");

        boolean isSuccess = intent.getBooleanExtra(API_CALL_SUCCEEDED,false);
        String result = intent.getStringExtra(API_RESULT_EXTRA);
        if (result == null) {
            // This should not happen, but you never know :P
            Log.e(TAG,"Not result received from AFR");
            return;
        }

        if (isSuccess) {
            Log.i(TAG,result);
        } else {
            Log.e(TAG,result);
        }

    }
}
