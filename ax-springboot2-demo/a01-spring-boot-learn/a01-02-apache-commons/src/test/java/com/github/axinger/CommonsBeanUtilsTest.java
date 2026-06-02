package com.github.axinger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class CommonsBeanUtilsTest {

    // 定义测试用的 Bean
    public static class User {
        private String name;
        private Integer age;
        private Address address;

        public User() {}

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public Address getAddress() { return address; }
        public void setAddress(Address address) { this.address = address; }

        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + ", address=" + address + "}";
        }
    }

    public static class Address {
        private String city;
        private String street;

        public Address() {}

        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        @Override
        public String toString() {
            return "Address{city='" + city + "', street='" + street + "'}";
        }
    }

    // ==================== BeanUtils ====================

    @Test
    void testBeanUtils_copyProperties() throws Exception {
        User source = new User("Alice", 25);
        source.setAddress(new Address("Beijing", "Chang'an Street"));

        User target = new User();

        // 属性复制（浅拷贝）
        BeanUtils.copyProperties(target, source);

        System.out.println("Source: " + source);
        System.out.println("Target: " + target);
        System.out.println("地址是否同一对象: " + (source.getAddress() == target.getAddress())); // true，浅拷贝
    }

    @Test
    void testBeanUtils_setProperty() throws Exception {
        User user = new User();

        // 动态设置属性
        BeanUtils.setProperty(user, "name", "Bob");
        BeanUtils.setProperty(user, "age", "30"); // 自动类型转换

        System.out.println(user); // User{name='Bob', age=30}
    }

    @Test
    void testBeanUtils_getProperty() throws Exception {
        User user = new User("Alice", 25);
        user.setAddress(new Address("Shanghai", "Nanjing Road"));

        // 获取简单属性
        String name = BeanUtils.getProperty(user, "name");
        System.out.println("Name: " + name); // Alice

        // 获取嵌套属性（使用 . 号）
        String city = BeanUtils.getProperty(user, "address.city");
        System.out.println("City: " + city); // Shanghai

        String street = BeanUtils.getProperty(user, "address.street");
        System.out.println("Street: " + street); // Nanjing Road
    }

    @Test
    void testBeanUtils_populate() throws Exception {
        // 从 Map 填充 Bean
        Map<String, String> map = new HashMap<>();
        map.put("name", "Charlie");
        map.put("age", "35");

        User user = new User();
        BeanUtils.populate(user, map);

        System.out.println(user); // User{name='Charlie', age=35}
    }

    @Test
    void testBeanUtils_describe() throws Exception {
        User user = new User("David", 28);

        // 将 Bean 转为 Map（注意：返回的 value 都是 String 类型）
        Map<String, String> map = BeanUtils.describe(user);
        map.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    // ==================== PropertyUtils ====================

    @Test
    void testPropertyUtils() throws Exception {
        User user = new User("Eve", 22);
        user.setAddress(new Address("Guangzhou", "Zhujiang Road"));

        // 获取属性（返回原始类型，不做类型转换）
        String name = (String) PropertyUtils.getProperty(user, "name");
        Integer age = (Integer) PropertyUtils.getProperty(user, "age");
        System.out.println("Name: " + name + ", Age: " + age);

        // 设置属性
        PropertyUtils.setProperty(user, "name", "Frank");
        System.out.println(user); // User{name='Frank', age=22}

        // 获取嵌套属性
        Address address = (Address) PropertyUtils.getProperty(user, "address");
        System.out.println("Address: " + address);

        // 获取属性类型
        Class<?> nameType = PropertyUtils.getPropertyType(user, "name");
        System.out.println("name 的类型: " + nameType); // class java.lang.String
    }
}
