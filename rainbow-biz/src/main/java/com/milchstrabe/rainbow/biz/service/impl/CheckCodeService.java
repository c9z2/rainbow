package com.milchstrabe.rainbow.biz.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.po.UserProperty;
import com.milchstrabe.rainbow.biz.mapper.IUserMappper;
import com.milchstrabe.rainbow.biz.mapper.IUserPropertyMapper;
import com.milchstrabe.rainbow.biz.service.ICheckCodeService;
import com.milchstrabe.rainbow.exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ch3ng
 * @Date 2020/9/3 13:07
 * @Version 1.0
 * @Description
 **/
@Service
@Slf4j
public class CheckCodeService implements ICheckCodeService {


    @Autowired
    private IUserPropertyMapper userPropertyMapper;

    @Autowired
    private IUserMappper userMappper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    private static final String SIGN_UP_KEY = "rainbow:sign:up:";

    private static final String RESET_PASSWD_KEY = "rainbow:reset:pwd:";

    @Override
    public void sendSignUpCheckcode(String email) throws LogicException {
        UserProperty userProperty = userPropertyMapper.findUserPropertyByEmail(email);
        if(userProperty != null){
            throw new LogicException(300,"电子邮件已经存在");
        }

        String code = RandomUtil.randomString(6);

        redisTemplate.opsForValue().set(SIGN_UP_KEY + email,code,5,TimeUnit.MINUTES);

        ClassPathResource classPathResource = new ClassPathResource("mail.html");
        try(InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = inputStream.read(buf,0, buf.length)) != -1){
                byteArrayOutputStream.write(buf,0,len);
            }
            String html = new String(byteArrayOutputStream.toByteArray(),"utf-8");
            String regEx = "<code>";
            Pattern p_html = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(html);
            html = m_html.replaceAll(code);
            MailUtil.send(email, "rainbow service", html, true);
        }catch (IOException e){
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }

    @Override
    public void sendResetCheckCode(String username,String email) throws LogicException {
        User user = userMappper.findUserAndPropertyByUsernameAndStatus(username,(short)1);
        if(user == null || user.getProperty() == null){
            return;
        }
        if(!email.equals(user.getProperty().getEmail())){
            //不存在这样的电子邮箱时，也提示发送成功。
            return;
        }

        String code = RandomUtil.randomString(6);

        redisTemplate.opsForValue().set(RESET_PASSWD_KEY + email,code,5, TimeUnit.MINUTES);
        ClassPathResource classPathResource = new ClassPathResource("mail.html");
        try(InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = inputStream.read(buf,0, buf.length)) != -1){
                byteArrayOutputStream.write(buf,0,len);
            }
            String html = new String(byteArrayOutputStream.toByteArray(),"utf-8");
            String regEx = "<code>";
            Pattern p_html = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(html);
            html = m_html.replaceAll(code);
            MailUtil.send(email, "rainbow service", html, true);
        }catch (IOException e){
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }
}
