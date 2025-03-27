package com.mylog.system.entity.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author pss
 * @date 2025/2/27
 */
@Data
public class CommentHomeDTO {
    /**
     * 文章ID
     */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;
    /**
     * 评论内容，使用utf8mb4字符集以支持更多字符类型
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
    /**
     * 父评论ID，用于实现回复评论的层级结构，若为顶级评论则为NULL
     */
    private Long pid;
}
