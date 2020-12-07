package com.atguigu.ossservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {
    String uploadFileOSS(MultipartFile file);
}
