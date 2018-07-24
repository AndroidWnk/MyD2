package com.etrans.myd2.test;

import android.content.Context;
import android.os.RemoteException;
import com.etrans.myd2.test.impl.IBatteryAlarm;
import com.etrans.myd2.test.impl.IBatteryParam;
import com.etrans.myd2.util.VehicleUtils;
import cn.com.etrans.etsdk.can.BatteryInfoChangeObserver;
import cn.com.etrans.etsdk.manager.CANManager;


public class Battery extends CoreComponents
        implements IBatteryParam, IBatteryAlarm {
    private BatteryInfoChangeObserver batteryObr;
    private float canRange = -1.0F;
    private CoreComponents.IDataNotify iDataNotify;

    public Battery(Context paramContext) {
        super(paramContext);
    }

    public void detBatteryObr() {
        if (this.batteryObr != null)
            this.canManager.detachBatteryInfoObserver(this.batteryObr);
        this.iDataNotify = null;
    }

    public float getCanRange() {//续航里程
        /*if (canRange < 0.0F)
            canRange = VehicleUtils.getCanRange(canRange,canManager.getSOCValue());
        return canRange;*/
        return canManager.getRangeValue();//hlj for test
    }

    public byte[] getCellWarning() {
        return this.canManager.getCellWarning();
    }

    public float getMostTempValue() {
        float f1 = 0.0F;
        for (int i = 1; ; i++) {
            if (i > 8)
                return f1;
            float f2 = getSensorTempValue(i);
            if (f2 <= f1)
                continue;
            f1 = f2;
        }
    }

    public float getPackCurrentValue() {//电池组电流
        return canManager.getPackCurrentValue();
    }

    public float getPackVoltageValue() {//电池组电压
        return canManager.getPackVoltageValue();
    }

    public int getPackageFaultManagerStatus(CANManager.WarningStatus paramWarningStatus) {//电池包故障状态管理信息
        return canManager.getPackageFaultManagerStatus(paramWarningStatus);
    }

    public int getSOCValue() {//电池包SOC数值
        return VehicleUtils.getOdoSoc(100, canManager.getSOCValue());
    }

    public float getSensorTempValue(int paramInt) {
        if ((paramInt <= 8) && (paramInt >= 1)) {
            CANManager.Sensor1Temp1 localSensor1Temp1 = null;
            switch (paramInt) {
                case 1:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor1;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 2:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor2;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 3:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor3;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 4:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor4;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 5:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor5;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 6:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor6;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 7:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor7;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                case 8:
                    localSensor1Temp1 = CANManager.Sensor1Temp1.Sensor8;
                    return canManager.getSensor1TempValue(localSensor1Temp1);
                    default:
                        break;
            }
        }
        if ((paramInt >= 9) && (paramInt <= 12)) {
            CANManager.Sensor2Temp2 localSensor2Temp2 = null;
            switch (paramInt) {
                case 9:
                localSensor2Temp2 = CANManager.Sensor2Temp2.Sensor9;
                    return this.canManager.getSensor2TempValue(localSensor2Temp2);
                case 10:
                    localSensor2Temp2 = CANManager.Sensor2Temp2.Sensor10;
                    return this.canManager.getSensor2TempValue(localSensor2Temp2);
                case 11:
                    localSensor2Temp2 = CANManager.Sensor2Temp2.Sensor11;
                    return this.canManager.getSensor2TempValue(localSensor2Temp2);
                case 12:
                    localSensor2Temp2 = CANManager.Sensor2Temp2.Sensor12;
                    return this.canManager.getSensor2TempValue(localSensor2Temp2);
                default:
                    break;
            }
        }
        return -1.0F;
    }

    protected void init(CANManager paramCANManager) {
    }

    public boolean isWarning(BatteryWarningType paramBatteryWarningType) {//hlj
        boolean b = false;
        switch (paramBatteryWarningType) {

            case BATTERY_TEMP_TYPE:
                b = false;
                break;
            case BATTERY_VOLA_TYPE:
                b = false;
                break;
            case BATTERY_CURRENT_TYPE:
                b = false;
                break;

            case SOC_TYPE:
                if (getSOCValue() <= 30) {
                    b = true;
                }
                break;
            case CAN_RANGE_TYPE:
                if ((getCanRange() <= 30.0F)) {
                    b = true;
                }
                break;
              default:
                  break;
        }

        return b;
    }

    @Override
    public void notifyDataChange(IDataNotify paramIDataNotify) {
        if (paramIDataNotify == null) {
            iDataNotify = paramIDataNotify;
        }
        while (batteryObr == null) {
            batteryObr = new BatteryObr();
            canManager.attachBatteryInfoObserver(batteryObr);
        }

    }

    public void setCanRange(float paramFloat) {
        canRange = paramFloat;
    }

    public class BatteryObr extends BatteryInfoChangeObserver {
        public BatteryObr() {
        }

        public void onPackCurrentValueChange(float paramFloat)
                throws RemoteException {
            if (Battery.this.iDataNotify != null)
                Battery.this.iDataNotify.refreshLimit();
        }

        public void onPackVoltageValueChange(float paramFloat)
                throws RemoteException {
            if (Battery.this.iDataNotify != null)
                Battery.this.iDataNotify.refreshLimit();
        }

        public void onSensorTempValueChange(int paramInt1, int paramInt2)
                throws RemoteException {
            if (Battery.this.iDataNotify != null)
                Battery.this.iDataNotify.refreshLimit();
        }
    }
}
