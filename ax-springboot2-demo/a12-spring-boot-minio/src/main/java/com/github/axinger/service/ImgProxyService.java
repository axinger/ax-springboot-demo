package com.github.axinger.service;

import com.github.axinger.model.WatermarkDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class ImgProxyService {

    @Value("${img-proxy.key}")
    private String key;

    @Value("${img-proxy.salt}")
    private String salt;

    @Value("${img-proxy.url}")
    private String proxyUrl;

    public static String signPath(byte[] key, byte[] salt, String path) throws Exception {
        final String HMACSHA256 = "HmacSHA256";

        Mac sha256HMAC = Mac.getInstance(HMACSHA256);
        SecretKeySpec secretKey = new SecretKeySpec(key, HMACSHA256);
        sha256HMAC.init(secretKey);
        sha256HMAC.update(salt);

        String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(sha256HMAC.doFinal(path.getBytes()));

        return "/" + hash + path;
    }

    private static byte[] hexStringToByteArray(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Even-length string required");
        }
        byte[] res = new byte[hex.length() / 2];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((Character.digit(hex.charAt(i * 2), 16) << 4) | (Character.digit(hex.charAt(i * 2 + 1), 16)));
        }
        return res;
    }

    /**
     * 根据路径和规则过去签名地址
     *
     * @param path              路径
     * @param processingOptions 处理规则
     */
    public String getImgProxyUrl(String minioBucketName, String path, String processingOptions) {
        byte[] readKey = hexStringToByteArray(key);
        byte[] readSalt = hexStringToByteArray(salt);

        String separator = "/";
        // 图像的原始 URL s3://tuine/tmp/test.jpg
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String base64Url = cn.hutool.core.codec.Base64.encode("s3://" + minioBucketName + separator + path);
        // 构建签名字符串
        String newPath = (StringUtils.isBlank(processingOptions) ? "" : separator + processingOptions) + separator + base64Url;

        // 最终访问路径开始带/
        String pathWithHash;
        try {
            pathWithHash = signPath(readKey, readSalt, newPath);
        } catch (Exception e) {
            log.error("图片转换签名失败。", e);
            throw new RuntimeException("处理图片失败");
        }

        // 确保 proxyUrl 和 pathWithHash 之间没有多余的斜杠
        String finalProxyUrl = proxyUrl.endsWith("/") ? proxyUrl.substring(0, proxyUrl.length() - 1) : proxyUrl;
        return finalProxyUrl + pathWithHash;
    }

    public String buildWatermarkParam(WatermarkDTO wm) {
        String text = wm.getText();
        if (text == null || text.isEmpty()) return null;

        int size = Math.max(10, Math.min(100, wm.getSize()));
        int opacity = Math.max(0, Math.min(100, wm.getOpacity()));
        String gravity = wm.getGravity();

        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        return String.format(
                "watermark:text:%s;size:%d;opacity:%d;gravity:%s",
                encodedText, size, opacity, gravity
        );
    }
}
