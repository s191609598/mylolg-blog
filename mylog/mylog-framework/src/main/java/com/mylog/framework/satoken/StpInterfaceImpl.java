package com.mylog.framework.satoken;

/**
 * @author pss
 * @date 2025/2/6 23:25
 */

import cn.dev33.satoken.stp.StpInterface;
import com.mylog.system.entity.user.SysUser;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sa-token获取用户权限实现类
 *
 * @Author: pss
 * @CreateTime: 2025-02-06
 * @Version: 1.0
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysUserService sysUserService;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();
        SysUser userById = sysUserService.getUserById(Long.valueOf(loginId.toString()));
        list.add(userById.getUserRole());
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<>();
        SysUser userById = sysUserService.getUserById(Long.valueOf(loginId.toString()));
        list.add(userById.getUserRole());
        return list;
    }

}
