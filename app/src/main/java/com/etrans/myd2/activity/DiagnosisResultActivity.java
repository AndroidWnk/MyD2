package com.etrans.myd2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.test.Battery;
import com.etrans.myd2.test.BatteryWarningType;
import com.etrans.myd2.test.Motor;
import com.etrans.myd2.test.MotorWarningType;

/**
 * Created by Administrator on 2018/5/4.
 */

public class DiagnosisResultActivity extends XxBaseActivity implements View.OnClickListener {


    private Battery battery;
    private String[] checkTexts;
    int check_num = 0;
    private ImageView[] ivCheck = new ImageView[6];
    private int[] ivCheckIds = {R.id.iv_result1, R.id.iv_result2, R.id.iv_result3, R.id.iv_result4, R.id.iv_result5, R.id.iv_result6};
    private Motor motor;
    private String notifyStr = "\n请到豆伴中查看维修站信息";
    private TextView tvCheckCore;
    private int[] tvCheckDetailIds = {R.id.tv_detail1, R.id.tv_detail2, R.id.tv_detail3, R.id.tv_detail4, R.id.tv_detail5, R.id.tv_detail6};
    private TextView[] tvCheckDetails = new TextView[6];
    private int[] tvTitleID = {R.id.tv_batteryTemp, R.id.tv_batteryVoltage, R.id.tv_batteryCurrent, R.id.tv_motorTemp, R.id.tv_motorVoltaget, R.id.tv_motoronitoring};
    private TextView[] tvTitles = new TextView[6];


    private void setText(int[] paramArrayOfInt, String[] paramArrayOfString) {
        String[] arrayOfString = new String[6];
        int i = 0;
        for (int j = 0; j < 6; j++) {
            if (paramArrayOfInt[j] == 1) {
                ivCheck[j].setImageResource(R.drawable.zhengchang);
                tvCheckDetails[j].setText(paramArrayOfString[i]);
                tvCheckDetails[j].setTextColor(Color.parseColor("#ff5353"));
                i++;
            } else {
                arrayOfString[j] = this.checkTexts[j];
            }
        }
    }

    private void setView() {
        tvCheckCore = (TextView) findViewById(R.id.tv_result_mark);
        for (int i = 0; ; i++) {
            if (i >= 6) {
                checkTexts = getResources().getStringArray(R.array.checkText);
                return;
            }
            tvTitles[i] = ((TextView) findViewById(tvTitleID[i]));
            tvCheckDetails[i] = ((TextView) findViewById(tvCheckDetailIds[i]));
            ivCheck[i] = ((ImageView) findViewById(ivCheckIds[i]));
        }
    }

    //刷新诊断结果数据
    public void refreshData() {
        int[] arrayOfInt = new int[6];
        String[] arrayOfString = new String[6];
        int i = 100;
        int j = (int) battery.getMostTempValue();
        if (battery.isWarning(BatteryWarningType.BATTERY_TEMP_TYPE)) {
            i -= 17;
            arrayOfInt[0] = 1;
            int i2 = check_num;
            check_num = (i2 + 1);
            arrayOfString[i2] = (checkTexts[0] + "为" + j + "℃" + notifyStr);
        }
        float f1 = battery.getPackVoltageValue();
        if (battery.isWarning(BatteryWarningType.BATTERY_VOLA_TYPE)) {
            i -= 17;
            arrayOfInt[1] = 1;
            int i1 = check_num;
            this.check_num = (i1 + 1);
            arrayOfString[i1] = (checkTexts[1] + "为" + f1 + "V" + notifyStr);
        }
        float f2 = battery.getPackCurrentValue();
        if (battery.isWarning(BatteryWarningType.BATTERY_CURRENT_TYPE)) {
            i -= 17;
            arrayOfInt[2] = 1;
            int n = this.check_num;
            this.check_num = (n + 1);
            arrayOfString[n] = (checkTexts[2] + "为" + f2 + "A" + notifyStr);
        }
        float f3 = motor.getMotorTempValue();
        if (motor.isWarning(MotorWarningType.MOTOR_TEMP_TYPE)) {
            i -= 17;
            arrayOfInt[3] = 1;
            int m = check_num;
            this.check_num = (m + 1);
            arrayOfString[m] = (checkTexts[3] + "为" + f3 + "℃" + notifyStr);
        }
        float f4 = motor.getMotorDCVoltageValue();
        if (motor.isWarning(MotorWarningType.MOTOR_VOLA_TYPE)) {
            i -= 17;
            arrayOfInt[4] = 1;
            int k = check_num;
            check_num = (k + 1);
            arrayOfString[k] = (checkTexts[4] + "为" + f4 + "V" + notifyStr);
        }
        motor.getMotorWorkStatus();
        if (motor.isMotorWarning()) {
            i -= 15;
            arrayOfInt[5] = 1;
            arrayOfString[check_num] = (checkTexts[5] + "发生异常" + notifyStr);
        }
        tvCheckCore.setText(i + "分");
        setText(arrayOfInt, arrayOfString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_result);
        setView();
        motor = new Motor(this);
        battery = new Battery(this);
        refreshData();
    }

    @Override
    protected void initView() {
//

    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onUpdateUI(Message msg) {

    }
}
