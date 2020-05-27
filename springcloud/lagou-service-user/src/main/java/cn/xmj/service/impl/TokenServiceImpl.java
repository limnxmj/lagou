/**
 * FileName: TokenServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 11:54
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.dao.TokenDao;
import cn.xmj.pojo.Token;
import cn.xmj.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenDao tokenDao;

    @Override
    public void saveToken(String email, String t) {
        Token token = new Token();
        token.setEmail(email);
        token.setToken(t);
        tokenDao.save(token);
    }

    @Override
    public Token getTokenByEmail(String email) {
        return getToken(email, null);
    }

    @Override
    public String getEmailByToken(String t) {
        Token token = getToken(null, t);
        if (token == null) {
            return "";
        }
        return token.getEmail();
    }

    private Token getToken(String email, String t) {
        Token token = new Token();
        token.setEmail(email);
        token.setToken(t);
        Example<Token> example = Example.of(token);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<Token> tokens = tokenDao.findAll(example, sort);
        return CollectionUtils.isEmpty(tokens) ? null : tokens.get(0);
    }
}
