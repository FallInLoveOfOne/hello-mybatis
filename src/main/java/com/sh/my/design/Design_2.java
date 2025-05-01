package com.sh.my.design;

import java.util.ArrayList;
import java.util.List;

public class Design_2 {

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前12个模式略）

// ✅ 13. 适配器模式（Adapter）
/**
 * 概念：将一个接口转换成客户希望的另一个接口，解决接口不兼容问题。
 * 通俗理解：插头转换器——日本电器接中国插座。
 */

 interface Target {
    void request();
}

class Adaptee {
    public void specificRequest() {
        System.out.println("被适配者的特殊请求");
    }
}

class Adapter implements Target {
    private Adaptee adaptee;
    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }
    public void request() {
        adaptee.specificRequest();
    }
}

// 🚀 实际项目应用：
// - Spring AOP 中的 MethodInterceptor -> InvocationHandler
// - JDBC 与数据库驱动适配

class AdapterApp {
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target target = new Adapter(adaptee);
        target.request();
    }
}

// ✅ 14. 迭代器模式（Iterator）
/**
 * 概念：提供一种方法顺序访问一个聚合对象中的元素，而不暴露其内部表示。
 * 通俗理解：用遥控器翻频道，不需要知道电视怎么存频道。
 */

interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface Aggregate<T> {
    Iterator<T> createIterator();
}

class NameRepository implements Aggregate<String> {
    private String[] names = {"Tom", "Jerry", "Spike"};

    public Iterator<String> createIterator() {
        return new NameIterator();
    }

    private class NameIterator implements Iterator<String> {
        int index = 0;
        public boolean hasNext() { return index < names.length; }
        public String next() { return names[index++]; }
    }
}

// 🚀 实际项目应用：
// - Java 集合中的 Iterator 接口
// - 自定义集合结构封装迭代行为

class IteratorApp {
    public static void main(String[] args) {
        NameRepository repo = new NameRepository();
        Iterator<String> iter = repo.createIterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}

// ✅ 15. 组合模式（Composite）
/**
 * 概念：将对象组合成树形结构以表示部分-整体层次结构，使客户端对单个对象和组合对象的使用具有一致性。
 * 通俗理解：文件夹可以包含文件和子文件夹，统一对待。
 */

abstract class ComponentNode {
    protected String name;
    public ComponentNode(String name) { this.name = name; }
    public abstract void display(String prefix);
}

class LeafNode extends ComponentNode {
    public LeafNode(String name) { super(name); }
    public void display(String prefix) {
        System.out.println(prefix + "- " + name);
    }
}

class CompositeNode extends ComponentNode {
    private List<ComponentNode> children = new ArrayList<>();
    public CompositeNode(String name) { super(name); }
    public void add(ComponentNode node) { children.add(node); }
    public void display(String prefix) {
        System.out.println(prefix + "+ " + name);
        for (ComponentNode child : children) {
            child.display(prefix + "  ");
        }
    }
}

// 🚀 实际项目应用：
// - DOM 节点树结构
// - 文件目录树
// - Spring BeanDefinition 中属性节点树

class CompositeApp {
    public static void main(String[] args) {
        CompositeNode root = new CompositeNode("根目录");
        CompositeNode folder1 = new CompositeNode("文件夹A");
        LeafNode file1 = new LeafNode("文件1.txt");
        LeafNode file2 = new LeafNode("文件2.txt");
        folder1.add(file1);
        folder1.add(file2);
        root.add(folder1);
        root.display("");
    }
}

