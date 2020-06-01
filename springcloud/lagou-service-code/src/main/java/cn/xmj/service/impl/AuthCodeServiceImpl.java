/**
 * FileName: AuthCodeServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 10:46
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.dao.AuthCodeDao;
import cn.xmj.pojo.AuthCode;
import cn.xmj.service.AuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class AuthCodeServiceImpl implements AuthCodeService {

    @Autowired
    private AuthCodeDao authCodeDao;

    @Override
    public void saveAuthCode(String email, int code) {
        AuthCode authCode = new AuthCode();
        authCode.setEmail(email);
        authCode.setCode(String.valueOf(code));
        Calendar nowTime = Calendar.getInstance();
        Date date = new Date();
        nowTime.setTime(date);
        authCode.setCreatetime(nowTime.getTime());
        nowTime.add(Calendar.MINUTE, 10);
        authCode.setExpiretime(nowTime.getTime());
        authCodeDao.save(authCode);
    }

    /**
     * 校验验证码是否正确，0正确 1错误 2超时
     */
    @Override
    public int validateCode(String email, String code) {
        AuthCode authCode = new AuthCode();
        authCode.setEmail(email);
        Example<AuthCode> example = Example.of(authCode);

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<AuthCode> authCodes = authCodeDao.findAll(example, sort);

        Calendar nowTime = Calendar.getInstance();
        Date date = new Date();
        nowTime.setTime(date);
        if (CollectionUtils.isEmpty(authCodes) || !Objects.equals(authCodes.get(0).getCode(), code)) {
            return 1;
        }
        if (authCodes.get(0).getExpiretime().before(nowTime.getTime())) {
            return 2;
        }
        return 0;
    }
}
