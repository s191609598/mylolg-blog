package com.mylog.system.entity.article.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pss
 * @date 2025/2/21
 */
@Data
public class UpdateArticleStatusDTO {
    @NotNull(message = "id不能为空")
    private Long id;
    @NotNull(message = "状态不能为空")
    private Integer status;
}
