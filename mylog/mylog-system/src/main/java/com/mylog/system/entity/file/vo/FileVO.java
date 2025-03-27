package com.mylog.system.entity.file.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pss
 * @date 2025/2/25
 */
@Data
public class FileVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;
}
