/**
 * FileName: IpDao.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 9:42
 * Description:
 */
package com.lagou.dao;

import java.io.Serializable;

public class IpDao implements Serializable {

    private String ip;
    private int port;
    private long time;
    private long lastTime;

    public IpDao() {
    }

    public IpDao(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.time = 0l;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "IpDao{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", time=" + time +
                ", lastTime=" + lastTime +
                '}';
    }
}
