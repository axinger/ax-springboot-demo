package com.axing.common.minio.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;

public class FilePathUtil {

    /**
     * 按日期分类,随机 文件名
     *
     * @return 文件路径
     */
    public static String getPath() {
        String date = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMdd");
        String uuid = IdUtil.fastSimpleUUID();
        return StrUtil.format("{}/{}", date, uuid);
    }

    /**
     * 按日期分类,随机 文件名
     *
     * @return 文件路径
     */
    public static String getFileName(String fileName) {
        String suffix = FileUtil.getSuffix(fileName);
        String date = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMdd");
        String uuid = IdUtil.fastSimpleUUID();
        return StrUtil.format("{}/{}.{}", date, uuid, suffix);
    }


}
