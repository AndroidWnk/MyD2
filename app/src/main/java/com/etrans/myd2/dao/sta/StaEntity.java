package com.etrans.myd2.dao.sta;

import android.util.Log;
import com.etrans.myd2.util.VehicleUtils;


public class StaEntity
{
  private int chCounts;//充电次数
  private float curDayFirstKilo;//今天初始里程
  private float curKilo;//当前里程
  private int curPStatus;//充放电状态
  private int curSoc;//当前Soc
  private float curSpeed;//当前速度
  private int driveState;//驾驶类型
  private String endAddressStr;//结束地址
  private int endChSoc;//结束充电时的Soc
  private long endChTime;//结束充电时间
  private int endCoSoc;//结束时总的Soc
  private long endCoTime;//结束时间
  private float endDriveKilo;//结束行驶里程
  private float endSpeed;//结束的速度
  private int endSpeedSoc;//结束行车耗电
  private long endSpeedTime;//结束行车时间
  private int gearCurStatus;//当前档位状态
  private int gearPreStatus;//
  private boolean isImmSta;
  private boolean isUnreasonable;//充电是否异常
  private int keyStatus;//钥匙状态
  private String startAddressStr;//开始时的地址
  private int startChSoc;//开始充电时的Soc
  private long startChTime;//开始充电时间
  private int startCoSoc;//开始的总Soc
  private long startCoTime;//开始的总时间
  private float startDriveKilo;//开始时的公里数
  private float startSpeed;//开始时的速度
  private int startSpeedSoc;//开始行车耗电
  private long startSpeedTime;//开始行车时的时间
//  private int i;

  //获取加速度
  public float getAccSpeed()
  {
    float f = (float)getSpeedTime();
    if (f <= 0.0F)
      return 0.0F;
    return 3600000.0F * (endSpeed - startSpeed) / f;
  }
  //获取平均速度
  public float getAverageSpeed()
  {
    float f1 = getDriveKilo();
    float f2 = (float)getSpeedTime();
    if ((f1 == 0.0F) || (f2 == 0.0F))
      return 0.0F;
    return 3600000.0F * f1 / f2;
  }
  //获取充电次数
  public int getChCounts()
  {
    return chCounts;
  }
  //获取充电电量
  public int getChSoc()
  {
    int chSoc = endChSoc - startChSoc;
    if ((chSoc > 100) || (chSoc < 0))
    {
      Log.i("hlj  getChSoc","本次充电电量异常!" + chSoc);
      isUnreasonable = true;
      chSoc = 0;
    }
    return chSoc;
  }
//获取充电时间
  public long getChTime()
  {
    long chTime = endChTime - startChTime;
    if ((chTime > 86400000L) || (chTime < 0L))
    {
      Log.i("hlj sta", "本次充电时间异常!" + chTime);
      this.isUnreasonable = true;
      chTime = 0L;
    }
    return chTime;
  }

  //获取总耗电电量
  public int getCoSoc()
  {
    int coSoc = startCoSoc - endCoSoc;
    if ((coSoc < 0) || (coSoc > 100))
    {
      Log.i("hlj getCoSoc","本次放电电量异常!" + coSoc);
      this.isUnreasonable = true;
      coSoc = 0;
    }
    return coSoc;
  }
//获取总耗电时间
  public long getCoTime()
  {
    long coTime = this.endCoTime - this.startCoTime;
    if ((coTime > 86400000L) || (coTime < 0L))
    {
      Log.i("hlj getCoTime","获取耗电时间异常!" + coTime);
      this.isUnreasonable = true;
      coTime = 0L;
    }
    return coTime;
  }

  ///获取当天的行驶里程
  public float getCurDayDriveKilo()
  {
    float curDayDriveKilo = curKilo - curDayFirstKilo;
    if ((curDayDriveKilo < 0.0F) || (curDayDriveKilo > 720.0F))
    {
      Log.i("hlj getCurDayDriveKilo","本次行驶里程异常!" + curDayDriveKilo);
      isUnreasonable = true;
      curDayDriveKilo = 0.0F;
    }
    return curDayDriveKilo;
  }

  public float getCurDayFirstKilo()
  {
    return this.curDayFirstKilo;
  }

  public float getCurKilo()
  {
    return this.curKilo;
  }

  public int getCurPStatus()
  {
    return this.curPStatus;
  }

  public int getCurSoc()
  {
    return this.curSoc;
  }

  public float getCurSpeed()
  {
    return this.curSpeed;
  }

  //行车里程
  public float getDriveKilo()
  {
    float f = endDriveKilo - startDriveKilo;
    if ((f < 0.0F) || (f > 720.0F))
    {
      Log.i("hlj  getDriveKilo","本次行驶里程异常!" + f);
      this.isUnreasonable = true;
      f = 0.0F;
    }
    return f;
  }

  public int getDriveState()
  {
    return this.driveState;
  }

  public String getEndAddressStr()
  {
    return this.endAddressStr;
  }

  public int getEndChSoc()
  {
    return this.endChSoc;
  }

  public long getEndChTime()
  {
    return this.endChTime;
  }

  public int getEndCoSoc()
  {
    return this.endCoSoc;
  }

  public float getEndSpeed()
  {
    return this.endSpeed;
  }

  public long getEndSpeedTime()
  {
    return this.endSpeedTime;
  }

  public int getGearCurStatus()
  {
    return this.gearCurStatus;
  }

  public int getGearPreStatus()
  {
    return this.gearPreStatus;
  }

  public int getKeyStatus()
  {
    return this.keyStatus;
  }


  //获取百公里电耗
  public float getKiloConsume()
  {
    int i = getSpeedSoc();
    float f = getDriveKilo();
    if ((i == 0) || (f == 0.0F))
      return 0.0F;
    return 15.33F * i / f;
  }
  //获取行车耗电量
  public int getSpeedSoc()
  {
    int i = startSpeedSoc - endSpeedSoc;
    if ((i < 0) || (i > 100))
    {
      Log.i("hlj getSpeedSoc","本次行车耗电电量异常!" + i);
      isUnreasonable = true;
      i = 0;
    }
    return i;
  }
  //获取行车时间
  public long getSpeedTime()
  {
    long l = endSpeedTime - startSpeedTime;
    if ((l > 86400000L) || (l < 0L))
    {
      Log.i("hlj getSpeedTime","本次行车时间异常!" + l);
      isUnreasonable = true;
      l = 0L;
    }
    return l;
  }

  public String getStartAddressStr()
  {
    return this.startAddressStr;
  }

  public int getStartChSoc()
  {
    return this.startChSoc;
  }

  public long getStartChTime()
  {
    return this.startChTime;
  }

  public int getStartCoSoc()
  {
    return this.startCoSoc;
  }

  public long getStartCoTime()
  {
    return this.startCoTime;
  }

  public float getStartDriveKilo()
  {
    return this.startDriveKilo;
  }

  public float getStartSpeed()
  {
    return this.startSpeed;
  }

  public int getStartSpeedSoc()
  {
    return this.startSpeedSoc;
  }

  public long getStartSpeedTime()
  {
    return this.startSpeedTime;
  }

  public boolean isBaseUnreasonable()//判断基础数据是否合理
  {
  if ((curSoc >= 0) && (curKilo >= 0.0F) && (curSpeed >= 0.0F))
  {
    if ((curSoc > 100) || (curKilo > 3000000.0F) || (curSpeed > 200.0F)) {
      Log.i("hlj ","当前值出现过大!\n当前电量curSoc:" + this.curSoc + "\n当前总里程curKilo:" + this.curKilo + "\n当前车速curSpeed:" + this.curSpeed);
      return true;
    }
    return false;
  }
  Log.i("hlj isBaseUnreasonable","当前值出现负数!\n当前电量curSoc:" + this.curSoc + "\n当前总里程curKilo:" + this.curKilo + "\n当前车速curSpeed:" + this.curSpeed);
  return true;
}

  public boolean isImmSta()
  {
    return this.isImmSta;
  }

  public boolean isStaUnreasonable()
  {
    int i = getChSoc();
    long l1 = getChTime();
    if ((l1 != 0L) && (Math.round((float)(i / l1)) > 180000))
    {
      Log.i("hlj isStaUnreasonable","充电速率异常!" + i + ":" + l1);
      return true;
    }
    int j = getCoSoc();
    long l2 = getCoTime();
    if ((l2 != 0L) && (Math.round((float)(j / l2)) > 18000))
    {
      Log.i("hlj ","放电速率异常!" + Math.round((float)(j / l2)));
      return true;
    }
    return isUnreasonable;
  }

  public void renewTime(long paramLong)
  {
    Log.i("hlj renewTime","统计数据时间统一手动修复...");
    if (getStartSpeedTime() > 0L)
    {
      setStartSpeedTime(paramLong - getSpeedTime());
      setEndSpeedTime(paramLong);
    }
    if (getStartCoTime() > 0L)
    {
      setStartCoTime(paramLong - getCoTime());
      setEndCoTime(paramLong);
    }
    if (getStartChTime() > 0L)
    {
      setStartChTime(paramLong - getChTime());
      setEndChTime(paramLong);
    }
  }

  public void reset()
  {
    this.startChTime = this.endChTime;
    this.startSpeedTime = this.endSpeedTime;
    this.startChSoc = this.endChSoc;
    this.startCoSoc = this.endCoSoc;
    this.startSpeedSoc = this.endSpeedSoc;
    this.startCoTime = this.endCoTime;
    this.startDriveKilo = this.endDriveKilo;
    resetReasonable();
    Log.i("hlj ","resetReasonable()  重置-------------");
    this.startAddressStr = this.endAddressStr;
    this.driveState = 0;
    this.startSpeed = this.endSpeed;
    this.chCounts = 0;
  }

  public void resetReasonable()
  {
    this.isUnreasonable = false;
  }

  public void setChCounts(int paramInt)
  {
    this.chCounts = paramInt;
  }

  public void setCurDayFirstKilo(float paramFloat)
  {
    this.curDayFirstKilo = paramFloat;
  }

  public void setCurKilo(float paramFloat)
  {
    if ((this.curKilo > 0.0F) && (Math.abs(paramFloat - this.curKilo) > 4.0F))
    {
      Log.i("hlj setCurKilo","当前Kilo出现异常！==原来：" + this.curKilo + "==当前：" + paramFloat);
      return;
    }
    this.curKilo = paramFloat;
  }
    //充放电状态
  public void setCurPStatus(int paramInt)
  {
    Log.i("canTest", "充放电状态:==" + paramInt);
    this.curPStatus = paramInt;
  }

  public void setCurSoc(int paramInt)
  {
    int i = VehicleUtils.getOdoSoc(100, paramInt);//hlj
    if ((this.curSoc > 0) && (Math.abs(i - this.curSoc) > 4))
    {
      Log.i("setCurSoc","当前Soc出现异常！==原来：" + this.curSoc + "==当前：" + i);
      return;
    }
    this.curSoc = paramInt;
  }

  public void setCurSpeed(float paramFloat)
  {
    this.curSpeed = paramFloat;
  }

  public void setDriveState(int paramInt)
  {
    this.driveState = paramInt;
  }

  public void setEndAddressStr(String paramString)
  {
    this.endAddressStr = paramString;
  }

  public void setEndChSoc(int paramInt)
  {
    this.endChSoc = paramInt;
  }

  public void setEndChTime(long paramLong)
  {
    this.endChTime = paramLong;
  }

  public void setEndCoSoc(int paramInt)
  {
    this.endCoSoc = paramInt;
  }

  public void setEndCoTime(long paramLong)
  {
    this.endCoTime = paramLong;
  }

  public void setEndDriveKilo(float paramFloat)
  {
    this.endDriveKilo = paramFloat;
  }

  public void setEndSpeed(float paramFloat)
  {
    this.endSpeed = paramFloat;
  }

  public void setEndSpeedSoc(int paramInt)
  {
    this.endSpeedSoc = paramInt;
  }

  public void setEndSpeedTime(long paramLong)
  {
    this.endSpeedTime = paramLong;
  }

  public void setGearCurStatus(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2))
    {
      this.gearCurStatus = 0;
      return;
    }
    this.gearCurStatus = paramInt;
  }

  public void setGearPreStatus(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2))
    {
      this.gearPreStatus = 0;
      return;
    }
    this.gearPreStatus = paramInt;
  }

  public void setImmSta(boolean paramBoolean)
  {
    this.isImmSta = paramBoolean;
  }

  public void setKeyStatus(int paramInt)
  {
    this.keyStatus = paramInt;
  }

  public void setStartAddressStr(String paramString)
  {
    this.startAddressStr = paramString;
  }

  public void setStartChSoc(int paramInt)
  {
    this.startChSoc = paramInt;
  }

  public void setStartChTime(long paramLong)
  {
    this.startChTime = paramLong;
  }

  public void setStartCoSoc(int paramInt)
  {
    Log.i("canTest", "记录开始耗电" + paramInt);
    this.startCoSoc = paramInt;
  }

  public void setStartCoTime(long paramLong)
  {
    this.startCoTime = paramLong;
  }

  public void setStartDriveKilo(float paramFloat)
  {
    this.startDriveKilo = paramFloat;
  }

  public void setStartSpeed(float paramFloat)
  {
    this.startSpeed = paramFloat;
  }

  public void setStartSpeedSoc(int paramInt)
  {
    this.startSpeedSoc = paramInt;
  }

  public void setStartSpeedTime(long paramLong)
  {
    this.startSpeedTime = paramLong;
  }
}