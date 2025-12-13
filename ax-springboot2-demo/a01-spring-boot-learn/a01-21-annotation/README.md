springboot的@Value中#和$区别详解

```text
配置文件的属性注入
```

```java


// 直接赋值
    @Value("hello")
    private String hello; // hello
 
    // 读取配置文件中的value.name
    @Value("${value.name}")
    private String name; // morris
 
    // 读取配置文件中的value.age，不存在给个默认值
    @Value("${value.age:18}")
    private Integer age; // 18
 
    private Integer num;
 
    // 通过set方法注入，这样可以对类型不匹配的异常进行捕获
    @Value("${value.num:xxx}")
    public void setNum(String num) {
        try {
            this.num = Integer.parseInt(num);
        } catch (Exception e) {
            // ignore
        }
    }

```

```text
    其实是Spring Expression Language (SpEL)表达式，可以在注入属性前进行一些简单的计算等逻辑。
    通过非配置文件的属性注入。
```

```java

    @Value("#｛｝") //中使用关系操作符：
    // Relational Operators 关系操作符
    @Value("#{2 == 2}")
    private boolean b1; // true

    @Value("#{2 < 1}")
    private boolean b2; // false

    @Value("#{2 > 1}")
    private boolean b3; // true

    // Logical Operators 逻辑操作符
    @Value("#{true && false}")
    private boolean b4; // false

    @Value("#{true || false}")
    private boolean b5; // true

    @Value("#{!false}")
    private boolean b6; // true


    /**
     * 注入普通字符串，相当于直接给属性默认值
     */
    @Value("程序新视界")
    private String wechatSubscription;

    /**
     *  注入操作系统属性
     */
    @Value("#{systemProperties['os.name']}")
    private String systemPropertiesName;

    /**
     * 注入表达式结果
     */
    @Value("#{ T(java.lang.Math).random() * 100.0 }")
    private double randomNumber;

    /**
     * 注入其他Bean属性：注入config对象的属性tool
     */
    @Value("#{config.tool}")
    private String tool;

    /**
     * 注入列表形式（自动根据"|"分割）
     */
    @Value("#{'${words}'.split('\\|')}")
    private List<String> numList;

    /**
     * 注入文件资源
     */
    @Value("classpath:config.xml")
    private Resource resourceFile;

    /**
     * 注入URL资源
     */
    @Value("http://www.choupangxia.com")
    private URL homePage;
```

```text
无论使用#{}或${}进行属性的注入，当无法获取对应值时需要设置默认值，可以采用如下方式来进行设置。
```

```java
/**
 * 如果属性中未配置ip，则使用默认值
 */
@Value("${ip:127.0.0.1}")
private String ip;

/**
 * 如果系统属性中未获取到port的值，则使用8888。
 */
@Value("#{systemProperties['port']?:'8888'}")
private String port;
```
