package com.etrans.myd2.test;

import android.content.Context;
import android.os.RemoteException;
import com.etrans.myd2.test.impl.IMotorAlarm;
import com.etrans.myd2.test.impl.IMotorParam;
import cn.com.etrans.etsdk.can.VehicleInfoChangeObserver;
import cn.com.etrans.etsdk.manager.CANManager;


public class Motor extends CoreComponents
        implements IMotorParam, IMotorAlarm {
    private CoreComponents.IDataNotify iDataNotify;
    private VehicleObr vehicleObr;

    public Motor(Context paramContext) {
        super(paramContext);
    }


    public void detMotorListener() {
        if (vehicleObr != null)
            canManager.detachVehicleInfoObserver( vehicleObr);
        iDataNotify = null;
    }

    public float getMotorDCVoltageValue() {
        return canManager.getPackVoltageValue();
    }

    public float getMotorTempValue() {
        return canManager.getMotorTempValue();
    }

    public int getMotorWorkStatus() {
        return canManager.getStatus3(CANManager.Status3.MotorWorkStatus);
    }

    protected void init(CANManager paramCANManager) {
    }

    public boolean isMotorControllerFaultWarning() {
        return canManager.getStatus3(CANManager.Status3.MotorControllerFaultWarning) == 1;
    }

    public boolean isMotorControllerTempHighWarning() {
        return canManager.getStatus3(CANManager.Status3.MotorControllerTempHighWarning) == 1;
    }

    public boolean isMotorTempHighWarning() {
        return canManager.getStatus3(CANManager.Status3.MotorTempHighWarning) == 1;
    }

    public boolean isMotorWarning() {
        boolean bool1 = isMotorTempHighWarning();
        boolean bool2 = isMotorControllerTempHighWarning();
        boolean bool3 = isMotorControllerFaultWarning();
        return (bool1) || (bool2) || (bool3);
    }

    public boolean isWarning(MotorWarningType paramMotorWarningType) {
        boolean b = false;
        switch (paramMotorWarningType) {
            case MOTOR_VOLA_TYPE:
                b = false;
                break;
            case MOTOR_CURRENT_TYPE:
                b = false;
                break;
            case MOTOR_TEMP_TYPE:
                b = isMotorTempHighWarning();
                break;
            default:
                break;
        }

        return b;
    }

    public void notifyDataChange(CoreComponents.IDataNotify paramIDataNotify) {
        if (paramIDataNotify == null) {
            iDataNotify = paramIDataNotify;

        }
        if (vehicleObr == null) {
            vehicleObr = new VehicleObr();
            canManager.attachVehicleInfoObserver(vehicleObr);
        }//修改后的
       /* while (vehicleObr != null) ;//hlj 修改
        vehicleObr = new VehicleObr();
        canManager.attachVehicleInfoObserver(vehicleObr);*///原来的
    }

    public class VehicleObr extends VehicleInfoChangeObserver {
        public VehicleObr() {
        }

        public void onMotorTempValueChange(float paramFloat)
                throws RemoteException {
            if (iDataNotify != null)
                iDataNotify.refreshLimit();
        }
    }
}