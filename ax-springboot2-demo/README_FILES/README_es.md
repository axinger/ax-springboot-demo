# elasticsearch

```text
存储在内存中  nosql类型产品
```

## 倒排索引

```text
docker 安装kibana 需要版本一致
```

```text
索引一个文档 (保存数据)

在 customer(数据库 -> 索引 ) 索引 下的 external(表 -> 类型) 类型 下保存1号(唯一)数据为

GET 查看索引 http://localhost:9200/_cat/indices

PUT customer/external/1 

POST 不指定id,会自增,带id更新,不带id新增,PUT请求不允许

GET 查询 http://localhost:9200/customer/external/1

PUT http://localhost:9200/customer/external/1?if_seq_no=1&&if_primary_term=1

POST http://localhost:9200/customer/external/1/_update 
      body      {
            "doc":{
                    json数据
                }
            }
 带_update:  对比原来数据,如果一样,不更新版本号
 不带 _update  不检查原数据,会更新版本号
   
 删除 DELETE http://localhost:9200/customer/external/1
 能删除索引,但是不是删除类型
 
bulk批量api

```

```text
{
    "_index": "customer", 索引(库)
    "_type": "external",   类型(表)
    "_id": "1", id 主键
    "_version": 1, 版本号
    "_seq_no": 0, 并发乐观锁版本号,自增
    "_primary_term": 1, 同上,主分片重新分配,如重启就会变换,集群使用
    "found": true, 找到数据
    "_source": {   内容
        "name": "jim",
        "age": 1
    }
}

```

## 排序

```text
QueryDSL

排序简写
```

## 聚合查询 aggs

## 映射 字段类型

```text
GET /bank/_mapping

"city" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        
        文本有 .keyword 精确查询属性
```

## 分词

```text
中文需要安装插件
```
