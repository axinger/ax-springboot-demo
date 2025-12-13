package com.github.axinger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true) // set 返回当前对象
public class Person {

    @Builder.Default // @Builder 才能使用,没有默认值会报错, new 会得到默认值
    private String name = "1";

    @lombok.NonNull // lombok set不能为空,但不set,不能控制,需要默认值
    @Builder.Default
    private String address = "";

    @With
    private Integer age;


    @Builder.Default
    private List<String> like = new ArrayList<>();

    /**
     * None来禁用getter 和 setter 的生成。
     */
    @Setter(value = AccessLevel.NONE)
    private String testNone;

    @Setter(value = AccessLevel.PRIVATE)
    private String testPrivate;

    /**
     * 生成的getter和setter不会以bean标准 get，is 或 set 为前缀，会和属性名一样,没有get,set前缀
     * 但jackjson无法正常解析,需要注意,使用@JsonProperty
     */
    @Accessors(fluent = true)
    @JsonProperty(value = "aFirst")
    private String aFirst;

    @JsonProperty(value = "bFirst")
    private String bFirst;

    @Accessors(fluent = true)
    private String cFirst;

    /**
     * 可以使用 += 添加前缀，或者使用 -= 删除前缀
     */
    @Accessors(prefix = {"+=my"})
    private String testPre;

    @Accessors(prefix = "test") // getter和setter方法会忽视属性名的指定前缀（遵守驼峰命名）
    @Getter
    private String testPre2;

    @SneakyThrows
    public static void main(String[] args) {
        Person person = new Person();
        person.getBFirst();
        person.aFirst = "a";
        System.out.println("person = " + person);


        person.getPre2();

        person.setName("")
                .setLike(new ArrayList<>());


        String jsonStr = """
                {
                    "name": "jim",
                    "aFirst": "a",
                    "bFirst": "b"
                }
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Person person1 = objectMapper.readValue(jsonStr, Person.class);
        System.out.println("person1 = " + person1);
    }

    void test() {

    }

}
