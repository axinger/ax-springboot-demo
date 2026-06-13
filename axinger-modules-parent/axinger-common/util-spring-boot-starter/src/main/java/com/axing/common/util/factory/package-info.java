/*
 * Copyright (c) 2024 Axinger. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 工厂类包
 *
 * <p>该包包含用于处理配置文件的工厂类，主要用于：</p>
 * <ul>
 *   <li>YAML 配置文件解析和加载</li>
 *   <li>Spring 属性源创建和配置</li>
 *   <li>多环境配置文件支持</li>
 * </ul>
 *
 * <h2>主要类：</h2>
 * <ul>
 *   <li>{@link com.axing.common.util.factory.YamlPropertySourceFactory} - 专用于 YAML 文件的属性源工厂</li>
 *   <li>{@link com.axing.common.util.factory.YamlAndPropertySourceFactory} - 支持 YAML 和普通属性文件的组合工厂</li>
 * </ul>
 *
 * <h2>使用场景：</h2>
 * <pre>{@code
 * // 在配置类中使用
 * @PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
 * public class ConfigClass {
 *     // 配置内容
 * }
 * }</pre>
 *
 * @author Axinger Team
 * @version 1.0.0
 */
package com.axing.common.util.factory;