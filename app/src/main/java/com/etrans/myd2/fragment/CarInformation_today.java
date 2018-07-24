package com.etrans.myd2.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.activity.CarInformationActivity;
import com.etrans.myd2.entity.CurDayVehicleState;

/**
 * Created by Administrator on 2018/5/7.
 */

@SuppressLint("ValidFragment")
public class CarInformation_today extends XxBaseFragment {

    private TextView tvDrivingKilo,tvKiloConsume,tvChargeSoc,tvDrivingTime,tvAvSpeed,tvChargeTime;

    public CarInformation_today(int paramInt, boolean paramBoolean) {
        super(paramInt, paramBoolean);
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        setView();
    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
        ((CarInformationActivity)getActivity()).getData();
    }

    private void setView() {
        tvDrivingKilo = (TextView) view.findViewById(R.id.tv_today_driving_kilo);
        tvKiloConsume = (TextView) view.findViewById(R.id.tv_today_hundred_kilo_consume);
        tvChargeSoc = (TextView) view.findViewById(R.id.tv_today_charge_soc);
        tvDrivingTime = (TextView) view.findViewById(R.id.tv_today_driving_time);
        tvAvSpeed = (TextView) view.findViewById(R.id.tv_today_average_speed);
        tvChargeTime = (TextView) view.findViewById(R.id.tv_today_charge_time);

    }

    public void refreshStaData(CurDayVehicleState paramCurDayVehicleState)
    {
        if (paramCurDayVehicleState != null)
        {
            tvChargeSoc.setText(paramCurDayVehicleState.getCh_soc() + "%");
            long l1 = Long.parseLong(paramCurDayVehicleState.getCh_time());
            int i = (int)(l1 / 3600000L);
            int j = (int)(l1 % 3600000L / 60000L);
            tvChargeTime.setText(i + "时" + j + "分");
            long l2 = Long.parseLong(paramCurDayVehicleState.getSpeed_time());
            int k = (int)(l2 / 3600000L);
            int m = (int)(l2 % 3600000L / 60000L);
            tvDrivingTime.setText(k + "时" + m + "分");
            tvDrivingKilo.setText(paramCurDayVehicleState.getDr_distance() + "km");
            tvAvSpeed.setText(paramCurDayVehicleState.getAv_speed() + "km/h");
            tvKiloConsume.setText(paramCurDayVehicleState.getKi_consume() + "kw.h");
        }
    }

}
