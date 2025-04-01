package com.mylog.common.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.LineCaptcha;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码工具类
 *
 * @author pss
 * @date 2025/3/28
 */
public class CaptchaUtils {

    /**
     * 验证码默认宽度
     */
    public static final Integer WIDTH = 200;
    /**
     * 验证码默认高度
     */
    public static final Integer HEIGHT = 100;

    /**
     * 获得线段干扰的验证码
     *
     * @param response
     * @return
     */
    public static LineCaptcha getLineCaptcha(HttpServletResponse response) {
        return getLineCaptcha(response, WIDTH, HEIGHT);
    }


    /**
     * 获得线段干扰的验证码
     *
     * @param response
     * @param width    宽度
     * @param height   高度
     * @return
     */
    public static LineCaptcha getLineCaptcha(HttpServletResponse response, Integer width, Integer height) {
        //定义图形验证码的长和宽
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height);
        try {
            captcha.write(response.getOutputStream());
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return captcha;
    }

    /**
     * 获得gif验证码
     * @param response
     * @return
     */

    public static GifCaptcha getGifCaptcha(HttpServletResponse response) {
        return getGifCaptcha(response, WIDTH, HEIGHT);
    }

    /**
     * 获得gif验证码
     * @param response
     * @param width
     * @param height
     * @return
     */
    public static GifCaptcha getGifCaptcha(HttpServletResponse response, Integer width, Integer height) {
        // 定义图形验证码的长和宽
        GifCaptcha captcha = CaptchaUtil.createGifCaptcha(width, height);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        try {
            captcha.write(response.getOutputStream());
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return captcha;
    }

    public static GifCaptcha getGifCaptcha() {
        // 定义图形验证码的长和宽
        return CaptchaUtil.createGifCaptcha(WIDTH, HEIGHT);
    }

    public static GifCaptcha getGifCaptcha(Integer width, Integer height) {
        // 定义图形验证码的长和宽
        return CaptchaUtil.createGifCaptcha(width, height);
    }


//    public static void main(String[] args) {
//        //定义图形验证码的长和宽
//        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
//        //图形验证码写出，可以写出到文件，也可以写出到流
//        lineCaptcha.write("d:/line.png");
//        //输出code
//        Console.log(lineCaptcha.getCode());
//        //验证图形验证码的有效性，返回boolean值
//        System.out.println(lineCaptcha.verify("1234"));
//        System.out.println(lineCaptcha.verify(lineCaptcha.getCode()));
//    }
}
