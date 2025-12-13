package com.xing;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import com.xing.entity.Dog;
import com.xing.entity.Person;
import com.xing.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MongoTemplateSQLTests.java
 * @description TODO
 * @createTime 2022年07月12日 16:32:00
 */

@SpringBootTest
@Slf4j
public class MongoTemplateTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void save_user() {

        User user = new User();

        // user.setId("123");
        user.setName("jim");
        user.setAge(10);
        User save = mongoTemplate.save(user);
        System.out.println("save = " + save);


    }

    @Test
    void save_dog() {


        for (int i = 0; i < 5; i++) {
            Dog dog = new Dog();
            if (i % 2 == 0) {
                dog.setName("name" + i);
                dog.setAddress("address" + i);
                dog.setBirthday(LocalDateTime.now());
            }
            dog.setAge(i);
            final Dog save = mongoTemplate.save(dog);
            System.out.println("save = " + save);
        }

    }


    @Test
    void save_dog_1() {
        /**
         * insert: 若新增数据的主键已经存在，则会抛 org.springframework.dao.DuplicateKeyException 异常提示主键重复，不保存当前数据。
         * save: 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作。
         */
        Dog dog = new Dog();
        dog.setId("1");
        dog.setName("name-001");
        mongoTemplate.save(dog);


//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(dog.getId()));
//        query.addCriteria(
//                new Criteria().orOperator(
//                        Criteria.where("delete").isNull(),
//                        Criteria.where("delete").is(false)
//                ));
//
//        Dog dog1 = mongoTemplate.findOne(query, Dog.class);
//        System.out.println("dog1 = " + dog1);

    }


    @Test
    void update_dog_2() {

        // 这里不设置name,为null
        Dog dog = new Dog();
        dog.setId("1");
        dog.setAge(RandomUtil.randomInt(1, 100));
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(dog.getId()));


        Update update = new Update();
        Optional.ofNullable(dog).map(Dog::getName).ifPresent(val -> {
            update.set(LambdaUtil.getFieldName(Dog::getName), dog.getName());
        });
        /// age 为null时,会被更新
        update.set("age", dog.getAge());

        /**
         * 数据库有，就新增， 没有，就修改
         */
        UpdateResult upsert = mongoTemplate.upsert(query, update, Dog.class);


    }


    @Test
    void find_dog() {
        final List<Dog> dogList = mongoTemplate.findAll(Dog.class);
        System.out.println("dogList = " + dogList);

    }

    @Test
    void query_dog() {

        /// Example 有局限
//        BeanUtil.copyProperties(queryVo, hospital);
//        Example<Hospital> example = Example.of(hospital, matcher);
//        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);


        Pageable pageable = PageRequest.of(0, 2);


        Query query = new Query();

        final Criteria criteria = Criteria.where("age").gte(9);
        criteria.and("name").is("golf");

        query.addCriteria(criteria);


//        query.addCriteria(new Criteria ().orOperator(
//                Criteria.where("name").not().isNull().and("age").not().isNull(),
//                ));


//        query.addCriteria(new Criteria().orOperator(Criteria.where("name").regex("tom"),
//                Criteria.where("idno").regex(mdo.getIdno())));


        query.with(Sort.by(Sort.Direction.DESC, "age"));
//        query.skip(0);
//        query.limit(10)

        // 分页
        query.with(pageable);

        long totoal = mongoTemplate.count(query, Dog.class);
        System.out.println("totoal = " + totoal);


        log.info("分页参数 = {},isPaged = {}, isUnpaged = {}", pageable.getPageNumber(),
                pageable.isPaged(),
                pageable.isUnpaged()
        );


//        query.fields().include("name").include("status")
        final List<Dog> dogList = mongoTemplate.find(query, Dog.class);
        System.out.println("dogList = " + dogList);
    }

    /// 不能同时为空
    @Test
    void query_dog_2() {

        Pageable pageable = PageRequest.of(0, 10);

        Query query = new Query();


        /// 添加判断: 不能同时为空
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("name").exists(true),
                Criteria.where("address").exists(true)
        ));

        query.with(Sort.by(Sort.Direction.ASC, "age"));

        // 分页
        query.with(pageable);

        long total = mongoTemplate.count(query, Dog.class);
        System.out.println("total = " + total);
        final List<Dog> dogList = mongoTemplate.find(query, Dog.class);
        dogList.forEach(varl -> System.out.println("getName = " + varl.getName() + " getAddress = " + varl.getAddress()));
    }

    @Test
    void query_dog_limit() {

        Query query = new Query();

        query.addCriteria(Criteria.where("age").is(4));

        /// 条件判断,不同时为空
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("name").exists(true),
                Criteria.where("address").exists(true)
        ));

//        query.with(Sort.by(Sort.Direction.DESC, "_id"));
//        query.limit(2);


        long total = mongoTemplate.count(query, Dog.class);
        System.out.println("total = " + total);
        final List<Dog> dogList = mongoTemplate.find(query, Dog.class);
        dogList.forEach(varl -> System.out.println("age = " + varl.getAge() + "getName = " + varl.getName() + " getAddress = " + varl.getAddress()));
    }

    @Test
    void test1() {
        List list = Arrays.asList(1, 4, 2);

//        list.stream().sorted(Comparator.reverseOrder()).forEach(val-> System.out.println("val = " + val));

//        list.stream().sorted(Comparator.reverse).forEach(val-> System.out.println("val = " + val));
        list.stream().forEachOrdered(val -> System.out.println("val = " + val));
//        Collections.reverse(list);list.forEach(val-> System.out.println("val = " + val));
    }

    @Test
    void test2() {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("id", "1");
        dbObject.put("name", "jim");

        // 指定返回的字段
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("id", true);
        fieldsObject.put("name", true);
        fieldsObject.put("age", true);
        Query query = new BasicQuery(dbObject.toJson(), fieldsObject.toJson());

        List<Person> list = mongoTemplate.find(query, Person.class, "collectionName");

    }


    @Test
    public void getLeaveMessage() {

        String postId = "1";   // 帖子id
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("DirectTrainPostLeaveMessageComment")   // 从表名
                .localField("_id")   // 主表关联字段
                .foreignField("leaveId")   // 从表关联字段
                .as("CommentList");   // 查询结果名
        // 匹配id条件
        MatchOperation matchOperation = new MatchOperation(Criteria.where("postId").is(postId));
        // 按回帖时间排序
        SortOperation sortOperation = new SortOperation(Sort.by(Sort.Order.desc("leaveMessageTime")));
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation);
        List<Map> result = mongoTemplate.aggregate(aggregation, "DirectTrainPostLeaveMessage", Map.class).getMappedResults();
    }

    @Test
    void test_sql_save() {

        for (int i = 1; i < 4; i++) {

            Map map = new HashMap();
            map.put("age", 10 + i);
            map.put("name", "jim" + i);
            map.put("birthday", LocalDateTimeUtil.parse(String.format("2020-01-%02d 12:00:00", i), "yyyy-MM-dd HH:mm:ss"));
            final Map student = mongoTemplate.save(map, "student");
            System.out.println("student = " + student);
        }

    }


    // SQL语句
    @Test
    void test_sql_find() {

        final FindIterable<Document> documents = mongoTemplate.getCollection("student")
                .find();
        List<Map> list = new ArrayList<>();
        documents.forEach(val -> {
            final Map<String, Object> map = val.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            map.put("id", String.valueOf(map.get("_id")));
            map.remove("_id");
            list.add(map);
        });

        System.out.println("list = " + list);
        System.out.println("JSON.toJSONString(list) = " + JSON.toJSONString(list));
    }

    @Test
    void test_sql_find_2() {

        Map sqlMap = new HashMap();
        sqlMap.put("age", 11);
        BasicDBObject basicDBObject = new BasicDBObject(sqlMap);
        final FindIterable<Map> findIterable = mongoTemplate.getCollection("student")
                .find(basicDBObject, Map.class);

        List<Map> list = new ArrayList<>();
        findIterable.forEach(val -> {
            val.put("id", String.valueOf(val.get("_id")));
            val.remove("_id");
            list.add(val);
        });

        System.out.println("list = " + list);
        System.out.println("JSON.toJSONString(list) = " + JSON.toJSONString(list));

    }

    @Test
    void test_sql_find_id() {


        Map sqlMap = new HashMap();
        sqlMap.put("_id", new ObjectId("62cd541108069b3d1bd92458"));
        sqlMap.put("age", 10);

        BasicDBObject basicDBObject = new BasicDBObject(sqlMap);
        final FindIterable<Document> documents = mongoTemplate.getCollection("student")
                .find(basicDBObject);
        List<Map> list = new ArrayList<>();
        documents.forEach(val -> {
            final Map<String, Object> map = val.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            map.put("id", String.valueOf(map.get("_id")));
            map.remove("_id");
            list.add(map);
        });

        System.out.println("list = " + list);
        System.out.println("JSON.toJSONString(list) = " + JSON.toJSONString(list));
    }

    @Test
    void test_sql_find_id_limit() {

        Map sqlMap = new HashMap();
        sqlMap.put("age", 10);

        BasicDBObject basicDBObject = new BasicDBObject(sqlMap);
        final FindIterable<Document> documents = mongoTemplate.getCollection("student")
                .find(basicDBObject)
                .limit(2);
        List<Map> list = new ArrayList<>();
        documents.forEach(val -> {
            final Map<String, Object> map = val.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            map.put("id", String.valueOf(map.get("_id")));
            map.remove("_id");
            list.add(map);
        });

        System.out.println("list = " + list);
        System.out.println("JSON.toJSONString(list) = " + JSON.toJSONString(list));
    }


}
