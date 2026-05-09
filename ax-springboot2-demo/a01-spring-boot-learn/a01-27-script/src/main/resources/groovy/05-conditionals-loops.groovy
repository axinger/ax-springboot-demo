// Groovy 条件语句和循环示例

// ==================== 条件语句 ====================

// 1. if-else 语句
def score = 85
def grade

if (score >= 90) {
    grade = 'A'
} else if (score >= 80) {
    grade = 'B'
} else if (score >= 70) {
    grade = 'C'
} else {
    grade = 'D'
}

println "分数: ${score}, 等级: ${grade}"

// 2. 三元运算符
def age = 20
def status = age >= 18 ? "成年" : "未成年"
println "年龄: ${age}, 状态: ${status}"

// 3. Elvis 运算符（简化三元）
def name = null
def displayName = name ?: "Anonymous"
println "显示名称: ${displayName}"

def name2 = "Alice"
def displayName2 = name2 ?: "Anonymous"
println "显示名称: ${displayName2}"

// 4. switch-case 语句
def day = 3
def dayName

switch (day) {
    case 1:
        dayName = 'Monday'
        break
    case 2:
        dayName = 'Tuesday'
        break
    case 3:
        dayName = 'Wednesday'
        break
    case 4:
        dayName = 'Thursday'
        break
    case 5:
        dayName = 'Friday'
        break
    case [6, 7]:  // 多个值
        dayName = 'Weekend'
        break
    default:
        dayName = 'Invalid day'
}

println "星期${day}: ${dayName}"

// 5. switch 使用类型
def value = "Hello"
def typeDescription

switch (value) {
    case Integer:
        typeDescription = "整数"
        break
    case String:
        typeDescription = "字符串"
        break
    case List:
        typeDescription = "列表"
        break
    default:
        typeDescription = "未知类型"
}

println "${value} 是 ${typeDescription}"

// ==================== 循环语句 ====================

// 6. for 循环 - 传统方式
println "\n传统for循环:"
for (int i = 1; i <= 5; i++) {
    print "${i} "
}
println()

// 7. for 循环 - 范围方式
println "\n范围for循环:"
for (def i in 1..5) {
    print "${i} "
}
println()

// 8. for 循环 - 集合遍历
println "\n集合遍历:"
def fruits = ["Apple", "Banana", "Cherry"]
for (fruit in fruits) {
    print "${fruit} "
}
println()

// 9. while 循环
println "\nwhile循环:"
def count = 0
def sum = 0
while (count < 5) {
    count++
    sum += count
    print "${count} "
}
println("\n总和: ${sum}")

// 10. do-while 循环
println "\ndo-while循环:"
def num = 0
do {
    num++
    print "${num} "
} while (num < 3)
println()

// 11. each 遍历
println "\neach遍历:"
[1, 2, 3, 4, 5].each { item ->
    print "${item * 2} "
}
println()

// 12. eachWithIndex 带索引遍历
println "\neachWithIndex遍历:"
["A", "B", "C"].eachWithIndex { item, index ->
    println "  索引 ${index}: ${item}"
}

// 13. times 循环
println "\ntimes循环:"
5.times { idx ->
    print "${idx + 1} "
}
println()

// 14. step 步进循环
println "\nstep循环(步长2):"
0.step(10, 2) { val ->
    print "${val} "
}
println()

// 15. 循环控制 - break 和 continue
println "\nbreak示例:"
for (def i in 1..10) {
    if (i > 5) break
    print "${i} "
}
println()

println "\ncontinue示例:"
for (def i in 1..10) {
    if (i % 2 != 0) continue
    print "${i} "
}
println()

// 16. 实际示例：计算阶乘
def factorial = 1
for (int i = 1; i <= 5; i++) {
    factorial *= i
}
println "\n5的阶乘: ${factorial}"

// 17. 实际示例：斐波那契数列
println "\n斐波那契数列前10项:"
def fib = [0, 1]
for (int i = 2; i < 10; i++) {
    fib << fib[i - 1] + fib[i - 2]
}
println fib
