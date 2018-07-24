package com.etrans.myd2.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.biz.MaintenanceBiz;


/**
 * Created by Administrator on 2018/5/4.
 */

public class MaintenanceActivity extends XxBaseActivity implements View.OnClickListener, MaintenanceBiz.IMaintenanceBiz {
    private static final int UI_TYPE_NO_NEED = 1;
    private static final int UI_TYPE_REACH = 0;
    private static final int UI_TYPE_SET_REACH = 5;
    private static final int UI_TYPE_SET_WILL_NEED = 7;
    private static final int UI_TYPE_WILL_NEED = 2;
    private View btnReset;
    private TextView firstMaintenanceShow;
    private View llCarShow;
    private View llMaintenanceNormal;
    private View llMaintenanceSet;
    private View llMaintenanceShowDialog;
    private View llSuggestMaintennace;
    private MaintenanceBiz maintenanceBiz;
    private TextView thirdMaintenanceShow;
    private LinearLayout secondMaintenanceShow;
    private LinearLayout noNeedMaintenanceShow;
    private LinearLayout needMaintenanceShow;
    private TextView topMaintenanceShow;
    private TextView topMaintenanceShowAfter;
    private View tvBtnJia;
    private View tvBtnJian;
    private TextView tvBtnZhong;
    private EditText tvPreKilo;
    private int uiType;
    private Context context;
    private int i;
    private int j;

    //无需保养
    private void noNeedMaintenanceUI() {
        secondMaintenanceShow.setVisibility(View.GONE);
        needMaintenanceShow.setVisibility(View.GONE);
        noNeedMaintenanceShow.setVisibility(View.VISIBLE);
    }

    //达到保养
    private void reachMaintenanceUI() {
        secondMaintenanceShow.setVisibility(View.GONE);
        noNeedMaintenanceShow.setVisibility(View.GONE);
        needMaintenanceShow.setVisibility(View.VISIBLE);

    }

    //手动设置保养里程
    private void setWillNeedMaintenanceUI() {
        secondMaintenanceShow.setVisibility(View.VISIBLE);
        topMaintenanceShow.setVisibility(View.VISIBLE);
        topMaintenanceShowAfter.setVisibility(View.VISIBLE);
        noNeedMaintenanceShow.setVisibility(View.GONE);
        needMaintenanceShow.setVisibility(View.GONE);
        firstMaintenanceShow.setVisibility(View.VISIBLE);
        topMaintenanceShowAfter.setVisibility(View.VISIBLE);

    }
    //手动设置达到保养里程
    private void setReachMaintenanceUI() {

        secondMaintenanceShow.setVisibility(View.VISIBLE);
        firstMaintenanceShow.setVisibility(View.GONE);
        topMaintenanceShow.setText("您的爱车已达到保养里程,请保养！");
        topMaintenanceShowAfter.setVisibility(View.GONE);
        noNeedMaintenanceShow.setVisibility(View.GONE);
        needMaintenanceShow.setVisibility(View.GONE);
    }

    //刷新数据
    private void refreshData(View paramView, boolean paramBoolean) {
        i = maintenanceBiz.getSetPeriodKilo();//保养间隔
        j = maintenanceBiz.getSetPreKilo();//上次保养里程
        Log.i("hlj", "getSetPeriodKilo == " + i + " getSetPreKilo == " + j);
        int remainMaintainKilo = maintenanceBiz.getSetRemainMaintainKilo();//剩余保养里程
        if (paramView != null) {
            switch (paramView.getId()) {
                case R.id.btn_zhong:

                    break;
                case R.id.edit_maintenance_preKilo:
                    if (i <= 3000)
                        tvBtnJian.setBackgroundResource(R.drawable.maintenance_btn_jian_down);
                    if (i >= 10000) {
                        tvBtnJia.setBackgroundResource(R.drawable.maintenance_btn_jia_down);
                        if (paramBoolean) {
                            this.tvBtnZhong.setText(i + "km");
                            this.tvPreKilo.setText(j + "km");
                        }

                        switch (this.uiType) {
                            case 6:
                            default:
                            case 7:
                            case 5:
                        }
                    }
                case R.id.btn_jian:
                    if (i - 1000 >= 3000) {
                        i -= 1000;
                        tvBtnZhong.setText(i + "km");
                        topMaintenanceShow.setText(i + "km");
                        maintenanceBiz.setRemainMaintainKilo(i, j);
                    }
                    break;
                case R.id.btn_jia:
                    if (i + 1000 <= 10000) {
                        i += 1000;
                        tvBtnZhong.setText(i + "km");
                        topMaintenanceShow.setText(i + "km");
                        maintenanceBiz.setRemainMaintainKilo(i, j);
                    }
                    break;

                case R.id.btn_maintenannce_reset_distance:
//                    maintenanceBiz.reset();
//                    maintenanceBiz.setRemainMaintainKilo(i, j);//hlj 0712
                   int curKilo = maintenanceBiz.getCurKilo();
                    tvPreKilo.setText(curKilo + "km");
                    if (remainMaintainKilo > 0) {
                    topMaintenanceShow.setText(remainMaintainKilo + "km");
                }
                    break;
                default:
                    break;
            }
        }
    }

/*
    //刷新数据
    private void refreshData3(View paramView, boolean paramBoolean) {

        int i = this.maintenanceBiz.getSetPeriodKilo();//保养间隔
        int j = this.maintenanceBiz.getSetPreKilo();//上一次保养里程
        if (paramView != null);
        int k = 0;
        switch (paramView.getId())
        {
            case R.id.btn_zhong:
            case R.id.edit_maintenance_preKilo:
            default:
                this.maintenanceBiz.reset();
                this.maintenanceBiz.setRemainMaintainKilo(i, j);
                if (i > 3000)
                    break;
                this.tvBtnJian.setBackgroundResource(R.drawable.maintenance_btn_jian_down);
                if (i >= 10000)
                {
                    this.tvBtnJia.setBackgroundResource(R.drawable.maintenance_btn_jia_down);
                    if (paramBoolean)
                    {
                        this.tvBtnZhong.setText(i + "km");
                        this.tvPreKilo.setText(j + "km");
                    }
                    k = this.maintenanceBiz.getSetRemainMaintainKilo();//获取设置的保养里程
                    switch (this.uiType)
                    {
                        case 6:
                        default:
                        case 7:
                        case 5:
                    }
                }
            case R.id.btn_jian:
                if (i - 1000 < 3000){
                this.tvBtnJian.setBackgroundResource(R.drawable.maintenance_btn_jian_down);
            }else {
                    tvBtnJian.setBackgroundResource(0);
                    i -= 1000;
                }
                break;
            case R.id.btn_jia:
                if (i + 1000 > 10000){
                    this.tvBtnJia.setBackgroundResource(R.drawable.maintenance_btn_jia_down);
                }else
                    tvBtnJia.setBackgroundResource(0);
                i += 1000;
                j = this.maintenanceBiz.getCurKilo();
                break ;
            case R.id.btn_maintenannce_reset_distance:
        }
            if (k > 0)
            {
                this.secondMaintenanceShow.setText(k + "km");
                return;
            }
            setReachMaintenanceUI();
            this.uiType = 5;
        if (k <= 0) {
            setWillNeedMaintenanceUI();
            this.uiType = 7;
            this.secondMaintenanceShow.setText(k + "km");
        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        maintenanceBiz = new MaintenanceBiz(this, this, false);
        setContentView(R.layout.activity_maintenance);
        setView();
        context = this;
        refreshUI(true);


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
    protected void onStart() {
        super.onStart();
        maintenanceBiz.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        maintenanceBiz.onStop();
    }

    @Override
    protected void initView() {


    }

    private void setView() {

        btnReset = findViewById(R.id.btn_maintenannce_reset_distance);
        tvBtnJia = findViewById(R.id.btn_jia);
        tvBtnJian = findViewById(R.id.btn_jian);
        tvBtnZhong = (TextView) findViewById(R.id.btn_zhong);
        tvPreKilo = (EditText) findViewById(R.id.edit_maintenance_preKilo);
        topMaintenanceShow = (TextView) findViewById(R.id.text_kilo_top);
        topMaintenanceShowAfter = (TextView) findViewById(R.id.text_after);
        secondMaintenanceShow = (LinearLayout) findViewById(R.id.ll_maintence_main);
        noNeedMaintenanceShow = (LinearLayout) findViewById(R.id.ll_noneed_maintence);
        needMaintenanceShow = (LinearLayout) findViewById(R.id.ll_need_maintence);
        firstMaintenanceShow = (TextView) findViewById(R.id.text_maintenannce_top1);

        topMaintenanceShow.setText(maintenanceBiz.getSetPeriodKilo() + "km");
        tvBtnZhong.setText(maintenanceBiz.getSetPeriodKilo() + "km");

        tvBtnJia.setOnClickListener(this);
        tvBtnJian.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        tvPreKilo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int paramInt, KeyEvent keyEvent) {
                Log.i("hlj", "keyEvent == " + keyEvent.getKeyCode());
                int i = 0;
                if (66 == keyEvent.getKeyCode()) {
                    String str = getNumberFromString(tvPreKilo.getText().toString(), 0);
                    if (str.length() < 8)
                        str = str.substring(0, str.length());
                    if (str != "") {
                        i = Integer.parseInt(str);
                    }
                    int j = maintenanceBiz.getCurKilo();
                    if (i > j) {
                        setToastText("不能大于当前总里程!");
                        i = j;
                    }
                    tvPreKilo.setText(i + "km");
                    int k = Integer.parseInt(getNumberFromString(tvBtnZhong.getText().toString(), 3000));
                    maintenanceBiz.setRemainMaintainKilo(k, i);
                    maintenanceBiz.reset();
                    refreshData(null, true);
                }
                return false;
            }
        });


    }

    public String getNumberFromString(String paramString, int paramInt) {
        String str1 = paramInt + "";
        String str2;
        if (!TextUtils.isEmpty(paramString)) {
            str2 = paramString.trim();
            str1 = "";
            for (int i = 0; i < str2.length(); i++) {
                char c = str2.charAt(i);
                if ((c >= '0') && (c <= '9'))
                    str1 = str1 + c;
            }
            return str1;
        }
        return str1;
    }


    @Override
    protected int getLayout() {
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        return 0;
    }

    @Override
    public void onUpdateUI(Message msg) {

    }

    public int getUiType() {
        if (maintenanceBiz.isCanSupport()) {
            int j = maintenanceBiz.getMaintainInfo();//保养信息：无信息(0)、剩余保养里程有效(1)、立即保养(2)
            setToastText("仪表模式-->保养状态为:" + j);
            switch (j) {
                case 0:
                default:
                    return 1;
                case 1:
                    return 2;
                case 2:
            }
            return 0;
        }else {
            int i = maintenanceBiz.getSetRemainMaintainKilo();
            setToastText("手动设置模式-->剩余保养里程为:" + i);
            if (i > 0) {
                return 7;
            }
            return 5;
        }
    }

    @Override
    public void onClick(View view) {

        refreshData(view, true);

    }

    @Override
    public void onMaintenanceInfoChange() {
        runOnUiThread(new Runnable() {
            public void run() {
                setToastText("刷新UI界面!");
                refreshUI(false);
            }
        });

    }

    private void refreshUI(boolean paramBoolean) {
        uiType = /*getUiType()*/7;
        switch (uiType) {
            case 0:
                reachMaintenanceUI();
                return;
            case 1:
                noNeedMaintenanceUI();
                return;
            case 2:
                willNeedMaintenanceUI();
                return;
            case 7:
                setWillNeedMaintenanceUI();
                refreshData(null, paramBoolean);
                return;
            case 5:
                setReachMaintenanceUI();//hlj 0712
                refreshData(null, paramBoolean);
            default:
                return;
        }


    }

    private void willNeedMaintenanceUI() {
        secondMaintenanceShow.setVisibility(View.VISIBLE);
        needMaintenanceShow.setVisibility(View.GONE);
        needMaintenanceShow.setVisibility(View.GONE);

    }

    @Override
    public void onNeedShowDilog(boolean paramBoolean) {

    }
}
