package com.xing;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.StrUtil;
import com.mongodb.client.result.UpdateResult;
import com.xing.entity.Dog;
import com.xing.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
public class MongoTemplateCRUDTests {


    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 插入多个,只有insertAll
     */
    @Test
    void save_user() {

        List<User> list = new ArrayList<>() {{
            {
                User user = new User();
                user.setId("1");
                user.setName("jim");
                user.setAge(10);

                Dog dog = new Dog();
                user.setDog(dog);
                dog.setName("大斑点");
                dog.setAge(5);
                dog.setDescription("address不传值");


                add(user);
            }
            {
                User user = new User();
                user.setId("2");
                user.setName("tom");
                user.setAge(10);

                Dog dog = new Dog();
                user.setDog(dog);
                dog.setName("大斑点");
                dog.setAge(5);
                dog.setAddress("a号道");
                dog.setDescription("address有值");


                add(user);
            }
            {
                User user = new User();
                user.setId("3");
                user.setName("jack");
                user.setAge(10);

                Dog dog = new Dog();
                user.setDog(dog);
                dog.setName("牧羊犬");
                dog.setAge(6);
                dog.setAddress("a号道");
                dog.setDescription("address有值");

                add(user);
            }
            {
                User user = new User();
                user.setId("4");
                user.setName("lili");
                user.setAge(10);

                Dog dog = new Dog();
                user.setDog(dog);
                dog.setName("小斑点");
                dog.setAge(8);
                dog.setAddress("b号道");
                dog.setDescription("address有值");

                add(user);
            }
            {
                User user = new User();
                user.setId("5");
                user.setName("luck");
                user.setAge(10);
                // 没有狗
                add(user);
            }
        }};
        mongoTemplate.insertAll(list);
        // mongoTemplate.save(list,"user"); //失败
    }


    /**
     * 只更新查询到的第一个,一般用在模糊搜索中
     */
    @Test
    void updateFirst() {

        // 更新审核实例
        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10)
                // ne 不等于
                .and(LambdaUtil.getFieldName(User::getName)).ne("jim")
        );

        // 会更新第一个 age =10, 且age!=jim的
        Update update = new Update();
        update.set(LambdaUtil.getFieldName(User::getAge), 20);

        UpdateResult result = this.mongoTemplate.updateFirst(query, update, User.class);
        System.out.println("result = " + result);
    }


    // mongoTemplate.upsert有三种用法，主要功能是更新数据，如果数据不存在就新增
    @Test
    void upsert() {

        // 更新审核实例
        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getId)).is(12)
                .and(LambdaUtil.getFieldName(User::getAge)).is(20)
                // ne 不等于
                .and(LambdaUtil.getFieldName(User::getName)).ne("jim")
        );

        // 会更新第一个 age =10, 且age!=jim的
        Update update = new Update();
        update.set(LambdaUtil.getFieldName(User::getAge), 21);

        UpdateResult result = this.mongoTemplate.upsert(query, update, User.class);
        System.out.println("result = " + result);
    }

    /**
     * 迷糊查询
     */
    @Test
    void find() {

        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10)
        );

        User one = this.mongoTemplate.findOne(query, User.class);
        System.out.println("one = " + one);


        List<User> allList = this.mongoTemplate.findAll(User.class);
        System.out.println("allList = " + allList);


        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list);

        //  skip 跳过1个, limit 查询几个, 不要用这个做分页
        query.skip(1);
        query.limit(2);
        List<User> limitList = this.mongoTemplate.find(query, User.class);
        System.out.println("limitList = " + limitList);

    }


    @Test
    void find_id_list_one() {

        User byId = mongoTemplate.findById(1, User.class);
        System.out.println("byId = " + byId);

        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getId)).is(1)
        );

        List<User> list = this.mongoTemplate.find(query, User.class);

        System.out.println("list = " + list);

        Query query2 = new Query(Criteria
                .where("_id").is(1)
        );
        User user = mongoTemplate.findOne(query2, User.class);
        System.out.println("user = " + user);

    }

    @Test
    void find_Pattern() {
        //
        // Pattern pattern = Pattern.compile("^ + content + .*");
        // 模糊查询，满足XXX条件：^.*(content).*$
        // 模糊查询，满足XXX条件或者YYY条件：^.*(content1|content2).*$
        // 模糊查询，不满足XXX条件：^((?!content).)*$
        // 模糊查询，查询以XXX开头：^content.*
        // 模糊查询，不满足XXX开头：^(?!content).*$
        //
        // 查询开头满足：String begin="4305" 中间不等于String center = "00"  结尾满足String end="YYYYY"
        //
        // Pattern pattern1 = Pattern.compile("^"+begin+"((?!"+center+").).*"+end+"$");
        // Pattern pattern1 = Pattern.compile("^4305((?!00).).*YYYYY$");

        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10)
        );


        Pattern name1 = Pattern.compile(StrUtil.format("^.*{}.*$", "jim"), Pattern.CASE_INSENSITIVE);
        Pattern name2 = Pattern.compile(StrUtil.format("^.*{}.*$", "tom"), Pattern.CASE_INSENSITIVE);

        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("name").regex(name1),
                Criteria.where("name").regex(name2));


        query.addCriteria(criteria);


        PageRequest request = PageRequest.of(0, 5);
        query.with(request);

        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list);
        System.out.println("request = " + request);

    }


    @Test
    void find_regex() {

        // //完全匹配
        // Pattern pattern = Pattern.compile("^hzb$", Pattern.CASE_INSENSITIVE);
        // //右匹配
        // Pattern pattern = Pattern.compile("^.*hzb$", Pattern.CASE_INSENSITIVE);
        // //左匹配
        // Pattern pattern = Pattern.compile("^hzb.*$", Pattern.CASE_INSENSITIVE);
        // //模糊匹配
        // Pattern pattern = Pattern.compile("^.*hzb.*$", Pattern.CASE_INSENSITIVE);

        Query query = new Query(Criteria
                .where("name").regex("^.*om$")
        );

        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list);


    }

    @Test
    void find_or_2() {


        Query query = new Query();

        Criteria criteria = Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10);

        query.addCriteria(criteria);


        // 多个条件, 多个or 需要包裹一下
        List<Criteria> criteriaList = new ArrayList<>();

        // .*$ 结尾,类似%结尾
        {

            Pattern patternAddress = Pattern.compile(StrUtil.format("^.*{}.*$", "b号道"), Pattern.CASE_INSENSITIVE);
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("dog.address").regex(patternAddress),
                    // 是空
                    Criteria.where("dog.address").isNull()
            ));
        }


        {

            Pattern patternSize = Pattern.compile(StrUtil.format("^.*{}.*$", "大"), Pattern.CASE_INSENSITIVE);
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("dog.name").regex(patternSize),
                    Criteria.where("dog.name").isNull()
            ));

        }

        criteria.andOperator(criteriaList);


        // {
        //     //  Criteria 没有where会报错,
        //     Criteria criteria = new Criteria();
        //     criteria.and("dog.age").gt(5);
        //     query.addCriteria(criteria);
        //
        // }

        {

            // Criteria gt = Criteria
            //         .where("dog.age").gt(4);
            // query.addCriteria(gt);

            criteria.and("dog.age").gt(4);
        }

        PageRequest request = PageRequest.of(0, 5);
        query.with(request);

        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list.stream().map(User::getId).toList());

    }

    @Test
    void find_or_3() {


        Query query = new Query();

        Criteria criteria = Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10);

        query.addCriteria(criteria);

        ;
        query.addCriteria(Criteria.where("dog.age").gte(6).lte(8));
        // query.addCriteria(Criteria.where("dog.age").gte(6).lt(8));


        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list.stream().map(User::getId).toList());

    }

    @Test
    void find_or_4() {


        Query query = new Query();

        Criteria criteria = Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10);

        query.addCriteria(criteria);

        // 分开写,不可以
        // query.addCriteria(
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.age").is(6),
        //                 Criteria.where("dog.age").is(8)
        //         )
        // );
        //
        // query.addCriteria(
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.name").is("牧羊犬"),
        //                 Criteria.where("dog.name").is("大斑点")
        //         )
        // );


        // 这个可以
        // criteria.andOperator(
        //
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.name").is("牧羊犬"),
        //                 Criteria.where("dog.name").is("大斑点")
        //         ),
        //
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.age").is(6),
        //                 Criteria.where("dog.age").is(8)
        //         )
        // );


        // 可以
        // query.addCriteria(new Criteria().andOperator(
        //
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.name").is("牧羊犬"),
        //                 Criteria.where("dog.name").is("大斑点")
        //         ),
        //
        //         new Criteria().orOperator(
        //                 Criteria.where("dog.age").is(6),
        //                 Criteria.where("dog.age").is(8)
        //         )
        //
        // ));


        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list.stream().map(User::getId).toList());

    }

    @Test
    void find_page() {
        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10)
        );

        long count = mongoTemplate.count(query, User.class);
        System.out.println("count = " + count);

        PageRequest request = PageRequest.of(0, 2);
        query.with(request);

        List<User> list = this.mongoTemplate.find(query, User.class);
        System.out.println("list = " + list);
        System.out.println("request = " + request);

        // System.out.println("request.isPaged() = " + request.isPaged());
        // System.out.println("request.isUnpaged() = " + request.isUnpaged());
        // System.out.println("request.getPageNumber() = " + request.getPageNumber());
        // System.out.println("request.getPageSize() = " + request.getPageSize());
        // System.out.println("request.getOffset() = " + request.getOffset());


        // 分页,需要总数量, 需要自定义查询总数量
        // PageableExecutionUtils.getPage()
        Page<User> page = new PageImpl<>(list, request, count);
        System.out.println("page = " + page);

        // mongoTemplate.

        // 有很多判断
        Page<User> page1 = PageableExecutionUtils.getPage(list, request, () -> count);
        System.out.println("page1 = " + page1);

        System.out.println("page1.getTotalPages() = " + page1.getTotalPages());
        System.out.println("page1.isFirst() = " + page1.isFirst());
        System.out.println("page1.isLast() = " + page1.isLast());
        System.out.println("page1.hasNext() = " + page1.hasNext());
        System.out.println("page1.getNumber() = " + page1.getNumber());
        System.out.println("page1.getTotalElements() = " + page1.getTotalElements());
    }


    /**
     * 只更新查询到的第一个,用主键
     */
    @Test
    void updateFirst_for_id() {

        // 更新审核实例
        Query query = new Query(Criteria
                .where("_id").is(2)
        );

        // 会更新第一个 age =10, 且age!=jim的
        Update update = new Update();
        update.set(LambdaUtil.getFieldName(User::getAge), 20);

        UpdateResult result = this.mongoTemplate.updateFirst(query, update, User.class);
        System.out.println("result = " + result);
    }

    /**
     * 更新多个
     */
    @Test
    void updateMulti() {

        // 更新审核实例
        Query query = new Query(Criteria
                .where(LambdaUtil.getFieldName(User::getAge)).is(10)
                // ne 不等于
                .and(LambdaUtil.getFieldName(User::getName)).ne("jim")
        );

        Update update = new Update();
        update.set(LambdaUtil.getFieldName(User::getAge), 20);

        UpdateResult result = this.mongoTemplate.updateMulti(query, update, User.class);
        System.out.println("result = " + result);

    }


}
