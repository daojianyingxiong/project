package com.enlightent.util;

import java.util.Arrays;

/**
 * @author jianglei
 * @since 2017/12/13
 */
public class ArrayUtils {

    // TODO: 2017/12/13 有时间可转换为多维的 
    /**
     * 二维数组toString
     */
   public static  String toString(Object[] objs) {
        if (objs == null) {
            return "null";
        }
        if (objs.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        Class<? extends Object[]> aClass = objs.getClass();
        Class<?> componentType = aClass.getComponentType();

        sb.append("[");
        for(int i = 0, iMax = objs.length - 1; ; i++) {
            if (componentType.isArray()) {
                sb.append(Arrays.toString((Object[]) objs[i]));
            } else {
                sb.append(objs[i]);
            }

            if (i == iMax) {
                return sb.append("]").toString();
            }
            sb.append(", ");
        }
    }

    public static void main(String[] args) {
        Object[][] obj = {{1, 2}, {"a", "b"}};
        String s = toString(obj);
        System.out.println(s);
    }
}
