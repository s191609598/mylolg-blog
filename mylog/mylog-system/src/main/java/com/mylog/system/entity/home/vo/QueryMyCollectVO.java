package com.mylog.system.entity.home.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author pss
 * @date 2025/4/4
 */
@Data
public class QueryMyCollectVO {
    /**
     * 文章ID
     */
    private Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章简介
     */
    private String excerpt;

    @JsonIgnore
    private Date createTime;

}
