package com.mylog.system.entity.article.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author pss
 * @date 2025/2/21
 */
@Data
public class QueryArticleVO implements Serializable {
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
     * 文章类型名称
     */
    private String categoryName;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章封面地址
     */
    private String cover;
    /**
     * 摘要
     */
    private String excerpt;
    /**
     * 状态 公开（无需任何条件就可以查看） 会员查看（只有登录后才可以查看）  私密（只有发布人（管理员）可以看到用于刚发布再次确认文章）
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
     * 轮播排序
     */
    private Integer sort;
    /**
     * 转载地址
     */
    private String reprintUrl;
    /**
     * 阅读数量
     */
    private Integer readNum;
    /**
     * 点赞数量
     */
    private Integer upNum;
    /**
     * 评论数量
     */
    private Integer commentNum;
    /**
     * 收藏数量
     */
    private Integer collectNum;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 创建者名称
     */
    private String createByName;
    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 标签
     */
    private List<String> tags;
}
