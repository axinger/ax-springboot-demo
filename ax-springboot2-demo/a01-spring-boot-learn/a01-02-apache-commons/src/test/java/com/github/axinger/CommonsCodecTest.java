package com.github.axinger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.codec.net.URLCodec;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class CommonsCodecTest {

    // ==================== Base64 ====================

    @Test
    void testBase64() {
        String original = "Hello Commons Codec! 你好";
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);

        // Base64 编码
        String encoded = Base64.encodeBase64String(bytes);
        System.out.println("Base64 编码: " + encoded);

        // Base64 解码
        byte[] decodedBytes = Base64.decodeBase64(encoded);
        String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println("Base64 解码: " + decoded);

        // URL 安全的 Base64（+ 和 / 替换为 - 和 _）
        String urlSafe = Base64.encodeBase64URLSafeString(bytes);
        System.out.println("URL Safe Base64: " + urlSafe);
    }

    @Test
    void testBase64_chunked() {
        String original = "这是一段很长的文本，用于测试 Base64 的 chunked 编码格式。".repeat(10);
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);

        // chunked 格式（每76字符换行）
        String chunked = Base64.encodeBase64String(bytes);
        System.out.println("Chunked Base64:\n" + chunked);
    }

    // ==================== Hex ====================

    @Test
    void testHex() throws DecoderException {
        String original = "Hello";
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);

        // 编码为 Hex 字符串
        String hexString = Hex.encodeHexString(bytes);
        System.out.println("Hex 编码: " + hexString); // 48656c6c6f

        // Hex 解码
        byte[] decoded = Hex.decodeHex(hexString);
        System.out.println("Hex 解码: " + new String(decoded, StandardCharsets.UTF_8));

        // 直接编码字节数组
        char[] hexChars = Hex.encodeHex(bytes);
        System.out.println("Hex 字符数组: " + new String(hexChars));
    }

    // ==================== DigestUtils (MD5/SHA) ====================

    @Test
    void testMD5() {
        String input = "password123";

        // MD5 哈希
        String md5Hex = DigestUtils.md5Hex(input);
        System.out.println("MD5: " + md5Hex);

        // MD5 字节数组
        byte[] md5Bytes = DigestUtils.md5(input);
        System.out.println("MD5 bytes length: " + md5Bytes.length); // 16
    }

    @Test
    void testSHA() {
        String input = "password123";

        // SHA-1
        String sha1Hex = DigestUtils.sha1Hex(input);
        System.out.println("SHA-1: " + sha1Hex);

        // SHA-256
        String sha256Hex = DigestUtils.sha256Hex(input);
        System.out.println("SHA-256: " + sha256Hex);

        // SHA-512
        String sha512Hex = DigestUtils.sha512Hex(input);
        System.out.println("SHA-512: " + sha512Hex);
    }

    @Test
    void testDigestUtils_stream() throws IOException {
        String input = "测试流式摘要计算";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        // 对流计算 MD5
        String md5 = DigestUtils.md5Hex(stream);
        System.out.println("流式 MD5: " + md5);
    }

    // ==================== HMAC ====================

    @Test
    void testHmac() {
        String data = "message";
        String key = "secret-key";

        // HMAC-MD5
        String hmacMd5 = new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(data);
        System.out.println("HMAC-MD5: " + hmacMd5);

        // HMAC-SHA256
        String hmacSha256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(data);
        System.out.println("HMAC-SHA256: " + hmacSha256);

        // HMAC-SHA512
        String hmacSha512 = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(data);
        System.out.println("HMAC-SHA512: " + hmacSha512);
    }

    // ==================== URLCodec ====================

    @Test
    void testURLCodec() throws Exception {
        URLCodec codec = new URLCodec();
        String original = "Hello World! 你好 @#$";

        // URL 编码
        String encoded = codec.encode(original, StandardCharsets.UTF_8.name());
        System.out.println("URL 编码: " + encoded);

        // URL 解码
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8.name());
        System.out.println("URL 解码: " + decoded);
    }
}
