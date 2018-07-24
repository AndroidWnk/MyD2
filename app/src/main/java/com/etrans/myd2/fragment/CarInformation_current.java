package com.etrans.myd2.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.activity.CarInformationActivity;
import com.etrans.myd2.test.Battery;
import com.etrans.myd2.test.BatteryWarningType;
import com.etrans.myd2.test.Motor;

import cn.com.etrans.etsdk.manager.CANManager;

/**
 * Created by Administrator on 2018/5/7.
 */

public class CarInformation_current extends XxBaseFragment {

    private TextView tvCurSoc, tvBatteryTemp, tvBatteryVola, tvBatteryCurrent, tvMotorTemp, tvMotorVola, tvMotorMon;


    public CarInformation_current(int paramInt, boolean paramBoolean) {
        super(paramInt, paramBoolean);
    }

    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
        setView();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
        ((CarInformationActivity)getActivity()).getData();
    }


    public void refreshCurState(Motor paramMotor, Battery paramBattery) {

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
