package com.mylog.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.comment.SysComment;
import org.apache.ibatis.annotations.Mapper;

/**
* 评论表sys_comment表持久层接口
*
* @author pss
* @since 2025-02-21 15:16:33
*/
@Mapper
public interface SysCommentDao extends BaseMapper<SysComment> {

}
