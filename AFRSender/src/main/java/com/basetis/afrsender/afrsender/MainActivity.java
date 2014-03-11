package com.basetis.afrsender.afrsender;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    public final String TAG = MainActivity.class.getCanonicalName();

    private static final String REQUIRED_ACTION = "com.basetis.afr.intent.action.INIT_TEXT_FLOW";
    private static final String AFR_PACKAGE = "com.basetis.blinkingread.blinkingread";
    private static final String AFR_REQUIRED_EXTRA_PACKAGE = "AFR_CALLER_PACKAGE";
    private static final String BROADCAST_AFR_REQUIRED_TOKEN_EXTRA = "AFR_API_TOKEN";

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

        Button btnSuccess = (Button)findViewById(R.id.button_success_call);
        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!IsAFRInstalled()) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, getApplicationContext().getPackageName());
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                sendBroadcast(i);

            }
        });


        Button btnFailNoExtraText = (Button)findViewById(R.id.button_fail_call_no_extra_text);
        btnFailNoExtraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!IsAFRInstalled()) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, getApplicationContext().getPackageName());
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                // NULL EXTRA
                //i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                sendBroadcast(i);

            }
        });


        Button btnFailExtraTextEmpty = (Button)findViewById(R.id.button_fail_call_extra_text_empty);
        btnFailExtraTextEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!IsAFRInstalled()) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, getApplicationContext().getPackageName());
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                // EMPTY EXTRA
                i.putExtra(Intent.EXTRA_TEXT, "");
                sendBroadcast(i);

            }
        });


        Button btnReceiveOtherAppCallback = (Button)findViewById(R.id.button_receive_other_app_callback);
        btnReceiveOtherAppCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!IsAFRInstalled()) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Simulate to receive a notification from other App that I must ignore
                i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, "com.basetis.dummy.afr.caller.package");
                i.setAction(REQUIRED_ACTION);
                i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine
                sendBroadcast(i);

            }
        });


        Button btnFailNoAPIToken = (Button)findViewById(R.id.button_fail_no_api_token_sent);
        btnFailNoAPIToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if AFR is Installed, if not redirect to Google Play
                if (!IsAFRInstalled()) return;

                Intent i = new Intent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, getApplicationContext().getPackageName());
                // NO API TOKEN
                //i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

                i.setAction(REQUIRED_ACTION);
                i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
                sendBroadcast(i);

            }
        });
    }

    private boolean IsAFRInstalled() {
        boolean AFRIsInstalled = false;
        PackageManager pm = mContext.getPackageManager();
        ApplicationInfo appInfo = null;
        if (pm != null) {
            try {
                appInfo = pm.getApplicationInfo(AFR_PACKAGE, 0);
            } catch (PackageManager.NameNotFoundException e) {
                goToGooglePlayAFRNotInstalled();
                return AFRIsInstalled;
            }
        }
        String appFile = appInfo.sourceDir;
        if (appFile != null && !appFile.isEmpty()) {
            AFRIsInstalled = new File(appFile).exists();
        }
        if (!AFRIsInstalled) {
            goToGooglePlayAFRNotInstalled();
            return AFRIsInstalled;
        }
        return AFRIsInstalled;
    }

    protected void goToGooglePlayAFRNotInstalled() {
        Toast.makeText(mContext,"AFR is not installed",Toast.LENGTH_LONG).show();
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AFR_PACKAGE)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
