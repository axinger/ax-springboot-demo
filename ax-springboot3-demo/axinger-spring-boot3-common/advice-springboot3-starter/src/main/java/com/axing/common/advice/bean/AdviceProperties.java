package com.axing.common.advice.bean;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "axing.advice")
public class AdviceProperties {
    /**
     * 错误是否,打印
     */
    private boolean printStackTrace = false;

    /**
     * 统一返回过滤包
     */
    private List<String> filterPackage = new ArrayList<>();

    /**
     * 统一返回过滤类
     */
    private List<String> filterClass = new ArrayList<>();


    /**
     * 默认值 拦截
     *
     * @return
     */
    public Set<String> filterPackageAllSet() {
        return new HashSet<>() {{
            add("springfox.documentation");
            add("org.springframework");
            add("org.springdoc");
            if (ObjUtil.isNotEmpty(filterPackage)) {
                addAll(filterPackage);
            }
        }};
        // return new ArrayList<>() {{
        //     add("springfox.documentation");
        //     add("org.springframework");
        //     add("org.springdoc");
        //     if (ObjUtil.isNotEmpty(filterPackage)) {
        //         addAll(filterPackage);
        //     }
        // }};
    }

}
