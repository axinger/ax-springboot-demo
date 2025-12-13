package com.github.axinger.stream;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.github.axinger.DeviceModel;
import com.github.axinger.FlowBean;
import com.github.axinger.Person;
import com.github.axinger.model.Staff;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    static List<Person> personList = Person.personList;


    public static Stream<Character> fromStringToStream(String str) {
        List<Character> list = new ArrayList<>();
        for (Character c : str.toCharArray()) {
            list.add(c);
        }


        return list.stream();
//        return  Arrays.asList(str.toCharArray()).stream();
    }

    @Test
    void test_1() {
//        List<String> list = null;
        List<String> list = null;
        //这个不会, null,count = 0
        long count = Stream.ofNullable(list).count();
        System.out.println("count = " + count);
        //这个空指针
//        System.out.println("list.stream().count() = " + list.stream().count());
        List<Integer> list1 = Stream.ofNullable(list).map(List::size).toList();
        System.out.println("list1 = " + list1);

        Stream.ofNullable(list).findFirst().ifPresentOrElse(val -> {
            System.out.println("val = " + val);
        }, () -> {
            System.out.println("null值");
        });

        Optional<String> element = Optional.ofNullable(list)
                .flatMap(l -> l.size() > 2 ? Optional.ofNullable(l.get(2)) : Optional.empty());

        System.out.println("element = " + element);


        list = List.of("a", "b");
        Optional.ofNullable(list)
                .map(l -> {
                    System.out.println("l = " + l);
                    return l.size() > 1 ? l.get(1) : null;
                }).ifPresentOrElse(val -> {
                    System.out.println("val = " + val);
                }, () -> {
                    System.out.println("index2: null值");
                });


    }

    @Test
    void test_mapToInt() {

        List<String> list2 = List.of("a", "bb", "ccc", "dddd");

        int sum = list2.stream().mapToInt(a -> a.length()).sum();
        System.out.println("sum = " + sum);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach=======");
    }

    /**
     * 按照月分组
     */
    @Test
    void test_group_date() {
        List<Staff> staffList = new ArrayList<>();
        staffList.add(new Staff("张三", DateUtil.parse("2022-01-01", "yyyy-MM-dd"), new BigDecimal(10)));
        staffList.add(new Staff("张三", DateUtil.parse("2022-01-02", "yyyy-MM-dd"), new BigDecimal(14)));
        staffList.add(new Staff("张三", DateUtil.parse("2022-01-03", "yyyy-MM-dd"), new BigDecimal(11)));

        staffList.add(new Staff("张三", DateUtil.parse("2022-02-03", "yyyy-MM-dd"), new BigDecimal(10)));
        staffList.add(new Staff("张三", DateUtil.parse("2022-02-04", "yyyy-MM-dd"), new BigDecimal(10)));


        // 日期月分组
        Map<String, List<Staff>> collect = staffList.stream().collect(
                Collectors.groupingBy(
                        o -> DateUtil.format(o.getDate(), "yyyy-MM")
                ));


        System.out.println("日期月分组 = " + collect);

        // 日期分组求和
        Map<String, BigDecimal> collect1 = staffList.stream().collect(
                Collectors.groupingBy(
                        o -> DateUtil.format(o.getDate(), "yyyy-MM"),
                        Collectors.mapping(Staff::getYield, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )));
        System.out.println("日期分组求和 = " + collect1);

    }

    @Test
    void test_any() {
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);

        // 匹配第一个
        list.stream().findFirst().ifPresent(val -> {
            System.out.println("匹配第一个值：" + val);
        });

        list.parallelStream().filter(x -> x > 5).findFirst().ifPresent(val -> {
            System.out.println("匹配第一个值,大于5的:" + val);
        });

        // 匹配任意（适用于并行流）,会随机返回一个
        list.parallelStream().filter(x -> x > 5).findAny().ifPresent(val -> {
            System.out.println("匹配任意一个值,大于5的:" + val);
        });

        // 是否包含符合特定条件的元素
        boolean anyMatch = list.stream().anyMatch(x -> x < 6);
        System.out.println("是否存在小于6的值：" + anyMatch);
    }

    @Test
    void test_boxed() {
        // boxed的作用就是将int类型的stream转成了Integer类型的Stream
        List<Integer> integers = Stream.of("1", "2")
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();
        System.out.println("integers = " + integers);
    }

    @Test
    void test_peek_map() {

        // peek 和 map 区别
        // peek 没有返回值,可以直接修改当前值
        List<Person> list1 = personList
                .stream()
                .peek((val) -> val.setName("pek:" + val.getName()))
                .toList();
        System.out.println("list1 = " + list1);
    }

    // 统计个数
    @Test
    void test_map_grouping_count() {
        List<DeviceModel> list = new ArrayList<>();

        list.add(DeviceModel.builder()
                .id("3")
                .state("2")
                .build());

        list.add(DeviceModel.builder()
                .id("1")
                .state("1")
                .build());


        list.add(DeviceModel.builder()
                .id("2")
                .state("2")
                .build());


        // 统计个数
        Map<String, Long> collect = list.stream().filter(val -> val.getState().equals("1")).collect(Collectors.groupingBy(DeviceModel::getId, Collectors.counting()));
        System.out.println("collect = " + collect);

        // map排序
        List<Map.Entry<String, Long>> collect2 = collect.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        System.out.println("map排序 = " + collect2);


        LinkedHashMap<String, List<DeviceModel>> collect3 = list.stream()
                .sorted(Comparator.comparing(DeviceModel::getId))
                .collect(Collectors.groupingBy(DeviceModel::getState, LinkedHashMap::new, Collectors.toList()));
        System.out.println("collect3 = " + JSON.toJSONString(collect3));


    }

    @Test
    void test_sum() {

        // 求总数
        long count = personList.size();
        System.out.println("count = " + count);

        // 求平均
        Double average = personList.stream().collect(Collectors.averagingDouble(Person::getAge));
        System.out.println("average = " + average);
        // averagingDouble 因为有参数,所以用上面方法简单
//        personList.stream().map(Person::getSalary).collect(Collectors.averagingInt(value -> value));

        // 求最大
        personList.stream().map(Person::getAge).max(Integer::compare).ifPresent(val -> {
            System.out.println("max = " + val);
        });

        // 求工资之和
        Integer sum = personList.stream().mapToInt(Person::getAge).sum();
        System.out.println("sum = " + sum);

        // 统计所有的
        IntSummaryStatistics collect1 = personList.stream().collect(Collectors.summarizingInt(Person::getAge));
        System.out.println("collect1.getCount() = " + collect1.getCount());
        System.out.println("collect1.getSum() = " + collect1.getSum());
        System.out.println("collect1.getMax() = " + collect1.getMax());
        System.out.println("collect1.getMin() = " + collect1.getMin());
        System.out.println("collect1.getAverage() = " + collect1.getAverage());

    }


    @Test
    void test_sum2() {

        String concat = Stream.of("a", "2").collect(StringBuilder::new, StringBuilder::append,
                        StringBuilder::append)
                .toString();
        System.out.println("concat = " + concat);

        //多个求和
        List<FlowBean> list = List.of(
                FlowBean.builder().upFlow(10).downFlow(11).build(),
                FlowBean.builder().upFlow(20).downFlow(21).build()
        );

        FlowBean outV = list.stream().collect(FlowBean::new, FlowBean::accumulate, FlowBean::combine);
        System.out.println("outV = " + outV);
    }

    @Test
    void test_groupingBy_sum() {

        // 优先使用这个方法
        int sum = personList.stream()
                .mapToInt(Person::getAge)
                .sum();
        System.out.println("sum = " + sum);


        int sum2 = personList.stream()
                .collect(Collectors.summingInt(Person::getAge));
        System.out.println("sum2 = " + sum2);


        // 分组求和
        Map<String, Integer> collect = personList.stream()
                .collect(Collectors.groupingBy(
                                Person::getSex,
                                Collectors.summingInt(Person::getAge)
                        )
                );
        System.out.println("分组求和 = " + collect);

        Map<String, Long> collect1 = personList.stream()
                .collect(Collectors.groupingBy(
                                Person::getSex,
                                Collectors.counting()
                        )
                );
        System.out.println("分组求个数 = " + collect1);
    }


    @Test
    void test() {

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        List<Integer> list = IntStream.range(0, nums.size() - 1)
                .map(i -> nums.get(i + 1) - nums.get(i))
                .boxed()
                .toList();

        System.out.println("list = " + list);

        Integer collect = IntStream.range(0, nums.size() - 1)
                .map(i -> nums.get(i + 1) - nums.get(i))
                .boxed()
                .collect(Collectors.summingInt(Integer::intValue));
        System.out.println("collect = " + collect);

    }

    @Test
    void test_findFirst() {

        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        final Integer integer = list.stream().filter(o -> o > 3).findFirst().orElse(-1);
        System.out.println("integer = " + integer);


        final int i = LocalDateTime.now().getHour() / 2;
        System.out.println("i = " + i);

        System.out.println("i = " + (4 % 2));
    }


    @Test
    void Optional_null() {

        List listNull = null;
        Optional.ofNullable(listNull).orElse(new ArrayList<>()).stream().forEach(val -> System.out.println("Optional 和 Stream val = " + val));
    }

    @Test
    void test_2_list() {

        List<Map<Object, Object>> list1 = Lists.newArrayList(new HashMap() {{
            put("id", "1");
            put("name", "jim");
            put("age", "");
        }}, new HashMap() {{
            put("id", "2");
            put("name", "tom");
            put("age", "");
        }});


        List<Map<Object, Object>> list2 = Lists.newArrayList(new HashMap() {{
            put("id", "1");
            put("age", "10");
        }}, new HashMap() {{
            put("id", "2");
            put("age", "12");
        }});

        list1.stream().forEach(m -> {

            final Object age = list2.stream().filter(m2 -> Objects.equals(m.get("id"), m2.get("id"))).findFirst().map(val -> val.get("age")).orElse("");

            m.put("age", age);

        });

        System.out.println("list1 = " + list1);

    }

    @Test
    void test_limit() {

        // 超出不回报错
        final List<Integer> list = Lists.newArrayList(1, 2, 3);

        list.stream().limit(3).forEach(System.out::println);
    }

    @Test
    void test_limit_null() {
        final List<Integer> list = Lists.newArrayList();
        list.stream().limit(1).forEach(val -> System.out.println("val = " + val));
        final List<Integer> collect = list.stream().limit(1).collect(Collectors.toList());
        System.out.println("collect = " + collect);

        final List<Integer> list2 = null;

//        List<Integer> list3 = null;
//        Optional.ofNullable(list2).map(List::stream).ifPresent(val->{
//
//            list3 = val.collect(Collectors.toList());
//
//        });

//        final List<Integer> list2 = Lists.newArrayList

//        Stream.of(list2).limit(1).forEach(val-> System.out.println("val = " + val));
//
//        final List<Object> collect1 = Stream.of(list2).flatMap(e -> {
//            System.out.println("e = " + e);
//            return Stream.empty();
//        }).collect(Collectors.toList());
//        System.out.println("collect1 = " + collect1);

//        final List<List<Integer>> collect2 = Stream.generate(() -> list2).collect(Collectors.toList());


    }

    @Test
    void test_limit2() {

        // 超出不回报错
        final List<Integer> list = Lists.newArrayList(1, 2);
//        final List<List<Integer>> collect1 = Stream.of(list2).limit(1).collect(Collectors.toList());
//        System.out.println("collect1 = " + collect1);1, 2,3,4);

        List<Integer> list1 = list.stream().skip(2).limit(1).toList();
        System.out.println("list1 = " + list1);
    }

    @Test
    void test_limit_findFirst() {

        // 超出不回报错
//        final List<Integer> list = Lists.newArrayList(1, 2,3,4);
        final List<Integer> list = Lists.newArrayList();
        final Integer integer = list.stream().findFirst().orElse(-1);

        System.out.println("integer = " + integer);

        List<Integer> list2 = null;
        list2 = Lists.newArrayList(1);
        final Integer val = Optional.ofNullable(list2).orElse(new ArrayList<>()).stream().findFirst().orElse(-1);
        System.out.println("val = " + val);

        // 判空查找指定位置元素
        list2 = Lists.newArrayList(1, 2, 3, 4);
        final Integer s2 = Optional.ofNullable(list2).orElse(new ArrayList<>()).stream().skip(2).findFirst().orElse(-1);
        System.out.println("s2 = " + s2);

    }

    @Test
    void test_reverseOrder() {

        final List<Integer> list1 = Lists.newArrayList(1, 3, 2, 4);
        final List<Integer> list2 = list1.parallelStream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println("倒序 = " + list2);

    }

    @Test
    void test_index() {

        final List<Integer> list1 = Lists.newArrayList(1, 3, 2, 4);

        List<Integer> list = list1.stream().map(val -> list1.indexOf(val)).toList();
        System.out.println("list = " + list);


        List<Integer> list2 = IntStream.rangeClosed(0, list1.size() - 1).mapToObj(i -> {
            System.out.println("i = " + i);
            return i;
        }).toList();

        System.out.println("list2 = " + list2);


        Stream.of("1", "2", "20", "3")
                .collect(Collectors.toCollection(ArrayDeque::new)) // or LinkedList
                .descendingIterator()
                .forEachRemaining(System.out::println);


        List<Integer> reverse = ListUtil.reverseNew(list1);
        System.out.println("reverse = " + reverse);
        System.out.println("list1 = " + list1);
    }

    // 拍扁
    @Test
    void test_list_map() {
        // list嵌套map 取map的值
//        String name = "jim";
        String name = null;
        final Map<String, Object> map = new HashMap() {{
            put("name", name);
            put("age", 10);
        }};
//        final Map<String, Object> map = null;
        System.out.println("map = " + map);
        final ArrayList<Map<String, Object>> list = Lists.newArrayList(map);

        final List<Object> collect = list.stream().flatMap(el -> {

            return Optional.ofNullable(el).map(Map::values).orElse(new ArrayList<>()).stream();
//            el.values().stream()


        }).collect(Collectors.toList());
        System.out.println("collect = " + collect);


        final List<Double> collect2 = list.parallelStream()
                // 拍扁,同时检查 list中map是否null,给出默认空list
                .flatMap(el -> Optional.ofNullable(el).map(Map::values).orElse(new ArrayList<>()).stream())
                // 转成double
                .map(el -> Double.parseDouble(Optional.ofNullable(el).orElse(0).toString())).collect(Collectors.toList());

        System.out.println("collect2 = " + collect2);
    }

    @Test
    void test3() {
        Object[] objects = new Object[]{1, 2, 3};

        Arrays.stream(objects).forEach(e -> System.out.println(e));

        Stream.of(objects).forEach(e -> System.out.println(e));

    }

    @Test
    void test2() {


        List<Person> list = new ArrayList<Person>() {
            {
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
            }
        };


        List<Person> list2 = new ArrayList<Person>() {
            {
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
                add(Person.builder()
                        .id(1)
                        .build());
            }
        };

        List<List<Person>> list3 = new ArrayList<>();
        list3.add(list);
        list3.add(list2);

        list3.stream().flatMap(e -> e.stream()).forEach(e -> {
            System.out.println("flatMap 拍扁 e = " + e);
        });
    }

    @Test
    public void test4() {

        List<String> list = Arrays.asList("aa", "bb", "cc", "dd");

        list.stream().flatMap(StreamTest::fromStringToStream).forEach(System.out::println);
    }

    @Test
    public void test5() {
        /// 排序
        List list = Arrays.asList(1, 2, 4, 3, -1);
        list.stream().sorted().forEach(System.out::println);

    }

    @Test
    void test6() {
        /// 排序
        List<Person> persons = new ArrayList<Person>() {{
            add(Person.builder()
                    .id(1)
                    .age(18)
                    .build());
            add(Person.builder()
                    .id(1)
                    .age(17)
                    .build());
            add(Person.builder()
                    .id(1)
                    .age(16)
                    .build());
            add(Person.builder()
                    .id(1)
                    .age(20)
                    .build());

        }};

        List<Person> result0 = persons.stream().sorted(Comparator.comparing(Person::getAge)).collect(Collectors.toList());
        System.out.println("getAge = " + result0);

        List<Person> result = persons.stream().sorted(Comparator.comparing(Person::getName)).collect(Collectors.toList());
        System.out.println("getName = " + result);

        List<Person> result2 = persons.stream().sorted(Comparator.comparing(Person::getName).thenComparing(Person::getAge)).collect(Collectors.toList());

        System.out.println("result2 = " + result2);


    }

    /**
     * Collectors.partitioningBy会根据值是否为true,把集合中的数据分割为两个列表，一个true列表，一个false列表。
     */
    @Test
    void test_partitioningBy() {
        Map<Boolean, List<Person>> collect = personList.stream().collect(Collectors.partitioningBy(p -> p.getAge() > 18));
        System.out.println("collect = " + collect);
    }

    @Test
    void test_reduce() {
        /// 匹配和查找
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        // 规约 反复结合起来, 求和,求积,类似用法
        Integer reduce1 = list.stream().reduce(1, (e1, e2) -> {
            System.out.println("reduce 1 : e1 = " + e1 + " e2 = " + e1);
            return e1 + e2;
        });
        System.out.println("reduce1 = " + reduce1);

        /// 没有初始值,返回Optional 类型
        Optional<Integer> reduce2 = list.stream().reduce((e1, e2) -> {
            System.out.println("reduce 1 : e1 = " + e1 + " e2 = " + e1);
            return e1 + e2;
        });
        System.out.println("reduce2 = " + reduce2);

        // reduce 第一个参数,给个默认值,
        Integer reduceSum = list.stream().reduce(Integer::sum).get();
        System.out.println("reduceSum = " + reduceSum);

        Integer reduceSum0 = list.stream().reduce(0, Integer::sum);
        System.out.println("reduceSum0 = " + reduceSum0);

        Integer reduceSum1 = list.stream().reduce(2, Integer::sum);
        System.out.println("reduceSum1 = " + reduceSum1);

        Integer mapToIntSum = list.stream().mapToInt(Integer::valueOf).sum();
        System.out.println("mapToIntSum = " + mapToIntSum);
    }

    // 去重

    // 根据name,sex两个属性去重
    @Test
    void test_TreeSet() {

        List<Person> persons = new ArrayList();
        persons.add(Person.builder().id(1).name("jim").sex("0").build());
        persons.add(Person.builder().id(2).name("jim").sex("0").build());
        persons.add(Person.builder().id(3).name("jim").sex("1").build());

        List<Person> uniqueByNameAndSex = persons.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getName() + o.getSex()))), ArrayList::new));

        System.out.println("uniqueByNameAndSex = " + uniqueByNameAndSex);

        List<Person> uniqueByNameAndSex1 = persons.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getName).thenComparing(Person::getSex))), ArrayList::new));
        System.out.println("uniqueByNameAndSex1 = " + uniqueByNameAndSex1);


    }

    @Test
    void collectingAndThen_test() {

        /// 不知道作用
        Stream<String> stream = Stream.of("d", "a", "c", "d");
        List<String> list = stream.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::<String>unmodifiableList));
        System.out.println(list);
    }


    @Test
    void count_test() {
        Stream<String> stream = Stream.of("1", "2", "3", "4");


        Long result = stream.collect(Collectors.counting());

        System.out.println(result);


        System.out.println(Arrays.asList("1", "2", "3", "4").stream().count());
    }

    @Test
    void groupingBy_test() {
        Stream<String> stream = Stream.of("apple", "apple", "pear", "banana");
        Map<String, Long> map = stream.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(map);
    }

    // map排序
    @Test
    void test_map() {
//        Map<String, Object> map = new HashMap();
        Map<String, Object> map = new LinkedHashMap();
        map.put("3", "c");
        map.put("1", "a");
        map.put("2", "b");


        System.out.println("map = " + map);

        // 按值排序 升序
        Map<String, Object> sorted = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        System.out.println("升序按值排序后的map: " + sorted);


        // 按值排序 升序
        Map<String, Object> collect = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, value -> value.getValue() + "aaa"));
        System.out.println("map改变value值 = " + collect);


        Map<String, Object> collect2 = map.entrySet().stream().map(val -> {
            val.setValue(val.getValue() + "ccc");
            return val;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("map改变value值 = " + collect2);


    }

    @Test
    void test_toMap() {

        Map<Integer, Person> map = personList.stream()
                .collect(Collectors.toMap(Person::getId,
                        Function.identity(),
                        (k1, k2) -> k1));
        System.out.println("toMap = " + map);
    }

    @Test
    void test_teeing() {

        Map<String, Person> map = personList.stream()
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.comparing(Person::getAge)),
                        Collectors.maxBy(Comparator.comparing(Person::getAge)),
                        (min, max) -> {
                            // 返回格式，
//                            return StrUtil.format("最大的={}，最小的={}", max.map(Person::getId).orElse(-1), min.map(Person::getId).orElse(-1));
                            return Map.of(
                                    "max", max.get(),
                                    "min", min.get()
                            );
                        }

                ));
        System.out.println("teeing map = " + map);
    }


    @Test
    void test11() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> list1 = list.stream()
                .skip(1)
                .limit(3)
                .toList();
        System.out.println("list1 = " + list1);
    }
}
















