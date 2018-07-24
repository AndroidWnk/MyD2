package com.etrans.myd2.biz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

import com.etrans.myd2.MyApplication;
import com.etrans.myd2.R;
import com.etrans.myd2.activity.MaintenanceActivity;
import com.etrans.myd2.activity.XxBaseActivity;
import com.etrans.myd2.service.MyD2Service;

import cn.com.etrans.etsdk.can.VehicleInfoChangeObserver;
import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;


public class MaintenanceBiz {
    public static final int MAINTENANCE_KILO_NOTIFICATION = 999;
    private static final int MAINTENANCE_NOTIFICATION_CODE = 100120;
    private static final String SP_FILE_NAME_MAINTENANCE = "maintenace_spfile";
    private static boolean isShowDialog;
    private CANManager canManager;
    private Context context;
    private IMaintenanceBiz iMaintenanceBiz;
    private boolean isService;
    private NotificationManager notiManager;
    private SharedPreferences sp;
    private VehicleInfoObr vInfo;
    private XxBaseActivity base;

    public MaintenanceBiz(Context paramContext, IMaintenanceBiz paramIMaintenanceBiz, boolean paramBoolean) {
        if (paramIMaintenanceBiz == null)
            throw new RuntimeException("保养回调接口参数不能为NULL");
        notiManager = ((NotificationManager) paramContext.getSystemService("notification"));
        iMaintenanceBiz = paramIMaintenanceBiz;
        context = paramContext;
        isService = paramBoolean;
        sp = paramContext.getSharedPreferences(SP_FILE_NAME_MAINTENANCE, 0);
        if (paramBoolean)
            isShowDialog = this.sp.getBoolean("isShowDialog", true);
        EtSDK.getInstance(paramContext).addListener(new EtSDK.SDKInitListener() {
            @Override
            public void onConnectSuccess() {
                Log.i("EtSDK","ready");
                canManager =EtSDK.getInstance(context).getCanManager();
            }
        });
        vInfo = new VehicleInfoObr();
    }


    private void clearNotification() {
        if (!isService)
            return;
//        XxBaseActivity.setToastText("ApkUtils.showIconToStatusBar(context, false)");//hlj test
//        ApkUtils.showIconToStatusBar(context, false);
        notiManager.cancel(MAINTENANCE_NOTIFICATION_CODE);
    }

    public int getCurKilo() {
        return (int) canManager.getVehileOdoMeterValue();
    }

    public int getMaintainInfo() {
        return canManager.getMaintainInfo();
    }

    public int getRemainMaintainKilo() {
        return canManager.getRemainMaintainKilo();
    }

    public int getSetPeriodKilo() {
        return sp.getInt("periodKilo", 3000);
    }

    public int getSetPreKilo() {
        return sp.getInt("preKilo", 0);
    }

    //获取剩余保养里程
    public int getSetRemainMaintainKilo() {
        int curKilo = getCurKilo();
        int preKilo = sp.getInt("preKilo", curKilo);
        int periodKilo = sp.getInt("periodKilo", 3000);
        if (curKilo - preKilo >= 0) {
            return preKilo + (periodKilo - curKilo);
        }
        Log.i("hlj", "上次保养里程比当前里程大?curKilo:" + curKilo + "===preMain:" + preKilo);
        return -1;
    }

    //判断是否支持仪表保养
    public boolean isCanSupport() {
        int i = canManager.getMaintainInfo();//保养信息：无信息(0)、剩余保养里程有效(1)、立即保养(2)
        int j = canManager.getRemainMaintainKilo();//剩余保养里程
        if (!isService)
            base.setToastText("是否支持仪表-->getMaintainInfo()=" + i);
        return (i >= 0) || (j >= 0);
    }

    public void lockShowDialog() {
        if (!isService)
            return;
        sp.edit().putBoolean("isShowDialog", false).apply();
        isShowDialog = false;
    }

    public void onMaintanceEvent(int paramInt, float paramFloat) {
        try {
            vInfo.onRemainMaintainKiloChange(paramInt);
            vInfo.onVehileOdoMeterValueChange(paramFloat);
            return;
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    public void onStart() {
        if (canManager != null) {//hlj 0716
            canManager.attachVehicleInfoObserver(vInfo);
            onMaintanceEvent(canManager.getRemainMaintainKilo(), getCurKilo());
            showNotification();
        }
    }

    public void onStop() {
        canManager.detachVehicleInfoObserver(vInfo);
    }

    public void reset() {
        if (isService) {
            sp.edit().putBoolean("isShowDialog", true).apply();
            isShowDialog = true;
            clearNotification();
            iMaintenanceBiz.onNeedShowDilog(false);
            return;
        }
        Intent localIntent = new Intent(context, MyD2Service.class);
        localIntent.setAction("intent.maintenance.reset");
        context.startService(localIntent);
    }

    public void setRemainMaintainKilo(int paramInt1, int paramInt2) {
        sp.edit().putInt("preKilo", paramInt2).putInt("periodKilo", paramInt1).apply();
        if (!isService) {
            Intent localIntent = new Intent(context, MyD2Service.class);
            localIntent.setAction("intent.maintenance.set");
            localIntent.putExtra("preKilo", paramInt2);
            localIntent.putExtra("periodKilo", paramInt1);
            context.startService(localIntent);
            return;
        }
        showNotification();
    }

    public void showNotification() {//此处逻辑还有问题hlj
        int i;
        if (!isService)
            return;
        if (isCanSupport()) ;
        for (i = getRemainMaintainKilo(); i > MAINTENANCE_KILO_NOTIFICATION; i = getSetRemainMaintainKilo()) {
            clearNotification();
            return;
        }
        if (i < 0)
            i = 0;
        base.setToastText("状态栏显示通知!");//hlj 0712
//        ApkUtils.showIconToStatusBar(this.context, true);//hlj
        Notification localNotification = new Notification();
        localNotification.flags = Notification.FLAG_NO_CLEAR;
        localNotification.icon = R.drawable.notification_icon;
        localNotification.when = System.currentTimeMillis();
        localNotification.tickerText = ("距离保养还剩" + i + "km!");
        Intent localIntent = new Intent(context, MaintenanceActivity.class);
        localIntent.setFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        localNotification.contentIntent = PendingIntent.getActivity(this.context, 0, localIntent, 0);
        localNotification.setLatestEventInfo(this.context, "保养提醒", localNotification.tickerText, localNotification.contentIntent);
        notiManager.notify(MAINTENANCE_NOTIFICATION_CODE, localNotification);
    }

    public static abstract interface IMaintenanceBiz {
        public abstract void onMaintenanceInfoChange();

        public abstract void onNeedShowDilog(boolean paramBoolean);
    }

    public class VehicleInfoObr extends VehicleInfoChangeObserver {
        @Override
        public void onMaintainInfoChange(int mInfo) throws RemoteException {
            super.onMaintainInfoChange(mInfo);
            iMaintenanceBiz.onMaintenanceInfoChange();
            if (isService) {
                base.setToastText("仪表保养状态变化!onMaintainInfoChange=" + mInfo);
                switch (mInfo) {
                    case 0:
                        reset();
                        break;
                    case 1:
                        isShowDialog = false;
                       iMaintenanceBiz.onNeedShowDilog(true);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onRemainMaintainKiloChange(int rmKilo) throws RemoteException {
            super.onRemainMaintainKiloChange(rmKilo);
            if (rmKilo >= 0) {
                iMaintenanceBiz.onMaintenanceInfoChange();
                if (isService)
                    base.setToastText("MyD2 剩余保养里程变化__onRemainMaintainKiloChange=" + rmKilo);
            }
        }

        @Override
        public void onVehileOdoMeterValueChange(float vehileOdoMeter) throws RemoteException {
            super.onVehileOdoMeterValueChange(vehileOdoMeter);
            if (!isCanSupport()) {
                iMaintenanceBiz.onMaintenanceInfoChange();
                if (isService) {
                    base.setToastText("MyD2 手动设置-剩余保养里程变化!(总里程)=" + vehileOdoMeter);
                    if (isShowDialog && (getSetRemainMaintainKilo() <= MAINTENANCE_KILO_NOTIFICATION)) {
                       isShowDialog = false;
                        iMaintenanceBiz.onNeedShowDilog(true);
                    }
                }
            }
        }
    }
}