package org.see.mao.strategy.snowflake;

import java.util.Random;

/**
 * @author Joshua Wang
 * @date 2016年7月1日
 */
public class IdWorker {
	// 机器标识位数
	private static final long workerIdBits = 5L;
	// 数据中心标识位数
	private static final long dataCenterIdBits = 5L;
	// 机器ID最大值
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 数据中心ID最大值
	private static final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
	// 毫秒内自增位
	private static final long sequenceBits = 12L;
	// 机器ID偏左移位数[12]
	private static final long workerIdShift = sequenceBits;
	// 数据中心ID左移17位
	private static final long dataCenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒左移22位
	private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

	private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private static final Random r = new Random();

	private final long workerId;

	private final long dataCenterId;

	private static final long startTimeStamp = 1464710400000L;

	private final long idEpoch;

	private long lastTimestamp = -1L;

	private long sequence = 0;

	public IdWorker() {
		this(startTimeStamp);
	}

	public IdWorker(long idEpoch) {
		this(r.nextInt((int) maxWorkerId), r.nextInt((int) maxDataCenterId), 0, idEpoch);
	}

	public IdWorker(long workerId, long dataCenterId, long sequence) {
		this(workerId, dataCenterId, sequence, startTimeStamp);
	}

	public IdWorker(long workerId, long dataCenterId, long sequence, long idEpoch) {
		this.workerId = workerId;
		this.dataCenterId = dataCenterId;
		this.sequence = sequence;
		this.idEpoch = idEpoch;

		if (workerId < 0 || workerId > maxWorkerId) {
			throw new IllegalArgumentException("workerId is illegal: " + workerId);
		}
		if (dataCenterId < 0 || dataCenterId > maxDataCenterId) {
			throw new IllegalArgumentException("dataCenterId is illegal: " + dataCenterId);
		}

		if (idEpoch >= timeGen()) {
			throw new IllegalArgumentException("idEpoch is illegal: " + idEpoch);
		}
	}

	public long getDataCenterId() {
		return dataCenterId;
	}

	public long getWorkerId() {
		return workerId;
	}

	public long getTime() {
		return timeGen();
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		// 时间错误判断
		if (timestamp < lastTimestamp) {
			throw new IllegalArgumentException("Clock moved backwards.");
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = timestamp;
		long id = ((timestamp - idEpoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift)
				| (workerId << workerIdShift) | sequence;
		return id;
	}

	public long getIdTimestamp(long id) {
		return idEpoch + (id >> timestampLeftShift);
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("IdBitWorker{");
		sb.append("workerId=").append(workerId);
		sb.append(", dataCenterId=").append(dataCenterId);
		sb.append(", idEpoch=").append(idEpoch);
		sb.append(", lastTimestamp=").append(lastTimestamp);
		sb.append(", sequence=").append(sequence);
		sb.append('}');
		return sb.toString();
	}

}
