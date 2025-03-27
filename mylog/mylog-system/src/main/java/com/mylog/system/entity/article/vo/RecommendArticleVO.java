package com.mylog.system.entity.article.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pss
 * @date 2025/3/20
 */
@Data
public class RecommendArticleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章封面地址
     */
    private String cover;
    /**
     * 是否推荐 0否 1是
     */
    private Integer isRecommend;
    /**
     * 轮播排序
     */
    private Integer sort;
    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date editTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;
}
