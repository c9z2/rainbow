package com.milchstrabe.rainbow.biz.service.impl;

import com.milchstrabe.rainbow.biz.domain.po.Contact;
import com.milchstrabe.rainbow.biz.mapper.IContactMappper;
import com.milchstrabe.rainbow.biz.service.IContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author ch3ng
 * @Date 2020/5/12 20:36
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    private IContactMappper contactMappper;

    @Override
    public List<Contact> list(String uid) {
        List<Contact> list = contactMappper.list(uid);
        return list;
    }
}
