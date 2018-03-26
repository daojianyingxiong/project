package com.enlightent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "schedule_task_info")
@Entity
public class ScheduleTaskInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private Integer status;
	private Date createDate = new Date();
	private String message;
	private String atMobiles;
	private Integer findTotal;
	private Integer percent;
	private Integer errTotal;
	private Integer printCount;
	private Long taskId;
	private String taskFinishDate;
	private String debugInfo;

	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskFinishDate() {
		return taskFinishDate;
	}

	public void setTaskFinishDate(String taskFinishDate) {
		this.taskFinishDate = taskFinishDate;
	}

	public String getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

}
