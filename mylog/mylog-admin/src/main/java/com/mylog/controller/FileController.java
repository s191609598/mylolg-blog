package com.mylog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.mylog.common.utils.img.ImageProcessor;
import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.system.entity.file.vo.FileVO;
import com.mylog.system.service.SysFileService;
import io.minio.errors.MinioException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author pss
 * @date 2025/2/25
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    SysFileService sysFileService;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/upload")
    public R<FileVO> upload(@RequestParam("file") MultipartFile file) {
        return R.ok(sysFileService.upload(file));
    }

    /**
     * 上传封面图片
     * @param file
     * @return
     */

    @SaCheckRole("admin")
    @PostMapping("/uploadcover")
    public R<FileVO> uploadCover(@RequestParam("file") MultipartFile file) {
        return R.ok(sysFileService.uploadCover(file));
    }

    /**
     * 查看文件
     *
     * @param id
     * @return
     */
    @SaCheckRole("admin")
    @GetMapping("/getfile")
    public R<FileVO> getFile(Long id) {
        AssertUtils.isNull(id, "id不能为空");
        FileVO file = sysFileService.getFile(id);
        return R.ok(file);
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @SaCheckRole("admin")
    @PostMapping("/deletefile")
    public R<Boolean> deleteFile(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        return R.ok(sysFileService.deleteFile(id.getId()));
    }
}
