package com.siebre.basic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class IdWorker {
    private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);
    private final long workerId;
    private final long snsEpoch = 1330328109047L;
    private long sequence = 0L;
    private final long workerIdBits = 10L;
    private final long maxWorkerId;
    private long sequenceBits = 12L;
    private final long workerIdShift;
    private final long timestampLeftShift;
    private final long sequenceMask;
    private long lastTimestamp;

    public IdWorker(long workerId) {
        getClass();
        this.maxWorkerId = (0xFFFFFFFF ^ -1L << 10);
        this.sequenceBits = 12L;

        getClass();
        this.workerIdShift = 12L;
        getClass();
        getClass();
        this.timestampLeftShift = (12L + 10L);
        getClass();
        this.sequenceMask = (0xFFFFFFFF ^ -1L << 12);

        this.lastTimestamp = -1L;

        if ((workerId > this.maxWorkerId) || (workerId < 0L)) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", new Object[]{Long.valueOf(this.maxWorkerId)}));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() throws Exception {
        long timestamp = timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & this.sequenceMask);
            if (this.sequence == 0L)
                timestamp = tilNextMillis(this.lastTimestamp);
        } else {
            this.sequence = 0L;
        }
        if (timestamp < this.lastTimestamp) {
            logger.error(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", new Object[]{Long.valueOf(this.lastTimestamp - timestamp)}));
            throw new Exception(String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", new Object[]{Long.valueOf(this.lastTimestamp - timestamp)}));
        }

        this.lastTimestamp = timestamp;

        getClass();
        return timestamp - 1330328109047L << (int) this.timestampLeftShift | this.workerId << (int) this.workerIdShift | this.sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {
        IdWorker iw = new IdWorker(1L);
        System.out.println("maxWorkerId:" + iw.maxWorkerId);
        System.out.println("sequenceMask:" + iw.sequenceMask);
        long begin = new Date().getTime();
        for (int i = 0; i < 4094; i++) {
            System.out.println(iw.nextId());
        }
        long end = new Date().getTime();
        System.out.println((end - begin) / 1000L);
    }
}
