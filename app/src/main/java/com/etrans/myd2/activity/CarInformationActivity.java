package com.etrans.myd2.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.etrans.myd2.R;
import com.etrans.myd2.biz.VehicleProviderBiz;
import com.etrans.myd2.dbhelper.CANContentConst;
import com.etrans.myd2.entity.BaseDayVehicleState;
import com.etrans.myd2.entity.CurDayVehicleState;
import com.etrans.myd2.fragment.CarInformation_current;
import com.etrans.myd2.fragment.CarInformation_today;
import com.etrans.myd2.fragment.fg_CarInformation_week;
import com.etrans.myd2.service.MyD2Service;
import com.etrans.myd2.test.Battery;
import com.etrans.myd2.test.CoreComponents;
import com.etrans.myd2.test.Motor;
import com.etrans.myd2.util.DateUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/5/4.
 */

public class CarInformationActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;
    private Button mBtn_current, mBtn_today, mBtn_week;
    private int[] doorStatusId = {R.drawable.state_car, R.drawable.state_all, R.drawable.state_left_right, R.drawable.state_left, R.drawable.state_right, R.drawable.state_under};
    private CarInformation_current carInformation_current;
    private CarInformation_today carInformation_today;
    private fg_CarInformation_week carInformation_week;
    private VehicleProviderBiz vProviderBiz;
    private Battery battery;
    private ContentResolver cr;
    private Motor motor;
    private MyStateDater myStateDater = new MyStateDater();
    private MyStaContentObserver staObr = new MyStaContentObserver();
    BaseDayVehicleState localBaseDayVehicleState ;
    CurDayVehicleState localCurDayVehicleState ;
    private MyBaseContentObserver baseObr = new MyBaseContentObserver();
    private Handler handler = new Handler() {
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {

                case 0:
                    localCurDayVehicleState = (CurDayVehicleState) paramMessage.obj;
                    carInformation_today.refreshStaData(localCurDayVehicleState);
                    break;
                case 1:
                    localBaseDayVehicleState = (BaseDayVehicleState) paramMessage.obj;
                    carInformation_current.refreshBaseData(localBaseDayVehicleState, doorStatusId);
//                  carInformation_current.refreshCurState(motor,battery);
                    break;
                case 100:
                    if(localBaseDayVehicleState != null){
                            String str = localBaseDayVehicleState.getDriveRange();
                            if (str != null) {
                                float f = Float.parseFloat(str);
                                battery.setCanRange(f);
                            }
                }
                    carInformation_today.refreshBaseData(localBaseDayVehicleState, doorStatusId);
                    vProviderBiz.refreshBase(DateUtils.getInstance().getNativeSystemDay());
                    vProviderBiz.refreshSta(DateUtils.getInstance().getNativeSystemDay());

                    break;
                case 150:
                    carInformation_today.refreshStaData(localCurDayVehicleState);
                    break;
                case 200:
                carInformation_week.refreshRecentWeekData((List) paramMessage.obj);
                    break;
                case 300:
                 carInformation_current.refreshCurState(motor,battery);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除状态栏
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_car_information);
        vProviderBiz = new VehicleProviderBiz(this);
        motor = new Motor(this);
        battery = new Battery(this);
        cr = getContentResolver();
        setView();
        Intent localIntent = new Intent(this, MyD2Service.class);
        localIntent.setAction("intent.sta.refresh");
        startService(localIntent);//hlj

    }

    @Override
    protected void onStart() {
        super.onStart();

        vProviderBiz.resumeListener(myStateDater);
        motor.notifyDataChange(iDaNotify);
        battery.notifyDataChange(iDaNotify);
        cr.registerContentObserver(CANContentConst.VehicleStatisticsStates.CONTENT_URI, true, staObr);
        cr.registerContentObserver(CANContentConst.VehicleBasicStates.CONTENT_URI, true, baseObr);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vProviderBiz.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    @Override
    protected void onStop() {
        vProviderBiz.stopListener();
        if (cr != null) {
            cr.unregisterContentObserver(staObr);
            cr.unregisterContentObserver(baseObr);
        }
        motor.detMotorListener();
        battery.detBatteryObr();
        super.onStop();
    }

    private void setView() {
        carInformation_current = new CarInformation_current(R.layout.fg_current, true);
        carInformation_today = new CarInformation_today(R.layout.fg_today, true);
        carInformation_week = new fg_CarInformation_week(R.layout.fg_week, false);

        mBtn_current = (Button) findViewById(R.id.btn_current);
        mBtn_today = (Button) findViewById(R.id.btn_today);
        mBtn_week = (Button) findViewById(R.id.btn_week);

        mBtn_current.setOnClickListener(this);
        mBtn_today.setOnClickListener(this);
        mBtn_week.setOnClickListener(this);

        switchFragment(this.carInformation_current);
        mBtn_current.setBackgroundResource(R.drawable.btn_down);
    }

    private void switchFragment(Fragment paramFragment) {
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fl_content, paramFragment);
        localFragmentTransaction.commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_current:
                mBtn_current.setBackgroundResource(R.drawable.btn_down);
                mBtn_today.setBackgroundResource(R.drawable.btn_normal);
                mBtn_week.setBackgroundResource(R.drawable.btn_normal);
                switchFragment(carInformation_current);

                break;
            case R.id.btn_today:
                mBtn_current.setBackgroundResource(R.drawable.btn_normal);
                mBtn_today.setBackgroundResource(R.drawable.btn_down);
                mBtn_week.setBackgroundResource(R.drawable.btn_normal);
                switchFragment(carInformation_today);
                break;
            case R.id.btn_week:
                mBtn_current.setBackgroundResource(R.drawable.btn_normal);
                mBtn_today.setBackgroundResource(R.drawable.btn_normal);
                mBtn_week.setBackgroundResource(R.drawable.btn_down);
                switchFragment(carInformation_week);

                break;

        }

    }


    private class MyBaseContentObserver extends ContentObserver {

        public MyBaseContentObserver() {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (((carInformation_current.isStart()) || (carInformation_today.isStart())) && (handler != null))
                handler.sendEmptyMessage(100);
        }
    }

    private class MyStaContentObserver extends ContentObserver {
        public MyStaContentObserver() {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if ((CarInformationActivity.this.carInformation_today.isStart()) && (CarInformationActivity.this.handler != null))
                CarInformationActivity.this.handler.sendEmptyMessage(150);
        }
    }

    private class MyStateDater
            implements VehicleProviderBiz.IVehicleStateDater {
        private MyStateDater() {
        }

        public void getBase(BaseDayVehicleState paramBaseDayVehicleState) {
            if ((paramBaseDayVehicleState != null) && (carInformation_current.isStart()) && (handler != null))
                handler.obtainMessage(1, paramBaseDayVehicleState).sendToTarget();
        }

        public void getCurSta(CurDayVehicleState paramCurDayVehicleState) {
            if ((paramCurDayVehicleState != null) && (carInformation_today.isStart()) && (handler != null))
                handler.obtainMessage(0, paramCurDayVehicleState).sendToTarget();
        }

        public void getStaList(ArrayList<CurDayVehicleState> paramArrayList) {
            if ((carInformation_week.isStart()) && (paramArrayList != null) && (handler != null)) {
                List localList = CurDayVehicleState.getBarData(paramArrayList);
                handler.obtainMessage(200, localList).sendToTarget();
            }
        }

        @Override
        public void getBaseList(ArrayList<BaseDayVehicleState> paramArrayList) {

        }
    }


    public void getData() {
        if (handler != null) {
            if (carInformation_current.isStart()) {
                handler.sendEmptyMessage(300);
                handler.sendEmptyMessage(100);
            }
            else if (carInformation_today.isStart()) {
                handler.sendEmptyMessage(100);
                handler.sendEmptyMessage(150);
            }
            else if (carInformation_week.isStart()) {
                vProviderBiz.queryStaList(7);
            }
        }
        /*{
            if (this.handler == null);
            do
            {
                if (this.carInformation_current.isStart())
                {
                    this.handler.sendEmptyMessage(300);
                    this.handler.sendEmptyMessage(100);
                }
                if (!this.carInformation_today.isStart())
                    continue;
                this.handler.sendEmptyMessage(100);
                this.handler.sendEmptyMessage(150);
            }
            while (!this.carInformation_week.isStart());
            XxBaseActivity.setToastText("查询7天的数据-----------");
            this.vProviderBiz.queryStaList(7);
        }
*/
    }

    private final CoreComponents.IDataNotify iDaNotify = new CoreComponents.IDataNotify() {
        public void refresh() {
            if (CarInformationActivity.this.carInformation_current.isStart())
                CarInformationActivity.this.handler.sendEmptyMessage(300);
        }
    };
}
