package com.milchstrabe.rainbow.biz.controller;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastFile;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @Author ch3ng
 * @Date 2020/6/5 21:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class ModifiedUserAvatarController {


    @Autowired
    private IUserService userService;

    @Autowired
    protected FastFileStorageClient storageClient;
    /**
     *
     * @param user
     * @param file
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @PostMapping(path = APIVersion.V_1 + "/avatar")
    public Result<String> avatar(@CurrentUser RequestUser user, MultipartFile file) throws LogicException, ParamMissException, IOException {
        Optional.ofNullable(file).orElseThrow(()->new ParamMissException("miss avatar"));
        /**
         * TrackerClient - TrackerServer接口
         * GenerateStorageClient - 一般文件存储接口 (StorageServer接口)
         * FastFileStorageClient - 为方便项目开发集成的简单接口(StorageServer接口)
         * AppendFileStorageClient - 支持文件续传操作的接口 (StorageServer接口)
         */
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            InputStream inputStream = file.getInputStream();

            Set<MetaData> metaDataSet = new HashSet<MetaData>();
            metaDataSet.add(new MetaData("Author", "ch3ng"));
            metaDataSet.add(new MetaData("CreateDate", System.currentTimeMillis()+""));

            FastFile fastFile = new FastFile.Builder()
                    .withFile(inputStream, file.getSize(), fileExtName)
                    .withMetaData(metaDataSet)
                    .toGroup("group1")
                    .build();
            StorePath storePath = storageClient.uploadFile(fastFile);

            UserDTO userDTO = UserDTO.builder().build();
            BeanUtils.copyProperties(user,userDTO);
            UserPropertyDTO userPropertyDTO = UserPropertyDTO.builder()
                    .avatar(storePath.getFullPath())
                    .build();
            userDTO.setProperty(userPropertyDTO);
            userService.modifiedUserAvatar(userDTO);
            return ResultBuilder.success();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }
}
