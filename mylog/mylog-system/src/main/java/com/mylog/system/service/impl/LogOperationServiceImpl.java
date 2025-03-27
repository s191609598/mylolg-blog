package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.system.dao.LogOperationDao;
import com.mylog.system.entity.SysLogOperation;
import com.mylog.system.service.LogOperationService;
import org.springframework.stereotype.Service;


@Service
public class LogOperationServiceImpl extends ServiceImpl<LogOperationDao, SysLogOperation> implements LogOperationService {


}