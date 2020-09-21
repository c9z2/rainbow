package com.milchstrabe.rainbow.biz.common.util;

import com.milchstrabe.rainbow.exception.LogicException;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
public class MinioUtils {


    @Value("${minio.host}")
    private String host;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    public void upload(String bucket,String path, InputStream inputStream) throws LogicException {
        try {
            MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
            minioClient.putObject(bucket,path,inputStream,null);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }

    public void delete(String bucket,String path) throws LogicException {
        try {
            MinioClient minioClient = new MinioClient(host, accessKey, secretKey);
            minioClient.removeObject(bucket,path);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }
}
