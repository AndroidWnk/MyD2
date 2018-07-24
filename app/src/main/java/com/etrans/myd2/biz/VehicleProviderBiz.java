package com.etrans.myd2.biz;

import android.content.Context;

import com.etrans.myd2.dao.VehicleProviderDao;
import com.etrans.myd2.entity.BaseDayVehicleState;
import com.etrans.myd2.entity.CurDayVehicleState;

import java.util.ArrayList;

public class VehicleProviderBiz extends BaseBiz
{
  private IVehicleStateDater dater;
  private VehicleProviderDao vProviderDao;

  public VehicleProviderBiz(Context paramContext)
  {
    vProviderDao = new VehicleProviderDao(paramContext);
  }

  public void deleteBase(final String paramString)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        vProviderDao.deleteBase(paramString);
      }
    });
  }

  public void deleteSta(final String paramString)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        vProviderDao.deleteSta(paramString);
      }
    });
  }

  public void insertBase(final BaseDayVehicleState paramBaseDayVehicleState)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        vProviderDao.insertBase(paramBaseDayVehicleState);
      }
    });
  }

  public void insertSta(final CurDayVehicleState paramCurDayVehicleState)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        vProviderDao.insertSta(paramCurDayVehicleState);
      }
    });
  }

  public void onDestroy()
  {
    vProviderDao.destroy();
    vProviderDao = null;
    dater = null;
  }

  public void queryBase(final String paramString)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        ArrayList localArrayList = vProviderDao.queryBase(paramString);
        if (dater != null)
          dater.getBaseList(localArrayList);
      }
    });
  }

  public void queryStaList(final int paramInt)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        ArrayList localArrayList = vProviderDao.queryStaList(paramInt);
        if (dater != null)
          dater.getStaList(localArrayList);
      }
    });
  }

  public void refreshBase(final String paramString)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        BaseDayVehicleState localBaseDayVehicleState = vProviderDao.queryDayBase(paramString, true);
        if (dater != null)
          dater.getBase(localBaseDayVehicleState);
      }
    });
  }

  public void refreshSta(final String paramString)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        CurDayVehicleState localCurDayVehicleState = vProviderDao.querySta(paramString);
        if (dater != null)
          dater.getCurSta(localCurDayVehicleState);
      }
    });
  }

  public void resumeListener(IVehicleStateDater paramIVehicleStateDater)
  {
    if (this.dater == null)
      this.dater = paramIVehicleStateDater;
  }

  public void stopListener()
  {
    this.dater = null;
  }

  public void updateSta(final String paramString, final CurDayVehicleState paramCurDayVehicleState)
  {
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        vProviderDao.updateSta(paramString, paramCurDayVehicleState);
      }
    });
  }

  public static abstract interface IVehicleStateDater
  {
    public abstract void getBase(BaseDayVehicleState paramBaseDayVehicleState);

    public abstract void getBaseList(ArrayList<BaseDayVehicleState> paramArrayList);

    public abstract void getCurSta(CurDayVehicleState paramCurDayVehicleState);

    public abstract void getStaList(ArrayList<CurDayVehicleState> paramArrayList);
  }
}