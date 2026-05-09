// Groovy 日期和时间处理示例

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.Duration
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.DayOfWeek
import java.time.temporal.ChronoUnit

// ==================== 基本日期时间 ====================

// 1. 获取当前日期时间
println "=== 当前日期时间 ==="
def now = LocalDateTime.now()
def today = LocalDate.now()
def currentTime = LocalTime.now()

println "当前日期时间: ${now}"
println "当前日期: ${today}"
println "当前时间: ${currentTime}"

// 2. 创建特定日期时间
println "\n=== 创建特定日期时间 ==="
def specificDate = LocalDate.of(2024, 1, 15)
def specificDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0)
def specificTime = LocalTime.of(14, 30, 0)

println "特定日期: ${specificDate}"
println "特定日期时间: ${specificDateTime}"
println "特定时间: ${specificTime}"

// 3. 解析字符串为日期
println "\n=== 解析日期字符串 ==="
def dateString = "2024-01-15"
def parsedDate = LocalDate.parse(dateString)
println "解析结果: ${parsedDate}"

def dateTimeString = "2024-01-15T10:30:00"
def parsedDateTime = LocalDateTime.parse(dateTimeString)
println "解析结果: ${parsedDateTime}"

// ==================== 日期格式化 ====================

// 4. 格式化日期时间
println "\n=== 格式化日期时间 ==="
def formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
def formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
def formatter3 = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")

println "格式1: ${today.format(formatter1)}"
println "格式2: ${now.format(formatter2)}"
println "格式3: ${now.format(formatter3)}"

// 5. 中文格式化
println "\n=== 中文格式化 ==="
def chineseFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒")
println "中文格式: ${now.format(chineseFormatter)}"

def weekFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE", Locale.CHINA)
println "带星期: ${today.format(weekFormatter)}"

// ==================== 日期计算 ====================

// 6. 日期加减
println "\n=== 日期加减 ==="
def tomorrow = today.plusDays(1)
def yesterday = today.minusDays(1)
def nextWeek = today.plusWeeks(1)
def lastMonth = today.minusMonths(1)
def nextYear = today.plusYears(1)

println "明天: ${tomorrow}"
println "昨天: ${yesterday}"
println "下周: ${nextWeek}"
println "上月: ${lastMonth}"
println "明年: ${nextYear}"

// 7. 时间加减
println "\n=== 时间加减 ==="
def oneHourLater = now.plusHours(1)
def thirtyMinAgo = now.minusMinutes(30)
def twoDaysLater = now.plusDays(2)

println "1小时后: ${oneHourLater}"
println "30分钟前: ${thirtyMinAgo}"
println "2天后: ${twoDaysLater}"

// 8. 使用 Period 和 Duration
println "\n=== Period 和 Duration ==="
def period = Period.ofDays(5).plusMonths(2)
def futureDate = today.plus(period)
println "加2个月5天: ${futureDate}"

def duration = Duration.ofHours(3).plusMinutes(30)
def futureTime = now.plus(duration)
println "加3小时30分钟: ${futureTime}"

// ==================== 日期比较 ====================

// 9. 日期比较
println "\n=== 日期比较 ==="
def date1 = LocalDate.of(2024, 1, 15)
def date2 = LocalDate.of(2024, 2, 20)

println "${date1} 在 ${date2} 之前? ${date1.isBefore(date2)}"
println "${date1} 在 ${date2} 之后? ${date1.isAfter(date2)}"
println "${date1} 等于 ${date2}? ${date1.isEqual(date2)}"

// 10. 计算日期差
println "\n=== 计算日期差 ==="
def daysBetween = ChronoUnit.DAYS.between(date1, date2)
def weeksBetween = ChronoUnit.WEEKS.between(date1, date2)
def monthsBetween = ChronoUnit.MONTHS.between(date1, date2)

println "天数差: ${daysBetween}"
println "周数差: ${weeksBetween}"
println "月数差: ${monthsBetween}"

// 11. 计算时间差
println "\n=== 计算时间差 ==="
def time1 = LocalDateTime.of(2024, 1, 15, 10, 0, 0)
def time2 = LocalDateTime.of(2024, 1, 15, 15, 30, 0)

def hoursBetween = ChronoUnit.HOURS.between(time1, time2)
def minutesBetween = ChronoUnit.MINUTES.between(time1, time2)

println "小时差: ${hoursBetween}"
println "分钟差: ${minutesBetween}"

// ==================== 日期属性访问 ====================

// 12. 获取日期属性
println "\n=== 日期属性 ==="
println "年份: ${today.year}"
println "月份: ${today.monthValue}"
println "月份名称: ${today.month}"
println "日: ${today.dayOfMonth}"
println "星期: ${today.dayOfWeek}"
println "一年中的第几天: ${today.dayOfYear}"

// 13. 获取时间属性
println "\n=== 时间属性 ==="
println "小时: ${now.hour}"
println "分钟: ${now.minute}"
println "秒: ${now.second}"
println "纳秒: ${now.nano}"

// ==================== 特殊日期计算 ====================

// 14. 月初月末
println "\n=== 月初月末 ==="
def firstDayOfMonth = today.withDayOfMonth(1)
def lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth())

println "本月第一天: ${firstDayOfMonth}"
println "本月最后一天: ${lastDayOfMonth}"

// 15. 年初年末
println "\n=== 年初年末 ==="
def firstDayOfYear = today.withDayOfYear(1)
def lastDayOfYear = today.withDayOfYear(today.lengthOfYear())

println "本年第一天: ${firstDayOfYear}"
println "本年最后一天: ${lastDayOfYear}"

// 16. 下一个星期几
println "\n=== 下一个星期几 ==="
def nextMonday = today.with(DayOfWeek.MONDAY)
if (nextMonday.isBefore(today) || nextMonday.isEqual(today)) {
    nextMonday = nextMonday.plusWeeks(1)
}
println "下一个星期一: ${nextMonday}"

// 17. 判断闰年
println "\n=== 闰年判断 ==="
def year = 2024
def isLeapYear = LocalDate.of(year, 1, 1).isLeapYear()
println "${year}年是闰年吗? ${isLeapYear ? '是' : '否'}"

def year2 = 2023
def isLeapYear2 = LocalDate.of(year2, 1, 1).isLeapYear()
println "${year2}年是闰年吗? ${isLeapYear2 ? '是' : '否'}"

// ==================== 实际应用场景 ====================

// 18. 计算年龄
println "\n=== 计算年龄 ==="
def birthDate = LocalDate.of(1990, 5, 15)
def age = Period.between(birthDate, today).years
println "出生日期: ${birthDate}"
println "年龄: ${age}岁"

// 19. 工作日判断
println "\n=== 工作日判断 ==="
def isWeekend = today.dayOfWeek in [DayOfWeek.SATURDAY, DayOfWeek.SUNDAY]
println "今天是周末吗? ${isWeekend ? '是' : '否'}"
println "今天是: ${today.dayOfWeek}"

// 20. 生成日期范围
println "\n=== 生成日期范围 ==="
def startDate = LocalDate.of(2024, 1, 1)
def endDate = LocalDate.of(2024, 1, 10)

def dateRange = []
def currentDate = startDate
while (!currentDate.isAfter(endDate)) {
    dateRange << currentDate
    currentDate = currentDate.plusDays(1)
}
println "日期范围: ${dateRange}"

// 21. 倒计时
println "\n=== 倒计时 ==="
def targetDate = LocalDate.of(2024, 12, 31)
def daysUntilTarget = ChronoUnit.DAYS.between(today, targetDate)
println "距离 ${targetDate} 还有 ${daysUntilTarget} 天"

// 22. 时区时间
println "\n=== 时区时间 ==="
def beijingTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
def newYorkTime = ZonedDateTime.now(ZoneId.of("America/New_York"))
def londonTime = ZonedDateTime.now(ZoneId.of("Europe/London"))

println "北京时间: ${beijingTime.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'))}"
println "纽约时间: ${newYorkTime.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'))}"
println "伦敦时间: ${londonTime.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm:ss'))}"

// 23. 时间戳转换
println "\n=== 时间戳转换 ==="
def timestamp = System.currentTimeMillis()
def fromTimestamp = LocalDateTime.ofInstant(
    java.time.Instant.ofEpochMilli(timestamp), 
    ZoneId.systemDefault()
)
println "时间戳: ${timestamp}"
println "转换后: ${fromTimestamp}"

// 24. 日期验证
println "\n=== 日期验证 ==="
def isValidDate = { dateStr ->
    try {
        LocalDate.parse(dateStr)
        return true
    } catch (Exception e) {
        return false
    }
}

println "2024-01-15 有效? ${isValidDate('2024-01-15')}"
println "2024-02-30 有效? ${isValidDate('2024-02-30')}"
println "invalid 有效? ${isValidDate('invalid')}"
