package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserPropertyDTO;
import com.milchstrabe.rainbow.exception.LogicException;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    String modifiedUserAvatar(String userId, MultipartFile file) throws LogicException;

    void modifiedUserProperty(UserDTO dto) throws LogicException;

    UserPropertyDTO getUserProperty(UserDTO userDTO) throws LogicException;

    UserDTO searchUser(UserDTO dto);

}
