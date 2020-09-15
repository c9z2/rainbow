package com.milchstrabe.rainbow.biz;

import com.milchstrabe.rainbow.ClientServer;
import com.milchstrabe.rainbow.biz.repository.ClientServerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class UCSTests {

    @Autowired
    private ClientServerRepository clientServerRepository;

    @Test
    void test() {

        Set<ClientServer> csByUid = clientServerRepository.findCSByUid("5f5cbc205e0a8b751da54e0c");

        System.out.println(csByUid);
    }

}
