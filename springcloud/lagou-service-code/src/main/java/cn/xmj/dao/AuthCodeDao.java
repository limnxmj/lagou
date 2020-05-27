/**
 * FileName: CodeDao.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:26
 * Description:
 */
package cn.xmj.dao;

import cn.xmj.pojo.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeDao extends JpaRepository<AuthCode, Integer> {

}
