/**
 * FileName: PersonNeo4jController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/6 10:48
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.bean.Person;
import cn.xmj.service.PersonNeo4jService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/person")
public class PersonNeo4jController {

    @Autowired
    private PersonNeo4jService personNeo4jService;

    @RequestMapping("/personList")
    @ResponseBody
    public List<Person> personList(String startName, String endName) {
        return personNeo4jService.personList(startName, endName);
    }

    @RequestMapping("/graph")
    public String graph(Model mv, String startName, String endName) {
        mv.addAttribute("personRelationShip", JSONObject.toJSONString(personNeo4jService.personRelationShip(startName, endName)));
        return "person/index";
    }
}
