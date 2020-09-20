package com.milchstrabe.rainbow.biz;

import com.milchstrabe.fastdfs.client.common.UploadResult;
import com.milchstrabe.fastdfs.client.service.FastdfsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AvatarTests {

    @Autowired
    protected FastdfsTemplate fastdfsTemplate;
    @Test
    void test() {

//        UploadResult png = fastdfsTemplate.upload_file("abc".getBytes(), "png", null);
//        System.out.println(png);
//        storageClient.deleteFile("group1/M00/00/00/wKgBdl9m_IaAfwQ5AAAks8XC8HQ091.jpg");
//        storageClient.deleteFile("group1","M00/00/00/wKgBdl9m_IaAfwQ5AAAks8XC8HQ091.jpg");
        UploadResult group1 = fastdfsTemplate.delete_file("group1", "M00/00/00/wKgBdl9m9s6AObATAAAks8XC8HQ583.jpg");
        System.out.println(group1);
    }

}
