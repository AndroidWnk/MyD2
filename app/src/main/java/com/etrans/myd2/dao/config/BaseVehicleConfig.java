package com.etrans.myd2.dao.config;


/**
 * 车辆参数配置
 *
 * @author Administrator
 **/


public abstract class BaseVehicleConfig {
    public static final int VEHICLE_TYPE = 100;
    private static BaseVehicleConfig vConfig;

    public static BaseVehicleConfig instance() {
        if (vConfig != null)
            return vConfig;

        switch (VEHICLE_TYPE) {
            case 80: //双80
                vConfig = new VehicleConfig_100();//hlj都使用双100
                break;
            case 100: //双100
                vConfig = new VehicleConfig_100();
                break;
        }
        return vConfig;

    }

    /**
     * 急加速_最小值
     */

    public abstract int getGREET_ACC_VALUE();

    /**
     * 起车_急加速
     */

    public abstract int getSTARTCAR_ACC_VALUE();

    /**
     * 急减速_最小值
     */

    public abstract int getTINY_ACC_VALUE();

    /**
     * 畅快驾驶_上值
     */

    public abstract int getSTEAD_UP_VALUE();

    /**
     * 畅快驾驶_下值
     */

    public abstract int getSTEAD_DOWN_VALUE();

    /**
     * 蜗牛驾驶_上值
     */

    public abstract int getSNAIL_UP_VALUE();

    /**
     * 蜗牛驾驶_下值
     */

    public abstract int getSNAIL_DOWN_VALUE();
}
