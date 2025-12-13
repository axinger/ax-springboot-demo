package com.github.axinger.enums;

public class StepTests {
    public static void main(String[] args) {
        Step currentStep = Step.STEP1;

        while (currentStep != null) {
            System.out.println("Current Step: " + currentStep);
            System.out.println("Result: " + currentStep.step(1, 1));
            currentStep = currentStep.next();
        }
    }
}
