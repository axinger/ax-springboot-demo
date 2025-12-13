package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.dao.EduCourseDAO;
import com.github.axinger.dao.EduStudentDAO;
import com.github.axinger.dao.SysUserAddressDTO;
import com.github.axinger.dao.SysUsersDAO;
import com.github.axinger.dto.Gender;
import com.github.axinger.entity.EduCourse;
import com.github.axinger.entity.EduStudent;
import com.github.axinger.entity.SysUser;
import com.github.axinger.entity.SysUserAddress;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * （1）CrudRepository和PagingAndSortingRepository由Spring Data提供；
 * （2）
 * JpaRepository 由Spring Data JPA提供，而Spring Data JPA又是Spring Data的一个子项目，这就是两者的关系
 * （3）存在继承关系：
 * PagingAndSortingRepository 继承 CrudRepository
 * 　　JpaRepository 继承 PagingAndSortingRepository
 * 也就是说， CrudRepository 提供基本的增删改查；PagingAndSortingRepository 提供分页和排序方法；JpaRepository 提供JPA需要的方法
 */
@SpringBootTest
class JpaDemoApplicationTest {

    @Autowired
    SysUserAddressDTO sysUserAddressDTO;
    @Autowired
    EduCourseDAO subjectDao;
    @Autowired
    EduStudentDAO studentDao;
    @Autowired
    private SysUsersDAO sysUsersDao;

    /**
     * 2.一对多 Users 有多个 Address
     */
    @Test
    void test_01_addOne() {
        SysUser user = new SysUser();
//        user.setId(1L); ///指定id无效,默认自增,和mybatis不一样
        user.setUsername("jim");
        sysUsersDao.save(user);
    }

    /// id存在就会更新,使用外键方式直接赋值,会自动更新
    @Test
    void test_02() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("jim");
        user.setSex(Gender.FEMALE);
        user.setUserAddress(List.of(
                        SysUserAddress.builder()
//                        .id(1L)
                                .city("上海")
                                .street("上海路")
                                .detail("上海路1号")
                                .sysUserId(user.getId())
                                .build(),
                        SysUserAddress.builder()
//                        .id(2L)
                                .city("上海")
                                .street("上海路")
                                .detail("上海路2号")
                                .sysUserId(user.getId())
                                .build())
        );
        sysUsersDao.saveAndFlush(user);
    }

    /// 不使用外键
    @Test
    void test_03() {

        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("张三");
        user.setSex(Gender.FEMALE);

        List<SysUserAddress> userAddress = List.of(
                SysUserAddress.builder()
                        .id(1L)
                        .city("上海")
                        .street("上海路")
                        .detail("上海路1号")
                        .sysUserId(user.getId())
                        .build(),
                SysUserAddress.builder()
                        .id(2L)
                        .city("上海")
                        .street("上海路")
                        .detail("上海路2号")
                        .sysUserId(user.getId())
                        .build());
        /// 返回值会有自增id,然后添加到userAddress中
        List<SysUserAddress> savedAll = sysUserAddressDTO.saveAll(userAddress);
        user.setUserAddress(savedAll);
        sysUsersDao.saveAndFlush(user);
    }

    @Test
    void test_02_addList() {

        List<SysUser> list = new ArrayList<>();
        {
            SysUser user = new SysUser();
            user.setId(2L);
            user.setUsername("李四");
            list.add(user);
        }
        {
            SysUser user = new SysUser();
            user.setId(3L);
            user.setUsername("王五");
            list.add(user);
        }
        sysUsersDao.saveAll(list);
    }

    @Test
    public void findAll() {
        List<SysUser> all = sysUsersDao.findAll();
        System.out.println("all = " + all);
        System.out.println("JSON all = " + JSON.toJSONString(all));
    }

    @Test
    public void updateUser() {
//        SysUser user = usersDao.getOne(1);
        SysUser user = sysUsersDao.getReferenceById(1);
        System.out.println("user = " + user);
        user.setAge(19);
        sysUsersDao.save(user);
    }

    @Test
    public void updateUser2() {
        // 2.x版本后需要.get()才能得到实体对象， id未查询到对应实体时会报错
        Optional<SysUser> userOptional = sysUsersDao.findById(1);
        userOptional.ifPresent(user -> {
            System.out.println("user = " + user);
            user.setAge(20);
            sysUsersDao.save(user);
        });
    }


    @Test
    public void deleteUserById() {
        int id = 1;
        sysUsersDao.deleteById(id);
    }

    /**
     * {
     * "content": [{}], // 数据列表
     * "last": true, // 是否最后一页
     * "totalPages": 1, // 总页数
     * "totalElements": 1, // 数据总数
     * "sort": null, // 排序
     * "first": true, // 是否首页
     * "numberOfElements": 1, // 本页数据条数
     * "size": 10, // 每页长度
     * "number": 0 // 当前页序号
     * }
     */
    // 分页查询
    @Test
    public void selectByPage() {
        PageRequest pageable = PageRequest.of(0, 2);
        Page<SysUser> page = sysUsersDao.findAll(pageable);
        List<SysUser> SysUserList = page.getContent();
        System.out.println("page = " + JSON.toJSONString(page));
        // 数据的总条数：page.getTotalElements();
        // 总页数：page.getTotalPages();
    }

    // 分页查询并排序
    @Test
    public void selectByPageByOrder() {

        List<SysUser> list = sysUsersDao.findAll();
        System.out.println("list = " + list);

        List<SysUser> list2 = sysUsersDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
        System.out.println("list2 = " + list2);


        // 按照id降序排
        PageRequest pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "id");
        Page<SysUser> page = sysUsersDao.findAll(pageable);
        List<SysUser> list3 = page.getContent();
        // 数据的总条数：page.getTotalElements();
        // 总页数：page.getTotalPages();
        System.out.println("list3 = " + JSON.toJSONString(list3));
    }

    @Test
    public void getMaxIdUser() {
        SysUser maxIdUser = sysUsersDao.getMaxIdUser();
        System.out.println("maxIdUser = " + maxIdUser);
    }

    @Test
    public void findByNameMatch() {
        List<SysUser> list = sysUsersDao.findByNameMatch("jim");
        System.out.println("list = " + list);

    }


    @Test
    void ManyToMany_saveEduCourse() {


        EduCourse EduCourse1 = new EduCourse();
        EduCourse1.setId(1L);
        EduCourse1.setName("语文");

        EduCourse EduCourse2 = new EduCourse();
        EduCourse2.setId(2L);
        EduCourse2.setName("数学");

        EduCourse EduCourse3 = new EduCourse();
        EduCourse3.setId(3L);
        EduCourse3.setName("英语");

        EduCourse EduCourse4 = new EduCourse();
        EduCourse4.setId(4L);
        EduCourse4.setName("化学");

        subjectDao.saveAll(Lists.newArrayList(EduCourse1, EduCourse2, EduCourse3, EduCourse4));
    }

    @Test
    void ManyToMany_saveStudent() {

        EduStudent student1 = new EduStudent();
        student1.setId(1L);
        student1.setName("学生1");

        EduStudent student2 = new EduStudent();
        student2.setId(2L);
        student2.setName("学生2");
        studentDao.saveAll(Lists.newArrayList(student1, student2));
    }

    @Test
    void ManyToMany_saveStudentEduCourse() {

        EduStudent student1 = new EduStudent();
        student1.setId(1L);
        student1.setName("学生1");

        EduStudent student2 = new EduStudent();
        student2.setId(2L);
        student2.setName("学生2");

        EduCourse EduCourse1 = new EduCourse();
        EduCourse1.setId(1L);
        EduCourse1.setName("语文");

        EduCourse EduCourse2 = new EduCourse();
        EduCourse2.setId(2L);
        EduCourse2.setName("数学");

        EduCourse EduCourse3 = new EduCourse();
        EduCourse3.setId(3L);
        EduCourse3.setName("英语");

        EduCourse EduCourse4 = new EduCourse();
        EduCourse4.setId(4L);
        EduCourse4.setName("化学");


        EduCourse EduCourse5 = new EduCourse();
        EduCourse5.setId(5L);
        EduCourse5.setName("物理");
        subjectDao.saveAndFlush(EduCourse5);


        EduCourse EduCourse6 = new EduCourse();
        EduCourse6.setId(6L);
        EduCourse6.setName("生物");
        subjectDao.saveAndFlush(EduCourse6);

        // id 为 1 的学生选修了 EduCourse1，EduCourse2 两门课
        // id 为 2 的学生选修了 EduCourse3，EduCourse4 两门课
        student1.setCourses(Lists.newArrayList(EduCourse1, EduCourse2, EduCourse5, EduCourse6));
        student2.setCourses(Lists.newArrayList(EduCourse3, EduCourse4));

        studentDao.saveAll(Lists.newArrayList(student1, student2));

        // studentDao.saveAndFlush(student1);
        // studentDao.saveAndFlush(student2);

    }

    @Test
    void manyToMany_updateEduCourse() {
        Optional<EduStudent> byId = studentDao.findById(2);
        EduStudent student = byId.get();
        // 删除一门课
        student.getCourses().removeFirst();

        // 新增一门课
        EduCourse EduCourse = new EduCourse();
        EduCourse.setId(5L);
        EduCourse.setName("物理");
        subjectDao.save(EduCourse);

        // 添加
        student.getCourses().add(EduCourse);
        studentDao.save(student);
    }


    @Test
    void ManyToMany_findStudent() {
        List<EduStudent> list = studentDao.findAll();
        System.out.println("list = " + list);
    }

}
