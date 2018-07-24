package com.etrans.myd2.test.impl;

public abstract interface IMotorAlarm
{
  public abstract int getMotorWorkStatus();

  public abstract boolean isMotorControllerFaultWarning();

  public abstract boolean isMotorControllerTempHighWarning();

  public abstract boolean isMotorTempHighWarning();
}
