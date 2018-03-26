package com.enlightent.been.ding;

public abstract class MsgType {
	
	private String title;
	private String text;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public abstract String getType();
	@Override
	public String toString() {
		return "MsgType [title=" + title + ", text=" + text + "]";
	}
}
