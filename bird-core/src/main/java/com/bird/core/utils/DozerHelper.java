package com.bird.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author liuxx
 * @date 2017/6/14
 */
public class DozerHelper  extends DozerBeanMapper {

    /**
     * 使用dozer进行对象集合之间的转换
     * @param sourceList 源集合
     * @param destinationClass 目标class<T>
     * @param <T> 泛型参数
     * @return 目标集合
     */
    public <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(sourceList)){
            sourceList.forEach(obj -> destinationList.add(map(obj, destinationClass)));
        }

        return destinationList;
    }
}
