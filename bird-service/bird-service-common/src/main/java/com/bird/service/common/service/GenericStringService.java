package com.bird.service.common.service;

import com.bird.service.common.incrementer.UUIDHexGenerator;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.StringPureModel;
import com.bird.service.common.service.dto.IEntityDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/23
 */
public abstract class GenericStringService<M extends AbstractMapper<T>,T extends StringPureModel> extends GenericService<M,T,String> implements IGenericStringService<T> {


    @Override
    protected Boolean isEmptyKey(String id) {
        return StringUtils.isBlank(id);
    }

    @Override
    public T insert(T record) {
        if (record == null) {
            logger.warn("新增的model信息为null");
            return null;
        }
        record.setId(UUIDHexGenerator.generate());
        return super.insert(record);
    }

    @Override
    public String insert(IEntityDTO<String> dto) {
        if (dto == null) {
            logger.warn("新增的dto信息为null");
            return null;
        }

        dto.setId(UUIDHexGenerator.generate());
        return super.insert(dto);
    }

    @Override
    public boolean insertBatch(List<T> entityList, int batchSize) {
        if(CollectionUtils.isNotEmpty(entityList)){
            entityList.forEach(p->p.setId(UUIDHexGenerator.generate()));
        }
        return super.insertBatch(entityList, batchSize);
    }
}
