/**
 * FileName: ResumeService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 10:07
 * Description:
 */
package cn.xmj.service;

import cn.xmj.pojo.Resume;

import java.util.List;

public interface ResumeService {

    public List<Resume> findAll();

    public Resume findById(Long id);

    public void saveResume(Resume resume);

    public void deleteResume(Long id);
}
