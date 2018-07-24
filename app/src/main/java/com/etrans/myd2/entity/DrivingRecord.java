package com.etrans.myd2.entity;

public class DrivingRecord
{
  private int Driving_type;
  private float average_speed;
  private String day;
  private long duration;
  private String end_address;
  private long end_time;
  private float kilo;
  private float kilo_consume;
  private String start_address;
  private long start_time;

  public float getAverage_speed()
  {
    return this.average_speed;
  }

  public String getDay()
  {
    return this.day;
  }

  public int getDriving_type()
  {
    return this.Driving_type;
  }

  public long getDuration()
  {
    return this.duration;
  }

  public String getEnd_address()
  {
    return this.end_address;
  }

  public long getEnd_time()
  {
    return this.end_time;
  }

  public float getKilo()
  {
    return this.kilo;
  }

  public float getKilo_consume()
  {
    return this.kilo_consume;
  }

  public String getStart_address()
  {
    return this.start_address;
  }

  public long getStart_time()
  {
    return this.start_time;
  }

  public void setAverage_speed(float paramFloat)
  {
    this.average_speed = paramFloat;
  }

  public void setDay(String paramString)
  {
    this.day = paramString;
  }

  public void setDriving_type(int paramInt)
  {
    this.Driving_type = paramInt;
  }

  public void setDuration(long paramLong)
  {
    this.duration = paramLong;
  }

  public void setEnd_address(String paramString)
  {
    this.end_address = paramString;
  }

  public void setEnd_time(long paramLong)
  {
    this.end_time = paramLong;
  }

  public void setKilo(float paramFloat)
  {
    this.kilo = paramFloat;
  }

  public void setKilo_consume(float paramFloat)
  {
    this.kilo_consume = paramFloat;
  }

  public void setStart_address(String paramString)
  {
    this.start_address = paramString;
  }

  public void setStart_time(long paramLong)
  {
    this.start_time = paramLong;
  }
}