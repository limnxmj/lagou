/**
 * FileName: PersonNeo4jRepository.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/6 10:20
 * Description:
 */
package cn.xmj.repository;

import cn.xmj.bean.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonNeo4jRepository extends Neo4jRepository<Person, Long> {

    @Query("match p = (person:Person {name:{startName}}) - [*] - (person2:Person {name:{endName}} ) return p")
    List<Person> personList(@Param("startName") String startName, @Param("endName") String endName);
}
