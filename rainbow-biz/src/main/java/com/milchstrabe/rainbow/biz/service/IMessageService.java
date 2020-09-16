package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.dto.SendMessageDTO;

public interface IMessageService {

    void doMessage(SendMessageDTO dto);
}
