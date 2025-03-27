package com.mylog.system.entity.user.dto;

/**
 * @author pss
 * @date 2025/2/6 21:02
 */

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *@Author: pss
 *@CreateTime: 2025-02-06
 *@Version: 1.0
 */

@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 6, max = 16, message = "账号长度必须在4到16之间")
    private String userAccount;
    /**
     * 密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在4到16之间")
    private String userPassword;


}
