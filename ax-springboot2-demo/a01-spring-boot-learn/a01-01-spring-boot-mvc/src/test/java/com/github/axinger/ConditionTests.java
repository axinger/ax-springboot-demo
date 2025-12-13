package com.github.axinger;

import cn.hutool.extra.spring.SpringUtil;
import com.github.axinger.model.bean.MyBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "my.custom1.property=true",
        "my.custom2.property=false"
})
public class ConditionTests {


    /// 这样不行
//    @Resource(name = "myBean1")
//    MyBean myBean1;
//    //
//    @Resource(name = "myBean2")
//    MyBean myBean2;

    /// 可以
    @Autowired(required = false)
    @Qualifier("myBean1")
    private MyBean myBean1;

    @Autowired(required = false)
    @Qualifier("myBean2")
    private MyBean myBean2;
    /// 可以， 有告警
    @Autowired
    @Qualifier("myBean1")
    private Optional<MyBean> myBean1Opt;
    @Autowired
    @Qualifier("myBean2")
    private Optional<MyBean> myBean2Opt;
    /// 可以
    @Autowired
    @Qualifier("myBean1")
    private ObjectProvider<MyBean> myBean1Provider;
    @Autowired
    @Qualifier("myBean2")
    private ObjectProvider<MyBean> myBean2Provider;

    @Test
    public void test1() {

        if (myBean1 != null) {
            System.out.println("myBean1 = " + myBean1);
        } else {
            System.out.println("myBean1 未创建，条件不满足");
        }

        if (myBean2 != null) {
            System.out.println("myBean2 = " + myBean2);
        } else {
            System.out.println("myBean2 未创建，条件不满足");
        }
    }

    @Test
    public void test2() {
        myBean1Opt.ifPresent(bean -> System.out.println("myBean1 = " + bean));
        myBean2Opt.ifPresent(bean -> System.out.println("myBean2 = " + bean));
    }

    @Test
    public void test4() {
        myBean1Provider.ifAvailable(bean ->
                System.out.println("myBean1 = " + bean));
        myBean2Provider.ifAvailable(bean ->
                System.out.println("myBean2 = " + bean));
    }

    @Test
    public void test5() {

        MyBean bean = SpringUtil.getBean(MyBean.class);
        System.out.println("bean = " + bean);


        MyBean bean1 = SpringUtil.getBean("myBean1", MyBean.class);
        System.out.println("bean1 = " + bean1);
        try {

            MyBean bean2 = SpringUtil.getBean("myBean2", MyBean.class);
            System.out.println("bean2 = " + bean2);
        } catch (Exception e) {
            System.err.println("bean2 未创建，条件不满足");
        }

        Map<String, MyBean> beansOfType = SpringUtil.getBeansOfType(MyBean.class);
        System.out.println("beansOfType = " + beansOfType);


        String property = SpringUtil.getProperty("my.custom2.property");
        System.out.println("property = " + property);


        Boolean property1 = SpringUtil.getProperty("my.custom2.property", Boolean.class, false);
        System.out.println("property1 = " + property1);
    }
}
