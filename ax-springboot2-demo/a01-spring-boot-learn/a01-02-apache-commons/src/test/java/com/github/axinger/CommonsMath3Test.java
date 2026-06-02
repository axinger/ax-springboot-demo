package com.github.axinger;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Apache Commons Math3 数学计算示例
 */
class CommonsMath3Test {

    // ==================== 描述统计 ====================

    @Test
    void testDescriptiveStatistics() {
        double[] data = {12.5, 15.0, 18.5, 20.0, 22.5, 25.0, 100.0}; // 含异常值

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double d : data) {
            stats.addValue(d);
        }

        System.out.println("数据个数 (N): " + stats.getN());                    // 7
        System.out.println("最小值 (Min): " + stats.getMin());                  // 12.5
        System.out.println("最大值 (Max): " + stats.getMax());                  // 100.0
        System.out.println("平均值 (Mean): " + stats.getMean());                // ~30.5
        System.out.println("中位数 (Median): " + stats.getPercentile(50));      // 20.0
        System.out.println("标准差 (StdDev): " + stats.getStandardDeviation()); // ~31.0
        System.out.println("方差 (Variance): " + stats.getVariance());          // ~961.9
        System.out.println("几何平均: " + stats.getGeometricMean());
        System.out.println("求和 (Sum): " + stats.getSum());                    // 213.5
        System.out.println("平方和: " + stats.getSumsq());
        System.out.println("偏度 (Skewness): " + stats.getSkewness());          // 偏度
        System.out.println("峰度 (Kurtosis): " + stats.getKurtosis());          // 峰度

        // 百分位数
        System.out.println("25% 分位数: " + stats.getPercentile(25));
        System.out.println("75% 分位数: " + stats.getPercentile(75));
        System.out.println("90% 分位数: " + stats.getPercentile(90));
    }

    @Test
    void testIncrementalStatistics() {
        // 增量式统计（流式数据场景）
        DescriptiveStatistics stats = new DescriptiveStatistics(5); // 只保留最近5个值

        stats.addValue(10);
        stats.addValue(20);
        stats.addValue(30);
        System.out.println("3个值的均值: " + stats.getMean()); // 20.0

        stats.addValue(40);
        stats.addValue(50);
        System.out.println("5个值的均值: " + stats.getMean()); // 30.0

        stats.addValue(60); // 10 被移除
        System.out.println("最新5个值的均值: " + stats.getMean()); // 40.0
    }

    // ==================== 单独统计量计算 ====================

    @Test
    void testIndividualStatistics() {
        double[] data = {10.0, 20.0, 30.0, 40.0, 50.0};

        StandardDeviation stdDev = new StandardDeviation();
        System.out.println("标准差: " + stdDev.evaluate(data)); // ~15.81

        Variance variance = new Variance();
        System.out.println("方差: " + variance.evaluate(data)); // ~250.0

        // 总体标准差（除以 N）vs 样本标准差（除以 N-1）
        StandardDeviation populationStdDev = new StandardDeviation(false);
        System.out.println("总体标准差: " + populationStdDev.evaluate(data));
    }

    // ==================== 线性回归 ====================

    @Test
    void testSimpleRegression() {
        SimpleRegression regression = new SimpleRegression();

        // 添加数据点 (x, y)
        regression.addData(1.0, 2.0);
        regression.addData(2.0, 3.0);
        regression.addData(3.0, 5.0);
        regression.addData(4.0, 6.0);
        regression.addData(5.0, 8.0);

        // 回归方程: y = intercept + slope * x
        System.out.println("截距 (Intercept): " + regression.getIntercept());       // ~0.2
        System.out.println("斜率 (Slope): " + regression.getSlope());               // ~1.5
        System.out.println("R² (决定系数): " + regression.getRSquare());            // ~0.98
        System.out.println("回归标准误差: " + regression.getRegressionSumSquares());

        // 预测
        double predictX = 6.0;
        double predictedY = regression.predict(predictX);
        System.out.println("当 x=" + predictX + " 时，预测 y=" + predictedY); // ~9.2

        // 批量添加
        SimpleRegression regression2 = new SimpleRegression();
        double[][] data = {
                {1, 2}, {2, 4}, {3, 6}, {4, 8}
        };
        regression2.addData(data);
        System.out.println("批量数据斜率: " + regression2.getSlope()); // 2.0
    }

    // ==================== 组合数学 ====================

    @Test
    void testCombinatorics() {
        // 阶乘
        long factorial5 = CombinatoricsUtils.factorial(5);
        System.out.println("5! = " + factorial5); // 120

        long factorial10 = CombinatoricsUtils.factorial(10);
        System.out.println("10! = " + factorial10); // 3628800

        // 排列数 A(n, k) = n! / (n-k)!
        long permutation = CombinatoricsUtils.factorial(5) / CombinatoricsUtils.factorial(5 - 3);
        System.out.println("A(5,3) = " + permutation); // 60

        // 组合数 C(n, k) = n! / (k! * (n-k)!)
        long combination = CombinatoricsUtils.factorial(10)
                / (CombinatoricsUtils.factorial(3) * CombinatoricsUtils.factorial(10 - 3));
        System.out.println("C(10,3) = " + combination); // 120

        // 二项式系数
        long binomial = CombinatoricsUtils.factorial(20)
                / (CombinatoricsUtils.factorial(5) * CombinatoricsUtils.factorial(15));
        System.out.println("C(20,5) = " + binomial); // 15504
    }

    // ==================== 数组统计工具 ====================

    @Test
    void testArrayStatistics() {
        double[] data = {5.0, 2.0, 8.0, 1.0, 9.0, 3.0};

        // 排序
        double[] sorted = data.clone();
        Arrays.sort(sorted);
        System.out.println("排序后: " + Arrays.toString(sorted)); // [1.0, 2.0, 3.0, 5.0, 8.0, 9.0]

        // 使用统计类计算
        DescriptiveStatistics stats = new DescriptiveStatistics(data);
        System.out.println("均值: " + stats.getMean());
        System.out.println("中位数: " + stats.getPercentile(50));
        System.out.println("极差: " + (stats.getMax() - stats.getMin())); // 9.0 - 1.0 = 8.0
    }
}
