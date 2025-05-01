package com.sh.my.design;

public class Design_0 {

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
