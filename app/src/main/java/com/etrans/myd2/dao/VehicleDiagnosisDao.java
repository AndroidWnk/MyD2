package com.etrans.myd2.dao;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.etrans.myd2.dao.idata.IDrivingHabitReport;
import com.etrans.myd2.entity.DrivingHabitRecord;
import com.etrans.myd2.entity.DrivingRecord;
import com.etrans.myd2.util.DateUtils;
import com.etrans.myd2.util.SystemTime;

public class VehicleDiagnosisDao {
	private VehicleProviderDao vProviderDao;
	private DateUtils du;
	private Context context;

	public VehicleDiagnosisDao(Context context) {
		this.context = context;
		du = DateUtils.getInstance();
		vProviderDao = new VehicleProviderDao(context);
	}
	/**
	 *
	 * 获取今日驾驶分析报告
	 * @return
     */

	public DrivingHabitRecord getDrivingHabitReport() {
		DrivingHabitRecord dhr = new DrivingHabitRecord();
		// 获取今天日期字符串
		String dayStr = du.getNativeSystemDay();
		// 查询数据库,准备加速过快次数
		ArrayList<DrivingRecord> speedUpdhr = vProviderDao
				.queryDrivingHabitRecord(dayStr, IDrivingHabitReport.SPEED_UP_DRIVING_TYPE + "");
		// 加速过猛次数
		int accSpeedUpCount = speedUpdhr == null ? 0 : speedUpdhr.size();
		dhr.setAccSpeedUpCount(accSpeedUpCount);
		// 查询数据库,准备减速过快次数
		ArrayList<DrivingRecord> speedDowndhr = vProviderDao
				.queryDrivingHabitRecord(dayStr, IDrivingHabitReport.SPEED_DOWN_DRIVING_TYPE + "");
		// 减速过猛次数
		int accSpeedDownCount = speedDowndhr == null ? 0 : speedDowndhr.size();
		dhr.setAccSpeedDownCount(accSpeedDownCount);
		int careFreeTempCount = 0;
		long careFreeTempDuration = 0;
		// 读取诊断临时时间
		int snailTempCount = 0;
		long snailTempDuration = 0;
		// 准备畅快驾驶的次数和累计时长
		ArrayList<DrivingRecord> careFreedhr = vProviderDao
				.queryDrivingHabitRecord(dayStr,IDrivingHabitReport.CAREFREE_DRIVING_TYPE + "");
		if (careFreedhr != null) {
			// 累加今日畅快驾驶时长
			careFreeTempCount = careFreedhr.size();
			for (DrivingRecord dr : careFreedhr) {
				careFreeTempDuration += dr.getDuration();
			}
		}
		// 准备蜗牛驾驶的次数和累计时长
		ArrayList<DrivingRecord> snaildhr = vProviderDao
				.queryDrivingHabitRecord(dayStr,IDrivingHabitReport.SNAIL_DRIVING_TYPE + "");
		if (snaildhr != null) {
			// 累加今日龟速驾驶时长
			snailTempCount = snaildhr.size();
			for (DrivingRecord dr : snaildhr) {
				snailTempDuration += dr.getDuration();
			}
		}
		// 添加临时值(因为数据库中记录的成段的行驶段,与用户点击诊断时存在时间差,通过临时记录时间补充时间差)
		if (context != null) {
			// 获取当前系统时间
			long curMillonsTime = SystemTime.currentTimeMillis();
			// 读取文件中记录的类型变化的时间点
			SharedPreferences spTemp = context.getSharedPreferences(
					DaoConst.DIA_INFO_FILE_NAME, Context.MODE_MULTI_PROCESS);
			long tempStartT=spTemp.getLong(DaoConst.TEMP_START_DURATION_PARAM, curMillonsTime);
			// 计算类型变化的时间点到用户点击诊断也就是现在的时间差
			long tempDuration = curMillonsTime - tempStartT;
			// 时间差是否大于24小时,认为当前时间异常,起始和结束时间来计算差值
			if(tempDuration>DaoConst.HOURS_24){
				tempDuration=SystemTime.getPreDefaultTime()-tempStartT;
			}
			// 判断时间差大于平稳驾驶统计最小时间
			if (tempDuration > DaoConst.STEAD_CONTINUE_TIME) {
				int tempDrivState = spTemp.getInt(
						DaoConst.TEMP_START_DRIV_PARAM, IDrivingHabitReport.NO_STA_TYPE);
				// 叠加计入通知次数
				switch (tempDrivState) {
					case IDrivingHabitReport.CAREFREE_DRIVING_TYPE:
						careFreeTempDuration += tempDuration;
						careFreeTempCount++;
						break;
					case IDrivingHabitReport.SNAIL_DRIVING_TYPE:
						snailTempDuration += tempDuration;
						snailTempCount++;
						break;
				}
				Log.i("dia","本次临时诊断参数为:"+tempDuration+"类型为:"+tempDrivState);
			}
		}
		// 回调
		dhr.setCarefreeDriveCount(careFreeTempCount);
		dhr.setCarefreeDriveDuration(careFreeTempDuration);
		dhr.setSnailDriveCount(snailTempCount);
		dhr.setSnailDriveDuration(snailTempDuration);

		// //准备当日最节能的百公里耗电
		//
		// iHabitReport.getDayBestEnergySaving(kilo_co);
		// //准备历史最节能的百公里耗电
		// iHabitReport.getHistoryBestEnergySaving(kilo_co);
		// 总得分
		// 驾驶分析分数核算,一共100分
		int tempCount = 100;
		// 加速过猛:共30分,出现一次扣1分
		if (accSpeedUpCount < 30) {// 正常扣分
			tempCount -= accSpeedUpCount;
		} else {
			tempCount -= 30;
		}
		// 减速过猛:共30分,出现一次扣1分
		if (accSpeedDownCount < 30) {// 正常扣分
			tempCount -= accSpeedDownCount;
		} else {
			tempCount -= 30;
		}
		// 龟速及畅快驾驶40分,行驶状态只分为龟速和畅快,按照其比例进行分数计算
		if (snailTempDuration != 0) {// 默认按照6分钟扣一分算
			int inScoreTemp=Math.round(snailTempDuration / (6 * 60 * 1000));
			if(inScoreTemp<40){
				tempCount -= inScoreTemp;
			}else{
				tempCount -= 40;
			}
		}
		// 返回分数
		dhr.setHabitReportScore(tempCount);
		return dhr;
	}

}
