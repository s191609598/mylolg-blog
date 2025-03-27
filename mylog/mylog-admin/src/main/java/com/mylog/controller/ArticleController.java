package com.mylog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.common.validator.group.AddGroup;
import com.mylog.system.entity.article.dto.EditArticleDTO;
import com.mylog.system.entity.article.dto.QueryArticleDTO;
import com.mylog.system.entity.article.dto.UpdateArticleStatusDTO;
import com.mylog.system.entity.article.vo.ArticleVO;
import com.mylog.system.entity.article.vo.QueryArticleVO;
import com.mylog.system.service.SysArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文章管理
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private SysArticleService sysArticleService;

    /**
     * 新增文章
     *
     * @param dto
     * @return
     */
    @PostMapping("/addarticle")
    public R<Boolean> addArticle(@RequestBody EditArticleDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return R.ok(sysArticleService.addArticle(dto));
    }

    /**
     * 修改文章
     *
     * @param dto
     * @return
     */
    @PostMapping("/updatearticle")
    public R<Boolean> updateArticle(@RequestBody EditArticleDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return R.ok(sysArticleService.updateArticle(dto));
    }

    /**
     * 修改文章状态
     *
     * @param
     * @return
     */
    @PostMapping("/updatearticlestatus")
    public R<Boolean> updateArticleStatus(@RequestBody UpdateArticleStatusDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return R.ok(sysArticleService.updateArticleStatus(dto));
    }

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getarticlebyid")
    public R<ArticleVO> getArticleById(Long id) {
        AssertUtils.isNull(id, "id不能为空");
        return R.ok(sysArticleService.getArticleById(id));
    }

    /**
     * 获取文章列表(后台)
     *
     * @param dto
     * @return
     */
    @PostMapping("/queryarticlelist")
    public R<Page<QueryArticleVO>> queryArticleList(@RequestBody QueryArticleDTO dto) {
        return R.ok(sysArticleService.queryArticleList(dto));
    }

    /**
     * 删除文章
     */
    @PostMapping("/deletearticle")
    public R<Boolean> deleteArticle(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        return R.ok(sysArticleService.deleteArticle(id.getId()));
    }
}