package com.github.axinger.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName Person.java
 * @description TODO
 * @createTime 2022年05月28日 20:27:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dog implements Serializable {

    private String string;

    @JsonIgnore
    private String string2;


    private transient String name2;

    // transient 文件流中不可被序列化, 前2个以上字母小写才有效
    // 只能修饰属性
    @JsonIgnore
//    @JSONField(serialize = false)
    private String aTransient;

    private transient String aTransient2;

    /**
     * @JsonIgnore , 其他功能在  @JsonFormat
     * @JSONField(serialize = false) 忽略序列号效果一致
     * @JSONField 属性更多
     */
    @JsonIgnore
    private String aaTransient;

    private transient String aaTransient2;

    //    @JsonIgnore
//    @JsonFormat(format = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ssXXX")
    public Date getMyName() {
        return new Date();
    }
}
