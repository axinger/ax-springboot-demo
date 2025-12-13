package com.github.axinger;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.NoneJWTSigner;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JWTTest {


    private static final Long NOW = System.currentTimeMillis();
    private static final Date NOW_TIME = new Date(NOW);
    private static final Long EXPIRED = 3 * 1000L;
    private static final Date EXPIRED_TIME = new Date(NOW + EXPIRED);
    String KEY = "123";

    @Test
    public void test1() throws InterruptedException {

//        DateTime now = DateTime.now();
//        DateTime newTime = now.offsetNew(DateField.SECOND, 2);
//
//        System.out.println("newTime = " + newTime);
//
////        LocalDateTime now = LocalDateTime.now();
////
////        LocalDateTime newTime = now.plusSeconds(5);
//
//        Map<String, Object> payload = new HashMap<String, Object>();
//        payload.put(JWTPayload.ISSUER, "axing");
//        payload.put(JWTPayload.SUBJECT, "all");
//        payload.put(JWTPayload.AUDIENCE, "to");
////        payload.put(JWTPayload.JWT_ID, "to");
//
//        // 生效时间
//        payload.put(JWTPayload.NOT_BEFORE, now);
//        // jwt的过期时间，这个过期时间必须要大于签发时间
//        payload.put(JWTPayload.EXPIRES_AT, newTime);
//
//        // jwt的签发时间
//        payload.put(JWTPayload.ISSUED_AT, now);
//
////
////        	 *     <li>Token是否正确</li>
////                *     <li>{@link JWTPayload#NOT_BEFORE}：生效时间不能晚于当前时间</li>
////                *     <li>{@link JWTPayload#EXPIRES_AT}：失效时间不能早于当前时间</li>
////                *     <li>{@link JWTPayload#ISSUED_AT}： 签发时间不能晚于当前时间</li>
////                * </ul>
////                *
////                * @param leeway 容忍空间，单位：秒。当不能晚于当前时间时，向后容忍；不能早于向前容忍。
//
//
//        // 载荷
//        payload.put("username", "jim");
//        payload.put("password", "123456");
//
////        String key = "aabb";
        byte[] key = "book".getBytes(StandardCharsets.UTF_8);


        DateTime signTime = DateTime.now();
        DateTime expiresAt = signTime.offsetNew(DateField.SECOND, 2);

        String token = JWT.create()
                .setIssuedAt(signTime)
                .setExpiresAt(expiresAt)
                .setPayload("username", "jim")
                .setPayload("password", "123456")
                .setKey(key)
//                .setSigner(HS256, key)
                .sign();


//        String token = JWTUtil.createToken(payload, NoneJWTSigner.NONE);
//        String token = JWTUtil.createToken(payload, key.getBytes());
        System.out.println(token);


        TimeUnit.SECONDS.sleep(5);
        JWT jwt = JWTUtil.parseToken(token);


        System.out.println(jwt.getPayload("username"));
        System.out.println("getClaim = " + jwt.getPayload().getClaim("username"));

        /// 是否合法,或者被篡改
        boolean verifyKey = jwt.setKey(key).verify();
        System.out.println("是否合法,或者被篡改 = " + verifyKey);

        boolean verify = JWTUtil.verify(token, key);
        System.out.println("是否合法,或者被篡改2 = " + verify);

//        JWT.of(token).setKey(KEY.getBytes()).validate(0)

        DateTime now2 = DateTime.now();

        System.out.println("now2 = " + now2);

        /// 是否过期
//        boolean verifyTime = jwt.setKey(KEY.getBytes()).validate(0);


        boolean validate = JWT.of(token)
                .setKey(key)
                .validate(0);
        System.out.println("是否过期 = " + (validate ? "过期" : "没有过期"));

        // 由于只定义了签发时间，因此只检查签发时间
        JWTValidator jwtValidator = JWTValidator.of(token).validateDate();
    }

    @Test
    public void test11() throws InterruptedException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiJhbGwiLCJhdWQiOiJ0byIsInBhc3N3b3JkIjoiMTIzNDU2IiwiaXNzIjoiYXhpbmciLCJleHAiOjU1LCJpYXQiOjUzLCJ1c2VybmFtZSI6ImppbSJ9.";
        boolean verify = JWTUtil.verify(token, NoneJWTSigner.NONE);
        System.out.println("verify = " + verify);
    }

    @Test
    public void test2() {
        String key = "aabb";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGwiLCJhdWQiOiJ0byIsInBhc3N3b3JkIjoiMTIzNDU2IiwibmJmIjoxNzM2NzgxODgwLCJpc3MiOiJheGluZyIsImV4cCI6MTczNjc4MTg4MiwiaWF0IjoxNzM2NzgxODgwLCJ1c2VybmFtZSI6ImppbSJ9.Jg7j9AFoOruz5F314awxjtPhKMlKDF3Oh-klLKkTfOY";
        JWT jwt = JWTUtil.parseToken(token);


        System.out.println(jwt.getPayload("username"));
        System.out.println("jwt.getPayload().getClaim(\"username\") = " + jwt.getPayload().getClaim("username"));
        boolean verifyKey = jwt.setKey(key.getBytes()).verify();

        System.out.println("verifyKey = " + verifyKey);

        boolean verifyTime = jwt.validate(1);
        System.out.println("verifyTime = " + verifyTime);

        boolean verifyTime2 = jwt.validate(2);
        System.out.println("verifyTime2 = " + verifyTime2);

        boolean verify = JWTUtil.verify(token, key.getBytes());
        System.out.println("verify = " + verify);


    }

    @Test
    void test4() {
        Map<String, Object> map = new HashMap<String, Object>() {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                put("uid", Integer.parseInt("123"));
                put("expire_time", System.currentTimeMillis() + 10);
            }
        };

        String token = JWTUtil.createToken(map, "1234".getBytes());
        System.out.println("token = " + token);

    }

    @Test
    void test3() {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEyMywiZXhwaXJlX3RpbWUiOjE3MzY3ODIxMjc2NjB9.3eaHDUKGUcDGqcyRXEN5ax6oeBI9idRsx09Sqbrewrg";

        boolean verify = JWTUtil.verify(token, "1234".getBytes());
        System.out.println("verify = " + verify);


    }

    @Test
    void test5() {
        // 设置过期时间为当前时间后的一小时
        long expireTimeMillis = System.currentTimeMillis() + 60 * 1000;
        Date date = new Date(expireTimeMillis);

        // 使用JwtBuilder创建JWT
        String token = JWT.create()
//                .setHeaderItem("alg", "HS256") // 设置算法
                .setPayload("userId", "12345") // 添加自定义claim
                .setExpiresAt(date) // 设置过期时间
                .setKey("abc".getBytes())
                .sign(); // 签名

        System.out.println("Generated JWT: " + token);

    }

    @Test
    void test6() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIxMjM0NSIsImV4cCI6MTczNjc4MzE1NH0.hVz1ph768iox_NounZ6cEKTCE38bDpnhz7rVZPzMvNo";
        boolean verify = JWTUtil.verify(token, "abc".getBytes());
        System.out.println("verify = " + verify);

        boolean verify1 = JWT.of(token).setKey("abc".getBytes()).verify();
        System.out.println("verify1 = " + verify1);

//        JWTValidator.of(token).validateDate().validateDate()

    }

    @Test
    void test7() throws InterruptedException {


// 使用这种方式生成token
        String token = JWT.create().setPayload("sub", "blue-light").setIssuedAt(NOW_TIME).setNotBefore(EXPIRED_TIME)
                .setExpiresAt(EXPIRED_TIME).setKey(KEY.getBytes()).sign();

        System.out.println("token = " + token);

        TimeUnit.SECONDS.sleep(5);
// 使用这种方式验证token
        boolean validate = JWT.of(token).setKey(KEY.getBytes()).validate(0);
        System.out.println("validate = " + validate);

        boolean verify = JWTUtil.verify(token, KEY.getBytes());
        System.out.println("verify = " + verify);

    }

    @Test
    void test8() throws InterruptedException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJibHVlLWxpZ2h0IiwiaWF0IjoxNzM2NzgzNzIxLCJuYmYiOjE3MzY3ODM3MjQsImV4cCI6MTczNjc4MzcyNH0.giwaLVEAnarxKZ9Laus9tDe2x7WAWNYU-vyeNSGCwGs";
        boolean verify = JWTUtil.verify(token, KEY.getBytes());
        System.out.println("verify = " + verify);

        boolean validate = JWT.of(token).setKey(KEY.getBytes()).validate(0);
        System.out.println("是否过期 = " + (validate ? "过期" : "没有过期"));
    }

}
