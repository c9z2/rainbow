package com.milchstrabe.rainbow.biz.service;

import com.milchstrabe.rainbow.biz.domain.po.Contact;

import java.util.List;

public interface IContactService {

    List<Contact> list(String uid);

}
