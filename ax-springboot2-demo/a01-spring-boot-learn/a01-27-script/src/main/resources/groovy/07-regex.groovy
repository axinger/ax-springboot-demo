// Groovy 正则表达式示例

// ==================== 基本正则操作 ====================

// 1. 创建正则表达式模式
def pattern = ~/world/
def text = "Hello World"

println "=== 基本匹配 ==="
println "文本: ${text}"
println "模式: ${pattern}"
println "是否匹配: ${text ==~ pattern}"  // 完全匹配
println "是否包含: ${text =~ pattern}"   // 部分匹配

// 2. 使用 find() 查找第一个匹配
println "\n=== 查找第一个匹配 ==="
def sentence = "The quick brown fox jumps over the lazy dog"
def wordPattern = ~/fox/
def found = sentence.find(wordPattern)
println "找到的单词: ${found}"

// 3. 查找所有匹配 findAll()
println "\n=== 查找所有匹配 ==="
def allWords = sentence.findAll(~/\b\w{4}\b/)  // 4个字母的单词
println "4个字母的单词: ${allWords}"

// 4. 替换 replaceAll()
println "\n=== 替换 ==="
def replaced = sentence.replaceAll(/fox|dog/, 'animal')
println "替换后: ${replaced}"

// 5. 替换第一个 replaceFirst()
def replacedFirst = sentence.replaceFirst(/the/, 'a')
println "替换第一个: ${replacedFirst}"

// ==================== 常用正则模式 ====================

// 6. 邮箱验证
println "\n=== 邮箱验证 ==="
def emailPattern = ~/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

def emails = [
        "user@example.com",
        "test.user@domain.co.uk",
        "invalid@",
        "@missing.com",
        "valid_email@test.org"
]

emails.each { email ->
    def isValid = email ==~ emailPattern
    println "${email}: ${isValid ? '✓ 有效' : '✗ 无效'}"
}

// 7. 手机号验证（中国）
println "\n=== 手机号验证 ==="
def phonePattern = ~/^1[3-9]\d{9}$/

def phones = [
        "13812345678",
        "19876543210",
        "12345678901",
        "1381234567"
]

phones.each { phone ->
    def isValid = phone ==~ phonePattern
    println "${phone}: ${isValid ? '✓ 有效' : '✗ 无效'}"
}

// 8. 身份证验证（简化版）
println "\n=== 身份证验证 ==="
def idCardPattern = ~/^\d{17}[\dXx]$/

def idCards = [
        "110101199001011234",
        "11010119900101123X",
        "123456789",
        "11010119900101123A"
]

idCards.each { id ->
    def isValid = id ==~ idCardPattern
    println "${id}: ${isValid ? '✓ 有效' : '✗ 无效'}"
}

// 9. URL验证
println "\n=== URL验证 ==="
def urlPattern = ~/^https?:\/\/[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(\/.*)?$/

def urls = [
        "https://www.example.com",
        "http://test.org/path",
        "ftp://invalid.com",
        "https://sub.domain.com/page"
]

urls.each { url ->
    def isValid = url ==~ urlPattern
    println "${url}: ${isValid ? '✓ 有效' : '✗ 无效'}"
}

// 10. 日期格式验证
println "\n=== 日期格式验证 ==="
def datePattern = ~/^\d{4}-\d{2}-\d{2}$/

def dates = [
        "2024-01-15",
        "2024/01/15",
        "2024-1-15",
        "2024-01-15 10:30:00"
]

dates.each { date ->
    def isValid = date ==~ datePattern
    println "${date}: ${isValid ? '✓ 有效' : '✗ 无效'}"
}

// ==================== 分组和捕获 ====================

// 11. 提取分组
println "\n=== 提取分组 ==="
def dateText = "Date: 2024-01-15"
def dateExtractor = /(\d{4})-(\d{2})-(\d{2})/

def matcher = (dateText =~ dateExtractor)
if (matcher.find()) {
    println "完整匹配: ${matcher[0]}"
    println "年: ${matcher[0][1]}"
    println "月: ${matcher[0][2]}"
    println "日: ${matcher[0][3]}"
}

// 12. 提取所有邮箱
println "\n=== 提取所有邮箱 ==="
def content = "Contact us at support@example.com or sales@test.org"
def emailExtractor = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/

def extractedEmails = content.findAll(emailExtractor)
println "提取的邮箱: ${extractedEmails}"

// 13. 命名分组
println "\n=== 命名分组 ==="
def logEntry = "2024-01-15 ERROR: Something went wrong"
def logPattern = /(?<date>\d{4}-\d{2}-\d{2}) (?<level>\w+): (?<message>.*)/

def logMatcher = (logEntry =~ logPattern)
if (logMatcher.find()) {
    println "日期: ${logMatcher.group('date')}"
    println "级别: ${logMatcher.group('level')}"
    println "消息: ${logMatcher.group('message')}"
}

// ==================== 高级用法 ====================

// 14. 分割字符串
println "\n=== 分割字符串 ==="
def csvData = "apple,banana,cherry,date"
def items = csvData.split(/,\s*/)
println "分割结果: ${items}"

def complexSplit = "one,two;three|four".split(/[;,|]/)
println "复杂分割: ${complexSplit}"

// 15. 去除空白
println "\n=== 去除空白 ==="
def messyText = "  Hello   World  "
def trimmed = messyText.trim()
def noSpaces = messyText.replaceAll(/\s+/, ' ').trim()

println "原始: '${messyText}'"
println "trim: '${trimmed}'"
println "去多余空格: '${noSpaces}'"

// 16. 验证密码强度
println "\n=== 密码强度验证 ==="
def strongPassword = ~/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/

def passwords = [
        "Password1!",
        "weak",
        "NoSpecial1",
        "Strong@Pass1"
]

passwords.each { pwd ->
    def isStrong = pwd ==~ strongPassword
    println "${pwd}: ${isStrong ? '✓ 强密码' : '✗ 弱密码'}"
}

// 17. HTML标签提取
println "\n=== HTML标签提取 ==="
def html = "<div><p>Hello</p><span>World</span></div>"
def tagPattern = /<(\w+)[^>]*>(.*?)<\/\1>/

def tagMatcher = (html =~ tagPattern)
while (tagMatcher.find()) {
    println "标签: ${tagMatcher.group(1)}, 内容: ${tagMatcher.group(2)}"
}

// 18. IP地址验证
println "\n=== IP地址验证 ==="
def ipPattern = /^(\d{1,3}\.){3}\d{1,3}$/

def ips = [
        "192.168.1.1",
        "255.255.255.255",
        "999.999.999.999",
        "10.0.0.1"
]

ips.each { ip ->
    def isValid = ip ==~ ipPattern
    if (isValid) {
        // 进一步验证每个段是否在0-255之间
        def parts = ip.split(/\./)
        def allValid = parts.every { it.toInteger() >= 0 && it.toInteger() <= 255 }
        println "${ip}: ${allValid ? '✓ 有效' : '✗ 无效'}"
    } else {
        println "${ip}: ✗ 无效"
    }
}

// 19. 驼峰命名转换
println "\n=== 驼峰命名转换 ==="
def toSnakeCase = { camelCase ->
    return camelCase.replaceAll(/([a-z])([A-Z])/, /\$1_\$2/).toLowerCase()
}

def toCamelCase = { snakeCase ->
    return snakeCase.replaceAll(/_([a-z])/) { match -> match[1].toUpperCase() }
}

def camel = "myVariableName"
def snake = toSnakeCase(camel)
def backToCamel = toCamelCase(snake)

println "驼峰: ${camel}"
println "下划线: ${snake}"
println "转回驼峰: ${backToCamel}"

// 20. 统计字符出现次数（修复版本）
println "\n=== 统计字符出现次数 ==="
def text2 = "Hello World"

// 方法1：使用 count 方法统计特定字符
def charCount = text2.count('l')
println "'${text2}' 中 'l' 出现了 ${charCount} 次"

// 方法2：使用正则表达式统计元音字母（不区分大小写）
def vowelCount = text2.findAll(~/[aeiou]/).size()  // 使用 ~// 语法
println "'${text2}' 中元音字母出现了 ${vowelCount} 次"

// 方法3：使用内联标志 (?i) 进行不区分大小写的匹配
def vowelCountIgnoreCase = text2.findAll(/(?i)[aeiou]/).size()
println "'${text2}' 中元音字母（不区分大小写）出现了 ${vowelCountIgnoreCase} 次"

// 额外示例：统计指定字符集
println "\n=== 额外统计示例 ==="
def statsText = "The quick brown fox jumps over the lazy dog"

// 统计元音（不区分大小写）
def vowels = statsText.findAll(/(?i)[aeiou]/).size()
println "元音字母数量: ${vowels}"

// 统计辅音（不区分大小写）
def consonants = statsText.findAll(/(?i)[^aeiou\s\W\d]/).size()
println "辅音字母数量: ${consonants}"

// 统计单词数
def wordCount = statsText.findAll(/\b\w+\b/).size()
println "单词数量: ${wordCount}"