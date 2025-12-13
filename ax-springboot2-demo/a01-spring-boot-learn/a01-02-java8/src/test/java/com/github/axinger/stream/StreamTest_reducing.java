package com.github.axinger.stream;

import com.github.axinger.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持。
public class StreamTest_reducing {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<Person>();
        personList.add(Person.builder()
                .id(1)
                .name("jim")
                .age(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .name("tom")
                .age(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .name("jack")
                .age(1)
                .build());
        // 这3个效果一样的

        // 每个员工减去起征点后的薪资之和（这个例子并不严谨，但一时没想到好的例子）
        Integer sum = personList.stream().map(Person::getAge).reduce(0, (i, j) -> (i + j + 1));
        System.out.println("员工扣税薪资总和：" + sum);

        // stream的reduce
        Optional<Integer> sum2 = personList.stream().map(val -> val.getAge() + 1).reduce(Integer::sum);
        System.out.println("员工薪资总和：" + sum2.get());

        int sum1 = personList.stream().map(val -> val.getAge() + 1).mapToInt(Integer::valueOf).sum();
        System.out.println("sum1 = " + sum1);

        Optional<String> reduce = personList.stream().map(val -> val.getName()).reduce(String::join);
        System.out.println("String::join = " + reduce);
    }
}
