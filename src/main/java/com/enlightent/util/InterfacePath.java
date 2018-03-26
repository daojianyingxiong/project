package com.enlightent.util;

public class InterfacePath {
	private static final String CONTEXT_NAME = "data-support";

	public static final String HOST = ConfUtil.DATA_SUPPORT_ADDRESS + CONTEXT_NAME + "/";

	public static final String TVCOUNT = HOST + "video/tvCount";
	public static final String ARTCOUNT = HOST + "video/serialList";
	
	
	private static final String ANALYTICS_CONTEXT_NAME = "analytics";
	public static final String ANALYTICS_HOST = ConfUtil.ANALYTICS_ADDRESS + ANALYTICS_CONTEXT_NAME + "/";
	//tv热度榜
	public static final String TVHOT = ANALYTICS_HOST + "public/getVideoOpinionTop.do";
	//tv top
	public static final String TVTOP = ANALYTICS_HOST + "videoTop.do";
}
