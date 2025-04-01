package com.mylog.common.constant;

/**
 * redis 常量
 * @author pss
 * @date 2025/3/31
 */
public class RedisConstants {
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
     * 验证码
     */
    public static final String REDIS_CAPTCHA = "captcha:";
    /**
     * 验证码计数器
     */
    public static final String REDIS_CAPTCHA_NUM = "captcha_num:";
    /**
     * 封禁
     */
    public static final String REDIS_BAN = "ban:";
    public static final String REDIS_BAN_PREFIX = "ban_prefix:";
    public static final String REDIS_CAPTCHA_NUM_PREFIX = "captcha_num_prefix:";
    public static final String REDIS_IP_LIMIT_PREFIX = "ip_limit_prefix:";
    public static final String REGISTER_ERROR_COUNT_KEY = "register_error_count_key";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";
}
