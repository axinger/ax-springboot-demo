package com.github.axinger.tool;

import com.github.axinger.db.domain.StudentEntity;
import com.github.axinger.model.StudentPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentTool {


    @Tool(name = "queryListByNameTool", description = "根据学生姓名模糊查询学生信息")
    public List<StudentEntity> queryListByName(@ToolParam(description = "学生姓名") String name) {
        log.info("开始根据姓名模糊查询学生信息，姓名: {}", name);
        // 处理查询条件
        List<StudentEntity> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setName(name + i);
            studentEntity.setAge(18 + i);
            studentEntity.setSex("男");
            studentEntity.setClassRoom("1" + i);
            studentEntity.setAddress("上海" + i);
            list.add(studentEntity);
        }

        log.info("查询结果：{}", list);
        return list;
    }

    @Tool(name = "queryListByAgeTool", description = "根据学生年龄模糊查询学生信息")
    public List<StudentEntity> queryListByAge(@ToolParam(description = "学生年龄") Integer age) {
        log.info("开始根据年龄模糊查询学生信息，年龄: {}", age);
        // 处理查询条件
        List<StudentEntity> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setAge(age + i);
            studentEntity.setSex("男");
            studentEntity.setClassRoom("1" + i);
            studentEntity.setAddress("上海" + i);
            list.add(studentEntity);
        }

        log.info("查询结果：{}", list);
        return list;
    }

    /// 根据学生指定条件模糊查询学生信息,age等于10
    @Tool(name = "queryListByDTOTool", description = "根据学生指定条件模糊查询学生信息")
    public List<StudentEntity> queryListBySexTool(@ToolParam(description = "学生指定条件") StudentPageDTO dto) {
        log.info("开始根据年龄模糊查询学生信息，年龄: {}", dto);
        // 处理查询条件
        List<StudentEntity> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setSex(dto.getSex() + i);
            studentEntity.setClassRoom("1" + i);
            studentEntity.setAddress("上海" + i);
            list.add(studentEntity);
        }

        log.info("查询结果：{}", list);
        return list;
    }

}
