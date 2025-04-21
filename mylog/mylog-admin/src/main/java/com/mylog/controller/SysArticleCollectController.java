package com.mylog.controller;

import com.mylog.common.annotation.OpLog;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.enums.BusinessType;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.service.SysArticleCollectService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pss
 * @date 2025/4/2
 */
@RestController
@RequestMapping("/articlecollect")
public class SysArticleCollectController {

    @Resource
    SysArticleCollectService sysArticleCollectService;

    @Transactional(rollbackFor = Exception.class)
    @OpLog(title = "收藏", businessType = BusinessType.INSERT)
//    @RepeatSubmit(interval = 10000)
    @GetMapping("/collectarticle")
    public R<Boolean> collectArticle(@RequestParam Long articleId) {
        AssertUtils.isNull(articleId, ErrorCode.PARAMS_ERROR);
        Boolean b = sysArticleCollectService.collectArticle(articleId);
        return R.ok(b);
    }

    @Transactional(rollbackFor = Exception.class)
    @OpLog(title = "取消收藏", businessType = BusinessType.DELETE)
//    @RepeatSubmit(interval = 10000)
    @GetMapping("/no")
    public R<Boolean> noArticle(@RequestParam Long articleId) {
        AssertUtils.isNull(articleId, ErrorCode.PARAMS_ERROR);
        Boolean b = sysArticleCollectService.noArticle(articleId);
        return R.ok(b);
    }
}
