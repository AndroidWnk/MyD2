package com.etrans.myd2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurDayVehicleState implements Serializable {
    private static final long serialVersionUID = 1L;
    private String av_speed;//平均速度
    private String ch_soc;//充电电量
    private String ch_time;//充电时间
    private String charge_counts;//充电次数
    private String co_soc;//总电量
    private String day;//日期
    private String dr_distance;//行驶里程
    private String ki_consume;//百公里耗电
    private String speed_soc;//行车耗电
    private String speed_time;//行车时间

    public static List<float[]> getBarData(ArrayList<CurDayVehicleState> paramArrayList) {
        ArrayList localArrayList = new ArrayList();
        int i = paramArrayList.size();
        float[] arrayOfFloat1 = new float[i];
        float[] arrayOfFloat2 = new float[i];
        float[] arrayOfFloat3 = new float[i];
        float[] arrayOfFloat4 = new float[i];
        float[] arrayOfFloat5 = new float[i];
        float[] arrayOfFloat6 = new float[i];
        float[] arrayOfFloat7 = new float[i];
        for (int j = 0; ; j++) {
            if (j >= i) {
                localArrayList.add(arrayOfFloat1);
                localArrayList.add(arrayOfFloat2);
                localArrayList.add(arrayOfFloat3);
                localArrayList.add(arrayOfFloat4);
                localArrayList.add(arrayOfFloat5);
                localArrayList.add(arrayOfFloat6);
                localArrayList.add(arrayOfFloat7);
                return localArrayList;
            }
            CurDayVehicleState localCurDayVehicleState = (CurDayVehicleState) paramArrayList.get(j);
            if (localCurDayVehicleState == null)
                continue;
            arrayOfFloat7[j] = 1.0F;
            String str1 = localCurDayVehicleState.getCharge_counts();
            if (str1 != null)
                arrayOfFloat1[j] = Integer.parseInt(str1);
            String str2 = localCurDayVehicleState.getCh_time();
            if (str2 != null)
                arrayOfFloat2[j] = (Math.round((float) (10L * Long.parseLong(str2) / 3600000L)) / 10.0F);
            if (localCurDayVehicleState.getDr_distance() != null)
                arrayOfFloat3[j] = Float.parseFloat(localCurDayVehicleState.getDr_distance());
            String str3 = localCurDayVehicleState.getSpeed_time();
            if (str3 != null)
                arrayOfFloat4[j] = (Math.round((float) (10L * Long.parseLong(str3) / 3600000L)) / 10.0F);
            String str4 = localCurDayVehicleState.getKi_consume();
            if (str4 != null)
                arrayOfFloat5[j] = Float.parseFloat(str4);
            String str5 = localCurDayVehicleState.getAv_speed();
            if (str5 != null)
            arrayOfFloat6[j] = Float.parseFloat(str5);
        }
    }

    public String getAv_speed() {
        return this.av_speed;
    }

    public String getCh_soc() {
        return this.ch_soc;
    }

    public String getCh_time() {
        return this.ch_time;
    }

    public String getCharge_counts() {
        return this.charge_counts;
    }

    public String getCo_soc() {
        return this.co_soc;
    }

    public String getDay() {
        return this.day;
    }

    public String getDr_distance() {
        return this.dr_distance;
    }

    public String getKi_consume() {
        return this.ki_consume;
    }

    public String getSpeed_soc() {
        return this.speed_soc;
    }

    public String getSpeed_time() {
        return this.speed_time;
    }

    public void setAv_speed(String paramString) {
        this.av_speed = paramString;
    }

    public void setCh_soc(String paramString) {
        this.ch_soc = paramString;
    }

    public void setCh_time(String paramString) {
        this.ch_time = paramString;
    }

    public void setCharge_counts(String paramString) {
        this.charge_counts = paramString;
    }

    public void setCo_soc(String paramString) {
        this.co_soc = paramString;
    }

    public void setDay(String paramString) {
        this.day = paramString;
    }

    public void setDr_distance(String paramString) {
        this.dr_distance = paramString;
    }

    public void setKi_consume(String paramString) {
        this.ki_consume = paramString;
    }

    public void setSpeed_soc(String paramString) {
        this.speed_soc = paramString;
    }

    public void setSpeed_time(String paramString) {
        this.speed_time = paramString;
    }

    public String toString() {
        return "CurDayVehicleState [day=" + this.day + ", ch_soc=" + this.ch_soc + ", ch_time=" + this.ch_time + ", co_soc=" + this.co_soc + ", speed_soc=" + this.speed_soc + ", speed_time=" + this.speed_time + ", dr_distance=" + this.dr_distance + ", av_speed=" + this.av_speed + ", charge_counts=" + this.charge_counts + ", ki_consume=" + this.ki_consume + "]";
    }

}