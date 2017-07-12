package com.siebre.payment.utils.serial;

import com.siebre.payment.serialnumber.entity.SerialNumber;
import com.siebre.payment.serialnumber.mapper.SerialNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by AdamTang on 2017/3/29.
 * Project:siebre-cloud-platform
 * Version:1.0
 */
public final class SerialNumberGenerator {
    private Logger logger = LoggerFactory.getLogger(SerialNumberGenerator.class);
    /**
     * 前缀
     */
    private String prefix = "DEFAULT_";
    /**
     * 长度
     */
    private Integer length = 6;
    /**
     * 增长步长
     */
    private Integer step = 1;
    /**
     * 是否需要日期填充
     */
    private Boolean dateFill = true;
    /**
     * 一次缓存的Step
     */
    private Long cacheStep = 1000L;

    private SerialNumber instance;

    private SerialNumberMapper mapper;

    private AtomicLong currentValue;

    private AtomicLong currentMaxValue;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public SerialNumberGenerator(SerialNumber instance, SerialNumberMapper mapper) {
        this.instance = instance;
        this.mapper = mapper;
        this.currentValue = new AtomicLong();
        this.currentMaxValue = new AtomicLong();
        init();
    }

    private void init() throws SerialNumberGenerateException {
        if (instance != null) {
            if (instance.getCacheStep() != null) {
                this.cacheStep = instance.getCacheStep();
            }
            if (instance.getStep() != null) {
                this.step = instance.getStep();
            }
            if (instance.getPrefix() != null) {
                this.prefix = instance.getPrefix();
            }
            if (instance.getDateFill() != null) {
                this.dateFill = instance.getDateFill();
            }
            if (mapper != null) {
                modifyCacheValue();
            } else {
                currentMaxValue.set(step * cacheStep);
            }
        } else {
            currentMaxValue.set(step * cacheStep);
        }
    }


    public String next() throws SerialNumberGenerateException {
        try {
            lock.writeLock().lock();

            StringBuilder builder = new StringBuilder();

            builder.append(prefix);//前缀

            if (dateFill) {//是否需要填充日期
                simpleDateBuilder(builder);
            }

            long tempCurrentValue = currentValue.get();

            long tmpMaxValue = this.currentMaxValue.get();

            //预估本次获取值之后是否超出数据库记录的最大申请值,如果超出则同步数据库到本地缓存
            if (tempCurrentValue + step >= tmpMaxValue) {
                modifyCacheValue();
            }

            //永远返回当前值增加步长之后的值
            long returnValue = currentValue.addAndGet(step);

            simpleLongBuilder(returnValue, builder);//

            return builder.toString();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void modifyCacheValue() throws SerialNumberGenerateException {
        try {
            lock.writeLock().lock();

            long tempIncreaseValue = step * cacheStep;//获取增长步长

            if (mapper != null) {//mapper为null则为纯内存增长
                long dbHistoryValue = mapper.selectAndUpdate(instance.getName(), tempIncreaseValue);//取得数据库历史数据 并且更新需要的序列到数据库

                currentValue.set(dbHistoryValue);//设置数据库存储值 本地内存中的当前值
                currentMaxValue.set(dbHistoryValue + tempIncreaseValue);//同步记录数据库最大值

            } else {
                currentMaxValue.addAndGet(tempIncreaseValue);
            }

        } catch (Exception e) {
            logger.error("更新数据库获取新序列失败", e);
            throw new SerialNumberGenerateException("更新数据库获取新序列失败", e);
        } finally {
            lock.writeLock().unlock();
        }

    }

    //日期格式化
    private void simpleDateBuilder(StringBuilder builder) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        builder.append(year);
        if (month < 10) {
            builder.append('0');
        }
        builder.append(month);
        if (day < 10) {
            builder.append('0');
        }
        builder.append(day);
    }

    //序列补0
    private void simpleLongBuilder(long value, StringBuilder builder) {
        int loop = length;
        char[] chars = new char[length];
        while (loop > 0) {
            if (value > 0) {
                chars[--loop] = (char) ((value % 10) + '0');
                value /= 10;
            } else {
                chars[--loop] = '0';
            }
        }
        if (value != 0) {
            builder.append(value);
            logger.warn("序列号设置下标不够,已自动添加");
        }
        builder.append(chars);
    }
}
