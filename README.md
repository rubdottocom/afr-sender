afr-sender
==========

AFRSender is an example Android App that integrates with "A Faster Reader" https://play.google.com/store/apps/details?id=com.basetis.blinkingread.blinkingread

A Faster Reader, AFR, is a new way to read text on mobile devices.

With AFR you can share text and URLs from any App to AFR and the text will flow allowing the user to read at a higher speed.

This App shows you how to integrate with AFR to give a better read experience to your users, you can add a dedicated button on your App to directly share with AFR the content that you want avoiding to use the Android native Share intent that can confuse the user with many options to choose.

##How to integrate your App with AFR


###Check if AFR is already installed on user's device

First, check if AFR is installed on the device, if not redirect to Google Play to let the user to install it

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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setTitle("AFR is not installed")
                .setMessage("Install AFR is needed to use this feature. Go to Google Play?")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AFR_PACKAGE)));
            }
        });
        alert.show();
    }

###Send text or URLs to AFR to invoke the reading widget

You need to send a Broadcast Intent to AFR to initialize the reading's flow.

First of all you need to create a PendingIntent that will contain a BroadcastReceiver.

The receiver will receive the notifications that AFR will send to your App to notify you about the success or failure of your call.
We'll see the BroadcastReceiver later, now you only need to create a Bundle with the whole thing with the key "API_AFR_CALLBACK_RECEIVER_EXTRA"

__Example:__

    Intent broadcastReceiverIntent = new Intent(mContext, AFRCallbackBroadcastReceiver.class);
    PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, broadcastReceiverIntent, 0);

    final Bundle bundle = new Bundle();
    bundle.putParcelable(API_AFR_CALLBACK_RECEIVER_EXTRA, pi);
    

After preparing the callback, you need to make the Intent that will call AFR.

Is a 3 steps task:

1. Set the Action to "com.basetis.afr.intent.action.INIT_TEXT_FLOW"
2. Add the Extra "API_AFR_REQUIRED_TOKEN_EXTRA" with your app token (we don't check the token yet, but is a mandatory parameter).
3. Add an EXTRA_TEXT Extra with the text or URL you want AFR read

__Example:__

  
    Intent i = new Intent();
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

    i.setAction(INIT_TEXT_FLOW_ACTION);

    // Mandatory parameter, currently a random string will be fine
    i.putExtra(API_AFR_REQUIRED_TOKEN_EXTRA, apiToken);
    i.putExtra(Intent.EXTRA_TEXT, textToRead);
       
Finally add the bundle that contains the BroadcastReceiver to the intent and sendBroadcast with it.

__Example:__

    i.putExtras(bundle); // Add pending Intent to let API be able to send us callbacks

    sendBroadcast(i);

###Catch AFR response

AFR will notify you about the success or failure of your call. 

A failure happens normally because you have missed an important parameter, like the API key or maybe the text is empty.

You will receive an Intent with the Action "API_AFR_CALLBACK_STATUS_ACTION", you don't need to filter this action or configure ar AndroidManifest, because you sent a PendingIntent previously, with that AFR is able to call your BroadcastReceiver directly without the need to expose anything on your App

AFR will send you 2 EXTRAS:

1. "API_AFR_CALLBACK_CALL_SUCCEEDED_EXTRA" that is a boolean that tells you if everything goes fien
2. "API_AFR_CALLBACK_MESSAGE_EXTRA" that gives you a message about whats going on

__Code:__

    public class AFRCallbackBroadcastReceiver extends BroadcastReceiver {
        public final String TAG = AFRCallbackBroadcastReceiver.class.getCanonicalName();

        private static final String API_AFR_CALLBACK_CALL_SUCCEEDED_EXTRA = "API_AFR_CALLBACK_CALL_SUCCEEDED_EXTRA";
        private static final String API_AFR_CALLBACK_MESSAGE_EXTRA = "API_AFR_CALLBACK_MESSAGE_EXTRA";

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Callback Received");

            boolean isSuccess = intent.getBooleanExtra(API_AFR_CALLBACK_CALL_SUCCEEDED_EXTRA, false);
            String result = intent.getStringExtra(API_AFR_CALLBACK_MESSAGE_EXTRA);
            if (result == null) {
                // This should not happen, but you never know :P
                Log.e(TAG, "Not result received from AFR");
                return;
            }
            if (isSuccess) {
                // Do your amazing stuff
                Log.d(TAG, result);
            } else {
                Log.e(TAG, result);
            }

            Toast.makeText(context,result,Toast.LENGTH_LONG).show();

        }
    }

