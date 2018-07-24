package com.etrans.myd2.dao.sta;


import android.util.Log;

import com.etrans.myd2.dao.DaoConst;
import com.etrans.myd2.dao.VehicleProviderDao;
import com.etrans.myd2.dao.config.BaseVehicleConfig;
import com.etrans.myd2.dao.idata.IDrivingHabitReport;
import com.etrans.myd2.entity.DrivingRecord;
import com.etrans.myd2.util.DateUtils;
import com.etrans.myd2.util.SystemTime;

public class DiaEntity {
	/** 用于行车状况记录的统计 */
	private static final StaEntity staAccEntity = new StaEntity();
	/** 用于行车状况记录的统计 */
	private static final StaEntity staSteEntity = new StaEntity();
	private DateUtils du = DateUtils.getInstance();
	private static long preAccUpTime = 0;
	private static long preAccDownTime = 0;
	private final static BaseVehicleConfig vConfig = BaseVehicleConfig.instance();
	private boolean dataIsFault;

	/**
	 * 当车速变化发生回调时,在方法public void onVehicleSpeedChange(float vehicleSpeed) 中调用此方法
	 * 触发驾驶分析统计
	 */
	public void speedAnalysis(StaEntity staEntity, VehicleProviderDao vProviderDao) {
		// 获取当前车速
		float curSpeed = staEntity.getCurSpeed();
		// 获取当前总里程数
		float curKilo = staEntity.getCurKilo();
		// 获取当前剩余SOC
		int curSoc = staEntity.getCurSoc();
		// 初始化当前挡位状态
		staSteEntity.setGearCurStatus(staEntity.getGearCurStatus());
		// 获取当前系统时间
		long curTime = SystemTime.currentTimeMillis();
		// 检查当前时间是否正确(联网或者GPS)
		boolean dataIsFaultTemp = SystemTime.dateIsFault(curTime);
		// 如果本次系统时间已经是对的,上次时间是错的,将临时统计数据处理过来
		if (dataIsFault && !dataIsFaultTemp) {
			// 设置结束时间。为出厂最后时间
			staAccEntity.setEndSpeedTime(SystemTime.getPreDefaultTime());
			staSteEntity.setEndSpeedTime(SystemTime.getPreDefaultTime());
			// 更新正常时间
			staAccEntity.renewTime(curTime);
			staSteEntity.renewTime(curTime);
			if (staSteEntity.getSpeedTime() > 0)
				vProviderDao.renewDiaDrivingStart(staSteEntity.getStartSpeedTime());
		}
		// 更新上次是否是错误时间的状态
		dataIsFault = dataIsFaultTemp;
		// 加速度分析
		accSpeedAnalysis(curTime, curSpeed, curKilo, curSoc, vProviderDao);
		// 平稳驾驶分析
		steadySpeedAnalysis(curTime, curSpeed, curKilo, curSoc, vProviderDao);
	}

	/**
	 * 加速度判断
	 */
	private void accSpeedAnalysis(long curTime, float curSpeed, float curKilo, int curSoc,
								  VehicleProviderDao vProviderDao) {
		// 获取当前定位地址
		String curAddress = "guangzhou" /*BD_LocUtils.addressStr*/;//hlj
		// 首次,记录开始时间
		if (staAccEntity.getStartSpeedTime() == 0) {
			// 更新当前车速
			staAccEntity.setStartSpeed(curSpeed);
			// 开始行车时间
			staAccEntity.setStartSpeedTime(curTime);
			// 起点地址
			staAccEntity.setStartAddressStr(curAddress);
			// 开始总里程数
			staAccEntity.setStartDriveKilo(curKilo);
			// 开始行车耗电
			staAccEntity.setStartSpeedSoc(curSoc);
			return;
		}
		// 更新当前车速
		staAccEntity.setEndSpeed(curSpeed);
		// 结束行车时间
		staAccEntity.setEndSpeedTime(curTime);
		// 结束地址
		staAccEntity.setEndAddressStr(curAddress);
		// 结束总里程数
		staAccEntity.setEndDriveKilo(curKilo);
		// 结束行车耗电
		staAccEntity.setEndSpeedSoc(curSoc);
		// 此次行驶时间
		float accSpeedT = staAccEntity.getSpeedTime();
		// 判断此次行驶时间是否小于最小统计时间,如果小于舍去
		if (accSpeedT < DaoConst.ACC_CONTINUE_TIME) {
			return;
		}
		// 此次更新的加速度
		float accSpeed = staAccEntity.getAccSpeed();
		int drivType = -1;
		// 分为加速和减速,以及异常
		float startSpeedValue = staAccEntity.getStartSpeed();
		// 开始速度小于15km/h,认为是车辆起速阶段,开始速度大于15km/h,认为是行驶过程中的加速
		if (startSpeedValue < DaoConst.STARTCAR_DIA_SPEED && accSpeed > 0) {
			if (accSpeed > vConfig.getSTARTCAR_ACC_VALUE()) {
				Log.i("dia", "=起车加速过猛=====" + accSpeedT);
				drivType = IDrivingHabitReport.SPEED_UP_DRIVING_TYPE;
			} else {// 正常车速,复位进入下一次记录
				staAccEntity.reset();
				return;
			}
		} else {// 行驶过程中
			if (accSpeed > vConfig.getGREET_ACC_VALUE()) {// 加速
				Log.i("dia", "=行驶加速==+过猛==" + accSpeed);
				drivType = IDrivingHabitReport.SPEED_UP_DRIVING_TYPE;
			} else if (accSpeed < vConfig.getTINY_ACC_VALUE()) {// 减速
				Log.i("dia", "=行驶减速==-过猛==" + accSpeed);
				drivType = IDrivingHabitReport.SPEED_DOWN_DRIVING_TYPE;
			} else {// 正常车速,复位进入下一次记录
				staAccEntity.reset();
				return;
			}
		}
		// 进入统计
		// 急加速和急减速出现频率过高,进行过滤算1次,不重复记录
		switch (drivType) {
			case IDrivingHabitReport.SPEED_UP_DRIVING_TYPE:// 加速过猛
				// 计数频次
				if (preAccUpTime < 1000) {
					preAccUpTime = SystemTime.currentTimeMillis();
				} else {
					if (curTime - preAccUpTime < DaoConst.ACC_FREQUENCY_DURATION) {
						Log.i("dia", "==加速频率过大,小于5s====");
						staAccEntity.reset();// 重置行车习惯参数
						return;
					} else {
						preAccUpTime = curTime;
					}
				}
				break;
			case IDrivingHabitReport.SPEED_DOWN_DRIVING_TYPE:// 减速过猛
				// 计数频次
				if (preAccDownTime < 1000) {
					preAccDownTime = SystemTime.currentTimeMillis();
				} else {
					if (curTime - preAccDownTime < DaoConst.ACC_FREQUENCY_DURATION) {
						Log.i("dia", "==减速过猛统计频率,小于5");
						staAccEntity.reset();// 重置行车习惯参数
						return;
					} else {
						preAccDownTime = curTime;
					}
				}
				break;
		}
		Log.i("dia", "=加减速统计入库==" + drivType + "一次!");
		// 创建对象,存入数据库
		DrivingRecord drRecord = new DrivingRecord();
		// 设置数据
		drRecord.setDay(du.getSystemDay());
		drRecord.setStart_address(staAccEntity.getStartAddressStr());
		drRecord.setEnd_address(staAccEntity.getEndAddressStr());
		drRecord.setKilo(staAccEntity.getDriveKilo());

		drRecord.setStart_time(staAccEntity.getStartSpeedTime());
		drRecord.setEnd_time(staAccEntity.getEndSpeedTime());
		drRecord.setDuration(staAccEntity.getSpeedTime());

		drRecord.setAverage_speed(staAccEntity.getAverageSpeed());
		drRecord.setKilo_consume(staAccEntity.getKiloConsume());
		drRecord.setDriving_type(drivType);
		// 插入数据库
		vProviderDao.insertDrivingHabitRecord(drRecord);
		staAccEntity.reset();// 重置行车习惯参数
	}

	/**
	 * 平稳车速判断
	 */
	private void steadySpeedAnalysis(long curTime, float curSpeed, float curKilo, int curSoc,
									 VehicleProviderDao vProviderDao) {
		// 获取当前地址
		String curAddress = "guangzhou"/*BD_LocUtils.addressStr*/;
		if (staSteEntity.getStartSpeedTime() == 0) {// 首次,记录开始时间
			// 更新当前车速
			staSteEntity.setStartSpeed(curSpeed);
			// 开始行车时间
			staSteEntity.setStartSpeedTime(curTime);
			// 起点地址
			staSteEntity.setStartAddressStr(curAddress);
			// 开始总里程数
			staSteEntity.setStartDriveKilo(curKilo);
			// 设置开始耗电电量
			staSteEntity.setStartSpeedSoc(curSoc);
		}
		// 更新结束值
		// 更新结束车速
		staSteEntity.setEndSpeed(curSpeed);
		// 结束行车时间
		staSteEntity.setEndSpeedTime(curTime);
		// 结束地址
		staSteEntity.setEndAddressStr(curAddress);
		// 结束总里程数
		staSteEntity.setEndDriveKilo(curKilo);
		// 设置结束耗电电量
		staSteEntity.setEndSpeedSoc(curSoc);
		// 此段行车时间
		long timerTime = staSteEntity.getSpeedTime();
		// 行驶状态
		int drivType = staSteEntity.getDriveState();
		// 检查是否状态改变.为改变返回,改变继续,进入统计
		switch (drivType) {
			case IDrivingHabitReport.CAREFREE_DRIVING_TYPE:// 畅快驾驶
				// 如果正常,则返回
				if (curSpeed > vConfig.getSTEAD_DOWN_VALUE() && curSpeed < vConfig.getSTEAD_UP_VALUE()) {
					return;
				}
				break;
			case IDrivingHabitReport.SNAIL_DRIVING_TYPE:// 蜗牛驾驶
				if (curSpeed > vConfig.getSNAIL_DOWN_VALUE() && curSpeed < vConfig.getSNAIL_UP_VALUE()
						&& staSteEntity.getGearCurStatus() == 1) {// 如果正常则返回
					return;
				}
				break;
			default:// 非统计类型时,记录起始时间
				// 通过车速辨别行驶类型，并记录相应的起始时间
				verdictDrivingState(curSpeed, vProviderDao);
				return;
		}

		DrivingRecord drRecord = null;
		if (timerTime > DaoConst.STEAD_CONTINUE_TIME) {// 如果,异常了,并且时间够了,就入库;
			Log.i("dia", "计入稳定驾驶一次:类型===" + drivType + "时间:" + timerTime);
			// 创建对象
			drRecord = new DrivingRecord();
			// 设置数据
			drRecord.setDay(du.getSystemDay());
			drRecord.setStart_address(staSteEntity.getStartAddressStr());
			drRecord.setEnd_address(staSteEntity.getEndAddressStr());
			drRecord.setKilo(staSteEntity.getDriveKilo());

			drRecord.setStart_time(staSteEntity.getStartSpeedTime());
			drRecord.setEnd_time(staSteEntity.getEndSpeedTime());
			drRecord.setDuration(timerTime);

			drRecord.setAverage_speed(staSteEntity.getAverageSpeed());
			drRecord.setKilo_consume(staSteEntity.getKiloConsume());
			drRecord.setDriving_type(drivType);
		} else {
			Log.i("dia", "稳定驾驶时间过短=====:类型===" + drivType + "时间:" + timerTime);
		}
		// 当前驾驶类型,计入文件
		verdictDrivingState(curSpeed, vProviderDao);
		// 将该段有效的平稳驾驶,插入数据库
		if (drRecord != null)
			vProviderDao.insertDrivingHabitRecord(drRecord);
	}

	/**
	 * 通过车速辨别行驶类型，并记录相应的起始时间和类型
	 *
	 * @param curSpeed
	 * @param vProviderDao
	 */

	private void verdictDrivingState(float curSpeed, VehicleProviderDao vProviderDao) {
		// 重置
		staSteEntity.reset();
		// 非统计态,为初始,根据车速进行行驶状态划分.
		if (curSpeed > vConfig.getSNAIL_DOWN_VALUE() && curSpeed < vConfig.getSNAIL_UP_VALUE()
				&& staSteEntity.getGearCurStatus() == 1) {// 蜗牛驾驶
			Log.i("dia", "蜗牛驾驶类型");
			staSteEntity.setDriveState(IDrivingHabitReport.SNAIL_DRIVING_TYPE);
		} else if (curSpeed > vConfig.getSTEAD_DOWN_VALUE() && curSpeed < vConfig.getSTEAD_UP_VALUE()) {// 畅快驾驶
			Log.i("dia", "畅快驾驶类型");
			staSteEntity.setDriveState(IDrivingHabitReport.CAREFREE_DRIVING_TYPE);
		} else {
			Log.i("dia", "不统计驾驶类型");
			staSteEntity.setDriveState(IDrivingHabitReport.NO_STA_TYPE);
		}
		// 计入文件
		vProviderDao.saveDiaDrivingStart(staSteEntity);
	}

	/**
	 * 获取驾驶分析报告
	 *
	 * @return
	 */
	public DrivingRecord getTempDrivingHabitRecord() {
		DrivingRecord drRecord = null;
		// 距离上次时间是否到了定时时间
		long timerTime = staSteEntity.getSpeedTime();

		if (timerTime > DaoConst.STEAD_CONTINUE_TIME) {// 如果,异常了,并且时间够了,就入库;
			// 创建对象
			drRecord = new DrivingRecord();
			// 设置数据
			drRecord.setDay(du.getSystemDay());
			// 此段开始地址
			drRecord.setStart_address(staSteEntity.getStartAddressStr());
			// 此段结束地址
			drRecord.setEnd_address(staSteEntity.getEndAddressStr());
			// 此段行驶的里程数
			drRecord.setKilo(staSteEntity.getDriveKilo());
			// 此段开始时间
			drRecord.setStart_time(staSteEntity.getStartSpeedTime());
			// 此段结束时间
			drRecord.setEnd_time(staSteEntity.getEndSpeedTime());
			// 此段行驶时间
			drRecord.setDuration(timerTime);
			// 此段平均车速
			drRecord.setAverage_speed(staSteEntity.getAverageSpeed());
			// 此段百公里耗电
			drRecord.setKilo_consume(staSteEntity.getKiloConsume());
			// 此段驾驶状态
			drRecord.setDriving_type(staSteEntity.getDriveState());
		}
		return drRecord;
	}
}
