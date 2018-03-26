package com.enlightent.been.ding;

public class Link extends MsgType {

	private String picUrl;
	private String messageUrl;
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getMessageUrl() {
		return messageUrl;
	}
	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}
	
	@Override
	public String getType() {
		return "link";
	}
	@Override
	public String toString() {
		return "Link [picUrl=" + picUrl + ", messageUrl=" + messageUrl + "]";
	}
	
}
