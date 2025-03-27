package com.mylog.system.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.user.SysUser;
import com.mylog.system.entity.user.dto.*;
import com.mylog.system.entity.user.vo.QueryUserVO;
import com.mylog.system.entity.user.vo.UserVO;

/**
 * 系统用户表
 *
 * @author pss
 * @email 360528766@qq.com
 * @date 2022-01-05 17:53:28
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 获得加密后的密码
     *
     * @param password
     * @return
     */
    String getEncryptedPassword(String password);


    /**
     * 用户注册
     *
     * @param dto
     * @return
     */
    Long register(UserRegisterDTO dto);

    /**
     * 登录
     *
     * @param dto
     */
    SaResult login(UserLoginDTO dto);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UserVO getUserInfo(Long id);

    /**
     * 获取用户信息（内部调用）
     * @param id
     * @return
     */
    SysUser getUserById(Long id);

    /**
     * 门户更新用户信息
     *
     * @param dto
     * @return
     */
    boolean updateUser(UpdateUserDTO dto);
    /**
     * 后台查询用户列表
     *
     * @param dto
     * @return
     */
    Page<QueryUserVO> queryUserList(UserQueryDTO dto);
    /**
     * 后台新增用户
     *
     * @param dto
     * @return
     */
    boolean adduser(EditUserDTO dto);
    /**
     * 后台修改用户
     *
     * @param dto
     * @return
     */
    boolean editUser(EditUserDTO dto);

}

