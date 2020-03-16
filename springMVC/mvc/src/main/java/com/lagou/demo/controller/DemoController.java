package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.LagouAutowired;
import com.lagou.edu.mvcframework.annotations.LagouController;
import com.lagou.edu.mvcframework.annotations.LagouRequestMapping;
import com.lagou.edu.mvcframework.annotations.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@LagouController
@LagouRequestMapping("/demo")
@Security("zhangsan")
public class DemoController {


    @LagouAutowired
    private IDemoService demoService;


    /**
     * URL: /demo/query?username=lisi
     *
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }

    @LagouRequestMapping("/handle01")
    @Security({"lisi", "wangwu"})
    public String handle01(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }

    @LagouRequestMapping("/handle02")
    @Security({"lisi"})
    public String handle02(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }

    @LagouRequestMapping("/handle03")
    @Security({"wangwu"})
    public String handle03(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }
}
