package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.system.dao.LogErrorDao;

import com.mylog.system.entity.SysLogError;
import com.mylog.system.service.LogErrorService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class LogErrorServiceImpl extends ServiceImpl<LogErrorDao, SysLogError> implements LogErrorService {

    @Async
    @Override
    public void insertErrorlog(SysLogError entity) {
        entity.setCreateDate(new Date());
        this.save(entity);
    }

}