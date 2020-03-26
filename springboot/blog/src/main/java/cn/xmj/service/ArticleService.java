/**
 * FileName: ArticleService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/26 9:47
 * Description:
 */
package cn.xmj.service;

import cn.xmj.pojo.Article;
import com.github.pagehelper.Page;

public interface ArticleService {

    public Page<Article> findArticlesByPage(Integer pageNum, Integer pageSize);
}
