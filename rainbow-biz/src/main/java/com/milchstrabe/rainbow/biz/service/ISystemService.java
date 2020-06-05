package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.exception.LogicException;

public interface ISystemService {

    String signIn(UserDTO userDTO) throws LogicException;

    void register(UserDTO userDTO) throws LogicException;

    String fingerprint(User user, String ctype) throws LogicException;
}
