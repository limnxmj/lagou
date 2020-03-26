package cn.xmj.blog;

import cn.xmj.pojo.Article;
import cn.xmj.service.ArticleService;
import com.github.pagehelper.Page;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private ArticleService articleService;

    @Test
    void contextLoads() {
        Page<Article> articlesByPage = articleService.findArticlesByPage(1, 2);
        System.out.println(articlesByPage.getResult());
    }

}
