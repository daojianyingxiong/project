package com.enlightent.entity;

/**
* @author 作者 wr:
* @version 创建时间：2018年2月3日 下午4:07:55
* 类说明
*/
import java.math.BigInteger;

public class JobAndTrigger {
	private String JOB_NAME;
	private String JOB_GROUP;
	private String JOB_CLASS_NAME;
	private byte[] JOB_DATA;
	private String TRIGGER_NAME;
	private String TRIGGER_GROUP;
	private Long START_TIME;
	private Long NEXT_FIRE_TIME;
	private Long PREV_FIRE_TIME;
	private String TRIGGER_STATE;
	private BigInteger REPEAT_INTERVAL;
	private BigInteger TIMES_TRIGGERED;
	private String CRON_EXPRESSION;
	private String TIME_ZONE_ID;

	private ScheduleTask scheduleTask;
	
	
	public Long getPREV_FIRE_TIME() {
		return PREV_FIRE_TIME;
	}

	public void setPREV_FIRE_TIME(Long pREV_FIRE_TIME) {
		PREV_FIRE_TIME = pREV_FIRE_TIME;
	}

	public String getTRIGGER_STATE() {
		return TRIGGER_STATE;
	}

	public void setTRIGGER_STATE(String tRIGGER_STATE) {
		TRIGGER_STATE = tRIGGER_STATE;
	}

	public Long getSTART_TIME() {
		return START_TIME;
	}

	public void setSTART_TIME(Long sTART_TIME) {
		START_TIME = sTART_TIME;
	}

	public Long getNEXT_FIRE_TIME() {
		return NEXT_FIRE_TIME;
	}

	public void setNEXT_FIRE_TIME(Long nEXT_FIRE_TIME) {
		NEXT_FIRE_TIME = nEXT_FIRE_TIME;
	}

	public ScheduleTask getScheduleTask() {
		return scheduleTask;
	}

	public void setScheduleTask(ScheduleTask scheduleTask) {
		this.scheduleTask = scheduleTask;
	}

	public byte[] getJOB_DATA() {
		return JOB_DATA;
	}

	public void setJOB_DATA(byte[] jOB_DATA) {
		JOB_DATA = jOB_DATA;
	}

	public String getJOB_NAME() {
		return JOB_NAME;
	}

	public void setJOB_NAME(String jOB_NAME) {
		JOB_NAME = jOB_NAME;
	}

	public String getJOB_GROUP() {
		return JOB_GROUP;
	}

	public void setJOB_GROUP(String jOB_GROUP) {
		JOB_GROUP = jOB_GROUP;
	}

	public String getJOB_CLASS_NAME() {
		return JOB_CLASS_NAME;
	}

	public void setJOB_CLASS_NAME(String jOB_CLASS_NAME) {
		JOB_CLASS_NAME = jOB_CLASS_NAME;
	}

	public String getTRIGGER_NAME() {
		return TRIGGER_NAME;
	}

	public void setTRIGGER_NAME(String tRIGGER_NAME) {
		TRIGGER_NAME = tRIGGER_NAME;
	}

	public String getTRIGGER_GROUP() {
		return TRIGGER_GROUP;
	}

	public void setTRIGGER_GROUP(String tRIGGER_GROUP) {
		TRIGGER_GROUP = tRIGGER_GROUP;
	}

	public BigInteger getREPEAT_INTERVAL() {
		return REPEAT_INTERVAL;
	}

	public void setREPEAT_INTERVAL(BigInteger rEPEAT_INTERVAL) {
		REPEAT_INTERVAL = rEPEAT_INTERVAL;
	}

	public BigInteger getTIMES_TRIGGERED() {
		return TIMES_TRIGGERED;
	}

	public void setTIMES_TRIGGERED(BigInteger tIMES_TRIGGERED) {
		TIMES_TRIGGERED = tIMES_TRIGGERED;
	}

	public String getCRON_EXPRESSION() {
		return CRON_EXPRESSION;
	}

	public void setCRON_EXPRESSION(String cRON_EXPRESSION) {
		CRON_EXPRESSION = cRON_EXPRESSION;
	}

	public String getTIME_ZONE_ID() {
		return TIME_ZONE_ID;
	}

	public void setTIME_ZONE_ID(String tIME_ZONE_ID) {
		TIME_ZONE_ID = tIME_ZONE_ID;
	}

}
