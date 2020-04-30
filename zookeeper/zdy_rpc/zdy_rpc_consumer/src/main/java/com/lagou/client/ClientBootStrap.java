package com.lagou.client;

import com.lagou.service.UserService;

public class ClientBootStrap {

    public static void main(String[] args){

        RpcConsumer rpcConsumer = new RpcConsumer();
        UserService proxy = (UserService) rpcConsumer.createProxy(UserService.class);

        while (true){
            try {
                Thread.sleep(2000);
                System.out.println(proxy.sayHello("are you ok?"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}
