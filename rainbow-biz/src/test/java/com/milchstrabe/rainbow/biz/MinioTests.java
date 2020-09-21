package com.milchstrabe.rainbow.biz;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class MinioTests {

    @Test
    void test() {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.1.118:9000", "minioadmin", "minioadmin");

            // 检查存储桶是否已经存在
//            boolean isExist = minioClient.bucketExists("test");
//            if(isExist) {
//                System.out.println("Bucket already exists.");
//            } else {
//                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
//                minioClient.makeBucket("asiatrip");
//            }

            // 使用putObject上传一个文件到存储桶中。
//            minioClient.putObject("asiatrip","asiaphotos.zip", "/home/user/Photos/asiaphotos.zip");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("aaaa".getBytes());
            minioClient.putObject("test","/123/123/abc",byteArrayInputStream,null);
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
