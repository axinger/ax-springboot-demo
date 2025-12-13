package com.github.axinger.dao;


import com.github.axinger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * collectionResourceRel是json的最外层显示名称，path是访问路径
 */
@RepositoryRestResource(
//        exported = true, //资源是否暴露，默认true
//        path = "users",//资源暴露的path访问路径，默认实体名字+s
//        collectionResourceRel = "list",//资源名字，默认实体名字
//        collectionResourceDescription = @Description("用户基本信息资源"),//资源描述
//        itemResourceRel = "userDetail",//取资源详情的Item名字 links 里面的
//        itemResourceDescription = @Description("用户详情")
) //指定基路径


public interface UserDAO extends JpaRepository<User, Long> {


    // http://localhost:8080/users/search/findByName?name=jim
    //自定义方法，JpaRepository提供了很多供使用
//    @RestResource(path = "names")
    List<User> findByName(String name);//参数可以用 @Param 自定义注解
//
//    //根据name查询，查询到的结果根据Id排序
//    List<User> findByNameInOrderById(List<String> list);


    /**
     * 不暴漏删除接口
     */
//    @Override
//    @RestResource(exported = false)
//    void deleteById(Long id);
//
//    @Override
//    @RestResource(exported = false)
//    void delete(User entity);
    @Override
    @RestResource(exported = true)
    List<User> findAll();
}
