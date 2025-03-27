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
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 用户ID
     */
    public static final String REDIS_USERID = "userid:";
    /**
     * 文章评论
     */
    public static final String REDIS_ARTICLE_COMMENT = "article_comment:";
    /**
     * 文章评论索引
     */
    public static final String REDIS_ARTICLE_COMMENT_INDEX = "article_comment_index:";
    /**
     * 文章评论总数
     */
    public static final String REDIS_ARTICLE_COMMENT_TOTAL = "article_comment_total:";
    /**
     * 文章评论根评论数量
     */
    public static final String REDIS_ARTICLE_COMMENT_ROOT_SIZE = "article_comment_root_size:";
    /**
     * 文章
     */
    public static final String REDIS_ARTICLE = "article:";
    /**
     * 文章轮播图
     */
    public static final String REDIS_ARTICLE_CAROUSEL = "article_carousel:";
    /**
     * 文章推荐
     */
    public static final String REDIS_ARTICLE_RECOMMEND = "article_recommend:";
    /**
     * 文章阅读量
     */
    public static final String REDIS_ARTICLE_READ_NUM = "article_read_num:";
    /**
     * 文章点赞量
     */
    public static final String REDIS_ARTICLE_UP_NUM = "article_up_num:";
    /**
     * 文章收藏量
     */
    public static final String REDIS_ARTICLE_COLLECT_NUM = "article_collect_num:";
    /**
     * 标签
     */
    public static final String REDIS_TAG = "tag:";
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

//    /**
//     * 资源映射路径 前缀
//     */
//    public static final String RESOURCE_PREFIX = "/profile";
//
//    /**
//     * 验证码 redis key
//     */
//    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
//
//    /**
//     * 限流 redis key
//     */
//    public static final String RATE_LIMIT_KEY = "rate_limit:";
//
//    /**
//     * 验证码有效期（分钟）
//     */
//    public static final Integer CAPTCHA_EXPIRATION = 2;
//
//    /**
//     * 参数管理 cache key
//     */
//    public static final String SYS_CONFIG_KEY = "sys_config:";
//
//    /**
//     * 字典管理 cache key
//     */
//    public static final String SYS_DICT_KEY = "sys_dict:";
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