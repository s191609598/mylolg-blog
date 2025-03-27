package com.mylog.controller;

import com.mylog.common.utils.resultutils.IdDTO;
import com.mylog.common.utils.resultutils.R;
import com.mylog.common.validator.AssertUtils;
import com.mylog.common.validator.ValidatorUtils;
import com.mylog.system.entity.file.vo.FileVO;
import com.mylog.system.service.SysFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
    @PostMapping("/upload")
    public R<FileVO> upload(@RequestParam("file") MultipartFile file) {
        return R.ok(sysFileService.upload(file));
    }

    /**
     * 查看文件
     *
     * @param id
     * @return
     */
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
    @PostMapping("/deletefile")
    public R<Boolean> deleteFile(@RequestBody IdDTO id) {
        ValidatorUtils.validateEntity(id);
        return R.ok(sysFileService.deleteFile(id.getId()));
    }
}
