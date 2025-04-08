package com.mylog.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 异常日志
 *
 * @author pss
 * @email 360528766@qq.com
 * @date 2022-01-11 10:59:46
 */
@Data
@TableName("sys_error_log")
public class SysErrorLog {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 操作IP
     */
    private String ip;
    /**
     * 异常信息
     */
    private String errorInfo;
    /**
     * 业务类型（0查询 1新增 2修改 3删除 4其他）
     */
    private Integer businessType;

    /**
     * IP地点
     */
    private String operLocation;
    /**
     * 异常类型 0=请求方式异常 1=业务异常 2=未知运行时异常 3=系统异常 4=token异常
     */
    private Integer errorType;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建者
     */
    private Long createBy;

}
