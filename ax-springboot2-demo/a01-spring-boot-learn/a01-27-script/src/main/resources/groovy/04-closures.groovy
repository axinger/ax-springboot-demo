// Groovy 闭包和高阶函数示例

// 1. 基本闭包定义
def greet = { name ->
    return "Hello, ${name}!"
}
println greet("Alice")

// 2. 简化闭包（省略return）
def add = { a, b -> a + b }
println "5 + 3 = ${add(5, 3)}"

// 3. 闭包作为参数
def numbers = [1, 2, 3, 4, 5]

// forEach
print "forEach: "
numbers.each { num ->
    print "${num * 2} "
}
println()

// 4. map/collect
def doubled = numbers.collect { it * 2 }
println "翻倍: ${doubled}"

// 5. filter/findAll
def evens = numbers.findAll { it % 2 == 0 }
println "偶数: ${evens}"

// 6. findFirst/find
def firstEven = numbers.find { it % 2 == 0 }
println "第一个偶数: ${firstEven}"

// 7. reduce/inject
def sum = numbers.inject(0) { acc, val ->
    acc + val
}
println "总和: ${sum}"

// 8. 链式调用
def result = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    .findAll { it % 2 == 0 }      // 过滤偶数
    .collect { it * 10 }           // 乘以10
    .sort { a, b -> b <=> a }      // 降序排序
    .take(3)                        // 取前3个
println "链式调用: ${result}"

// 9. 闭包引用方法
def multiply = { a, b -> a * b }
def operation = multiply
println "闭包引用: ${operation(4, 5)}"

// 10. 高阶函数 - 接受闭包作为参数
def executeTwice = { value, closure ->
    def first = closure(value)
    def second = closure(first)
    return second
}

def doubleIt = { it * 2 }
println "执行两次翻倍: ${executeTwice(5, doubleIt)}"  // 5 -> 10 -> 20

// 11. 返回闭包的函数
def createMultiplier = { factor ->
    return { number -> number * factor }
}

def triple = createMultiplier(3)
def quintuple = createMultiplier(5)

println "三倍: ${triple(7)}"      // 21
println "五倍: ${quintuple(7)}"   // 35

// 12. 闭包的委托（Delegate）
class Calculator {
    def add(a, b) { a + b }
    def subtract(a, b) { a - b }
}

def calc = new Calculator()
def calculationClosure = {
    delegate.add(10, 5)
}
calculationClosure.delegate = calc
calculationClosure.resolveStrategy = Closure.DELEGATE_FIRST
def calculation = calculationClosure.call()

println "委托调用: ${calculation}"

// 13. curry - 柯里化
def greetWithTime = { greeting, name, time ->
    "${greeting} ${name}, it's ${time}"
}

def goodMorning = greetWithTime.curry("Good morning")
println goodMorning("Alice", "8:00 AM")

// 14. 闭包中的隐式参数 it
def squares = [1, 2, 3, 4, 5].collect { it * it }
println "平方: ${squares}"

// 15. 带状态的闭包
def createCounter = {
    def count = 0
    return {
        count++
        return count
    }
}

def counter = createCounter()
println "计数: ${counter()}"  // 1
println "计数: ${counter()}"  // 2
println "计数: ${counter()}"  // 3
