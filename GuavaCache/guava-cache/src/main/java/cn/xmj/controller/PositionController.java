/**
 * FileName: PositionController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 22:02
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.pojo.Position;
import cn.xmj.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @RequestMapping("/index")
    public String index(Model model) {

        List<Position> positions = positionService.findPositions();
        model.addAttribute("positions", positions);
        return "position/index";
    }

    /**
     * 刷新db数据到redis和guavacache
     */
    @RequestMapping("/flushCache")
    @ResponseBody
    public String flushCache() {
        positionService.flushCache();
        return "success";
    }

    /**
     * 清空redis和guavacache
     */
    @RequestMapping("/clearCache")
    @ResponseBody
    public String clearCache() {
        positionService.clearRedis();
        positionService.clearGuava();
        return "success";
    }

    @RequestMapping("/clearRedis")
    @ResponseBody
    public String clearRedis() {
        positionService.clearRedis();
        return "success";
    }

    @RequestMapping("/clearGuava")
    @ResponseBody
    public String clearGuava() {
        positionService.clearGuava();
        return "success";
    }


}
