package com.github.axinger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户实体类
 * 实现 UserDetails 接口，用于 Spring Security 认证
 * 使用 MyBatis-Plus 注解实现 ORM 映射
 */
@Data
@TableName("sys_user")
public class SysUser implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("real_name")
    private String realName;

    @TableField("avatar")
    private String avatar;

    @TableField("enabled")
    private Integer enabled;

    @TableField("account_non_expired")
    private Integer accountNonExpired;

    @TableField("account_non_locked")
    private Integer accountNonLocked;

    @TableField("credentials_non_expired")
    private Integer credentialsNonExpired;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("deleted")
    private Integer deleted;

    /**
     * 用户角色列表，非数据库字段
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 获取用户权限列表
     * 将角色编码转换为 Spring Security 的 GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired != null && accountNonExpired == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked != null && accountNonLocked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired != null && credentialsNonExpired == 1;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null && enabled == 1;
    }
}
