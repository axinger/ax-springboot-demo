package com.xing.controller;

import com.axing.common.response.dto.Result;
import com.xing.entity.StudentInfo;
import com.xing.service.IStudentInfoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description：学生控制器
 * @Author :zks
 * @Date :10:51 2020/9/15
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private IStudentInfoService studentInfoService;

    /**
     * 保存学生信息
     *
     * @param studentInfo
     * @return
     */
    @PostMapping("save")
    public Result save(@RequestBody StudentInfo studentInfo) {
        studentInfoService.save(studentInfo);
        return Result.success();
    }

    /**
     * 根据主键id查找学生信息
     *
     * @param id
     * @return
     */
    @GetMapping("getStudentInfoById")
    public Result getStudentInfoById(@RequestParam("id") ObjectId id) {
        StudentInfo studentInfo = studentInfoService.getStudentInfoById(id);
        return Result.success(studentInfo);
    }

    /**
     * 根据所选课程id查找学生信息列表并分页
     *
     * @param courseId
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("getStudentInfoListByCourseId")
    public Result getStudentInfoListByCourseId(@RequestParam("courseId") Integer courseId,
                                               @RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer limit,
                                               @RequestParam(defaultValue = "addTime") String sort,
                                               @RequestParam(defaultValue = "desc") String order) {
        List<StudentInfo> studentInfoList = studentInfoService.getStudentInfoListByCourseId(courseId, page, limit, sort, order);
        return Result.success(studentInfoList);
    }

    /**
     * 更新学生信息
     *
     * @param studentInfo
     * @return
     */
    @PostMapping("update")
    public Result update(@RequestBody StudentInfo studentInfo) {
        studentInfoService.update(studentInfo);
        return Result.success();
    }

    /**
     * 根据学生id以及课程id删除选修课程
     *
     * @param id
     * @param courseId
     * @return
     */
    @GetMapping("deleteCourseByIdAndCourseId")
    public Result deleteCourseByIdAndCourseId(@RequestParam("id") ObjectId id, @RequestParam("courseId") Integer courseId) {
        studentInfoService.deleteCourseByIdAndCourseId(id, courseId);
        return Result.success();
    }

    /**
     * 删除学生信息
     *
     * @param id
     * @return
     */
    @GetMapping("delete")
    public Result delete(@RequestParam("id") ObjectId id) {
        studentInfoService.delete(id);
        return Result.success();
    }


}
