package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.ServerNodesCache;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.biz.domain.po.User;
import com.milchstrabe.rainbow.biz.domain.vo.ContactVO;
import com.milchstrabe.rainbow.biz.domain.vo.NodeVO;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ch3ng
 * @Date 2020/5/12 17:09
 * @Version 1.0
 * @Description
 **/
@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private IContactService contactService;

    @GetMapping(path = APIVersion.V_1 + "/list")
    public Result<ContactVO> list(@CurrentUser RequestUser user) {
        String uid = user.getUserId();
        List<Contact> list = contactService.list(uid);
        List<ContactVO> result = new ArrayList<>();
        list.stream().forEach(contact -> {
            ContactVO contactVO = ContactVO.builder().build();
            BeanUtils.copyProperties(contact,contactVO);
            contactVO.setUserId(contact.getUser().getUserId());
            contactVO.setUsername(contact.getUser().getUsername());
            contactVO.setAvatar(contact.getUser().getProperty().getAvatar());
            result.add(contactVO);
        });
        return ResultBuilder.success(result);
    }


}
