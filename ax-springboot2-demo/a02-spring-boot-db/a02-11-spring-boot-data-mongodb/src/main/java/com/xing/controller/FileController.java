package com.xing.controller;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsUpload;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;


/**
 * 文件上传下载
 * MongoTemplate
 **/
@Controller
@RequestMapping("/file")
@Tag(name = "FileController", description = "文件")
public class FileController {
    // 获得SpringBoot提供的mongodb的GridFS对象
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Resource
    private MongoDatabaseFactory mongoDbFactory;

    /**
     * 文件上传
     */
    @SneakyThrows
    @ResponseBody
    @Operation(summary = "上传文件")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadByMinio(@RequestParam(value = "file") MultipartFile file) {


        String fileName = file.getOriginalFilename();
        // 获得文件输入流
        InputStream ins = file.getInputStream();
        // 获得文件类型
        String contentType = file.getContentType();
        // 存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询
        DBObject metadata = new BasicDBObject("userId", "1001");
        // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
        // ObjectId objectId = gridFsTemplate.store(ins, fileName, contentType, metadata);

        GridFsUpload<ObjectId> build = GridFsUpload.fromStream(file.getInputStream())
                .filename(fileName)
                .contentType(contentType)
                .metadata(new Document(Map.of("name", "jim", "age", 10, "MD5", "1111")))
                .build();

        ObjectId objectId = gridFsTemplate.store(build);

        // 将文件信息保存到关系型数据库中进行维护
        return "id===>" + objectId.toHexString(); // id 16进制字符串, 一定拿到
    }

    /**
     * 下载
     *
     * @param fileId 文件id
     */
    @RequestMapping(value = "/down")
    public void downloadFile(@RequestParam(name = "file_id") String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("Finding by ID: " + fileId);
        // fileId 16 进制字符串
        GridFSFile fsFile = gridFsTemplate.findOne(new Query(Criteria
                .where("_id").is(new ObjectId(fileId))));
        // 打开下载流对象

        // 获取数据库 demodb
        MongoDatabase db = mongoDbFactory.getMongoDatabase("demo");
        System.out.println("db===> " + db.getName());
        GridFSBucket gridFSBucket = GridFSBuckets.create(db);
        assert fsFile != null;
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        // 创建GridFsResource用于获取流对象
        GridFsResource resource = new GridFsResource(fsFile, gridFSDownloadStream);

        String fileName = fsFile.getFilename().replace(",", "");
        // 处理中文文件名乱码
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 通知浏览器进行文件下载
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * 删除文件
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public String deleteFile(@RequestParam(name = "file_id") String fileId) {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        // 查询单个文件
        GridFSFile gfsfile = gridFsTemplate.findOne(query);

        gridFsTemplate.delete(query);
        return "删除==>" + gfsfile.getFilename();
    }

}
