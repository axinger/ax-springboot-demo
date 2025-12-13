package com.github.axinger.gateway;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

class A0706MallGatewayApplicationTest {

    @Test
    public void test1() {
        String username = "jim";

        LocalDateTime signTime = LocalDateTime.now();

        LocalDateTime expiresAt = signTime.plusDays(7);

        // 生成 HMAC-SHA 密钥（推荐）
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 自动生成随机密钥

// 或者从固定字符串生成（需确保足够安全）
        String secretString = "my-very-secure-secret-that-is-at-least-256-bits-long";
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

        String format = key.getFormat();
        System.out.println("format = " + format);
        String string = new String(key.getEncoded());
        System.out.println("string = " + string);
        String jwt = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(DateUtil.date(signTime))
                .setExpiration(DateUtil.date(expiresAt))
                .signWith(key)//加密方式,解密密码
                .claim("name", "tom")
                .claim("sex", "男")
                .compact();

        System.out.println("jwt = " + jwt);


        System.out.println("=============解析 JWT===========");
        try {

            Claims body = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            // 以下步骤随实际情况而定，只要上一行代码执行不抛异常就证明 jwt 是有效的、合法的
            System.out.println("载荷-标准中注册的声明 id:" + body.getId());
            System.out.println("载荷-标准中注册的声明 subject:" + body.getSubject());
            System.out.println("载荷-标准中注册的声明 issueAt:" + body.getIssuedAt());
            LocalDateTime issued = LocalDateTimeUtil.of(body.getIssuedAt());
            System.out.println("issued = " + issued);

            LocalDateTime expiration = LocalDateTimeUtil.of(body.getExpiration());
            System.out.println("expiration = " + expiration);

            System.out.println("载荷-公共的声明的 id:" + body.get("id"));
            System.out.println("载荷-公共的声明的 name:" + body.get("name"));
            System.out.println("载荷-公共的声明的 sex:" + body.get("sex"));
        } catch (ExpiredJwtException e) {

            System.out.println("登陆过期,请重新登陆");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token不合法,请自重");
        } catch (SignatureException e) {
            System.out.println("签名不正确");
        }

    }
}
