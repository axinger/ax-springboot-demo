// Groovy Map 集合操作示例

// 1. 创建 Map
def person = [
    name: 'Alice',
    age: 30,
    city: 'Beijing',
    skills: ['Java', 'Groovy', 'Python']
]

println "原始Map: ${person}"

// 2. 访问属性
println "姓名: ${person.name}"
println "年龄: ${person['age']}"
println "城市: ${person.city}"

// 3. 添加新属性
person.email = 'alice@example.com'
person['phone'] = '1234567890'
println "添加后: ${person}"

// 4. 修改属性
person.age = 31
println "修改年龄后: ${person.age}"

// 5. 删除属性
person.remove('phone')
println "删除后: ${person}"

// 6. 判断键是否存在
println "包含name键? ${person.containsKey('name')}"
println "包含address键? ${person.containsKey('address')}"

// 7. 获取所有键和值
println "所有键: ${person.keySet()}"
println "所有值: ${person.values()}"

// 8. 遍历Map
println "\n遍历Map:"
person.each { key, value ->
    println "  ${key}: ${value}"
}

// 9. 过滤Map
def filtered = person.findAll { key, value -> 
    key != 'skills' 
}
println "\n过滤后(不含skills): ${filtered}"

// 10. 转换Map
def upperCase = person.collectEntries { key, value ->
    [(key): value.toString().toUpperCase()]
}
println "\n大写转换: ${upperCase}"

// 11. Map合并
def address = [
    street: 'Nanjing Road',
    district: 'Huangpu'
]
def merged = person + address
println "\n合并后: ${merged}"

// 12. 嵌套Map
def company = [
    name: 'Tech Corp',
    employees: [
        [name: 'Alice', position: 'Developer'],
        [name: 'Bob', position: 'Designer']
    ]
]

println "\n公司名称: ${company.name}"
println "第一个员工: ${company.employees[0].name}"
println "第一个员工职位: ${company.employees[0].position}"

// 13. 使用默认值
def missingValue = person.getOrDefault('address', 'Unknown')
println "\n默认值: ${missingValue}"

// 14. Map查找
def found = person.find { key, value -> 
    key == 'name' 
}
println "查找结果: ${found}"

// 15. 统计
def counts = person.countBy { key, value ->
    value.getClass().simpleName
}
println "\n类型统计: ${counts}"
