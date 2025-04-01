package com.mylog.system.entity.home.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/3/28
 */
@Data
public class CaptchaVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 验证码key
     */
    private String captchaKey;
    /**
     * 验证码图片
     */
    private String captchaImg;
}
