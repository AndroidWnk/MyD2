package com.etrans.myd2.util;

import android.widget.Toast;

import com.etrans.myd2.MyApplication;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ToastUtil {

    private static Toast singleToast = null;

    public static void setToastText(String text) {
        if (singleToast == null) {
            singleToast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
        }
        singleToast.setText(text);
        singleToast.show();
    }

}
