package com.bird.service.common.service.query;

import com.bird.service.common.service.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuxx
 * @date 2018/11/6
 */
@Getter
@Setter
public class ColumnInfo extends AbstractDTO {
    private String field;
    private String name;
    private String type;
    private String sourceUrl;
}
