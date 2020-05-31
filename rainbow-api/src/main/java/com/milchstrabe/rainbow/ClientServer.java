package com.milchstrabe.rainbow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @Author ch3ng
 * @Date 2020/4/28 17:23
 * @Version 1.0
 * @Description
 **/
@Setter
@Getter
@Builder
public class ClientServer {

    private String host;
    private Integer port;
    private String cid;
    private String cType;

    public ClientServer() {
    }

    public ClientServer(String host, Integer port, String cid, String cType) {
        this.host = host;
        this.port = port;
        this.cid = cid;
        this.cType = cType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientServer that = (ClientServer) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(cid, that.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, cid);
    }
}
