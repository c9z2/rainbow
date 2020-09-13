package com.milchstrabe.rainbow.biz;

import com.milchstrabe.rainbow.server.domain.po.AddContactMessage;
import com.milchstrabe.rainbow.server.domain.po.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class MessageTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void addMessage() {
        Message<AddContactMessage> message = new Message<>();
        AddContactMessage addContactMessage = new AddContactMessage();
        addContactMessage.setAvatar("https://upload.jianshu.io/users/upload_avatars/2387828/f76d8ba4-fafa-4c43-afc0-cafc24c62538.jpg");
        addContactMessage.setNickname("xiaoming");
        addContactMessage.setNote("跪求好友位");
        addContactMessage.setStatus((short)0);
        addContactMessage.setUsername("admin");

        message.setDate(System.currentTimeMillis());
        message.setId("ajsd0i234n0ds");
        message.setMsgType(10);
        message.setReceiver("5f5364955e0a5556c440ecfd");
        message.setSender("5f5364955e423c440ecfd");
        message.setStatus((short)1);
        message.setContent(addContactMessage);

        mongoTemplate.save(message);

    }

}
