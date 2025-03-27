package com.mylog.system.entity.comment.vo;

import cn.hutool.core.lang.tree.TreeNode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author pss
 * @date 2025/2/27
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CommentHomeTreeVO extends TreeNode<Long> {

    /**
     * 主键
     */
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
     * 是否置顶
     */
    private Integer isStick;
    /**
     * 父评论ID，用于实现回复评论的层级结构，若为顶级评论则为NULL
     */
    private Long pid;
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
     * 用户名称
     */
    private String createName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否点赞
     */
    private Boolean isLiked;

    private List<CommentHomeTreeVO> children;

    public Long getId() {
        return id;
    }

    public TreeNode<Long> setId(Long id) {
        this.id = id;
        return null;
    }

    public Long getpid() {
        return pid;
    }

    public void setpid(Long pId) {
        this.pid = pId;
    }
}
