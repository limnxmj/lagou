/**
 * FileName: Article.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/26 9:18
 * Description:
 */
package cn.xmj.pojo;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {

    private Integer id;
    //文章标题
    private String title;
    //文章具体内容
    private String content;
    //发表时间
    private Date created;
    //修改时间
    private Date modified;
    //文章分类
    private String categories;
    //文章标签
    private String tags;
    //是否允许评论
    private Integer allowComment;
    //文章缩略图
    private String thumbnail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", categories='" + categories + '\'' +
                ", tags='" + tags + '\'' +
                ", allowComment=" + allowComment +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
