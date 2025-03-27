package com.mylog.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.comment.SysComment;
import com.mylog.system.entity.comment.dto.CommentHomeDTO;
import com.mylog.system.entity.comment.dto.queryCommentDTO;

/**
 * @author pss
 * @description 针对表【sys_comment(评论表)】的数据库操作Service
 * @createDate 2025-02-21 15:34:21
 */
public interface SysCommentService extends IService<SysComment> {

    boolean submitComment(CommentHomeDTO dto);

    IPage<Tree<Long>> queryByArticleId(queryCommentDTO dto);

    Integer queryByArticleIdCount(Long articleId);

}
