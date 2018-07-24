package com.etrans.myd2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etrans.myd2.MyApplication;
import com.etrans.myd2.R;
import com.etrans.myd2.biz.VehicleProviderBiz;
import com.etrans.myd2.dbhelper.CANContentConst;
import com.etrans.myd2.entity.BaseDayVehicleState;
import com.etrans.myd2.entity.CurDayVehicleState;
import com.etrans.myd2.util.VIChangeObserver;

import java.util.ArrayList;

import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;

/**
 * Created by Administrator on 2018/5/7.
 */

public class fg_CarInformation_today extends BaseFragment {

    private View view;
    private Context mContext;
    private CANManager canManager;
    private int leftDoor, rightDoor, backDoor;
    private ImageView iv_car_door_state;
    private TextView tvDrivingKilo; //行驶里程
    private TextView tvKiloConsume;//百公里耗电
    private TextView tvChargeSoc;//充电电量
    private TextView tvDrivingTime;//行车时间(精确到分)
    private TextView tvAvSpeed;//平均速度
    private TextView tvChargeTime;//充电时间(精确到分)
    private CurDayVehicleState curDayVehicleState;
    private VehicleProviderBiz vProviderBiz;
    boolean isStart;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    curDayVehicleState = (CurDayVehicleState) msg.obj;
                    refreshStaData(curDayVehicleState);
                    break;
                case 1:

                    break;

            }

        }
    };
    private Runnable runnable = new Runnable() {
        public void run() {
            refreshDoorState(); //更新数据
//            refreshStaData(curDayVehicleState);
            Log.i("hlj  今天页面", " 每隔3秒刷新数据");
            handler.postDelayed(this, 3000);//定时时间
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_today, null);
        mContext = getActivity();

        vProviderBiz = new VehicleProviderBiz(mContext);
        curDayVehicleState = new CurDayVehicleState();
        //初始化CANManager
        EtSDK.getInstance(mContext).addListener(new EtSDK.SDKInitListener() {
            @Override
            public void onConnectSuccess() {
                Log.i("EtSDK", "fg_CarInformation_today ready");
                canManager = EtSDK.getInstance(mContext).getCanManager();
            }
        });
        setView();
        return view;
    }

    private void setView() {
        tvDrivingKilo = (TextView) view.findViewById(R.id.tv_today_driving_kilo);
        tvKiloConsume = (TextView) view.findViewById(R.id.tv_today_hundred_kilo_consume);
        tvChargeSoc = (TextView) view.findViewById(R.id.tv_today_charge_soc);
        tvDrivingTime = (TextView) view.findViewById(R.id.tv_today_driving_time);
        tvAvSpeed = (TextView) view.findViewById(R.id.tv_today_average_speed);
        tvChargeTime = (TextView) view.findViewById(R.id.tv_today_charge_time);

        refreshDoorState();
        refreshStaData(curDayVehicleState);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
//        vProviderBiz.resumeListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isStart = false;
        handler.removeCallbacks(runnable);

    }

    private void refreshDoorState() {
        leftDoor = canManager.getLFDoorState();
        rightDoor = canManager.getRFDoorState();
        backDoor = canManager.getBackDoorState();
        Log.i("hlj  today","leftDoor == "+leftDoor+" rightDoor == " +rightDoor+" backDoor == "+backDoor);
        if (0 == leftDoor && 0 == rightDoor && 0 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_car);
        } else if (1 == leftDoor && 0 == rightDoor && 0 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_left);
        } else if (0 == leftDoor && 1 == rightDoor && 0 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_right);
        } else if (1 == leftDoor && 1 == rightDoor && 0 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_left_right);
        } else if (0 == leftDoor && 0 == rightDoor && 1 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_under);
        } else if (1 == leftDoor && 1 == rightDoor && 1 == backDoor) {
            iv_car_door_state.setImageResource(R.drawable.state_all);
        }

    }


    public void refreshStaData(CurDayVehicleState paramCurDayVehicleState) {
        if (paramCurDayVehicleState != null) {
            if (paramCurDayVehicleState.getCh_soc() != null) {
                tvChargeSoc.setText(paramCurDayVehicleState.getCh_soc() + "%");
            }
            if (paramCurDayVehicleState.getCh_time() != null) {
                long l1 = Long.parseLong(paramCurDayVehicleState.getCh_time());
                int i = (int) (l1 / 3600000L);
                int j = (int) (l1 % 3600000L / 60000L);
                tvChargeTime.setText(i + "时" + j + "分");
            }
            if (paramCurDayVehicleState.getSpeed_time() != null) {
                long l2 = Long.parseLong(paramCurDayVehicleState.getSpeed_time());
                int k = (int) (l2 / 3600000L);
                int m = (int) (l2 % 3600000L / 60000L);
                tvDrivingTime.setText(k + "时" + m + "分");
            }
            if (paramCurDayVehicleState.getDr_distance() != null) {
                tvDrivingKilo.setText(paramCurDayVehicleState.getDr_distance() + "km");
            }
            if (paramCurDayVehicleState.getAv_speed() != null) {
                tvAvSpeed.setText(paramCurDayVehicleState.getAv_speed() + "km/h");
            }
            if (paramCurDayVehicleState.getKi_consume() != null) {
                tvKiloConsume.setText(paramCurDayVehicleState.getKi_consume() + "kw.h");
            }
        }
    }
}
