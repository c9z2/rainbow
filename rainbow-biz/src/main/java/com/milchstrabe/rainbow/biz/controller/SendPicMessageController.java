package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.common.util.MinioUtils;
import com.milchstrabe.rainbow.biz.common.util.SnowFlake;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.SendMessageDTO;
import com.milchstrabe.rainbow.biz.domain.vo.SendingPicMessageVO;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.biz.service.IMessageService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

/**
 * @Author ch3ng
 * @Date 2020/5/11 11:52
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/message")
public class SendPicMessageController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.prefix}")
    private String prefix;


    @Autowired
    private IContactService contactService;

    @PutMapping(path = APIVersion.V_1 + "/pic/sending")
    public Result<String> sendMessage(@CurrentUser RequestUser user,
                                      SendingPicMessageVO vo) throws ParamMissException, LogicException {

        List<Map<String, String>> contactRelationship = contactService.findContactRelationship(user.getUserId(), vo.getReceiver());
        if(contactRelationship.size() != 2){
            throw new LogicException(300,"已开启好友认证");
        }

        MultipartFile file = vo.getFile();
        Optional.ofNullable(file).orElseThrow(()->new ParamMissException("miss pic"));
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            String path = "/user/message/"+user.getUserId() + "/" + UUID.randomUUID().toString().replace("-","")+"."+fileExtName;

            InputStream inputStream = file.getInputStream();
            BufferedImage read = ImageIO.read(inputStream);
            int width = read.getWidth();
            int height = read.getHeight();

            minioUtils.upload(bucket,path,file.getInputStream());

            Map<String,Object> content = new HashMap<>();
            content.put("uri",prefix + path);
            content.put("width",width);
            content.put("height",height);

            SendMessageDTO dto = new SendMessageDTO();
            dto.setReceiver(vo.getReceiver());
            dto.setMsgType(2);
            dto.setContent(content);
            dto.setDate(System.currentTimeMillis());
            dto.setId(SnowFlake.id());
            dto.setSender(user.getUserId());

            messageService.doMessage(dto);
            return ResultBuilder.success(dto);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new LogicException(500,e.getMessage());
        }
    }
}
