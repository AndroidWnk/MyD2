package com.etrans.myd2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.etrans.myd2.R;
import com.etrans.myd2.util.VehicleUtils;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CarDiagnosisActivity extends XxBaseActivity  implements View.OnClickListener {

    private Button mCarDiagnosis;
    private ImageView mCheck_Sao;
    private Animation mCheck_Sao_Animation;
    private Button mBtn_Diagnosis;



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

        mCarDiagnosis = (Button) findViewById(R.id.btn_diagnosis);
        mCheck_Sao = (ImageView) findViewById(R.id.iv_diagnosis_sao);
        mBtn_Diagnosis = (Button) findViewById(R.id.btn_diagnosis);
        mCheck_Sao_Animation = AnimationUtils.loadAnimation(this,R.anim.valuestext);

        mCarDiagnosis.setOnClickListener(this);

        mCarDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheck_Sao.setVisibility(View.VISIBLE);
                mCarDiagnosis.setClickable(false);
//                mBtn_Diagnosis.setBackgroundResource(0);//设置为无背景
                mBtn_Diagnosis.setText(R.string.diagnosing_car);
                mCheck_Sao.startAnimation(mCheck_Sao_Animation);

            }
        });

        mCheck_Sao_Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent = new Intent();
                intent.setClass(CarDiagnosisActivity.this,DiagnosisResultActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }



    @Override
    protected int getLayout() {
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.activity_car_diagnosis;
    }

    @Override
    public void onUpdateUI(Message msg) {

    }


    @Override
    public void onClick(View view) {


    }
}
