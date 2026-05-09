// Groovy List 集合操作示例

// 1. 创建列表
def numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
def names = ["Alice", "Bob", "Charlie", "David"]

println "原始列表: ${numbers}"
println "名称列表: ${names}"

// 2. 访问元素
println "第一个元素: ${numbers[0]}"
println "最后一个元素: ${numbers[-1]}"
println "切片 [2..5]: ${numbers[2..5]}"

// 3. 添加元素
numbers << 11
numbers.add(12)
println "添加后: ${numbers}"

// 4. 过滤 - 找出偶数
def evens = numbers.findAll { it % 2 == 0 }
println "偶数: ${evens}"

// 5. 映射 - 每个元素乘以2
def doubled = numbers.collect { it * 2 }
println "翻倍: ${doubled}"

// 6. 查找 - 第一个大于5的元素
def firstGt5 = numbers.find { it > 5 }
println "第一个大于5的: ${firstGt5}"

// 7. 排序（降序）
def sortedDesc = numbers.sort { a, b -> b <=> a }
println "降序排序: ${sortedDesc}"

// 8. 分组
def grouped = numbers.groupBy { it % 2 == 0 ? 'even' : 'odd' }
println "分组: ${grouped}"

// 9. 去重
def duplicates = [1, 2, 2, 3, 3, 3, 4]
def unique = duplicates.unique()
println "去重: ${unique}"

// 10. 求和、最大值、最小值
println "总和: ${numbers.sum()}"
println "最大值: ${numbers.max()}"
println "最小值: ${numbers.min()}"

// 11. 遍历
print "遍历: "
numbers.each { print "${it} " }
println()

// 12. 带索引遍历
numbers.eachWithIndex { num, idx ->
    println "索引 ${idx}: ${num}"
}

// 13. 链式调用
def result = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    .findAll { it % 2 == 0 }      // 过滤偶数
    .collect { it * 10 }           // 乘以10
    .sort { a, b -> b <=> a }      // 降序
    .take(3)                        // 取前3个
println "链式调用结果: ${result}"

// 14. inject/reduce
def sum = numbers.inject(0) { acc, val -> acc + val }
println "inject求和: ${sum}"

// 15. any 和 every
def hasEven = numbers.any { it % 2 == 0 }
def allPositive = numbers.every { it > 0 }
println "有偶数吗? ${hasEven}"
println "都是正数吗? ${allPositive}"
