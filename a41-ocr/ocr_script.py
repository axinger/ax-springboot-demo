import easyocr
import sys


def extract_text_from_image(image_path):
    # 初始化 EasyOCR Reader，支持中文和英文
    reader = easyocr.Reader(['ch_sim', 'en'])  # 'ch_sim' 用于简体中文，'ch_tra' 用于繁体中文
    results = reader.readtext(image_path)

    text = ""
    for result in results:
        text += result[1] + "\n"
    return text


if __name__ == "__main__":
    image_path = sys.argv[1]  # 从命令行参数获取图片路径
    text = extract_text_from_image(image_path)
    print(text)  # 输出识别结果
