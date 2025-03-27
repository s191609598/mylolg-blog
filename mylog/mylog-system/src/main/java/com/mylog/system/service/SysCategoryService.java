package com.mylog.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.category.SysCategory;
import com.mylog.system.entity.category.dto.CategoryPageListDTO;
import com.mylog.system.entity.category.dto.EditCategoryDTO;
import com.mylog.system.entity.category.vo.CategoryPageListVO;
import com.mylog.system.entity.category.vo.CategoryVO;

import java.util.List;

/**
 * @author pss
 * @description 针对表【sys_category(文章类型表)】的数据库操作Service
 * @createDate 2025-02-21 15:34:21
 */
public interface SysCategoryService extends IService<SysCategory> {

    List<CategoryVO> getCategoryAll();

    IPage<CategoryPageListVO> queryCategoryPageList(CategoryPageListDTO dto);

    void addCategory(EditCategoryDTO dto);

    void updateCategory(EditCategoryDTO dto);

    void deleteById(Long id);
}
