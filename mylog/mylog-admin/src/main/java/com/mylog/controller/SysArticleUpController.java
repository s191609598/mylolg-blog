package com.mylog.controller;

import com.mylog.common.annotation.OpLog;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.enums.BusinessType;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.service.SysArticleUpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 文章点赞
 *
 * @author pss
 * @date 2025/4/2
 */
@RestController
@RequestMapping("/articleup")
public class SysArticleUpController {
    @Resource
    private SysArticleUpService sysArticleUpService;

    @OpLog(title = "点赞", businessType = BusinessType.INSERT)
    @RepeatSubmit(interval = 10000)
    @GetMapping("/up")
    public R<Boolean> upArticle(@RequestParam Long articleId) {
        AssertUtils.isNull(articleId, ErrorCode.PARAMS_ERROR);
        Boolean b = sysArticleUpService.upArticle(articleId);
        return R.ok(b);
    }

    @OpLog(title = "取消点赞", businessType = BusinessType.DELETE)
    @RepeatSubmit(interval = 10000)
    @GetMapping("/no")
    public R<Boolean> noArticle(@RequestParam Long articleId) {
        AssertUtils.isNull(articleId, ErrorCode.PARAMS_ERROR);
        Boolean b = sysArticleUpService.noArticle(articleId);
        return R.ok(b);
    }
}
