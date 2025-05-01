package com.sh.my.design;

public class Design_4 {

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前18个模式略）

// ✅ 19. 访问者模式（Visitor）
/**
 * 概念：将数据结构与操作分离，在不改变数据结构的前提下，定义新的操作。
 * 通俗理解：给不同元素“加访问器”进行额外处理。
 */

 interface Element {
    void accept(Visitor visitor);
}

class File implements Element {
    String name;
    public File(String name) { this.name = name; }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Folder implements Element {
    String name;
    public Folder(String name) { this.name = name; }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

interface Visitor {
    void visit(File file);
    void visit(Folder folder);
}

class PrintVisitor implements Visitor {
    public void visit(File file) {
        System.out.println("访问文件: " + file.name);
    }
    public void visit(Folder folder) {
        System.out.println("访问文件夹: " + folder.name);
    }
}

// 🚀 实际应用：
// - XML/AST 树遍历
// - 编译器中语法分析器

class VisitorApp {
    public static void main(String[] args) {
        Element file = new File("文档.txt");
        Element folder = new Folder("我的文件");

        Visitor visitor = new PrintVisitor();
        file.accept(visitor);
        folder.accept(visitor);
    }
}

// ✅ 20. 状态模式（State）
/**
 * 概念：允许对象在内部状态改变时改变其行为。
 * 通俗理解：电梯在不同楼层，按钮行为不同。
 */

interface State {
    void handle();
}

class StartState implements State {
    public void handle() {
        System.out.println("开始状态，准备启动");
    }
}

class StopState implements State {
    public void handle() {
        System.out.println("停止状态，终止操作");
    }
}

class Context {
    private State state;
    public void setState(State state) {
        this.state = state;
    }
    public void request() {
        state.handle();
    }
}

// 🚀 应用场景：
// - 订单状态流转（已支付、已发货等）
// - 游戏角色状态切换

class StateApp {
    public static void main(String[] args) {
        Context ctx = new Context();
        ctx.setState(new StartState());
        ctx.request();

        ctx.setState(new StopState());
        ctx.request();
    }
}

// ✅ 21. 解释器模式（Interpreter）
/**
 * 概念：给定语言定义其文法并构建解释器解释句子。
 * 通俗理解：自定义脚本解析器，如简单数学表达式解释。
 */

interface Expression {
    int interpret();
}

class Num implements Expression {
    private int value;
    public Num(int value) { this.value = value; }
    public int interpret() { return value; }
}

class Add implements Expression {
    private Expression left, right;
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    public int interpret() {
        return left.interpret() + right.interpret();
    }
}

class Sub implements Expression {
    private Expression left, right;
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    public int interpret() {
        return left.interpret() - right.interpret();
    }
}

// 🚀 实际应用：
// - SQL、正则表达式解析器
// - 自定义规则引擎（EL表达式）

class InterpreterApp {
    public static void main(String[] args) {
        Expression exp = new Add(new Num(10), new Sub(new Num(5), new Num(2)));
        System.out.println("结果: " + exp.interpret()); // 输出 13
    }
}

