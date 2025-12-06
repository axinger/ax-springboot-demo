package com.github.axinger.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @TableName sys_user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// autoResultMap 解析json格式
@TableName(value = "sys_user", autoResultMap = true)
//@TableName(value = "sys_user",resultMap="getInfo")
public class SysUserEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;
    /**
     *
     */
    @TableField(value = "age")
    private Integer age;


    // JSON 字段映射
    //java 和 XML，都需要写 typeHandler
//    @TableField(value = "info")
    @TableField(value = "info", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> info = new HashMap<>();


    @TableField(value = "info_list", typeHandler = JacksonTypeHandler.class)
    private List<String> infoList;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 逻辑删除0正常,1删除
     */
    @TableField(value = "deleted")
    @TableLogic
    @Builder.Default()
    private Integer deleted = 0;


}
