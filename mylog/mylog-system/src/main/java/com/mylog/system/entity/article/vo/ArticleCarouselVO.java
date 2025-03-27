package com.mylog.system.entity.article.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/3/7
 */
@Data
public class ArticleCarouselVO implements Serializable {
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
     * 文章类别 0原创 1转载
     */
    private Integer articleType;
    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;
    /**
     * 是否推荐 0否 1是
     */
    private Integer isRecommend;
    /**
     * 轮播排序
     */
    private Integer sort;


}
