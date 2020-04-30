/**
 * FileName: ResumeMapper.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 14:21
 * Description:
 */
package cn.xmj.mapper;


import cn.xmj.dto.Resume;

import java.util.List;

public interface ResumeMapper {
    public List<Resume> queryResumeList();
}
