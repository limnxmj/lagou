/**
 * FileName: ResumeServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 14:20
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.dto.Resume;
import cn.xmj.mapper.ResumeMapper;
import cn.xmj.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    public List<Resume> queryResumeList() {
        return resumeMapper.queryResumeList();
    }

}
