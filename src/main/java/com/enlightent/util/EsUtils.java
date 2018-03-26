package com.enlightent.util;

import com.enlightent.been.EsSql;

import java.util.List;

public abstract class EsUtils {
	
	public static String buildWhere(String[] keys, List<String> list) {
		StringBuilder stringBuilder = new StringBuilder(300);
		stringBuilder.append("(");
		for (String string : list) {
			String splitSymbol = "\\+";
			String symbol = "=";
			boolean contains = string.contains("+");
			if (contains) {
				symbol = "=";
			} else if (string.contains("<>")) {
				symbol = "<>";
				splitSymbol = "\\<>";
			}
			String[] split = string.split(splitSymbol);
			StringBuilder sBuilder1 = new StringBuilder();
			sBuilder1.append("(");
			for (String key : keys) {
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("(").append(key).append("=").append("\"").append(split[0]).append("\"");
				for (int i = 1; i < split.length; i++) {
					sBuilder.append("  and ");
					sBuilder.append(key).append(symbol).append("\"").append(split[i]).append("\"");
				}
				sBuilder.append(")");
				sBuilder1.append(sBuilder);
				sBuilder1.append(" or ");
			}
			sBuilder1.delete(sBuilder1.length() - 4, sBuilder1.length());
			sBuilder1.append(")");
			stringBuilder.append(sBuilder1).append(" or ");
		}
		stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	/**
	 * 获取Essql实例
	 *
	 * @return Essql实例
	 */
	public static EsSql getPrototypeInstance() {
		return new EsSql();
	}
}
