package com.mylog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mylog.common.annotation.OpLog;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.enums.BusinessType;
import com.mylog.common.exception.MyException;
import com.mylog.common.utils.redis.RedissonUtil;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import com.mylog.system.entity.user.dto.*;
import com.mylog.system.entity.user.vo.QueryUserVO;
import com.mylog.system.entity.user.vo.UserVO;
import com.mylog.common.utils.redis.RedisCacheUtils;
import com.mylog.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Pss
 * @date 2022/3/22 17:12
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RedissonUtil redissonUtil;

    @Resource
    private RedisCacheUtils redisCacheUtils;


    /**
     * 登录
     *
     * @returni
     */
    @OpLog(title = "登录", businessType = BusinessType.OTHER)
    @RepeatSubmit
    @PostMapping("/login")
    public R<SaResult> login(@RequestBody UserLoginDTO dto) {
        ValidatorUtils.validateEntity(dto);
        String key = dto.getUserAccount();
        RLock lock = redissonUtil.lock(key);
        SaResult login = null;
        if (lock.isLocked()) {
            login = sysUserService.login(dto);
        }
        redissonUtil.isUnlock(lock);
        return R.ok(login);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @OpLog(title = "退出登录", businessType = BusinessType.OTHER)
    @RepeatSubmit
    @PostMapping("/logout")
    public R logout() {
        sysUserService.logout();
        return R.ok();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @RepeatSubmit
    @OpLog(title = "获取用户信息", businessType = BusinessType.QUERY)
    @GetMapping("/getuserinfo")
    public R<UserVO> getUserInfo() {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        if (ObjUtil.isNull(loginIdDefaultNull)) {
            return R.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        UserVO userInfo = sysUserService.getUserInfo(userId);
        return R.ok(userInfo);
    }

    /**
     * 用户注册
     *
     * @param dto
     * @return
     */

    @OpLog(title = "用户注册", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @SaIgnore
    @PostMapping("/userregister")
    @Transactional(rollbackFor = Exception.class)
    public R<Long> userRegister(@RequestBody UserRegisterDTO dto, HttpServletRequest request) {
        // 在方法开始处添加
        log.info("REGISTER_ATTEMPT", "account:{}, ip:{}", dto.getUserAccount(), IpUtils.getIp(request));
        //参数校验
        ValidatorUtils.validateEntity(dto);
        AssertUtils.assertIf(!dto.getUserPassword().equals(dto.getCheckPassword()), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        //验证码校验
        String captchaCode = dto.getCaptchaCode();
        String captchaKey = dto.getCaptchaKey();
        String captcha = redisCacheUtils.getCacheObject(RedisConstants.REDIS_CAPTCHA + captchaKey);
        if (StringUtils.isEmpty(captcha)) {
            return R.error("验证码错误，请刷新页面");
        }
        //忽略大小写
        if (!captchaCode.equalsIgnoreCase(captcha)) {
            //记录验证码错误次数
            this.captchaCounter(captchaKey, request);
            this.handleFailedCaptcha(captchaKey, request);
            return R.error("验证码错误");
        }
        //删除验证码
        Set<String> keys = new HashSet<>();
        keys.add(RedisConstants.REDIS_CAPTCHA + captchaKey);
        keys.add(RedisConstants.REDIS_CAPTCHA_NUM + captchaKey);
        redisCacheUtils.deleteObject(keys);


        // 用户注册
        String key = String.format("register:%s", dto.getUserAccount());
        RLock lock = redissonUtil.lock(key);
        try {
            if (lock.isLocked()) {
                Long register = sysUserService.register(dto);
                log.info("REGISTER_RESULT", "account:{}, result:{}", dto.getUserAccount(), register != null ? "success" : "fail");
            }
            redissonUtil.isUnlock(lock);
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            log.error("REGISTER_ERROR", e);
            redisCacheUtils.incrementValue(RedisConstants.REGISTER_ERROR_COUNT_KEY);
            throw new MyException("注册服务暂时不可用，请稍后再试");
        } finally {
            redissonUtil.isUnlock(lock);
        }
        return R.ok();
    }

    /**
     * 门户网站用户修改信息
     *
     * @param dto
     * @return
     */
    @OpLog(title = "首页-用户修改信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/updateuser")
    public R<Boolean> updateUser(@RequestBody UpdateUserDTO dto) {
        ValidatorUtils.validateEntity(dto);
        //获取当前登录用户
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, "请先登录");
        Long id = Long.valueOf(loginIdDefaultNull.toString());
        dto.setId(id);
        String key = dto.getId().toString();
        RLock lock = redissonUtil.lock(key);
        boolean b = false;
        if (lock.isLocked()) {
            b = sysUserService.updateUser(dto);
        }
        redissonUtil.isUnlock(lock);
        return R.ok(b);
    }

    /**
     * 后台查询用户列表
     *
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/queryuserlist")
    public R<Page<QueryUserVO>> queryUserList(@RequestBody UserQueryDTO dto) {
        Page<QueryUserVO> queryUserVOPage = sysUserService.queryUserList(dto);
        return R.ok(queryUserVOPage);
    }

    /**
     * 后台新增用户
     *
     * @param dto
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/adduser")
    public R<Boolean> addUser(@RequestBody EditUserDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return R.ok(sysUserService.adduser(dto));
    }


    /**
     * 后台修改用户
     *
     * @param dto
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/edituser")
    public R<Boolean> editUser(@RequestBody EditUserDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        return R.ok(sysUserService.editUser(dto));
    }

    /**
     * 后台删除用户
     *
     * @param id
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/deleteuser")
    public R<Boolean> deleteUser(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        return R.ok(sysUserService.removeById(id.getId()));
    }

    /**
     * 验证码错误次数过多，封禁5分钟
     *
     * @param captchaKey
     * @param request
     */
    public void captchaCounter(String captchaKey, HttpServletRequest request) {
        long num = redisCacheUtils.incrementValue(RedisConstants.REDIS_CAPTCHA_NUM + captchaKey);
        if (num >= 10) {
            String useragent = request.getHeader(HttpHeaders.USER_AGENT);
            String ip = IpUtils.getIp(request);
            String address = useragent + "-" + ip;
            //错误次数过多，封禁5分钟
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_BAN + address, 0, 5, TimeUnit.MINUTES);
            throw new MyException("验证码错误次数过多，请稍后再试");
        }
    }

    /**
     * 防爆破
     *
     * @param captchaKey
     * @param request
     */
    private void handleFailedCaptcha(String captchaKey, HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        String counterKey = RedisConstants.REDIS_CAPTCHA_NUM_PREFIX + captchaKey;
        long retryCount = redisCacheUtils.incrementValue(counterKey);
        // 设置计数器过期时间
        if (retryCount == 1) {
            redisCacheUtils.expire(counterKey, 5, TimeUnit.MINUTES);
        }
        // IP维度限制
        String ipLimitKey = RedisConstants.REDIS_IP_LIMIT_PREFIX + ip;
        long ipCount = redisCacheUtils.incrementValue(ipLimitKey);
        if (ipCount == 1) {
            redisCacheUtils.expire(ipLimitKey, 1, TimeUnit.HOURS);
        }
        // 双重限制策略
        if (retryCount >= 10 || ipCount >= 20) {
            String banKey = RedisConstants.REDIS_BAN_PREFIX + ip;
            redisCacheUtils.setCacheObject(banKey, 1, 30, TimeUnit.MINUTES);
            log.warn("REGISTER_BANNED", "IP:{}, Retry:{}, IPCount:{}", ip, retryCount, ipCount);
            throw new MyException("操作过于频繁，请30分钟后再试");
        }
    }

}
