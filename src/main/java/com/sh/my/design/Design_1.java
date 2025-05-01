package com.sh.my.design;

public class Design_1 {

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前9个模式略）

// ✅ 10. 责任链模式（Chain of Responsibility）
/**
 * 概念：使多个对象都有机会处理请求，从而避免请求发送者与接收者之间的耦合。
 * 通俗理解：请假审批——班主任批不了找年级主任，逐级上报。
 */

 abstract class Handler {
    protected Handler next;
    public void setNext(Handler next) { this.next = next; }
    public abstract void handleRequest(int level);
}

class GroupLeader extends Handler {
    public void handleRequest(int level) {
        if (level <= 1) {
            System.out.println("组长批准");
        } else if (next != null) {
            next.handleRequest(level);
        }
    }
}

class Manager extends Handler {
    public void handleRequest(int level) {
        if (level <= 2) {
            System.out.println("经理批准");
        } else if (next != null) {
            next.handleRequest(level);
        }
    }
}

class Boss extends Handler {
    public void handleRequest(int level) {
        if (level <= 3) {
            System.out.println("老板批准");
        } else {
            System.out.println("申请被拒绝");
        }
    }
}

// 🚀 应用场景：
// - Servlet Filter、Spring Interceptor
// - 异常处理链
// - 权限认证链

class ChainApp {
    public static void main(String[] args) {
        Handler leader = new GroupLeader();
        Handler manager = new Manager();
        Handler boss = new Boss();
        leader.setNext(manager);
        manager.setNext(boss);

        leader.handleRequest(2);
    }
}

// ✅ 11. 装饰器模式（Decorator）
/**
 * 概念：动态地给对象添加职责，不改变其结构。
 * 通俗理解：手机壳、贴膜是在不改手机的基础上加功能。
 */

interface Component {
    void operation();
}

class ConcreteComponent implements Component {
    public void operation() {
        System.out.println("基础操作");
    }
}

abstract class Decorator implements Component {
    protected Component component;
    public Decorator(Component component) {
        this.component = component;
    }
}

class LoggingDecorator extends Decorator {
    public LoggingDecorator(Component component) {
        super(component);
    }
    public void operation() {
        System.out.println("日志记录开始...");
        component.operation();
        System.out.println("日志记录结束");
    }
}

// 🚀 实际项目应用：
// - Java IO（BufferedReader 包装 FileReader）
// - Spring AOP（增强功能）

class DecoratorApp {
    public static void main(String[] args) {
        Component core = new ConcreteComponent();
        Component decorated = new LoggingDecorator(core);
        decorated.operation();
    }
}

// ✅ 12. 策略模式（Strategy）
/**
 * 概念：定义一系列算法，把它们一个个封装起来，并且使它们可以互相替换。
 * 通俗理解：打车不同支付方式（支付宝/微信），算法不同但都能“付款”。
 */

interface PaymentStrategy {
    void pay(int amount);
}

class AliPay implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("使用支付宝支付: " + amount);
    }
}

class WeChatPay implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("使用微信支付: " + amount);
    }
}

class PaymentContext {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    public void execute(int amount) {
        strategy.pay(amount);
    }
}

// 🚀 实际项目应用：
// - Comparator 排序策略
// - Spring Bean 中 @ConditionalOnProperty 决定策略
// - 营销系统中多种优惠策略

class StrategyApp {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();
        context.setStrategy(new AliPay());
        context.execute(100);
    }
}

