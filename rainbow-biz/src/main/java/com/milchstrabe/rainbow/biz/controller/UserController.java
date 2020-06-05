package com.milchstrabe.rainbow.biz.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.GenerateStorageClient;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/6/5 21:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private ISystemService systemService;


    /**
     *
     * @param user
     * @param file
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @PostMapping(path = "/avatar")
    public Result<String> avatar(@CurrentUser User user, MultipartFile file) throws LogicException, ParamMissException {
        Optional.ofNullable(file).orElseThrow(()->new ParamMissException("miss avatar"));
        /**
         * TrackerClient - TrackerServer接口
         * GenerateStorageClient - 一般文件存储接口 (StorageServer接口)
         * FastFileStorageClient - 为方便项目开发集成的简单接口(StorageServer接口)
         * AppendFileStorageClient - 支持文件续传操作的接口 (StorageServer接口)
         */
        FastFileStorageClient fastFileStorageClient = new DefaultFastFileStorageClient();
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length() - 1);
            InputStream inputStream = file.getInputStream();
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.getSize(), fileExtName, null);
            String path = storePath.getPath();
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
