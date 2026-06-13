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
 * Axinger 通用工具模块
 *
 * <p>该模块提供了 Spring Boot 应用程序中常用的工具类和实用功能，包括：</p>
 *
 * <ul>
 *   <li><b>工厂类</b> - YAML 和属性源工厂，用于配置文件处理</li>
 *   <li><b>JWT 工具</b> - JSON Web Token 的生成、解析和验证功能</li>
 *   <li><b>请求响应工具</b> - HTTP 请求和响应处理的实用方法</li>
 * </ul>
 *
 * <h2>主要组件：</h2>
 * <ul>
 *   <li>{@link com.axing.common.util.factory.YamlPropertySourceFactory} - YAML 属性源工厂</li>
 *   <li>{@link com.axing.common.util.factory.YamlAndPropertySourceFactory} - YAML 和属性源组合工厂</li>
 *   <li>{@link com.axing.common.util.jwt.JwtHelper} - JWT 操作助手类</li>
 *   <li>{@link com.axing.common.util.utils.RequestUtil} - HTTP 请求工具类</li>
 *   <li>{@link com.axing.common.util.utils.ResponseUtil} - HTTP 响应工具类</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>{@code
 * // JWT 使用示例
 * String token = JwtHelper.generateToken(claims);
 * Claims claims = JwtHelper.parseToken(token);
 *
 * // 响应工具使用示例
 * return ResponseUtil.success(data);
 * return ResponseUtil.error("操作失败");
 * }</pre>
 *
 * <h2>依赖要求：</h2>
 * <ul>
 *   <li>Spring Boot 2.7.x 或 3.x</li>
 *   <li>Java 8 或更高版本</li>
 *   <li>JJWT 库用于 JWT 功能</li>
 *   <li>SnakeYAML 用于 YAML 处理</li>
 * </ul>
 *
 * <p>该模块作为 Spring Boot 启动器发布，可以通过 Maven 依赖轻松集成到其他项目中。</p>
 *
 * @author Axinger Team
 * @version 1.0.0
 * @since 2024-01-01
 * @see <a href="https://github.com/axinger/ax-springboot-demo">Axinger Spring Boot Demo</a>
 */
package com.axing.common.util;