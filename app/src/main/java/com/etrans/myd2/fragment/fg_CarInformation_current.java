package com.etrans.myd2.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.etrans.myd2.R;
import com.etrans.myd2.test.Battery;
import com.etrans.myd2.test.BatteryWarningType;
import com.etrans.myd2.test.Motor;
import cn.com.etrans.etsdk.can.VehicleInfoChangeObserver;
import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;

/**
 * Created by Administrator on 2018/5/7.
 */

public class fg_CarInformation_current extends XxBaseFragment {

    private View view;
    private Context mContext;
    private ImageView iv_car_door_state;
    private TextView tvCurSoc, tvBatteryTemp, tvBatteryVola, tvBatteryCurrent, tvMotorTemp, tvMotorVola, tvMotorMon;
    private int[] doorStatusId = {R.drawable.state_car, R.drawable.state_all, R.drawable.state_left_right, R.drawable.state_left, R.drawable.state_right, R.drawable.state_under};
    private CANManager canManager;
    private int leftDoor, rightDoor, backDoor;
    private Motor motor;
    private Battery battery;
    private ContentResolver cr;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            refreshDoorState(); //更新数据
            refreshState(motor, battery);
            Log.i("hlj  实时页面", " 每隔1秒刷新数据");
            handler.postDelayed(this, 1000);//定时时间
        }
    };

    public fg_CarInformation_current(int paramInt, boolean paramBoolean) {
        super(paramInt, paramBoolean);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_current, null);
        mContext = getActivity();
        motor = new Motor(mContext);
        battery = new Battery(mContext);
        //初始化CANManager
        if (EtSDK.isReady()) {
            Log.i("EtSDK", "ready");
            init();
        } else {
            EtSDK.getInstance(mContext).addListener(new EtSDK.SDKInitListener() {
                @Override
                public void onConnectSuccess() {
                    Log.i("EtSDK", "ready");
                    init();
                }

            });
        }

        setView();
        return view;


    }

    private void init() {
        canManager = EtSDK.getInstance(mContext).getCanManager();
    }

    //初始化view
    private void setView() {
        tvCurSoc = (TextView) view.findViewById(R.id.tv_cur_soc);
        tvBatteryTemp = (TextView) view.findViewById(R.id.tv_cur_battery_temperature);
        tvBatteryVola = (TextView) view.findViewById(R.id.tv_cur_battery_voltage);
        tvBatteryCurrent = (TextView) view.findViewById(R.id.tv_cur_battery_current);
        tvMotorTemp = (TextView) view.findViewById(R.id.tv_cur_motor_temperature);
        tvMotorVola = (TextView) view.findViewById(R.id.tv_cur_motor_voltage);
        tvMotorMon = (TextView) view.findViewById(R.id.tv_cur_motor_monitor);
        iv_car_door_state = (ImageView) view.findViewById(R.id.iv_car_door_state);

        refreshDoorState();
        refreshState(motor, battery);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        battery.detBatteryObr();
        motor.detMotorListener();
    }

    private void refreshDoorState() {
        leftDoor = canManager.getLFDoorState();
        rightDoor = canManager.getRFDoorState();
        backDoor = canManager.getBackDoorState();
    Log.i("hlj  current","leftDoor == "+leftDoor+" rightDoor == " +rightDoor+" backDoor == "+backDoor);
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

    private void refreshState(Motor paramMotor, Battery paramBattery) {

        int curSoc = paramBattery.getSOCValue();
        if (paramBattery.isWarning(BatteryWarningType.SOC_TYPE)) {
            tvCurSoc.setText(curSoc + "%");
            tvCurSoc.setTextColor(Color.RED);
        }else {
            tvCurSoc.setText(curSoc + "%");
            tvCurSoc.setTextColor(Color.WHITE);
        }
        int m = (int) paramBattery.getMostTempValue();
        tvBatteryTemp.setText(m + "℃");

        int n = Math.round(paramBattery.getPackVoltageValue());
        tvBatteryVola.setText(n + "V");

        int i1 = (int) paramBattery.getPackCurrentValue();
        tvBatteryCurrent.setText(i1 + "A");

        int k = (int) paramMotor.getMotorTempValue();
        tvMotorTemp.setText(k + "℃");

        int j = Math.round(paramMotor.getMotorDCVoltageValue());
        tvMotorVola.setText(j + "V");

        boolean bool = paramMotor.isMotorWarning();
        String str;
        if (!bool) {
            str = "正常";
            tvMotorMon.setText(str);
            tvMotorMon.setTextColor(Color.WHITE);
        } else {
            str = "异常";
            tvMotorMon.setText(str);
            tvMotorMon.setTextColor(Color.RED);
        }

    }
}
