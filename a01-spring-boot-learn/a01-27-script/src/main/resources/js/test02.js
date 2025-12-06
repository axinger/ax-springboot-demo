function sum(a, b) {
    return a + b + 10;
}

// 假设我们导出函数或直接执行逻辑
let result = sum;
console.log('js日志 result==' + result);
// 最后一行是函数声明，返回函数对象
result
