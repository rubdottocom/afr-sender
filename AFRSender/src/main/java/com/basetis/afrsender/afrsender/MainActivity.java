package com.basetis.afrsender.afrsender;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

/**
 * Created by rubdottocom on 27/03/14.
 */
public class MainActivity extends Activity {

    public final String TAG = MainActivity.class.getCanonicalName();

    private static final String REQUIRED_ACTION = "com.basetis.afr.intent.action.INIT_TEXT_FLOW";
    private static final String BROADCAST_AFR_REQUIRED_TOKEN_EXTRA = "AFR_API_TOKEN";
    public static final String CALLBACK_RECEIVER = "CALLBACK_RECEIVER";

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        // API_TOKEN IS MANDATORY, but currently we don't check that parameter, only must not be empty
        final String apiToken = UUID.randomUUID().toString();

        // If textToBeRead is an URL AFR will try to fetch content from that URL
        // If textToBeRead contains text and URLs, AFR will try to fetch only the first URL content
        final String textToBeRead = "Hello World, this is a test to invoke AFR. Thank you for your support";

        //this is the intent that will be broadcasted by service.
        Intent broadcastReceiverIntent = new Intent(mContext, MainBroadcastReceiver.class);
        //create pending intent for broadcasting the DataBroadcastReceiver
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, broadcastReceiverIntent, 0);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(CALLBACK_RECEIVER, pi);

        Button btnSuccess = (Button)findViewById(R.id.button_success_call);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!Utils.IsAFRInstalled(mContext)) return;

                Intent i = new Intent();
                i.setAction(REQUIRED_ACTION);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine
                i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                i.putExtras(bundle); // Add pending Intent to let API be able to send us callbacks
                sendBroadcast(i);

            }
        });

        Button btnFailNoExtraText = (Button)findViewById(R.id.button_fail_call_no_extra_text);
        btnFailNoExtraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!Utils.IsAFRInstalled(mContext)) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                // NULL EXTRA
                //i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                i.putExtras(bundle); // Add pending Intent to let API be able to send us callbacks
                sendBroadcast(i);

            }
        });

        Button btnFailExtraTextEmpty = (Button)findViewById(R.id.button_fail_call_extra_text_empty);
        btnFailExtraTextEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!Utils.IsAFRInstalled(mContext)) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                // EMPTY EXTRA
                i.putExtra(Intent.EXTRA_TEXT, "");
                i.putExtras(bundle); // Add pending Intent to let API be able to send us callbacks
                sendBroadcast(i);

            }
        });

        Button btnFailNoAPIToken = (Button)findViewById(R.id.button_fail_no_api_token_sent);
        btnFailNoAPIToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!Utils.IsAFRInstalled(mContext)) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // NO API TOKEN
                //i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                i.putExtras(bundle); // Add pending Intent to let API be able to send us callbacks
                sendBroadcast(i);

            }
        });
    }
}
