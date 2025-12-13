package com.github.axinger;


import com.github.axinger.model.Address;
import org.junit.Test;

public class NonNullTests {

    public void getName(@lombok.NonNull String name) {

        System.out.println("name = " + name);
    }

    @Test
    public void test() {
        getName(null);
    }

    @Test
    public void test1() {
        Address address = new Address();
        address.setStreet(null);
        System.out.println("address = " + address);

        Address address1 = Address.of("中国", "北京");
        System.out.println("address1 = " + address1);
    }
}
