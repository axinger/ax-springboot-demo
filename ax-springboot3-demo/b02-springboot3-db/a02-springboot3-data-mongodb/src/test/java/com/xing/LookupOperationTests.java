package com.xing;

import com.xing.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName LookupOperationTests.java
 * @description TODO
 * @createTime 2022年06月13日 10:40:00
 */
@SpringBootTest
@Slf4j
public class LookupOperationTests {
    @Autowired
    private MongoTemplate mongotemplate;

    // 关联查询的测试，我们以学生、班级、学校为测试模型，
    // 进行一对多、多对一、多对多等情况下的关联查询

    @Test
    public void addStudent() {
        List<Student> students = new ArrayList<>();
        Student student = Student.builder().id(1).username("zhangsan").classId(1).build();
        Student student1 = Student.builder().id(2).username("lisi").classId(2).build();
        Student student2 = Student.builder().id(3).username("wangwu").classId(2).build();
        students.add(student);
        students.add(student1);
        students.add(student2);
        mongotemplate.insertAll(students);
    }

    @Test
    public void addStudentClass() {
        List<StudentClass> studentClasses = new ArrayList<>();
        StudentClass studentClass = new StudentClass(1, "一班", 1);
        StudentClass studentClass1 = new StudentClass(2, "二班", 2);
        studentClasses.add(studentClass);
        studentClasses.add(studentClass1);
        mongotemplate.insertAll(studentClasses);
    }

    @Test
    public void addSchool() {
        List<School> schoolList = new ArrayList<>();
        School school = new School(1, "一中");
        School school1 = new School(2, "二中");
        schoolList.add(school);
        schoolList.add(school1);
        mongotemplate.insertAll(schoolList);
    }

    // 先看下LookupOperation的使用方法：
    // 但是源码中不推荐使用这种方式，建议使用静态工厂方法Aggregation.lookup(String, String, String, String)而不是直接创建此类的实例，
    // 以下我们都会使用静态工厂方法来创建LookupOperation。

    /**
     * 学生与班级关联(学生为主表) - 多对一
     */
    @Test
    public void test() {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("studentClass")// 关联从表名
                .localField("classId")// 主表中的关联字段
                .foreignField("_id")// 从表关联的字段
                .as("classStudents");// 查询结果名
        // 源码中建议使用静态工厂方法Aggregation.lookup(String, String, String, String)而不是直接创建此类的实例
        LookupOperation lookup = Aggregation.lookup("studentClass",
                "classId",
                "_id",
                "classStudents");

        Aggregation aggregation = Aggregation.newAggregation(lookup);
        // 使用Map接收结果
        AggregationResults<Map> results = mongotemplate.aggregate(aggregation,
                "student",
                Map.class);
        System.out.println("使用Map接收结果 = " + results.getMappedResults());
        // 使用对象接收结果
        AggregationResults<ClassStudentsDto> results1 = mongotemplate.aggregate(aggregation,
                "student",
                ClassStudentsDto.class);
        System.out.println("使用对象接收结果 = " + results1.getMappedResults());
    }

    // 查询条件来自主表
    @Test
    public void test1() {
        LookupOperation lookup = Aggregation.lookup("studentClass", "classId", "_id",
                "classStudents");
        // 追加查询条件 -- 查询条件来自主表
        Criteria criteria = Criteria.where("classId").is(2);
        // 将筛选条件放入管道
        MatchOperation match = Aggregation.match(criteria);
        Aggregation aggregation1 = Aggregation.newAggregation(lookup, match);
        AggregationResults<ClassStudentsDto> results2 = mongotemplate.aggregate(aggregation1,
                "student", ClassStudentsDto.class);
        System.out.println(results2.getMappedResults());
    }

    @Test
    public void test6() {
        LookupOperation lookup = Aggregation.lookup("student", "_id", "classId",
                "studentList");
        Aggregation aggregation = Aggregation.newAggregation(lookup);
        AggregationResults<StudentDto> results = mongotemplate.aggregate(aggregation, "studentClass"
                , StudentDto.class);
        System.out.println(results.getMappedResults());
    }

    /**
     * 多表关联查询时 可能从表会是下一次查询的主表  比如学员关联班机、班级关联学校，班级关联学校时 班级表就成了主表
     * 那么在班级表和学校表关联时 localField就不能直接写schoolId 而是通过上一步的结果集来取值
     * 如：classStudents.schoolId
     * 同样我们想只返回指定字段时，也需要通过上一步的结果集来取想要的属性，
     * 如：classStudents.className schoolClasses.schoolName
     */
    @Test
    public void test7() {
        // 学员表关联班级表
        LookupOperation lookup = Aggregation.lookup("studentClass", "classId", "_id",
                "classStudents");
        // 班级表关联学校表
        LookupOperation lookup1 = Aggregation.lookup("school", "classStudents.schoolId", "_id",
                "schoolClasses");
        Aggregation aggregation = Aggregation.newAggregation(lookup, lookup1);
        AggregationResults<Map> results = mongotemplate.aggregate(aggregation, "student",
                Map.class);
        System.out.println(results);
        AggregationResults<Map> results1 = mongotemplate.aggregate(aggregation,
                "student", Map.class);
        // [StudentClassSchoolDto(id=1, username=zhangsan, classStudents=StudentClass(id=1, className=class one, schoolId=1), schoolClasses=School(id=1, schoolName=一中)), StudentClassSchoolDto(id=2, username=lisi, classStudents=StudentClass(id=2, className=class two, schoolId=2), schoolClasses=School(id=2, schoolName=二中)), StudentClassSchoolDto(id=3, username=wangwu, classStudents=StudentClass(id=2, className=class two, schoolId=2), schoolClasses=School(id=2, schoolName=二中))]
        System.out.println(results1.getMappedResults());
        // 只返回指定字段
        ProjectionOperation project = Aggregation.project("id", "username", "classStudents.className", "schoolClasses.schoolName");
        Aggregation aggregation1 = Aggregation.newAggregation(lookup, lookup1, project);
        AggregationResults<Map> results2 = mongotemplate.aggregate(aggregation1,
                "student", Map.class);
        // [StudentClassSchoolDto(id=1, username=zhangsan, className=class one, schoolName=一中), StudentClassSchoolDto(id=2, username=lisi, className=class two, schoolName=二中), StudentClassSchoolDto(id=3, username=wangwu, className=class two, schoolName=二中)]
        System.out.println(results2.getMappedResults());
    }
}
