package com.bird.service.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author liuxx
 * @date 2019/8/22
 */
@Getter @Setter
@EqualsAndHashCode
public abstract class StringPurePO implements IPO<String> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
