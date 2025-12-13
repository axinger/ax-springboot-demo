package com.github.axinger;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TreeTests {
    @Test
    void test() {
//        // 构建node列表
//        List<TreeNode<Integer>> nodeList = CollUtil.newArrayList();
//
//        nodeList.add(new TreeNode<>(1, 0, "系统管理", 5));
//        nodeList.add(new TreeNode<>(11, 1, "用户管理", 222222));
//        nodeList.add(new TreeNode<>(111, 11, "用户添加", 0));
//        nodeList.add(new TreeNode<>(2, 0, "店铺管理", 1));
//        nodeList.add(new TreeNode<>(21, 2, "商品管理", 44));
//        nodeList.add(new TreeNode<>(221, 2, "商品管理2", 2));
//
//        for (TreeNode<Integer> node : nodeList) {
//            System.out.println("node = " + node.getId());
//            System.out.println("node.getExtra() = " + node.getExtra());
//            System.out.println("node.getWeight() = " + node.getWeight());
//            System.out.println("node.getName() = " + node.getName());
//        }
//
//
//        // 0表示最顶层的id是0
//        List<Tree<Integer>> treeList = TreeUtil.build(nodeList, "0");


        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

//        nodeList.add(new TreeNode<>("0", "-1", "菜单栏", 0));

        {
            TreeNode<String> node = new TreeNode<>("1", "0", "系统管理", 5);
            node.setExtra(Map.of("extraField", 666));
            nodeList.add(node);
        }
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));

        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));

        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

// 0表示最顶层的id是0
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
//        System.out.println("treeList = " + treeList);


        Tree<String> stringTree = TreeUtil.buildSingle(nodeList, "0");
        System.out.println("stringTree = " + stringTree);


        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
// 自定义属性名 都有默认值的
//        treeNodeConfig.setWeightKey("order");
//        treeNodeConfig.setIdKey("rid");
//// 最大递归深度
//        treeNodeConfig.setDeep(5);

//转换器 (含义:找出父节点为字符串零的所有子节点, 并递归查找对应的子节点, 深度最多为 3)
        List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    // 扩展属性 ...
                    tree.putExtra("extraField", 666);
                    tree.putExtra("other", new Object());
                });

//        System.out.println("treeNodes = " + treeNodes);


        for (Tree<String> tree : treeNodes) {

            System.out.println("id=" + tree.getId() + ",name=" + tree.getName() + ",tree.getChildren() = " + tree.getChildren());
        }


    }
}
