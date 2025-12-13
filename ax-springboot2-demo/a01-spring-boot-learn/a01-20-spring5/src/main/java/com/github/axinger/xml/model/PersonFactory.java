package com.github.axinger.xml.model;

import org.springframework.beans.factory.FactoryBean;

public class PersonFactory implements FactoryBean<Person> {


    @Override
    public Person getObject() throws Exception {
        Person person = new Person();
        person.setName("person工厂名称");
        return person;
    }

    @Override
    public Class<?> getObjectType() {
        return Person.class;
    }

    // 是否单例
    @Override
    public boolean isSingleton() {
//        return false;
        return true;
    }
}
