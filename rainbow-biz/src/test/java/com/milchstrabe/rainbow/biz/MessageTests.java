package com.milchstrabe.rainbow.biz;

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


    }

}
