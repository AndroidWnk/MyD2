package com.etrans.myd2.entity;


import com.etrans.myd2.util.DateUtils;
import com.etrans.myd2.util.VehicleUtils;

import java.io.Serializable;

import cn.com.etrans.etsdk.manager.CANManager;

/**
 * Created by Administrator on 2018/5/14.
 */
public class BaseDayVehicleState implements Serializable{


    private static final long serialVersionUID = 1L;
    private String behindDoor;
    private String carLeftDoor;
    private String carLeftWindow;
    private String carRightDoor;
    private String carRightWindow;
    private String ch_state;
    private String day;
    private String driveRange;
    private String hours;
    private String kilo;
    private String soc;
    private String speed;

    public static BaseDayVehicleState getCurBase(CANManager paramCANManager)
    {
        BaseDayVehicleState localBaseDayVehicleState = new BaseDayVehicleState();
        DateUtils localDateUtils = DateUtils.getInstance();
        String str1 = localDateUtils.getNativeSystemDay();
        String str2 = localDateUtils.getNativeSystemTime();
        int i = paramCANManager.getPackageStatus();//0x0：搁置, 0x1：放电, 0x2：回馈,0x3 ：充电机充电
        float f1 = paramCANManager.getVehileOdoMeterValue();//整车行驶里程信息
        int j = paramCANManager.getSOCValue();//电池包SOC数值
        float f2 = paramCANManager.getVehicleSpeedValue();//车辆速度信息
        int k = paramCANManager.getLFDoorState();//左门状态 0：关 ， 1：开
        int m = paramCANManager.getRFDoorState();//右门状态 0：关 ， 1：开
        int back = paramCANManager.getBackDoorState();//后门状态 0：关 ， 1：开
        float f3 = j * 105 / 100;
        float canRange = paramCANManager.getRangeValue();//hlj for test
        localBaseDayVehicleState.setCh_state(""+i);
        localBaseDayVehicleState.setDay(str1);
        localBaseDayVehicleState.setHours(str2);
        localBaseDayVehicleState.setKilo(""+f1);
        localBaseDayVehicleState.setSoc(""+ VehicleUtils.getOdoSoc(100, j));//hlj
        localBaseDayVehicleState.setSpeed(""+f2);
        localBaseDayVehicleState.setCarLeftDoor(""+k);
        localBaseDayVehicleState.setCarRightDoor(""+m);
        localBaseDayVehicleState.setCarLeftWindow(""+0);
        localBaseDayVehicleState.setCarRightWindow(""+0);
        localBaseDayVehicleState.setBehindDoor(""+back);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Float.valueOf(VehicleUtils.getCanRange(f3, j));//工具类有问题暂时注释
//        localBaseDayVehicleState.setDriveRange(String.format("%.1f", arrayOfObject));//hlj
        localBaseDayVehicleState.setDriveRange(String.format("%.1f", canRange));//hlj for test
        return localBaseDayVehicleState;
    }

    public String getBehindDoor()
    {
        return this.behindDoor;
    }

    public String getCarLeftDoor()
    {
        return this.carLeftDoor;
    }

    public String getCarLeftWindow()
    {
        return this.carLeftWindow;
    }

    public String getCarRightDoor()
    {
        return this.carRightDoor;
    }

    public String getCarRightWindow()
    {
        return this.carRightWindow;
    }

    public String getCh_state()
    {
        return this.ch_state;
    }

    public String getDay()
    {
        return this.day;
    }

    public String getDriveRange()
    {
        return this.driveRange;
    }

    public String getHours()
    {
        return this.hours;
    }

    public String getKilo()
    {
        return this.kilo;
    }

    public String getSoc()
    {
        return this.soc;
    }

    public String getSpeed()
    {
        return this.speed;
    }

    public void setBehindDoor(String paramString)
    {
        this.behindDoor = paramString;
    }

    public void setCarLeftDoor(String paramString)
    {
        this.carLeftDoor = paramString;
    }

    public void setCarLeftWindow(String paramString)
    {
        this.carLeftWindow = paramString;
    }

    public void setCarRightDoor(String paramString)
    {
        this.carRightDoor = paramString;
    }

    public void setCarRightWindow(String paramString)
    {
        this.carRightWindow = paramString;
    }

    public void setCh_state(String paramString)
    {
        this.ch_state = paramString;
    }

    public void setDay(String paramString)
    {
        this.day = paramString;
    }

    public void setDriveRange(String paramString)
    {
        this.driveRange = paramString;
    }

    public void setHours(String paramString)
    {
        this.hours = paramString;
    }

    public void setKilo(String paramString)
    {
        this.kilo = paramString;
    }

    public void setSoc(String paramString)
    {
        this.soc = paramString;
    }

    public void setSpeed(String paramString)
    {
        this.speed = paramString;
    }

    public String toString()
    {
        return "BaseDayVehicleState [day=" + this.day + ", hours=" + this.hours + ", soc=" + this.soc + ", ch_state=" + this.ch_state + ", speed=" + this.speed + ", kilo=" + this.kilo + ", carLeftDoor=" + this.carLeftDoor + ", carRightDoor=" + this.carRightDoor + ", carLeftWindow=" + this.carLeftWindow + ", carRightWindow=" + this.carRightWindow + ", behindDoor=" + this.behindDoor + "]";
    }


}
