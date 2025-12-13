import json
import sys


def process_data(data):
    """处理数据的 Python 3 函数"""

    # 使用 Python 3 特性
    # result['hobby_count'] = len(data['hobbies'])
    # result['uppercase_name'] = data['name'].upper()

    # 添加处理信息

    return {'greeting': f"Hello, {data.get('name', '未知')}!",
            'is_adult': '成年' if data.get('age', 0) >= 18 else '未成年',
            'hobbies': len(data.get('hobbies', [])),
            'uppercase_name': data.get('name', '').upper(),
            'processed_with': 'Python 3',
            'python_version': sys.version.split()[0]
            }


if __name__ == "__main__":
    try:
        # 读取 JSON 参数
        params = json.loads(sys.argv[1])
        # 处理数据
        result = process_data(params)
        # 输出 JSON 结果
        print(json.dumps(result, ensure_ascii=False))
    except Exception as e:
        # 错误处理
        error_result = {'error': str(e), 'success': False}
        print(json.dumps(error_result))
        sys.exit(1)
