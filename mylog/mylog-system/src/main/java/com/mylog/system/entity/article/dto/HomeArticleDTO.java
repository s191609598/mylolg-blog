package com.mylog.system.entity.article.dto;

import com.mylog.common.utils.resultutils.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/2/26
 */
@Data
public class HomeArticleDTO extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 文章类型ID
     */
    private Long categoryId;
    /**
     * 是否显示置顶 false=不显示置顶并且不参与排序 true=显示置顶并且参与排序
     */
    private Boolean showTop;
}
