package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.common.util.MinioUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

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
    private MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.prefix}")
    private String prefix;


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
            String path = "/user/avatar/"+ UUID.randomUUID().toString().replace("-","")+"."+fileExtName;
            minioUtils.upload(bucket,path,file.getInputStream());

            //get old avatar info
            UserProperty property = userPropertyMapper.findUserPropertyByUserId(userId);
            String avatar = property.getAvatar();
            if(StringUtils.hasLength(avatar)){
                //delete old avatar
                minioUtils.delete(bucket,avatar);
            }
            User user = User.builder().userId(userId).build();
            UserProperty userProperty = UserProperty.builder()
                    .avatar(prefix + path)
                    .build();
            user.setProperty(userProperty);
            boolean isSuccess = userPropertyMapper.updateUserAvatar(user);
            if(isSuccess){
                return bucket + path;
            }
            throw new LogicException(500,"modified avatar fail");
        } catch (Exception e) {
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
