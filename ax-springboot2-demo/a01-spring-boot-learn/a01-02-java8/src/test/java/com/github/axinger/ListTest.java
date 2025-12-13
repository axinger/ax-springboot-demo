package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListTest {


    @Test
    void test1() {
//
//        Arrays.asList(1,2,3,4).stream().forEach(val->{
//
//            if (val ==2){
//                return;
//            }
//            System.out.println("val = " + val);
//        });

        for (Integer integer : Arrays.asList(1, 2, 3, 4)) {
            System.out.println("val = " + integer);
            if (integer == 2) {
                break;
            }
        }

        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(null);
        list.add(null);
        System.out.println("list = " + list);


        List<Object> list2 = new ArrayList<>(Collections.nCopies(3, null));
        System.out.println("list2 = " + list2);


        List<Object> list3 = IntStream.range(0, 3)
                .mapToObj(i -> null)
                .toList();
        System.out.println("list3 = " + list3);
    }

    @Test
    public void test11() {

        /*
        增加：
        add(E e)：在链表后添加一个元素；
        addFirst(E e)：在链表头部插入一个元素；
        addLast(E e)：在链表尾部添加一个元素；


        删除：
        remove() ：移除链表中第一个元素;
        remove(index i)：移除指定元素；
        pop()：删除第一个元素
        poll()：返回第一个元素并移除

        查：
        get(int index)：按照下标获取元素；
        peek()：获取第一个元素，但是不移除；
        peekFirst()：获取第一个元素，但是不移除；
        peekLast()：获取最后一个元素，但是不移除；
        pollFirst()：查询并删除头；
        pollLast()：删除尾；
        poll()：查询并移除第一个元素
        */

        /*
        增加：
        add(E e)：在链表后添加一个元素；
        addFirst(E e)：在链表头部插入一个元素；
        addLast(E e)：在链表尾部添加一个元素；
         */
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);  //末尾添加   1
        list.addLast(2);  //末尾添加  1，2
        list.addFirst(3);  //头部添加   3，1，2
        System.out.println(list.toString());   //3，1，2
        System.out.println("------------------------------------------------------------------------------------------------------------------");


        /*
        删除：
        remove() ：移除链表中第一个元素;
        remove(index i)：移除指定元素；
        pop()：删除第一个元素
        poll()：返回第一个元素并移除
         */
        LinkedList<Integer> list2 = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list2.add(i + 1);
        }
        System.out.println("当前元素有: " + list2.toString());//1,2,3,4,5,6,7,8,9,10

        list2.remove();   //删除第一个元素
        System.out.println("删除第一个元素后，链表为：" + list2.toString());   //2，3,4,5,6,7,8,9,10

        list2.remove(2);   //删除索引为2的元素
        System.out.println("删除索引为" + 2 + "的元素后，链表为：" + list2.toString());  //2，3,5,6,7,8,9,10

        list2.pop();
        System.out.println("删除第一个元素后，链表为：" + list2.toString());   //3,5,6,7,8,9,10

        System.out.println("删除第一个元素后，链表为：" + list2.toString());   //5,6,7,8,9,10
        System.out.println("删除元素为" + list2.poll());
        System.out.println("------------------------------------------------------------------------------------------------------------------");


        /*
        查：
        get(int index)：按照下标获取元素；
        peek()：获取第一个元素，但是不移除；
        peekFirst()：获取第一个元素，但是不移除；
        peekLast()：获取最后一个元素，但是不移除；
        pollFirst()：查询并删除头；
        pollLast()：删除尾；
        poll()：查询并移除第一个元素
         */
        LinkedList<Integer> list3 = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            list3.add(i + 1);
        }
        System.out.println("当前元素有: " + list3.toString());//1,2,3,4,5,6,7,8,9,10
        System.out.println("获取下标为3的元素:" + list3.get(3));  //4

        System.out.println("使用peek()获取第一个元素，但不删除,获取的元素为:" + list3.peek());  //1
        System.out.println("使用peek()后链表为：" + list3.toString());  //1,2,3,4,5,6,7,8,9,10

        System.out.println("使用peekFirst()获取第一个元素:" + list3.peekFirst());   //1
        System.out.println("使用peekLast()获取最后一个元素" + list3.peekLast());   //10
        System.out.println("使用peekFirst()和peekLast后链表为:" + list3.toString());   //1,2,3,4,5,6,7,8,9,10

        System.out.println("使用poll()查看并删除第一个元素，查看元素为" + list3.poll());    //1
        System.out.println("使用pollLast()查看并删除最后一个元素，查看元素为" + list3.pollLast());   //10
        System.out.println("使用poll()和pollLast()后链表为:" + list3.toString());    //2,3,4,5,6,7,8,9

    }

    @Test
    void test2() {
        List<String> list = new ArrayList<>();
        System.out.println("list.get(0) = " + list.get(0));
    }


    @Test
    void test3() {
        List<Integer> list = List.of(1, 3, 2, 4);

        System.out.println("list.stream().unordered() = " + list.stream().unordered().toList());
        ;
        System.out.println("list.stream().sorted() = " + list.stream().sorted().toList());
    }

    @Test
    void test4() {
        List<Integer> list = List.of(1, 3, 2, 4);
        System.out.println("list.indexOf(5) = " + list.indexOf(5));
    }

    @Test
    void test5() {

        User user1 = new User();
        user1.setId(1);
        user1.setName("jim");

        User user2 = new User();
        user2.setId(1);
        user2.setName("tom");
        List<User> list = List.of(user1, user2);

        // Map<Integer, User> maps2 = list.stream().collect(Collectors.toMap(User::getId,Function.identity()));

        // 另外，转换成map的时候，可能出现key一样的情况，如果不指定一个覆盖规则，上面的代码是会报错的。转成map的时候，最好使用下面的方式：
        Map<Integer, User> map = list.stream().collect(Collectors.toMap(User::getId, Function.identity(), (key1, key2) -> key1));
        System.out.println("map = " + map);

        Map<Integer, String> collect = list.stream().collect(Collectors.toMap(User::getId, User::getName, (key1, key2) -> key2));

        System.out.println("collect = " + collect);
    }

    @Test
    @SuppressWarnings("all")
    void test6() {
        /// 不可以
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        for (Integer i : list) {
            if (i == 1) { // 1不可用,2可以
                list.remove(i);
            }
        }
        System.out.println("list = " + list);
    }

    @Test
    void test7() {
        /// 可以
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.removeIf(i -> i == 1);
        System.out.println("list = " + list);
    }

    @Test
    @SuppressWarnings("all")
    void test8() {
        /// 可以, 建议使用removeIf
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if (next == 0) {
                iterator.remove();
            }
        }
        System.out.println("list = " + list);
    }

    @Test
    void test9() {
        /// 不可以
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(2);
        list.add(1);
//        for (int i = 0; i < list.size(); i++) {
//            if (i == 0) {
//                Integer obj = list.get(i);
//                list.remove(obj);
//            }
//        }
//        System.out.println("list = " + list);

        /// 不可以
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i) == 2) {  // 删除所有等于 2 的元素
//                list.remove(i);      // 删除后，后面的元素会左移，但 i 仍然递增，可能跳过检查某些元素
//            }
//        }
//        System.out.println("list = " + list);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 2) {
                list.remove(i);
                i--;  // 调整索引，防止跳过下一个元素
            }
        }
        System.out.println("list = " + list);
    }


    @Test
    void test10() {

        User user1 = new User();
        user1.setId(2);
        user1.setName("A");

        User user2 = new User();
        user2.setId(1);
        user2.setName("B");

        User user3 = new User();
        user3.setId(2);
        user3.setName("C");


        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        System.out.println("list = " + list);

        list.sort(Comparator.comparing(User::getId));
        System.out.println("list = " + list);

        //reversed 倒序 .reversed()  naturalOrder升序,reverseOrder降序
        list.sort(Comparator.comparing(User::getId, Comparator.naturalOrder()).thenComparing(User::getName, Comparator.reverseOrder()));
        System.out.println("reversed list = " + list);


        Collections.sort(list, Comparator.comparing(User::getName));
        System.out.println("list = " + list);

        User user = list.stream().findFirst().orElse(null);
        System.out.println("user = " + user);
    }
}
