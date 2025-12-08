package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

@Data
@JsonAppend(attrs = {
        @JsonAppend.Attr(value = "version"),
        @JsonAppend.Attr(value = "traceId"),
        @JsonAppend.Attr(value = "timestamp")
})

//@JsonAppend(
//        props = {
//                @JsonAppend.Prop(
//                        name = "timestamp",
//                        value = ToStringSerializer.class,
//                        // 使用静态值
//                        valueSerializer = ToStringSerializer.class,
//                        // 或者使用表达式
//                        value = "now()"
//                ),
//                @JsonAppend.Prop(
//                        name = "processed",
//                        value = BooleanSerializer.class,
//                        namespace = MyNamespace.class
//                )
//        }
//)
public class JacksonUser {
    @JsonPropertyDescription("用户全局唯一标识，由系统分配")
    private String name;
    private Integer age;
    @JsonPropertyDescription("用户电子邮箱，用于接收通知")
    public String email;
    private String password;
}
