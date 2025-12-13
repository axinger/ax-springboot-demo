//package com.github.axinger;
//
//import com.alibaba.fastjson2.JSON;
//import com.github.axinger.dao.*;
//import com.github.axinger.model.*;
//import com.google.common.collect.Lists;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
/// **
// * （1）CrudRepository和PagingAndSortingRepository由Spring Data提供；
// * （2）JpaRepository 由Spring Data JPA提供，而Spring Data JPA又是Spring Data的一个子项目，这就是两者的关系
// * （3）存在继承关系：
// * 　　PagingAndSortingRepository 继承 CrudRepository
// * 　　JpaRepository 继承 PagingAndSortingRepository
// * 也就是说， CrudRepository 提供基本的增删改查；PagingAndSortingRepository 提供分页和排序方法；JpaRepository 提供JPA需要的方法
// */
//@SpringBootTest
//class JpaDemoApplicationTest {
//
//    @Autowired
//    private PersonJpaRepository personJpaRepository;
//    /**
//     * 2.一对多 Users 有多个 Address
//     */
//
//    @Autowired
//    private AddressJpaRepository addressJpaRepository;
//    @Autowired
//    private SchoolJpaRepository schoolJpaRepository;
//    @Autowired
//    private StudentDao studentDao;
//    @Autowired
//    private SubjectDao subjectDao;
//
//    @Test
//    void test_增one() {
//        Person user = new Person();
//        user.setId(1);
//        user.setUserName("jim");
//        personJpaRepository.save(user);
//    }
//
//    @Test
//    void test_增list() {
//
//        List<Person> list = new ArrayList<>();
//        {
//            Person user = new Person();
//            user.setId(1);
//            user.setUserName("jim");
//            list.add(user);
//        }
//        {
//            Person user = new Person();
//            user.setId(2);
//            user.setUserName("tom");
//            list.add(user);
//        }
//
//
//        personJpaRepository.saveAll(list);
//    }
//
//    @Test
//    public void findAll() {
//        List<Person> all = personJpaRepository.findAll();
//        System.out.println("all = " + all);
//    }
//
//    @Test
//    public void updateUser() {
//        Person user = personJpaRepository.getOne(1);
//        System.out.println("user = " + user);
//        user.setAge(19);
//        personJpaRepository.save(user);
//    }
//
//    @Test
//    public void updateUser2() {
//        personJpaRepository.findById(1).ifPresent(user -> {
//            System.out.println("user = " + user);
//            user.setAge(20);
//            personJpaRepository.save(user);
//        });
//    }
//
//    @Test
//    public void selectById() {
//        personJpaRepository.findById(1).ifPresent(users -> {
//            System.out.println("users = " + users);
//        });
//        // 2.x版本后需要.get()才能得到实体对象， id未查询到对应实体时会报错
//    }
//
//    @Test
//    public void deleteUserById() {
//        int id = 1;
//        personJpaRepository.deleteById(id);
//    }
//
//    /**
//     * {
//     * "content": [{}], // 数据列表
//     * "last": true, // 是否最后一页
//     * "totalPages": 1, // 总页数
//     * "totalElements": 1, // 数据总数
//     * "sort": null, // 排序
//     * "first": true, // 是否首页
//     * "numberOfElements": 1, // 本页数据条数
//     * "size": 10, // 每页长度
//     * "number": 0 // 当前页序号
//     * }
//     */
//    // 分页查询
//    @Test
//    public void selectByPage() {
//        PageRequest pageable = PageRequest.of(0, 2);
//        Page<Person> page = personJpaRepository.findAll(pageable);
//        List<Person> personList = page.getContent();
//        System.out.println("page = " + JSON.toJSONString(page));
//        // 数据的总条数：page.getTotalElements();
//        // 总页数：page.getTotalPages();
//    }
//
//    // 分页查询并排序
//    @Test
//    public void selectByPageByOrder() {
//
//        // 按照id降序排
//        PageRequest pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "id");
//        Page<Person> page = personJpaRepository.findAll(pageable);
//        List<Person> personList = page.getContent();
//        // 数据的总条数：page.getTotalElements();
//        // 总页数：page.getTotalPages();
//        System.out.println("page = " + JSON.toJSONString(page));
//    }
//
//    @Test
//    public void getMaxIdUser() {
//
//        Person maxIdUser = personJpaRepository.getMaxIdUser();
//        System.out.println("maxIdUser = " + maxIdUser);
//
//    }
//
//    @Test
//    public void findByNameMatch() {
//
//        List<Person> list = personJpaRepository.findByNameMatch("jim");
//        System.out.println("list = " + list);
//
//    }
//
//    @Test
//    void addUserOneToOne() {
//        Person user = new Person();
//        user.setId(1);
//        user.setUserName("jim");
//
//        Cards card = new Cards();
//        card.setId(1);
//        card.setCardType("A");
//        user.setCard(card);
//        personJpaRepository.save(user);
//    }
//
//    @Test
//    void OneToMany_saveUser() {
//
//        Person user = new Person();
//        user.setId(1);
//        user.setUserName("jim");
//
//        Address address1 = new Address();
//        address1.setId(1);
//        address1.setDetail("A");
//        address1.setUser(user);
//
//        Address address2 = new Address();
//        address2.setId(2);
//        address2.setDetail("B");
//        address2.setUser(user);
//
//        user.setAddressList(Lists.newArrayList(address1, address2));
//
//
//        Tasks tasks1 = new Tasks();
//        // tasks1.setId(1);
//        tasks1.setContent("任务1");
//
//        Tasks tasks2 = new Tasks();
//        // tasks2.setId(2);
//        tasks2.setContent("任务2");
//
//        user.setTasksList(Lists.newArrayList(tasks1, tasks2));
//
//        personJpaRepository.save(user);
//    }
//
//    @Test
//    public void findAllOneToMany() {
//        List<Person> list = personJpaRepository.findAll();
//
//        System.out.println("list = " + list);
//
//    }
//
//    @Test
//    public void findAllOneToMany2() {
//        List<Address> all = addressJpaRepository.findAll();
//        System.out.println("all = " + all);
//    }
//
//    @Test
//    void OneToMany_saveSchool() {
//
//        School school = new School();
//        school.setId(1);
//        school.setSchoolName("一小");
//
//        Room room1 = new Room();
//        room1.setId(1);
//        room1.setRoomName("80后希望小学-1班");
//        // 切记：many端也需set one端所关联的实体对象，否则many端关联one端的字段值为null
//        room1.setSchool(school);
//
//        Room room2 = new Room();
//        room2.setId(2);
//        room2.setRoomName("80后希望小学-2班");
//        room2.setSchool(school);
//
//        List<Room> roomList = new ArrayList<>();
//        roomList.add(room1);
//        roomList.add(room2);
//
//        school.setRoomList(roomList);
//
//        School save = schoolJpaRepository.save(school);
//        System.out.println("学校的信息是：" + save);
//    }
//
//    @Test
//    void OneToMany_findAllSchool() {
//
//        List<School> list = schoolJpaRepository.findAll();
//        System.out.println("list = " + list);
//    }
//
//    @Test
//    void ManyToMany_saveSubject() {
//
//
//        Subject subject1 = new Subject();
//        subject1.setId(1);
//        subject1.setName("语文");
//
//        Subject subject2 = new Subject();
//        subject2.setId(2);
//        subject2.setName("数学");
//
//        Subject subject3 = new Subject();
//        subject3.setId(3);
//        subject3.setName("英语");
//
//        Subject subject4 = new Subject();
//        subject4.setId(4);
//        subject4.setName("化学");
//
//        subjectDao.saveAll(Lists.newArrayList(subject1, subject2, subject3, subject4));
//    }
//
//    @Test
//    void ManyToMany_saveStudent() {
//
//        Student student1 = new Student();
//        student1.setId(1);
//        student1.setName("学生1");
//
//        Student student2 = new Student();
//        student2.setId(2);
//        student2.setName("学生2");
//        studentDao.saveAll(Lists.newArrayList(student1, student2));
//    }
//
//    @Test
//    void ManyToMany_saveStudentSubject() {
//
//        Student student1 = new Student();
//        student1.setId(1);
//        student1.setName("学生1");
//
//        Student student2 = new Student();
//        student2.setId(2);
//        student2.setName("学生2");
//
//        Subject subject1 = new Subject();
//        subject1.setId(1);
//        subject1.setName("语文");
//
//        Subject subject2 = new Subject();
//        subject2.setId(2);
//        subject2.setName("数学");
//
//        Subject subject3 = new Subject();
//        subject3.setId(3);
//        subject3.setName("英语");
//
//        Subject subject4 = new Subject();
//        subject4.setId(4);
//        subject4.setName("化学");
//
//
//        Subject subject5 = new Subject();
//        subject5.setId(5);
//        subject5.setName("物理");
//        subjectDao.saveAndFlush(subject5);
//
//
//        Subject subject6 = new Subject();
//        subject6.setId(6);
//        subject6.setName("生物");
//        subjectDao.saveAndFlush(subject6);
//
//        // id 为 1 的学生选修了 subject1，subject2 两门课
//        // id 为 2 的学生选修了 subject3，subject4 两门课
//        student1.setSubjectList(Lists.newArrayList(subject1, subject2, subject5, subject6));
//        student2.setSubjectList(Lists.newArrayList(subject3, subject4));
//
//        studentDao.saveAll(Lists.newArrayList(student1, student2));
//
//        // studentDao.saveAndFlush(student1);
//        // studentDao.saveAndFlush(student2);
//
//    }
//
//    @Test
//    void manyToMany_updateSubject() {
//        Optional<Student> byId = studentDao.findById(2);
//        Student student = byId.get();
//        // 删除一门课
//        student.getSubjectList().remove(0);
//
//
//        // 新增一门课
//        Subject subject = new Subject();
//        subject.setId(5);
//        subject.setName("物理");
//        subjectDao.save(subject);
//
//
//        // 添加
//        student.getSubjectList().add(subject);
//        studentDao.save(student);
//
//    }
//
//
//    @Test
//    void ManyToMany_findStudent() {
//
//        List<Student> list = studentDao.findAll();
//        System.out.println("list = " + list);
//
//    }
//
//}
