/**
 * FileName: ResumeDao.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 9:54
 * Description:
 */
package cn.xmj.dao;

import cn.xmj.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResumeDao extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {

}
