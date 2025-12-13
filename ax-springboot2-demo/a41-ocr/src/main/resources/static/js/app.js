$(document).ready(function () {
    $('#uploadForm').on('submit', function (event) {
        event.preventDefault();

        // 获取文件输入
        var fileInput = $('#fileInput')[0].files[0];

        // 检查是否选择了文件
        if (!fileInput) {
            alert("请选择一个文件");
            return;
        }

        // 创建 FormData 对象
        var formData = new FormData();
        formData.append('file', fileInput);

        // 使用 jQuery 的 AJAX 发送 POST 请求
        $.ajax({
            url: '/api/ocr/extract',
            type: 'POST',
            data: formData,
            contentType: false, // 不设置内容类型，让浏览器自动处理
            processData: false, // 不处理数据，让它保持原样
            success: function (result) {
                // 在页面上显示识别结果
                $('#result').text(result);
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                alert('识别失败，请稍后重试。');
            }
        });
    });
});
