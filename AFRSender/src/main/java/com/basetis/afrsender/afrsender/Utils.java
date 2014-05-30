package com.basetis.afrsender.afrsender;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by rubdottocom on 27/03/14.
 */
public final class Utils {
    private static final String AFR_PACKAGE = "com.basetis.blinkingread.blinkingread";

    public static final boolean IsAFRInstalled(Context ctx) {
        boolean AFRIsInstalled = false;
        PackageManager pm = ctx.getPackageManager();
        ApplicationInfo appInfo = null;
        if (pm != null) {
            try {
                appInfo = pm.getApplicationInfo(AFR_PACKAGE, 0);
            } catch (PackageManager.NameNotFoundException e) {
                goToGooglePlayAFRNotInstalled(ctx);
                return AFRIsInstalled;
            }
        }
        String appFile = appInfo.sourceDir;
        if (appFile != null && !appFile.isEmpty()) {
            AFRIsInstalled = new File(appFile).exists();
        }
        if (!AFRIsInstalled) {
            goToGooglePlayAFRNotInstalled(ctx);
            return AFRIsInstalled;
        }
        return AFRIsInstalled;
    }

    public static final void goToGooglePlayAFRNotInstalled(Context ctx) {
        ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AFR_PACKAGE)));
    }
}
