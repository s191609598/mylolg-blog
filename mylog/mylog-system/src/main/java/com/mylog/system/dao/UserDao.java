package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.user.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户表
 *
 * @author pss
 * @email 360528766@qq.com
 * @date 2022-01-05 17:53:28
 */
@Mapper
public interface UserDao extends BaseMapper<SysUser> {


}
