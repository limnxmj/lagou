/**
 * FileName: ResumeController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 14:17
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.dto.Resume;
import cn.xmj.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/queryResumeList")
    @ResponseBody
    public List<Resume> queryResumeList() {
        return resumeService.queryResumeList();
    }
}
