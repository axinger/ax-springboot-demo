package com.github.axinger;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.axinger.domain.*;
import com.github.axinger.dto.EmployeeDTO;
import com.github.axinger.dto.EmployeeDTO2;
import com.github.axinger.service.*;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.query.FunctionQueryColumn;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.update.UpdateWrapper;
import com.mybatisflex.core.util.UpdateEntity;
import org.apache.ibatis.cursor.Cursor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;

import static com.github.axinger.domain.table.AccountTableDef.ACCOUNT;
import static com.github.axinger.domain.table.ArticleTableDef.ARTICLE;
import static com.github.axinger.domain.table.DepartmentTableDef.DEPARTMENT;
import static com.github.axinger.domain.table.EmployeeTableDef.EMPLOYEE;
import static com.github.axinger.domain.table.PersonTableDef.PERSON;
import static com.mybatisflex.core.constant.FuncName.GROUP_CONCAT;
import static com.mybatisflex.core.query.QueryMethods.*;

@SpringBootTest
class A02MybatisFlexApplicationTest {

    @Autowired
    private ITbAccountService iTbAccountService;

    @Autowired
    private PersonService personService;
    @Autowired
    private ITbArticleService iTbArticleService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void test_新增list() {

        List<Account> list = new ArrayList<>();
        {
            Account account = new Account();
            account.setId(1);
            account.setAge(10);
            account.setUserName("jim");
            account.setBirthday(LocalDateTimeUtil.parse("1990-05-01 12:00:00", "yyyy-MM-dd HH:mm:ss"));
            list.add(account);
        }
        {
            Account account = new Account();
            account.setId(2);
            account.setAge(11);
            account.setUserName("tom");
            account.setBirthday(LocalDateTimeUtil.parse("1990-05-01 12:00:00", "yyyy-MM-dd HH:mm:ss"));
            list.add(account);
        }
        iTbAccountService.saveBatch(list);
    }

    //    insert(entity)：插入实体类数据，不忽略 null 值。
//    insertSelective(entity)：插入实体类数据，但是忽略 null 的数据，只对有值的内容进行插入。这样的好处是数据库已经配置了一些默认值，这些默认值才会生效。
    @Test
    public void test_新增2() {
        Account account = new Account();
        account.setUserName("michael");


//        account.setUserName();
        // 新增数据字段内容是数据库的某个 函数 或者 SQL片段 生成的内容，而非我们手动设置的内容。
        Account newAccount = UpdateWrapper.of(account)
//       .setRaw("birthday", "now()")
//       .setRaw(ACCOUNT.BIRTHDAY, "now()")
                .setRaw(Account::getBirthday, "now()")
                .toEntity();

        System.out.println("newAccount = " + newAccount);

        iTbAccountService.save(newAccount); // 不可以
//        iTbAccountService.getMapper().insert(newAccount);
    }

    @Test
    void test_删除() {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(ACCOUNT);
        queryWrapper.where(ACCOUNT.ID.eq(1));

//通过 queryWrapper 删除
        iTbAccountService.remove(queryWrapper);

        iTbAccountService.getMapper().deleteByCondition(ACCOUNT.ID.le(10));
    }

    @Test
    void test_更新1() {

        Account account = UpdateEntity.of(Account.class, 1);
        account.setUserName(null);
        account.setAge(10);
        iTbAccountService.getMapper().update(account);

        iTbAccountService.updateById(account);
    }

    @Test
    void test_更新2() {

        Account account = new Account();
        account.setId(1);
        account.setUserName("jim");
        account.setAge(11);

        iTbAccountService.updateById(account);
    }

    @Test
    void test_更新3() {

        // QueryWrapper 条件修改
        Account account = new Account();
        account.setUserName(null);
        account.setAge(12);

        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(1));
        iTbAccountService.update(account, queryWrapper);
    }

    @Test
    void test_更新4() {

        Account account = UpdateEntity.of(Account.class, 1);

//        account.setUserName(null);

// 通过 UpdateWrapper 操作 account 数据
        UpdateWrapper<Account> wrapper = UpdateWrapper.of(account);
//        wrapper.setRaw("age", "age + 1");
        //更高级的用法
        wrapper.set(ACCOUNT.AGE, ACCOUNT.AGE.add(1));
        wrapper.set(Account::getUserName, "张三");


        //会直接参与 SQL 拼接，可能会造成 SQL 错误
        wrapper.setRaw(Account::getUserName, "张三");


//        wrapper.set(ACCOUNT.USER_NAME,"TOM");
//        iTbAccountService.getMapper().update(account);
        iTbAccountService.updateById(wrapper.toEntity());


    }

    @Test
    void test_更新5() {
        UpdateChain.of(Account.class)
                .from(ACCOUNT)
                .set(Account::getUserName, "张三")
                .setRaw(Account::getAge, "age + 1")
                .where(Account::getId).eq(1)
                .update();

    }


    @Test
    void test_更新6() {
        iTbAccountService.updateChain()
                .set(Account::getAge, 20)
                .where(Account::getId).eq(1)
                .update();
    }

    @Test
    void test_更新_填充() {

        Person person = personService.getById(2);
//
        UpdateWrapper<Person> wrapper = UpdateWrapper.of(person);
        wrapper.set(PERSON.AGE, PERSON.AGE.add(1));
//        personService.updateById(wrapper.toEntity());


        UpdateChain.of(personService.getMapper())
                .from(PERSON)
                .set(PERSON.AGE, PERSON.AGE.add(1))
                .where(PERSON.ID.eq(2)
                        .and(PERSON.VERSION.eq(person.getVersion()))
                )
                .update();

    }

    @Test
    public void test_查询1() {

        QueryWrapper wrapper = QueryWrapper.create()
                .select()
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(1));
        Account account = iTbAccountService.getOne(wrapper);
        System.out.println("account = " + account);

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select()
//                .from(ACCOUNT)
                .where(Account::getId).eq(1)
                .and(Account::getAge).eq(15)
                .orderBy(Account::getId)
                .desc();
        List<Account> list1 = iTbAccountService.list(queryWrapper);
        System.out.println("list1 = " + list1);

        QueryCondition and = QueryCondition.createEmpty()
                .and(ACCOUNT.ID.eq(1));

        List<Account> list2 = iTbAccountService.list(and);
        System.out.println("list2 = " + list2);


//        QueryChain.of(iTbAccountService.getMapper())
        Account one = QueryChain.of(Account.class)
                .from(ACCOUNT)
                .select(Account::getId)
//                .where(ACCOUNT.ID.eq(1))
                .and(Account::getId).eq(1)
                .and(Account::getAge).eq(15)
                .orderBy(Account::getId)
                .desc()
                .one();
        System.out.println("one = " + one);


        Account one1 = new Account()
                .where(Account::getId).eq(1L)
                .one();
        System.out.println("one1 = " + one1);

        // 继承model
        Account one2 = Account.create()
                .where(Account::getId).eq(1L)
                .one();
        System.out.println("one2 = " + one2);


        String sql = iTbAccountService.queryChain()
                .select()
                .from(ACCOUNT)
                .where(Account::getId).eq(1L)
                .and(val -> {
                    val.and(Account::getAge).eq(1L)
                            .or(Account::getBirthday).eq(1L);

                }).toSQL();
        System.out.println("sql = " + sql);


    }

    @Test
    public void test_查询_one() {

        Account one = iTbAccountService.queryChain()
                .select("top 1 * ")
                .from(ACCOUNT)
                .where(Account::getAge).ge(1)
//                .limit(1) // 会自动匹配数据库,但是sql语句不好
                .one();
        System.out.println("one = " + one);

    }

    @Test
    public void test_查询_list() {

        List<Account> list = iTbAccountService.queryChain()
                .select()
                .from(ACCOUNT)
                .where(Account::getAge).ge(1)
                .list();
        System.out.println("list = " + list);


        List<Account> list2 = iTbAccountService.queryChain()
                .select()
                .from(Account.class)
                .where(Account::getAge).ge(1)
                .list();
        System.out.println("list2 = " + list2);

    }

    @Test
    public void test_查询2() {

        //进行事务操作，
        Db.tx(() -> {

            QueryWrapper wrapper = QueryWrapper.create()
                    .from(ACCOUNT)
                    .select()
                    .where(ACCOUNT.ID.ge(1));

            //游标查询
//            但由于游标查询是在 for 循环的时候，才去数据库拿数据。
//            因此必须保证 selectCursorByQuery 方法及其处理必须是在事务中进行，才能保证其链接并未与数据库断开。
//            以下场景经常需要用到游标查询功能：
//
//            1、数据查询并写入到缓存
//            2、Excel 导出等
            Cursor<Account> accounts = iTbAccountService.getMapper().selectCursorByQuery(wrapper);


            for (Account account : accounts) {
                System.out.println(account);
            }
            return true;
        });
    }

    @Test
    public void test_查询_自动映射() {
        Account list = QueryChain.of(iTbAccountService.getMapper())
                .from(ACCOUNT)
                .select(
                        groupConcat(ACCOUNT.ID),
                        max(ACCOUNT.AGE).as(Account::getMaxAge),
                        min(ACCOUNT.AGE).as(Account::getMimAge),
                        avg(ACCOUNT.AGE).as(Account::getAvgAge)
                ).where(ACCOUNT.ID.ge(1))
                .one();
        System.out.println("list = " + list);
    }

    @Test
    public void test_查询_分组() {
        List<Account> list = QueryChain.of(iTbAccountService.getMapper())
                .from(ACCOUNT)
                .select(
                        groupConcat(ACCOUNT.ID).as(Account::getGroupAfterId),
                        new FunctionQueryColumn(GROUP_CONCAT, ACCOUNT.USER_NAME).as(Account::getUserName),
                        ACCOUNT.AGE
                )
                .where(ACCOUNT.ID.ge(1))
                .groupBy(ACCOUNT.AGE)
                .list();
        System.out.println("list = " + list);
    }

    @Test
    public void test_查询_逻辑删除() {

        List<Person> list = personService.list();
        System.out.println("list = " + list);

        List<Person> list1 = QueryChain.of(personService.getMapper())
                .from(PERSON)
                .select(PERSON.ALL_COLUMNS)
                .list();
        System.out.println("list1 = " + list1);


        // 跳过逻辑删除,查询
        LogicDeleteManager.execWithoutLogicDelete(() -> {
            List<Person> list2 = QueryChain.of(personService.getMapper())
                    .from(PERSON)
                    .select(PERSON.DEFAULT_COLUMNS)
                    .list();
            System.out.println("list2 = " + list2);
        });

    }

    @Test
    public void test_查询_多租户() {

        TenantManager.setTenantFactory(() -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            Long tenantId = (Long) attributes.getAttribute("tenantId", RequestAttributes.SCOPE_REQUEST);


            //通过这里返回当前租户 ID
            return new Object[]{100};
        });
        List<Person> list = personService.list();
        System.out.println("list = " + list);

    }

    @Test
    public void test_多表查询() {

        QueryWrapper query = QueryWrapper.create()
                .select(ARTICLE.ALL_COLUMNS)
                .select(ACCOUNT.USER_NAME.as(Article::getAuthorName)
                        , ACCOUNT.AGE.as(Article::getAuthorAge)
                        , ACCOUNT.BIRTHDAY
                )
                .from(ARTICLE)
                .leftJoin(ACCOUNT).on(ARTICLE.ACCOUNT_ID.eq(ACCOUNT.ID))
                .where(ACCOUNT.ID.ge(1));

        List<Article> results = iTbArticleService.getMapper().selectListByQueryAs(query, Article.class);
        System.out.println(results);

    }

    @Test
    public void test_多表查询_自定义对象字段映射() {
        List<EmployeeDTO> list = QueryChain.of(employeeService.getMapper())
                .select(
                        EMPLOYEE.ALL_COLUMNS, //员工所有字段
                        DEPARTMENT.NAME.as(EmployeeDTO::getDeptName) //部门字段,别名
                ).from(EMPLOYEE)
                .leftJoin(DEPARTMENT).on(DEPARTMENT.ID.eq(EMPLOYEE.DEPT_ID))
                .where(EMPLOYEE.ID.ge(1))
                .listAs(EmployeeDTO.class);
        System.out.println("list = " + list);

    }

    @Test
    public void test_多表查询_自定义对象字段映射2() {

        List<EmployeeDTO2> list = QueryChain.of(employeeService.getMapper())
                .select(
                        EMPLOYEE.DEFAULT_COLUMNS, //员工所有字段
                        DEPARTMENT.DEFAULT_COLUMNS //部门字段,别名
//                        DEPARTMENT.ID.as(DepartmentDTO::getId),
//                        DEPARTMENT.NAME.as(DepartmentDTO::getName)
                ).from(EMPLOYEE)
                .leftJoin(DEPARTMENT).on(DEPARTMENT.ID.eq(EMPLOYEE.DEPT_ID))
                .where(EMPLOYEE.ID.ge(1))
                .listAs(EmployeeDTO2.class);
        System.out.println("list = " + list);

    }

    @Test
    public void test_多表查询_一对多() {

        List<Department> departments = departmentService.getMapper().selectAllWithRelations();
        System.out.println(departments);
    }

    @Test
    public void test_多表查询_关系为多对一() {

        List<Employee> employees = employeeService.getMapper().selectAllWithRelations();

//        QueryWrapper queryWrapper = QueryWrapper.create()
//                .from(EMPLOYEE);
//        queryWrapper.select(EMPLOYEE.ID,EMPLOYEE.NAME,EMPLOYEE.DEPT_ID,EMPLOYEE.DEPARTMENT);
//        List<Employee> employees = employeeService.getMapper().selectListByQuery(queryWrapper)
        ;
        System.out.println(employees);
    }
}
