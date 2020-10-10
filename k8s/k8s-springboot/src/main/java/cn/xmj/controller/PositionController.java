package cn.xmj.controller;

import cn.xmj.entity.Position;
import cn.xmj.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @RequestMapping("/list")
    public List<Position> list() {

        return positionService.list();
    }

    @RequestMapping("/add/{name}/{salary}/{city}")
    public String add(@PathVariable(name = "name") String name,
                      @PathVariable(name = "salary") String salary,
                      @PathVariable(name = "city") String city
    ) {
        Position position = new Position(null, name, salary, city);
        positionService.add(position);
        return "add success";
    }

    @RequestMapping("/update/{id}/{name}/{salary}/{city}")
    public String update(@PathVariable(name = "id") long id,
                         @PathVariable(name = "name") String name,
                         @PathVariable(name = "salary") String salary,
                         @PathVariable(name = "city") String city
    ) {

        Position position = new Position(id, name, salary, city);
        positionService.update(position);
        return "update success";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        positionService.delete(id);
        return "delete success";
    }

}
