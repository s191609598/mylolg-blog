package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.system.dao.SysConfigDao;
import com.mylog.system.entity.SysConfig;
import com.mylog.system.service.SysConfigService;
import org.springframework.stereotype.Service;

/**
 * @author pss
 * @description 针对表【sys_config(系统参数配置表)】的数据库操作Service实现
 * @createDate 2025-03-31
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig>
        implements SysConfigService {

    @Override
    public SysConfig getConfigByKey(String key) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("configKey", key);
        SysConfig config = this.getOne(queryWrapper);
        return config;
    }
}




