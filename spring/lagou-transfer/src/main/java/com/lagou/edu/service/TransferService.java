package com.lagou.edu.service;

import com.lagou.edu.dao.annos.Service;

/**
 * @author 应癫
 */
public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
