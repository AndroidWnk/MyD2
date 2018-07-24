package com.etrans.myd2.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;

import com.etrans.myd2.R;
import com.etrans.myd2.bean.AppInfoBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class AppUtil {

    private static HashMap<String, Object> filterApp;

    public static List<PackageInfo> getInstalledPackages(Context context) {
        PackageManager manager = context.getPackageManager();
        return manager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    }

    public static List<AppInfoBean> getAllAppInfo(Context context) {
        List<ResolveInfo> lst = getAllResolveInfo(context);
        HashSet<String> set = new HashSet<String>();
        List<AppInfoBean> lstApp = new ArrayList<AppInfoBean>();
        for (int i = 0; i < lst.size(); i++) {

            String packageName = lst.get(i).activityInfo.packageName;
            if (packageName == null)
                continue;

            if (set.contains(packageName)) {
                continue;
            }
            set.add(packageName);

            AppInfoBean data = new AppInfoBean();

            data.setPackageName(lst.get(i).activityInfo.packageName);
            data.setType(0);
            boolean flag = false;
            if (filterApp == null) {
                filterApp = new HashMap<String, Object>();
                String[] filterNames = context.getResources().getStringArray(R.array.filter_app_names);
                for (String str : filterNames) {
                    filterApp.put(str, null);
                }
            }
            if (packageName.equals("com.estrongs.android.pop")) {
                lstApp.add(data);
            }
            if (filterApp.containsKey(packageName)) {
                flag = true;
            }
            if (!flag) {
                lstApp.add(data);
            }
        }
        return lstApp;
    }

    public static List<ResolveInfo> getAllResolveInfo(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = context.getPackageManager()
                .queryIntentActivities(intent,
                        0);

        for (int i = 0; i < resolveInfos.size(); i++) {
            String packageName = resolveInfos.get(i).activityInfo.packageName;
            if (packageName != null && (packageName.equals("com.iflytek.afadapter") || packageName.equals("com.hero.caritmarketapp")
                    || packageName.equals("com.android.calculator2") || packageName.equals("com.android.browser")
                    || packageName.equals("com.android.calendar") || packageName.equals("com.android.gallery3d") || packageName.equals("com.cyanogenmod.filemanager")
                    || packageName.equals("com.linkgent.xxvideoplayer") || packageName.equals("com.android.setting") || packageName.equals("carnetapp.calendar")
                    || packageName.equals("com.harlan.calculator2") || packageName.equals("com.opera.mini.native")
                    || packageName.equals("net.micode.fileexplorer"))) {

                resolveInfos.remove(i);
            }
        }
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(context.getPackageManager()));
        return resolveInfos;
    }

    public static Intent getAppIntent(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        return manager.getLaunchIntentForPackage(packageName);
    }

    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public static boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }

    public static void startAppWithPackageName(Context context, String packagename) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        //返回第一个元素，迭代器
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    public static void sendTitleBroadCast(Context context, String title) {
        Intent intent1 = new Intent("com.etrans.vehicle.currentapp.fixed");
//        Intent intent2 = new Intent("com.etrans.vehicle.currentapp");
        intent1.putExtra("label", title);
        context.sendBroadcast(intent1);
//        Intent intent2 = new Intent("com.etrans.vehicle.currentapp");
//        intent2.putExtra("label", title);
//        context.sendBroadcast(intent2);

    }

    public static void unLockTitleBroadCast(Context context) {
        Intent intent = new Intent("com.etrans.vehicle.currentapp.fixed");
        context.sendBroadcast(intent);
    }

    public static String getLanguage(){
        return Locale.getDefault().getLanguage();
    }

    public static void updateLanguage(Locale locale) {
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            // IActivityManager iActMag = ActivityManagerNative.getDefault();
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            // Configuration config = iActMag.getConfiguration();
            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = locale;
            Class[] clzParams = { Configuration.class };
            Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
                    "updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
