package com.github.axinger.enums;

public enum Process implements ProcessItem {
    收货 {
        @Override
        public Process next(int a, int b) {
            // 根据计算结果决定下一个步骤
            if (a + b > 10) {
                return 检验;
            } else {
                System.out.println("this = " + this);

                previousStep = this;
                return 打包;
            }
        }
    },
    检验 {
        @Override
        public Process next(int a, int b) {
            // 根据计算结果决定下一个步骤
            if (a * b > 20) {
                return 打包;
            } else {
                return 发货;
            }
        }
    },
    打包 {
        @Override
        public Process next(int a, int b) {
            // 根据计算结果决定下一个步骤
            if (a - b > 0) {
                return 发货;
            } else {
                return 完成;
            }
        }
    },
    发货 {
        @Override
        public Process next(int a, int b) {
            // 根据计算结果决定下一个步骤
            if (a / b > 1) {
                return 完成;
            } else {
                return 打包;
            }
        }
    },
    完成 {
        @Override
        public Process next(int a, int b) {
            // 完成步骤没有下一个步骤
            return null;
        }
    };

    private static Process previousStep = null;

    @Override
    public int step(int a, int b) {
        // 执行具体的步骤操作
        switch (this) {
            case 收货:
                return a + b;
            case 检验:
                return a * b;
            case 打包:
                return a - b;
            case 发货:
                return a / b;
            case 完成:
                return 0; // 或者其他适当的值
            default:
                throw new IllegalStateException("Unknown process step");
        }
    }

    @Override
    public Process previous() {
        // 返回上一个步骤
        return previousStep;
    }

    @Override
    public abstract Process next(int a, int b);


}

interface ProcessItem {
    int step(int a, int b);

    Process previous();

    Process next(int a, int b);
}
