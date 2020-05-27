/**
 * FileName: TokenService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 11:53
 * Description:
 */
package cn.xmj.service;

import cn.xmj.pojo.Token;

public interface TokenService {

    public void saveToken(String email, String token);

    public Token getTokenByEmail(String email);

    public String getEmailByToken(String token);
}
