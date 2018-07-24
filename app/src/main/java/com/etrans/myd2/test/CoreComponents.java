package com.etrans.myd2.test;

import android.content.Context;
import android.util.Log;

import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;


public abstract class CoreComponents {
    protected CANManager canManager;

    public CoreComponents(final Context paramContext) {

            EtSDK.getInstance(paramContext).addListener(new EtSDK.SDKInitListener() {
                @Override
                public void onConnectSuccess() {
                    Log.i("EtSDK","ready");
                    canManager =EtSDK.getInstance(paramContext).getCanManager();
                }
            });
        init(canManager);
    }

    protected abstract void init(CANManager paramCANManager);

    public abstract void notifyDataChange(IDataNotify paramIDataNotify);

    public static abstract class IDataNotify {
        private long preSpeedTime = System.currentTimeMillis();

        public abstract void refresh();

        public void refreshLimit() {
            long l = System.currentTimeMillis();
            if (l - preSpeedTime > 500L) {
                preSpeedTime = l;
                refresh();
            }
        }
    }
}

