package com.etrans.myd2.biz;

import android.content.Context;

import com.etrans.myd2.dao.VehicleDiagnosisDao;
import com.etrans.myd2.dao.idata.IDrivingHabitReport;
import com.etrans.myd2.entity.DrivingHabitRecord;


public class VehicleDiagnosisBiz extends BaseBiz
{
  private VehicleDiagnosisDao vDiagnosisDao;

  public VehicleDiagnosisBiz(Context paramContext)
  {
    vDiagnosisDao = new VehicleDiagnosisDao(paramContext);
  }

  //获取驾驶习惯报告
  public void getDrivingHabitReport(final IDrivingHabitReport paramIDrivingHabitReport)
  {
    if (paramIDrivingHabitReport != null)
    dataEs.execute(new Runnable()
    {
      public void run()
      {
        DrivingHabitRecord localDrivingHabitRecord = vDiagnosisDao.getDrivingHabitReport();
        paramIDrivingHabitReport.getDrivingHabitReport(localDrivingHabitRecord);
      }
    });
  }
}