package com.mylog.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mylog.system.entity.file.SysFile;
import com.mylog.system.entity.file.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author pss
* @description 针对表【sys_file(文件表)】的数据库操作Service
* @createDate 2025-02-25 22:20:15
*/
public interface SysFileService extends IService<SysFile> {

    FileVO upload(MultipartFile file);
    FileVO uploadCover(MultipartFile file);
    FileVO getFile(Long id);
    boolean deleteFile(Long id);



}
