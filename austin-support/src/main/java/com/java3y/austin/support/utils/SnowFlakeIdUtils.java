package com.java3y.austin.support.utils;

/**
 * 雪花算法生成唯一id工具类
 *
 * @author cao
 * @date 2022-04-20 13:12
 */
public class SnowFlakeIdUtils {

    /**
     * 初始时间截 (2017-01-01)
     */
    private final static long START_TIMESTAMP = 1483200000000L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12;   //***占用的位数
    private final static long MACHINE_BIT = 5;     //机器标识占用的位数
    private final static long DATA_CENTER_BIT = 5; //数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //***
    private long lastTimeStamp = -1L;  //上一次时间戳


    /**
     * 根据指定的数据中心ID和机器标志ID生成指定的***
     *
     * @param dataCenterId 数据中心ID
     * @param machineId    机器标志ID
     */
    public SnowFlakeIdUtils(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("DtaCenterId  不能大于 %d 或小于 0", MAX_DATA_CENTER_NUM));
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException(String.format("MachineId  不能大于 %d 或小于 0", MAX_MACHINE_NUM));
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }


    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currTimeStamp = System.currentTimeMillis();
        if (currTimeStamp < lastTimeStamp) {
            throw new RuntimeException("当前时间小于上一次记录的时间戳！");
        }

        if (currTimeStamp == lastTimeStamp) {
            //相同毫秒内，***自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currTimeStamp = getNextMill();
            }
        } else {
            //不同毫秒内，***置为0
            sequence = 0L;
        }

        lastTimeStamp = currTimeStamp;

        return (currTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT //时间戳部分
                | dataCenterId << DATA_CENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //***部分
    }


    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @return 当前时间戳
     */
    private long getNextMill() {

        long mill = System.currentTimeMillis();
        while (mill <= lastTimeStamp) {
            mill = System.currentTimeMillis();
        }
        return mill;

    }

}
