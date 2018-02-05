package com.bird.core.utils;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuxx on 2017/6/14.
 */
@Component
public class DozerHelper  extends DozerBeanMapper {

    public <T> List<T> mapList(Collection sourceList, Class<T> destinationClass){
        List destinationList = new ArrayList();
        for (Iterator i$ = sourceList.iterator(); i$.hasNext(); ) { Object sourceObject = i$.next();
            Object destinationObject = map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }
}
