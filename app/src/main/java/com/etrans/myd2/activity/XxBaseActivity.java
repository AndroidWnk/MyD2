package com.etrans.myd2.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etrans.myd2.MyApplication;
import com.etrans.myd2.XxBaseModule;
import com.etrans.myd2.util.AppUtil;

public abstract class XxBaseActivity extends Activity implements XxBaseModule.IUpdateUI {
    protected LayoutInflater mInflater;

    private GestureDetector mGestureDetector;

    private boolean mbExitOnFling = false;
    private ImageView mSoundState;
    private ProgressDialog m_pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("XxBaseActivity", "onCreate()");
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGestureDetector = new GestureDetector(this, onGestureListener);

        if (getLayout() != 0) {
            View view = mInflater.inflate(getLayout(), null);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

            if (getKeepScreenFlag())
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(view);
        }

        if (getDelayInit()) {
             handler.sendEmptyMessageDelayed(1, 30);
        } else {
            initView();
        }
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtil.sendTitleBroadCast(this, (String) getTitle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (m_pDialog != null)
        {
            m_pDialog.dismiss();
            m_pDialog = null;
        }
    }

    //添加不随系统修改字体而修改字体
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    initView();

                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }

    };

    protected abstract void initView();

    protected abstract int getLayout();

    protected boolean getDelayInit() {
        return false;
    }

    protected boolean getKeepScreenFlag() {
        return false;
    }

    protected boolean onGestrueDown(MotionEvent e) {
        return false;
    }

    protected boolean onGestrueSingleTap(MotionEvent e) {
        return false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO 自动生成的方法存根
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
        public boolean onDown(MotionEvent e) {

            if (onGestrueDown(e))
                return true;

            return super.onDown(e);
        }

    };

    private static Toast singleToast = null;

    public static void setToastText(String text) {
        if (singleToast == null) {
            singleToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
        }
        singleToast.setText(text);
        singleToast.show();
    }


    public void cancelProgress()
    {
        if ((m_pDialog != null) && (m_pDialog.isShowing()))
            m_pDialog.cancel();
    }


    //弹出dialog
    public void showProgress(String paramString)
    {
        if (m_pDialog == null)
        {
            m_pDialog = new ProgressDialog(this);
            m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            m_pDialog.setMessage("请稍等。。。");
            m_pDialog.setIndeterminate(false);
            m_pDialog.setCancelable(true);
        }
        if (paramString != null)
            m_pDialog.setMessage(paramString);
        if (!m_pDialog.isShowing())
            m_pDialog.show();
    }

}
