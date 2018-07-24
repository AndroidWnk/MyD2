package com.etrans.myd2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 2018/6/11.
 */

public class MyD2Receiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String str = intent.getAction();
        if (ACTION_BOOT.equals(str)) {
            Log.i("hlj", "收到开机广播...");
//            startMyD2Service(context);//hlj0723
        }
    }

    private void startMyD2Service(Context paramContext)
    {
        paramContext.startService(new Intent(paramContext, MyD2Service.class));
    }
}
