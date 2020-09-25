package com.milchstrabe.rainbow.biz.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.milchstrabe.rainbow.biz.common.config.JWTConfig;
import com.milchstrabe.rainbow.biz.common.util.BeanUtils;
import com.milchstrabe.rainbow.biz.domain.dto.ResetPasswdDTO;
import com.milchstrabe.rainbow.biz.domain.dto.UserDTO;
import com.milchstrabe.rainbow.biz.domain.po.CLI;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.mapper.ICLIMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserPropertyMapper;
import com.milchstrabe.rainbow.biz.service.ISystemService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private IUserPropertyMapper userPropertyMapper;


    @Autowired
    private ICLIMappper cliMappper;

    @Value("{encrypt.secret:123}")
    private String secret;

    private static final String RESET_PASSWD_KEY = "rainbow:reset:pwd:";


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public String signIn(UserDTO userDTO) throws LogicException {
        User userInDatabase = userMappper.findUserByUsernameAndStatus(userDTO.getUsername(),(short)1);
        User user = Optional.ofNullable(userInDatabase).orElseThrow(()->new LogicException(30000,"username or password err"));
        String md5Pwd = MD5.create().digestHex(userDTO.getPasswd());
        if(!user.getPasswd().equals(md5Pwd)){
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
                .withClaim("userId",userInDatabase.getUserId())
                .withClaim("username",userDTO.getUsername())
                .sign(algorithmHS);

        return token;

    }

    @Transactional
    @Override
    public void register(UserDTO userDTO) throws LogicException {

        User userInDatabase = userMappper.findUserByUsername(userDTO.getUsername());
        if(userInDatabase != null){
            throw new LogicException(300,"user exist");
        }
        User user = BeanUtils.map(userDTO, User.class);
        boolean isSuccess = userMappper.addUser(user);
        if(!isSuccess){
            throw new LogicException(300,"add user fail");
        }
        boolean isOk = userPropertyMapper.addUserProperty(user);
        if(!isOk){
            throw new LogicException(300,"add user fail");
        }
    }

    @Override
    public String fingerprint(User user, String ctype) throws LogicException {
        StringBuilder sb = new StringBuilder(System.currentTimeMillis()+":").append(ctype).append(":").append(user.getUsername());
        String md5= MD5.create().digestHex(sb.toString());
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        String encryptHex = aes.encryptHex(md5);

        CLI cli = CLI.builder()
                .cid(encryptHex)
                .createTime(System.currentTimeMillis())
                .ctype(ctype)
                .user(user)
                .build();
        boolean isSuccess = cliMappper.addCLI(cli);
        if(isSuccess){
            return encryptHex;
        }
        throw new LogicException(3000,"create cid fail");
    }


    @Override
    public void resetPasswd(ResetPasswdDTO dto) throws LogicException {
        String username = dto.getUsername();
        String email = dto.getEmail();
        String code = dto.getCode();
        String passwd = dto.getPasswd();
        User user = userMappper.findUserByUsername(username);
        if(!email.equals(user.getProperty().getEmail())){
            throw new LogicException(3000,"重置密码失败，账号和邮箱不匹配");
        }

        String codeInDatabase = redisTemplate.opsForValue().get(RESET_PASSWD_KEY + email);

        if(codeInDatabase == null){
            throw new LogicException(3000,"重置密码失败，验证码失效");
        }

        if(!code.equals(codeInDatabase)){
            throw new LogicException(3000,"重置密码失败，验证码错误");
        }

        String md5Pwd = MD5.create().digestHex(passwd);
        boolean isSuccess = userMappper.updatePasswordByUsername(username, md5Pwd);
        if(!isSuccess){
            throw new LogicException(3000,"重置密码失败,原因未知");
        }

    }

}
