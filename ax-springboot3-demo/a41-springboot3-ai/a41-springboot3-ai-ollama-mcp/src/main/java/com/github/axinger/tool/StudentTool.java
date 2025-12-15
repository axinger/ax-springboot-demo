package com.github.axinger.tool;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.db.domain.StudentEntity;
import com.github.axinger.db.service.StudentService;
import com.github.axinger.model.StudentPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentTool {

    private final StudentService studentService;


    @Tool(name = "queryListByName", description = "根据学生姓名模糊查询学生信息")
    public List<StudentEntity> queryListByName(@ToolParam(description = "学生姓名") String name) {
        log.info("开始根据姓名模糊查询学生信息，姓名: {}", name);
        // 处理查询条件
        List<StudentEntity> list = studentService.lambdaQuery()
                .like(name != null && !name.isEmpty(), StudentEntity::getName, name)
                .list();
        log.info("查询结果：{}", list);
        return list;
    }


    @McpTool(name = "pageInfo", description = "根据条件分页查询学生信息")
    public IPage<StudentEntity> pageInfo(@McpToolParam(description = "学生分页信息") StudentPageDTO pageDTO) {
        IPage<StudentEntity> page = new Page<>();
        page.setCurrent(pageDTO.getCurrent());
        page.setSize(pageDTO.getSize());
        // 处理查询条件
        LambdaQueryWrapper<StudentEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StudentEntity::getSex, pageDTO.getSex());
        wrapper.like(StudentEntity::getName, pageDTO.getName());
        wrapper.like(StudentEntity::getClassRoom, pageDTO.getClassRoom());
        wrapper.like(StudentEntity::getAddress, pageDTO.getAddress());
        wrapper.orderByDesc(StudentEntity::getId);


        IPage<StudentEntity> page1 = studentService.page(page, wrapper);
        log.info("分页查询查询结果：{}", page1);
        return page1;
    }

}