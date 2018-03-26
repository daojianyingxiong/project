//package com.enlightent.util;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import org.apache.commons.lang3.StringUtils;
//import com.enlightent.entity.ScheduleTask;
//
//public class ScheduleTaskUtil {
//	public static void invokMethod(ScheduleTask wt) {
//		Object object = null;
//		Class<?> clazz = null;
//		if (StringUtils.isNotBlank(wt.getSourceOne())) {
//			object = SpringUtils.getBean(wt.getSourceOne());
//		}
//		if (object == null) {
//			return;
//		}
//		clazz = object.getClass();
//		Method method = null;
//
//		try {
//			method = clazz.getDeclaredMethod(wt.getMethodName(), ScheduleTask.class);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		}
//
//		if (method != null) {
//			try {
//				method.invoke(object, wt);
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//}
