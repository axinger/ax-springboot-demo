package com.github.axinger._02lock;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * list 不安全演示
 */
public class ThreadDemo3 {
    public static void main(String[] args) {

        /** 这里会报错,线程不安全*/
//        List<String> list = new ArrayList<>();

        /** 方式1: Vector 这个安全的,1.0方式,不常用*/
//        List<String> list = new Vector<>();


        /** 方式2: Collections工具类 这个安全的式,不常用*/
//        List<String> list = Collections.synchronizedList(new ArrayList<>());

        /** 方式3: CopyOnWriteArrayList 这个安全的式,常用
         * 写时复制技术: 并发读,独立写
         * 签到表例子,第一次 复制一份,同学查看,老师在副本中再写入,合并
         * */
        List<String> list = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 30; i++) {
            int finalI = i;
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println("list = " + list);
                System.out.println("list.count = " + list.stream().count());
                System.out.println("list.size = " + list.size());
            }, String.valueOf(i)).start();
        }

        /**HashSet 不安全*/
//        Set set = new HashSet();
        /**CopyOnWriteArraySet 安全*/
        Set set = new CopyOnWriteArraySet();

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 5));
                System.out.println("set = " + set);
            }, String.valueOf(i)).start();
        }

        /**map线程安全: ConcurrentHashMap 安全*/
        Map map = new ConcurrentHashMap();


    }
}
