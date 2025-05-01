package com.sh.my.design;

public class HelloDesign {
    

}

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ✅ 1. 单例模式（Singleton）
/**
 * 概念：保证一个类只有一个实例，并提供一个全局访问点。
 * 通俗理解：整个系统中只能有一个某类对象，比如配置管理器、线程池、数据库连接池。
 */

// ❌ 反例：每次调用都创建新对象
class BadConfigManager {
    public BadConfigManager() {}
}

// ✅ 最佳实践：懒汉式（线程安全）
class ConfigManager {
    private static volatile ConfigManager instance;

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public void printConfig() {
        System.out.println("读取配置...");
    }
}

// 🚀 实际项目应用：
// - 日志记录器（LogManager）
// - 数据库连接池管理（如 Druid）
// - 配置加载器（Spring Environment）

class MainApp {
    public static void main(String[] args) {
        ConfigManager mgr = ConfigManager.getInstance();
        mgr.printConfig();
    }
}

// ✅ 2. 工厂方法模式（Factory Method）
/**
 * 概念：定义一个用于创建对象的接口，让子类决定实例化哪个类。
 * 通俗理解：就像“开店卖货”，父类提供开店流程，子类决定卖哪种商品。
 */

interface Product {
    void use();
}

class Phone implements Product {
    public void use() { System.out.println("使用手机"); }
}

class Laptop implements Product {
    public void use() { System.out.println("使用笔记本"); }
}

abstract class ProductFactory {
    public abstract Product createProduct();
}

class PhoneFactory extends ProductFactory {
    public Product createProduct() {
        return new Phone();
    }
}

class LaptopFactory extends ProductFactory {
    public Product createProduct() {
        return new Laptop();
    }
}

// 🚀 实际项目应用：
// - JDBC 中的 Connection 对象由不同厂商驱动生成
// - Spring 中 BeanFactory（IOC容器）就是工厂方法应用

class FactoryApp {
    public static void main(String[] args) {
        ProductFactory factory = new PhoneFactory();
        Product product = factory.createProduct();
        product.use();
    }
}

// ✅ 3. 抽象工厂模式（Abstract Factory）
/**
 * 概念：提供一组相关或相互依赖对象的创建接口，而不需要指定具体类。
 * 通俗理解：像生产家具的工厂，工厂能同时造椅子、沙发、桌子这类“同一系列”的东西。
 */

interface Chair {
    void sit();
}

interface Table {
    void use();
}

class ModernChair implements Chair {
    public void sit() { System.out.println("坐在现代椅子上"); }
}

class ModernTable implements Table {
    public void use() { System.out.println("使用现代桌子"); }
}

class ClassicChair implements Chair {
    public void sit() { System.out.println("坐在古典椅子上"); }
}

class ClassicTable implements Table {
    public void use() { System.out.println("使用古典桌子"); }
}

interface FurnitureFactory {
    Chair createChair();
    Table createTable();
}

class ModernFurnitureFactory implements FurnitureFactory {
    public Chair createChair() { return new ModernChair(); }
    public Table createTable() { return new ModernTable(); }
}

class ClassicFurnitureFactory implements FurnitureFactory {
    public Chair createChair() { return new ClassicChair(); }
    public Table createTable() { return new ClassicTable(); }
}

// 🚀 实际项目应用：
// - Java GUI 工具包提供多种主题（如 Metal、Motif）对应抽象组件工厂
// - JDBC 中 Connection + Statement 属于同一系列

class AbstractFactoryApp {
    public static void main(String[] args) {
        FurnitureFactory factory = new ModernFurnitureFactory();
        Chair chair = factory.createChair();
        Table table = factory.createTable();
        chair.sit();
        table.use();
    }
}

// ✅ 4. 模板方法模式（Template Method）
/**
 * 概念：定义算法框架，将部分步骤延迟到子类实现。
 * 通俗理解：就像炒菜流程一样，先热锅、加油，再让不同厨师加不同食材。
 */

abstract class CookTemplate {
    public final void cook() {
        boilOil();
        addIngredients();
        stirFry();
    }

    private void boilOil() {
        System.out.println("热锅倒油");
    }

    protected abstract void addIngredients();

    private void stirFry() {
        System.out.println("翻炒出锅");
    }
}

class CookBeef extends CookTemplate {
    protected void addIngredients() {
        System.out.println("加入牛肉和青椒");
    }
}

class CookTomatoEgg extends CookTemplate {
    protected void addIngredients() {
        System.out.println("加入番茄和鸡蛋");
    }
}

// 🚀 项目应用：
// - JUnit 的测试用例运行流程（setUp → test → tearDown）
// - Spring JdbcTemplate、HibernateTemplate

class TemplateMethodApp {
    public static void main(String[] args) {
        CookTemplate dish = new CookBeef();
        dish.cook();
    }
}

// 缺少的设计模式：

// Java 23种设计模式完整详解（概念 + 反例 + 最佳实践 + 项目场景）

// ...（前6个模式略）

// ✅ 7. 原型模式（Prototype）
/**
 * 概念：用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象。
 * 通俗理解：复制粘贴——复制已有对象当模板，不重新new。
 */

 class Prototype implements Cloneable {
    private String field;

    public Prototype(String field) {
        this.field = field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void show() {
        System.out.println("字段内容: " + field);
    }

    public Prototype clone() {
        try {
            return (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// 🚀 项目应用：
// - 原型模式常用于对象构建成本较高时（如图形编辑器中的图元）
// - Spring 中 Bean 的 Scope=prototype

class PrototypeApp {
    public static void main(String[] args) {
        Prototype p1 = new Prototype("原始");
        Prototype p2 = p1.clone();
        p2.setField("复制");
        p1.show();
        p2.show();
    }
}

// ✅ 8. 中介者模式（Mediator）
/**
 * 概念：用一个中介对象封装一系列对象交互。中介者使各对象不需要显式引用彼此，降低耦合。
 * 通俗理解：群聊群主——所有人通过群主联系，彼此不直接发消息。
 */

interface Mediator {
    void notify(Component sender, String event);
}

class DialogMediator implements Mediator {
    private Button button;
    private TextBox textBox;

    public void setButton(Button button) { this.button = button; }
    public void setTextBox(TextBox textBox) { this.textBox = textBox; }

    public void notify(Component sender, String event) {
        if (sender == button && event.equals("click")) {
            textBox.showText("按钮被点击");
        }
    }
}

abstract class Component {
    protected Mediator mediator;
    public Component(Mediator mediator) { this.mediator = mediator; }
}

class Button extends Component {
    public Button(Mediator mediator) { super(mediator); }
    public void click() {
        mediator.notify(this, "click");
    }
}

class TextBox extends Component {
    public TextBox(Mediator mediator) { super(mediator); }
    public void showText(String msg) {
        System.out.println("文本框显示: " + msg);
    }
}

// 🚀 实际项目应用：
// - GUI 中控件通信（Swing、Android）
// - MVC 框架中的 Controller（Spring）作为中介协调请求和数据

class MediatorApp {
    public static void main(String[] args) {
        DialogMediator mediator = new DialogMediator();
        Button button = new Button(mediator);
        TextBox textBox = new TextBox(mediator);
        mediator.setButton(button);
        mediator.setTextBox(textBox);
        button.click();
    }
}

// ✅ 9. 命令模式（Command）
/**
 * 概念：将请求封装成对象，从而使用户可用不同请求对客户端参数化。
 * 通俗理解：遥控器——按键背后是命令，命令和执行器分离。
 */

interface Command {
    void execute();
}

class Light {
    public void on() { System.out.println("灯开了"); }
    public void off() { System.out.println("灯关了"); }
}

class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
}

class RemoteControl {
    private Command command;
    public void setCommand(Command command) { this.command = command; }
    public void pressButton() { command.execute(); }
}

// 🚀 项目应用：
// - GUI按钮事件绑定
// - 事务日志、操作撤销（undo）
// - Java Runnable、线程池任务封装

class CommandApp {
    public static void main(String[] args) {
        Light light = new Light();
        Command command = new LightOnCommand(light);
        RemoteControl remote = new RemoteControl();
        remote.setCommand(command);
        remote.pressButton();
    }
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



