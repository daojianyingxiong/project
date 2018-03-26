package com.enlightent.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class ExpressionUtils {

	private static final String GT = ">";
	private static final String LT = "<";
	private static final String EQ = "=";
	private static final String N_EQ = "!=";
	
	private static final String[] CONDITIONS = new String[]{ GT, LT, EQ, N_EQ };

	private static final String EMPTY = "null";

	public static boolean checkResult(Object source, String expression) {
		if (source == null) {
			source = "";
		}
		expression = expression.trim();
		for (String condition : CONDITIONS) {
			if (expression.startsWith(condition)) {
				String value = expression.substring(condition.length(), expression.length()).trim();
				switch (condition) {
				case GT:
					return gtCompare(source, value);
				case LT:
					return ltCompare(source, value);
				case EQ:
					return eqCompare(source, value);
				case N_EQ:
					return notEqCompare(source, value);
				}
			}
		}
		return false;
	}
	
	private static boolean gtCompare(Object source, String value){
		
		if (source == null || StringUtils.isBlank(source.toString())) {
			return false;
		}
		
		double int1;
		if (source instanceof Collection) {
			int1 = ((Collection) source).size();
		} else {
			int1 = stringToDouble(source.toString());
		}
		return int1 > stringToDouble(value);
	}
	
	private static boolean ltCompare(Object source, String value){
		
		if (source == null || StringUtils.isBlank(source.toString())) {
			return false;
		}
		
		double int1;
		if (source instanceof Collection) {
			int1 = ((Collection) source).size();
		} else {
			int1 = stringToDouble(source.toString());
		}
		return int1 < stringToDouble(value);
	}
	
	private static boolean eqCompare(Object source, String value){
		if (source instanceof Collection) {
			int size = ((Collection) source).size();
			if (size == 0) {
				source = "0";
			}else{
				source = size;
			}
		}
		if (EMPTY.equals(value)) {
			if (source == null) {
				return true;
			}
			return StringUtils.isBlank(source.toString());
		} else {
			if (source == null || StringUtils.isBlank(source.toString())) {
				return false;
			}
			return stringToDouble(source.toString()) == stringToDouble(value);
		}
	}
	private static boolean notEqCompare(Object source, String value){
		if (source instanceof Collection) {
			int size = ((Collection) source).size();
			if (size == 0) {
				source = "0";
			}
		}
		
		if (EMPTY.equals(value)) {
			if (source == null) {
				return false;
			}
			return StringUtils.isNotBlank(source.toString());
		} else {
			if (source == null || StringUtils.isBlank(source.toString())) {
				return false;
			}
			return stringToDouble(source.toString()) != stringToDouble(value);
		}
	}
	
	private static int stringToInt(String str) {
		int indexOf = str.indexOf(".");
		if (indexOf > -1) {
			str = str.substring(0, indexOf);
		}
		return Integer.parseInt(str);
	}
	
	private static double stringToDouble(String str) {
		return Double.parseDouble(str);
	}
	
	public static void main(String[] args) {
		String expression = "!=0";
		boolean checkResult = checkResult(0, expression);
		System.out.println(checkResult);
	}
}
