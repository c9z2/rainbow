package com.milchstrabe.rainbow.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.milchstrabe.rainbow.server.domain.dto.Message;
import com.milchstrabe.rainbow.server.domain.dto.TextMessage;

import java.lang.reflect.Type;

/**
 * @Author ch3ng
 * @Date 2020/9/3 16:56
 * @Version 1.0
 * @Description
 **/

@Converter
public class TextMessageConverter implements IMessageConverter<TextMessage>{

    @Override
    public Message<TextMessage> converter(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Message<TextMessage>>() {}.getType();
        Message<TextMessage> messageMessage =  gson.fromJson(json, type);
        return messageMessage;
    }
}
