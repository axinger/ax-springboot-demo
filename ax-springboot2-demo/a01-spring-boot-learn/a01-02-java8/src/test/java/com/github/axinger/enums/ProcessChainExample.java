package com.github.axinger.enums;

public class ProcessChainExample {
    public static void main(String[] args) {
        Process currentStep = Process.收货;
        int a = 5;
        int b = 3;

        currentStep = currentStep.next(a, b);

        System.out.println("currentStep.previous() = " + currentStep.previous());

        System.out.println("currentStep = " + currentStep);

//        while (currentStep != null) {
//            System.out.println("Current Step: " + currentStep);
//            System.out.println("上一个: " + currentStep.previous());
//            int result = currentStep.step(a, b);
//            System.out.println("Result: " + result);
//            ProcessItem.setCurrentStep(currentStep);
//            currentStep = currentStep.next(a, b);
//        }
    }
}
