package com.github.axinger;

import com.github.axinger.model.ImmutablePerson;
import org.junit.Test;

public class WithTests {

    @Test
    public void test1() {
        ImmutablePerson person = new ImmutablePerson("jim", 20);

        System.out.println(System.identityHashCode(person));
        System.out.println("person = " + person);

        System.out.println("person.getName() = " + person.getName());


        // 新对象
        ImmutablePerson person1 = person.withAge(21);


        System.out.println("person1 = " + person1);
        System.out.println(Integer.toHexString(System.identityHashCode(person1)));


        // 新对象
        ImmutablePerson person2 = person1.withName("tom");
        System.out.println("person2 = " + person2);
        System.out.println(Integer.toHexString(System.identityHashCode(person2)));
    }
}
