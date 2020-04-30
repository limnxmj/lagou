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

//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1588221293623l)));
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1588105305753l)));
    }




}
