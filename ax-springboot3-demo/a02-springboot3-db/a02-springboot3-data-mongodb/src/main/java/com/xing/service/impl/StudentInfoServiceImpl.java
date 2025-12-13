package com.xing.service.impl;

import com.xing.entity.StudentInfo;
import com.xing.service.IStudentInfoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description：学生Service实现类
 * @Author :zks
 * @Date :9:59 2020/9/15
 */
@Service
public class StudentInfoServiceImpl implements IStudentInfoService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void save(StudentInfo studentInfo) {
        mongoTemplate.save(studentInfo);
    }

    @Override
    public StudentInfo getStudentInfoById(ObjectId id) {
        StudentInfo studentInfo = mongoTemplate.findById(id, StudentInfo.class);
        return studentInfo;
    }

    @Override
    public List<StudentInfo> getStudentInfoListByCourseId(Integer courseId, Integer page, Integer limit, String sort, String order) {
        // 查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("chooseCourse.courseId").is(courseId).and("chooseCourse.deleted").is(false)
                .and("deleted").is(false));
        // 分页
        query.skip((long) (page - 1) * limit).limit(limit);
        // 排序
        query.with(Sort.by(Sort.Direction.fromString(order), sort));
        // springboot2.2.1（含）以上的版本Sort已经不能再实例化了，构造方法已经是私有的了！
        // query.with(Sort.by(Sort.Direction.fromString(order), sort));
        ArrayList<StudentInfo> studentInfos = (ArrayList<StudentInfo>) mongoTemplate.find(query, StudentInfo.class);
        return studentInfos;
    }

    @Override
    public void update(StudentInfo studentInfo) {
        // 查询条件
        Query query = Query.query(Criteria.where("_id").is(studentInfo.getId()).and("deleted").is(false));
        // 需要修改的属性
        Update update = new Update();
        update.set("name", studentInfo.getName());
        update.set("age", studentInfo.getAge());
        update.set("updateTime", LocalDateTime.now());
        mongoTemplate.updateFirst(query, update, StudentInfo.class);
    }

    @Override
    public void deleteCourseByIdAndCourseId(ObjectId id, Integer courseId) {
        // 查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id).and("chooseCourse.courseId").is(courseId));
        // 需要修改的属性
        Update update = Update.update("chooseCourse.$.deleted", true);// 注意：在这里因为涉及到数组中的文档操作，需要加位置界定符"$"
        mongoTemplate.updateFirst(query, update, StudentInfo.class);
    }

    @Override
    public void delete(ObjectId id) {
        // 查询条件
        Query query = Query.query(Criteria.where("_id").is(id).and("deleted").is(false));
        // 需要修改的属性
        Update update = Update.update("deleted", true);
        mongoTemplate.updateFirst(query, update, StudentInfo.class);
    }
}

