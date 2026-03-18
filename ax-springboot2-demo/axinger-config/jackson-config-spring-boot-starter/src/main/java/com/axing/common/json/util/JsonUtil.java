package com.axing.common.json.util;

import com.axing.common.json.model.DateTimeFormatterUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JsonUtil extends ObjectMapper {


    // 创建ObjectMapper对象
    public static final JsonUtil mapper;

    static {
        mapper = new JsonUtil(JsonInclude.Include.ALWAYS);
    }

    public JsonUtil(JsonInclude.Include incl) {
        ObjectMapper mapper = this;
        // configure方法，配置一些需要的参数
        // 转换为格式化的json,显示出来的格式美化
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 序列化的时候序列对象的那些属性
        // JsonInclude.Include.ALWAYS  --所有属性
        // JsonInclude.Include.NON_DEFAULT --属性为默认值不序列化
        // JsonInclude.Include.NON_EMPTY --属性为 空（“”）或者为NULL都不序列化
        // JsonInclude.Include.NON_NULL --属性为NULL 不序列化
        mapper.setSerializationInclusion(incl);

        // 返序列化时，遇到未知属性会不会报错
        // true:遇到没有的属性就报错   false：遇到没有的属性不会管，也不会报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 如果是空对象的时候，不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 忽略 transient 修饰的属性
//        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

        // 修改序列化后日期格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 处理不同的时区偏移格式
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setLocale(Locale.CHINA);
        mapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 配置按字母顺序排序,无法指定 JsonAnySetter @JsonPropertyOrder
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

//        mapper.registerModule(new JavaTimeModule());
//        mapper.registerModule(new Java8CommonTimeModule());

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 替换默认的序列化器/反序列化器
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.dateFormat)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.dateFormat)));
        mapper.registerModule(javaTimeModule);
    }

    /**
     * 对象转json,返回string或者null
     */
    @SneakyThrows
    public static String toJsonStr(Object value) {
        return mapper.writeValueAsString(value);
    }

    @SneakyThrows
    public static String toJson(Object value) {
        return mapper.writeValueAsString(value);
    }

    /**
     * string转对象,返回对象或者null
     */
    @SneakyThrows
    public static <T> T toBean(String content, Class<T> valueType) {
        return mapper.readValue(content, valueType);
    }

    /**
     * string转对象,返回对象或者null
     */
    @SneakyThrows
    public static <T> T toBean(String str, TypeReference<T> valueTypeRef) {
        return mapper.readValue(str, valueTypeRef);
    }

    @SneakyThrows
    public static <T> List<T> toBeanList(String str, Class<T> valueType) {
//        TypeReference<List<T>> typeReference = new TypeReference<>() {
//        };
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, valueType);
        return mapper.readValue(str, listType);
    }

}
