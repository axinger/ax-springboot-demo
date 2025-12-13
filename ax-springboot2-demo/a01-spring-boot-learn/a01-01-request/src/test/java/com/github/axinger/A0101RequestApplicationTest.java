package com.github.axinger;

import com.github.axinger.bean.Product;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class A0101RequestApplicationTest {


    @Test
    void test() {

        /// 母店产品
        List<Product> motherStoreProducts = new java.util.ArrayList<>(List.of(
                new Product("001", "apple", 1.2),
                new Product("002", "banana", 2.3),
                new Product("003", "orange", 3.4),
                new Product("004", "pear", 4.5)
        ));

        /// 母店产品卖完的产品
        Set<String> motherStoreOutCodes = new HashSet<>(List.of("001", "004"));

        // 保存母店卖完的产品
        List<Product> motherStoreOutProducts = motherStoreProducts.stream()
                .filter(product -> motherStoreOutCodes.contains(product.getCode()))
                .collect(Collectors.toList());

        motherStoreProducts.removeIf(product -> motherStoreOutCodes.contains(product.getCode()));

        /// 母店下的子店
        List<String> sonStoreCodes = List.of("001", "002");
        // 需求2：找出母店和子店都卖完的产品
        List<Product> bothStoreOutProducts = new ArrayList<>();

        for (String sonStoreCode : sonStoreCodes) {

            ///  子店产品
            List<Product> sonStoreProducts = getSonStoreProducts(sonStoreCode);

            /// 子店卖完的产品
            Set<String> sonStoreOutCodes = getSonStoreOutCodes(sonStoreCode);

            // 需求1：如果子店没有卖完，则将母店卖完的产品重新添加到母店的产品列表中
            for (Product product : motherStoreOutProducts) {
                if (!sonStoreOutCodes.contains(product.getCode())) {
                    motherStoreProducts.add(product);
                }
            }

            // 需求2：找出母店和子店都卖完的产品
            List<Product> sonStoreOutProducts = sonStoreProducts.stream()
                    .filter(product -> sonStoreOutCodes.contains(product.getCode()))
                    .toList();

            bothStoreOutProducts.addAll(sonStoreOutProducts.stream()
                    .filter(product -> motherStoreOutCodes.contains(product.getCode()))
                    .collect(Collectors.toList()));
        }

        System.out.println("motherStoreProducts = " + motherStoreProducts.stream().sorted(Comparator.comparing(Product::getCode)).toList());
        System.out.println("bothStoreOutProducts = " + bothStoreOutProducts.stream().sorted(Comparator.comparing(Product::getCode)).toList());
    }

    List<Product> getSonStoreProducts(String sonStoreCode) {

        if ("b01".equals(sonStoreCode)) {
            /// 子店产品
            return new java.util.ArrayList<>(List.of(
                    new Product("001", "apple", 1.2),
                    new Product("002", "banana", 2.3),
                    new Product("003", "子店1orange", 3.4),
                    new Product("004", "pear", 4.5),
                    new Product("005", "grape", 5.6),
                    new Product("007", "peach", 7.8)
            ));

        } else if ("b02".equals(sonStoreCode)) {

            return new java.util.ArrayList<>(List.of(
                    new Product("001", "apple", 1.2),
                    new Product("002", "banana", 2.3),
                    new Product("003", "子店2的orange", 3.4),
                    new Product("004", "pear", 4.5),
                    new Product("005", "grape", 5.6),
                    new Product("007", "peach", 7.8)
            ));
        }

        return new java.util.ArrayList<>();
    }

    Set<String> getSonStoreOutCodes(String sonStoreCode) {
        if ("b01".equals(sonStoreCode)) {
            return new HashSet<>(List.of("002", "004"));
        } else if ("b02".equals(sonStoreCode)) {
            return new HashSet<>(List.of("003", "004"));
        }

        return new HashSet<>();
    }


    @Test
    void test2() {
        Set<String> motherStoreOutCodes = new HashSet<>(List.of("001", "004"));

        List<String> sonStoreCodes = List.of("b01", "b02");
        Set<String> commonOutCodes = null;
        for (String sonStoreCode : sonStoreCodes) {

            /// 子店卖完的产品
            Set<String> sonStoreOutCodes = getSonStoreOutCodes(sonStoreCode);

            // 找出同时存在的元素
            commonOutCodes = new HashSet<>(motherStoreOutCodes);
            commonOutCodes.retainAll(sonStoreOutCodes);

            System.out.println("Common out codes for " + sonStoreCode + ": " + commonOutCodes);
        }


        System.out.println("Common out codes for " + commonOutCodes);
    }

}
