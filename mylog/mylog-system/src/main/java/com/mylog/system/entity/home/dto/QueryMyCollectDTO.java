package com.mylog.system.entity.home.dto;

import com.mylog.common.utils.resultutils.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/4/4
 */
@Data
public class QueryMyCollectDTO extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
}
