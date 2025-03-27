package com.mylog.system.entity.article.dto;

import com.mylog.common.utils.resultutils.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryArticleDTO extends PageRequest implements Serializable {
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
     * 文章类型ID
     */
    private Long categoryId;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 摘要
     */
    private String excerpt;
    /**
     * 状态 0公开（无需任何条件就可以查看） 1会员查看（只有登录后才可以查看）  2私密（只有发布人（管理员）可以看到用于刚发布再次确认文章）
     */
    private Integer state;
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
     * 是否轮播图 0否 1是
     */
    private Integer isCarousel;
    /**
     * 阅读数量
     */
    private Integer minReadNum;
    private Integer maxReadNum;
    /**
     * 点赞数量
     */
    private Integer minUpNum;
    private Integer maxUpNum;
    /**
     * 评论数量
     */
    private Integer minCommentNum;
    private Integer maxCommentNum;

}
