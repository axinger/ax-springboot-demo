package com.github.axinger.dao;

import com.github.axinger.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * CrudRepository提供了基本的增删改查，不再需要我们自定义。
 * PagingAndSortingRepository 该接口提供了分页与排序的操作， 也就是该接口不用自己定义增删改查方法和分页排序方法
 */
public interface PersonJpaRepository extends JpaRepository<Person, Integer> {

    List<Person> findByUserName(String userName);

    List<Person> findByUserNameLike(String userName);

    @Query(value = "select * from sys_person where age>:age", nativeQuery = true)
    Page<Person> findByAgeGreaterThan(@Param("age") Integer age, Pageable pageable);
//    /**
//     * 4.@Query 注解
//     * 有的时候，Spring Data规范里提供的查询关键字并不能满足我们的查询需求，这个时候就可以使用 @Query 关键字，来自定义查询 SQL。
//     * nativeQuery：代表本地查询，就是使用原生的sql语句。
//     *
//     * @return
//     */
//    @Query(value = "select * from sys_person  where id=(select max(id) from users)", nativeQuery = true)
//    Person getMaxIdUser();
//
//    /**
//     * 5.@Param注解
//     * 用来注入参数
//     *
//     * @param userName
//     * @return
//     */
//    @Query(value = "select * from sys_person where user_name like %:userName%", nativeQuery = true)
//    List<Person> findByNameMatch(@Param("userName") String userName);
}
