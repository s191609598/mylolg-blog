package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.system.dao.SysErrorLogDao;
import com.mylog.system.entity.SysErrorLog;
import com.mylog.system.service.SysErrorLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SysErrorSysLogServiceImpl extends ServiceImpl<SysErrorLogDao, SysErrorLog> implements SysErrorLogService {

    @Async
    @Override
    public void insertErrorlog(SysErrorLog entity) {
        entity.setCreateDate(new Date());
        this.save(entity);
    }

}