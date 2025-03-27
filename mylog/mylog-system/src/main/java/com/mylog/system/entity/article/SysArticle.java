package com.mylog.system.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 文章表
 *
 * @author pss
 * @since 2025-02-21 15:16:33
 */
@Data
@TableName("sys_article")
public class SysArticle {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 文章封面地址
     */
    private String cover;
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
     * 是否删除（0否 1是）
     */
    @TableLogic(value = "0", delval = "1")
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
     * 修改者
     */
    private Long updateBy;
    /**
     * 编辑时间
     */
    private Date editTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}