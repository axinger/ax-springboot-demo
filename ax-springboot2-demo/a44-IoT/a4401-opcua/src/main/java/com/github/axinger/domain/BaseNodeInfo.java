package com.github.axinger.domain;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * opc节点信息
 *
 * @author xing
 * @TableName opc_node_info
 */
@TableName(value = "base_node_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseNodeInfo implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键id,opc节点编号
     */
//    @TableId
    private String id;
    /**
     *
     */
    private String name;
    /**
     * 节点所在的设备id
     */
    private String deviceId;

    /**
     * 每个节点对应的opc id,目前出现一个设备多个节点,在不同opc服务上
     */
    private String opcServerPointId;

    /**
     * 清洗数据规则, 0 默认不处理, 1 状态类型 2 步长最大最小类型
     */
    private Integer ruleType;
    /**
     * 节点描述
     */
    private String description;
    /**
     * 乐观锁版本号
     */
    @JSONField(serialize = false)
    private Long version;
    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    private Date createTime;
    /**
     * 更新时间
     */
    @JSONField(serialize = false)
    private Date updateTime;

    @Transient
    public String getAttribute() {

        String splitter;
        // 1. 先去含有 _ 的
        if (id.contains("_")) {
            splitter = "_";
        } else {
            // 2. 在默认常用格式
            splitter = ".";
        }

        /// 再截取 node id 对应的属性值, 最后一部分
        List<String> list = Splitter.on(splitter).omitEmptyStrings().trimResults().splitToList(id);
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }

        final String key = list.get(list.size() - 1);

        if (Optional.ofNullable(key).isEmpty()) {
            return null;
        }
        return key;
    }
}
