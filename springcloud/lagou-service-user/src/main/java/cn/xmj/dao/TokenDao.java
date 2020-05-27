/**
 * FileName: TokenDao.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 11:53
 * Description:
 */
package cn.xmj.dao;

import cn.xmj.pojo.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token, Integer> {
}
