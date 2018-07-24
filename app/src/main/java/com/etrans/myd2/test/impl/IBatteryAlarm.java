package com.etrans.myd2.test.impl;


import cn.com.etrans.etsdk.manager.CANManager;

public abstract interface IBatteryAlarm
{
  public abstract byte[] getCellWarning();

  public abstract int getPackageFaultManagerStatus(CANManager.WarningStatus paramWarningStatus);
}

