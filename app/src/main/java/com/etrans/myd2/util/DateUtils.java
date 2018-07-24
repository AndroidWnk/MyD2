package com.etrans.myd2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
  private static final DateUtils du = new DateUtils();
  private String dayFormatStr;
  private SimpleDateFormat sdf;
  private String timeFormatStr;

  private DateUtils()
  {
    reset();
  }

  public static DateUtils getInstance()
  {
    return du;
  }

  public String getDayAgo(int paramInt)
  {
    sdf = new SimpleDateFormat(this.dayFormatStr);
    return sdf.format(new Date(SystemTime.currentTimeMillis() - 1000L * (60L * (60L * (24L * Long.valueOf(paramInt).longValue())))));
  }

  public String getDayAgo(long paramLong, int paramInt)
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr);
    return this.sdf.format(new Date(paramLong - 1000L * (60L * (60L * (24L * Long.valueOf(paramInt).longValue())))));
  }

  public String getDayAndTimeStr(long paramLong)
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr + " " + this.timeFormatStr);
    return this.sdf.format(new Date(paramLong));
  }

  public String getDayStr(long paramLong)
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr);
    return this.sdf.format(new Date(paramLong));
  }

  public String getDurationStr(long paramLong)
  {
    int i = (int)(paramLong / 3600000L);
    int j = (int)((paramLong - 1000 * (60 * (i * 60))) / 60000L);
    String str = (int)((paramLong - 1000 * (60 * (i * 60)) - 1000 * (j * 60)) / 1000L) + "秒";
    if (j > 0)
      str = j + "分" + str;
    if (i > 0)
      str = i + "时" + str;
    return str;
  }

  public String getDurationToMinStr(long paramLong)
  {
    int i = (int)(paramLong / 3600000L);
    String str = (int)((paramLong - 1000 * (60 * (i * 60))) / 60000L) + "分钟";
    if (i > 0)
      str = i + "时" + str;
    return str;
  }

  public int getHours(long paramLong)
  {
    return (int)((SystemTime.currentTimeMillis() - paramLong) / 3600000L);
  }

  public long getMillonFromDayAndTime(String paramString)
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr + " " + this.timeFormatStr);
    try
    {
      long l = this.sdf.parse(paramString).getTime();
      return l;
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    return 0L;
  }

  public String getNativeSystemDay()
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr);
    return this.sdf.format(new Date(System.currentTimeMillis()));
  }

  public String getNativeSystemDayAndTime()
  {
    this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return this.sdf.format(new Date(System.currentTimeMillis()));
  }

  public String getNativeSystemTime()
  {
    this.sdf = new SimpleDateFormat(this.timeFormatStr);
    return this.sdf.format(new Date(System.currentTimeMillis()));
  }

  public String getSystemDay()
  {
    sdf = new SimpleDateFormat(this.dayFormatStr);
    return sdf.format(new Date(SystemTime.currentTimeMillis()));
  }

  public String getSystemDayAndTime()
  {
    this.sdf = new SimpleDateFormat(this.dayFormatStr + " " + this.timeFormatStr);
    return this.sdf.format(new Date(SystemTime.currentTimeMillis()));
  }

  public String getSystemDayAndTime(String paramString)
  {
    this.sdf = new SimpleDateFormat(paramString);
    return this.sdf.format(new Date(SystemTime.currentTimeMillis()));
  }

  public String getSystemTime()
  {
    this.sdf = new SimpleDateFormat(this.timeFormatStr);
    return this.sdf.format(new Date(SystemTime.currentTimeMillis()));
  }

  public void reset()
  {
    this.dayFormatStr = "yyyy-MM-dd";
    this.timeFormatStr = "HH:mm:ss";
  }

  public void setFormat(String paramString1, String paramString2)
  {
    this.dayFormatStr = paramString1;
    this.timeFormatStr = paramString2;
  }
}
