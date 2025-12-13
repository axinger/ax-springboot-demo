package com.github.axinger;

import com.github.axinger.dao.SysDepartmentDTO;
import com.github.axinger.dao.SysEmployeeDTO;
import com.github.axinger.dto.Gender;
import com.github.axinger.entity.SysDepartmentEntity;
import com.github.axinger.entity.SysEmployeeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JpaSysDepartmentTests {

    @Autowired
    SysDepartmentDTO sysDepartmentDTO;

    @Autowired
    SysEmployeeDTO sysEmployeeDTO;

    @Test
    public void test() {
        // 创建部门
        SysDepartmentEntity department = SysDepartmentEntity.builder()
                .code("001")
                .name("行政部")
                .build();
        department.setId(2L); // 测试用 ID，实际应由 DB 生成

        // 创建员工（使用不同 ID！）
        SysEmployeeEntity zhangsan = SysEmployeeEntity.builder()
                .id(21L)
                .code("EMP001")  // ✅ 补上 code！你原代码漏了
                .name("张三")
                .email("zhangsan@163.com")
                .gender(Gender.MALE)
                .build();

        SysEmployeeEntity lisi = SysEmployeeEntity.builder()
                .id(22L)  // ✅ 修改为不同 ID
                .code("EMP002")
                .name("李四")
                .email("lisi@163.com")
                .gender(Gender.MALE)
                .build();

        // 使用安全方法维护双向关系
        department.addEmployee(zhangsan);
        department.addEmployee(lisi);

        // ✅ 现在 department.getEmployees() 和每个 employee.getDepartment() 都正确

        // 保存（假设 save 方法支持级联）
        SysDepartmentEntity saved = sysDepartmentDTO.save(department);

        System.out.println("saved = " + saved);
        System.out.println("员工数量: " + saved.getEmployees().size());
    }
}
