package com.etrans.myd2.test.impl;

public abstract interface IBatteryParam
{
  public abstract float getPackCurrentValue();

  public abstract float getPackVoltageValue();

  public abstract int getSOCValue();

  public abstract float getSensorTempValue(int paramInt);
}
