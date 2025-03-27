package com.mylog.system.entity.tag.vo;

/**
 * @author pss
 * @date 2025/3/12 18:22
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: pss
 * @CreateTime: 2025-03-12
 * @Version: 1.0
 */

@Data
public class HomeTagVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 数量
     */
    private Integer num;

}
