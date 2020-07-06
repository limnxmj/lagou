package cn.xmj.dao;

import cn.xmj.bean.Resume;

import java.util.List;

public interface ResumeDao {

    void insertResume(Resume resume);

    Resume findByName(String name);

    List<Resume> findListByName(String name);

    List<Resume> findListByNameAndExpectSalary(String name, double expectSalary);
}
