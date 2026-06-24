//package com.github.axinger;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.github.axinger.sys.domain.DepartmentEntity;
//import com.github.axinger.sys.service.DepartmentService;
//import com.github.axinger.util.MyBatisPlusSQLExtensionMethodUtil;
//import lombok.experimental.ExtensionMethod;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Date;
//
///**
// * MyBatis-Plus @ExtensionMethod 扩展示例
// * <p>
// * 通过 Lombok 的 @ExtensionMethod 注解，可以为 LambdaQueryChainWrapper / LambdaQueryWrapper
// * 添加自定义的链式条件构建方法，如 $matchAgainst、$rangeDateTime、$limit 等。
// */
//@ExtensionMethod(MyBatisPlusSQLExtensionMethodUtil.class)
//@SpringBootTest
//public class ExtensionMethodDemoTest {
//
//    @Autowired
//    private DepartmentService departmentService;
//
//    /**
//     * 示例 1：使用 $limit 限制查询数量
//     */
//    @Test
//    public void testLimit() {
//        // 扩展前：.last("LIMIT 10")
//        // 扩展后：.$limit(10)
//        var list = departmentService.lambdaQuery()
//                .eq(DepartmentEntity::getId, 1)
//                .$limit(10)
//                .list();
//
//        System.out.println("list = " + list);
//    }
//
//    /**
//     * 示例 2：使用 $rangeDateTime 进行日期范围查询
//     */
//    @Test
//    public void testRangeDateTime() {
//        Date startTime = new Date(System.currentTimeMillis() - 86400000L); // 昨天
//        Date endTime = new Date(); // 现在
//
//        // 扩展前：
//        //   .ge(startTime != null, DepartmentEntity::getCreateTime, startTime)
//        //   .le(endTime != null, DepartmentEntity::getCreateTime, endTime)
//        // 扩展后：
//        var list = departmentService.lambdaQuery()
//                .$rangeDateTime(DepartmentEntity::getCreateTime, startTime, endTime)
//                .$limit(10)
//                .list();
//
//        System.out.println("list = " + list);
//    }
//
//    /**
//     * 示例 3：使用 $matchAgainst 进行全文检索（MySQL）
//     */
//    @Test
//    public void testMatchAgainst() {
//        String keywords = "技术部";
//
//        // 扩展前：
//        //   .apply(keywords != null, "MATCH(name,description) AGAINST ({0})", keywords)
//        // 扩展后：
//        var list = departmentService.lambdaQuery()
//                .$matchAgainst(keywords != null, DepartmentEntity::getName, DepartmentEntity::getDescription, keywords)
//                .$limit(10)
//                .list();
//
//        System.out.println("list = " + list);
//    }
//
//    /**
//     * 示例 4：扩展方法也可以用于 LambdaQueryWrapper
//     */
//    @Test
//    public void testWrapperExtension() {
//        Date startTime = new Date(System.currentTimeMillis() - 86400000L);
//        Date endTime = new Date();
//
//        LambdaQueryWrapper<DepartmentEntity> wrapper = new LambdaQueryWrapper<DepartmentEntity>()
//                .eq(DepartmentEntity::getId, 1)
//                .$rangeDateTime(DepartmentEntity::getCreateTime, startTime, endTime)
//                .$limit(5);
//
//        var list = departmentService.list(wrapper);
//        System.out.println("list = " + list);
//    }
//
//    /**
//     * 示例 5：与自定义 chainQuery 结合使用
//     * <p>
//     * 注意：$matchAgainst 等扩展方法扩展的是 MyBatis-Plus 原生的 LambdaQueryChainWrapper，
//     * 而 chainQuery() 返回的是自定义的 DepartmentServiceChainWrapper。
//     * 两者可以分别用于不同场景：
//     * - lambdaQuery() + @ExtensionMethod：通用条件构建
//     * - chainQuery()：自定义终端方法（如 listLeftSon()）
//     */
//    @Test
//    public void testChainQuery() {
//        // 使用自定义 chainQuery 调用 listLeftSon
//        var list = departmentService.chainQuery()
//                .eq(DepartmentEntity::getId, 1)
//                .listLeftSon();
//
//        System.out.println("list = " + list);
//    }
//}
