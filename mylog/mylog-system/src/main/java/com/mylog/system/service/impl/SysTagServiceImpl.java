package com.mylog.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysArticleTagDao;
import com.mylog.system.dao.SysTagDao;
import com.mylog.system.entity.article.SysArticleTag;
import com.mylog.system.entity.article.vo.TagNumVO;
import com.mylog.system.entity.tag.SysTag;
import com.mylog.system.entity.tag.dto.EditTagDTO;
import com.mylog.system.entity.tag.dto.TagPageListDTO;
import com.mylog.system.entity.tag.vo.HomeTagVO;
import com.mylog.system.entity.tag.vo.TagPageListVO;
import com.mylog.system.redis.RedisCacheUtils;
import com.mylog.system.service.SysArticleTagService;
import com.mylog.system.service.SysTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author pss
 * @description 针对表【sys_tag(标签表)】的数据库操作Service实现
 * @createDate 2025-02-21 15:34:21
 */
@Service
public class SysTagServiceImpl extends ServiceImpl<SysTagDao, SysTag> implements SysTagService {


    @Resource
    RedisCacheUtils redisCacheUtils;

    @Resource
    SysArticleTagService sysArticleTagService;

    @Resource
    SysArticleTagDao sysArticleTagDao;

    /**
     * 根据标签名称获取标签对象  TODO  可以缓存起来  用map结构
     *
     * @param names
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysTag> selectByNames(List<String> names) {
        List<SysTag> tagList = new ArrayList<>();
        AtomicInteger n = new AtomicInteger(0);
        if (CollUtil.isNotEmpty(names)) {
            for (String name : names) {
                QueryWrapper<SysTag> wq = new QueryWrapper<>();
                wq.eq("name", name);
                SysTag sysTag = this.getOne(wq);
                if (StringUtils.isNull(sysTag)) {
                    sysTag = new SysTag();
                    sysTag.setName(name);
                    int sort = baseMapper.getSort();
                    sysTag.setSort(sort + 1);
                    boolean save = this.save(sysTag);
                    AssertUtils.assertIf(!save, ErrorCode.OPERATION_ERROR);
                    n.getAndDecrement();
                }
                tagList.add(sysTag);
            }
            if (n.get() >= 1) {
                redisCacheUtils.deleteObject(Constants.REDIS_TAG);
            }
        }
        return tagList;
    }

    @Override
    public List<String> queryTagAll() {
        List<SysTag> list = this.list();
        if (StringUtils.isNotEmpty(list)) {
            List<String> tags = list.stream().sorted(Comparator.comparingInt(SysTag::getSort)).map(SysTag::getName).collect(Collectors.toList());
            return tags;
        }
        return Collections.emptyList();
    }

    @Override
    public List<HomeTagVO> queryHomeTagAll() {
        List<HomeTagVO> homeTagVOS = null;
        homeTagVOS = redisCacheUtils.getCacheObject(Constants.REDIS_TAG);
        if (StringUtils.isEmpty(homeTagVOS)) {
            List<TagNumVO> tagNum = sysArticleTagDao.getTagNum();
            homeTagVOS = this.queryTagVoAll();
            Map<Long, Integer> map = new HashMap<>(16);
            tagNum.forEach(i -> map.put(i.getTagId(), i.getNum()));
            homeTagVOS.forEach(i -> i.setNum(map.getOrDefault(i.getId(), 0)));
            //排序 根据数量降序  再根据排序升序
            homeTagVOS =  homeTagVOS.stream().sorted(Comparator.comparing(HomeTagVO::getNum).reversed().thenComparing(HomeTagVO::getSort)).collect(Collectors.toList());
            redisCacheUtils.setCacheObject(Constants.REDIS_TAG, homeTagVOS, 60 * 60 * 24, TimeUnit.SECONDS);
        }
        return homeTagVOS;
    }

    @Override
    public List<HomeTagVO> queryTagVoAll() {
        List<HomeTagVO> homeTagVOS = null;
        List<SysTag> list = this.list(new QueryWrapper<SysTag>().orderByDesc("sort"));
        if (StringUtils.isNotEmpty(list)) {
            homeTagVOS = ConvertUtils.sourceToTarget(list, HomeTagVO.class);
        }
        return homeTagVOS;
    }

    @Override
    public IPage<TagPageListVO> queryTagPageList(TagPageListDTO dto) {
        QueryWrapper<SysTag> queryWrapper = this.getQueryWrapper(dto);
        Page<SysTag> iPage = baseMapper.selectPage(new Page<>(dto.getPageNo(), dto.getPageSize()), queryWrapper);
        Page<TagPageListVO> page = new Page<>();
        page.setCurrent(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        page.setSize(iPage.getSize());
        page.setPages(iPage.getPages());
        page.setRecords(ConvertUtils.sourceToTarget(iPage.getRecords(), TagPageListVO.class));
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addTag(EditTagDTO dto) {
        String name = dto.getName();
        SysTag sysTag = ConvertUtils.sourceToTarget(dto, SysTag.class);
        QueryWrapper<SysTag> wq = new QueryWrapper<>();
        wq.eq("name", name);
        SysTag one = this.getOne(wq);
        AssertUtils.assertIf(StringUtils.isNotNull(one), "标签名称已存在");
        sysTag.setCreateTime(new Date());
        sysTag.setUpdateTime(new Date());
        if (StringUtils.isNull(sysTag.getSort())) {
            int sort = baseMapper.getSort();
            sysTag.setSort(sort + 1);
        }
        boolean save = this.save(sysTag);
        if (save) {
            redisCacheUtils.deleteObject(Constants.REDIS_TAG);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTag(EditTagDTO dto) {
        String name = dto.getName();
        Long id = dto.getId();
        SysTag sysTag = ConvertUtils.sourceToTarget(dto, SysTag.class);
        QueryWrapper<SysTag> wq = new QueryWrapper<>();
        wq.ne("id", id);
        wq.eq("name", name);
        SysTag one = this.getOne(wq);
        AssertUtils.assertIf(StringUtils.isNotNull(one), "标签名称已存在");
        sysTag.setUpdateTime(new Date());
        boolean b = this.updateById(sysTag);
        if (b) {
            redisCacheUtils.deleteObject(Constants.REDIS_TAG);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        List<SysArticleTag> list = sysArticleTagService.queryByTagId(id);
        AssertUtils.assertIf(StringUtils.isNotEmpty(list), "该标签已关联文章，无法删除，请先解除关联");
        boolean b = this.removeById(id);
        if (b) {
            redisCacheUtils.deleteObject(Constants.REDIS_TAG);
        }
    }

    /**
     * 条件构造
     *
     * @param dto
     * @return
     */

    public QueryWrapper<SysTag> getQueryWrapper(TagPageListDTO dto) {
        AssertUtils.isNull(dto, ErrorCode.PARAMS_ERROR);
        QueryWrapper<SysTag> queryWrapper = new QueryWrapper<>();
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




