package com.github.axinger.enums;

public enum Step implements StepItem {
    STEP1 {
        @Override
        public int step(int a, int b) {
            return a + b;
        }

        @Override
        public Step next() {
            return STEP2;
        }
    },
    STEP2 {
        @Override
        public int step(int a, int b) {
            return a * b;
        }

        @Override
        public Step next() {
            return STEP3;
        }
    },
    STEP3 {
        @Override
        public int step(int a, int b) {
            return a - b;
        }

        @Override
        public Step next() {
            return null; // 或者返回 STEP1 以形成循环
        }
    };

    public abstract int step(int a, int b);

    public abstract Step next();
}

interface StepItem {
    int step(int a, int b);

    Step next();
}
