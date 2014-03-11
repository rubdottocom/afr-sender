afr-sender
==========

An example Android App that integrates with "A Faster Reader" https://play.google.com/store/apps/details?id=com.basetis.blinkingread.blinkingread

A Faster Reader, AFR, is a new way to read text on mobile devices.

With AFR you can share text and URLs from any App to AFR and the text will flow allowing the user to read a higher speed.

This is an Android Example App that shows how to integrate with AFR App to give a better read experience to your users, you can add a button on your App to directly share with AFR avoid the Share intent that can confuse the user with many options to choose.

###How to integrate your App with AFR

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
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AFR_PACKAGE)));
    }

Be aware of this constant values

    // Action that AFR will capture
    private static final String REQUIRED_ACTION = "com.basetis.afr.intent.action.INIT_TEXT_FLOW";

    // Package to check if AFR is installed on the device
    private static final String AFR_PACKAGE = "com.basetis.blinkingread.blinkingread";

    // Mandatory Extra that must contain the Package String of your app, needed to return to you a callback
    private static final String AFR_REQUIRED_EXTRA_PACKAGE = "AFR_CALLER_PACKAGE";

    // Mandatory Extra that must not be empty, currently we don't check the value so a random String will be fine
    private static final String BROADCAST_AFR_REQUIRED_TOKEN_EXTRA = "AFR_API_TOKEN";


Once AFR is installed, you need to send a Broadcast Intent that AFR will capture and starts the flow if everything is OK

    Intent i = new Intent();
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    i.putExtra(AFR_REQUIRED_EXTRA_PACKAGE, getApplicationContext().getPackageName());
    i.putExtra(BROADCAST_AFR_REQUIRED_TOKEN_EXTRA, apiToken); // Mandatory parameter, currently a randome string will be fine

    i.setAction(REQUIRED_ACTION);
    i.putExtra(Intent.EXTRA_TEXT, textToBeRead);
    sendBroadcast(i);

Don't forget the Flags

    // This flag is needed to call AFR from third Apps
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

Catch the callback

    // AFR Sends you a Callback that informs you if everything went well
    // Create a BroadcastReceiver class on your project

    public class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }

And listen for _COM.BASETIS.BLINKING.BROADCAST.CALLBACK.AFR_ Action

So you can filter your callback getting Intent Extra _AFR_REQUIRED_EXTRA_PACKAGE_ that will match with _AFR_REQUIRED_EXTRA_PACKAGE_ that you set when calling to AFR

        if (intent.getAction().equals(BROADCAST_AFR_CALLBACK_ACTION)) {

            String callerPackage = intent.getStringExtra(AFR_REQUIRED_EXTRA_PACKAGE);
            if (callerPackage != null && !callerPackage.isEmpty()) {

                String myPackage = context.getApplicationContext().getPackageName();
                if (callerPackage.equals(myPackage)) {
                    // Process callback
                }
            }
        }

Configure your _AndroidManifest_ properly

            <!-- your fancy activities -->
            </activity>
            <receiver android:name=".MainBroadcastReceiver" android:enabled="true">
                <intent-filter>
                    <action android:name="COM.BASETIS.BLINKING.BROADCAST.CALLBACK.AFR"></action>
                </intent-filter>
            </receiver>
        </application>

    </manifest>

Take a look to the example App AFRSender to see how the whole thing works and how you can capture different responses from AFR Callback
