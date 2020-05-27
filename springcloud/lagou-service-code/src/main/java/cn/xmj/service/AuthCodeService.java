/**
 * FileName: AuthCodeService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:28
 * Description:
 */
package cn.xmj.service;

public interface AuthCodeService {

    public void saveAuthCode(String email, int code);

    public int validateCode(String email, String code);
}
