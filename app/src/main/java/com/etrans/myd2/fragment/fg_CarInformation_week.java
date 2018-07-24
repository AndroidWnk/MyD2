package com.etrans.myd2.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etrans.myd2.R;
import com.etrans.myd2.activity.CarInformationActivity;
import com.etrans.myd2.util.DateUtils2;
import com.etrans.myd2.view.MyMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class fg_CarInformation_week extends XxBaseFragment {

    private View view;
    private Context mContext;
    private BarChart barChart;//充电次数表
    private BarChart barChart1;//充电时间表
    private BarChart barChart2;//行驶里程表
    private BarChart barChart3;//行车时间表
    private BarChart barChart4;//百公里耗电表
    private BarChart barChart5;//行车速度表

    private TextView tv_barAvSpeed;
    private TextView tv_barChargeCounts;
    private TextView tv_barChargeTime;
    private TextView tv_barDriveDis;
    private TextView tv_barDriveTime;
    private TextView tv_barKiloConsume;
    private List<Float> listData;
    private List<Float> listData1;
    private List<Float> listData2;
    private List<Float> listData3;
    private List<Float> listData4;
    private List<Float> listData5;
    private float driveDayPerWeek = 0.0f;
    private int FIVE,FOUR,THREE,TWO,ONE,ZERO;
    public fg_CarInformation_week(int paramInt, boolean paramBoolean) {
        super(paramInt, paramBoolean);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_week, null);
        mContext = getActivity();
        initView();
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        isStart = true;
        ((CarInformationActivity)getActivity()).getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        driveDayPerWeek = 0.0f;
    }

    private void initView() {
//        setTitle(getIntent().getStringExtra("title"));11

        listData = new ArrayList<>();
        listData1 = new ArrayList<>();
        listData2 = new ArrayList<>();
        listData3 = new ArrayList<>();
        listData4 = new ArrayList<>();
        listData5 = new ArrayList<>();
        barChart = (BarChart) view.findViewById(R.id.mBarChartChargeNum);
        barChart1 = (BarChart) view.findViewById(R.id.mBarChartChargeTime);
        barChart2 = (BarChart) view.findViewById(R.id.mBarChartDrivingKilo);
        barChart3 = (BarChart) view.findViewById(R.id.mBarChartDrivingTime);
        barChart4 = (BarChart) view.findViewById(R.id.mBarChartHundred_kilo_consume);
        barChart5 = (BarChart) view.findViewById(R.id.mBarChartAvSpeed);
        tv_barChargeCounts = (TextView) view.findViewById(R.id.tv_week_charge_number_sum);
        tv_barChargeTime = (TextView) view.findViewById(R.id.tv_week_charge_time_sum);
        tv_barDriveDis = (TextView) view.findViewById(R.id.tv_week_driving_kilo_sum);
        tv_barDriveTime = (TextView) view.findViewById(R.id.tv_week_driving_time_sum);
        tv_barKiloConsume = (TextView) view.findViewById(R.id.tv_week_hundred_kilo_consume_sum);
        tv_barAvSpeed = (TextView) view.findViewById(R.id.tv_week_average_speed_sum);

    }
    public void refreshRecentWeekData(List<float[]> paramList)
    {
        float[] arrayOfFloat1 = paramList.get(6);
        for (int i = 0; i <arrayOfFloat1.length ; i++) {
            driveDayPerWeek += arrayOfFloat1[i];
        }
        Log.i("hlj","近一周行驶的天数 f === "+driveDayPerWeek);
        float[] arrayOfFloat2 = paramList.get(5);
        FIVE = (int)Math.ceil(getMaxFloat(arrayOfFloat2));
        Log.i("hlj0703"," ===FIVE=== "+FIVE);
        initData(arrayOfFloat2,listData5);
        float[] arrayOfFloat3 = paramList.get(4);
        FOUR = (int)Math.ceil(getMaxFloat(arrayOfFloat3));
        Log.i("hlj0703"," ===FOUR=== "+FOUR);
        initData(arrayOfFloat3,listData4);
        float[] arrayOfFloat4 = paramList.get(3);
        THREE = (int)Math.ceil(getMaxFloat(arrayOfFloat4));
        Log.i("hlj0703"," ===THREE=== "+THREE);
        initData(arrayOfFloat4,listData3);
        float[] arrayOfFloat5 = paramList.get(2);
        TWO = (int)Math.ceil(getMaxFloat(arrayOfFloat5));
        Log.i("hlj0703"," ===TWO=== "+TWO);
        initData(arrayOfFloat5,listData2);
        float[] arrayOfFloat6 = paramList.get(1);
        ONE = (int)Math.ceil(getMaxFloat(arrayOfFloat6));
        Log.i("hlj0703"," ===ONE=== "+ONE);
        initData(arrayOfFloat6,listData1);
        float[] arrayOfFloat7 = paramList.get(0);
        ZERO = (int)Math.ceil(getMaxFloat(arrayOfFloat7));
        Log.i("hlj0703"," ===ZERO=== "+ZERO);
        initData(arrayOfFloat7,listData);
        showBarChart(7);
    }

    public static float getMaxFloat(float[] paramArrayOfFloat)
    {
        float f1;
        if (paramArrayOfFloat == null) {
            f1 = -1.0F;
        }
        while (true)
        {
            f1 = 0.0F;
            for (int i = 0; i < paramArrayOfFloat.length; i++)
            {
                float f2 = paramArrayOfFloat[i];
                if (f2 <= f1)
                    continue;
                f1 = f2;
            }
            return f1;
        }
    }

    //Y轴的值
    private void initData(float[] f,List listData) {
        for (int i = 0; i <f.length ; i++) {
            listData.add(f[i]);
        }
    }


    private void showBarChart(int count) {//count表示横坐标个数
        initSrc(barChart);
        initSrc(barChart1);
        initSrc(barChart2);
        initSrc(barChart3);
        initSrc(barChart4);
        initSrc(barChart5);

        initXAxis(barChart,ZERO);
        initXAxis(barChart1,ONE);
        initXAxis(barChart2,TWO);
        initXAxis(barChart3,THREE);
        initXAxis(barChart4,FOUR);
        initXAxis(barChart5,FIVE);

        BarData barData = getBarData(tv_barChargeCounts,count);
        BarData barData1 = getBarData1(tv_barChargeTime,count);
        BarData barData2 = getBarData2(tv_barDriveDis,count);
        BarData barData3 = getBarData3(tv_barDriveTime,count);
        BarData barData4 = getBarData4(tv_barKiloConsume,count);
        BarData barData5 = getBarData5(tv_barAvSpeed,count);

        barData.setValueFormatter(new DayIntegerYValueFormatter());//将float数据格式化为整数;
        barChart.setData(barData); // 设置数据
        barData1.setValueFormatter(new YValueFormatter());//保留1一位小数;
        barChart1.setData(barData1);
        barData2.setValueFormatter(new YValueFormatter());
        barChart2.setData(barData2);
        barData3.setValueFormatter(new YValueFormatter());
        barChart3.setData(barData3);
        barData4.setValueFormatter(new YValueFormatter());
        barChart4.setData(barData4);
        barData5.setValueFormatter(new YValueFormatter());
        barChart5.setData(barData5);
        barChart.invalidate();//刷新图表
        barChart1.invalidate();
        barChart2.invalidate();
        barChart3.invalidate();
        barChart4.invalidate();
        barChart5.invalidate();

    }

    private void initSrc(BarChart barChart) {
        barChart.setNoDataText("");//没有数据时显示为空
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");// 数据描述
        barChart.setDescriptionColor(Color.RED);//数据的颜色
        barChart.setDescriptionTextSize(30);//数据字体大小
        barChart.setTouchEnabled(false);//设置柱形图不可触摸
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.BLUE); // 表格的的颜色

//        barChart.setBackgroundColor(Color.WHITE);// 设置整个图表控件的背景

     /*   barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果
        barChart.setScaleEnabled(true);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setDoubleTapToZoomEnabled(false);//设置双击不放大
        barChart.setDrawValueAboveBar(true);//柱状图上面的数值显示在柱子上面还是柱子里面
        barChart.zoom(5, 0, 0, 0);//设置缩放比例
        barChart.animateXY(2000, 3000);*/

        Legend mLegend = barChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
        mLegend.setEnabled(false);//true和false能控制比例图标识是否显示

        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);//设置比例图标的位置
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//设置比例图标和文字之间的位置方向
        mLegend.setTextColor(0xff1AA9E9);

        ValueFormatter custom = new MyYAxisValueFormatter();//自定义Y轴文字样式
        barChart.getAxisLeft().setValueFormatter(custom);
        barChart.getAxisLeft().setTextSize(18);//字体大小
        barChart.getAxisLeft().setTextColor(Color.WHITE);


        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view);
        barChart.setMarkerView(mv);
    }

    private void initXAxis(BarChart barChart,int maxNumber) {
        barChart.getXAxis().setDrawGridLines(false);//是否显示竖直标尺线
        barChart.getXAxis().setLabelsToSkip(0);//设置横坐标显示的间隔
        barChart.getXAxis().setDrawLabels(true);//是否显示X轴数值
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置 默认在上方
        barChart.getXAxis().setDrawAxisLine(true);//是否显示X轴
        barChart.getXAxis().setTextSize(18);//设置X轴坐标字体的大小
        barChart.getXAxis().setTextColor(Color.WHITE);//设置X轴坐标字体颜色
        barChart.getXAxis().setAxisLineColor(0xff1AA9E9);//设置X轴的颜色

        barChart.getAxisRight().setDrawLabels(false);//右侧是否显示Y轴数值
        barChart.getAxisRight().setEnabled(false);//是否显示最右侧竖线
        barChart.getAxisRight().setDrawAxisLine(false);//是否画最右侧竖线
        barChart.getAxisLeft().setAxisLineColor(0xff1AA9E9);
        barChart.getAxisLeft().setDrawAxisLine(true);//是否画最左侧竖线
        if (driveDayPerWeek > 0) {
            barChart.getAxisLeft().setDrawLabels(true);//左侧显示Y轴数值
        }else {
            barChart.getAxisLeft().setDrawLabels(false);//左侧不显示Y轴数值
        }
        if (maxNumber < 5) {
            barChart.getAxisLeft().setAxisMaxValue(6);//为了解决数据过小导致Y轴坐标重复的问题
        }else {
            barChart.getAxisLeft().setAxisMaxValue(maxNumber + (maxNumber / driveDayPerWeek));
        }
        barChart.getAxisLeft().setDrawGridLines(false);//是否显示横向标尺线
    }

    private BarData getBarData(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和
        textView.setText("本周共充电" + String.valueOf((int) yValueSum) + "次");//获取柱形图上面数字的总和
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }
    private BarData getBarData1(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData1.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和//
        textView.setText("本周累计充电" + String.format("%.1f",yValueSum) + "小时");
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }
    private BarData getBarData2(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData2.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        if (driveDayPerWeek > 0) {
            barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        }
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和
        textView.setText("本周累计行驶里程" + String.format("%.1f",yValueSum) + "km");//获取柱形图上面数字的总和
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    private BarData getBarData3(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData3.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和//
        textView.setText("本周累计行驶" + String.format("%.1f",yValueSum) + "小时");
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    private BarData getBarData4(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData4.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和//
        if (yValueSum > 0.0f) {
            yValueSum /= driveDayPerWeek;
        }
        textView.setText("本周百公里耗电" + String.format("%.1f",yValueSum) + "kw.h");//获取柱形图上面数字的总和
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    private BarData getBarData5(TextView textView,int count) {
        //count  x轴个数
        List<String> xValues = DateUtils2.getAllTheDateOftheWeek(new Date());//获取近一周日期
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yValues.add(new BarEntry(listData5.get(i), i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(50);//设置柱子之间的宽度
        barDataSet.setVisible(true);//是否显示柱状图柱子
        barDataSet.setColor(0xff1AA9E9);//设置柱子颜色
        barDataSet.setDrawValues(true);//是否显示柱子上面的数值
        barDataSet.setValueTextColor(Color.WHITE);//设置柱子上面数字的颜色
        float yValueSum = barDataSet.getYValueSum();//获取Y轴柱形图值的总和//
        if (yValueSum > 0.0f) {
            yValueSum /= driveDayPerWeek;
        }
        textView.setText("本周平均车速" + String.format("%.1f", yValueSum) + "km/h");//获取柱形图上面数字的总和
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);

        BarData barData = new BarData(xValues, barDataSet);

        return barData;
    }

    //格式化float数据为int
    public class DayIntegerYValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float v) {
            return String.valueOf((int)v);
        }
    }

    public class YValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float v) {

            return String.format("%.1f",v);
        }
    }

    //设置Y轴刻度样式
    public class MyYAxisValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,###,##0");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + "";
        }
    }
}
