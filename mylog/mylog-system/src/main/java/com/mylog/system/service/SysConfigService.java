package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.SysConfig;

/**
* @author pss
* @description 针对表【sys_config(系统参数配置表)】的数据库操作Service
* @createDate 2025-03-31
*/
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 根据key获取配置
     * @param key
     * @return
     */
    SysConfig getConfigByKey(String key);
}
