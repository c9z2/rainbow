package com.milchstrabe.rainbow.converter;

import com.milchstrabe.rainbow.server.domain.dto.Message;

public interface IMessageConverter<T> {

    Message<T> converter(String msgStr);
}
