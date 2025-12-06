package com.github.axinger;

import com.github.axinger.model.dto.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SpringUtilTests {

    @Test
    public void test1() throws Exception {

        // 检查字符串是否为空
        boolean isEmpty = StringUtils.isEmpty(null);  // true
        boolean isEmpty2 = StringUtils.isEmpty("");  // true

        // 检查字符串是否有文本内容
        boolean hasText = StringUtils.hasText("  ");  // false
        boolean hasText2 = StringUtils.hasText("hello");  // true

        // 分割字符串
        String[] parts = StringUtils.tokenizeToStringArray("a,b,c", ",");
        System.out.println("parts = " + Arrays.toString(parts));
        String[] parts2 = StringUtils.tokenizeToStringArray("a", ",");
        System.out.println("parts2 = " + Arrays.toString(parts2)); //parts2 = [a]
        String[] parts3 = StringUtils.split("a", ",");
        System.out.println("parts3 = " + Arrays.toString(parts3)); //parts3 = null

        // 清除首尾空白
        String trimmed = StringUtils.trimWhitespace("  hello  ");  // "hello"
    }


    @Test
    public void test5() throws Exception {
        // 创建 PropertyPlaceholderHelper 实例
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");

        // 创建属性
        Properties props = new Properties();
        props.setProperty("app.name", "MyApplication");
        props.setProperty("app.version", "1.0.0");
        props.setProperty("app.description", "${app.name} v${app.version} - A simple application");

        // 简单替换
        String simpleResult = helper.replacePlaceholders("Welcome to ${app.name} version ${app.version}!", props::getProperty);
        System.out.println("Simple replacement: " + simpleResult);

        // 嵌套替换
        String nestedResult = helper.replacePlaceholders("${app.description}", props::getProperty);
        System.out.println("Nested replacement: " + nestedResult);

        // 使用默认值 (如果属性不存在，则使用冒号后面的默认值)
        String withDefault = helper.replacePlaceholders("Server running on port ${server.port:8080}", props::getProperty);
        System.out.println("With default value: " + withDefault);
    }

    @Test
    public void test6() throws Exception {

// 检查集合是否为空
//        boolean isEmpty = CollectionUtils.isEmpty(null);  // true
        boolean isEmpty2 = CollectionUtils.isEmpty(Collections.emptyList());  // true

// 集合操作
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("b", "c", "d");
//        Collection<String> intersection = CollectionUtils.intersection(list1, list2);  // [b, c]

// 合并集合
        List<String> target = new ArrayList<>();
        CollectionUtils.mergeArrayIntoCollection(new String[]{"a", "b"}, target);
        System.out.println("target = " + target);

    }

    /// 相同key map
    @Test
    public void test7() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("colors", "red");
        map.add("colors", "blue");
        map.add("sizes", "large");

        List<String> colors = map.get("colors");  // [red, blue]
        System.out.println("colors = " + colors);

        List<String> list = map.get("colors");
    }

    @Test
    public void test8() throws Exception {

        // 创建高并发场景下的引用Map (类似WeakHashMap但线程安全)
        Map<String, Object> cache = new ConcurrentReferenceHashMap<>();
        cache.put("key1", "new LargeObject()");
        System.out.println("cache = " + cache);

    }

    @Test
    public void test9() throws Exception {

        // 解析含系统属性的字符串
        String javaHome = SystemPropertyUtils.resolvePlaceholders("${java.home}");
        System.out.println("javaHome = " + javaHome);

        String version = SystemPropertyUtils.resolvePlaceholders("${java.version}");
        System.out.println("version = " + version);

        String pathWithDefault = SystemPropertyUtils.resolvePlaceholders("${unknown.property:default}");  // "default"

        System.out.println("pathWithDefault = " + pathWithDefault);
    }

    @Test
    public void test10() throws Exception {

// 获取类的字段
        Field field = ReflectionUtils.findField(Person.class, "name");
        System.out.println("field = " + field);
        ReflectionUtils.makeAccessible(field);
        System.out.println("field = " + field);

        Person person = new Person();
        ReflectionUtils.setField(field, person, "John");
        System.out.println("person = " + person);

// 调用方法
        Method method = ReflectionUtils.findMethod(Person.class, "setAge", Integer.class);
        ReflectionUtils.makeAccessible(method);
        ReflectionUtils.invokeMethod(method, person, 30);

// 字段回调
        ReflectionUtils.doWithFields(Person.class, field2 -> {
            System.out.println("doWithFields = " + field2.getName());
        });

        ReflectionUtils.doWithLocalFields(Person.class, field2 -> {
            System.out.println("doWithLocalFields==" + field2.getName());
        });

    }

    @Test
    public void test11() throws Exception {

// 获取类名
        String shortName = ClassUtils.getShortName("org.example.MyClass");  // "MyClass"

// 检查类是否存在
        boolean exists = ClassUtils.isPresent("java.util.List", null);  // true

// 获取所有接口
        Class<?>[] interfaces = ClassUtils.getAllInterfaces(ArrayList.class);

// 获取用户定义的类加载器
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    }


    @Test
    public void test12() throws Exception {

        MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(new MyService());
        invoker.setTargetMethod("calculateTotal");
        invoker.setArguments(new Object[]{100, 0.2});
        invoker.prepare();
        Object result = invoker.invoke();

        System.out.println("result = " + result);
    }

    @Test
    public void test13() throws Exception {

// 复制属性
        Person source = new Person();
        source.setName("John");
        source.setAge(30);
        Person target = new Person();
        BeanUtils.copyProperties(source, target);

// 实例化类
        Person newPerson = BeanUtils.instantiateClass(Person.class);

// 查找方法
        Method method = BeanUtils.findMethod(Person.class, "setName", String.class);

    }

    @Test
    public void test14() throws Exception {
        // 复制文件内容
        byte[] bytes = FileCopyUtils.copyToByteArray(new File("input.txt"));
        FileCopyUtils.copy(bytes, new File("output.txt"));

// 读取文本
        String content = FileCopyUtils.copyToString(
                new InputStreamReader(new FileInputStream("input.txt"), "UTF-8"));

// 流复制
//        FileCopyUtils.copy(inputStream, outputStream);
    }

    @Test
    public void test15() throws Exception {

// 流操作
//        byte[] data = StreamUtils.copyToByteArray(inputStream);
//        String text = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//        StreamUtils.copy(inputStream, outputStream);
//        StreamUtils.copy("Hello", StandardCharsets.UTF_8, outputStream);

    }

    @Test
    public void test16() throws Exception {

// 删除目录
        boolean deleted = FileSystemUtils.deleteRecursively(new File("/tmp/test"));

// 复制目录
        FileSystemUtils.copyRecursively(new File("source"), new File("target"));

    }

    @Test
    public void test17() throws Exception {

// 获取匹配资源
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(
                        new PathMatchingResourcePatternResolver())
                .getResources("classpath*:META-INF/*.xml");

        for (Resource resource : resources) {
            System.out.println("resource = " + resource);
        }

    }

    @Test
    public void test18() throws Exception {

// 获取Cookie
//        Cookie cookie = WebUtils.getCookie(request, "sessionId");

//// 获取请求路径
//        String path = WebUtils.getLookupPathForRequest(request);
//
//// 从请求中获取参数
//        int pageSize = WebUtils.getInitParameter(request, "pageSize", 10);

    }

    @Test
    public void test19() throws Exception {

// 编解码URI组件
        String encoded = UriUtils.encodePathSegment("path with spaces", "UTF-8");
        System.out.println("encoded = " + encoded);
        String decoded = UriUtils.decode(encoded, "UTF-8");
        System.out.println("decoded = " + decoded);

    }

    @Test
    public void test20() throws Exception {

// 构建URI
        URI uri = UriComponentsBuilder.fromHttpUrl("http://example.com")
                .path("/products")
                .queryParam("category", "books")
                .queryParam("sort", "price")
                .build()
                .toUri();

        System.out.println("uri = " + uri);

    }

    @Test
    public void test21() throws Exception {

//        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
//// 请求处理后
//        byte[] body = wrapper.getContentAsByteArray();

    }

    @Test
    public void test22() throws Exception {

// HTML转义
        String escaped = HtmlUtils.htmlEscape("<script>alert('XSS')</script>");
// &lt;script&gt;alert('XSS')&lt;/script&gt;

// HTML反转义
        String unescaped = HtmlUtils.htmlUnescape("&lt;b&gt;Bold&lt;/b&gt;");
// <b>Bold</b>

    }

    @Test
    public void test23() throws Exception {

// 常用断言
//        Assert.notNull(object, "Object must not be null");
//        Assert.hasText(name, "Name must not be empty");
//        Assert.isTrue(amount > 0, "Amount must be positive");
//        Assert.notEmpty(items, "Items must not be empty");
//        Assert.state(isInitialized, "Service is not initialized");

    }

    @Test
    public void test24() throws Exception {

// 对象工具
        boolean isEmpty = ObjectUtils.isEmpty(null);  // true
        boolean isEmpty2 = ObjectUtils.isEmpty(new String[0]);  // true

        String object = null;
        String nullSafe = ObjectUtils.nullSafeToString(object);  // "null"
        System.out.println("nullSafe = " + nullSafe);
//        boolean equals = ObjectUtils.nullSafeEquals(obj1, obj2);

// 默认值
//        String value = ObjectUtils.getOrDefault(null, "default");

        String displayString = ObjectUtils.getDisplayString(object);
        System.out.println("displayString = " + displayString); //displayString =

    }

    @Test
    public void test25() throws Exception {

// 数字转换
        Integer parsed = NumberUtils.parseNumber("42A", Integer.class);
        System.out.println("parsed = " + parsed);
        Double converted = NumberUtils.convertNumberToTargetClass(42, Double.class);

    }

    @Test
    public void test26() throws Exception {

// 格式化日期
//        String formatted = DateFormatterRegistrar.getDateTimeInstance().format(new Date());

    }

    @Test
    public void test27() throws Exception {

// MD5哈希
        String md5 = DigestUtils.md5DigestAsHex("password".getBytes());

// 文件MD5
        String fileMd5;
        try (InputStream is = new FileInputStream("file.txt")) {
            fileMd5 = DigestUtils.md5DigestAsHex(is);
        }

// Base64编解码
        byte[] data = "Hello World".getBytes();
        String encoded = Base64Utils.encodeToString(data);
        byte[] decoded = Base64Utils.decodeFromString(encoded);


// 文本加密
        String password = "secret";
        String salt = "deadbeef";
        TextEncryptor encryptor = Encryptors.text(password, salt);
        String encrypted = encryptor.encrypt("Message");
        String decrypted = encryptor.decrypt(encrypted);

    }

    @Test
    public void test28() throws Exception {

        /// if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
        /// 			return new JacksonJsonParser();
        ///                }
        // JSON解析
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> parsed = parser.parseMap("""
                {"name":"John", "age":30}
                """);
        System.out.println("parsed = " + parsed);
        List<Object> parsedList = parser.parseList("[1, 2, 3]");
        System.out.println("parsedList = " + parsedList);

    }

    @Test
    public void test29() throws Exception {

// 类型解析
        ResolvableType type = ResolvableType.forClass(List.class);
        ResolvableType elementType = type.getGeneric(0);

// 泛型类型处理
        ResolvableType mapType = ResolvableType.forClassWithGenerics(Map.class, String.class, Integer.class);

    }

    @Test
    public void test30() throws Exception {

        // 随机字符串生成
        String random = RandomStringUtils.randomAlphanumeric(10);
        System.out.println("random = " + random);
        String randomLetters = RandomStringUtils.randomAlphabetic(8);
        System.out.println("randomLetters = " + randomLetters);
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        System.out.println("randomNumeric = " + randomNumeric);
    }

    @Test
    public void test31() throws Exception {
        // 缓存控制
        CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS)
                .noTransform()
                .mustRevalidate();

        String headerValue = cacheControl.getHeaderValue();

    }

    @Test
    public void test32() throws Exception {
        // 查找注解
//        Component annotation = AnnotationUtils.findAnnotation(MyClass.class, Component.class);
//
//// 获取注解属性
//        String value = AnnotationUtils.getValue(annotation, "value").toString();
//
//// 合并注解
//        Component mergedAnnotation = AnnotationUtils.synthesizeAnnotation(
//                annotation, MyClass.class);

    }

    @Test
    public void test33() throws Exception {

// 类型转换
        DefaultConversionService conversionService = new DefaultConversionService();
        String strValue = "42A";
        Integer intValue = conversionService.convert(strValue, Integer.class);

        System.out.println("intValue = " + intValue);
    }

    @Test
    public void test34() throws Exception {
        // 根据文件名推断媒体类型
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType("document.pdf");
        System.out.println("mediaType = " + mediaType);
    }

    @Test
    public void test35() throws Exception {
        // MIME类型常量和解析
//        boolean isCompatible = MimeTypeUtils.APPLICATION_JSON.isCompatibleWith(
//                MimeType.APPLICATION_JSON_UTF8);

    }

    @Test
    public void test36() throws Exception {
        // 创建WebClient
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.example.com")
                .defaultHeader("Authorization", "Bearer token")
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                        clientRequest -> {
                            // 请求处理
                            return Mono.just(clientRequest);
                        }))
                .build();

    }

    @Test
    public void test37() throws Exception {

        // 添加属性源
        StandardEnvironment env = new StandardEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("app.name", "MyApp");
        env.getPropertySources().addFirst(new MapPropertySource("my-properties", map));

    }

    @Test
    public void test38() throws Exception {
        // 发布事件
//        ApplicationEventPublisher publisher =  null;/* 获取发布器 */;
//        publisher.publishEvent(new CustomEvent("Something happened"));
    }

    @Test
    public void test39() throws Exception {
        // 获取/设置当前语言环境
        Locale currentLocale = LocaleContextHolder.getLocale();
        String language = currentLocale.getLanguage();
        System.out.println("language = " + language);
        LocaleContextHolder.setLocale(Locale.FRENCH);
    }

    @Test
    public void test40() throws Exception {
        // AOP工具方法
//        boolean isAopProxy = AopUtils.isAopProxy(bean);
//        boolean isCglibProxy = AopUtils.isCglibProxy(bean);
//        Class<?> targetClass = AopUtils.getTargetClass(bean);

    }

    @Test
    public void test41() throws Exception {
        // 创建代理
//        ProxyFactory factory = new ProxyFactory(targetObject);
//        factory.addInterface(MyInterface.class);
//        factory.addAdvice(new MyMethodInterceptor());
//        MyInterface proxy = (MyInterface) factory.getProxy();

    }

    @Test
    public void test42() throws Exception {
        // 扫描类路径
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        Set<BeanDefinition> beans = scanner.findCandidateComponents("org.example");

    }

    @Test
    public void test43() throws Exception {
        // 解析YAML文件
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties = yaml.getObject();

    }

    private static class MyService {
        public int calculateTotal(int amount, double taxRate) {
            return (int) (amount * (1 + taxRate));
        }
    }
}
