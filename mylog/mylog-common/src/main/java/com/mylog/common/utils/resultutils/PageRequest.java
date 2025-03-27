package com.mylog.common.utils.resultutils;

import lombok.Data;

/**
 * 通用分页请求参数
 * @author pss
 * @date 2025/2/10
 */
@Data
public class PageRequest {
    /**
     * 默认当前页码
     */
    private int pageNo = 1;
    /**
     * 默认每页显示条数
     */
    private int pageSize = 10;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序方式  desc/asc
     */
    private String sortOrder = "desc";
}
