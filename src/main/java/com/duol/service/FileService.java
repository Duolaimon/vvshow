package com.duol.service;

import com.duol.cache.ValueCache;
import com.duol.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author Duolaimon
 * 18-7-14 下午9:08
 */
@Service
public class FileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);


    public String upload(MultipartFile file, String path) {
        if (file.isEmpty()) throw new RuntimeException("未选择文件");
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName = ValueCache.getUUID() + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        Path rootLocation = FTPUtil.createDirectory(path);


        try {
            Path filePath = rootLocation.resolve(uploadFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            //文件已经上传成功了


            boolean result = FTPUtil.uploadFile(Lists.newArrayList(filePath.toFile()));
            //已经上传到ftp服务器上
            if (!result) throw new IOException();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return uploadFileName;
    }

}
