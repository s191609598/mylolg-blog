package com.mylog.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.category.SysCategory;
import org.apache.ibatis.annotations.Mapper;

/**
* 文章类型表sys_category表持久层接口
*
* @author pss
* @since 2025-02-21 15:16:33
*/
@Mapper
public interface SysCategoryDao extends BaseMapper<SysCategory> {

}
