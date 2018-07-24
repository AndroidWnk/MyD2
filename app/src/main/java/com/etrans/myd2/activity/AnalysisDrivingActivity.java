package com.etrans.myd2.activity;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.BatteryStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.etrans.myd2.R;
import com.etrans.myd2.biz.VehicleDiagnosisBiz;
import com.etrans.myd2.dao.idata.IDrivingHabitReport;
import com.etrans.myd2.dbhelper.CANContentConst;
import com.etrans.myd2.entity.DrivingHabitRecord;
import com.etrans.myd2.util.DateUtils;

/**
 * Created by Administrator on 2018/5/4.
 */

public class AnalysisDrivingActivity extends XxBaseActivity  implements View.OnClickListener,IDrivingHabitReport {

    private ContentResolver cr;
    private DateUtils du;
    private MyHabitContentObserver habitObr = new MyHabitContentObserver();
    private VehicleDiagnosisBiz vDiagnosisBiz;
    private TextView tvScore;
    private TextView tvSpeedCarefree;
    private TextView tvSpeedDown;
    private TextView tvSpeedSnail;
    private TextView tvSpeedUp;
/*    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
//            handlerAd.obtainMessage(4).sendToTarget();
        }
    };*/


    private Handler handlerAd = new Handler() {
        public void handleMessage(Message paramMessage) {
            if (paramMessage.what == 4 ) {
                int mark;
                DrivingHabitRecord localDrivingHabitRecord = (DrivingHabitRecord) paramMessage.obj;
                if (localDrivingHabitRecord != null) {
                    tvSpeedUp.setText(localDrivingHabitRecord.getAccSpeedUpCount() + "次");
                    tvSpeedDown.setText(localDrivingHabitRecord.getAccSpeedDownCount() + "次");
                    tvSpeedCarefree.setText(du.getDurationToMinStr(localDrivingHabitRecord.getCarefreeDriveDuration()));
                    tvSpeedSnail.setText(du.getDurationToMinStr(localDrivingHabitRecord.getSnailDriveDuration()));
                    mark = localDrivingHabitRecord.getHabitReportScore();

                    if (mark > 80) {
                        tvScore.setText(mark + "分");
                        tvScore.setTextColor(Color.GREEN);
                    } else {
                        tvScore.setText(mark + "分");
                        tvScore.setTextColor(Color.RED);
                    }
                    cancelProgress();

                }
            }
           /* else  {
                cancelProgress();
//                AnalysisDrivingActivity.setToastText("获取数据失败!");
            }*/
        }

    };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            du = DateUtils.getInstance();
            vDiagnosisBiz = new VehicleDiagnosisBiz(this);
            cr = getContentResolver();
//            showProgress("正在加载...");
//            handlerAd.postDelayed(mRunable,5000);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        handlerAd.removeCallbacksAndMessages(handlerAd);
        handlerAd = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        vDiagnosisBiz.getDrivingHabitReport(this);
        if (cr != null)
            cr.registerContentObserver(CANContentConst.VehicleDrivingRecord.CONTENT_URI, true, habitObr);
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (cr != null)
            cr.unregisterContentObserver(habitObr);

        super.onStop();
    }

    @Override
    protected void initView() {

        tvSpeedUp = ((TextView)findViewById(R.id.tv_speed_up_sum));
        tvSpeedDown = ((TextView)findViewById(R.id.tv_speed_down_sum));
        tvSpeedSnail = ((TextView)findViewById(R.id.tv_speed_snail_sum));
        tvSpeedCarefree = ((TextView)findViewById(R.id.tv_speed_carfree_sum));
        tvScore = ((TextView)findViewById(R.id.tv_mark));

    }

    @Override
    protected int getLayout() {
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.activity_anlysis_driving;
    }

    @Override
    public void onUpdateUI(Message msg) {

    }


    @Override
    public void onClick(View view) {


    }

    @Override
    public void getDrivingHabitReport(DrivingHabitRecord paramDrivingHabitRecord) {
        handlerAd.obtainMessage(4,paramDrivingHabitRecord).sendToTarget();
    }

    private class MyHabitContentObserver extends ContentObserver
    {
        public MyHabitContentObserver()
        {
            super(handlerAd);
        }

        public boolean deliverSelfNotifications()
        {
            return super.deliverSelfNotifications();
        }

        public void onChange(boolean paramBoolean)
        {
            super.onChange(paramBoolean);
            if (handlerAd != null)
                vDiagnosisBiz.getDrivingHabitReport(AnalysisDrivingActivity.this);
        }
    }

}
