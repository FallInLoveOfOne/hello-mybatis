package com.sh.my.design;

import java.util.ArrayList;
import java.util.List;

public class Design_3 {

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前15个模式略）

// ✅ 16. 观察者模式（Observer）
/**
 * 概念：对象间一对多依赖关系，一个对象状态变更，所有依赖它的对象都会收到通知。
 * 通俗理解：公众号订阅，推送新消息给订阅者。
 */

 interface Observer {
    void update(String message);
}

interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}

class WeChatOfficialAccount implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String message;

    public void attach(Observer o) { observers.add(o); }
    public void detach(Observer o) { observers.remove(o); }
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(message);
        }
    }

    public void publish(String msg) {
        this.message = msg;
        notifyObservers();
    }
}

class User implements Observer {
    private String name;
    public User(String name) { this.name = name; }
    public void update(String message) {
        System.out.println(name + " 收到消息: " + message);
    }
}

// 🚀 项目应用：
// - 事件系统：GUI按钮监听、Java EventListener
// - Spring ApplicationEventPublisher

class ObserverApp {
    public static void main(String[] args) {
        WeChatOfficialAccount account = new WeChatOfficialAccount();
        Observer u1 = new User("小明");
        Observer u2 = new User("小红");
        account.attach(u1);
        account.attach(u2);
        account.publish("Java 23种设计模式上线了！");
    }
}

// ✅ 17. 门面模式（Facade）
/**
 * 概念：为子系统提供统一的接口，隐藏系统复杂性，对外提供简化访问方式。
 * 通俗理解：电脑一键开机，背后调 CPU、硬盘、电源等子系统。
 */

class CPU {
    public void start() { System.out.println("CPU 启动"); }
}

class Memory {
    public void load() { System.out.println("内存加载"); }
}

class Disk {
    public void read() { System.out.println("硬盘读取"); }
}

class ComputerFacade {
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private Disk disk = new Disk();

    public void start() {
        System.out.println("---- 开机流程 ----");
        cpu.start();
        memory.load();
        disk.read();
        System.out.println("---- 启动完成 ----");
    }
}

// 🚀 应用场景：
// - Controller 调用多个 Service 聚合数据
// - Spring 的 JdbcTemplate 封装底层数据库访问逻辑

class FacadeApp {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.start();
    }
}

// ✅ 18. 备忘录模式（Memento）
/**
 * 概念：保存对象的某个状态，以便之后恢复。
 * 通俗理解：游戏存档，失败时读档回到之前状态。
 */

class Editor {
    private String content;
    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }
    public Memento save() { return new Memento(content); }
    public void restore(Memento m) { this.content = m.getContent(); }
}

class Memento {
    private final String content;
    public Memento(String content) { this.content = content; }
    public String getContent() { return content; }
}

class Caretaker {
    private Memento memento;
    public void save(Memento m) { this.memento = m; }
    public Memento retrieve() { return memento; }
}

// 🚀 应用场景：
// - IDE 撤销（Ctrl+Z）功能
// - 数据库事务回滚

class MementoApp {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.setContent("初始内容");

        Caretaker caretaker = new Caretaker();
        caretaker.save(editor.save());

        editor.setContent("修改后的内容");
        System.out.println("当前内容: " + editor.getContent());

        editor.restore(caretaker.retrieve());
        System.out.println("恢复内容: " + editor.getContent());
    }
}

