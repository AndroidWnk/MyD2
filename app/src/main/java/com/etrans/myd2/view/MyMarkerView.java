
package com.etrans.myd2.view;

import android.content.Context;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;


public class MyMarkerView extends MarkerView {
    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        tvContent.setTextColor(0xff148CEF);
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
//            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));//值为四舍五入后的整数
            tvContent.setText("" + ce.getHigh());
        } else {
//            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));//值为四舍五入后的整数
            tvContent.setText("" + e.getVal());
        }
    }

    @Override
    public int getXOffset() {//设置偏移量
        return -getMeasuredWidth()/2;
    }

    @Override
    public int getYOffset() {//设置偏移量
        return -getMeasuredHeight();
    }
}
