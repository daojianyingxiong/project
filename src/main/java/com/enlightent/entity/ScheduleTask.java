package com.enlightent.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "schedule_task")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleTask implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final Integer STATUS_RUNNING = 1;
	public static final Integer STATUS_NOT_RUNNING = 0;
	
	@Id
	@GeneratedValue
	private Long id;
	private String title;

	private String cronExpression;

	private String lastFinishedDate;
	
	private Integer status;
	
	private Date createDate;
	
	private String note;
	
	private Integer send;//是否往钉钉发送消息1发送0不发动
	
	private String config;

	private String message;

	private String atMobiles;

	private Integer findTotal;

	private Integer errTotal;
	
	@Transient
	private boolean failed = false;
	
	private String robot;
	
	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getRobot() {
		return robot;
	}

	public void setRobot(String robot) {
		this.robot = robot;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAtMobiles() {
		return atMobiles;
	}

	public void setAtMobiles(String atMobiles) {
		this.atMobiles = atMobiles;
	}

	public Integer getFindTotal() {
		return findTotal;
	}

	public void setFindTotal(Integer findTotal) {
		this.findTotal = findTotal;
	}

	public Integer getErrTotal() {
		return errTotal;
	}

	public void setErrTotal(Integer errTotal) {
		this.errTotal = errTotal;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Integer getSend() {
		return send;
	}

	public void setSend(Integer send) {
		this.send = send;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getLastFinishedDate() {
		return lastFinishedDate;
	}

	public void setLastFinishedDate(String lastFinishedDate) {
		this.lastFinishedDate = lastFinishedDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "ScheduleTask [id=" + id + ", title=" + title + ", cronExpression=" + cronExpression + ", lastFinishedDate=" + lastFinishedDate + ", status=" + status + ", createDate=" + createDate + ", note=" + note
				+ ", send=" + send + ", config=" + config + "]";
	}

}
