package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mylog.system.entity.SysLogError;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常日志
 *
 * @author pss
 * @email 360528766@qq.com
 * @date 2022-01-11 10:59:46
 */
@Mapper
public interface LogErrorDao extends BaseMapper<SysLogError> {

}
