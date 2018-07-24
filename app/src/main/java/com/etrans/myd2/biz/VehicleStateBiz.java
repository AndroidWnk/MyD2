package com.etrans.myd2.biz;

import android.content.Context;

import com.etrans.myd2.dao.VehicleStateDao;


public class VehicleStateBiz extends BaseBiz
{
  private VehicleStateDao vStateDao;

  public VehicleStateBiz(Context paramContext)
  {
    vStateDao = new VehicleStateDao(paramContext);
  }

  public void curSta()
  {
    vStateDao.curSta();
  }

  public void stopSta()
  {
    vStateDao.stopSta();
  }
}