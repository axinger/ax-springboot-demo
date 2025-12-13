package com.github.axinger.reactor8;

import java.util.Observable;


public class ObserverDemo extends Observable {


    public static void main(String[] args) {
        ObserverDemo demo = new ObserverDemo();
        /// 添加观察者
        demo.addObserver((o, arg) -> {
            System.out.println("发生了变换................");
        });
        demo.addObserver((o, arg) -> {
            System.out.println("发生了变换😝😝😝😝😝😝😝😝😝😝😝😝😝");
        });

        demo.setChanged();// 数据变换
        demo.notifyObservers();// 通知
    }
}
