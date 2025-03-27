package com.mylog.system.entity.article.dto;

import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pss
 * @date 2025/2/21
 */
// 编辑文章的数据传输对象
@Data
public class EditArticleDTO {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 文章标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
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
    private String coverId;
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
    @NotNull(message = "文章类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer articleType;
    /**
     * 是否置顶 0否 1是
     */
    @NotNull(message = "是否置顶不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer isTop;
    /**
     * 是否推荐 0否 1是
     */
    @NotNull(message = "是否推荐不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer isRecommend;
    /**
     * 是否轮播图 0否 1是
     */
    @NotNull(message = "是否轮播图不能为空", groups = {AddGroup.class, UpdateGroup.class})
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
     * 标签名称
     */
    private List<String> tags;

}
