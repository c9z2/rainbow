package com.milchstrabe.rainbow.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.google.gson.Gson;
import com.milchstrabe.rainbow.biz.common.Result;
import com.milchstrabe.rainbow.biz.common.ResultBuilder;
import com.milchstrabe.rainbow.biz.domain.vo.NodeVO;
import com.milchstrabe.rainbow.biz.exception.InternalException;
import com.milchstrabe.rainbow.biz.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Autowired
    private CuratorFramework curatorFramework;

    private final static String ROOT_PATH = "/rainbow";

    @GetMapping(path = "/nodes")
    public Result<NodeVO> nodes() throws InternalException {
        try {
            List<String> nodes = curatorFramework.getChildren().forPath(ROOT_PATH);
            if(nodes == null || nodes.size() == 0){
                throw new LogicException("no server node available");
            }
            List<Node> nodeList = new ArrayList<>();
            Gson gson = null;
            for (String node : nodes){
                byte[] bytes = curatorFramework.getData().forPath(ROOT_PATH + "/" + node);
                gson = new Gson();
                Node znode = gson.fromJson(new String(bytes, Charset.forName("utf-8")), Node.class);
                nodeList.add(znode);
            }

            Collections.sort(nodeList, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if(o1.getPlayload() > o2.getPlayload()){
                        return 1;
                    }else if(o1.getPlayload() < o2.getPlayload()){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });

            Node node = nodeList.get(0);
            NodeVO nodeVO = NodeVO.builder().build();
            BeanUtil.copyProperties(node,nodeVO);
            return ResultBuilder.success(nodeVO);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalException(e.getMessage());
        }
    }
}
