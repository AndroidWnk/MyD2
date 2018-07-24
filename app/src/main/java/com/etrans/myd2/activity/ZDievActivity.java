package com.etrans.myd2.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.etrans.myd2.R;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ZDievActivity extends XxBaseActivity  implements View.OnClickListener {

    private RelativeLayout car_information_layout,car_diagnosis_layout,analysis_driving_layout;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void initView() {

        car_information_layout = (RelativeLayout) findViewById(R.id.car_information_layout);
        car_diagnosis_layout = (RelativeLayout) findViewById(R.id.car_diagnosis_layout);
        analysis_driving_layout = (RelativeLayout) findViewById(R.id.analysis_driving_layout);

        initListener();
    }

    public void initListener() {
        car_information_layout.setOnClickListener(this);
        car_diagnosis_layout.setOnClickListener(this);
        analysis_driving_layout.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.activity_zdiev;
    }

    @Override
    public void onUpdateUI(Message msg) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.car_information_layout:
                Intent intent = new Intent();
                intent.setClass(this,CarInformationActivity.class);
                startActivity(intent);

                /* Intent intent2 = new Intent(Intent.ACTION_MAIN);
                intent2.setComponent(new ComponentName("com.xdy.douban", "com.xdy.douban.activity.Act_VehicleState"));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent2);*///测试直接可以跳转到豆伴应用的车辆信息页面
                break;

            case R.id.car_diagnosis_layout:
                Intent intent2 = new Intent();
                intent2.setClass(this,CarDiagnosisActivity.class);
                startActivity(intent2);
                break;

            case R.id.analysis_driving_layout:

                Intent intent3 = new Intent();
                intent3.setClass(this,AnalysisDrivingActivity.class);
                startActivity(intent3);

                break;
        }

    }
}
