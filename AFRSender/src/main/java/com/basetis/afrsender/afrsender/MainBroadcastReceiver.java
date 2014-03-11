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

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, intent.getAction());
        Log.d(TAG, "Received package: " + intent.getStringExtra(AFR_REQUIRED_EXTRA_PACKAGE));
        Log.d(TAG, "My package: " + context.getApplicationContext().getPackageName());


        if (intent.getAction().equals(BROADCAST_AFR_CALLBACK_ACTION)) {

            String callerPackage = intent.getStringExtra(AFR_REQUIRED_EXTRA_PACKAGE);
            if (callerPackage != null && !callerPackage.isEmpty()) {

                String myPackage = context.getApplicationContext().getPackageName();
                if (callerPackage.equals(myPackage)) {

                    // So this is my callback
                    Log.d(TAG, "Callback Received");

                    String result = intent.getStringExtra(BROADCAST_AFR_CALLBACK_EXTRA);

                    if (result != null && !result.isEmpty()) {
                        if (result.equals(BROADCAST_AFR_CALLBACK_SUCCESS_RESULT_DESC)) {
                            Log.d(TAG, "Everything OK");
                        } else {
                            // result contains error description
                            Log.e(TAG,result);
                        }
                    } else {
                        // This should not happen, but you never know :P
                        Log.e(TAG,"Not result received from AFR");
                    }


                } else {

                    Log.d(TAG, "This broadcast is not mine");
                }

            }

        }

    }
}
