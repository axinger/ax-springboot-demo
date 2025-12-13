package com.github.axinger;

import com.github.axinger.model.Role;
import com.github.axinger.model.User;
import com.github.axinger.model.UserRoleDto;
import com.github.axinger.model.UserRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoMapstructAppTest {


    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void test() {
        Role role = new Role(2L, "administrator", "超级管理员");
        User user = new User();
        user.setId(1L);
        user.setAge(20);
        user.setUsername("jm");
        user.setRole(role);


//        UserRoleDto userRoleDto = userRoleMapper.useParameter(user, "myUserRole");
//        System.out.println(userRoleDto);
    }


    @Test
    public void test2() {
        Role role1 = Role.builder()
                .roleName("管理员")
                .build();
        User user1 = User.builder()
                .id(1L)
                .username("jim")
                .role(role1)
                .age(20)
                .form(Map.of("other", "p2"))
                .build();

        UserRoleDto userRoleDto = userRoleMapper.toUserRoleDto(user1);
        System.out.println("userRoleDto = " + userRoleDto);
    }


    @Test
    public void test3() {
        UserRoleDto userRoleDto = userRoleMapper.defaultConvert();
        System.out.println(userRoleDto);
    }


    @Test
    public void test4() {
        Role role1 = Role.builder()
                .roleName("管理员")
                .build();
        User user1 = User.builder()
                .id(1L)
                .username("jim")
                .role(role1)
                .build();
        UserRoleDto userRoleDto = userRoleMapper.toUserRoleDto(user1);
        System.out.println("builder方式userRoleDto = " + userRoleDto);
    }

    @Test
    public void test5() {
        Role role1 = Role.builder()
                .roleName("管理员")
                .build();
        User user1 = User.builder()
                .id(1L)
                .username("jim")
                .role(role1)
                .build();
        List<UserRoleDto> list = userRoleMapper.toUserRoleDtoList(List.of(user1));
        System.out.println("list = " + list);
    }
}
