/**
 * FileName: IpUtil.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 9:11
 * Description:
 */
package com.lagou.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpUtil {

    public static String getIp() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {

                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        if (ip.getHostAddress().startsWith("127") || (ip.getHostAddress().startsWith("192") && ip.getHostAddress().lastIndexOf(".1") != -1)) {
                            continue;
                        }
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    public static void main(String[] args) {
        System.out.println(IpUtil.getIp());
    }
}