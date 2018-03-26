package com.enlightent.been;

public enum ChannelTypeEnum {
	TV("0", "tv"), MOVIE("1", "movie"), ART("2", "art");
	private String index;
	private String channelType;

	private ChannelTypeEnum(String index, String channelType) {
		this.index = index;
		this.channelType = channelType;
	}

	public static String ChangeChannelType(String channelTy) {
		ChannelTypeEnum[] values = ChannelTypeEnum.values();
		for (ChannelTypeEnum channelType : values) {
			if (channelTy.equals(channelType.getIndex())) {
				return channelType.getChannelType();
			}
		}
		return null;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
}
