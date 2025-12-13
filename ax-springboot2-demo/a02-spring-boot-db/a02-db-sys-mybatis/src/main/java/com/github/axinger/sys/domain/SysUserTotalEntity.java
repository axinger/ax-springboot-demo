package com.github.axinger.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName sys_user_total  mpp不支持复合主键注解
 */
@TableName(value = "sys_user_total")
@Data
public class SysUserTotalEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
//    @TableId(value = "name",type = IdType.NONE)
    private String name;
    /**
     *
     */
//    @TableId(value = "type",type = IdType.NONE)

    private String type;
    /**
     *
     */
    @TableField(value = "num")
    private Integer num;
}
