```text
工作原理分为六个部分：

    读取核心配置文件并返回InputStream流对象。

    根据InputStream流对象解析出Configuration对象，然后创建SqlSessionFactory工厂对象

    根据一系列属性从SqlSessionFactory工厂中创建SqlSession

    从SqlSession中调用Executor执行数据库操作&&生成具体SQL指令

    对执行结果进行二次封装

    提交与事务


```

```yaml
## Mybatis 配置
mybatis.type-aliases-package=com.xfind.core.entity.xianyu
mybatis.mapper-locations=classpath:mapper/*.xml
#使全局的映射器启用或禁用缓存。
mybatis.configuration.cache-enabled=true
#全局启用或禁用延迟加载。当禁用时，所有关联对象都会即时加载。
mybatis.configuration.lazy-loading-enabled=true
#当启用时，有延迟加载属性的对象在被调用时将会完全加载任意属性。否则，每种属性将会按需要加载。
mybatis.configuration.aggressive-lazy-loading=true
#是否允许单条sql 返回多个数据集  (取决于驱动的兼容性) default:true
mybatis.configuration.multiple-result-sets-enabled=true
#是否可以使用列的别名 (取决于驱动的兼容性) default:true
mybatis.configuration.use-column-label=true
#允许JDBC 生成主键。需要驱动器支持。如果设为了true，这个设置将强制使用被生成的主键，有一些驱动器不兼容不过仍然可以执行。  default:false
mybatis.configuration.use-generated-keys=true
#指定 MyBatis 如何自动映射 数据基表的列 NONE：不隐射\u3000PARTIAL:部分  FULL:全部
mybatis.configuration.auto-mapping-behavior=partial
#这是默认的执行类型  （SIMPLE: 简单； REUSE: 执行器可能重复使用prepared statements语句；BATCH: 执行器可以重复执行语句和批量更新）
mybatis.configuration.default-executor-type=simple
#使用驼峰命名法转换字段。
mybatis.configuration.map-underscore-to-camel-case=true
#设置本地缓存范围 session:就会有数据的共享  statement:语句范围 (这样就不会有数据的共享 ) defalut:session
mybatis.configuration.local-cache-scope=session 
#设置但JDBC类型为空时,某些驱动程序 要指定值,default:OTHER，插入空值时不需要指定类型
mybatis.configuration.jdbc-type-for-null=null
#如果数据为空的字段，则该字段省略不显示，可以通过添加配置文件，规定查询数据为空是则返回null。
mybatis.configuration.call-setters-on-nulls=true

```

mybatis 的 <![CDATA[ ]]> 标签作用
![xml中出现 > < 大于小于符号,解析异常](README_IMG1/img_6.png)

```text
因为这个是xml格式的，所以不允许出现类似“>”这样的字符，但是都可以使用<![CDATA[ ]]>符号进行说明，将此类符号不进行解析；
```

## where标签

```text
  <!-- where标签，会将第一个and过滤（不会过滤第二个）-->
  
   <where>
        <if test="username != null and username != ''">
            and user_name = #{username,jdbcType=VARCHAR}
        </if>

        <if test="loginState == 0 || loginState == 1">
            and login_state = #{loginState}
        </if>
    </where>
        
        
```

## trim 标签 - 字符串截取

```text

prefix: 前缀,整体标签后结果
prefixOverrides: 前缀覆盖,去掉多余的字符
suffix: 
suffixOverrides: 


  <!-- 拼接case when 这是另一种写法，这种写着更专业的感觉 -->
    <trim prefix="username=case" suffix="end,">
        <foreach collection="list" item="item">
            <if test="item.username!=null">
                when id=#{item.id} then #{item.username}
            </if>
        </foreach>
    </trim>
    
```

## choose when , swtich-case

## set 封装条件更新, 避免 条件逗号

## for

```text
遍历list
<foreach collection="list" item="item" open="(" separator="," close=")">
    #{item}
</foreach>
    
    
遍历 map   

<foreach collection="list"  index="key" item="value" open="(" separator="," close=")">
    #{item}
</foreach>
 
```

## 内置参数

```text
不只是方法传递的参数可以用来判断,
还有内置参数
_parameter : 代表整个参数,单个参数就是这个参数, 多个参数,参数会封装map,_parameter就是map
_databaseId : 如果配置了 _databaseIdprovider标签,就当前数据库别名
```

## bind

```text
将OGNL 表达式 绑定到一个变量中,后来引用
```

## 批量执行语句

```text
语句太长,mysql会失败
```

## 两级缓存

```text
1.一级缓存,本地缓存,同一次会话查询的数据会缓存到本地中,默认开启,无法关闭,sqlSession
四种缓存失效情况
1.sqlSession不同
2.sqlSession相同,查询条件不同,缓存失效
3.sqlSession相同,两次查中间执行了增删改操作,缓存失效
4.sqlSession相同,手动清除一级缓存

2.二级缓存,全局缓存,默认不开启
基与nameSpace级别的缓存,一个nameSpace对应一个二级缓存


```
