package com.milchstrabe.rainbow.skt.repository;

import com.milchstrabe.rainbow.skt.server.ClientServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/4/28 17:12
 * @Version 1.0
 * @Description
 **/
@Repository
public class ClientServerRepository {

    private static final String ROOT_PATH = "rainbow:ucs:";

    @Resource
    private RedisTemplate<String,List<ClientServer>> redisTemplate;


    public List<ClientServer> findCSByUid(String uid){
        List<ClientServer> clientServers = redisTemplate.opsForValue().get(ROOT_PATH + uid);
        return clientServers;
    }

}
