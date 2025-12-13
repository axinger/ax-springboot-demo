package com.github.axinger;

import cn.hutool.extra.cglib.CglibUtil;
import org.junit.jupiter.api.Test;

//CGLib (Code Generation Library) 是一个强大的,高性能,高质量的Code生成类库，通过此库可以完成动态代理、Bean拷贝等操作。
public class CglibUtilTests {

    @Test
    void test1() {
        Person bean = new Person();
        bean.setName("Hello world");

        Person otherBean = new Person();

        CglibUtil.copy(bean, otherBean);

// 值为"Hello world"
        String name = otherBean.getName();
        System.out.println("name = " + name);

    }
}
