package com.auro.scholr.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import com.auro.scholr.core.common.AppConstant;
import com.auro.scholr.core.common.CommonDataModel;
import com.auro.scholr.core.common.Status;

import org.json.JSONObject;

import java.util.Locale;

public class AppUtil {

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getVersionNumber(Context pContext) {
        String lStrVersion = "";
        try {
            PackageInfo pInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
            lStrVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return lStrVersion;
    }


    public static String getLocale() {


        return Locale.getDefault().getLanguage().toUpperCase();
    }

    public static CommonDataModel getCommonClickModel(int source, Status clickType, Object object) {
        CommonDataModel commonDataModel = new CommonDataModel();
        commonDataModel.setSource(source);
        commonDataModel.setClickType(clickType);
        commonDataModel.setObject(object);
        return commonDataModel;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static String errorMessageHandler(String defaultMsg, String responseData) {

        JSONObject errorJson = null;
        try {
            errorJson = new JSONObject(responseData);
            JSONObject description = (JSONObject) errorJson.getJSONArray("errorDetails").opt(0);
           // return description.optString(AppConstant.DESCRIPTION);
            return "";
        } catch (Exception e) {
            return defaultMsg;
        }
    }



    public static void openDialer(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void openUrlInBrowser(String url, Context context)
    {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


}