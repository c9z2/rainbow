package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.fastdfs.client.common.UploadResult;
import com.milchstrabe.fastdfs.client.service.FastdfsTemplate;
import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserPropertyMapper;
import com.milchstrabe.rainbow.biz.service.IUserService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/6/10 18:44
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMappper userMappper;

    @Autowired
    private IUserPropertyMapper userPropertyMapper;

    @Autowired
    private FastdfsTemplate fastdfsTemplate;

    @Override
    public String modifiedUserAvatar(String userId, MultipartFile file) throws LogicException {
        /**
         * TrackerClient - TrackerServer接口
         * GenerateStorageClient - 一般文件存储接口 (StorageServer接口)
         * FastFileStorageClient - 为方便项目开发集成的简单接口(StorageServer接口)
         * AppendFileStorageClient - 支持文件续传操作的接口 (StorageServer接口)
         */
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            byte[] bytes = file.getBytes();

            UploadResult fs = fastdfsTemplate.upload_file(bytes, fileExtName, null);
            String path = fs.getPath();

            //get old avatar info
            UserProperty property = userPropertyMapper.findUserPropertyByUserId(userId);
            String avatar = property.getAvatar();
            if(StringUtils.hasLength(avatar)){
                //delete old avatar
                UploadResult group1 = fastdfsTemplate.delete_file("group1", avatar.replace("group1/", ""));
                if(!group1.isSuccess()){
                    log.error(group1.getMsg());
                }

            }
            User user = User.builder().userId(userId).build();
            UserProperty userProperty = UserProperty.builder()
                    .avatar(path)
                    .build();
            user.setProperty(userProperty);
            boolean isSuccess = userPropertyMapper.updateUserAvatar(user);
            if(isSuccess){
                return path;
            }
            throw new LogicException(500,"modified avatar fail");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }

    @Override
    public void modifiedUserProperty(UserDTO dto) throws LogicException {
        User user = BeanUtils.map(dto, User.class);
        boolean isSuccess = userPropertyMapper.updateUserPropertyByUserId(user);
        if(!isSuccess){
            throw new LogicException(500,"修改信息失败");
        }
    }

    @Override
    public UserPropertyDTO getUserProperty(UserDTO userDTO) throws LogicException {
        UserProperty userProperty = userPropertyMapper.findUserPropertyByUserId(userDTO.getUserId());
        Optional.ofNullable(userProperty).orElseThrow(()-> new LogicException(500,"user property not found"));
        UserPropertyDTO userPropertyDTO = BeanUtils.map(userProperty, UserPropertyDTO.class);
        return userPropertyDTO;
    }


    @Override
    public UserDTO searchUser(UserDTO dto) {
        User user = userMappper.searchUser(dto.getUsername(), dto.getStatus(), dto.getProperty().getEmail());
        if(user == null){
            return null;
        }
        return BeanUtils.map(user, UserDTO.class);
    }


}
