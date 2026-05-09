// Groovy 类和对象示例

// ==================== 基本类定义 ====================

// 1. 简单类
println "=== 简单类 ==="
class Person {
    String name
    int age
    String email
    
    // 构造方法
    Person(String name, int age) {
        this.name = name
        this.age = age
    }
    
    // 方法
    String introduce() {
        return "Hi, I'm ${name}, ${age} years old."
    }
    
    boolean isAdult() {
        return age >= 18
    }
}

def person = new Person('Alice', 25)
person.email = 'alice@example.com'

println person.introduce()
println "是成年人? ${person.isAdult()}"
println "邮箱: ${person.email}"

// 2. 使用Map构造
println "\n=== Map构造 ==="
class Student {
    String name
    int age
    String major
    
    String study() {
        return "${name} is studying ${major}"
    }
}

def student = new Student(name: 'Bob', age: 20, major: 'Computer Science')
println student.study()

// ==================== 属性和访问控制 ====================

// 3. 私有属性
println "\n=== 私有属性 ==="
class BankAccount {
    private String accountNumber
    private double balance
    
    BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber
        this.balance = initialBalance
    }
    
    void deposit(double amount) {
        if (amount > 0) {
            balance += amount
            println "存入: ¥${amount}, 余额: ¥${balance}"
        }
    }
    
    void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount
            println "取出: ¥${amount}, 余额: ¥${balance}"
        } else {
            println "余额不足或金额无效"
        }
    }
    
    double getBalance() {
        return balance
    }
}

def account = new BankAccount('123456789', 1000)
account.deposit(500)
account.withdraw(200)
println "当前余额: ¥${account.getBalance()}"

// 4. Getter和Setter
println "\n=== Getter和Setter ==="
class Employee {
    private String name
    private double salary
    
    String getName() {
        return name?.toUpperCase()
    }
    
    void setName(String name) {
        if (name && !name.isEmpty()) {
            this.name = name
        }
    }
    
    double getSalary() {
        return salary
    }
    
    void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary
        }
    }
}

def emp = new Employee()
emp.setName('charlie')
emp.setSalary(5000)
println "姓名: ${emp.getName()}"
println "薪资: ¥${emp.getSalary()}"

// ==================== 继承 ====================

// 5. 类的继承
println "\n=== 继承 ==="
class Animal {
    String name
    int age
    
    Animal(String name, int age) {
        this.name = name
        this.age = age
    }
    
    String speak() {
        return "..."
    }
    
    String info() {
        return "${name} (${age}岁)"
    }
}

class Dog extends Animal {
    String breed
    
    Dog(String name, int age, String breed) {
        super(name, age)
        this.breed = breed
    }
    
    @Override
    String speak() {
        return "Woof! Woof!"
    }
    
    String fetch() {
        return "${name} is fetching the ball"
    }
}

class Cat extends Animal {
    Cat(String name, int age) {
        super(name, age)
    }
    
    @Override
    String speak() {
        return "Meow! Meow!"
    }
    
    String purr() {
        return "${name} is purring"
    }
}

def dog = new Dog('Buddy', 3, 'Golden Retriever')
def cat = new Cat('Kitty', 2)

println "狗: ${dog.info()}"
println "叫声: ${dog.speak()}"
println "品种: ${dog.breed}"
println "动作: ${dog.fetch()}"

println "\n猫: ${cat.info()}"
println "叫声: ${cat.speak()}"
println "动作: ${cat.purr()}"

// ==================== 接口和特质 ====================

// 6. 接口
println "\n=== 接口 ==="
interface Flyable {
    void fly()
    String getFlightSpeed()
}

class Bird implements Flyable {
    String name
    
    Bird(String name) {
        this.name = name
    }
    
    @Override
    void fly() {
        println "${name} is flying"
    }
    
    @Override
    String getFlightSpeed() {
        return "Fast"
    }
}

def eagle = new Bird('Eagle')
eagle.fly()
println "飞行速度: ${eagle.getFlightSpeed()}"

// 7. 特质（Trait）
println "\n=== 特质 ==="
trait Swimmable {
    void swim() {
        println "${getName()} is swimming"
    }
    
    abstract String getName()
}

class Fish implements Swimmable {
    String name
    
    Fish(String name) {
        this.name = name
    }
    
    String getName() {
        return name
    }
}

def fish = new Fish('Nemo')
fish.swim()

// ==================== 静态方法 ====================

// 8. 静态方法和属性
println "\n=== 静态方法 ==="
class MathHelper {
    static final double PI = 3.14159265358979
    
    static double circleArea(double radius) {
        return PI * radius * radius
    }
    
    static int max(int a, int b) {
        return a > b ? a : b
    }
}

println "圆周率: ${MathHelper.PI}"
println "半径5的圆面积: ${MathHelper.circleArea(5)}"
println "最大值: ${MathHelper.max(10, 20)}"

// ==================== 抽象类 ====================

// 9. 抽象类
println "\n=== 抽象类 ==="
abstract class Shape {
    String color
    
    Shape(String color) {
        this.color = color
    }
    
    abstract double area()
    abstract double perimeter()
    
    String info() {
        return "${this.getClass().simpleName} (颜色: ${color})"
    }
}

class Rectangle extends Shape {
    double width
    double height
    
    Rectangle(double width, double height, String color) {
        super(color)
        this.width = width
        this.height = height
    }
    
    @Override
    double area() {
        return width * height
    }
    
    @Override
    double perimeter() {
        return 2 * (width + height)
    }
}

class Circle extends Shape {
    double radius
    
    Circle(double radius, String color) {
        super(color)
        this.radius = radius
    }
    
    @Override
    double area() {
        return MathHelper.PI * radius * radius
    }
    
    @Override
    double perimeter() {
        return 2 * MathHelper.PI * radius
    }
}

def rect = new Rectangle(5, 3, 'Red')
def circle = new Circle(4, 'Blue')

println rect.info()
println "面积: ${rect.area()}"
println "周长: ${rect.perimeter()}"

println "\n${circle.info()}"
println "面积: ${circle.area()}"
println "周长: ${circle.perimeter()}"

// ==================== 内部类 ====================

// 10. 内部类
println "\n=== 内部类 ==="
class OuterClass {
    String outerField = "Outer"
    
    static class InnerClass {
        String innerField = "Inner"
        
        void display() {
            println "内部字段: ${innerField}"
        }
    }
}

def inner = new OuterClass.InnerClass()
inner.display()

// ==================== 枚举 ====================

// 11. 枚举
println "\n=== 枚举 ==="
enum DayOfWeek {
    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日")
    
    private final String chineseName
    
    DayOfWeek(String chineseName) {
        this.chineseName = chineseName
    }
    
    String getChineseName() {
        return chineseName
    }
    
    boolean isWeekend() {
        return this in [SATURDAY, SUNDAY]
    }
}

def today = DayOfWeek.WEDNESDAY
println "今天: ${today}"
println "中文: ${today.getChineseName()}"
println "是周末? ${today.isWeekend() ? '是' : '否'}"

println "\n所有星期:"
DayOfWeek.values().each { day ->
    println "  ${day} - ${day.getChineseName()} ${day.isWeekend() ? '(周末)' : ''}"
}

// ==================== 数据类 ====================

// 12. 使用@Canonical注解
println "\n=== 数据类 ==="
import groovy.transform.Canonical

@Canonical
class Point {
    double x
    double y
}

def p1 = new Point(3.0, 4.0)
def p2 = new Point(3.0, 4.0)
def p3 = new Point(5.0, 6.0)

println "点1: ${p1}"
println "点2: ${p2}"
println "点1 == 点2? ${p1 == p2}"
println "点1 == 点3? ${p1 == p3}"

// ==================== 动态特性 ====================

// 13. 动态添加方法
println "\n=== 动态方法 ==="
class DynamicClass {
    String value
    
    DynamicClass(String value) {
        this.value = value
    }
}

def obj = new DynamicClass('test')

// 运行时添加方法
obj.metaClass.uppercase = { ->
    return value.toUpperCase()
}

println "原始值: ${obj.value}"
println "大写: ${obj.uppercase()}"

// 14. 动态属性
println "\n=== 动态属性 ==="
def dynamicObj = new Expando()
dynamicObj.name = 'Dynamic'
dynamicObj.age = 25
dynamicObj.greet = { -> "Hello, I'm ${name}" }

println "姓名: ${dynamicObj.name}"
println "年龄: ${dynamicObj.age}"
println "问候: ${dynamicObj.greet()}"
