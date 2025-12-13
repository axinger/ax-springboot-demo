package com.github.axinger;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;

public class Demo {

//    public static void main(String[] args) throws Exception {
//        String password = "123456";
//        //使用默认的publicKey ，建议还是使用下面的自定义
//        String encodePassword = CryptoUtils.encrypt(password);
//        System.out.println(encodePassword);
//    }

    //自定义publicKey
    public static void main(String[] args) throws Exception {
        String[] arr = CryptoUtils.genKeyPair(512);
        System.out.println("privateKey:  " + arr[0]);
        System.out.println("publicKey:  " + arr[1]);
        System.out.println("url:  " + CryptoUtils.encrypt(arr[0], "jdbc:mysql://127.0.0.1:3306/order"));
        System.out.println("username:  " + CryptoUtils.encrypt(arr[0], "root"));
        System.out.println("password:  " + CryptoUtils.encrypt(arr[0], "123456"));
    }
}
