/**
 * FileName: ArticleController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/26 10:08
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.pojo.Article;
import cn.xmj.service.ArticleService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/index")
    public String findArticlesByPage(Model model, Integer pageNum, Integer pageSize) {
        Page<Article> page = articleService.findArticlesByPage(pageNum, pageSize);
        model.addAttribute("articles", page.getResult());
        model.addAttribute("pageInfo", page.toPageInfo());
        return "client/index";
    }
}
