/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ax.com.dubbo.provider.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;

@Configuration
/***
 * 1.在服务启动类上加上注解 @EnableDubbo 。作用范围：当前同级的包及其子类会被扫描识别。
 * 2.在服务启动类上加上注解 @DubboComponentScan( basePackages = “com.esint.service”) 这个指定的包下的带有@DubboService的注解类会被扫描识别。
 * 3.在yml配置文件中设置
 */
@EnableDubbo
//@EnableDiscoveryClient //默认开启
//@DubboComponentScan( basePackages = "ax.com.dubbo.provider.service")
public class DemoNacosConfig {

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}
