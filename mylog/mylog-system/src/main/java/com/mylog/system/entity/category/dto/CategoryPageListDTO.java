package com.mylog.system.entity.category.dto;

import com.mylog.common.utils.resultutils.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/3/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryPageListDTO extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 名称
     */
    private String name;
}
