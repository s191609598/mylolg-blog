package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.SysErrorLog;

/**
 * 异常日志
 *
 * @author pss
 * @email 360528766@qq.com
 * @date 2022-01-11 10:59:46
 */
public interface SysErrorLogService extends IService<SysErrorLog> {


    void insertErrorlog(SysErrorLog entity);
}

