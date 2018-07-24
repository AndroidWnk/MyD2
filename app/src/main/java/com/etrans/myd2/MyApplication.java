package com.etrans.myd2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.etrans.myd2.util.SystemTime;

import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MyApplication extends Application {

    private static Handler appHandler;
    private static MyApplication instance;
    public static  boolean isConnected = false;
    private CANManager canManager;

    public static MyApplication getInstance()
    {
        return instance;
    }

    public static void post(Runnable paramRunnable)
    {
        if (appHandler == null)
            appHandler = new Handler(Looper.getMainLooper());
        appHandler.post(paramRunnable);
    }

    public void onCreate()
    {
        super.onCreate();
        instance = this;
        SystemTime.init(this);
        EtSDK.getInstance(this).addListener(new EtSDK.SDKInitListener() {
            @Override
            public void onConnectSuccess() {
                isConnected = true;
                canManager =EtSDK.getInstance(getApplicationContext()).getCanManager();

            }
        });
    }

}
