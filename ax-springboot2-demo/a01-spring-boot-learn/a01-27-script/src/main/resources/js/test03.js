function sum(a, b) {
    return a + b;
}

function multiply(a, b) {
    return a * b;
}

function add(params) {
    console.log('参数map=' + JSON.stringify(params))
    // 从params对象中根据key取值
    const a = params.a || 0;
    const b = params.b || 0;
    return a + b
}

function add2(list) {
    console.log('参数list=' + list)
    return list[0] + list[1]
}

// 不需要最后一行返回值
