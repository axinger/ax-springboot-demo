package com.axing.common.util.jwt;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author xing
 */
@Slf4j
public class JwtHelper {


    private static final String signKey = "abc123";
    private static final long tokenExpires = 24 * 60 * 60;//一天
    private static final String AuthorizationPrefix = "Bearer ";
    //    private static final String userId = "userId";
    private static final String usernameKey = "userName ";
    private static final String expireTime = "expireTime ";

    /**
     * 签发JWT
     */
    public static String createToken(String id, String userName) {

        DateTime signTime = DateTime.now();
        Date expiryDate = new Date(signTime.getTime() + tokenExpires); // 1 天有效期

        String jwt = Jwts.builder()
                .setIssuer("axing")
                .setSubject(userName)
                .setIssuedAt(signTime)
                .setExpiration(expiryDate)
                .claim(usernameKey, userName)
//                .signWith(SignatureAlgorithm.HS512, secret)
                .signWith(SignatureAlgorithm.HS256, signKey.getBytes())//加密方式,解密密码
                .compact();
        String authorization = AuthorizationPrefix + jwt;
        log.info("生成 Authorization: {}", authorization);
        return authorization;
    }

    ///  验证是否过期
    public static String validateToken(String bearer) {
        String token = StrUtil.removePrefix(bearer, AuthorizationPrefix);

        try {
            Jws<Claims> result = Jwts.parser().setSigningKey(signKey.getBytes()).parseClaimsJws(token);
            // 以下步骤随实际情况而定，只要上一行代码执行不抛异常就证明 jwt 是有效的、合法的
            Claims body = result.getBody();
//            System.out.println("载荷-标准中注册的声明 id:" + body.getId());
//            System.out.println("载荷-标准中注册的声明 subject:" + body.getSubject());
//            System.out.println("载荷-标准中注册的声明 issueAt:" + body.getIssuedAt());
//            LocalDateTime issued = LocalDateTimeUtil.of(body.getIssuedAt());
//            System.out.println("issued = " + issued);
//
//            LocalDateTime expiration = LocalDateTimeUtil.of(body.getExpiration());
//            System.out.println("expiration = " + expiration);
//
//            System.out.println("载荷-公共的声明的 id:" + result.getBody().get("id"));
//            System.out.println("载荷-公共的声明的 name:" + result.getBody().get("name"));
//            System.out.println("载荷-公共的声明的 sex:" + result.getBody().get("sex"));
            String subject = body.getSubject();
            log.error("token验证={}", subject);
            return subject;
        } catch (Exception e) {
            log.error("token验证错误={}", ExceptionUtil.getSimpleMessage(e));
            return null;
        }
//        } catch (ExpiredJwtException e) {
//            System.out.println("登陆过期,请重新登陆");
//        } catch (UnsupportedJwtException e) {
//            System.out.println("Token不合法,请自重");
//        } catch (SignatureException e) {
//            System.out.println("签名不正确");
//        }

    }

    // 根据token字符串得到用户id
//    public static String getUserId(String token) {
//        if (Objects.isNull(token)) {
//            return null;
//        }
//        final JWT jwt = JWTUtil.parseToken(token);
//        return Optional.ofNullable(jwt.getPayload(userId)).map(String::valueOf).orElse(null);
//    }

//    public static String getUserName(String bearer) {
//        String token = StrUtil.removePrefix(bearer, AuthorizationPrefix);
//        if (Objects.isNull(token)) {
//            return null;
//        }
//        final JWT jwt = JWTUtil.parseToken(token);
//        return Optional.ofNullable(jwt.getPayload(username)).map(String::valueOf).orElse(null);
//    }

}


//@Slf4j
//public class JwtHelper {
//
//
//    private static final String tokenSignKey = "abc123";
//    private static final long tokenExpires = 24 * 60 * 60;//一天
//    private static final String AuthorizationPrefix = "Bearer ";
//    private static final String userId = "userId";
//    private static final String username = "userName ";
//    private static final String expireTime = "expireTime ";
//
//    /**
//     * 签发JWT
//     */
//    public static String createToken(String id, String userName) {
//
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + tokenExpires); // 1 天有效期
//
//        Map<String, Object> map = new HashMap<>();
//        map.put(userId, id);
//        map.put(username, userName);
/// /        map.put(expireTime, expiryDate);
//
//        map.put(JWTPayload.ISSUER, "axing");
//        map.put(JWTPayload.SUBJECT, "all");
//        map.put(JWTPayload.AUDIENCE, "to");
/// /        payload.put(JWTPayload.JWT_ID, "to");
//        // jwt的签发时间
//        map.put(JWTPayload.ISSUED_AT, now);
//        // jwt的过期时间，这个过期时间必须要大于签发时间
//        map.put(JWTPayload.EXPIRES_AT, expiryDate);
//        // 生效时间
//        map.put(JWTPayload.NOT_BEFORE, now);
//
//
//        DateTime signTime = DateTime.now();
//        DateTime expiresAt = signTime.offsetNew(DateField.SECOND, 2);
//
//        String jwt = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(signTime)
//                .setExpiration(expiresAt)
/// /                .signWith(SignatureAlgorithm.HS512, secret)
//                .signWith(SignatureAlgorithm.HS256, "000000".getBytes())//加密方式,解密密码
//                .compact();
//
//
//        final String token = JWTUtil.createToken(map, tokenSignKey.getBytes());
//        String authorization = AuthorizationPrefix + token;
//        log.info("生成 Authorization: {}", authorization);
//        return authorization;
//    }
//
//    ///  验证是否过期
//    public static boolean validateToken(String bearer) {
//        String token = StrUtil.removePrefix(bearer, AuthorizationPrefix);
//        try {
//            ///  验证是否过期
//            boolean validate = JWT.of(token).setKey(tokenSignKey.getBytes()).validate(0);
//            log.info("token 验证失效: {}", validate);
//            return validate;
/// /            return JWTUtil.verify(token, tokenSignKey.getBytes());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // 根据token字符串得到用户id
//    public static String getUserId(String token) {
//        if (Objects.isNull(token)) {
//            return null;
//        }
//        final JWT jwt = JWTUtil.parseToken(token);
//        return Optional.ofNullable(jwt.getPayload(userId)).map(String::valueOf).orElse(null);
//    }
//
//    public static String getUserName(String bearer) {
//        String token = StrUtil.removePrefix(bearer, AuthorizationPrefix);
//        if (Objects.isNull(token)) {
//            return null;
//        }
//        final JWT jwt = JWTUtil.parseToken(token);
//        return Optional.ofNullable(jwt.getPayload(username)).map(String::valueOf).orElse(null);
//    }
//
//}

