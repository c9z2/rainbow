package com.milchstrabe.rainbow.tcp.repository;

import com.milchstrabe.rainbow.ClientServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private RedisTemplate<String,Set<ClientServer>> redisTemplate;


    public Set<ClientServer> findCSByUid(String username){
        Set<ClientServer> clientServers = redisTemplate.opsForValue().get(ROOT_PATH + username);
        clientServers = Optional.ofNullable(clientServers).orElse(new HashSet<>());
        return clientServers;
    }

    public void addCS(ClientServer clientServer,String username){
        Set<ClientServer> csByUid = findCSByUid(username);
        if(csByUid == null){
            csByUid = new HashSet<>();
        }
        csByUid.add(clientServer);
        redisTemplate.opsForValue().set(ROOT_PATH + username,csByUid);
    }

}
