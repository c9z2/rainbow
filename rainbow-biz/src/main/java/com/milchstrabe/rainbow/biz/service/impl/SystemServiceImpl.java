package com.milchstrabe.rainbow.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.milchstrabe.rainbow.biz.common.config.JWTConfig;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.mapper.ICLIMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.biz.domain.po.CLI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author ch3ng
 * @Date 2020/4/29 23:30
 * @Version 1.0
 * @Description
 **/
@Service
@Slf4j
public class SystemServiceImpl implements ISystemService {

    @Autowired
    private IUserMappper userMappper;

    @Autowired
    private ICLIMappper cliMappper;

    @Value("{encrypt.secret:123}")
    private String secret;


    @Override
    public String signIn(String username, String password) throws LogicException {
        User userInDatabase = userMappper.findUserByUsername(username);
        User user = Optional.ofNullable(userInDatabase).orElseThrow(()->new LogicException(30000,"username or password err"));
        String md5Pwd = MD5.create().digestHex(password);
        if(!user.getPassword().equals(md5Pwd)){
            throw new LogicException(30000,"username or password err");
        }

        //用户名密码正确
        Map<String,Object> headerMap = new HashMap<>();
        headerMap.put("alg","HS256");
        headerMap.put("type","JWT");

        Algorithm algorithmHS = Algorithm.HMAC256(secret);
        Date now = new Date();
        /**
         * iss (issuer)：签发人
         * exp (expiration time)：过期时间
         * sub (subject)：主题
         * aud (audience)：受众
         * nbf (Not Before)：生效时间
         * iat (Issued At)：签发时间
         * jti (JWT ID)：编号
         */
        String token = JWT.create()
                .withHeader(headerMap)
                .withIssuer("https://rainbow.milchstrabe.com")
                .withExpiresAt(DateUtil.offsetMinute(now, JWTConfig.EXPIRATION_TIME))
                .withSubject("1")
                .withAudience("dev")
                .withNotBefore(now)
                .withIssuedAt(now)
                .withJWTId(UUID.randomUUID().toString().replace("-",""))
                .withClaim("userId",userInDatabase.getId())
                .withClaim("username",username)
                .sign(algorithmHS);

        return token;

    }

    @Override
    public String fingerprint(User user, String ctype) throws LogicException {
        StringBuilder sb = new StringBuilder(System.currentTimeMillis()+":").append(ctype).append(":").append(user.getUsername());
        String md5= MD5.create().digestHex(sb.toString());
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        String encryptHex = aes.encryptHex(md5);

        CLI cli = CLI.builder().cid(encryptHex).createTime(new Date()).ctype(ctype).user(user).build();
        boolean isSuccess = cliMappper.addCLI(cli);
        if(isSuccess){
            return encryptHex;
        }
        throw new LogicException(300,"create cid fail");
    }

}
