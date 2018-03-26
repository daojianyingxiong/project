package com.enlightent.been.ding;

import java.util.List;

public class AtMobile {

	private boolean isAtAll = false;
	
	private List<String> atMobiles;

	public boolean isAtAll() {
		return isAtAll;
	}

	public void setAtAll(boolean isAtAll) {
		this.isAtAll = isAtAll;
	}

	public List<String> getAtMobiles() {
		return atMobiles;
	}

	public void setAtMobiles(List<String> atMobiles) {
		this.atMobiles = atMobiles;
	}

	@Override
	public String toString() {
		return "AtMobile [isAtAll=" + isAtAll + ", atMobiles=" + atMobiles + "]";
	}

}
