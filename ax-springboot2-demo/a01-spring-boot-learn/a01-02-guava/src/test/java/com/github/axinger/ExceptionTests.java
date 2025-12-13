package com.github.axinger;

import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.Test;

public class ExceptionTests {


//    @Test
//    void test() {
//
//        Throwables.getRootCause()
//
//        String stackTrace = Throwables.getStackTraceAsString(e);
//    }

    @Test
    @Subscribe
    public void test() {
        try {
            this.showCaseThrowable1();
        } catch (InvalidInputException e) {
            System.out.println("getStackTraceAsString==" + Throwables.getStackTraceAsString(e));
            System.out.println("InvalidInputException====" + Throwables.getRootCause(e));
        } catch (Exception e) {
            System.out.println("Exception=====" + Throwables.getStackTraceAsString(e));
        }

//        try {
//            this.showCaseThrowable2();
//        } catch (Exception e) {
//            System.out.println(Throwables.getStackTraceAsString(e));
//        }
    }

    public void showCaseThrowable1() throws InvalidInputException {
        try {
            this.sqrt(-3.0);
        } catch (Exception e) {
//            Throwables.throwIfInstanceOf(e, InvalidInputException.class);
//            Throwables.propagate(e);
            String message = "错误了=" + e.getMessage();
            String message2 = "错误了=" + Throwables.getRootCause(e).getMessage();
            throw new RuntimeException("错误了=" + message + ",message2=" + message2);
        }
    }


    public void showCaseThrowable2() {
        try {
            int[] arr = {1, 2, 3};
            getValue(arr, 4);
        } catch (Throwable e) {
            //Throwable类型为X才抛出
            Throwables.throwIfInstanceOf(e, IndexOutOfBoundsException.class);
            //把throwable包装成RuntimeException，用该方法保证异常传递，抛出一个RuntimeException异常.
            Throwables.propagate(e);
        }
    }

    //获取平方根
    public double sqrt(double input) throws InvalidInputException {
        if (input < 0) throw new InvalidInputException("获取平方根失败");
        return Math.sqrt(input);
    }

    //获取数组索引值
    public double getValue(int[] arr, int index) throws IndexOutOfBoundsException {
        return arr[index];
    }

    //自定义异常类
    class InvalidInputException extends Throwable {
        public InvalidInputException() {

        }

        public InvalidInputException(String message) {
            super(message);
        }
    }

}
