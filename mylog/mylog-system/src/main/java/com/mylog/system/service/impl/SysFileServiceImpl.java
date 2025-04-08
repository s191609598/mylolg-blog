package com.mylog.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mylog.common.constant.Constants;
import com.mylog.common.exception.MyException;
import com.mylog.common.miniio.MinIOUtil;
import com.mylog.common.utils.ConvertUtils;
import com.mylog.common.utils.StringUtils;
import com.mylog.common.utils.img.ImageProcessor;
import com.mylog.common.utils.resultutils.ErrorCode;
import com.mylog.common.validator.AssertUtils;
import com.mylog.system.dao.SysFileDao;
import com.mylog.system.entity.SysConfig;
import com.mylog.system.entity.file.SysFile;
import com.mylog.system.entity.file.vo.FileVO;
import com.mylog.system.service.SysConfigService;
import com.mylog.system.service.SysFileService;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author pss
 * @description 针对表【sys_file(文件表)】的数据库操作Service实现
 * @createDate 2025-02-25 22:20:15
 */
@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileDao, SysFile> implements SysFileService {

    @Value("${hostip}")
    private String hostip;

    @Resource
    MinIOUtil minIOUtil;

    @Resource
    SysConfigService sysConfigService;

    @Override
    public FileVO upload(MultipartFile file) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        String upload = minIOUtil.upload(file);
        if (StringUtils.isNotEmpty(upload)) {
            return this.createFile(upload, userId, file);
        }
        return null;
    }


    @Override
    public FileVO uploadCover(MultipartFile file) {
        Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
        AssertUtils.isNull(loginIdDefaultNull, ErrorCode.NOT_LOGIN_ERROR);
        Long userId = Long.valueOf(loginIdDefaultNull.toString());
        byte[] imageBytes = null;
        try {
            imageBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            byte[] newImageBytes = ImageProcessor.processAndUpload(imageBytes);
            String upload = minIOUtil.upload(newImageBytes, file.getOriginalFilename(), file.getContentType());
            if (StringUtils.isNotEmpty(upload)) {
                return this.createFile(upload, userId, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MinioException e) {
            throw new RuntimeException(e);
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


    public FileVO createFile(String upload, Long userId, MultipartFile file) {
        SysFile sysFile = new SysFile();
        SysConfig platformType = sysConfigService.getConfigByKey(Constants.SYS_FILE_PLATFORM_TYPE);
        if (platformType.getConfigValue().equals(Constants.FILE_PLATFORM_MINIO)) {
            SysConfig configByKey = sysConfigService.getConfigByKey(Constants.MINIO_BUCKETNAME_KEY);
            if (StringUtils.isNull(configByKey) || StringUtils.isBlank(configByKey.getConfigValue())) {
                throw new MyException("请先配置minio桶名称");
            }
            String preview = minIOUtil.preview(upload);
            String fileUrl = preview.substring(0, preview.indexOf("?"));
            String configValue = configByKey.getConfigValue();
            String bucketName = "/" + configValue;
            sysFile.setPlatformUrl(Constants.PLATFORM_URL + configValue);
            String basePath = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length(), fileUrl.lastIndexOf("/") + 1);
            sysFile.setBasePath(basePath);
            sysFile.setProxyUrl(Constants.PROXY_URL + configValue + "/" + upload);
            sysFile.setFileUrl(fileUrl);
            sysFile.setPlatform(platformType.getConfigValue());
        }
        String fileName = upload.substring(upload.lastIndexOf("/") + 1);
        sysFile.setFileName(fileName);
        sysFile.setOFileName(file.getOriginalFilename());
        sysFile.setFileSize(file.getSize());
        sysFile.setFileType(file.getContentType());
        sysFile.setCreateBy(userId);
        boolean save = this.save(sysFile);
        if (save) {
            FileVO vo = new FileVO();
            vo.setId(sysFile.getId());
            if (sysFile.getPlatform().equals(Constants.FILE_PLATFORM_MINIO) && StringUtils.isNoneBlank(hostip) && !StringUtils.contains(hostip, "192.168.124")) {
                vo.setFileUrl(sysFile.getProxyUrl());
            } else {
                vo.setFileUrl(sysFile.getFileUrl());
            }
            vo.setFileName(sysFile.getFileName());
            return vo;
        }
        return null;
    }

}




