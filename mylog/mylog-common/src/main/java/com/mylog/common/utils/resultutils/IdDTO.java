package com.mylog.common.utils.resultutils;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通用ID请求参数
 * @author pss
 * @date 2025/2/10
 */
@Data
public class IdDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    private static final long serialVersionUID = 1L;
}
