package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.constant.RedisConstants;
import com.mylog.common.enums.UserRoleEnum;
import com.mylog.common.exception.MyException;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.ip.IpUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysUserDao;
import com.mylog.system.entity.user.SysUser;
import com.mylog.system.entity.user.dto.*;
import com.mylog.system.entity.user.vo.QueryUserVO;
import com.mylog.system.entity.user.vo.UserVO;
import com.mylog.system.redis.RedisCacheUtils;
import com.mylog.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Resource
    RedisCacheUtils redisCacheUtils;

    /**
     * 用户注册
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long register(UserRegisterDTO dto) {
        //判断账号是否重复
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", dto.getUserAccount());
        Long l = baseMapper.selectCount(queryWrapper);
        AssertUtils.assertIf(l > 0, ErrorCode.PARAMS_ERROR, "账号已存在");
        SysUser sysUser = ConvertUtils.sourceToTarget(dto, SysUser.class);
        //密码加盐加密
        sysUser.setUserPassword(getEncryptedPassword(dto.getUserPassword()));
        //插入数据
        sysUser.setUserRole(UserRoleEnum.USER.getValue());
        sysUser.setUserName("匿名");
        int i = baseMapper.insert(sysUser);
        if (i < 1) {
            throw new MyException(ErrorCode.OPERATION_ERROR);
        }
        return sysUser.getId();
    }

    /**
     * 登录
     *
     * @param dto
     */
    @Override
    public SaResult login(UserLoginDTO dto) {
        //判断用户是否存在
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", dto.getUserAccount());
        queryWrapper.eq("userPassword", getEncryptedPassword(dto.getUserPassword()));
        SysUser user = baseMapper.selectOne(queryWrapper);
        AssertUtils.isNull(user, ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        //登录
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        //异步更新登录时间 登录IP 同时更新用户信息到redis
//        this.updateUserLoginDate(user.getId());
        return SaResult.data(tokenInfo);
    }

    @Override
    public void logout() {
        //获取当前登录用户
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        //验证用户是否登录
        AssertUtils.isNull(loginIdDefaultNull, "请先登录");
        Long id = Long.valueOf(loginIdDefaultNull.toString());
        //退出登录
        StpUtil.logout();
        //删除缓存
        redisCacheUtils.deleteObject(RedisConstants.REDIS_USERID + id);
    }


    @Override
    public UserVO getUserInfo(Long id) {
        SysUser sysUser = redisCacheUtils.getCacheObject(RedisConstants.REDIS_USERID + id);
        if (StringUtils.isNull(sysUser)) {
            sysUser = this.getUserById(id);
            //将用户信息存入缓存8小时
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_USERID + id, sysUser, 8, TimeUnit.MINUTES);
        }
        UserVO vo = ConvertUtils.sourceToTarget(sysUser, UserVO.class);
        return vo;
    }

    @Override
    public SysUser getUserById(Long id) {
        //从缓存中获取用户信息
        SysUser sysUser = redisCacheUtils.getCacheObject(RedisConstants.REDIS_USERID + id);
        if (StringUtils.isNull(sysUser)) {
            //从数据库中获取用户信息
            sysUser = baseMapper.selectById(id);
            AssertUtils.isNull(sysUser, ErrorCode.PARAMS_ERROR, "用户不存在");
            //将用户信息存入缓存8小时
            redisCacheUtils.setCacheObject(RedisConstants.REDIS_USERID + id, sysUser, 8, TimeUnit.MINUTES);
        }
        return sysUser;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UpdateUserDTO dto) {
        //获取当前登录用户
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, "请先登录");
        Long id = Long.valueOf(loginIdDefaultNull.toString());
        //验证用户信息
        //从缓存中获取用户信息
        SysUser sysUser = redisCacheUtils.getCacheObject(RedisConstants.REDIS_USERID + id);
        if (StringUtils.isNull(sysUser)) {
            //从数据库中获取用户信息
            sysUser = baseMapper.selectById(id);
            AssertUtils.isNull(sysUser, ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        //更新数据
        SysUser updateUser = ConvertUtils.sourceToTarget(dto, SysUser.class);
        if (StringUtils.isNotBlank(dto.getUserPassword())) {
            updateUser.setUserPassword(getEncryptedPassword(dto.getUserPassword()));
        }
        updateUser.setId(id);
        updateUser.setUpdateTime(new Date());
        updateUser.setEditTime(new Date());
        updateUser.setUpdateBy(id);
        int i = baseMapper.updateById(updateUser);
        AssertUtils.assertIf(i < 1, ErrorCode.OPERATION_ERROR, "更新失败");
        return true;
    }

    @Override
    public Page<QueryUserVO> queryUserList(UserQueryDTO dto) {
        QueryWrapper<SysUser> queryWrapper = this.getQueryWrapper(dto);
        Page<SysUser> iPage = baseMapper.selectPage(new Page<>(dto.getPageNo(), dto.getPageSize()), queryWrapper);
        Page<QueryUserVO> page = new Page<>();
        page.setCurrent(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        page.setSize(iPage.getSize());
        page.setPages(iPage.getPages());
        page.setRecords(ConvertUtils.sourceToTarget(iPage.getRecords(), QueryUserVO.class));
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean adduser(EditUserDTO dto) {
        //判断账号是否重复
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", dto.getUserAccount());
        Long l = baseMapper.selectCount(queryWrapper);
        AssertUtils.assertIf(l > 0, ErrorCode.PARAMS_ERROR, "账号已存在");
        //转换实体类
        SysUser sysUser = ConvertUtils.sourceToTarget(dto, SysUser.class);
        sysUser.setId(null);
        //密码加盐加密
        sysUser.setUserPassword(getEncryptedPassword(dto.getUserPassword()));
        boolean save = this.save(sysUser);
        AssertUtils.assertIf(!save, ErrorCode.OPERATION_ERROR, "新增失败");
        return save;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editUser(EditUserDTO dto) {
        //判断账号是否重复
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", dto.getUserAccount());
        queryWrapper.ne("id", dto.getId());
        Long l = baseMapper.selectCount(queryWrapper);
        AssertUtils.assertIf(l > 0, ErrorCode.PARAMS_ERROR, "账号已存在");
        //转换实体类
        SysUser sysUser = ConvertUtils.sourceToTarget(dto, SysUser.class);
        sysUser.setUpdateTime(new Date());
        sysUser.setUserPassword(null);
        boolean update = this.updateById(sysUser);
        AssertUtils.assertIf(!update, ErrorCode.OPERATION_ERROR, "更新失败");
        return update;
    }

    /**
     * 获得加密后的密码
     *
     * @param password
     * @return
     */
    @Override
    public String getEncryptedPassword(String password) {
        return DigestUtils.md5DigestAsHex((password + Constants.SALT).getBytes());
    }


    /**
     * 异步处理 失败不影响登录
     *
     * @param id
     */
    @Async
    @Override
    public void updateUserLoginDate(Long id) {
        try {
            // 从 RequestContextHolder 中获取 ServletRequestAttributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 获取 HttpServletRequest 对象
            HttpServletRequest request = attributes.getRequest();
            SysUser sysUser = baseMapper.selectById(id);
            Date loginDate = sysUser.getLoginDate();
            String loginIp = sysUser.getLoginIp();
            String ip = IpUtils.getIp(request);
            SysUser updateUser = new SysUser();
            updateUser.setId(id);
            if (StringUtils.isNull(loginDate)) {
                updateUser.setLastLoginDate(new Date());
            } else {
                updateUser.setLastLoginDate(loginDate);
            }
            if (StringUtils.isBlank(loginIp)) {
                updateUser.setLastLoginIp(ip);
            } else {
                updateUser.setLastLoginIp(loginIp);
            }
            updateUser.setLoginDate(new Date());
            updateUser.setLoginIp(ip);
            int i = baseMapper.updateById(updateUser);
            if (i > 0) {
                sysUser = baseMapper.selectById(id);
                //将用户信息存入缓存8小时
                redisCacheUtils.setCacheObject(RedisConstants.REDIS_USERID + id, sysUser, 8, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.error("更新用户登录时间IP失败", e);
        }
    }


    public QueryWrapper<SysUser> getQueryWrapper(UserQueryDTO dto) {
        AssertUtils.isNull(dto, ErrorCode.PARAMS_ERROR);
        QueryWrapper<SysUser> wq = new QueryWrapper<>();
        Long id = dto.getId();
        String userAccount = dto.getUserAccount();
        String userName = dto.getUserName();
        String userProfile = dto.getUserProfile();
        String email = dto.getEmail();
        String phonenumber = dto.getPhonenumber();
        String remark = dto.getRemark();
        Integer sex = dto.getSex();
        String userRole = dto.getUserRole();
        Integer isDelete = dto.getIsDelete();
        String sortField = dto.getSortField();
        String sortOrder = dto.getSortOrder();
        wq.like(StringUtils.isNotBlank(userName), "userName", userName);
        wq.like(StringUtils.isNotBlank(userAccount), "userAccount", userAccount);
        wq.like(StringUtils.isNotBlank(phonenumber), "phonenumber", phonenumber);
        wq.like(StringUtils.isNotBlank(email), "email", email);
        wq.like(StringUtils.isNotBlank(userProfile), "remark", userProfile);
        wq.like(StringUtils.isNotBlank(remark), "userProfile", remark);
        wq.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        wq.eq(StringUtils.isNotNull(isDelete), "isDelete", isDelete);
        wq.eq(StringUtils.isNotNull(id), "id", id);
        wq.eq(StringUtils.isNotNull(sex), "sex", sex);
        wq.orderBy(StringUtils.isNotBlank(sortField), StringUtils.equals(sortOrder, Constants.ASC), sortField);
        return wq;
    }

}