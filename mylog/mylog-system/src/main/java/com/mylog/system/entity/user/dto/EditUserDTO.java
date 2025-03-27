package com.mylog.system.entity.user.dto;

import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增修改用户DTO
 *
 * @author pss
 * @date 2025/2/10
 */
@Data
public class EditUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
    private Long id;
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private String userAccount;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class})
    private String userPassword;
    /**
     * 微信开放平台id
     */
    private String unionId;
    /**
     * 公众号openId
     */
    private String mpOpenId;
    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空", groups = {UpdateGroup.class, AddGroup.class})
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
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
}
