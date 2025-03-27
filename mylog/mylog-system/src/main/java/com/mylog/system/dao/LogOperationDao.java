package com.mylog.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mylog.system.entity.SysLogOperation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 *
 * @author pss
 * @email 360528766@qq.com
 */
@Mapper
public interface LogOperationDao extends BaseMapper<SysLogOperation> {

}
