package com.enlightent.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "enlightent_daily.video_basic_info")
@Entity
public class VideoBasicInfo implements Serializable{

	private static final long serialVersionUID = -3275588918686919567L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String channelType;
	
	private Date releaseTime;
	
	private Integer editedFlag;
	
	private Date offLineTime;
	
	public Date getOffLineTime() {
		return offLineTime;
	}

	public void setOffLineTime(Date offLineTime) {
		this.offLineTime = offLineTime;
	}

	public Integer getEditedFlag() {
		return editedFlag;
	}

	public void setEditedFlag(Integer editedFlag) {
		this.editedFlag = editedFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
}
