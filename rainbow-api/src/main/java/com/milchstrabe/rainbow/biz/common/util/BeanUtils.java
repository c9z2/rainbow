package com.milchstrabe.rainbow.biz.common.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/6/13 17:23
 * @Version 1.0
 * @Description
 **/
public class BeanUtils {

    private static Mapper MAPPER = new DozerBeanMapper();

    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        return MAPPER.map(source, destinationClass);
    }

    public static void map(Object source, Object destination) {
        MAPPER.map(source, destination);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<>();
        for (Object sourceObject : sourceList) {
            destinationList.add(MAPPER.map(sourceObject, destinationClass));
        }
        return destinationList;
    }
}
