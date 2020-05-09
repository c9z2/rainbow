package com.milchstrabe.rainbow.skt.scheduler;

import com.milchstrabe.rainbow.server.domain.Node;
import com.milchstrabe.rainbow.skt.server.ServerByCurator;
import com.milchstrabe.rainbow.skt.server.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    /**
     * *    *    *    *    *    *    *
     * -    -    -    -    -    -    -
     * |    |    |    |    |    |    |
     * |    |    |    |    |    |    + year [optional]
     * |    |    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7) OR sun,mon,tue,wed,thu,fri,sat
     * |    |    |    |    +---------- month (1 - 12) OR jan,feb,mar,apr ...
     * |    |    |    +--------------- day of month (1 - 31)
     * |    |    +-------------------- hour (0 - 23)
     * |    +------------------------- min (0 - 59)
     * +------------------------------ second (0 - 59)
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void run(){
        Integer countOnline = SessionManager.countOnline();
        try {
            Node dataFromNode = serverByCurator.getDataFromNode();
            dataFromNode.setPlayload(countOnline);
            serverByCurator.setData2Node(dataFromNode);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
