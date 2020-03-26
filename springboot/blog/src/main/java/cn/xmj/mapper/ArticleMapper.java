/**
 * FileName: ArticleMapper.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/26 9:23
 * Description:
 */
package cn.xmj.mapper;

import cn.xmj.pojo.Article;

import java.util.List;

public interface ArticleMapper {

    public List<Article> findArticlesByPage();
}
