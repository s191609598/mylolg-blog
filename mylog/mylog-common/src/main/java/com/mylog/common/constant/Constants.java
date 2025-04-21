package com.mylog.common.constant;

/**
 * 通用常量信息
 *
 * @author pss
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    /**
     * 文章状态 0公开（无需任何条件就可以查看）
     */
    public static final Integer ARTICL_STATUS_0 = 0;
    /**
     * 文章状态 1会员查看（只有登录后才可以查看）
     */
    public static final Integer ARTICL_STATUS_1 = 1;
    /**
     * 文章状态 2私密（只有发布人（管理员）可以看到用于刚发布再次确认文章）
     */
    public static final Integer ARTICL_STATUS_2 = 2;
    /**
     * 排序字段 asc
     */
    public static final String ASC = "asc";
    /**
     * 排序字段 desc
     */
    public static final String DESC = "desc";
    /**
     * 加密盐
     */
    public static final String SALT = "mylog";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 5;
    /**
     * 间隔时间(ms)，小于此时间视为重复提交
     */
    public static final int REPEAT_SUBMIT_INTERVAL = 1000;

    /**
     * 文件平台 minio
     */
    public static final String FILE_PLATFORM_MINIO = "minio";
    /**
     * 文件平台 阿里云
     */
    public static final String FILE_PLATFORM_ALI = "ali";
    /**
     * 文件平台 本地
     */
    public static final String FILE_PLATFORM_LOCAL = "local";
    /**
     * 文件平台 七牛云
     */
    public static final String FILE_PLATFORM_QINIU = "qiniu";
    /**
     * 文件平台 腾讯云
     */
    public static final String FILE_PLATFORM_TENCENT = "tencent";
    /**
     * 代理URL
     */
//    public static final String PROXY_URL = "https://psszxhy.cn/";
    public static final String PROXY_URL = "https://pssapi.cn/";
    /**
     * 存储平台前缀URL
     */
    public static final String PLATFORM_URL = "http://192.168.124.177:9000/";

    // 目标尺寸配置（示例：1200x630 常见文章封面尺寸）
    public static final int TARGET_WIDTH = 1009;
    public static final int TARGET_HEIGHT = 384;

    /**
     * 系统配置表的key
     */
    public static final String MINIO_BUCKETNAME_KEY = "sys.minio.bucketName";
    /**
     * 存储方式
     */
    public static final String SYS_FILE_PLATFORM_TYPE = "sys.file.platform.type";
    /**
     * 备案号
     */
    public static final String SYS_BEIANHAO = "sys.beianhao";

//    /**
//     * 资源映射路径 前缀
//     */
//    public static final String RESOURCE_PREFIX = "/profile";
//
//

//
//    /**
//     * RMI 远程方法调用
//     */
//    public static final String LOOKUP_RMI = "rmi://";
//
//    /**
//     * LDAP 远程方法调用
//     */
//    public static final String LOOKUP_LDAP = "ldap://";
//
//    /**
//     * 定时任务违规的字符
//     */
//    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
//            "org.springframework.jndi"};
}