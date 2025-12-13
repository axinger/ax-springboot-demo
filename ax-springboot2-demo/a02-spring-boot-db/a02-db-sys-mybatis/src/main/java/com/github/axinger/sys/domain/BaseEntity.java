package com.github.axinger.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseEntity implements Serializable {

    //    @TableId(type = IdType.AUTO)
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * INSERT代表只在插入时填充
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * INSERT_UPDATE 首次插入、其次更新时填充(或修改)
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除,默认 0未删除,1删除,boolean 需要自定义
     * 不要定义为 delete,与数据库冲突
     */
    @TableLogic(value = "0", delval = "1")
    @Builder.Default()
    private int deleted = 0;

    /**
     * 乐观锁
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();
}
