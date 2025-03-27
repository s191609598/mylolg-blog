package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.exception.MyException;
import com.mylog.common.miniio.MinIOUtil;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysFileDao;
import com.mylog.system.entity.file.SysFile;
import com.mylog.system.entity.file.vo.FileVO;
import com.mylog.system.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author 彭上尚
 * @description 针对表【sys_file(文件表)】的数据库操作Service实现
 * @createDate 2025-02-25 22:20:15
 */
@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileDao, SysFile> implements SysFileService {

    @Resource
    MinIOUtil minIOUtil;

    @Override
    public FileVO upload(MultipartFile file) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        String upload = minIOUtil.upload(file);
        if (StringUtils.isNotEmpty(upload)) {
            SysFile sysFile = new SysFile();
            String preview = minIOUtil.preview(upload);
            String substring = preview.substring(0, preview.indexOf("?"));
            sysFile.setFileUrl(substring);
            sysFile.setFileName(upload);
            sysFile.setFileSize(file.getSize());
            sysFile.setFileType(file.getContentType());
            sysFile.setCreateBy(userId);
            boolean save = this.save(sysFile);
            if (save) {
                FileVO vo = new FileVO();
                vo.setId(sysFile.getId());
                vo.setFileUrl(sysFile.getFileUrl());
                vo.setFileName(sysFile.getFileName());
                return vo;
            }
        }
        return null;
    }

    @Override
    public FileVO getFile(Long id) {
        SysFile byId = this.getById(id);
        AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
        FileVO vo = ConvertUtils.sourceToTarget(byId, FileVO.class);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteFile(Long id) {
        SysFile byId = this.getById(id);
        AssertUtils.isNull(byId, ErrorCode.NOT_FOUND_ERROR);
        try {
            boolean removeById = this.removeById(id);
            if (removeById) {
                boolean remove = minIOUtil.remove(byId.getFileName());
                if (remove) {
                    return true;
                }
            }
        } catch (Exception e) {
            //todo  minIO 报错后 把文件再上传一遍  先检查文件是否存在 再上传
            log.error("删除文件失败", e);
            log.error("删除文件失败-参数：{}", JSON.toJSONString(byId));
            throw new MyException(ErrorCode.OPERATION_ERROR);
        }
        return false;
    }
}




