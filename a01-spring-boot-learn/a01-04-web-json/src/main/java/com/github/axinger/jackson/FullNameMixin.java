package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.jackson.JsonMixin;

@Data
/// 为无法修改的类（如第三方库类、JDK 类等）定制 JSON 序列化/反序列化规则。
@JsonMixin(JacksonUser.class) // 1
public abstract class FullNameMixin {
    @JsonProperty("fullName")  // 2
    String name; // 3 把原有的name属性改为fullName


    @JsonIgnore ///  原有的就不会被序列化
    private String password;
}
