import json
import math
import sys


def test1():
    # 获取Java传递的参数
    args = sys.argv[1:]  # 第一个参数是脚本名，后面的才是真正的参数

    print(f"Python脚本接收到参数: {args}")

    # 简单的数据处理示例
    if len(args) >= 2:
        try:
            num1 = float(args[0])
            num2 = float(args[1])

            # 计算并返回结果
            result = {
                "sum": num1 + num2,
                "difference": num1 - num2,
                "product": num1 * num2,
                "quotient": num1 / num2 if num2 != 0 else "undefined",
                "power": math.pow(num1, num2),
                "sqrt_num1": math.sqrt(abs(num1)),
                "sqrt_num2": math.sqrt(abs(num2))
            }

            # 输出JSON格式的结果，方便Java解析
            # print(json.dumps(result))

        except ValueError as e:
            print(f"错误: 参数必须是数字 ({e})")
    else:
        print("用法: python test01.py <数字1> <数字2>")


if __name__ == "__main__":
    test1()
