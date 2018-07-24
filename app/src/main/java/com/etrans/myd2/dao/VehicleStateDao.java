package com.etrans.myd2.dao;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.etrans.myd2.entity.BaseDayVehicleState;
import com.etrans.myd2.entity.CurDayVehicleState;
import com.etrans.myd2.dao.sta.DiaEntity;
import com.etrans.myd2.dao.sta.StaEntity;
import com.etrans.myd2.util.DateUtils;
import com.etrans.myd2.util.SystemTime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.etrans.etsdk.can.BCMInfoChangeObserver;
import cn.com.etrans.etsdk.can.BatteryInfoChangeObserver;
import cn.com.etrans.etsdk.can.VehicleInfoChangeObserver;
import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;

public class VehicleStateDao
        implements Runnable {
    private static final DiaEntity diaEntity;
    private static final ExecutorService esBase;
    private static final ExecutorService esSta;
    private static final StaEntity staEntity = new StaEntity();
    private final BatteryObr batteryObr;
    private CANManager canManager;
    private CurDayVehicleState curState;
    private boolean dateIsFault;
    private Thread defandThread;
    private DateUtils du = DateUtils.getInstance();
    private boolean isBaseInit;
    private boolean isCurDayKiloInit;
    private boolean isDefand;
    private boolean isLoop;
    private BaseDayVehicleState lastBase;
    private long lastStaTime;
    private final VehicleBCMObr myBCMObr;
    private Thread timerThread;
    private boolean toClearInit;
    private final VehicleInfoObr vInfoObr;
    private VehicleProviderDao vProviderDao;
    private long l;
    private Context context;

    static {
        diaEntity = new DiaEntity();
        esSta = Executors.newSingleThreadExecutor();
        esBase = Executors.newSingleThreadExecutor();
    }

    public VehicleStateDao(Context paramContext) {
        context = paramContext;
        vProviderDao = new VehicleProviderDao(paramContext);
        //初始化CANManager
        EtSDK.getInstance(paramContext).addListener(new EtSDK.SDKInitListener() {
            @Override
            public void onConnectSuccess() {
                Log.i("EtSDK", "VehicleStateDao ready");
                canManager = EtSDK.getInstance(context).getCanManager();
            }
        });

        vInfoObr = new VehicleInfoObr();
        myBCMObr = new VehicleBCMObr();
        batteryObr = new BatteryObr();
        if (canManager != null) {//hlj 0716
            canManager.attachVehicleInfoObserver(vInfoObr);
            canManager.attachBCMInfoObserver(myBCMObr);
            canManager.attachBatteryInfoObserver(batteryObr);
        }
        initBaseData();//初始化基础数据
        startTimerSta();//
        defandThread();
    }

    private void defandThread() {
        isDefand = true;
        defandThread = new Thread() {
            public void run() {
//                while (true) {//hlj 修改
                    if (!isDefand) {
                        return;
                    }
                    if ((!isLoop) && (timerThread != null)) {
                        timerThread.interrupt();
                        startTimerSta();
                    }
                    try {
                        Thread.sleep(60000L);
                    } catch (InterruptedException localInterruptedException) {
                        localInterruptedException.printStackTrace();
                    }
//                }
            }
        };
        defandThread.start();
    }

    private void initBaseData() {
        if (canManager != null) {//hlj 0716
            staEntity.setCurSoc(canManager.getSOCValue());//获取SOC值
            staEntity.setCurKilo(canManager.getVehileOdoMeterValue());//整车行驶里程信息
            staEntity.setCurSpeed(canManager.getVehicleSpeedValue());//车辆速度信息
            staEntity.setGearCurStatus(canManager.getGearKEYReadyStatus(CANManager.GearKEYReady.GEAR));//车辆档位
        }
    }

    private void initCurDayKilo() {
        float curDayKilo = vProviderDao.getCurDayKilo();
        if (curDayKilo != -1) {
            Log.i("hlj initCurDayKilo", "当日最早里程为:" + curDayKilo);
            staEntity.setCurDayFirstKilo(curDayKilo);
            return;
        }
        float f2 = staEntity.getCurKilo() - staEntity.getDriveKilo();
        if (lastBase != null) {
            String str = lastBase.getKilo();
            if (str != null)
                f2 = Float.parseFloat(str);
        }
        if (curState != null) {
            float f3 = Float.parseFloat(curState.getDr_distance());
            if (f3 > 0.0F)
                f2 -= f3;
        }
        if (f2 < 0.0F)
            f2 = 0.0F;
        Log.i("hlj ", "未查到当日最早里程,取当前开机时里程:" + f2);
        vProviderDao.saveCurDayKilo(f2);
        staEntity.setCurDayFirstKilo(f2);
    }

    private void initStaKilo() {
        long currentTime = 0;//当前时间
        float rangeIncrement = 0;//里程增量
        if (lastBase == null) {
            Log.i("hlj  lastBase", "数据库中没有base数据...");
        } else {
            currentTime = System.currentTimeMillis();
            float f1 = Float.parseFloat(lastBase.getKilo());
            rangeIncrement = staEntity.getCurKilo() - f1;
        }
        if (rangeIncrement > 0.0F) {//hlj
            Log.i("hlj ", "发现关机里程增量:" + rangeIncrement + "km");
        }
        float avSpeed = 40.0F;//关机前的平均速度为0，则按默认的40km/h进行计算关机时间段的平均速度
        CurDayVehicleState localCurDayVehicleState1 = curState;
        String str1 = null;
        if (localCurDayVehicleState1 != null)
            str1 = curState.getAv_speed();
        if (str1 != null) {
            float f6 = Float.parseFloat(str1);
            if (f6 > 0.0F) {
                avSpeed = f6;
                Log.i("hlj ", "关机前平均车速:" + avSpeed);
            }
        }
        float f4 = 12.0F;
        CurDayVehicleState localCurDayVehicleState2 = curState;
        String str2 = null;
        if (localCurDayVehicleState2 != null)
            str2 = curState.getKi_consume();
        if (str2 != null) {
            float f5 = Float.parseFloat(str2);
            if (f5 > 0.0F) {
                f4 = f5;
                Log.i("hlj ", "关机前百公里耗电:" + f4);
            }
        }
        int i = Math.round(f4 * rangeIncrement / 15.33F);
        long speedTime = (long) (3600000.0F * rangeIncrement / avSpeed);//关机行车时间
        Log.i("hlj ", "关机期间加入行车耗电:" + i + "===行车时间:" + du.getDurationToMinStr(speedTime));
        if (staEntity.getStartSpeedSoc() > 0)
            staEntity.setStartSpeedSoc(i + staEntity.getStartSpeedSoc());
        while (staEntity.getStartSpeedTime() > 0L) {
            staEntity.setStartSpeedTime(staEntity.getStartSpeedTime() - speedTime);
            staEntity.setStartSpeedSoc(i + staEntity.getCurSoc());
            staEntity.setEndSpeedSoc(staEntity.getCurSoc());
        }
        staEntity.setStartSpeedTime(currentTime - speedTime);
        staEntity.setEndSpeedTime(currentTime);
    }

    private void initStaSoc() {
        if (lastBase == null) {
            Log.i("hlj  lastBase", "数据库中没有base数据。。。");
        } else {
            int lastBaseSoc = (int) Float.parseFloat(lastBase.getSoc());
            int curSoc = staEntity.getCurSoc();
            Log.i("hlj", "开机电量比较：\n上次关机电量为：" + lastBaseSoc + "==\n" + "当前电量为：" + curSoc + "==");
            long l1 = System.currentTimeMillis();
            if (curSoc < lastBaseSoc) {
                long l2 = 1000 * (60 * (lastBaseSoc - curSoc));
                staEntity.setStartCoSoc(lastBaseSoc);
                staEntity.setEndCoSoc(curSoc);
                staEntity.setStartCoTime(l1 - l2);
                staEntity.setEndCoTime(l1);
                Log.i("hlj", "关机期间总耗电:==" + staEntity.getCoSoc() + "====耗电时间：" + this.du.getDurationToMinStr(staEntity.getCoTime()));
            }
            if (curSoc - lastBaseSoc > 2) {
                staEntity.setStartChSoc(lastBaseSoc);
                staEntity.setEndChSoc(curSoc);
                staEntity.setStartChTime(l1 - 10 * (60 * (60 * (7 * (curSoc - lastBaseSoc)))));
                staEntity.setEndChTime(l1);
                staEntity.setChCounts(1);
                vProviderDao.saveChargeCounts(true);
                Log.i("hlj", "关机期间充电电量为：" + staEntity.getChSoc() + "====充电时间：" + du.getDurationToMinStr(staEntity.getChTime()));
            }
        }

    }

    private void startTimerSta() {
        isLoop = true;
        timerThread = new Thread() {
            public void run() {
                curSta();
//                while (true) {//hlj 修改
                    if (!isLoop)
                        return;
                    try {
                        l = 60000L - (System.currentTimeMillis() - lastStaTime);
                        if (l >= 5000L)
                            curSta();
                        Thread.sleep(55000L);
                    } catch (InterruptedException localInterruptedException) {
                        localInterruptedException.printStackTrace();
                    }
                    try {
                        Thread.sleep(10000);//hlj为l变量的话会报错
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//            }
        };
        timerThread.start();
    }

    public void collectBaseData() {
        if (!isBaseInit)
            return;//hlj 0712
        esBase.execute(new Runnable() {
            public void run() {
                BaseDayVehicleState localBaseDayVehicleState = BaseDayVehicleState.getCurBase(canManager);
                vProviderDao.insertBase(localBaseDayVehicleState);
            }
        });
    }

    public void collectStaData() {
        Log.i("hlj ", "开始收集基础数据是否合理：" + runSta());
        lastStaTime = SystemTime.currentTimeMillis();
        if (!runSta()) {
            Log.i("hlj ", "runSta 中有不合理的数据");
            staEntity.resetReasonable();
            return;
        } else {
            Log.i("hlj ", "runSta 基础数据正常");
            String str1 = du.getSystemDay();
            CurDayVehicleState localCurDayVehicleState = vProviderDao.querySta(str1);
            int i = staEntity.getChSoc();
            long l1 = staEntity.getChTime();
            long l2 = staEntity.getSpeedTime();
            int j = staEntity.getCoSoc();
            int k = staEntity.getSpeedSoc();
            float f1 = staEntity.getCurDayDriveKilo();
            int m = staEntity.getChCounts();

            if (localCurDayVehicleState == null) {
                localCurDayVehicleState = new CurDayVehicleState();
                localCurDayVehicleState.setDay(str1);
            }
            String str2 = localCurDayVehicleState.getCh_soc();
            if (str2 != null) {
                i += Integer.parseInt(str2);
            }
            String str3 = localCurDayVehicleState.getCh_time();
            if (str3 != null) {
                l1 += Long.parseLong(str3);
            }
            String str4 = localCurDayVehicleState.getCo_soc();
            if (str4 != null) {
                j += Integer.parseInt(str4);
            }
            String str5 = localCurDayVehicleState.getSpeed_soc();
            if (str5 != null) {
                k += Integer.parseInt(str5);
            }
            String str6 = localCurDayVehicleState.getSpeed_time();
            if (str6 != null) {
                Log.i("bbs", str6);
                l2 += Long.parseLong(str6);
            }
            float f2 = 0.0F;
            if (l2 > 0) {
                f2 = 3600000.0F * f1 / (float) l2;
            }
            String str7 = localCurDayVehicleState.getCharge_counts();
            if (str7 != null) {
                m += Integer.parseInt(str7);
            }
            float f3 = 0.0F;
            String str8 = localCurDayVehicleState.getKi_consume();
            if (f1 > 0.0F) {
                if (staEntity.getSpeedSoc() > 0)
                    f3 = 15.33F * k / f1;
            } else {
                if (str8 != null)
                    f3 = Float.parseFloat(str8);
            }
            if (!toClearInit) {
                toClearInit = true;
            }
            if ((i > 400) && (j > 400) && (k > 400) && (f1 > 720.0F) && (f2 > 120.0F)) {
                Log.i("hlj ", "入库校验出现问题!ch_Soc:co_Soc:speed_Soc:dr_Dis:speed" + i + ":" + j + ":" + k + ":" + f1 + ":" + f2);
                Log.i("hlj ", "staEntity.reset() 入库校验出现问题!  重置-------------");
                staEntity.reset();
            }
//            collectBaseData();

            localCurDayVehicleState.setCh_soc(i + "");
            localCurDayVehicleState.setCh_time(l1 + "");
            localCurDayVehicleState.setCo_soc(j + "");
            localCurDayVehicleState.setSpeed_soc(k + "");
            localCurDayVehicleState.setSpeed_time(l2 + "");
            Object[] arrayOfObject1 = new Object[1];
            arrayOfObject1[0] = Float.valueOf(f1);
            localCurDayVehicleState.setDr_distance(String.format("%.1f", arrayOfObject1));
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = Float.valueOf(f2);
            localCurDayVehicleState.setAv_speed(String.format("%.1f", arrayOfObject2));
            localCurDayVehicleState.setCharge_counts(m + "");
            Object[] arrayOfObject3 = new Object[1];
            arrayOfObject3[0] = Float.valueOf(f3);
            localCurDayVehicleState.setKi_consume(String.format("%.1f", arrayOfObject3));
            vProviderDao.insertSta(localCurDayVehicleState);
        }
    }

    public void curSta() {
        esSta.execute(this);
    }

    @Override
    public void run() {
        collectStaData();
    }


    public boolean runSta2() {//hlj 0718
        if (staEntity.isBaseUnreasonable()) {
            Log.i("hlj", "====基础数据异常====");
            return false;
        }
        if (!isBaseInit) {
            isBaseInit = true;
            vProviderDao.saveDiaDrivingStart(null);
            lastBase = vProviderDao.queryDayBase(null, null, true);
            initStaSoc();
        }
        long l = System.currentTimeMillis();
        dateIsFault = /*true*/ SystemTime.dateIsFault(l);//hlj test
        if ((!isCurDayKiloInit) && (!dateIsFault)) {
            Log.i("hlj", "时间是对了，进行关机历程初始化和当天最早总里程初始化");
            isCurDayKiloInit = true;
            curState = vProviderDao.querySta(du.getSystemDay());
            staEntity.renewTime(l);
            initCurDayKilo();
            initStaKilo();
            vProviderDao.clearData(staEntity.getCurKilo());
        }
        int i = staEntity.getCurSoc();
        int j = staEntity.getGearCurStatus();
        int k = staEntity.getCurPStatus();
        float f = staEntity.getCurKilo();
        switch (k) {
            case 2:
                if (toClearInit) {
                    Log.i("hlj ", "搁置状态,清除充电和放电soc以及充放电时间.");
                    if (staEntity.getStartChSoc() != 0) {
                        staEntity.setStartChTime(0L);
                        staEntity.setEndChTime(0L);
                        staEntity.setStartChSoc(0);
                        staEntity.setEndChSoc(0);
                    }
                    if (staEntity.getStartCoSoc() != 0) {
                        staEntity.setStartCoSoc(0);
                        staEntity.setEndCoSoc(0);
                        staEntity.setStartCoTime(0L);
                        staEntity.setEndCoTime(0L);
                    }
                }
                if (j == 0)
                    break;
                if (staEntity.getGearPreStatus() == 0) {
                    if (j == 1)
                        diaEntity.speedAnalysis(staEntity, vProviderDao);
                    if (staEntity.getSpeedSoc() == 0) {
                        staEntity.setStartSpeedSoc(i);
                        staEntity.setStartSpeedTime(l);
                        Log.i("hlj ", "开始记录行车时间:" + l);
                    }
                    if (staEntity.getStartDriveKilo() == 0.0F)
                        staEntity.setStartDriveKilo(f);
                    staEntity.setGearPreStatus(j);
                }
                staEntity.setEndSpeedSoc(i);
                staEntity.setEndSpeedTime(l);
                staEntity.setEndDriveKilo(f);
            case 3:
            case 1:
        }
        if ((!staEntity.isStaUnreasonable()) && (!dateIsFault)) {
            if (staEntity.getStartChSoc() == 0) {
                Log.i("hlj ", "记录开始充电时间!");
                staEntity.setStartChTime(l);
                staEntity.setStartChSoc(i);
            }
            staEntity.setEndChTime(l);
            staEntity.setEndChSoc(i);
            if (staEntity.getStartCoSoc() == 0) {
                Log.i("hlj ", "记录开始放电时间!");
                staEntity.setStartCoSoc(i);
                staEntity.setStartCoTime(l);
            }
            staEntity.setEndCoSoc(i);
            staEntity.setEndCoTime(l);
            if (staEntity.getGearPreStatus() == 1)
                diaEntity.speedAnalysis(staEntity, this.vProviderDao);
            if (staEntity.getGearPreStatus() == 0)
                staEntity.setEndSpeedSoc(i);
            staEntity.setEndSpeedTime(l);
            staEntity.setEndDriveKilo(f);
            Log.i("hlj ", "变为N档停止计时:" + l);
            staEntity.setGearPreStatus(j);
        }
        return true;
    }

    public boolean runSta()
    {
        if (staEntity.isBaseUnreasonable())
        {
            Log.i("hlj","====基础数据异常====");
            return false;
        }
        if (!isBaseInit)
        {
            isBaseInit = true;
            vProviderDao.saveDiaDrivingStart(null);
            lastBase = vProviderDao.queryDayBase(null, null, true);
            initStaSoc();
        }
        long l = System.currentTimeMillis();
        dateIsFault = SystemTime.dateIsFault(l);
        if ((!isCurDayKiloInit) && (!this.dateIsFault))
        {
            Log.i("hlj","时间是对了，进行关机历程初始化和当天最早总里程初始化");
            this.isCurDayKiloInit = true;
            this.curState = this.vProviderDao.querySta(this.du.getSystemDay());
            staEntity.renewTime(l);
            initCurDayKilo();
            initStaKilo();
            this.vProviderDao.clearData(staEntity.getCurKilo());
        }
        int i = staEntity.getCurSoc();
        int j = staEntity.getGearCurStatus();
        int k = staEntity.getCurPStatus();
        float f = staEntity.getCurKilo();
        switch (k)
        {
            case 2:
            default:
                if (this.toClearInit)
                {
                    Log.i("hlj","搁置状态,清除充电和放电soc以及充放电时间.");
                    if (staEntity.getStartChSoc() != 0)
                    {
                        staEntity.setStartChTime(0L);
                        staEntity.setEndChTime(0L);
                        staEntity.setStartChSoc(0);
                        staEntity.setEndChSoc(0);
                    }
                    if (staEntity.getStartCoSoc() != 0)
                    {
                        staEntity.setStartCoSoc(0);
                        staEntity.setEndCoSoc(0);
                        staEntity.setStartCoTime(0L);
                        staEntity.setEndCoTime(0L);
                    }
                }
                if (j == 0)
                    break;
                if (staEntity.getGearPreStatus() == 0)
                {
                    if (j == 1)
                        diaEntity.speedAnalysis(staEntity, this.vProviderDao);
                    if (staEntity.getSpeedSoc() == 0)
                    {
                        staEntity.setStartSpeedSoc(i);
                        staEntity.setStartSpeedTime(l);
                        Log.i("hlj","开始记录行车时间:" + l);
                    }
                    if (staEntity.getStartDriveKilo() == 0.0F)
                        staEntity.setStartDriveKilo(f);
                    staEntity.setGearPreStatus(j);
                }
                staEntity.setEndSpeedSoc(i);
                staEntity.setEndSpeedTime(l);
                staEntity.setEndDriveKilo(f);
            case 3:
            case 1:
        }
        Log.i("hlj ","isStaUnreasonable == "+staEntity.isStaUnreasonable() +",, dateIsFault == "+dateIsFault);
        if ((!staEntity.isStaUnreasonable()) && (!this.dateIsFault))
        {

            if (staEntity.getStartChSoc() == 0)
            {
                Log.i("hlj","记录开始充电时间!");
                staEntity.setStartChTime(l);
                staEntity.setStartChSoc(i);
            }
            staEntity.setEndChTime(l);
            staEntity.setEndChSoc(i);
            if (staEntity.getStartCoSoc() == 0)
            {
                Log.i("hlj","记录开始放电时间!");
                staEntity.setStartCoSoc(i);
                staEntity.setStartCoTime(l);
            }
            staEntity.setEndCoSoc(i);
            staEntity.setEndCoTime(l);
            if (staEntity.getGearPreStatus() == 1)
                diaEntity.speedAnalysis(staEntity, this.vProviderDao);
            if (staEntity.getGearPreStatus() == 0)
            staEntity.setEndSpeedSoc(i);
            staEntity.setEndSpeedTime(l);
            staEntity.setEndDriveKilo(f);
            Log.i("hlj","变为N档停止计时:" + l);
            staEntity.setGearPreStatus(j);
        }
        return true;
    }

    public void stopSta() {
        try {
            vInfoObr.onGearStatusChange(0);
            return;
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    public class BatteryObr extends BatteryInfoChangeObserver {
        private long preSpeedTime = System.currentTimeMillis();

        public BatteryObr() {
        }

        @Override
        public void onPackCurrentValueChange(float currentValue) throws RemoteException {//电池包电压改变
            super.onPackCurrentValueChange(currentValue);
            Log.i("hlj","onPackCurrentValueChange currentValue == "+currentValue);//hlj 修改
            long l = SystemTime.currentTimeMillis();
            if (l - this.preSpeedTime > 500L) {
                preSpeedTime = l;
                if (staEntity.getCurSpeed() <= 0.0F) {
                    if (currentValue > 1.0F) {
                        if (staEntity.getCurPStatus() != 1)
                            staEntity.setCurPStatus(1);//放电状态
                        curSta();
                        return;
                    }
                    if (currentValue < -5.0F) {
                        if (staEntity.getCurPStatus() != 3)
                            staEntity.setCurPStatus(3);//充电状态
                        curSta();
                        return;
                    }
                    if (staEntity.getCurPStatus() != 0) {
                        staEntity.setCurPStatus(0);//搁置状态
                        curSta();
                    }
                    return;
                }
                if (staEntity.getCurPStatus() != 1) {
                    staEntity.setCurPStatus(1);//放电状态
                    curSta();
                }
            }
        }

        @Override
        public void onSOCPercentValueChange(int percentValue) throws RemoteException {//电池包SOC数值
            super.onSOCPercentValueChange(percentValue);
            VehicleStateDao.staEntity.setCurSoc(percentValue);
            curSta();
        }
    }

    public class VehicleBCMObr extends BCMInfoChangeObserver {

        @Override
        public void onLFDoorStateChange(int LFDoorState) throws RemoteException {
            super.onLFDoorStateChange(LFDoorState);
            collectBaseData();
        }

        @Override
        public void onRFDoorStateChange(int RFDoorState) throws RemoteException {
            super.onRFDoorStateChange(RFDoorState);
            collectBaseData();
        }

        @Override
        public void onBackDoorStateChange(int BackDoorState) throws RemoteException {
            super.onBackDoorStateChange(BackDoorState);
            collectBaseData();
        }
    }

    public class VehicleInfoObr extends VehicleInfoChangeObserver {
        private long preSpeedTime = System.currentTimeMillis();

        public VehicleInfoObr() {
        }

        @Override
        public void onGearStatusChange(int paramInt) throws RemoteException {
            super.onGearStatusChange(paramInt);
            Log.i("canTest", "档位状态:===" + paramInt);
            VehicleStateDao.staEntity.setGearCurStatus(paramInt);
            curSta();
        }

        @Override
        public void onMaintainInfoChange(int mInfo) throws RemoteException {//保养信息改变
            super.onMaintainInfoChange(mInfo);
        }

        @Override
        public void onReadySignalStatusChange(int readySignal) throws RemoteException {
            super.onReadySignalStatusChange(readySignal);
            Log.i("hlj canTest", "Ready状态:===" + readySignal);
        }

        @Override
        public void onRemainMaintainKiloChange(int rmKilo) throws RemoteException {
            super.onRemainMaintainKiloChange(rmKilo);
        }

        @Override
        public void onVehicleSpeedChange(float vehicleSpeed) throws RemoteException {//车辆速度改变
            super.onVehicleSpeedChange(vehicleSpeed);
            VehicleStateDao.staEntity.setCurSpeed(vehicleSpeed);
            long l = System.currentTimeMillis();
            if (l - preSpeedTime > 500L) {
                preSpeedTime = l;
                esBase.execute(new Runnable() {
                    public void run() {
                        diaEntity.speedAnalysis(staEntity, vProviderDao);
                    }
                });
            }
        }

        @Override
        public void onVehileOdoMeterValueChange(float vehileOdoMeter) throws RemoteException {//整车行驶里程信息改变
            super.onVehileOdoMeterValueChange(vehileOdoMeter);
            staEntity.setCurKilo(vehileOdoMeter);
            curSta();

        }
    }
}
