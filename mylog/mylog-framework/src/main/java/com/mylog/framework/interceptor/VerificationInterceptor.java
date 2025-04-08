package com.mylog.framework.interceptor;

import com.mylog.common.constant.RedisConstants;
import com.mylog.common.exception.MyException;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.redis.RedisCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 防止刷屏
 *
 * @author pss
 * @date 2025/3/28
 */
@Slf4j
@Component
public class VerificationInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCacheUtils redisCacheUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String useragent = request.getHeader(HttpHeaders.USER_AGENT);

        String ip = IpUtils.getIp(request);

        String address = useragent + "-" + ip;
        boolean exists = redisCacheUtils.exists(RedisConstants.REDIS_BAN + address);
        if (exists) {
            throw new RuntimeException("您的操作过于频繁，请5分钟后再试");
        }

        String banKey = RedisConstants.REDIS_BAN_PREFIX + ip;
        boolean exists1 = redisCacheUtils.exists(banKey);
        if (exists1) {
            throw new MyException("操作过于频繁，请30分钟后再试");
        }
        return true;
    }
}
