package com.github.axinger.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("circularFilter") // 声明过滤器
public class Node {
    // getters 和 setters
    private String name;

//    @JsonManagedReference → 指向 要序列化 的关联（通常是集合端）
//
//    @JsonBackReference → 指向 不序列化 的关联（通常是指向父级的引用）

//    @JsonManagedReference("node-parent") // 指定引用名称
//    private Node parent;
//
//    @JsonBackReference("node-parent") // 必须与@JsonManagedReference名称匹配
//    private List<Node> children = new ArrayList<>();


//    @JsonBackReference  // 子→父引用（不序列化）
//    private Node parent;
//
//    @JsonManagedReference  // 父→子引用（序列化）
//    private List<Node> children = new ArrayList<>();


    @JsonBackReference// ← 子节点指向父节点（不序列化）
    private Node parent;

    @JsonManagedReference // ← 父节点指向子节点（序列化）
    private List<Node> children = new ArrayList<>();

    // 构造方法
    public Node(String name) {
        this.name = name;
    }

    public void addChild(Node child) {
        children.add(child);
        child.parent = this;
    }
}
