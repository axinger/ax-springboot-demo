package com.github.axinger;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Optional;

public class FileTests {

    @Test
    void test() {
        String fileUrl = "a/b/c.jpg";
        File file = new File(fileUrl);
        String name = FileUtil.getName(file);
        System.out.println("name = " + name);
        String suffix = FileUtil.getSuffix(file);
        System.out.println("suffix = " + suffix);
        String prefix = FileUtil.getPrefix(file);
        System.out.println("prefix = " + prefix);

        String extName = FileUtil.extName(file);
        System.out.println("extName = " + extName);
        String mainName = FileUtil.mainName(file);
        System.out.println("mainName = " + mainName);

        System.out.println("FileUtil.getAbsolutePath(file) = " + FileUtil.getAbsolutePath(file));
        System.out.println("FileUtil.getLastPathEle(file.toPath()) = " + FileUtil.getLastPathEle(file.toPath()));


        String replace = StrUtil.replace(fileUrl, "." + FileUtil.extName(fileUrl), ".pdf");
        System.out.println("replace = " + replace);
//        System.out.println("FileUtil.listFileNames(fileUrl) = " + FileUtil.listFileNames(fileUrl));

        String tmpDirPath = FileUtil.getTmpDirPath();
        System.out.println("tmpDirPath = " + tmpDirPath);
        String userHomePath = FileUtil.getUserHomePath();
        System.out.println("userHomePath = " + userHomePath);
        File userHomeDir = FileUtil.getUserHomeDir();
        String path = userHomeDir.getPath();
        System.out.println("path = " + path);

        File touch = FileUtil.touch(userHomePath, "/a/b/c.docx");
        System.out.println("touch = " + touch.getPath());

        String path1 = FileUtil.mkdir(userHomePath + "/f/b/c").getPath();
        System.out.println("path1 = " + path1);

//        String type = FileTypeUtil.getTypeByPath("/Users/xing/Develop/work/iot-document/118-加热炉-设备.xlsx");
//        System.out.println("type = " + type);
    }


    @Test
    void test2() {
        File[] ls = FileUtil.ls("/Users/xing/Desktop/flink-ck");


        Optional<File> min1 = List.of(ls).stream()
                .filter(val -> FileUtil.isDirectory(val))
                .min((val1, val2) -> {
                    FileTime t1 = null;
                    FileTime t2 = null;
                    try {
                        t1 = Files.readAttributes(Paths.get(val1.getPath()), BasicFileAttributes.class).creationTime();
                        t2 = Files.readAttributes(Paths.get(val2.getPath()), BasicFileAttributes.class).creationTime();
                    } catch (Exception e) {
                    }
                    return CompareUtil.compare(t1, t2);
                });

        System.out.println("min = " + min1);

        File[] ls1 = FileUtil.ls(min1.get().getPath());
        Optional<File> min2 = List.of(ls1).stream()
                .filter(val -> FileUtil.isDirectory(val) && StrUtil.startWith(val.getName(), "chk-"))
                .min((val1, val2) -> {
                    FileTime t1 = null;
                    FileTime t2 = null;
                    try {
                        t1 = Files.readAttributes(Paths.get(val1.getPath()), BasicFileAttributes.class).creationTime();
                        t2 = Files.readAttributes(Paths.get(val2.getPath()), BasicFileAttributes.class).creationTime();
                    } catch (Exception e) {
                    }
                    return CompareUtil.compare(t1, t2);
                });

        System.out.println("min2 = " + min2);

    }

    @Test
    void test3() {
        File file = FileUtil.file("/opt/test.txt");

        //test.txt
        String name1 = FileUtil.getName(file);
        System.out.println("name1 = " + name1);


        // test
        String name = FileNameUtil.mainName(file);
        System.out.println("name = " + name);


        // txt
        String extName = FileNameUtil.extName(file);
        String extName1 = FileUtil.extName(file);
        System.out.println("extName = " + extName);
        System.out.println("extName1 = " + extName1);

        // test
        String prefix = FileNameUtil.getPrefix(file);
        String prefix1 = FileUtil.getPrefix(file);
        System.out.println("prefix = " + prefix);
        System.out.println("prefix1 = " + prefix1);

        // txt
        String suffix = FileNameUtil.getSuffix(file);
        String suffix2 = FileUtil.getSuffix(file);
        System.out.println("suffix = " + suffix);
        System.out.println("suffix2 = " + suffix2);

    }


    /**
     * 根据文件流的头部信息获得文件类型
     *
     * <pre>
     *     1、无法识别类型默认按照扩展名识别
     *     2、xls、doc、msi头信息无法区分，按照扩展名区分
     *     3、zip可能为jar、war头信息无法区分，按照扩展名区分
     * </pre>
     */
    // 文件类型
    @Test
    void test4() {
        File file = FileUtil.file("C:\\Users\\xing\\Desktop\\test.png");
        String type = FileTypeUtil.getType(file);
        //输出 jpg则说明确实为jpg文件
        Console.log(type);
        System.out.println("type = " + type);
    }

    // 文件类型
    @Test
    void test5() {
        File file = FileUtil.file("C:\\Users\\xing\\Desktop\\test.xlsx");
        String type = FileTypeUtil.getType(file);
        System.out.println("type = " + type);
    }

    @Test
    void test6() {

        String decode = URLUtil.decode("/Users/xing//Desktop/test.txt");
        System.out.println("decode = " + decode);

        String replace = StrUtil.replace(decode, "//", "/");
        System.out.println("replace = " + replace);

        String s = StrUtil.removePrefix(replace, "/");
        System.out.println("s = " + s);


    }

    @Test
    void test7() {
        Path path = Path.of("a", "\\/c////d/f/");
        String string = path.toString();
        System.out.println("string = " + string);

        Path absNormal = PathUtil.toAbsNormal(path);
        System.out.println("absNormal = " + absNormal.toString());


//        Path tempFile = PathUtil.createTempFile("A", "B.zip", path);
//        System.out.println("tempFile = " + tempFile.toString());
    }
}
