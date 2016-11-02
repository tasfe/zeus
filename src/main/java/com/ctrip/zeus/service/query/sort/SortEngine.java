package com.ctrip.zeus.service.query.sort;

import com.ctrip.zeus.exceptions.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhoumy on 2016/11/2.
 */
public class SortEngine {
    Map<Class, Map<String, Method>> methodLoader = new HashMap<>();

    public void sort(final String propertyName, PropertySortable[] input) {
        Arrays.sort(input, new Comparator<PropertySortable>() {
            @Override
            public int compare(PropertySortable o1, PropertySortable o2) {
                return o1.getValue(propertyName).compareTo(o2.getValue(propertyName));
            }
        });
    }

    public <T> void sort(String propertyName, T[] input) {
        Map<String, Method> mCached = methodLoader.get(input.getClass().getComponentType());
        if (mCached == null) return;
        final Method m = mCached.get(propertyName);
        if (m == null) return;

        Arrays.sort(input, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try {
                    Comparable v1 = (Comparable) m.invoke(o1);
                    Comparable v2 = (Comparable) m.invoke(o2);
                    return v1.compareTo(v2);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
                return 0;
            }
        });
    }

    public SortEngine register(String propertyName, Class<?> clazz) throws ValidationException {
        Map<String, Method> mCached = methodLoader.get(clazz);
        Method m;
        if (mCached != null) {
            m = mCached.get(propertyName);
            if (m != null) return this;
        }

        try {
            m = clazz.getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
        } catch (NoSuchMethodException e) {
            throw new ValidationException("Getter is not found by propertyName " + propertyName + " in class " + clazz.getName() + ".");
        }
        validate(m);

        if (mCached == null) {
            mCached = new HashMap<>();
            methodLoader.put(clazz, mCached);
        }
        mCached.put(propertyName, m);
        return this;
    }

    private void validate(Method m) throws ValidationException {
        if (m.getParameterTypes().length > 0) {
            throw new ValidationException("Parameter count mismatched in " + m.getName() + ".");
        }
        if (!Comparable.class.isAssignableFrom(m.getReturnType())) {
            throw new ValidationException("Returned value from " + m.getName() + " is not sortable.");
        }
    }
}