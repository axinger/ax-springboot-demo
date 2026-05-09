// Groovy 基本变量和字符串操作示例

// 1. 变量声明
def name = "Groovy"
def version = 4.0
def isActive = true

// 2. 字符串插值
def greeting = "Hello, ${name}! Version: ${version}"
println greeting

// 3. 多行字符串
def multiline = """
    This is a
    multiline string
    with ${name} interpolation
"""
println multiline.trim()

// 4. 单引号字符串（不插值）
def singleQuote = 'This is ${name}'  // 不会插值
println singleQuote

// 5. 三单引号多行字符串（不插值）
def tripleSingle = '''
    This is a
    triple single quote
    string
'''
println tripleSingle.trim()

// 6. 字符串常用方法
def text = "Hello World"
println text.toUpperCase()
println text.toLowerCase()
println text.contains("World")
println text.startsWith("Hello")
println text.endsWith("World")
