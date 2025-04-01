package com.mylog.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mylog.common.annotation.OpLog;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.constant.Constants;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.enums.BusinessType;
import com.mylog.common.utils.CaptchaUtils;
import com.mylog.common.utils.RedissonUtil;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.system.entity.article.dto.SearchArticleByKeywordDTO;
import com.mylog.system.entity.article.vo.ArticleCarouselVO;
import com.mylog.system.entity.article.vo.HomeArticleVO;
import com.mylog.system.entity.article.vo.RecommendArticleVO;
import com.mylog.system.entity.article.vo.SearchArticleByKeywordVO;
import com.mylog.system.entity.category.vo.CategoryVO;
import com.mylog.system.entity.comment.dto.CommentHomeDTO;
import com.mylog.system.entity.comment.dto.queryCommentDTO;
import com.mylog.system.entity.home.vo.CaptchaVO;
import com.mylog.system.entity.tag.vo.HomeTagVO;
import com.mylog.system.redis.RedisCacheUtils;
import com.mylog.system.service.SysArticleService;
import com.mylog.system.service.SysCategoryService;
import com.mylog.system.service.SysCommentService;
import com.mylog.system.service.SysTagService;
import org.redisson.api.RLock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 首页接口
 *
 * @author pss
 * @date 2025/2/26
 */
@RestController
@RequestMapping("/home")
public class HomeController {


    @Resource
    private SysArticleService sysArticleService;

    @Resource
    private SysTagService sysTagService;

    @Resource
    private SysCommentService sysCommentService;

    @Resource
    private SysCategoryService sysCategoryService;

    @Resource
    private RedissonUtil redissonUtil;

    @Resource
    private RedisCacheUtils redisCacheUtils;


    /**
     * 首页-文章列表
     *
     * @return
     */
//    @SaIgnore
//    @PostMapping("/queryhomearticlelist")
//    public R<IPage<HomeArticleListVO>> queryHomeArticleList(@RequestBody HomeArticleDTO dto) {
//        IPage<HomeArticleListVO> homeArticleVOIPage = sysArticleService.queryHomeArticleList(dto);
//        return R.ok(homeArticleVOIPage);
//    }

    /**
     * 首页-文章详情
     *
     * @param id
     * @return
     */
    @OpLog(title = "首页-文章详情", businessType = BusinessType.QUERY)
    @SaIgnore
    @GetMapping("/gethomearticlebyid")
    public R<HomeArticleVO> getHomeArticleById(Long id) {
        AssertUtils.isNull(id, "id不能为空");
        HomeArticleVO vo = sysArticleService.getHomeArticleById(id);
        return R.ok(vo);
    }

    /**
     * 首页-获取轮播图
     *
     * @return
     */
    @OpLog(title = "首页-获取轮播图", businessType = BusinessType.QUERY)
    @SaIgnore
    @GetMapping("/queryarticlecarouselall")
    public R<List<ArticleCarouselVO>> queryArticleCarouselAll() {
        List<ArticleCarouselVO> articleCarouselVOS = sysArticleService.queryArticleCarouselAll();
        return R.ok(articleCarouselVOS);
    }

    /**
     * 首页-获取推荐文章
     *
     * @return
     */
    @OpLog(title = "首页-获取推荐文章", businessType = BusinessType.QUERY)
    @SaIgnore
    @GetMapping("/queryrecommendarticle")
    public R<List<RecommendArticleVO>> queryRecommendArticle() {
        List<RecommendArticleVO> recommendArticleVOS = sysArticleService.queryRecommendArticle();
        return R.ok(recommendArticleVOS);
    }

    /**
     * 首页-搜索文章
     *
     * @param dto
     * @return
     */
//    @SaIgnore
//    @PostMapping("/searcharticlebykeyword")
//    public R<IPage<SearchArticleByKeywordVO>> searchArticleByKeyword(@RequestBody SearchArticleByKeywordDTO dto) {
//        IPage<SearchArticleByKeywordVO> searchArticleByKeywordVOIPage = sysArticleService.searchArticleByKeyword(dto);
//        return R.ok(searchArticleByKeywordVOIPage);
//    }

    /**
     * 首页-ES搜索文章
     *
     * @param dto
     * @return
     */
    @OpLog(title = "首页-ES搜索文章", businessType = BusinessType.QUERY)
    @SaIgnore
    @PostMapping("/searchfromes")
    public R<IPage<SearchArticleByKeywordVO>> searchFromEs(@RequestBody SearchArticleByKeywordDTO dto) {
        IPage<SearchArticleByKeywordVO> searchArticleByKeywordVOIPage = sysArticleService.searchFromEs(dto);
        return R.ok(searchArticleByKeywordVOIPage);
    }

    /**
     * 首页-查询所有标签
     *
     * @return
     */
    @OpLog(title = "首页-查询所有标签", businessType = BusinessType.QUERY)
    @SaIgnore
    @GetMapping("/queryhometagall")
    public R<List<HomeTagVO>> queryHomeTagAll() {
        List<HomeTagVO> tagAll = sysTagService.queryHomeTagAll();
        return R.ok(tagAll);
    }

    /**
     * 首页文章详情页-提交评论
     *
     * @param dto
     * @return
     */
    @OpLog(title = "首页文章详情页-提交评论", businessType = BusinessType.INSERT)
    @PostMapping("/submitcomment")
    public R submitComment(@RequestBody CommentHomeDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return R.ok(sysCommentService.submitComment(dto));
    }

    /**
     * 首页文章详情页-查询评论
     *
     * @param dto
     * @return
     */
    @OpLog(title = "首页文章详情页-查询评论", businessType = BusinessType.QUERY)
    @SaIgnore
    @PostMapping("/querycommentbyarticleid")
    public R<IPage<Tree<Long>>> queryCommentByArticleId(@RequestBody queryCommentDTO dto) {
        ValidatorUtils.validateEntity(dto);
        AssertUtils.assertIf(dto.getPageSize() != 5, ErrorCode.PARAMS_ERROR);
        String key = dto.getPageNo() + ":" + dto.getArticleId();
        RLock lock = redissonUtil.lock(key);
        IPage<Tree<Long>> treeIPage = null;
        if (lock.isLocked()) {
            treeIPage = sysCommentService.queryByArticleId(dto);
        }
        redissonUtil.isUnlock(lock);
        return R.ok(treeIPage);
    }


    /**
     * 首页-获取所有文章类型
     *
     * @return
     */
    @OpLog(title = "首页-获取所有文章类型", businessType = BusinessType.QUERY)
    @SaIgnore
    @GetMapping("/gethomecategoryall")
    public R<List<CategoryVO>> getHomeCategoryAll() {
        return R.ok(sysCategoryService.getCategoryAll());
    }

    @RepeatSubmit
    @OpLog(title = "获取验证码", businessType = BusinessType.OTHER)
    @SaIgnore
    @GetMapping("/getcaptcha")
    public R<CaptchaVO> getCaptcha(HttpServletRequest request) {
        String useragent = request.getHeader(HttpHeaders.USER_AGENT);
        String ip = IpUtils.getIp(request);
        String key = useragent + ip;
        RLock lock = redissonUtil.lock(key);
        CaptchaVO vo = new CaptchaVO();
        if (lock.isLocked()) {
            GifCaptcha captcha = CaptchaUtils.getGifCaptcha();
            String captchaCode = captcha.getCode();
            String captchaKey = UUID.randomUUID().toString(true);
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_CAPTCHA + captchaKey, captchaCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            vo.setCaptchaKey(captchaKey);
            vo.setCaptchaImg(captcha.getImageBase64Data());
        }
        redissonUtil.isUnlock(lock);
        return R.ok(vo);
    }


    // 查看实际接收的请求头
    @SaIgnore
    @GetMapping("/debug")
    public ResponseEntity<?> debugHeaders(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(headers);
    }
}
