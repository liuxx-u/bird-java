package com.bird.core.utils;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuxx on 2017/6/14.
 */
@Component
public class DozerHelper  extends DozerBeanMapper {

    /**
     * 使用dozer进行对象集合之间的转换
     * @param sourceList 源集合
     * @param destinationClass 目标class<T>
     * @param <T>
     * @return 目标集合
     */
    public <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List destinationList = new ArrayList();

        sourceList.forEach(obj -> destinationList.add(map(obj, destinationClass)));
        return destinationList;
    }
}
