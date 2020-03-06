/**
 * FileName: ContextLoaderListener.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/6 7:53
 * Description:
 */
package com.lagou.edu.listener;

import com.lagou.edu.factory.AnnoBeanFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    private AnnoBeanFactory annoBeanFactory;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        annoBeanFactory = new AnnoBeanFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        annoBeanFactory = null;
    }
}
