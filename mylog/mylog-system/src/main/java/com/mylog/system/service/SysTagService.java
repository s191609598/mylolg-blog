package com.mylog.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.tag.SysTag;
import com.mylog.system.entity.tag.dto.EditTagDTO;
import com.mylog.system.entity.tag.dto.TagPageListDTO;
import com.mylog.system.entity.tag.vo.HomeTagVO;
import com.mylog.system.entity.tag.vo.TagPageListVO;

import java.util.List;

/**
 * @author pss
 * @description 针对表【sys_tag(标签表)】的数据库操作Service
 * @createDate 2025-02-21 15:34:21
 */
public interface SysTagService extends IService<SysTag> {
    /**
     * 根据标签名称获取标签对象  TODO  可以缓存起来  用map结构
     *
     * @param names
     * @return
     */
    List<SysTag> selectByNames(List<String> names);


    List<String> queryTagAll();

    List<HomeTagVO> queryHomeTagAll();
    List<HomeTagVO> queryTagVoAll();

    IPage<TagPageListVO> queryTagPageList(TagPageListDTO dto);

    void addTag(EditTagDTO dto);

    void updateTag(EditTagDTO dto);

    void deleteById(Long id);
}
