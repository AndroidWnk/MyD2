/*
package com.etrans.myd2.dao.can.abs;

import android.content.Context;
import android.util.Log;


import com.etrans.myd2.util.SystemTime;

import cn.com.etrans.etsdk.config.EtSDK;
import cn.com.etrans.etsdk.manager.CANManager;

public abstract class CoreComponents
{
  protected CANManager mCanManager;
  private Context context;

  public CoreComponents(Context paramContext)
  {

    context = paramContext;
    EtSDK.getInstance(paramContext).addListener(new EtSDK.SDKInitListener() {
      @Override
      public void onConnectSuccess() {
        Log.i("EtSDK","ready");
        mCanManager =EtSDK.getInstance(context).getCanManager();
      }
    });
    init(mCanManager);
  }

  protected abstract void init(CANManager paramCANManager);

  public abstract void notifyDataChange(IDataNotify paramIDataNotify);

  public static abstract class IDataNotify
  {
    private long preSpeedTime = SystemTime.currentTimeMillis();

    public abstract void refresh();

    public void refreshLimit()
    {
      long l = SystemTime.currentTimeMillis();
      if (l - this.preSpeedTime > 500L)
      {
        this.preSpeedTime = l;
        refresh();
      }
    }
  }
}*/
