/**
 * FileName: PersonNeo4jService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/6 10:23
 * Description:
 */
package cn.xmj.service;

import cn.xmj.bean.Person;
import cn.xmj.repository.PersonNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonNeo4jService {

    @Autowired
    private PersonNeo4jRepository personNeo4jRepository;

    public List<Person> personList(String startName, String endName) {
        return personNeo4jRepository.personList(startName, endName);
    }

    public Map<String, Object> personRelationShip(String startName, String endName) {
        return toD3Format(personList(startName, endName));
    }

    private Map<String, Object> toD3Format(List<Person> personList) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();

        if (!CollectionUtils.isEmpty(personList)) {
            for (Person person : personList) {
                nodes.add(map("name", person.getName(), "pid", person.getPid()));

                if (!CollectionUtils.isEmpty(person.getFriendsPerson())) {
                    for (Person friend : person.getFriendsPerson()) {
                        rels.add(relaMap(person, friend, "好友"));
                    }
                }
                if (!CollectionUtils.isEmpty(person.getFatherPerson())) {
                    for (Person father : person.getFatherPerson()) {
                        rels.add(relaMap(person, father, "父子"));
                    }
                }
            }
        }
        return map("nodes", nodes, "edges", rels);
    }

    private Map<String, Object> map(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    private Map<String, Object> relaMap(Person person, Person relaPerson, String rela) {
        Map<String, Object> relMap = new HashMap<>();
        relMap.put("source", person.getPid());
        relMap.put("target", relaPerson.getPid());
        relMap.put("relation", rela);
        relMap.put("value", 1);
        return relMap;
    }
}
