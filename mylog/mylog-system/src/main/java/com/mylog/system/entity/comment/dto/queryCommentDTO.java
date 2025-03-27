package com.mylog.system.entity.comment.dto;

import com.mylog.common.utils.resultutils.PageRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author pss
 * @date 2025/3/1
 */
@Data
public class queryCommentDTO extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

}
