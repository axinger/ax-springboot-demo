# 一.基础

## 1.架构

```text
逻辑架构4层
```

## 2.mysql 四种隔离级别

```
事务的 四个特征（ACID）

事务具有四个特征：原子性（ Atomicity ）、一致性（ Consistency ）、隔离性（ Isolation ）和持续性（ Durability ）。这四个特性简称为 ACID 特性。

1 、原子性。事务是数据库的逻辑工作单位，事务中包含的各操作要么都做，要么都不做

2 、一致性。事 务执行的结果必须是使数据库从一个一致性状态变到另一个一致性状态。
因此当数据库只包含成功事务提交的结果时，就说数据库处于一致性状态。
如果数据库系统 运行中发生故障，有些事务尚未完成就被迫中断，这些未完成事务对数据库所做的修改有一部分已写入物理数据库，
这时数据库就处于一种不正确的状态，或者说是 不一致的状态。

3 、隔离性。一个事务的执行不能其它事务干扰。即一个事务内部的操作及使用的数据对其它并发事务是隔离的，并发执行的各个事务之间不能互相干扰。

4 、持续性。也称永久性，指一个事务一旦提交，它对数据库中的数据的改变就应该是永久性的。接下来的其它操作或故障不应该对其执行结果有任何影响。
```

# 二.语法

```text
mysql不存储boolean,默认为0是false,非0是true
```

## 1.建表

```text
VARCHAR(22) 必须指定长度,22个字符
```

```shell
service mysqld status
service mysqld start
systemctl restart mysqld.service
mysql -u root -p  -P 3306
wget http://download.redis.io/releases/redis-6.0.6.tar.gz -P /root
#启动服务端：
src/redis-server
#启动客户端：
src/redis-cli
# 进入 redis
auth "yourpassword"

```

```mysql
# 创建ip登录信息
CREATE TABLE t_ip_log(
                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
                         user_name VARCHAR(200) COMMENT '姓名',
                         login_time TIMESTAMP COMMENT '登录时间',
                         ip VARCHAR(22) COMMENT 'ip地址',
                         login_state TINYINT  COMMENT '登录状态 0 1 2',
                         user_type TINYINT  COMMENT '用户类型 0 1 2',
                         user_info_id BIGINT COMMENT '关联User表id'
) COMMENT='ip登录信息';


# 创建用户信息
CREATE TABLE t_user_info(
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
                            user_name VARCHAR(200) COMMENT '姓名',
                            password VARCHAR(200) COMMENT '密码MD5加密的',
                            user_type TINYINT  COMMENT '用户类型 0 1 2'
)COMMENT='用户信息';



# 创建用户角色
CREATE TABLE t_user_role(
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键id',
                            role_name VARCHAR(200) COMMENT '角色说明',
                            user_id TINYINT  COMMENT '关联User表id'
)COMMENT='角色信息';

```

循环插入数据

```mysql 
CREATE PROCEDURE test_22 () #创建存储函数；
BEGIN
	DECLARE
		i INT DEFAULT 1;
	WHILE
			i < 20 DO#下面的insert语法要改成自己的表，同时构造value.id，要考虑防重；
			INSERT INTO `t_person` ( `name`, `title` )
		VALUES
			( CONCAT( 'tom_', i ), 'd' );
		
		SET i = i + 1;
		
	END WHILE;
END;

CALL test_22();#调用存储函数
```

## 2.GROUP BY

8.0语法

```
GROUP_CONCAT 将某个字段的多个值合并成一个字符串输出的聚合函数,获取所有分组的内容
```

```mysql
SELECT
	age,
	GROUP_CONCAT( `id` ),
	GROUP_CONCAT( `name` ) 
FROM
	t_student 
GROUP BY
	age
```

返回表中没有的字段,

```mysql
select  id,name , '10岁' as age from user;
```

逆序分页查询
需要传上次最后的一个值,每次查询,num始终为0

```mysql
select * from tb_order  WHERE id<4 order by order_id desc limit 0,2;
```

顺序查询,一般的格式

```mysql
select * from tb_order limit 2,2;
```

分组获得所有字段,默认逗号分割 GROUP_CONCAT

```mysql
SELECT
	GROUP_CONCAT( id ),
	age 
FROM
	`tb_account` 
WHERE
	`id` >= 1 
GROUP BY
	age
```

复合主键更新

```sql
-- 创建表时定义
CREATE TABLE `order_details` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `order_id` int DEFAULT NULL,
                                 `product_id` int DEFAULT NULL,
                                 `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                 `quantity` int DEFAULT NULL,
                                 `update_count` int DEFAULT '0',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_order_product` (`order_id`,`product_id`),
                                 UNIQUE KEY `idx_order_product_name` (`order_id`,`product_name`)
)
```

插入数据时，若复合索引冲突（即数据已存在），则执行更新操作：

```sql
INSERT INTO order_details (order_id, product_id, product_name, quantity)
VALUES
    (100, 200, '苹果', 1),
    (101, 201, '香蕉', 1)
ON DUPLICATE KEY UPDATE
                     quantity = VALUES(quantity),
                     update_count = IF(update_count IS NULL, 1, update_count + 1);
```

```sql
REPLACE INTO order_details (order_id, product_id, product_name, quantity)
VALUES (100, 200, 'Apple', 30);
```

## 3.JSON

### (1) 建表

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    info JSON
);

INSERT INTO users (name, info) VALUES
('Alice', '{"age": 30, "city": "Beijing", "hobbies": ["reading", "swimming"]}'),
('Bob', '{"age": 25, "city": "Shanghai", "hobbies": ["gaming"]}');

```

### (2) 查询

使用 -> 和 ->> 提取 JSON 值
col->'$.key'：返回 JSON 类型（带引号）
col->>'$.key'：返回字符串/数值（不带引号)

```sql
SELECT name, info->>'$.city' AS city FROM users;
-- 结果：
-- Alice | Beijing
-- Bob   | Shanghai
```

```sql
SELECT name, info->'$.city' AS city FROM users;
-- 结果：
-- Alice | "Beijing"
-- Bob   | "Shanghai"
```

### (3).条件查询（WHERE 中使用 JSON）

```sql
-- 查找 age > 28 的用户
SELECT * FROM users WHERE (info->>'$.age') > 28;

-- 查找 city 是 Beijing 的用户
SELECT * FROM users WHERE info->>'$.city' = 'Beijing';
```

更安全的方式（显式转为 UNSIGNED）：

```sql
SELECT * FROM users WHERE CAST(info->>'$.age' AS UNSIGNED) > 28;
-- 或者直接用 -> 并配合 JSON_UNQUOTE
SELECT * FROM users WHERE JSON_UNQUOTE(info->'$.age') > 28;
```

```sql
SELECT * FROM users WHERE info->'$.age' > 28;
-- 因为 -> 返回的是 JSON 标量（如 30），可直接比较
```

### (4).检查 JSON 是否包含某个键或值

```sql
   -- 检查是否存在 city 字段
SELECT * FROM users WHERE JSON_CONTAINS_PATH(info, 'one', '$.city');

-- 检查 hobbies 是否包含 "reading"
SELECT * FROM users WHERE JSON_CONTAINS(info->'$.hobbies', '"reading"');
-- 注意：第二个参数必须是 JSON 字符串，所以要加双引号

```

### (5).搜索 JSON 数组中是否包含某值

（适用于 MySQL 8.0+）

```sql
-- 使用 JSON_OVERLAPS（MySQL 8.0.17+）
SELECT * FROM users WHERE JSON_OVERLAPS(info->'$.hobbies', '["reading"]');

-- 或使用 JSON_SEARCH（适合模糊匹配字符串）
SELECT * FROM users WHERE JSON_SEARCH(info->'$.hobbies', 'one', 'reading') IS NOT NULL;
```

# 三.7种jon

## 1. inner join

join其实就是inner join，是inner join缩写

```
SELECT <select_list>
FROM Table_A A
INNER JOIN Table_B B
ON A.Key = B.Key
```

![inner_join](README_IMG1/inner_join.png)

## 2.Left Join 左连接

左连接返回左表中的所有行，以及右表中与左表匹配的行。如果右表中没有匹配的行，则返回NULL值。

```
SELECT <select_list>
FROM Table_A A
LEFT JOIN Table_B B
ON A.Key = B.Key
```

![](./Left join.png)

## 3.Right Join 右连接

右连接返回右表中的所有行，以及左表中与右表匹配的行。如果左表中没有匹配的行，则返回NULL值。

```
SELECT <select_list>
FROM Table_A A
RIGHT JOIN Table_B B
ON A.Key = B.Key
```

![](./Right join.png)

## 4.Outer Join 全连接

全连接返回左表和右表中的所有行，如果左表或右表中没有匹配的行，则返回NULL值。

```
SELECT <select_list>
FROM Table_A A
FULL OUTER JOIN Table_B B
ON A.Key = B.Key
```

![](README_IMG1/Outer%20Join.png)

## 5-Left Excluding Join 左排除连接

左排除连接返回左表中没有在右表中找到匹配的行。它只返回左表中没有与右表匹配的行，而右表中匹配的行将被排除在结果集之外。

```
SELECT <select_list>
FROM Table_A A
LEFT JOIN Table_B B
ON A.Key = B.Key
WHERE B.Key IS NULL
```

![](README_IMG1/Left%20Excluding%20Join.png)

## 6-Right Excluding Join 右排除连接

右排除连接返回右表中没有在左表中找到匹配的行。它只返回右表中没有与左表匹配的行，而左表中匹配的行将被排除在结果集之外。

```
SELECT <select_list>
FROM Table_A A
RIGHT JOIN Table_B B
ON A.Key = B.Key
WHERE A.Key IS NULL
```

![](README_IMG1/Right%20Excluding%20Join.png)

## 7-Outer Excluding Join 外部排除连接

外部排除连接是左排除连接和右排除连接的结合，返回左表和右表中没有匹配的行。它返回左表和右表中没有与对方表匹配的行，而匹配的行将被排除在结果集之外。

```
SELECT <select_list>
FROM Table_A A
FULL OUTER JOIN Table_B B
ON A.Key = B.Key
WHERE A.Key IS NULL OR B.Key IS NULL
```

![](README_IMG1/Outer%20Excluding%20Join.png)

# 四.函数

```text
1.单行函数: 返回参数一个,各种三角函数,取值等;
三角函数
字符串函数
时间函数
2.多行函数:聚合函数
AVG,sum, max ,min
having 关键字,过滤,where也是过滤
```

```mysql
## 函数
SELECT RAND(), RAND();
SELECT RAND()*100, RAND(  );
#因子一样,随机数就一样
SELECT RAND(1), RAND(1);

# 聚合函数, 条件查询,必须用 HAVING
# 聚合函数, 条件查询,必须用 HAVING 和 GROUP BY 组合使用
SELECT id,MAX(code) FROM t_person GROUP BY id HAVING MAX(code)> 101; #
# 错误的,不分组,不用max ,直接用where,
SELECT id,MAX(code) FROM t_person  WHERE MAX(code)> 101; #
SELECT id,code FROM t_person  WHERE code> 101; #

# 聚合函数,指定范围,
# 方式1,推荐使用,效率高
SELECT id,MAX(code) FROM t_person WHERE id IN(4,5)  GROUP BY id HAVING MAX(code)> 101; #
# 方式2
SELECT id,MAX(code) FROM t_person   GROUP BY id HAVING MAX(code)> 101 AND id IN(4,5); #
#结论: 当过滤条件中有聚合函数时,过滤条件必须在 HAVING 中
#没有聚合函数时,WHERE HAVING 都可以使用,推荐使用WHERE
```

## 1.子查询

```text
查询语句嵌套在另一个查询语句内部的查询
```

```mysql
# 子查询 
## 需求 谁的code比 tom大
## 方式1,两条语句
SELECT `name`,`code` FROM t_person WHERE `name`='tom'; #
SELECT `name`,`code` FROM t_person WHERE `code`> 101;#
## 方式2,自链接, 返回结果,要是p2的
SELECT
	p2.`name`,
	p2.`code` 
FROM
	t_person p1,
	t_person p2 
WHERE
	p2.`code` > p1.`code` 
	AND p1.`name` = 'tom';
#
## 方式3,子查询,外查询,内查询 
SELECT
	`name`,
	`code` ,
		`title`
FROM
	t_person 
WHERE
	`code` > ( SELECT CODE FROM t_person WHERE `name` = 'tom' );#
	
## 单行(内部查询 返回一个结果),操作符: = > < 等 
SELECT
	`name`,
	`code`,
	`title` 
FROM
	t_person 
WHERE
	`code` > ( SELECT `code` FROM t_person WHERE `name` = 'tom' ) 
	AND `title` = ( SELECT title FROM t_person WHERE `name` = 'lili' );#

##多行 子查询 ,操作符: in any all some(any 别名,一般使用any)
SELECT
	`name`,
	`code`,
	`title` ,
	code2
FROM
	t_person 
WHERE
	`code` IN ( SELECT min( `code` ) FROM t_person GROUP BY id );#
	
## 相关性 :查询 工资 大于 本部门 平均工资的员工信息
## 不相关性: 查询 工资 大于 公司 平均工资的员工信息
	
```

# 五.MySQL优化

## 索引

合适建立索引

```text
1.主键自动建立唯一索引;
2.频繁作为查询条件的字段应该创建索引;
3.外键建立索引;
4.频繁更新的字段不合适建立索引;
5.where条件里面用不到的字段不创建索引;
6.单建/组合索引选择,在高并发小倾向创建组合索引;
7.查询中排序的字段,排序字段若通过索引去访问将提高排序速度;
8.查询中统计或者分组字段;
  
```

不合适建立索引

```text
1.表记录太少;
2.经常增删改的表;
3.数据重复且分布平均的表字段,应该只为经常查询和经常排序的数据建立索引;

```

mysql query optimize 查询优化器

常见瓶颈

```text
cpu在饱和时候一般发生在数据转入内存或者磁盘上读取数据时候;
io发生在数据远大于内存容量的时候;
服务器硬件的性能瓶颈,top,free,iostat,vmstat,查看系统性能状态;
```

## expand

### 使用

```text
EXPLAIN + sql
```

### 能干嘛

```text
1.表的读取顺序;
2.数据读取操作的操作类型;
3.哪些索引可以使用;
4.哪些索引被实际使用;
5.表之间的引用;
每张表有多少行被优化器查询;
```

![expand效果图](README_IMG1/img.png)

```mysql
EXPLAIN SELECT
	`name`,
	`code`,
	`title` 
FROM
	t_person 
WHERE
	`code` > ( SELECT CODE FROM t_person WHERE `name` = 'tom' );#
```

![expand子查询效果图](README_IMG1/img_1.png)

#### id

```text
1.id相同:可以人为是一组,从上往下顺序执行,
2.id不同:如果是子查询,id的序号会递增,id值越大优先级越高,越先执行
3.id相同不同,同时存在
衍生=DERIVED
```

#### select_type

```text
select_type 6种情况,数据读取操作的操作类型
```

#### type 访问数据类型排列

```text
显示查询使用了何种类型,
从最好到最差依次是
system > const > eq_ref > ref > range > index > ALL
一般来说,保证至少达到range,最好 ref 
```

#### possible_keys,keys

```text
possible_keys:显示可能应用在这个表中的索引
keys:最终使用的

possible_keys=null, keys=xx;
覆盖索引: 查询的 字段顺序个数,和复合索引一致
```

```mysql
EXPLAIN  SELECT*FROM t_person WHERE id=1; 
```

![expand 使用到了key](README_IMG1/img_2.png)

#### key_len

```text
索引使用的 字节数,在不损失精度的情况下,长度越短约好,
显示值为索引字段的最大可能长度,并非实际使用长度
```

#### ref

```text
显示索引的那一列被使用了,如果可能的话,是一个常数;
```

#### row

```text
每张表多少被优化器查询,越少越好
```

#### Extra,一个字段多种值

```text
Using where
Using index 使用了索引 ,情况 possible_keys=null, keys=xx;,没有使用主键,但是返回字段匹配索引,出现了好
Using filesort 文件类排序,产生了不好,尽快优化
Using temporary 新建内部临时表,常见order by, group by

```

### 单表优化

```mysql
## 查询 category_id=1 AND comments > 1  views 最多的( 排序 取1)
EXPLAIN SELECT id,author_id FROM article WHERE category_id=1 AND comments > 1 ORDER BY views DESC LIMIT 1;
```

![expand 练习1](README_IMG1/img_3.png)

```text
type: all 最坏情况
extra 还出现了 Using filesort也是最坏情况
```

```mysql
#建立索引 方式1 
CREATE INDEX  idx_article_cv ON article(category_id,views);

#建立索引 方式2
ALTER TABLE `article` ADD INDEX idx_article_cv (`category_id`,`views`);
# 删除索引  方式2
DROP INDEX idx_article_cv ON article;

# 查看索引 
show INDEX FROM article;
```

![建立索引,并查看](README_IMG1/img_4.png)

```text
type: ref
extra  Using filesort 消失
```

![建立索引,优化了一下](README_IMG1/img_5.png)

### 双表优化

```text
左右 外连接 相反表添加索引
A left B , B建索引
A right B ,A建索引
```

### 三表优化

```mysql
select  * from a left join b on a.id = b.id   left join  c  on b.id = c.id;

b c 建索引


```

```text
1.尽量减少join中的nestedloop的循环总次数,永远用小结果集驱动大的结果集;
2.优先优化 尽量减少join中的nestedloop的循环总次数的内层循环
3.保证join语句中被驱动表上join条件字段已经被索引(重要)
4.当无法保证被驱动表的join条件字段被索引且内存资源充足条件下,不要吝啬joinBuffer的设置
```

## 索引失效

```text
1.全值匹配我最爱: 查询结果字段 是复合索引组成字段;
2.**最佳做前缀法则** (重要): 如果索引多列,遵守**最左前**缀法则,是查询从索引的最左前列开始并且 **不跳过索引**中的列;
3.不在索引上做任何操作,计算,函数,会导致索引失效转向全表扫描;
4.存储引擎不能使用索引中范围条件右边的列;
5.尽量使用覆盖索引,减少select *;
6.mysql使用 不等于 != <>的时候无法使用索引,导致全表扫描;
7.is null , is not null  也无法使用索引;
8.like 统配符开通的 mysql 索引也会失效,% 尽量在右边,效果好一点, 左右%, %xx%, 使用覆盖索引;
9.字符串不加单引号,索引失效;
10.少用or,用or 会索引失效;
```

```mysql
 #执行细节,和生命周期情况
show profile    
```

## 小表驱动大表

```text
双for循环,外部取小的, 因为多链接

IN exists
b表必须小于a的数据,,用in优先exists
a表小于b的数据,exists优于in
```

# 六.主从复制 binlog 日志

```text
https://zhuanlan.zhihu.com/p/307288925
```

## 虚拟字段

```sql
CREATE TABLE `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `full_name` varchar(511) GENERATED ALWAYS AS (CONCAT(first_name, ' ', last_name)) VIRTUAL,
  PRIMARY KEY (`id`)
);
```

```text
在 MySQL 中,5.1 版本 开始支持，分区表（Partitioned Table）是将一个大表按照某种规则拆分为多个物理子表的技术，
逻辑上仍然是一个表。分区表可以显著提升查询性能和数据维护效率，尤其是在处理大数据量时
```

## 分区表

```sql
CREATE TABLE sales (
    id INT NOT NULL AUTO_INCREMENT,
    sale_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id, sale_date)
)
PARTITION BY RANGE (YEAR(sale_date)) (
    PARTITION p0 VALUES LESS THAN (2020),
    PARTITION p1 VALUES LESS THAN (2021),
    PARTITION p2 VALUES LESS THAN (2022),
    PARTITION p3 VALUES LESS THAN (2023),
    PARTITION p4 VALUES LESS THAN MAXVALUE
);
```
