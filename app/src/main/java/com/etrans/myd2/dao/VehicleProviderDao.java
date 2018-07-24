package com.etrans.myd2.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.etrans.myd2.dao.config.VehicleConfig_100;
import com.etrans.myd2.dbhelper.CANContentConst;
import com.etrans.myd2.entity.BaseDayVehicleState;
import com.etrans.myd2.entity.CurDayVehicleState;
import com.etrans.myd2.entity.DrivingRecord;
import com.etrans.myd2.dao.sta.StaEntity;
import com.etrans.myd2.util.DateUtils;
import com.etrans.myd2.util.SystemTime;

import java.util.ArrayList;


public class VehicleProviderDao
{
  private Context context;
  private ContentResolver cr;

  public VehicleProviderDao(Context paramContext)
  {
    context = paramContext;
    cr = paramContext.getContentResolver();
  }

  public void clearData(float paramFloat)
  {
    Log.i("hlj","执行数据库清理策略~");
    deleteOnlySaveCurday(paramFloat);
    deleteSta(VehicleConfig_100.STA_SAVE_DAYS);
  }

  public long deleteBase(int paramInt)
  {
    Cursor localCursor = this.cr.query(CANContentConst.VehicleBasicStates.CONTENT_URI, new String[] { "_id" }, null, null, "_id desc limit " + paramInt + ",1");
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getInt(0);
      ContentResolver localContentResolver = this.cr;
      Uri localUri = CANContentConst.VehicleBasicStates.CONTENT_URI;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = String.valueOf(i);//hlj
      return localContentResolver.delete(localUri, "_id<?", arrayOfString);
    }
    if ((localCursor != null) && (!localCursor.isClosed()))
      localCursor.close();
    return -1L;
  }

  public long deleteBase(String paramString)
  {
    return cr.delete(CANContentConst.VehicleBasicStates.CONTENT_URI, "day=?", new String[] { paramString });
  }

  public int deleteDrivingHabitRecord(String paramString1, String paramString2, String paramString3)
  {
    String[] arrayOfString = null;
    String str = null;
    if (paramString1 != null)
    {
      str = "day=?";
      arrayOfString = new String[] { paramString1 };
      if (paramString2 != null)
      {
        str = str + " and Driving_type=?";
        arrayOfString = new String[2];
        arrayOfString[0] = paramString1;
        arrayOfString[1] = paramString2;
      }
    }
    if (paramString3 != null)
    {
      str = "_id=?";
      arrayOfString = new String[] { paramString3 };
    }
    return cr.delete(CANContentConst.VehicleDrivingRecord.CONTENT_URI, str, arrayOfString);
  }

  public long deleteOnlySaveCurday(float paramFloat)
  {
    if (paramFloat <= 300.0F)
      return -1L;
    ContentResolver localContentResolver1 = cr;
    Uri localUri1 = CANContentConst.VehicleBasicStates.CONTENT_URI;
    String[] arrayOfString1 = DaoConst.baseProjection;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = String.valueOf((paramFloat - 300.0F));//hlj
    Cursor localCursor = localContentResolver1.query(localUri1, arrayOfString1, "kilo<?", arrayOfString2, "_id asc limit 1");
    if (localCursor.moveToNext())
    {
      int i = localCursor.getInt(0);
      ContentResolver localContentResolver2 = this.cr;
      Uri localUri2 = CANContentConst.VehicleBasicStates.CONTENT_URI;
      String[] arrayOfString3 = new String[1];
      arrayOfString3[0] = String.valueOf(i);//hlj
      return localContentResolver2.delete(localUri2, "_id<=?", arrayOfString3);
    }
    if ((localCursor != null) && (!localCursor.isClosed()))
      localCursor.close();
    return -1L;
  }

  public long deleteSta(int paramInt)
  {
    Cursor localCursor = cr.query(CANContentConst.VehicleStatisticsStates.CONTENT_URI, new String[] { "_id" }, null, null, "_id desc limit " + paramInt + ",1");
    if (localCursor.moveToFirst())
    {
      int i = localCursor.getInt(0);
      ContentResolver localContentResolver = cr;
      Uri localUri = CANContentConst.VehicleStatisticsStates.CONTENT_URI;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = String.valueOf(i);//hlj
      return localContentResolver.delete(localUri, "_id<?", arrayOfString);
    }
    if ((localCursor != null) && (!localCursor.isClosed()))
      localCursor.close();
    return -1L;
  }

  public long deleteSta(String paramString)
  {
    return cr.delete(CANContentConst.VehicleStatisticsStates.CONTENT_URI, "day=?", new String[] { paramString });
  }

  public void destroy()
  {
    cr = null;
    context = null;
  }

  public float getCurDayKilo()
  {
    String str = DateUtils.getInstance().getSystemDay();
    return context.getSharedPreferences("STAInfo", 4).getFloat(str, -1.0F);
  }

  public int[] getRecentChargeCounts(int paramInt)
  {
    SharedPreferences localSharedPreferences = context.getSharedPreferences("CHInfo", 4);
    DateUtils localDateUtils = DateUtils.getInstance();
    int[] arrayOfInt = new int[paramInt];
    for (int i = 1; ; i++)
    {
      if (i > paramInt)
        return arrayOfInt;
      String str = localDateUtils.getDayAgo(paramInt);
      arrayOfInt[(i - 1)] = localSharedPreferences.getInt(str, 0);
    }
  }

  public Uri insertBase(BaseDayVehicleState paramBaseDayVehicleState)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("day", paramBaseDayVehicleState.getDay());
    localContentValues.put("hours", paramBaseDayVehicleState.getHours());
    localContentValues.put("soc", paramBaseDayVehicleState.getSoc());
    localContentValues.put("ch_state", paramBaseDayVehicleState.getCh_state());
    localContentValues.put("speed", paramBaseDayVehicleState.getSpeed());
    localContentValues.put("kilo", paramBaseDayVehicleState.getKilo());
    localContentValues.put("left_door", paramBaseDayVehicleState.getCarLeftDoor());
    localContentValues.put("right_door", paramBaseDayVehicleState.getCarRightDoor());
    localContentValues.put("left_window", paramBaseDayVehicleState.getCarLeftWindow());
    localContentValues.put("right_window", paramBaseDayVehicleState.getCarRightWindow());
    localContentValues.put("behind_door", paramBaseDayVehicleState.getBehindDoor());
    localContentValues.put("drive_range", paramBaseDayVehicleState.getDriveRange());
    return cr.insert(CANContentConst.VehicleBasicStates.CONTENT_URI, localContentValues);
  }

  public Uri insertDrivingHabitRecord(DrivingRecord paramDrivingRecord)
  {
    if (paramDrivingRecord == null)
      return null;
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("day", paramDrivingRecord.getDay());
    localContentValues.put("start_address", paramDrivingRecord.getStart_address());
    localContentValues.put("end_address", paramDrivingRecord.getEnd_address());
    float kilo = paramDrivingRecord.getKilo();
    localContentValues.put("kilo", String.format("%.1f", kilo));
    localContentValues.put("start_time", paramDrivingRecord.getStart_time());
    localContentValues.put("end_time", paramDrivingRecord.getEnd_time());
    localContentValues.put("duration", paramDrivingRecord.getDuration());
    float speed = paramDrivingRecord.getAverage_speed();
    localContentValues.put("average_speed", String.format("%.1f", speed));
    float kiloConsume = paramDrivingRecord.getKilo_consume();
    localContentValues.put("kilo_consume", String.format("%.1f", kiloConsume));
    localContentValues.put("Driving_type", paramDrivingRecord.getDriving_type());
    return cr.insert(CANContentConst.VehicleDrivingRecord.CONTENT_URI, localContentValues);
  }

  public Uri insertSta(CurDayVehicleState paramCurDayVehicleState)
  {
    String str = paramCurDayVehicleState.getDay();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("day", str);
    localContentValues.put("ch_soc", paramCurDayVehicleState.getCh_soc());
    localContentValues.put("ch_time", paramCurDayVehicleState.getCh_time());
    localContentValues.put("co_soc", paramCurDayVehicleState.getCo_soc());
    localContentValues.put("speed_soc", paramCurDayVehicleState.getSpeed_soc());
    localContentValues.put("co_time", paramCurDayVehicleState.getSpeed_time());
    localContentValues.put("dr_distance", paramCurDayVehicleState.getDr_distance());
    localContentValues.put("av_speed", paramCurDayVehicleState.getAv_speed());
    localContentValues.put("charge_counts", paramCurDayVehicleState.getCharge_counts());
    localContentValues.put("ki_consume", paramCurDayVehicleState.getKi_consume());
    Cursor localCursor = cr.query(CANContentConst.VehicleStatisticsStates.CONTENT_URI, new String[] { "day" }, "day=?", new String[] { str }, null);
    if (!localCursor.moveToNext());
    for (Uri localUri = cr.insert(CANContentConst.VehicleStatisticsStates.CONTENT_URI, localContentValues); ; localUri = null)
    {
      if ((localCursor != null) && (!localCursor.isClosed()))
        updateSta(paramCurDayVehicleState.getDay(), paramCurDayVehicleState);
        localCursor.close();
      return localUri;

    }
  }

  public ArrayList<BaseDayVehicleState> queryBase(String paramString)
  {
    Cursor localCursor = cr.query(CANContentConst.VehicleBasicStates.CONTENT_URI, DaoConst.baseProjection, "day=?", new String[] { paramString }, null);
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      if (!localCursor.moveToNext())
      {
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        if (!localArrayList.isEmpty())
          break;
        return null;
      }
      BaseDayVehicleState localBaseDayVehicleState = new BaseDayVehicleState();
      localBaseDayVehicleState.setDay(localCursor.getString(localCursor.getColumnIndex("day")));
      localBaseDayVehicleState.setHours(localCursor.getString(localCursor.getColumnIndex("hours")));
      localBaseDayVehicleState.setSoc(localCursor.getString(localCursor.getColumnIndex("soc")));
      localBaseDayVehicleState.setCh_state(localCursor.getString(localCursor.getColumnIndex("ch_state")));
      localBaseDayVehicleState.setSpeed(localCursor.getString(localCursor.getColumnIndex("speed")));
      localBaseDayVehicleState.setKilo(localCursor.getString(localCursor.getColumnIndex("kilo")));
      localBaseDayVehicleState.setCarLeftDoor(localCursor.getString(localCursor.getColumnIndex("left_door")));
      localBaseDayVehicleState.setCarRightDoor(localCursor.getString(localCursor.getColumnIndex("right_door")));
      localBaseDayVehicleState.setCarLeftWindow(localCursor.getString(localCursor.getColumnIndex("left_window")));
      localBaseDayVehicleState.setCarRightWindow(localCursor.getString(localCursor.getColumnIndex("right_window")));
      localBaseDayVehicleState.setBehindDoor(localCursor.getString(localCursor.getColumnIndex("behind_door")));
      localBaseDayVehicleState.setDriveRange(localCursor.getString(localCursor.getColumnIndex("drive_range")));
      localArrayList.add(localBaseDayVehicleState);
    }
    return localArrayList;
  }

  public BaseDayVehicleState queryDayBase(String paramString, boolean paramBoolean)
  {
    BaseDayVehicleState localBaseDayVehicleState = null;
    ContentResolver localContentResolver = cr;
    Uri localUri = CANContentConst.VehicleBasicStates.CONTENT_URI;
    String[] arrayOfString1 = DaoConst.baseProjection;
    String[] arrayOfString2 = { paramString };
    StringBuilder localStringBuilder = new StringBuilder("_id ");
    String str;
    Cursor localCursor = null;
    if (paramBoolean)
    {
      str = "desc";
      localCursor = localContentResolver.query(localUri, arrayOfString1, "day=?", arrayOfString2, str + " limit 1");
    }
    while (true)
    {
      if (!localCursor.moveToNext())
      {
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        return localBaseDayVehicleState;

      }
      localBaseDayVehicleState = new BaseDayVehicleState();
      localBaseDayVehicleState.setDay(localCursor.getString(localCursor.getColumnIndex("day")));
      localBaseDayVehicleState.setHours(localCursor.getString(localCursor.getColumnIndex("hours")));
      localBaseDayVehicleState.setSoc(localCursor.getString(localCursor.getColumnIndex("soc")));
      localBaseDayVehicleState.setCh_state(localCursor.getString(localCursor.getColumnIndex("ch_state")));
      localBaseDayVehicleState.setSpeed(localCursor.getString(localCursor.getColumnIndex("speed")));
      localBaseDayVehicleState.setKilo(localCursor.getString(localCursor.getColumnIndex("kilo")));
      localBaseDayVehicleState.setCarLeftDoor(localCursor.getString(localCursor.getColumnIndex("left_door")));
      localBaseDayVehicleState.setCarRightDoor(localCursor.getString(localCursor.getColumnIndex("right_door")));
      localBaseDayVehicleState.setCarLeftWindow(localCursor.getString(localCursor.getColumnIndex("left_window")));
      localBaseDayVehicleState.setCarRightWindow(localCursor.getString(localCursor.getColumnIndex("right_window")));
      localBaseDayVehicleState.setBehindDoor(localCursor.getString(localCursor.getColumnIndex("behind_door")));
      localBaseDayVehicleState.setDriveRange(localCursor.getString(localCursor.getColumnIndex("drive_range")));
    }
  }

  public BaseDayVehicleState queryDayBase(String paramString, String[] paramArrayOfString, boolean paramBoolean)
  {
    BaseDayVehicleState localBaseDayVehicleState = null;
    ContentResolver localContentResolver = cr;
    Uri localUri = CANContentConst.VehicleBasicStates.CONTENT_URI;
    String[] arrayOfString = DaoConst.baseProjection;
    StringBuilder localStringBuilder = new StringBuilder("_id ");
    String str;
    Cursor localCursor = null;
    if (paramBoolean)
    {
      str = "desc";
      localCursor = localContentResolver.query(localUri, arrayOfString, paramString, paramArrayOfString, str + " limit 1");
    }
    while (true)
    {
      if (!localCursor.moveToNext())
      {
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        return localBaseDayVehicleState;
      }
      localBaseDayVehicleState = new BaseDayVehicleState();
      localBaseDayVehicleState.setDay(localCursor.getString(localCursor.getColumnIndex("day")));
      localBaseDayVehicleState.setHours(localCursor.getString(localCursor.getColumnIndex("hours")));
      localBaseDayVehicleState.setSoc(localCursor.getString(localCursor.getColumnIndex("soc")));
      localBaseDayVehicleState.setCh_state(localCursor.getString(localCursor.getColumnIndex("ch_state")));
      localBaseDayVehicleState.setSpeed(localCursor.getString(localCursor.getColumnIndex("speed")));
      localBaseDayVehicleState.setKilo(localCursor.getString(localCursor.getColumnIndex("kilo")));
      localBaseDayVehicleState.setCarLeftDoor(localCursor.getString(localCursor.getColumnIndex("left_door")));
      localBaseDayVehicleState.setCarRightDoor(localCursor.getString(localCursor.getColumnIndex("right_door")));
      localBaseDayVehicleState.setCarLeftWindow(localCursor.getString(localCursor.getColumnIndex("left_window")));
      localBaseDayVehicleState.setCarRightWindow(localCursor.getString(localCursor.getColumnIndex("right_window")));
      localBaseDayVehicleState.setBehindDoor(localCursor.getString(localCursor.getColumnIndex("behind_door")));
      localBaseDayVehicleState.setDriveRange(localCursor.getString(localCursor.getColumnIndex("drive_range")));
    }
  }
  public ArrayList<DrivingRecord> queryDrivingHabitRecord(String paramString1, String paramString2)
  {
    String str = null;
    String[] arrayOfString = null;
    if (paramString1 != null)
    {
      str = "day=?";
      arrayOfString = new String[] { paramString1 };
      if (paramString2 != null)
      {
        str = str + " and Driving_type=?";
        arrayOfString = new String[2];
        arrayOfString[0] = paramString1;
        arrayOfString[1] = paramString2;
      }
    }
    Cursor localCursor = cr.query(CANContentConst.VehicleDrivingRecord.CONTENT_URI, DaoConst.habitProjection, str, arrayOfString, null);
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      if (!localCursor.moveToNext())
      {
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        if (!localArrayList.isEmpty())
          break;
        return null;
      }
      DrivingRecord localDrivingRecord = new DrivingRecord();
      localDrivingRecord.setDay(localCursor.getString(localCursor.getColumnIndex("day")));
      localDrivingRecord.setStart_address(localCursor.getString(localCursor.getColumnIndex("start_address")));
      localDrivingRecord.setEnd_address(localCursor.getString(localCursor.getColumnIndex("end_address")));
      localDrivingRecord.setKilo(localCursor.getFloat(localCursor.getColumnIndex("kilo")));
      localDrivingRecord.setStart_time(localCursor.getLong(localCursor.getColumnIndex("start_time")));
      localDrivingRecord.setEnd_time(localCursor.getLong(localCursor.getColumnIndex("end_time")));
      localDrivingRecord.setDuration(localCursor.getLong(localCursor.getColumnIndex("duration")));
      localDrivingRecord.setAverage_speed(localCursor.getFloat(localCursor.getColumnIndex("average_speed")));
      localDrivingRecord.setKilo(localCursor.getFloat(localCursor.getColumnIndex("kilo_consume")));
      localDrivingRecord.setDriving_type(localCursor.getInt(localCursor.getColumnIndex("Driving_type")));
      localArrayList.add(localDrivingRecord);
    }
    return localArrayList;
  }
  public CurDayVehicleState querySta(String paramString)
  {
    Cursor localCursor = cr.query(CANContentConst.VehicleStatisticsStates.CONTENT_URI, null, "day=?", new String[] { paramString }, null);
    CurDayVehicleState localCurDayVehicleState = null;
    if (localCursor.moveToNext())
    {
      localCurDayVehicleState = new CurDayVehicleState();
      localCurDayVehicleState.setDay(localCursor.getString(localCursor.getColumnIndex("day")));
      localCurDayVehicleState.setCh_soc(localCursor.getString(localCursor.getColumnIndex("ch_soc")));
      localCurDayVehicleState.setCh_time(localCursor.getString(localCursor.getColumnIndex("ch_time")));
      localCurDayVehicleState.setCo_soc(localCursor.getString(localCursor.getColumnIndex("co_soc")));
      localCurDayVehicleState.setSpeed_soc(localCursor.getString(localCursor.getColumnIndex("speed_soc")));
      localCurDayVehicleState.setSpeed_time(localCursor.getString(localCursor.getColumnIndex("co_time")));
      localCurDayVehicleState.setDr_distance(localCursor.getString(localCursor.getColumnIndex("dr_distance")));
      localCurDayVehicleState.setAv_speed(localCursor.getString(localCursor.getColumnIndex("av_speed")));
      localCurDayVehicleState.setCharge_counts(localCursor.getString(localCursor.getColumnIndex("charge_counts")));
      localCurDayVehicleState.setKi_consume(localCursor.getString(localCursor.getColumnIndex("ki_consume")));
    }
    if ((localCursor != null) && (!localCursor.isClosed()))
      localCursor.close();
    return localCurDayVehicleState;
  }

  public ArrayList<CurDayVehicleState> queryStaList(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0;i< paramInt ; i++)
    {
      localArrayList.add(querySta(DateUtils.getInstance().getDayAgo(paramInt - i)));
    }
    return localArrayList;
  }

  public void renewDiaDrivingStart(long paramLong)
  {
    SharedPreferences localSharedPreferences = context.getSharedPreferences("DIAInfo", 4);
    int i = localSharedPreferences.getInt("tempStartDrivParam", 0);
    localSharedPreferences.edit().putInt("tempStartDrivParam", i).putLong("tempStartDurationParam", paramLong).apply();
  }

  public int saveChargeCounts(boolean paramBoolean)
  {
    String str = DateUtils.getInstance().getSystemDay();
    SharedPreferences localSharedPreferences = context.getSharedPreferences("CHInfo", 4);
    int staChargeCounts = localSharedPreferences.getInt("staChargeCounts", 0);
    int j = localSharedPreferences.getInt(str, 0);
    if (paramBoolean)
    {
      localSharedPreferences.edit().putInt("staChargeCounts", staChargeCounts + 1).putInt(str, j + 1).apply();
      Log.i("hlj","电池累计充电：" + (staChargeCounts + 1) + "次数");
      staChargeCounts++;
    }
    return staChargeCounts;
  }

  public void saveCurDayKilo(float paramFloat)
  {
    String str = DateUtils.getInstance().getSystemDay();
    context.getSharedPreferences("STAInfo", 4).edit().putFloat(str, paramFloat).apply();
  }

  public void saveDiaDrivingStart(StaEntity paramStaEntity)
  {
    SharedPreferences localSharedPreferences = context.getSharedPreferences("DIAInfo", 4);
    long l = SystemTime.currentTimeMillis();
    int i = 0;
    if (paramStaEntity != null)
    {
      l = paramStaEntity.getEndSpeedTime();
      i = paramStaEntity.getDriveState();
    }
    Log.i("dia", "记录开始日期和类型" + DateUtils.getInstance().getDayAndTimeStr(l) + "======" + i);
    localSharedPreferences.edit().putInt("tempStartDrivParam", i).putLong("tempStartDurationParam", l).commit();
  }

  public int updateDrivingHabitRecord(String paramString, DrivingRecord paramDrivingRecord)
  {
    if ((paramString == null) || (paramDrivingRecord == null))
      return -1;
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("day", paramDrivingRecord.getDay());
    localContentValues.put("start_address", paramDrivingRecord.getStart_address());
    localContentValues.put("end_address", paramDrivingRecord.getEnd_address());
    localContentValues.put("kilo", Float.valueOf(paramDrivingRecord.getKilo()));
    localContentValues.put("start_time", Long.valueOf(paramDrivingRecord.getStart_time()));
    localContentValues.put("end_time", Long.valueOf(paramDrivingRecord.getEnd_time()));
    localContentValues.put("duration", Long.valueOf(paramDrivingRecord.getDuration()));
    localContentValues.put("average_speed", Float.valueOf(paramDrivingRecord.getAverage_speed()));
    localContentValues.put("kilo_consume", Float.valueOf(paramDrivingRecord.getKilo()));
    localContentValues.put("Driving_type", Integer.valueOf(paramDrivingRecord.getDriving_type()));
    return cr.update(CANContentConst.VehicleDrivingRecord.CONTENT_URI, localContentValues, "_id=?", new String[] { paramString });
  }

  public int updateSta(String paramString, CurDayVehicleState paramCurDayVehicleState)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("day", paramCurDayVehicleState.getDay());
    localContentValues.put("ch_soc", paramCurDayVehicleState.getCh_soc());
    localContentValues.put("ch_time", paramCurDayVehicleState.getCh_time());
    localContentValues.put("co_soc", paramCurDayVehicleState.getCo_soc());
    localContentValues.put("speed_soc", paramCurDayVehicleState.getSpeed_soc());
    localContentValues.put("co_time", paramCurDayVehicleState.getSpeed_time());
    localContentValues.put("dr_distance", paramCurDayVehicleState.getDr_distance());
    localContentValues.put("av_speed", paramCurDayVehicleState.getAv_speed());
    localContentValues.put("charge_counts", paramCurDayVehicleState.getCharge_counts());
    localContentValues.put("ki_consume", paramCurDayVehicleState.getKi_consume());
    return cr.update(CANContentConst.VehicleStatisticsStates.CONTENT_URI, localContentValues, "day=?", new String[] { paramString });
  }
}
