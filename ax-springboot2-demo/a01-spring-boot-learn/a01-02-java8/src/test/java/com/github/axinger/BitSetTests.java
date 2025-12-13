package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

// java.util.BitSet，字面意思是位集合，含义是位图，
public class BitSetTests {


    @Test
    public void testBitSet() {
        //创建与清除
        BitSet bit1 = new BitSet(20);
        bit1.set(0);
        bit1.set(1);
        bit1.set(3);
        bit1.set(4); // 将索引为10的位设置为1
        System.out.println("bit1 = " + bit1); // 只会打印位上为true的下标
        bit1.clear(1);  // 将索引为1的位设置为0,
        System.out.println("bit1 = " + bit1);
        boolean bitValue = bit1.get(10); // 获取索引为10的位的值
        System.out.println("bitValue = " + bitValue);

        bit1.flip(5); // 如果索引为5的位是0，则变为1；如果是1，则变为0
        System.out.println("bit1 = " + bit1);

    }

    @Test
    public void testBitSet2() {
        //一个BitSet中删除另一个BitSet中的所有位。
        BitSet bit1 = new BitSet(16);
        BitSet bit2 = new BitSet(16);
        bit1.set(1);
        bit1.set(2);

        bit2.set(2);
        bit1.andNot(bit2);

        System.out.println("bit1 = " + bit1);
    }

    @Test
    public void testBitSet3() {
        //高效位操作
        //创建了一个包含1000万个位的BitSet
        BitSet bigInt = new BitSet();
        System.out.println("bigInt = " + bigInt);
//设置第900000位为1
        bigInt.set(9000000);
        System.out.println("bigInt = " + bigInt);
    }

    @Test
    public void testBitSet4() {

        //将两个BitSet进行按位或操作
        BitSet bitSet1 = new BitSet();
        BitSet bitSet2 = new BitSet();
        bitSet1.set(0);
        bitSet2.set(1);
        bitSet1.or(bitSet2);

        System.out.println("bitSet1 = " + bitSet1);
    }

    @Test
    public void testBitSet5() {

//        将两个BitSet进行按位与操作.

        BitSet bitSet1 = new BitSet();
        BitSet bitSet2 = new BitSet();
        bitSet1.set(0);

        bitSet2.set(0);
        bitSet2.set(1);
        bitSet1.and(bitSet2); // 2个0位上都是true, 1位置上只有一个是true, 所有打印就只有下标0的


        System.out.println("bitSet1 = " + bitSet1);

        List<Integer> queue = new LinkedList<>();
        queue.add(1);
        queue.add(2);
//        输出：[1, 2]
    }
}
