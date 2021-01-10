package com.backend.utils;

import com.google.common.collect.Maps;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

@Data
@Slf4j
public class BeanMapUtil<T> extends BeanMap {

    private Map<Object, Object> map;

    private T obj;

    @Override
    public BeanMap newInstance(Object o) {
        BeanMap beanMap = BeanMap.create(o);
//        Map<Object, Object> objectMap = beanToMapObjKey(o);
//        this.map = objectMap;
//        this.obj = (T) o;
        return beanMap;
    }

    @Override
    public Class getPropertyType(String propertyName) {
        return null;
    }

    @Override
    public Object get(Object o, Object o1) {
        return null;
    }

    @Override
    public Object put(Object o, Object o1, Object o2) {
        return null;
    }

    @Override
    public Set keySet() {
        return null;
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        if (Objects.isNull(bean)) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        try {
            BeanMap beanMap = BeanMap.create(bean);
            Set keys = beanMap.keySet();
            for (Object key : keys) {
                map.put(String.valueOf(key), beanMap.get(key));
            }
        } catch (Exception e) {
        }
        return map;
    }

    private static <T> Map<Object, Object> beanToMapObjKey(T bean) {
        Map<Object, Object> map = Maps.newHashMap();
        try {
            BeanMap beanMap = BeanMap.create(bean);
            Set keys = beanMap.keySet();
            for (Object key : keys) {
                map.put(key, beanMap.get(key));
            }
        } catch (Exception e) {
        }
        return map;
    }

    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            T bean = declaredConstructor.newInstance();
            return mapToBean(map, bean);
        } catch (Exception e) {
            log.info("method mapToBean exception, map convert to bean [{}] exception: {}", clazz, e);
        }
        return null;
    }

    /**
     * 请慎用该方法：该方法采用 {@link org.apache.commons.beanutils.BeanUtils} 工具类，与 lombok的getter和setter 存在问题,会找不到这些方法
     *
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T mapToBeanObj(Map<String, Object> map, T bean) {
        try {
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            log.info("method mapToBeanObj exception: {}", e);
        }
        return bean;
    }

}
