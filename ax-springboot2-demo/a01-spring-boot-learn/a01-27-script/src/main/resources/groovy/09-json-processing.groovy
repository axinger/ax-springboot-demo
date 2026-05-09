// Groovy JSON 处理示例

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.text.SimpleDateFormat

// ==================== JSON 解析 ====================

// 1. 解析简单JSON
println "=== 解析简单JSON ==="
def jsonString = '''
{
    "name": "Alice",
    "age": 30,
    "city": "Beijing"
}
'''

def json = new JsonSlurper().parseText(jsonString)
println "姓名: ${json.name}"
println "年龄: ${json.age}"
println "城市: ${json.city}"

// 2. 解析嵌套JSON
println "\n=== 解析嵌套JSON ==="
def nestedJson = '''
{
    "user": {
        "name": "Bob",
        "contact": {
            "email": "bob@example.com",
            "phone": "13812345678"
        },
        "addresses": [
            {"type": "home", "city": "Shanghai"},
            {"type": "work", "city": "Beijing"}
        ]
    }
}
'''

def nested = new JsonSlurper().parseText(nestedJson)
println "用户名: ${nested.user.name}"
println "邮箱: ${nested.user.contact.email}"
println "家庭地址: ${nested.user.addresses[0].city}"
println "工作地址: ${nested.user.addresses[1].city}"

// 3. 解析JSON数组
println "\n=== 解析JSON数组 ==="
def arrayJson = '''
[
    {"id": 1, "name": "Apple", "price": 5.5},
    {"id": 2, "name": "Banana", "price": 3.2},
    {"id": 3, "name": "Cherry", "price": 15.0}
]
'''

def fruits = new JsonSlurper().parseText(arrayJson)
println "水果列表:"
fruits.each { fruit ->
    println "  - ${fruit.name}: ¥${fruit.price}"
}

// 4. 解析复杂JSON
println "\n=== 解析复杂JSON ==="
def complexJson = '''
{
    "company": "Tech Corp",
    "departments": [
        {
            "name": "Engineering",
            "employees": [
                {"name": "Alice", "position": "Developer", "skills": ["Java", "Groovy"]},
                {"name": "Bob", "position": "Designer", "skills": ["Photoshop", "Figma"]}
            ]
        },
        {
            "name": "Marketing",
            "employees": [
                {"name": "Charlie", "position": "Manager", "skills": ["SEO", "Content"]}
            ]
        }
    ]
}
'''

def company = new JsonSlurper().parseText(complexJson)
println "公司: ${company.company}"
println "\n部门列表:"
company.departments.each { dept ->
    println "  部门: ${dept.name}"
    dept.employees.each { emp ->
        println "    - ${emp.name} (${emp.position})"
        println "      技能: ${emp.skills.join(', ')}"
    }
}

// ==================== JSON 生成 ====================

// 5. 创建简单JSON对象
println "\n=== 创建简单JSON ==="
def person = [
        name: 'David',
        age: 25,
        city: 'Guangzhou'
]

def personJson = JsonOutput.toJson(person)
println "JSON: ${personJson}"

// 6. 格式化JSON输出
println "\n=== 格式化JSON ==="
def prettyJson = JsonOutput.prettyPrint(personJson)
println "格式化:\n${prettyJson}"

// 7. 创建嵌套JSON
println "\n=== 创建嵌套JSON ==="
def user = [
        name: 'Eve',
        contact: [
                email: 'eve@example.com',
                phone: '13987654321'
        ],
        hobbies: ['reading', 'coding', 'gaming']
]

def userJson = JsonOutput.prettyPrint(JsonOutput.toJson(user))
println "嵌套JSON:\n${userJson}"

// 8. 创建JSON数组
println "\n=== 创建JSON数组 ==="
def products = [
        [id: 1, name: 'Laptop', price: 5999],
        [id: 2, name: 'Mouse', price: 99],
        [id: 3, name: 'Keyboard', price: 299]
]

def productsJson = JsonOutput.prettyPrint(JsonOutput.toJson(products))
println "产品列表:\n${productsJson}"

// ==================== JSON 操作 ====================

// 9. 修改JSON数据
println "\n=== 修改JSON数据 ==="
def originalJson = '''
{
    "name": "Frank",
    "age": 28,
    "score": 85
}
'''

def data = new JsonSlurper().parseText(originalJson)
data.age = 29
data.score = 90
data.grade = 'A'

def modifiedJson = JsonOutput.prettyPrint(JsonOutput.toJson(data))
println "修改后:\n${modifiedJson}"

// 10. 过滤JSON数据
println "\n=== 过滤JSON数据 ==="
def studentsJson = '''
[
    {"name": "Alice", "score": 92, "grade": "A"},
    {"name": "Bob", "score": 85, "grade": "B"},
    {"name": "Charlie", "score": 78, "grade": "C"},
    {"name": "David", "score": 95, "grade": "A"}
]
'''

def students = new JsonSlurper().parseText(studentsJson)
def excellentStudents = students.findAll { it.score >= 90 }

println "优秀学生:"
excellentStudents.each { student ->
    println "  - ${student.name}: ${student.score}分 (${student.grade}级)"
}

// 11. 转换JSON数据
println "\n=== 转换JSON数据 ==="
def transformed = students.collect { student ->
    [
            name: student.name.toUpperCase(),
            score: student.score,
            passed: student.score >= 60
    ]
}

def transformedJson = JsonOutput.prettyPrint(JsonOutput.toJson(transformed))
println "转换后:\n${transformedJson}"

// 12. 统计JSON数据
println "\n=== 统计JSON数据 ==="
def totalScore = students.sum { it.score }
def averageScore = totalScore / students.size()
def maxScore = students.max { it.score }.score
def minScore = students.min { it.score }.score

println "总分: ${totalScore}"
println "平均分: ${averageScore}"
println "最高分: ${maxScore}"
println "最低分: ${minScore}"

// ==================== 高级用法 ====================

// 13. 处理null值
println "\n=== 处理null值 ==="
def jsonWithNull = '''
{
    "name": "Grace",
    "age": null,
    "email": "grace@example.com"
}
'''

def withNull = new JsonSlurper().parseText(jsonWithNull)
println "姓名: ${withNull.name}"
println "年龄: ${withNull.age ?: '未知'}"
println "邮箱: ${withNull.email}"

// 14. 动态构建JSON（修复版本）
println "\n=== 动态构建JSON ==="
def buildUserJson = { userName, userAge, userEmail ->
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def newUser = [
            name: userName,
            age: userAge,
            email: userEmail,
            created: sdf.format(new Date())
    ]
    return JsonOutput.prettyPrint(JsonOutput.toJson(newUser))
}

println buildUserJson('Henry', 30, 'henry@example.com')

// 15. JSON验证
println "\n=== JSON验证 ==="
def isValidJsonString = { jsonStr ->
    try {
        new JsonSlurper().parseText(jsonStr)
        return true
    } catch (Exception e) {
        return false
    }
}

println "\"{name: test}\" 有效? ${isValidJsonString('{"name": "test"}')}"
println "{invalid} 有效? ${isValidJsonString('{invalid}')}"

// 16. 从文件读取JSON（模拟）
println "\n=== 读取JSON字符串 ==="
def configFile = '''
{
    "database": {
        "host": "localhost",
        "port": 3306,
        "username": "root",
        "password": "secret"
    },
    "cache": {
        "enabled": true,
        "ttl": 3600
    }
}
'''

def config = new JsonSlurper().parseText(configFile)
println "数据库主机: ${config.database.host}"
println "数据库端口: ${config.database.port}"
println "缓存启用: ${config.cache.enabled}"
println "缓存TTL: ${config.cache.ttl}秒"

// 17. JSON深度合并
println "\n=== JSON合并 ==="
def defaults = [
        timeout: 30,
        retry: 3,
        debug: false
]

def overrides = [
        timeout: 60,
        debug: true
]

def merged = defaults + overrides
def mergedJson = JsonOutput.prettyPrint(JsonOutput.toJson(merged))
println "合并配置:\n${mergedJson}"

// 18. 提取特定字段
println "\n=== 提取字段 ==="
def usersJson = '''
[
    {"id": 1, "name": "Alice", "email": "alice@test.com", "age": 25},
    {"id": 2, "name": "Bob", "email": "bob@test.com", "age": 30},
    {"id": 3, "name": "Charlie", "email": "charlie@test.com", "age": 28}
]
'''

def users = new JsonSlurper().parseText(usersJson)
def namesOnly = users.collect { [id: it.id, name: it.name] }

def namesJson = JsonOutput.prettyPrint(JsonOutput.toJson(namesOnly))
println "只提取ID和姓名:\n${namesJson}"

// 19. 分组统计
println "\n=== 分组统计 ==="
def ordersJson = '''
[
    {"product": "Apple", "category": "Fruit", "amount": 100},
    {"product": "Banana", "category": "Fruit", "amount": 150},
    {"product": "Carrot", "category": "Vegetable", "amount": 80},
    {"product": "Broccoli", "category": "Vegetable", "amount": 120}
]
'''

def orders = new JsonSlurper().parseText(ordersJson)
def groupedByCategory = orders.groupBy { it.category }

groupedByCategory.each { category, items ->
    def total = items.sum { it.amount }
    println "${category}: 总量=${total}, 商品数=${items.size()}"
}

// 20. 条件查询
println "\n=== 条件查询 ==="
def expensiveProducts = orders.findAll { it.amount > 100 }
println "数量大于100的商品:"
expensiveProducts.each { product ->
    println "  - ${product.product}: ${product.amount}"
}