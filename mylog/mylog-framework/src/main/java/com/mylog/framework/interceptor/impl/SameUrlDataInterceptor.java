package com.mylog.framework.interceptor.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.filter.RepeatedlyRequestWrapper;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.http.HttpHelper;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.framework.interceptor.RepeatSubmitInterceptor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 默认有效时间为3秒内。
 *
 * @author pss
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    // 令牌自定义标识
    @Value("${sa-token.token-name}")
    private String header;

    @Resource
    RedissonClient redissonClient;


    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper) {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        // body参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSONObject.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();

        StringBuffer submitKey = new StringBuffer();

        // 唯一值
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        if (StringUtils.isNotNull(loginIdDefaultNull)) {
            submitKey.append(loginIdDefaultNull.toString());
            submitKey.append("-");
        } else {
            String header1 = request.getHeader(header);
            if (StringUtils.isNotBlank(header1)) {
                submitKey.append(header1);
                submitKey.append("-");
            }
            String useragent = request.getHeader(HttpHeaders.USER_AGENT);
            if (StringUtils.isNotBlank(useragent)) {
                submitKey.append(useragent);
                submitKey.append("-");
            }
            String ip = IpUtils.getIp(request);
            if (StringUtils.isNotBlank(ip)) {
                submitKey.append(ip);
                submitKey.append("-");
            }
        }
        if (StringUtils.isNotBlank(url)) {
            submitKey.append(url);
            submitKey.append("-");
        }
        // 唯一标识
        String cacheRepeatKey = RedisConstants.REPEAT_SUBMIT_KEY + submitKey;
        RLock lock1 = redissonClient.getLock(cacheRepeatKey);
        try {
            boolean b = lock1.tryLock(0, annotation.interval(), TimeUnit.MILLISECONDS);
            if (!b) {
                return true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < interval) {
            return true;
        }
        return false;
    }
}
