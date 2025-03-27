package com.mylog.system.entity.comment.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/3/14
 */
@Data
public class CommentPreventDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long articleId;
    /**
     * 默认当前页码
     */
    private Integer pageNo;
    /**
     * 默认每页显示条数
     */
    private Integer pageSize;

    private Integer preventSize;
}
