package com.etrans.myd2.dao;

public class DaoConst
{
  public static final int ACC_CONTINUE_TIME = 1000;
  public static final int ACC_FREQUENCY_DURATION = 5000;
  public static final String ALL_CHARGE_COUNTS_PARAM = "staChargeCounts";
  public static final int BASE_SAVE_COUNTS = 1000;
  public static final String CHARGE_INFO_FILE_NAME = "CHInfo";
  public static final String DATA_PARAM = "data";
  public static final long DEFAULT_YEAR_2015_01_01 = Long
          .parseLong("1420041600000");
  public static final long DEFAULT_YEAR_2015_01_01_LOW = 0L;
  public static final String DIA_INFO_FILE_NAME = "DIAInfo";
  public static final long HOURS_24 = 86400000L;
  public static final String IS_LOGIN = "isLogin";
  public static final String LOGIN_SHAREPREFERENCE_FILE_NAME = "LoginInfo";
  public static final String MY_DISTANCE = "myDistance";
  public static final String OS_INFO_FILE_NAME = "OSInfo";
  public static final String PARAM_CUR_DAY_KILO = "curDayKilo";
  public static final String PARAM_PK_CODE = "pkCode";
  public static final String PARAM_VIN_CODE = "vinCode";
  public static final int SNAIL_POINTS_RATIO = 6;
  public static final int SPEED_CALLBACK = 500;
  public static final int STARTCAR_DIA_SPEED = 15;
  public static final String STA_INFO_FILE_NAME = "STAInfo";
  public static final int STA_SAVE_DAYS = 500;
  public static final int STEAD_CONTINUE_TIME = 10000;
  public static final String TEMP_START_DRIV_PARAM = "tempStartDrivParam";
  public static final String TEMP_START_DURATION_PARAM = "tempStartDurationParam";
  public static final String[] agencyProjection = { "dealerId", "dealerName", "salePhone", "linkName", "bizStatus", "address", "latitude", "longitude", "dealerType", "bizTime", "distance", "imgUrl" };
  public static final String[] baseProjection = new String[] { "day", "hours", "soc", "ch_state", "speed", "kilo", "left_door", "right_door", "left_window", "right_window", "behind_door", "drive_range" };
  public static final String[] habitProjection = new String[] { "day", "start_address", "end_address", "kilo", "start_time", "end_time", "duration", "average_speed", "kilo_consume", "Driving_type" };
  public static final String[] repairProjection = { "stationId", "stationName", "stationType", "address", "bizStatus", "bizTime", "salePhone", "longitude", "latitude", "distance", "imgUrl" };
  public static final String[] staProjection = { "day", "ch_soc", "ch_time", "co_soc", "speed_soc", "co_time", "dr_distance", "av_speed", "ki_consume", "charge_counts" };

}
