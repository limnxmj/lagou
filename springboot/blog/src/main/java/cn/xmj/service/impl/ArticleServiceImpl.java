/**
 * FileName: ArticleServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/26 9:48
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.mapper.ArticleMapper;
import cn.xmj.pojo.Article;
import cn.xmj.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Page<Article> findArticlesByPage(Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 2;
        }
        return PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> {
            articleMapper.findArticlesByPage();
        });
    }
}
