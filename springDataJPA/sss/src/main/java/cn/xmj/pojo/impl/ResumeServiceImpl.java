/**
 * FileName: ResumeServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 10:08
 * Description:
 */
package cn.xmj.pojo.impl;

import cn.xmj.dao.ResumeDao;
import cn.xmj.pojo.Resume;
import cn.xmj.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public List<Resume> findAll() {
        return resumeDao.findAll();
    }

    @Override
    public Resume findById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Resume> optionalResume = resumeDao.findById(id);
        return optionalResume == null ? null : optionalResume.get();
    }

    @Override
    public void saveResume(Resume resume) {
        if (resume == null) {
            return;
        }
        resumeDao.save(resume);
    }

    @Override
    public void deleteResume(Long id) {
        if (id == null) {
            return;
        }
        resumeDao.deleteById(id);
    }
}
