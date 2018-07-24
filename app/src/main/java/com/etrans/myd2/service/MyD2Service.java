package com.etrans.myd2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.etrans.myd2.biz.MaintenanceBiz;
import com.etrans.myd2.biz.VehicleStateBiz;

/**
 * Created by Administrator on 2018/5/11.
 */

public class MyD2Service extends Service {
    private VehicleStateBiz vStateBiz;
    private MaintenanceBiz maintenanceBiz;
    private Context context;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
        Log.i("hlj", "--------MyD2Service创建---------");
        maintenanceBiz = new MaintenanceBiz(this, new MaintenanceBiz.IMaintenanceBiz() {
            @Override
            public void onMaintenanceInfoChange() {

            }

            @Override
            public void onNeedShowDilog(boolean paramBoolean) {

            }
        }, true);
        maintenanceBiz.onStart();
        vStateBiz = new VehicleStateBiz(context);
    }

    @Override
    public int onStartCommand(Intent paramIntent, int flags, int startId) {
        Log.i("MyD2Service ","--------onStartCommand" +" ,, paramIntent == "+paramIntent);
        handleServiceIntent(paramIntent);

        return Service.START_STICKY;
    }

    private void handleServiceIntent(Intent paramIntent) {

        String str;
        if (paramIntent != null)
        {
            str = paramIntent.getAction();
            if ("intent.sta.refresh".equals(str)) {
                Log.i("hlj ","接收到===intent.sta.refresh===");
                vStateBiz.curSta();
                return;
            }
            if ("intent.maintenance.set".equals(str)) {
                maintenanceBiz.setRemainMaintainKilo(paramIntent.getIntExtra("periodKilo", 3000), paramIntent.getIntExtra("preKilo", 0));
                return;
            }
            if ("intent.maintenance.reset".equals(str)) {
                maintenanceBiz.reset();
                return;
            }
           /* if (!"intent.maintenance.cancel_dialog".equals(str))
                break;
            if (this.isCalling)
                continue;
            this.isCalling = true;
            this.isNeedShowAgain = cancelMaintenanceDialog();
            return;*/
        }
        /*while ((!"intent.maintenance.check_show_dialog".equals(str)) || (!this.isCalling));
        this.isCalling = false;
        if (this.isNeedShowAgain)
        {
            this.handler.sendEmptyMessage(101);
            return;
        }
        BugLoger.showToast("无需再弹保养对话框!");*/

    }

    @Override
    public void onDestroy() {

        vStateBiz.stopSta();
        maintenanceBiz.onStop();

    /*    Intent intent = new Intent();
        intent.setClass(context,MyD2Service.class);
        Log.i("hlj","--------------重新打开服务-----------");
        startService(intent);*/

        super.onDestroy();

    }
}
