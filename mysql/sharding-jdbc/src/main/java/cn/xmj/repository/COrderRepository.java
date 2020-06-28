/**
 * FileName: COrderRepository.java
 * Author:   limn_xmj@163.com
 * Date:     2020/6/28 9:44
 * Description:
 */
package cn.xmj.repository;

import cn.xmj.entity.COrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface COrderRepository extends JpaRepository<COrder, Long> {
}
