package com.github.axinger;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.config.MyTableNameHandler;
import com.github.axinger.config.MyTenantLineHandler;
import com.github.axinger.domain.PersonEntity;
import com.github.axinger.mapper.PersonMapper;
import com.github.axinger.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonMapper personMapper;


    @Test
    void test_新增list() {
        String jsonList = """
                [
                    {
                        "name": "jim",
                        "age": 10,
                        "id":1
                    },
                    {
                        "name": "tom",
                        "age": 10,
                        "id":2
                    },
                    {
                        "name": "lili",
                        "age": 11,
                        "id":3
                    }
                ]
                """;

        List<PersonEntity> entityList = JSON.parseArray(jsonList, PersonEntity.class);
        personService.saveBatch(entityList);
    }

    @Test
    void test_新增一个() {
        String jsonList = """
                    {
                        "name": "jim",
                        "age": 10,
                        "id":1
                    }
                """;


        PersonEntity person = JSONObject.parseObject(jsonList, PersonEntity.class);
        personService.save(person);
    }

    @Test
    void test_saveOrUpdateBatch() {
        PersonEntity book = personService.getById(1);
        book.setName(book.getName() + "_v1");
        List<PersonEntity> list = List.of(book);
        personService.saveOrUpdateBatch(list);
    }

    @Test
    void test_saveOrUpdateBatch2() {
        PersonEntity book = personService.getById(1);
        book.setName(book.getName() + "_v1");

        PersonEntity book1 = new PersonEntity();
        book1.setName("一千零一夜");

        List<PersonEntity> list = List.of(book, book1);
        personService.saveOrUpdateBatch(list);
    }

    @Test
    void test_stream() throws InterruptedException {

        personMapper.streamSelect(val -> {
            PersonEntity entity = val.getResultObject();
            System.out.println("entity = " + entity);
        });

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void test_findAll() {

        List<PersonEntity> list = personMapper.findAll();
        System.out.println("list = " + list);
    }

    @Test
    void test() {
        List<PersonEntity> list = personService.list();
        System.out.println("list = " + JSON.toJSON(list));
    }


    @Test
    void test_lambdaQuery() {
        // lambdaQuery ( 对象 ) 就相当于 ==
        PersonEntity book = new PersonEntity();
        book.setName("jim");
        List<PersonEntity> list1 = personService.lambdaQuery(book)
                .eq(PersonEntity::getId, 1)
                .list();
        System.out.println("list1 = " + list1);
    }

    @Test
    void test_lambdaQuery2() {

        LambdaQueryChainWrapper<PersonEntity> wrapper = personService.lambdaQuery();

        wrapper.or(w -> {
            w.eq(PersonEntity::getName, "jim")
                    .eq(PersonEntity::getAge, 10);
        });

        wrapper.or(w -> {
            w.eq(PersonEntity::getName, "tom")
                    .eq(PersonEntity::getAge, 20);
        });

        List<PersonEntity> list = wrapper.list();
        System.out.println("list = " + list);
    }

    @Test
    void test_LambdaUtils() {
        LambdaMeta extract = LambdaUtils.extract(PersonEntity::getName);
        System.out.println("extract.getImplMethodName() = " + extract.getImplMethodName()); // getBookName
        System.out.println("extract.getInstantiatedClass() = " + extract.getInstantiatedClass()); // class com.github.axinger.domain.BookEntity
    }

    @Test
    void test_save() {
        personService.removeById(1L);
        PersonEntity book1 = new PersonEntity();
        book1.setId(1L);
        book1.setName("tom");
        personService.save(book1);
    }

    @Test
    void test_分页() {
        Page<PersonEntity> page = personService.lambdaQuery()
                .page(Page.of(1, 100));
        System.out.println("page = " + page.getRecords());
    }

    @Test
    void test_更新全表() {
        personService.lambdaUpdate()
                .setSql("age = age+1")
                .update(new PersonEntity());
    }


    @Test
    void test_updateById() {
        PersonEntity book = personService.getById(1);
        // book.setVersion(book.getVersion()+1);
        // book.setBookName(book.getBookName()+"_v1");
        // book.setBookName("_v1");
        book.setAge(10); // 自动+1,再set值,就无效,@Version原理如此
        personService.updateById(book);
    }

    @Test
    void test_updateById_bookPrice() {
        personService.lambdaUpdate()
                .eq(PersonEntity::getId, 1)
                .setSql("age = age+1")
                .update(new PersonEntity());
    }

    @Test
    void test_updateById_bookPrice2() {


//        LambdaUpdateWrapper<BookEntity> updateWrapper = Wrappers.lambdaUpdate();

        LambdaUpdateWrapper<PersonEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PersonEntity::getId, 1);


        // 对 age 字段进行自增操作

        // 对 salary 字段进行数学计算
//        updateWrapper.set(BookEntity::getBookPrice, (item)->{
//        return  1D;
//        });


    }

    @Test
    void test_updateById_bookPrice_2() {
        // 有bug
        PersonEntity entity = new PersonEntity();
        entity.setAge(10);
        entity.setId(1L);
        AbstractWrapper<PersonEntity, SFunction<PersonEntity, ?>, LambdaUpdateWrapper<PersonEntity>> wrapper = personService.lambdaUpdate()
                .eq(PersonEntity::getId, 1)
                .setSql("book_price = book_price+10")
                .getWrapper();

//        personService.saveOrUpdate(entity, wrapper);
        personService.update(entity, wrapper);
    }

    @Test
    void test_lambdaUpdate() {
        //  1.不会会自动填充
        PersonEntity book = personService.getById(1);
        personService.lambdaUpdate()
                .ge(PersonEntity::getId, 1)
                .set(PersonEntity::getName, book.getName() + "_v1")
                .update();
    }

    @Test
    void test_lambdaUpdate1() {
        // bookService.lambdaUpdate()
        //         .eq(BookEntity::getBookAuthor, "tom")
        //         .set(BookEntity::getBookName, "一千零一夜")
        //         .update(new BookEntity());

        PersonEntity book = new PersonEntity();
        book.setName("tom");
        book.setAge(20);

        // bookService.saveOrUpdate(book, Wrappers.<BookEntity>lambdaQuery()
        //         .eq(BookEntity::getBookAuthor, "tom"));

        personService.update(book, personService.lambdaQuery()
                .eq(PersonEntity::getName, "tom")
                .getWrapper());

    }

    @Test
    void test_lambdaUpdate2() {
        //  1.update(new 对象)才会自动填充
        //  2. 乐观锁失效,不支持,需要手动添加锁
        PersonEntity book = personService.getById(1);
        personService.lambdaUpdate()
                .eq(PersonEntity::getId, 1)
                .set(PersonEntity::getName, "jim")
                .update(new PersonEntity());
    }

    @Test
    void test_lambdaUpdate3() {
        //  乐观锁失效,不支持,需要手动添加锁
        PersonEntity book = personService.getById(1);
        personService.lambdaUpdate()
                .eq(PersonEntity::getId, 1)
                .eq(PersonEntity::getVersion, book.getVersion())
                .set(PersonEntity::getName, "jim")
                .set(PersonEntity::getVersion, book.getVersion() + 1)
                .update(new PersonEntity());
    }


    @Test
    void test2() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);

        LocalDateTime startTime = LocalDateTimeUtil.beginOfDay(dateTime);
        LocalDateTime endTime = LocalDateTimeUtil.endOfDay(dateTime);
        List<PersonEntity> list = personService.lambdaQuery()
                .ge(PersonEntity::getBirthday, startTime)
                .list();
        System.out.println("list = " + list);
    }

    @Test
    void test_remove() {
        boolean remove = personService.lambdaUpdate()
                .remove();
        System.out.println("remove = " + remove);
    }


    @Test
    void test3() {

        PersonMapper baseMapper = (PersonMapper) personService.getBaseMapper();
        IPage<PersonEntity> page = baseMapper.customSqlSegment(Wrappers.<PersonEntity>lambdaQuery()
                        .select(PersonEntity::getId)
                        .gt(PersonEntity::getAge, 10),
                Page.of(1, 3));
        System.out.println("page.getSize() = " + page.getSize());
        System.out.println("page.getPages() = " + page.getPages());
        System.out.println("page.getCurrent() = " + page.getCurrent());
        System.out.println("page.getTotal() = " + page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    void test_tableListHasDelete() {

        PersonMapper baseMapper = (PersonMapper) personService.getBaseMapper();
        IPage<PersonEntity> page = baseMapper.sqlSegment(Wrappers.<PersonEntity>lambdaQuery()
                        // .select(BookEntity::getId)
                        .gt(PersonEntity::getAge, 10),
                Page.of(1, 3));
        System.out.println("page.getSize() = " + page.getSize());
        System.out.println("page.getPages() = " + page.getPages());
        System.out.println("page.getCurrent() = " + page.getCurrent());
        System.out.println("page.getTotal() = " + page.getTotal());
        System.out.println(page.getRecords());
    }


    // 动态表名
    @Test
    @SuppressWarnings(value = "all")
    void test_动态表() {
        MyTableNameHandler.setData("_01");//此处调用方法
        List<PersonEntity> list = personService.list();
        System.out.println("list = " + list);
    }

    @Test
    @SuppressWarnings(value = "all")
    void test_多租户() {
        MyTenantLineHandler.setTenantId("100");
        List<PersonEntity> list = personService.list();
        System.out.println("list = " + list);
    }
}
