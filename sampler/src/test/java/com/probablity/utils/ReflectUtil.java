package com.probablity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

    /**
     * @Description:only for check some private field for test
     * 
     * @param instance
     *            the class be called
     * @param variableName
     *            the name of member
     * @return
     */
    public static Object getClassMemberField(Object instance, String variableName) {
        Class<?> targetClass = instance.getClass();
        Field declaredField = null;
        try {
            declaredField = targetClass.getDeclaredField(variableName);
        } catch (NoSuchFieldException | SecurityException e1) {
            e1.printStackTrace();
        }

        declaredField.setAccessible(true);

        Object object = null;
        try {
            object = declaredField.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {

            e.printStackTrace();
        }

        return object;
    }

    public static void setClassMemberField(Object instance, String variableName, Object obj) {
        Class<?> targetClass = instance.getClass();
        Field declaredField = null;
        try {
            declaredField = targetClass.getDeclaredField(variableName);
        } catch (NoSuchFieldException | SecurityException e1) {
            e1.printStackTrace();
        }

        declaredField.setAccessible(true);

        try {
            declaredField.set(instance, obj);
        } catch (IllegalArgumentException | IllegalAccessException e1) {

            e1.printStackTrace();
        }
    }

    public static Method getClassMemberMethod(Object instance, String methodName) {

        Class<?> targetClass = instance.getClass();

        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method method : declaredMethods) {

            String name = method.getName();
            if (name.equals(methodName)) {

                method.setAccessible(true);
                return method;
            }
        }

        return null;

    }
}
