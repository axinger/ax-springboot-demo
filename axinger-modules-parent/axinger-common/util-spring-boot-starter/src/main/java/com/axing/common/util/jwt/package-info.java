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
 * JWT 工具包
 *
 * <p>该包提供 JSON Web Token 相关的完整功能实现，包括：</p>
 * <ul>
 *   <li>JWT 令牌的生成和签发</li>
 *   <li>JWT 令牌的解析和验证</li>
 *   <li>Claims 数据的提取和操作</li>
 *   <li>令牌过期时间管理</li>
 *   <li>签名算法支持（HS256, HS384, HS512, RS256 等）</li>
 * </ul>
 *
 * <h2>主要功能：</h2>
 * <ul>
 *   <li>生成访问令牌和刷新令牌</li>
 *   <li>验证令牌的有效性和完整性</li>
 *   <li>从令牌中提取用户信息</li>
 *   <li>令牌黑名单管理</li>
 *   <li>自定义 Claims 支持</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>{@code
 * // 生成令牌
 * Map<String, Object> claims = new HashMap<>();
 * claims.put("userId", 123);
 * claims.put("username", "john_doe");
 * String token = JwtHelper.generateToken(claims);
 *
 * // 解析令牌
 * Claims parsedClaims = JwtHelper.parseToken(token);
 * String username = parsedClaims.get("username", String.class);
 *
 * // 验证令牌
 * boolean isValid = JwtHelper.validateToken(token);
 * }</pre>
 *
 * <h2>配置要求：</h2>
 * <ul>
 *   <li>JWT 签名密钥（通过配置文件设置）</li>
 *   <li>令牌过期时间（默认 24 小时）</li>
 *   <li>刷新令牌过期时间（默认 7 天）</li>
 * </ul>
 *
 * <p>该模块支持多种签名算法，可根据安全需求选择合适的算法。</p>
 *
 * @author Axinger Team
 * @version 1.0.0
 * @see <a href="https://github.com/jwtk/jjwt">JJWT Library</a>
 */
package com.axing.common.util.jwt;