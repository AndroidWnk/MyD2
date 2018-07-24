package com.etrans.myd2.dbhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VehicleStateDBHelper extends SQLiteOpenHelper
{
  private static String CAN_DB_NAME = "can.db";
  private static final int CAN_DB_VERSION = 4;
  public static final String TABLE_NAME_BASE = "car_state_base";
  public static final String TABLE_NAME_HABIT = "car_state_habit";
  public static final String TABLE_NAME_STA = "car_state_sta";

  @SuppressLint({"SimpleDateFormat"})
  public VehicleStateDBHelper(Context paramContext)
  {

    super(paramContext, CAN_DB_NAME, null, CAN_DB_VERSION);
  }

  private void addChargeCountsWord(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE car_state_sta ADD COLUMN charge_counts TEXT");
  }

  private void addRangCountsWord(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("ALTER TABLE car_state_base ADD COLUMN drive_range TEXT");
  }

  private void createBaseTable(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table if not exists "+ TABLE_NAME_BASE +"(_id integer primary key,day text,hours text,soc text,ch_state text,speed text,left_door text,right_door text,left_window text,right_window text,behind_door text,drive_range text,kilo text);");
  }

  private void createHabitTable(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table if not exists "+ TABLE_NAME_HABIT+" (_id integer primary key,day text,start_address text,end_address text,kilo text,start_time text,end_time text,duration text,average_speed text,kilo_consume text,Driving_type text);");
  }

  private void createStaTable(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table if not exists "+ TABLE_NAME_STA+" (_id integer primary key,day text UNIQUE,ch_soc text,ch_time text,co_soc text,speed_soc text,co_time text,dr_distance text,av_speed text,charge_counts text,ki_consume text);");
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    createStaTable(paramSQLiteDatabase);
    createBaseTable(paramSQLiteDatabase);
    createHabitTable(paramSQLiteDatabase);
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    Log.i("hlj","更新数据库版本");
    if ((paramInt1 < 2) && (paramInt2 > 1))
      createHabitTable(paramSQLiteDatabase);
    if ((paramInt1 < 3) && (paramInt2 > 2))
      addChargeCountsWord(paramSQLiteDatabase);
    if ((paramInt1 < 4) && (paramInt2 > 3))
      addRangCountsWord(paramSQLiteDatabase);
  }
}