package com.etrans.myd2.dao.idata;


import com.etrans.myd2.entity.DrivingHabitRecord;

public abstract interface IDrivingHabitReport
{
  public static final int CAREFREE_DRIVING_TYPE = 3;//畅快驾驶
  public static final int NO_STA_TYPE = 0;//无
  public static final int SNAIL_DRIVING_TYPE = 4;//龟速驾驶
  public static final int SPEED_DOWN_DRIVING_TYPE = 2;//急减速
  public static final int SPEED_UP_DRIVING_TYPE = 1;//急加速

  public abstract void getDrivingHabitReport(DrivingHabitRecord paramDrivingHabitRecord);
}
