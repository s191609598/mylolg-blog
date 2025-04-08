package com.mylog.system.entity.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author pss
 * @date 2025/2/12
 */
@Data
public class UpdateUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 备注
     */
    private String remark;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private Integer sex;
}
