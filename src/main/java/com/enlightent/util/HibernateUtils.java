package com.enlightent.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.CollectionUtils;

public class HibernateUtils {
	
	public static <T> List<T> convertList(Class<T> clazz, List<Map<String, Object>> list) {
		List<T> result;
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		result = new ArrayList<T>(list.size());
		try {
			for (Map<String, Object> map : list) {
				result.add(convertObject(clazz, map));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static <T> T convertObject(Class<T> clazz, Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		try {
			PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
			T obj = clazz.newInstance();
			
			for (PropertyDescriptor prop : props) {
				String name = prop.getName();
				Object value = map.get(name);
				if (value != null) {
					value = ConvertUtils.convert(value, prop.getPropertyType());
					Method method = prop.getWriteMethod();
					method.invoke(obj, value);
				}
			}
			
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
