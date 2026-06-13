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
 * 通用工具包
 *
 * <p>该包提供 Spring Web 应用程序中常用的工具类和辅助方法，包括：</p>
 * <ul>
 *   <li>HTTP 请求处理工具</li>
 *   <li>HTTP 响应构建工具</li>
 *   <li>参数验证和绑定工具</li>
 *   <li>常用数据格式转换</li>
 * </ul>
 *
 * <h2>主要工具类：</h2>
 * <ul>
 *   <li>{@link com.axing.common.util.utils.RequestUtil} - HTTP 请求相关工具方法</li>
 *   <li>{@link com.axing.common.util.utils.ResponseUtil} - HTTP 响应构建和封装工具</li>
 * </ul>
 *
 * <h2>功能特性：</h2>
 *
 * <h3>RequestUtil 主要功能：</h3>
 * <ul>
 *   <li>获取当前 HTTP 请求对象</li>
 *   <li>获取客户端 IP 地址</li>
 *   <li>读取请求头信息</li>
 *   <li>获取请求参数</li>
 *   <li>获取 Session 属性</li>
 * </ul>
 *
 * <h3>ResponseUtil 主要功能：</h3>
 * <ul>
 *   <li>统一响应格式封装</li>
 *   <li>成功响应快捷方法</li>
 *   <li>错误响应快捷方法</li>
 *   <li>分页数据响应</li>
 *   <li>文件下载响应</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>{@code
 * // 获取客户端 IP
 * String clientIp = RequestUtil.getClientIp();
 *
 * // 构建成功响应
 * return ResponseUtil.success("操作成功", data);
 *
 * // 构建错误响应
 * return ResponseUtil.error(400, "参数错误");
 *
 * // 构建分页响应
 * PageResult<User> pageResult = userService.getUsers(page, size);
 * return ResponseUtil.page(pageResult);
 * }</pre>
 *
 * <h2>响应格式：</h2>
 * <pre>{@code
 * // 标准成功响应格式
 * {
 *     "code": 200,
 *     "message": "操作成功",
 *     "data": { ... },
 *     "timestamp": 1704067200000
 * }
 *
 * // 标准错误响应格式
 * {
 *     "code": 400,
 *     "message": "参数错误",
 *     "data": null,
 *     "timestamp": 1704067200000
 * }
 * }</pre>
 *
 * <p>该工具包提供了统一的 API 响应格式，便于前后端协作开发。</p>
 *
 * @author Axinger Team
 * @version 1.0.0
 */
package com.axing.common.util.utils;