package com.mylog.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import com.mylog.system.entity.tag.dto.EditTagDTO;
import com.mylog.system.entity.tag.dto.TagPageListDTO;
import com.mylog.system.entity.tag.vo.TagPageListVO;
import com.mylog.system.service.SysTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private SysTagService sysTagService;

    /**
     * 获取所有标签名称
     *
     * @return
     */
    @SaIgnore
    @GetMapping("/querytagall")
    public R<List<String>> queryTagAll() {
        return R.ok(sysTagService.queryTagAll());
    }

    /**
     * 获取标签分页列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/querytagpagelist")
    public R<IPage<TagPageListVO>> queryTagPageList(@RequestBody TagPageListDTO dto) {
        IPage<TagPageListVO> tagPageListVOIPage = sysTagService.queryTagPageList(dto);
        return R.ok(tagPageListVOIPage);
    }

    /**
     * 添加标签
     *
     * @param dto
     * @return
     */
    @PostMapping("/addtag")
    public R<Boolean> addTag(@RequestBody EditTagDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        sysTagService.addTag(dto);
        return R.ok();
    }

    /**
     * 修改标签
     *
     * @param dto
     * @return
     */
    @PostMapping("/updatetag")
    public R<Boolean> updateTag(@RequestBody EditTagDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        sysTagService.updateTag(dto);
        return R.ok();
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @PostMapping("/deletetagbyid")
    public R<Boolean> deleteTagById(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        sysTagService.deleteById(id.getId());
        return R.ok();
    }
}