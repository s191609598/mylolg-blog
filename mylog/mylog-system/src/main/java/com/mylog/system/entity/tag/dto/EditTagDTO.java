package com.mylog.system.entity.tag.dto;

import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author pss
 * @date 2025/3/20
 */
@Data
public class EditTagDTO {
    @NotNull(message = "id不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {UpdateGroup.class})
    private Integer sort;
}
