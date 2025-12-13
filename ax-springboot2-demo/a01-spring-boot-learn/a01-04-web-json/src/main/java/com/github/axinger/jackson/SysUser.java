package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data

/// 用于类级别，忽略指定的属性（常用于反序列化时忽略未知字段）。
@JsonIgnoreProperties(ignoreUnknown = true, value = {"password", "secretKey"})

/// 控制哪些字段会被序列化（例如非 null、非 empty）,如果为 null，则不会出现在 JSON 中
@JsonInclude(JsonInclude.Include.NON_NULL)

/// 指定命名策略，如将驼峰转为下划线。
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

/// 为类指定一个动态过滤器名称，配合 SimpleFilterProvider 实现运行时字段过滤
/// 过滤逻辑由 FilterProvider 决定，而不是注解本身。注解只是提供一个“钩子名称”。
//@JsonFilter("userFilter")


@JsonRootName("user")
@JsonPropertyOrder(alphabetic = true) // 按字母排序 ，无法指定 JsonAnySetter
//@JsonPropertyOrder({"name", "lastName", "age"}) // 指定顺序

//用途：忽略整个类 的序列化和反序列化（类级别注解）
//@JsonIgnoreType
public class SysUser {


    /// JsonManagedReference：标记“正向”引用（会被序列化）
    /// 序列化 User 时会包含 orders，但每个 Order 中的 user 字段不会被输出，避免循环。
    @JsonManagedReference
    public List<SysOrder> orders;
    /// 用途：在反序列化时 合并 JSON 数据到已有对象（而非完全覆盖），适用于部分更新（PATCH）场景
    @JsonMerge
    public Map<String, Object> settings = new HashMap<>();
    private Map<String, Object> otherProperties = new HashMap<>();
    private String id;
    /// JSON 中使用 "user_name" 或 "uname" 都能正确反序列化到 username
    @JsonAlias({"user_name", "uname", "username", "firstName"})
    private String name;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER) // 强制枚举用数字而非字符串
    private SysGender gender;
    /// 忽略某个字段，不参与序列化和反序列化
    @JsonIgnore
    private String password;
    private String secretKey;
    @JsonProperty(value = "xPoint")
    private String xPoint;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @JsonRawValue
    private String data;
    private String data2;

    //"status":{"code":"active","description":"用户活跃"}}
    private Status status;

//    @JsonSetter("full_name")
//    public void setFullNameFromJson(String name) {
//        this.fullName = name.trim();
//    }

//    @JacksonInject("dataSource")
//    private DataSource ds; // 不从 JSON 来，而是由外部注入

    // 动态属性,不解析
    @JsonAnySetter
    public void setOtherProperty(String key, Object value) {
        otherProperties.put(key, value);
    }

    /// 重载
    @JsonAnySetter
    public void setOtherProperty(Map<String, Object> otherProperties) {
        this.otherProperties = otherProperties;
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherProperties() {
        return otherProperties;
        // 返回按键字母排序的视图
//        return new TreeMap<>(otherProperties);
    }

    /// 不指定名称,就是默认的fullName
    @JsonGetter("full_name")
    public String getFullName() {
        return name + " " + lastName;
    }
}
