
package com.mylog.system.entity.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 评论表
 *
 * @author pss
 * @since 2025-02-21 15:16:33
 */
@Data
@TableName("sys_comment")
public class SysComment {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 评论内容，使用utf8mb4字符集以支持更多字符类型
     */
    private String content;
    /**
     * 点赞数，记录该评论获得的点赞数量
     */
    private Integer upNum;
    /**
     * 是否置顶 0否 1是
     */
    private Integer isStick;
    /**
     * 父评论ID，用于实现回复评论的层级结构，若为顶级评论则为NULL
     */
    private Long pid;
    /**
     * 根ID （第一个父节点的ID）
     */
    private Long rootId;
    /**
     * ip
     */
    private String ip;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 系统
     */
    private String os;
    /**
     * ip来源
     */
    private String ipSource;
    /**
     * 发表评论的用户ID
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
}