package com.etrans.myd2.util;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.etrans.myd2.dao.DaoConst;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemTime
{
  private static Context contex;
  private static int counter;
  private static Handler handler = new Handler()
  {

    public void handleMessage(Message paramMessage)
    {
      SystemTime.isChecked = SystemTime.isGpsChecked(SystemTime.contex);
      Log.i("hlj","GPS校对时间：" + SystemTime.isChecked);
      if (!SystemTime.isChecked) {
        SystemTime.check(SystemTime.contex);
        Log.i("hlj", "!SystemTime.isChecked");
      }
    }
  };


  private static volatile boolean isChecked;
  private static long lastCurT = 1527143603133L;//hlj测试使用的固定时间
  private static volatile long preDefaultTime = DaoConst.DEFAULT_YEAR_2015_01_01;
  private static Timer t;
  private static TimerTask tTask;
  public static final ExecutorService esBug = Executors.newSingleThreadExecutor();

  public static void check(Context paramContext)
  {
    esBug.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(2000L);
          SystemTime.isChecked =  System.currentTimeMillis() > 1527143603133L ? true : false; /*SystemTime.isNetChecked();*///hlj for test

          Log.i("hlj" , "NET校对时间：===="+SystemTime.isChecked);
          if (!SystemTime.isChecked)
          {
            Message localMessage = new Message();
            localMessage.what = 0;
            SystemTime.handler.sendMessageDelayed(localMessage, 3000L);
          }
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
            localInterruptedException.printStackTrace();
        }
      }
    });
  }//hlj

  public static long currentTimeMillis()
  {
    long l1;
      if (isChecked)
      {
        long l2 = System.currentTimeMillis();
        l1 = l2;
        return l1;
      }else {
        l1 = preDefaultTime;
        return l1;
      }

  }

  public static boolean dateIsFault(long paramLong)
  {
   if (paramLong > lastCurT) {
      Log.i("hlj dateIsFault ","当前时间大于默认的时间认为已经更新");
      return false;

    }
    return true;

  }//hlj

  public static long getPreDefaultTime()
  {
    return preDefaultTime;
  }

  public static void init(Context paramContext)
  {
    contex = paramContext;
    if (!isChecked)
      startTimer(1);
    if (!isChecked)
      handler.sendEmptyMessage(0);
  }

  public static boolean isChecked()
  {
    return isChecked;
  }

  private static boolean isChecked(long paramLong)
  {
    long l = paramLong - System.currentTimeMillis();
    if (l < 0L)
      l = -l;
    return l < 30000L;
  }

  public static boolean isGpsChecked(Context paramContext)
  {
    long l = 0;
    if (!isOPen(paramContext))
      openGPS(paramContext);
    Location localLocation;
    {
      LocationManager localLocationManager = (LocationManager)paramContext.getSystemService("location");
      Criteria localCriteria = new Criteria();
      localCriteria.setAccuracy(1);
      localCriteria.setAltitudeRequired(false);
      localCriteria.setBearingRequired(false);
      localCriteria.setCostAllowed(true);
      localCriteria.setPowerRequirement(1);
      localLocation = localLocationManager.getLastKnownLocation(localLocationManager.getBestProvider(localCriteria, true));
    }
    if (localLocation != null) {
      l = localLocation.getTime();
      Log.i("hlj", "本次获取到GPS时间：" + l);
      Log.i("hlj", "当前系统时间为：" + System.currentTimeMillis());
      return isChecked(l);
    }
    return false;
  }

  public static boolean isNetChecked()
  {
    long l = getTimeFromURL("http://www.baidu.com");
//    BugLoger.ii("本次获取到NET时间：" + l);
//    BugLoger.ii("当前系统时间为：" + System.currentTimeMillis());
    return isChecked(l);
  }

  public static long getTimeFromURL(String paramString)
  {
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
      localHttpURLConnection.setConnectTimeout(15000);
      localHttpURLConnection.setRequestProperty("Charset", "UTF-8");
      int response = localHttpURLConnection.getResponseCode();
      Log.i("hlj","response == "+response);
      if (response == 200)
      {
        long l = localHttpURLConnection.getDate();
        return l;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();

    }
    catch (IOException localIOException)
    {
        localIOException.printStackTrace();
    }
    return 0L;
  }

  public static void getBaiduTime () {
    URL url = null;//取得资源对象
    try {
      url = new URL("http://www.baidu.com");
      URLConnection uc = url.openConnection();//生成连接对象
      uc.connect(); //发出连接
      long ld = uc.getDate(); //取得网站日期时间
      Log.i("hlj","------- ld =="+ld);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  //网络状态
  public static boolean isNetState(Context mContext) {
    boolean netSataus = false;
    contex = mContext;
    ConnectivityManager connectMgr = (ConnectivityManager)mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectMgr.getActiveNetworkInfo() != null) {
      netSataus = connectMgr.getActiveNetworkInfo().isAvailable();// 网络状态
    }
    return netSataus;
  }


  //判断网络或者GPS其中之一是否打开
  public static final boolean isOPen(Context paramContext)
  {
    LocationManager localLocationManager = (LocationManager)paramContext.getSystemService("location");
    boolean bool1 = localLocationManager.isProviderEnabled("gps");
    boolean bool2 = localLocationManager.isProviderEnabled("network");
    return (bool1) || (bool2);
  }

  public static boolean isSystemFaultTime()
  {
    return System.currentTimeMillis() < DaoConst.DEFAULT_YEAR_2015_01_01;
  }

  public static final void openGPS(Context paramContext)
  {
    Intent gpsIntent = new Intent();
    gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
    gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
    gpsIntent.setData(Uri.parse("custom:3"));
    try  {
      PendingIntent.getBroadcast(paramContext, 0, gpsIntent, 0).send();
    } catch (CanceledException e)    {
      e.printStackTrace();
      Log.i("openGps","  exception == "+e.toString());
    }
  }

  public static void startTimer(int paramInt)
  {
    if (tTask != null)
      tTask.cancel();
    if (t != null)
      t.cancel();
    if (paramInt == -1)
    {
      t = null;
      counter = 0;
      tTask = null;
      return;
    }
    Timer localTimer = new Timer(true);
     TimerTask local3 = new TimerTask() {
    public void run() {
//      SystemTime.counter = 1 + SystemTime.counter;
//      SystemTime.preDefaultTime = DaoConst.DEFAULT_YEAR_2015_01_01_LOW + 1000 * SystemTime.counter;//hlj test
    }
  };
    tTask = local3;
    localTimer.schedule(local3, 0L, paramInt * 1000);
  }

  public static void stopTimer()
  {
    if (counter > 0)
      startTimer(-1);
  }
}