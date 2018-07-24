package com.etrans.myd2.dbhelper;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * can常量
 *
 */
public class CANContentConst
{
  public static final String AUTHORITY = "com.etrans.myd2.can";
  //车辆基础状态
  public static final class VehicleBasicStates
    implements BaseColumns
  {
    public static final String BEHIND_DOOR = "behind_door";
    public static final String CHARGE_STATE = "ch_state";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vehicle_state_base";
    public static final Uri CONTENT_URI = Uri.parse("content://com.etrans.myd2.can/vehicle_state_base");
    public static final String DAY = "day";
    public static final String DRIVE_RANGE = "drive_range";
    public static final String HOURS = "hours";
    public static final String KILO = "kilo";
    public static final String LEFT_DOOR = "left_door";
    public static final String LEFT_WINDOW = "left_window";
    public static final String RIGHT_DOOR = "right_door";
    public static final String RIGHT_WINDOW = "right_window";
    public static final String SOC = "soc";
    public static final String SPEED = "speed";
    public static final String URI_PATH = "vehicle_state_base";
  }

  //车辆驾驶记录
  public static final class VehicleDrivingRecord
    implements BaseColumns
  {
    public static final String AVERAGE_SPEED = "average_speed";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vehicle_driving_record";
    public static final Uri CONTENT_URI = Uri.parse("content://com.etrans.myd2.can/vehicle_driving_record");
    public static final String DAY = "day";
    public static final String DRIVING_TYPE = "Driving_type";
    public static final String DURATION = "duration";
    public static final String END_ADDRESS = "end_address";
    public static final String END_TIME = "end_time";
    public static final String KILO = "kilo";
    public static final String KILO_CONSUME = "kilo_consume";
    public static final String START_ADDRESS = "start_address";
    public static final String START_TIME = "start_time";
    public static final String URI_PATH = "vehicle_driving_record";
  }

  //车辆统计状态
  public static final class VehicleStatisticsStates
    implements BaseColumns
  {
    public static final String AVERAGE_SPEED = "av_speed";
    public static final String CHARGE_COUNTS = "charge_counts";
    public static final String CHARGE_SOC = "ch_soc";
    public static final String CHARGE_TIME = "ch_time";
    public static final String CONSUME_SOC = "co_soc";
    public static final String CONSUME_TIME = "co_time";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vehicle_state_sta";
    public static final Uri CONTENT_URI = Uri.parse("content://com.etrans.myd2.can/vehicle_state_sta");
    public static final String DAY = "day";
    public static final String DRIVE_DISTANCE = "dr_distance";
    public static final String KILO_CONSUME = "ki_consume";
    public static final String SPEED_SOC = "speed_soc";
    public static final String URI_PATH = "vehicle_state_sta";
  }
}
