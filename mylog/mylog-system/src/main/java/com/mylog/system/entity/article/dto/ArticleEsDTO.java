package com.mylog.system.entity.article.dto;

/**
 * @author pss
 * @date 2025/3/12 21:59
 */

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: pss
 * @CreateTime: 2025-03-12
 * @Version: 1.0
 */
@Document(indexName = "article_index")
@Data
public class ArticleEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 文章ID
     */
    private Long ArticleId;
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
     * 摘要
     */
    private String excerpt;
    /**
     * 标签
     */
    private List<String> tags;
    /**
     * 标签ids
     */
    private List<Long> tagsids;
    /**
     * 文章封面地址
     */
    private String cover;

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
     * 是否删除（0否 1是）
     */
    private Integer isDelete;
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
     * 编辑时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date editTime;
    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;
    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;
}
