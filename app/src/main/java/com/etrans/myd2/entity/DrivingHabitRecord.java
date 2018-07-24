package com.etrans.myd2.entity;

public class DrivingHabitRecord
{
  private int accSpeedDownCount;
  private int accSpeedUpCount;
  private int carefreeDriveCount;
  private long carefreeDriveDuration;
  private float dayBestKiloCo;
  public int habitReportScore;
  public float historyBestKiloCo;
  private int snailDriveCount;
  private long snailDriveDuration;

  public int getAccSpeedDownCount()
  {
    return this.accSpeedDownCount;
  }

  public int getAccSpeedUpCount()
  {
    return this.accSpeedUpCount;
  }

  public int getCarefreeDriveCount()
  {
    return this.carefreeDriveCount;
  }

  public long getCarefreeDriveDuration()
  {
    return this.carefreeDriveDuration;
  }

  public float getDayBestKiloCo()
  {
    return this.dayBestKiloCo;
  }

  public int getHabitReportScore()
  {
    return this.habitReportScore;
  }

  public float getHistoryBestKiloCo()
  {
    return this.historyBestKiloCo;
  }

  public int getSnailDriveCount()
  {
    return this.snailDriveCount;
  }

  public long getSnailDriveDuration()
  {
    return this.snailDriveDuration;
  }

  public void setAccSpeedDownCount(int paramInt)
  {
    this.accSpeedDownCount = paramInt;
  }

  public void setAccSpeedUpCount(int paramInt)
  {
    this.accSpeedUpCount = paramInt;
  }

  public void setCarefreeDriveCount(int paramInt)
  {
    this.carefreeDriveCount = paramInt;
  }

  public void setCarefreeDriveDuration(long paramLong)
  {
    this.carefreeDriveDuration = paramLong;
  }

  public void setDayBestKiloCo(float paramFloat)
  {
    this.dayBestKiloCo = paramFloat;
  }

  public void setHabitReportScore(int paramInt)
  {
    this.habitReportScore = paramInt;
  }

  public void setHistoryBestKiloCo(float paramFloat)
  {
    this.historyBestKiloCo = paramFloat;
  }

  public void setSnailDriveCount(int paramInt)
  {
    this.snailDriveCount = paramInt;
  }

  public void setSnailDriveDuration(long paramLong)
  {
    this.snailDriveDuration = paramLong;
  }
}
