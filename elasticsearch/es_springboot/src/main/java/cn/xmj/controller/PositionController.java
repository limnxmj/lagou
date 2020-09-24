package cn.xmj.controller;

import cn.xmj.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping({"/", "/index"})
    public String indexPage() {
        return "index";
    }

    @GetMapping("/search/{keyword}")
    @ResponseBody
    public List<Map<String, Object>> searchPosition(@PathVariable("keyword") String keyword) throws IOException {

        List<Map<String, Object>> positions = positionService.searchPos("positionName", keyword, 0, 5);

        if (positions.size() < 5) {
            //根据美女多或者员工福利好查询，补齐5条数据
            String queryStr = "美女多 OR 员工福利好";
            positions.addAll(positionService.searchPos("positionAdvantage", queryStr, 0, 5 - positions.size()));
        }

        return positions;
    }

    @RequestMapping("/importAll")
    @ResponseBody
    public String importAll() {
        try {
            positionService.importAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
