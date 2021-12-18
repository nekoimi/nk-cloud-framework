package com.nekoimi.nk.framework.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * nekoimi  2021/8/31 16:06
 */
public class ClazzUtils {

    /**
     * @param clazz
     * @param target
     * @return
     */
    public static boolean instanceOf(Class clazz, Class target) {
        if (clazz == null) return false;
        if (clazz == target) return true;
        if (target.isInterface()) {
            // 目标类型是接口，则当且仅当 clazz 实现了 target 接口
            for (Class clazzInterface : clazz.getInterfaces()) {
                if (clazzInterface == target) return true;
            }
        }

        if (clazz.isInterface()) {
            for (Class clazzInterface : clazz.getInterfaces()) {
                if (instanceOf(clazzInterface, target)) {
                    return true;
                }
            }
        } else {
            if (clazz.getSuperclass() == target) {
                return true;
            }
        }

        return instanceOf(clazz.getSuperclass(), target);
    }

    /**
     * 获取类中所有的字段
     * @param clazz
     * @return
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        Field[] results = new Field[fieldList.size()];
        return fieldList.toArray(results);
    }

    /**
     * 查找类中的字段
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        if (StringUtils.isBlank(fieldName)) return null;
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (fieldName.equals(field.getName())) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 获取实例
     * @param clazz
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T instance(Class<T> clazz, Object...args) {
        try {
            return clazz.getDeclaredConstructor().newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }
}
