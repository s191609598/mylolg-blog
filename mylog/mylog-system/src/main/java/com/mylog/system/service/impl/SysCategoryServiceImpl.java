package com.mylog.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleDao;
import com.mylog.system.dao.SysCategoryDao;
import com.mylog.system.entity.article.SysArticle;
import com.mylog.system.entity.category.SysCategory;
import com.mylog.system.entity.category.dto.CategoryPageListDTO;
import com.mylog.system.entity.category.dto.EditCategoryDTO;
import com.mylog.system.entity.category.vo.CategoryPageListVO;
import com.mylog.system.entity.category.vo.CategoryVO;
import com.mylog.system.service.SysCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pss
 * @description 针对表【sys_category(文章类型表)】的数据库操作Service实现
 * @createDate 2025-02-21 15:34:21
 */
@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryDao, SysCategory> implements SysCategoryService {

    @Resource
    SysArticleDao sysArticleDao;

    @Override
    public List<CategoryVO> getCategoryAll() {
        List<SysCategory> list = this.list();
        if (StringUtils.isNotEmpty(list)) {
            List<SysCategory> collect = list.stream().sorted(Comparator.comparingInt(SysCategory::getSort)).collect(Collectors.toList());
            List<CategoryVO> categoryVOS = ConvertUtils.sourceToTarget(collect, CategoryVO.class);
            return categoryVOS;
        }
        return Collections.emptyList();
    }

    @Override
    public IPage<CategoryPageListVO> queryCategoryPageList(CategoryPageListDTO dto) {
        QueryWrapper<SysCategory> queryWrapper = this.getQueryWrapper(dto);
        Page<SysCategory> iPage = baseMapper.selectPage(new Page<>(dto.getPageNo(), dto.getPageSize()), queryWrapper);
        Page<CategoryPageListVO> page = new Page<>();
        page.setCurrent(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        page.setSize(iPage.getSize());
        page.setPages(iPage.getPages());
        page.setRecords(ConvertUtils.sourceToTarget(iPage.getRecords(), CategoryPageListVO.class));
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCategory(EditCategoryDTO dto) {
        String name = dto.getName();
        SysCategory sysCategory = ConvertUtils.sourceToTarget(dto, SysCategory.class);
        QueryWrapper<SysCategory> wq = new QueryWrapper<>();
        wq.eq("name", name);
        SysCategory one = this.getOne(wq);
        AssertUtils.assertIf(StringUtils.isNotNull(one), "类型名称已存在");
        sysCategory.setCreateTime(new Date());
        sysCategory.setUpdateTime(new Date());
        this.save(sysCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCategory(EditCategoryDTO dto) {
        String name = dto.getName();
        Long id = dto.getId();
        SysCategory sysCategory = ConvertUtils.sourceToTarget(dto, SysCategory.class);
        QueryWrapper<SysCategory> wq = new QueryWrapper<>();
        wq.ne("id", id);
        wq.eq("name", name);
        SysCategory one = this.getOne(wq);
        AssertUtils.assertIf(StringUtils.isNotNull(one), "标签名称已存在");
        sysCategory.setUpdateTime(new Date());
        this.updateById(sysCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        QueryWrapper<SysArticle> wq = new QueryWrapper();
        wq.eq("categoryId", id);
        List<SysArticle> list = sysArticleDao.selectList(wq);
        AssertUtils.assertIf(StringUtils.isNotEmpty(list), "该类型已关联文章，无法删除，请先解除关联");
        this.removeById(id);
    }

    /**
     * 条件构造
     *
     * @param dto
     * @return
     */

    public QueryWrapper<SysCategory> getQueryWrapper(CategoryPageListDTO dto) {
        AssertUtils.isNull(dto, ErrorCode.PARAMS_ERROR);
        QueryWrapper<SysCategory> queryWrapper = new QueryWrapper<>();
        Long id = dto.getId();
        String name = dto.getName();
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), StringUtils.equals(sortOrder, Constants.ASC), sortField);
        return queryWrapper;
    }
}




