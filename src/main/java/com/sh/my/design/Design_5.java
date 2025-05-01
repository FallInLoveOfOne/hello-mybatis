package com.sh.my.design;

import java.util.HashMap;
import java.util.Map;

public class Design_5 {

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前21个模式略）

// ✅ 22. 享元模式（Flyweight）
/**
 * 概念：运用共享技术有效支持大量细粒度对象，避免重复创建。
 * 通俗理解：五子棋每颗黑子/白子不重复造，用同一对象共享坐标。
 */

 interface Chess {
    void display(int x, int y);
}

class BlackChess implements Chess {
    public void display(int x, int y) {
        System.out.println("黑棋落子位置：(" + x + ", " + y + ")");
    }
}

class WhiteChess implements Chess {
    public void display(int x, int y) {
        System.out.println("白棋落子位置：(" + x + ", " + y + ")");
    }
}

class ChessFactory {
    private static final Map<String, Chess> pool = new HashMap<>();

    public static Chess getChess(String color) {
        if (!pool.containsKey(color)) {
            if (color.equals("黑")) pool.put("黑", new BlackChess());
            else if (color.equals("白")) pool.put("白", new WhiteChess());
        }
        return pool.get(color);
    }
}

// 🚀 实际应用：
// - 字符串常量池、数据库连接池
// - 游戏中地图块、NPC等重复对象

class FlyweightApp {
    public static void main(String[] args) {
        Chess black1 = ChessFactory.getChess("黑");
        Chess black2 = ChessFactory.getChess("黑");
        System.out.println("是否同一对象: " + (black1 == black2));
        black1.display(1, 2);
        black2.display(3, 4);
    }
}

// ✅ 23. 桥梁模式（Bridge）
/**
 * 概念：将抽象与实现解耦，使两者可以独立变化。
 * 通俗理解：遥控器是抽象，电视品牌是实现；组合而非继承。
 */

interface Brand {
    void turnOn();
    void turnOff();
}

class Sony implements Brand {
    public void turnOn() { System.out.println("索尼电视开机"); }
    public void turnOff() { System.out.println("索尼电视关机"); }
}

class TCL implements Brand {
    public void turnOn() { System.out.println("TCL电视开机"); }
    public void turnOff() { System.out.println("TCL电视关机"); }
}

abstract class RemoteControl {
    protected Brand brand;
    public RemoteControl(Brand brand) { this.brand = brand; }
    public abstract void turnOn();
    public abstract void turnOff();
}

class AdvancedRemoteControl extends RemoteControl {
    public AdvancedRemoteControl(Brand brand) { super(brand); }
    public void turnOn() { brand.turnOn(); }
    public void turnOff() { brand.turnOff(); }
}

// 🚀 实际应用：
// - JDBC 接口与数据库驱动桥接
// - 抽象UI组件与平台适配实现解耦

class BridgeApp {
    public static void main(String[] args) {
        RemoteControl rc = new AdvancedRemoteControl(new Sony());
        rc.turnOn();
        rc.turnOff();
    }
}
