package com.milchstrabe.rainbow.biz.controller;

import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.common.ServerNodesCache;
import com.milchstrabe.rainbow.biz.domain.vo.NodeVO;
import com.milchstrabe.rainbow.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ch3ng
 * @Date 2020/4/30 11:52
 * @Version 1.0
 * @Description
 **/
@Slf4j
@RestController
@RequestMapping("/server")
public class ServerController {


    @GetMapping(path = "/nodes")
    public Result<NodeVO> nodes() throws LogicException {

        Node node = ServerNodesCache.getNode();
        NodeVO nodeVO = NodeVO.builder().build();
        BeanUtils.copyProperties(node,nodeVO);
        return ResultBuilder.success(nodeVO);
    }
}
