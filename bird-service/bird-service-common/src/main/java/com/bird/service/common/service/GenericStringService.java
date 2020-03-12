package com.bird.service.common.service;

import com.bird.service.common.incrementer.UUIDHexGenerator;
import com.bird.service.common.mapper.AbstractMapper;
import com.bird.service.common.model.StringPureModel;
import com.bird.service.common.service.dto.IEntityDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

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
        if(isEmptyKey(record.getId())){
            record.setId(UUIDHexGenerator.generate());
        }
        return super.insert(record);
    }

    @Override
    public String insert(IEntityDTO<String> dto) {
        if (dto == null) {
            logger.warn("新增的dto信息为null");
            return null;
        }
        if(isEmptyKey(dto.getId())){
            dto.setId(UUIDHexGenerator.generate());
        }
        return super.insert(dto);
    }

    @Override
    public boolean insertBatch(Collection<T> entityList, int batchSize) {
        this.initModelIds(entityList);
        return super.insertBatch(entityList, batchSize);
    }

    /**
     * 批量生成id
     *
     * @param models {@link Collection<T>}
     */
    protected void initModelIds(Collection<T> models) {
        if (CollectionUtils.isNotEmpty(models)) {
            models.forEach(p -> p.setId(UUIDHexGenerator.generate()));
        }
    }

    /**
     * 批量生成id
     *
     * @param dtos {@link Collection<IEntityDTO>}
     */
    protected void initDtoIds(Collection<IEntityDTO<String>> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(p -> p.setId(UUIDHexGenerator.generate()));
        }
    }
}
