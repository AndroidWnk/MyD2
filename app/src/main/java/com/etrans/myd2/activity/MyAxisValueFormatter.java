package com.etrans.myd2.activity;

import com.etrans.myd2.util.DateUtils2;
import com.github.mikephil.charting.components.AxisBase;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */
public class MyAxisValueFormatter /*implements IAxisValueFormatter*/ {
/*    private DecimalFormat mFormat;
    private List<String> list = DateUtils2.getAllTheDateOftheWeek2(new Date());

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String  date=list.get( (int)value);
        return  date.substring(date.length()-5,date.length());
//        if ((int)(value+1)>=0&&((int)(value+1)<10)){
//            return "0"+(int)(value+1);
//        }else{
//            return (int)(value+1)+"";
//        }
    }*/
}
