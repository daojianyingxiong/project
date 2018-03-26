package com.enlightent.been;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String channel;
	private Integer index;
	private Long dayPlayTimes;
	private Long commentCount = 0L;
	private Long dayBarrageCount;
	private Integer fake;
	private Date date;

	public Integer getFake() {
		return fake;
	}

	public void setFake(Integer fake) {
		this.fake = fake;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Long getDayPlayTimes() {
		return dayPlayTimes;
	}

	public void setDayPlayTimes(Long dayPlayTimes) {
		this.dayPlayTimes = dayPlayTimes;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getDayBarrageCount() {
		return dayBarrageCount;
	}

	public void setDayBarrageCount(Long dayBarrageCount) {
		this.dayBarrageCount = dayBarrageCount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "name=" + name + ", channel=" + channel + ", index=" + index + ", dayPlayTimes=" + dayPlayTimes + ", commentCount=" + commentCount + ", dayBarrageCount=" + dayBarrageCount + ", date=" + date +";";
	}

}
