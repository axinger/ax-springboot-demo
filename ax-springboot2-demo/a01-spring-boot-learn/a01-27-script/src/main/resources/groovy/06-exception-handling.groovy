// Groovy 异常处理和安全操作符示例

// ==================== 异常处理 ====================

// 1. try-catch-finally
println "=== 基本异常处理 ==="
try {
    def list = [1, 2, 3]
    def value = list[10]  // 这会抛出异常
    println "值: ${value}"
} catch (IndexOutOfBoundsException e) {
    println "捕获到索引越界异常: ${e.message}"
} finally {
    println "finally 块总是执行"
}

// 2. 多个catch块
println "\n=== 多个catch块 ==="
try {
    def str = "123abc"
    def num = Integer.parseInt(str)
    println "数字: ${num}"
} catch (NumberFormatException e) {
    println "数字格式异常: ${e.message}"
} catch (Exception e) {
    println "其他异常: ${e.message}"
}

// 3. try-with-resources（自动关闭资源）
println "\n=== try-with-resources ==="
try {
    def reader = new StringReader("Hello World")
    def content = reader.text
    println "读取内容: ${content}"
} catch (Exception e) {
    println "读取异常: ${e.message}"
}

// 4. 抛出异常
println "\n=== 抛出异常 ==="
def checkAge = { age ->
    if (age < 0) {
        throw new IllegalArgumentException("年龄不能为负数: ${age}")
    }
    return "年龄有效: ${age}"
}

try {
    println checkAge(25)
    println checkAge(-5)
} catch (IllegalArgumentException e) {
    println "参数异常: ${e.message}"
}

// 5. 自定义异常
class BusinessException extends Exception {
    String errorCode
    
    BusinessException(String message, String errorCode) {
        super(message)
        this.errorCode = errorCode
    }
}

println "\n=== 自定义异常 ==="
try {
    throw new BusinessException("业务处理失败", "BUSINESS_001")
} catch (BusinessException e) {
    println "业务异常: ${e.message}, 错误码: ${e.errorCode}"
}

// ==================== 安全导航操作符 ====================

// 6. 安全导航操作符 ?.
println "\n=== 安全导航操作符 ==="

class Address {
    String city
    String street
}

class User {
    String name
    Address address
}

def user1 = new User(name: 'Bob', address: new Address(city: 'Shanghai', street: 'Nanjing Rd'))
def user2 = new User(name: 'Charlie', address: null)
def user3 = null

// 安全访问嵌套属性
def city1 = user1?.address?.city ?: 'Unknown'
def city2 = user2?.address?.city ?: 'Unknown'
def city3 = user3?.address?.city ?: 'Unknown'

println "user1城市: ${city1}"
println "user2城市: ${city2}"
println "user3城市: ${city3}"

// 7. Elvis 运算符 ?:
println "\n=== Elvis运算符 ==="

def name1 = null
def displayName1 = name1 ?: 'Anonymous'
println "displayName1: ${displayName1}"

def name2 = 'Alice'
def displayName2 = name2 ?: 'Anonymous'
println "displayName2: ${displayName2}"

// 空字符串也视为false
def emptyStr = ''
def result = emptyStr ?: 'default'
println "空字符串结果: '${result}'"

// 8. 组合使用 ?. 和 ?:
println "\n=== 组合使用 ==="

def getUserName = { user ->
    return user?.name ?: 'Guest'
}

println "用户名: ${getUserName(user1)}"
println "用户名: ${getUserName(user2)}"
println "用户名: ${getUserName(user3)}"

// 9. 安全方法调用
println "\n=== 安全方法调用 ==="

def text = null
def upperText = text?.toUpperCase() ?: 'DEFAULT'
println "大写文本: ${upperText}"

def text2 = "hello"
def upperText2 = text2?.toUpperCase() ?: 'DEFAULT'
println "大写文本: ${upperText2}"

// 10. 安全索引访问
println "\n=== 安全索引访问 ==="

def list = [1, 2, 3]
def value1 = list?.getAt(1)  // 安全获取索引1的值
def value2 = list?.getAt(10)  // 索引不存在返回null

println "索引1的值: ${value1}"
println "索引10的值: ${value2 ?: '不存在'}"

// 11. 展开操作符 *.(Spread Operator)
println "\n=== 展开操作符 ==="

def users = [
    new User(name: 'Alice', address: null),
    new User(name: 'Bob', address: null),
    new User(name: 'Charlie', address: null)
]

def names = users*.name
println "所有用户名: ${names}"

// 12. 安全的类型转换
println "\n=== 安全类型转换 ==="

def asInt = { obj ->
    try {
        return obj as int
    } catch (Exception e) {
        return 0
    }
}

println "转换'123': ${asInt('123')}"
println "转换'abc': ${asInt('abc')}"

// 13. 断言
println "\n=== 断言 ==="

def positiveNumber = 5
assert positiveNumber > 0 : "数字必须为正数"
println "断言通过: ${positiveNumber} 是正数"

try {
    def negativeNumber = -5
    assert negativeNumber > 0 : "数字必须为正数"
} catch (AssertionError e) {
    println "断言失败: ${e.message}"
}

// 14. 防御性编程示例
println "\n=== 防御性编程 ==="

def safeDivide = { numerator, denominator ->
    if (denominator == null || denominator == 0) {
        return 0
    }
    return numerator / denominator
}

println "10 / 2 = ${safeDivide(10, 2)}"
println "10 / 0 = ${safeDivide(10, 0)}"
println "10 / null = ${safeDivide(10, null)}"

// 15. 可选链的实际应用
println "\n=== 可选链实际应用 ==="

class Company {
    String name
    List departments
}

class Department {
    String deptName
    List employees
}

class Employee {
    String empName
    String email
}

def company = new Company(
    name: 'Tech Corp',
    departments: [
        new Department(
            deptName: 'Engineering',
            employees: [
                new Employee(empName: 'Alice', email: 'alice@tech.com'),
                new Employee(empName: 'Bob', email: 'bob@tech.com')
            ]
        )
    ]
)

// 安全地深度访问
def firstEmployeeEmail = company?.departments?.first()?.employees?.first()?.email
println "第一个员工邮箱: ${firstEmployeeEmail ?: 'N/A'}"

def missingEmail = company?.departments?.find { it.deptName == 'HR' }?.employees?.first()?.email
println "HR部门第一个员工邮箱: ${missingEmail ?: 'N/A'}"
