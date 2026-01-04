package com.movie.controller;

import com.movie.common.Result;
import com.movie.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient minioClient;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file){
        try{
            String bucketName = minioConfig.getBucketName();

            // 生成唯一文件名 (防止重名覆盖)
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + suffix;

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 拼接返回URL
            // 格式: http://localhost:9000/movie-images/xxx.jpg
            String url = minioConfig.getEndpoint() + "/" + bucketName + "/" + fileName;
            return Result.success(url);
        }catch(Exception e){
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}
