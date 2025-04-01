package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.tag.SysTag;
import org.apache.ibatis.annotations.Mapper;

/**
* 标签表sys_tag表持久层接口
*
* @author pss
* @since 2025-02-21 15:16:33
*/
@Mapper
public interface SysTagDao extends BaseMapper<SysTag> {
    Integer getSort();


}
