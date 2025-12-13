package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName FileController.java
 * @description TODO
 * @createTime 2022年07月23日 21:27:00
 */

//@Tag(name = "文件控制器", description = "文件")
@RestController
@RequestMapping("/file")
public class FileController {

    @Operation(summary = "文件上传,使用RequestPart", description = "这个方式可以")
    @PostMapping(value = "/test1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload1(@RequestPart(value = "file") MultipartFile file,
                                  @RequestPart("name") String name) {
        System.out.println("file = " + file);
        System.out.println("name = " + name);
        return Result.success();
    }

    /**
     * form-data 表单格式,swagger 才会有提示,没有提示postman也可以成功,
     * 文件也是参数
     *
     * @param file
     * @param name
     * @return
     */
    @Operation(summary = "文件上传,使用RequestParam", description = "这个方式可以")
    @PostMapping(value = "/test2", name = "upload222222", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload2(@RequestParam(value = "file") MultipartFile file,
                                  @RequestParam("name") String name) {
        System.out.println("file = " + file);
        System.out.println("name = " + name);
        return Result.success();
    }


}
