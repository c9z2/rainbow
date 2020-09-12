package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.CurrentUser;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.constant.APIVersion;
import com.milchstrabe.rainbow.biz.domain.RequestUser;
import com.milchstrabe.rainbow.biz.domain.dto.ModifiedContactRemarkDTO;
import com.milchstrabe.rainbow.biz.domain.vo.ModifiedContactRemarkVO;
import com.milchstrabe.rainbow.biz.service.IContactService;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.exception.ParamMissException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/6/5 21:56
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/contact")
public class ModifiedContactRemarkController {

    @Autowired
    private IContactService contactService;


    /**
     *
     * @param user
     * @return
     * @throws LogicException
     * @throws ParamMissException
     */
    @PostMapping(path = APIVersion.V_1 + "/remark")
    public Result<String> property(@CurrentUser RequestUser user,
                                 @RequestBody ModifiedContactRemarkVO vo) throws LogicException {

        ModifiedContactRemarkDTO dto = new ModifiedContactRemarkDTO();
        dto.setContactId(vo.getContactId());
        dto.setUserId(user.getUserId());
        dto.setRemark(vo.getRemark());

        contactService.modifiedContactRemark(dto);
        return ResultBuilder.success();
    }
}
