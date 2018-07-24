package com.etrans.myd2.util;

import android.os.RemoteException;
import android.util.Log;

import cn.com.etrans.etsdk.can.VehicleInfoChangeObserver;


/**
 * @author YC_Li
 */
public class VIChangeObserver extends VehicleInfoChangeObserver {

    private static final String TAG = "VIChangeObserver";

    @Override
    public void onBrakesStatusChange(int brakesStatus)
            throws RemoteException {
        // TODO Auto-generated method stub
        super.onBrakesStatusChange(brakesStatus);
    }

    @Override
    public void onGearStatusChange(int gearStatus) throws RemoteException {//车辆档位变化
        super.onGearStatusChange(gearStatus);
        //DebugIO.RunDebugOut(TAG + "onGearStatusChange",gearStatus);
//        Gear = gearStatus;
//        tx_Gear.setText(String.valueOf(gearStatus));
    }

    @Override
    public void onKeyOnStatusFlagChange(int keyOnStatus)
            throws RemoteException {
        // TODO Auto-generated method stub
        super.onKeyOnStatusFlagChange(keyOnStatus);
    }

    @Override
    public void onKeyPositionStatusChange(int keyStatus)
            throws RemoteException {
        super.onKeyPositionStatusChange(keyStatus);
        //DebugIO.RunDebugOut(TAG + "onKeyPositionStatusChange",keyStatus);
//        key = keyStatus;
//        tx_key.setText(String.valueOf(keyStatus));
    }

    @Override
    public void onMotorControllerFaultWarningChange(//电机控制器故障报警
            int motorControllerFaultWarning) throws RemoteException {
        super.onMotorControllerFaultWarningChange(motorControllerFaultWarning);
        //DebugIO.RunDebugOut(TAG + "onMotorControllerFaultWarningChange",motorControllerFaultWarning);
//        MotorControllerFaul = motorControllerFaultWarning;
//        tx_MotorControllerFaul.setText(String.valueOf(motorControllerFaultWarning));
    }

    @Override
    public void onMotorControllerTempHighWarningChange(//电机控制器高温报警
            int motorControllerTempHighWarning) throws RemoteException {
        super.onMotorControllerTempHighWarningChange(motorControllerTempHighWarning);
        //DebugIO.RunDebugOut(TAG + "onMotorControllerTempHighWarningChange",motorControllerTempHighWarning);
//        MotorControllerTempHigh = motorControllerTempHighWarning;
//        tx_MotorControllerTempHigh.setText(String.valueOf(motorControllerTempHighWarning));
    }

    @Override
    public void onMotorDCVoltageValueChange(float motorDCVoltage)//电机直流母线电压信息
            throws RemoteException {
        super.onMotorDCVoltageValueChange(motorDCVoltage);
        //DebugIO.RunDebugOut(TAG + "onMotorDCVoltageValueChange",motorDCVoltage);
//        MotorDCVoltage = motorDCVoltage;
//        tx_MotorDCVoltage.setText(String.valueOf(motorDCVoltage));
    }

    @Override
    public void onMotorTempHighWarningChange(int motorTempHighWarning)//电机高温警告
            throws RemoteException {
        super.onMotorTempHighWarningChange(motorTempHighWarning);
        //DebugIO.RunDebugOut(TAG + "onMotorTempHighWarningChange",motorTempHighWarning);
//        MotorTempHigh = motorTempHighWarning;
//        tx_MotorTempHigh.setText(String.valueOf(motorTempHighWarning));
    }

    @Override
    public void onMotorTempValueChange(float motorTemp)//电机温度信息
            throws RemoteException {
        super.onMotorTempValueChange(motorTemp);
        //DebugIO.RunDebugOut(TAG + "onMotorTempValueChange",motorTemp);
//        MotorTemp = motorTemp;
//        tx_MotorTemp.setText(String.valueOf(motorTemp));
    }

    @Override
    public void onMotorWarningStatusChange(int warningIndex,
                                           int warningStatus) throws RemoteException {//电机报警状态改变
        // TODO Auto-generated method stub
        super.onMotorWarningStatusChange(warningIndex, warningStatus);
    }

    @Override
    public void onMotorWorkStatusChange(int motorWorkStatus)//电机工作状态改变
            throws RemoteException {
        super.onMotorWorkStatusChange(motorWorkStatus);
        //DebugIO.RunDebugOut(TAG + "onMotorWorkStatusChange",motorWorkStatus);
//        MotorWork = motorWorkStatus;
//        tx_MotorWork.setText(String.valueOf(motorWorkStatus));
    }

    @Override
    public void onReadySignalStatusChange(int readySignal)
            throws RemoteException {
        super.onReadySignalStatusChange(readySignal);
        //DebugIO.RunDebugOut(TAG + "onReadySignalStatusChange",readySignal);
//        Ready = readySignal;
//        tx_Ready.setText(String.valueOf(readySignal));
    }

    @Override
    public void onVehicleSpeedChange(float vehicleSpeed)//车辆速度改变
            throws RemoteException {
        super.onVehicleSpeedChange(vehicleSpeed);
        Log.e(TAG, "vehicleSpeed==" + vehicleSpeed);
        //DebugIO.RunDebugOut(TAG + "onVehicleSpeedChange",vehicleSpeed);
//        chesu = vehicleSpeed;
//        tx_chesu.setText(String.valueOf(vehicleSpeed));
    }

    @Override
    public void onVehileOdoMeterValueChange(float vehileOdoMeter)////整车行驶里程信息
            throws RemoteException {
        super.onVehileOdoMeterValueChange(vehileOdoMeter);
        //DebugIO.RunDebugOut(TAG + "onVehileOdoMeterValueChange",vehileOdoMeter);
//        OdoMeter = vehileOdoMeter;
//        tx_OdoMeter.setText(String.valueOf(vehileOdoMeter));
    }

    @Override
    public void onRangeValueChange(float rangeValue) throws RemoteException {//续航里程
        super.onRangeValueChange(rangeValue);
//        RangeValue = rangeValue;
//        tx_NMile.setText(String.valueOf(rangeValue));
    }

    @Override
    public void onMaintainInfoChange(int mInfo) throws RemoteException {//保养信息
        super.onMaintainInfoChange(mInfo);
//        MaintainInfo = mInfo;
//        tx_MaintainInfo.setText(String.valueOf(mInfo));
    }
    @Override
    public void onRemainMaintainKiloChange(int rmKilo) throws RemoteException {//剩余保养里程
        super.onRemainMaintainKiloChange(rmKilo);
//        RemainMaintainKilo = rmKilo;
//        tx_RemainMaintainKilo.setText(String.valueOf(rmKilo));
    }

}