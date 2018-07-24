package com.etrans.myd2.dao.config;


import com.etrans.myd2.dbhelper.CANContentConst;

public class VehicleConfig_80 extends BaseVehicleConfig{
	// 登陆
	public static final String LOGIN_SHAREPREFERENCE_FILE_NAME = "LoginInfo";
	public static final String OS_INFO_FILE_NAME = "OSInfo";
	public static final String DIA_INFO_FILE_NAME = "DIAInfo";
	public static final String PARAM_PK_CODE = "pkCode";
	public static final String PARAM_VIN_CODE = "vinCode";
	public static final String MY_DISTANCE = "myDistance";
	public static final String IS_LOGIN = "isLogin";
	public static final String TEMP_START_DURATION_PARAM = "tempStartDurationParam";
	public static final String TEMP_START_DRIV_PARAM = "tempStartDrivParam";
	public static final String DATA_PARAM = "data";

	// 数据库
	public static final int STA_SAVE_DAYS = 500;
	public static final int BASE_SAVE_COUNTS = 1000;
	public static final long HOURS_24 = 24 * 60 * 60 * 1000;
	public static final long DEFAULT_YEAR_2015_01_01 = Long
			.parseLong("1420041600000");
	// 诊断指标

/** 速度回调接口周期*/
	public static final int SPEED_CALLBACK = 500;

/** 急加速_最小值 */
	public static final int GREET_ACC_VALUE = 18000;

/** 起车_急加速 */
	public static final int STARTCAR_ACC_VALUE = 22000;

/** 只计起车_急加速的起始速度值*/
	public static final int STARTCAR_DIA_SPEED=15;

/** 急减速_最小值*/
	public static final int TINY_ACC_VALUE = -35000;

/** 稳定驾驶_统计最小时间单位*/
	public static final int STEAD_CONTINUE_TIME = 10 * 1000;

/** 加速度_统计最小时间单位*/
	public static final int ACC_CONTINUE_TIME = 1 * 1000;


/** 畅快驾驶_上值*/
	public static final int STEAD_UP_VALUE = 120;

/** 畅快驾驶_下值*/
	public static final int STEAD_DOWN_VALUE = 35;


/** 蜗牛驾驶_上值*/
	public static final int SNAIL_UP_VALUE = 20;

/** 蜗牛驾驶_下值*/
	public static final int SNAIL_DOWN_VALUE = -1;

/** 急加速和急减速_频率限制*/
	public final static int ACC_FREQUENCY_DURATION = 5*1000;

/** 蜗牛驾驶扣分比例*/
	public final static int SNAIL_POINTS_RATIO= 6;


/**
	 * 统计数据所有字段*/

	public static final String[] staProjection = { CANContentConst.VehicleStatisticsStates.DAY,// 日期
			CANContentConst.VehicleStatisticsStates.CHARGE_SOC,// 充电电量
			CANContentConst.VehicleStatisticsStates.CHARGE_TIME,// 充电时间
			CANContentConst.VehicleStatisticsStates.CONSUME_SOC,// 耗电电量
			CANContentConst.VehicleStatisticsStates.SPEED_SOC,// 行车耗电
			CANContentConst.VehicleStatisticsStates.CONSUME_TIME,// 行车时间
			CANContentConst.VehicleStatisticsStates.DRIVE_DISTANCE,// 行驶里程
			CANContentConst.VehicleStatisticsStates.AVERAGE_SPEED,// 平均车速
			CANContentConst.VehicleStatisticsStates.KILO_CONSUME // 百公里耗电
	};

/**
	 * 基础数据所有字段*/

	public static final String[] baseProjection = {
			CANContentConst.VehicleBasicStates.DAY,// 日期
			CANContentConst.VehicleBasicStates.HOURS,// 具体时间
			CANContentConst.VehicleBasicStates.SOC,// 当前电量
			CANContentConst.VehicleBasicStates.CHARGE_STATE,// 充放电状态
			CANContentConst.VehicleBasicStates.SPEED,// 平均车速
			CANContentConst.VehicleBasicStates.KILO,// 总里程
			//
			CANContentConst.VehicleBasicStates.LEFT_DOOR, CANContentConst.VehicleBasicStates.RIGHT_DOOR,
			CANContentConst.VehicleBasicStates.LEFT_WINDOW, CANContentConst.VehicleBasicStates.RIGHT_WINDOW,
			CANContentConst.VehicleBasicStates.BEHIND_DOOR };

/**
	 * 驾驶习惯记录所有字段*/

	public static final String[] habitProjection = {
			// 数据库中的表字段
			CANContentConst.VehicleDrivingRecord.DAY,// 日期
			CANContentConst.VehicleDrivingRecord.START_ADDRESS,// 起始地址
			CANContentConst.VehicleDrivingRecord.END_ADDRESS,// 结束地址
			CANContentConst.VehicleDrivingRecord.KILO,// 里程数

			CANContentConst.VehicleDrivingRecord.START_TIME,// 开始时间
			CANContentConst.VehicleDrivingRecord.END_TIME,// 结束时间
			CANContentConst.VehicleDrivingRecord.DURATION,// 时长

			CANContentConst.VehicleDrivingRecord.AVERAGE_SPEED,// 平均速度
			CANContentConst.VehicleDrivingRecord.KILO_CONSUME,// 百公里耗电
			CANContentConst.VehicleDrivingRecord.DRIVING_TYPE // 行驶状态
	};
	@Override
	public int getGREET_ACC_VALUE() {
		// TODO Auto-generated method stub
		return GREET_ACC_VALUE;
	}
	@Override
	public int getSTARTCAR_ACC_VALUE() {
		// TODO Auto-generated method stub
		return STARTCAR_ACC_VALUE;
	}
	@Override
	public int getTINY_ACC_VALUE() {
		// TODO Auto-generated method stub
		return TINY_ACC_VALUE;
	}
	@Override
	public int getSTEAD_UP_VALUE() {
		// TODO Auto-generated method stub
		return STEAD_UP_VALUE;
	}
	@Override
	public int getSTEAD_DOWN_VALUE() {
		// TODO Auto-generated method stub
		return STEAD_DOWN_VALUE;
	}
	@Override
	public int getSNAIL_UP_VALUE() {
		// TODO Auto-generated method stub
		return SNAIL_UP_VALUE;
	}
	@Override
	public int getSNAIL_DOWN_VALUE() {
		// TODO Auto-generated method stub
		return SNAIL_DOWN_VALUE;
	}
}
