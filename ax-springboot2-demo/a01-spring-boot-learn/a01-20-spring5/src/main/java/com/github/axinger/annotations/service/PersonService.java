package com.github.axinger.annotations.service;

import com.github.axinger.annotations.dao.PersonDao;
import com.github.axinger.annotations.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


//@Component(value = "personService")
@Service
public class PersonService {

//    @Resource
    /**
     * 会导致组件与IoC容器紧耦合（这是最重要的原因，离开了IoC容器去使用组件，在注入依赖时就会十分困难）
     * <p>
     * Field注入虽然有很多缺点，但它的好处也不可忽略：那就是太方便了。
     * 使用构造器或者setter注入需要写更多业务无关的代码，十分麻烦，而字段注入大幅简化了它们。并且绝大多数情况下业务代码和框架就是强绑定的，完全松耦合只是一件理想上的事，牺牲了敏捷度去过度追求松耦合反而得不偿失。
     */
//    @Autowired

//    private Person person;

    /// 构造器注解
    private final Person person;
    @Resource // javax.annotation.Resource; 非spring 的注解
    private PersonDao personDao;
    @Value(value = "jim")
    private String name;

    public PersonService(Person person) {
        this.person = person;
    }

    public void add() {
        System.out.println("add.....PersonService" + " name = " + name);

        person.add();
        personDao.add();
    }
}
