/**
 * FileName: ResumeController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 10:11
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.pojo.Resume;
import cn.xmj.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/findAll")
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView("resume");
        mv.addObject("resumes", resumeService.findAll());
        return mv;
    }

    @RequestMapping("/add")
    public String add() {
        return "resumeEdit";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("resumeEdit");
        mv.addObject("resume", resumeService.findById(id));
        return mv;
    }

    @RequestMapping("/save")
    public String edit(Resume resume) {
        resumeService.saveResume(resume);
        return "redirect:/resume/findAll";
    }

    @RequestMapping("/delete")
    public String delete(Long id) {
        resumeService.deleteResume(id);
        return "redirect:/resume/findAll";
    }

}
