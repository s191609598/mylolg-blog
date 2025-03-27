package com.mylog.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mylog.common.annotation.RepeatSubmit;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.common.validator.group.AddGroup;
import com.mylog.common.validator.group.UpdateGroup;
import com.mylog.system.entity.user.dto.*;
import com.mylog.system.entity.user.vo.QueryUserVO;
import com.mylog.system.entity.user.vo.UserVO;
import com.mylog.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Pss
 * @date 2022/3/22 17:12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;


    /**
     * 登录
     *
     * @returni
     */
//    @RepeatSubmit
    @PostMapping("/login")
    public R<SaResult> login(@RequestBody UserLoginDTO userLoginDTO) {
        ValidatorUtils.validateEntity(userLoginDTO);
        SaResult login = sysUserService.login(userLoginDTO);
        return R.ok(login);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RepeatSubmit
    @PostMapping("/logout")
    public R logout() {
        sysUserService.logout();
        return R.ok();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
//    @RepeatSubmit
    @GetMapping("/getuserinfo")
    public R<UserVO> getUserInfo() {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        if (ObjUtil.isNull(loginIdDefaultNull)) {
            return R.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        UserVO userInfo = sysUserService.getUserInfo(userId);
        return R.ok(userInfo);
    }

    /**
     * 用户注册
     *
     * @param dto
     * @return
     */
//    @RepeatSubmit
    @PostMapping("/userregister")
    public R<Long> userRegister(@RequestBody UserRegisterDTO dto) {
        ValidatorUtils.validateEntity(dto);
        AssertUtils.assertIf(!dto.getUserPassword().equals(dto.getCheckPassword()), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        Long register = sysUserService.register(dto);
        return R.ok(register);
    }

    /**
     * 门户网站用户修改信息
     *
     * @param dto
     * @return
     */
//    @RepeatSubmit
    @PostMapping("/updateuser")
    public R<Boolean> updateUser(@RequestBody UpdateUserDTO dto) {
        ValidatorUtils.validateEntity(dto);
        boolean b = sysUserService.updateUser(dto);
        return R.ok(b);
    }

    /**
     * 后台查询用户列表
     *
     * @return
     */
    @PostMapping("/queryuserlist")
    public R<Page<QueryUserVO>> queryUserList(@RequestBody UserQueryDTO dto) {
        Page<QueryUserVO> queryUserVOPage = sysUserService.queryUserList(dto);
        return R.ok(queryUserVOPage);
    }

    /**
     * 后台新增用户
     *
     * @param dto
     * @return
     */
    @PostMapping("/adduser")
    public R<Boolean> addUser(@RequestBody EditUserDTO dto) {
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return R.ok(sysUserService.adduser(dto));
    }


    /**
     * 后台修改用户
     *
     * @param dto
     * @return
     */
    @PostMapping("/edituser")
    public R<Boolean> editUser(@RequestBody EditUserDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        return R.ok(sysUserService.editUser(dto));
    }

    /**
     * 后台删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteuser")
    public R<Boolean> deleteUser(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        return R.ok(sysUserService.removeById(id.getId()));
    }
}
