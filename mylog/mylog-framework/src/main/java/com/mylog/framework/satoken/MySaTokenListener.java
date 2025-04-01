package com.mylog.framework.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.mylog.system.service.SysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author pss
 * @date 2025/3/31
 */
@Component
public class MySaTokenListener implements SaTokenListener {

    @Resource
    private SysUserService sysUserService;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        System.out.println("---------- 自定义侦听器实现 doLogin");
        sysUserService.updateUserLoginDate(Long.valueOf(loginId.toString()));
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doLogout");
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doKickout");
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        System.out.println("---------- 自定义侦听器实现 doReplaced");
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        System.out.println("---------- 自定义侦听器实现 doDisable");
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        System.out.println("---------- 自定义侦听器实现 doUntieDisable");
    }

    /**
     * 每次二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        System.out.println("---------- 自定义侦听器实现 doOpenSafe");
    }

    /**
     * 每次退出二级认证时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        System.out.println("---------- 自定义侦听器实现 doCloseSafe");
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doCreateSession");
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doLogoutSession");
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        System.out.println("---------- 自定义侦听器实现 doRenewTimeout");
    }
}
