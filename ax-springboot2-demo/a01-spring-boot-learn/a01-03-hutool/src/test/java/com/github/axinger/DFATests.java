package com.github.axinger;

import cn.hutool.core.lang.Assert;
import cn.hutool.dfa.WordTree;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DFATests {

    WordTree tree = new WordTree() {{
        addWord("大");
        addWord("大土豆");
        addWord("土豆");
        addWord("刚出锅");
        addWord("出锅");

    }};
    //正文
    String text = "我有一颗大土豆，刚出锅的";

    @Test
    void test1() {

// 匹配到【大】，就不再继续匹配了，因此【大土豆】不匹配
// 匹配到【刚出锅】，就跳过这三个字了，因此【出锅】不匹配（由于【刚】首先被匹配，因此长的被匹配，最短匹配只针对第一个字相同选最短）
        List<String> matchAll = tree.matchAll(text, -1, false, false);

        System.out.println("matchAll = " + matchAll);
        Assert.equals(matchAll.toString(), "[大, 土豆, 刚出锅]");


    }
}
