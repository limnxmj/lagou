package cn.xmj.controller;

import cn.xmj.bean.Position;
import cn.xmj.service.PositionSolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionSolrService positionSolrService;

    @RequestMapping("/query/{position}")
    public List<Position> query(@PathVariable("position") String position) {

        //根据positionName查询5条数据
        List<Position> positions =
                positionSolrService.queryPositionsFromSolr("positionName:" + position, 0, 5);
        if (positions.size() < 5) {
            //根据美女多或者员工福利好查询，补齐5条数据
            String queryStr = "positionAdvantage:美女多 OR positionAdvantage:员工福利好";
            positions.addAll(positionSolrService.queryPositionsFromSolr(queryStr, 0, 5 - positions.size()));
        }
        return positions;
    }
}
