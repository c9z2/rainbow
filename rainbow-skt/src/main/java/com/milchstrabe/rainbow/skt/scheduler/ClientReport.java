package com.milchstrabe.rainbow.skt.scheduler;

import com.milchstrabe.rainbow.server.domain.Node;
import com.milchstrabe.rainbow.skt.server.ServerByCurator;
import com.milchstrabe.rainbow.skt.server.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author ch3ng
 * @Date 2020/5/9 18:48
 * @Version 1.0
 * @Description
 **/
@Slf4j
@Component
public class ClientReport {

    @Autowired
    private ServerByCurator serverByCurator;

    Timer timer = new Timer();

    public ClientReport(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Integer countOnline = SessionManager.countOnline();
                try {
                    Node dataFromNode = serverByCurator.getDataFromNode();
                    dataFromNode.setPlayload(countOnline);
                    serverByCurator.setData2Node(dataFromNode);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        },0,1000 * 60 * 1);

    }


}
