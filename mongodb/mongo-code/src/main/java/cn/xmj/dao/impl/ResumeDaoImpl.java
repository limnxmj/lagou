package cn.xmj.dao.impl;

import cn.xmj.bean.Resume;
import cn.xmj.dao.ResumeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeDaoImpl implements ResumeDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertResume(Resume resume) {
//        mongoTemplate.insert(resume);
        mongoTemplate.insert(resume, "lg_resume_datas");
    }

    @Override
    public Resume findByName(String name) {
        List<Resume> datas = findListByName(name);
        return datas.isEmpty() ? null : datas.get(0);
    }

    @Override
    public List<Resume> findListByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Resume.class, "lg_resume_datas");
    }

    @Override
    public List<Resume> findListByNameAndExpectSalary(String name, double expectSalary) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name).andOperator(Criteria.where("expectSalary").is(expectSalary)));
        return mongoTemplate.find(query, Resume.class, "lg_resume_datas");
    }
}
