package com.mylog.system.entity.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户注册DTO
 * @author pss
 * @date 2025/2/12
 */
@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    @Size(min = 6, max = 16, message = "账号长度必须在4到16之间")
    private String userAccount;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在4到16之间")
    private String userPassword;
    /**s
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在4到16之间")
    private String checkPassword;
}
