package com.mylog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import com.mylog.system.entity.category.dto.CategoryPageListDTO;
import com.mylog.system.entity.category.dto.EditCategoryDTO;
import com.mylog.system.entity.category.vo.CategoryPageListVO;
import com.mylog.system.entity.category.vo.CategoryVO;
import com.mylog.system.service.SysCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章类型
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private SysCategoryService sysCategoryService;

    /**
     * 获取所有文章类型
     *
     * @return
     */
    @GetMapping("/querycategoryall")
    public R<List<CategoryVO>> getCategoryAll() {
        return R.ok(sysCategoryService.getCategoryAll());
    }


    /**
     * 获取标签分页列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/queryCategorypagelist")
    public R<IPage<CategoryPageListVO>> queryCategoryPageList(@RequestBody CategoryPageListDTO dto) {
        IPage<CategoryPageListVO> CategoryPageListVOIPage = sysCategoryService.queryCategoryPageList(dto);
        return R.ok(CategoryPageListVOIPage);
    }

    /**
     * 添加标签
     *
     * @param dto
     * @return
     */
    @PostMapping("/addCategory")
    public R<Boolean> addCategory(@RequestBody EditCategoryDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        sysCategoryService.addCategory(dto);
        return R.ok();
    }

    /**
     * 修改标签
     *
     * @param dto
     * @return
     */
    @PostMapping("/updateCategory")
    public R<Boolean> updateCategory(@RequestBody EditCategoryDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        sysCategoryService.updateCategory(dto);
        return R.ok();
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @PostMapping("/deletecategorybyid")
    public R<Boolean> deleteCategoryById(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        sysCategoryService.deleteById(id.getId());
        return R.ok();
    }
}