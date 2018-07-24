package com.etrans.myd2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.etrans.myd2.R;
import com.etrans.myd2.dao.config.VehicleConfig_100;
import com.etrans.myd2.util.AppUtil;
import com.etrans.myd2.util.SystemTime;
import com.etrans.myd2.util.VehicleUtils;

import java.util.Date;


public class MainActivity extends XxBaseActivity implements View.OnClickListener {

    private ImageView mFile_explorer_image, mZd_iev_image, mMaintenance_information_image, mUser_manual_image;
    private XxBaseActivity bs;


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

        mFile_explorer_image = (ImageView) findViewById(R.id.main_file_explorer_image);
        mZd_iev_image = (ImageView) findViewById(R.id.main_zd_iev_image);
        mMaintenance_information_image = (ImageView) findViewById(R.id.main_maintenance_information_image);
        mUser_manual_image = (ImageView) findViewById(R.id.main_user_manual_image);

        mFile_explorer_image.setOnClickListener(this);
        mZd_iev_image.setOnClickListener(this);
        mMaintenance_information_image.setOnClickListener(this);
        mUser_manual_image.setOnClickListener(this);

    }


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onUpdateUI(Message msg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_file_explorer_image:
                AppUtil.startAppWithPackageName(MainActivity.this, "net.micode.fileexplorer");

                break;
            case R.id.main_zd_iev_image:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ZDievActivity.class);
                startActivity(intent);
                break;
            case R.id.main_maintenance_information_image:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, MaintenanceActivity.class);
                startActivity(intent2);
                break;
            case R.id.main_user_manual_image:
                bs.setToastText("敬请期待！！！");

                break;
        }

    }


}
