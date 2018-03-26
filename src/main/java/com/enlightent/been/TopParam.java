package com.enlightent.been;

import java.util.Date;

public class TopParam {

	/**
	 * total = 全渠道
	 */
	private String channel;

	/**
	 * tv, movie, art, animation
	 */
	private String channelType;

	/**
	 * 1, 7
	 */
	private String day;

	private Date date;

	/**
	 * 0 = 网络剧， 1 = 非网剧
	 */
	private Integer netType;

	private String sort;

	private Integer size;

	/**
	 * inc = 日增数据， acc = 年度统计
	 */
	private String dataType;

	
	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getChannelType() {
		return channelType;
	}


	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}


	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Integer getNetType() {
		return netType;
	}


	public void setNetType(Integer netType) {
		this.netType = netType;
	}


	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public Integer getSize() {
		return size;
	}


	public void setSize(Integer size) {
		this.size = size;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	@Override
	public String toString() {
		return "TopParam [channel=" + channel + ", channelType=" + channelType + ", day=" + day + ", date=" + date
				+ ", netType=" + netType + ", sort=" + sort + ", size=" + size + ", dataType=" + dataType + "]";
	}

}
