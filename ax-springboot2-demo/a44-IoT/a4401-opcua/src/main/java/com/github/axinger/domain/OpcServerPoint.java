package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * kepwaere服务配置
 *
 * @author xing
 * @TableName opc_server_point
 */
@TableName(value = "opc_server_point")
@Data
public class OpcServerPoint implements Serializable {
    @TableField(exist = false)
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private String id;
    /**
     * url和端口号
     */
    private String url;
    /**
     * 服务名称描述
     */
    private String name;
    /**
     * 服务用户名
     */
    private String username;
    /**
     * 服务用户密码
     */
    private String password;
    /**
     * 状态 0 不可用, 1可用
     */
    private Integer status;
    /**
     * opc服务描述
     */
    private String description;
    /**
     * 乐观锁版本号
     */
    @TableField(exist = false)
    private Long version;
    /**
     * 创建时间
     */
    @TableField(exist = false)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(exist = false)
    private Date updateTime;
}
