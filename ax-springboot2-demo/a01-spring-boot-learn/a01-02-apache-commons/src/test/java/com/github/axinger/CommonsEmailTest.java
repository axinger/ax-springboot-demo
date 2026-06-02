package com.github.axinger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.jupiter.api.Test;

/**
 * Commons Email 邮件发送示例
 * 注意：实际发送需要配置正确的 SMTP 服务器信息
 */
class CommonsEmailTest {

    // 测试配置（请替换为实际的 SMTP 配置）
    private static final String SMTP_HOST = "smtp.example.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "your-email@example.com";
    private static final String PASSWORD = "your-password";
    private static final String FROM_EMAIL = "your-email@example.com";
    private static final String TO_EMAIL = "recipient@example.com";

    @Test
    void testSimpleEmail_build() throws EmailException {
        // 构建纯文本邮件（不实际发送）
        SimpleEmail email = new SimpleEmail();
        email.setHostName(SMTP_HOST);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthentication(USERNAME, PASSWORD);
        email.setStartTLSEnabled(true);

        email.setFrom(FROM_EMAIL);
        email.addTo(TO_EMAIL);
        email.setSubject("测试邮件主题");
        email.setMsg("这是一封测试邮件的内容。\n第二行内容。");

        // 获取邮件内容用于验证
        System.out.println("邮件主题: " + email.getSubject());
        System.out.println("发件人: " + email.getFromAddress());
        System.out.println("收件人: " + email.getToAddresses());
        System.out.println("邮件内容: 这是一封测试邮件的内容。\\n第二行内容。");
    }

    @Test
    void testHtmlEmail_build() throws EmailException {
        // 构建 HTML 邮件（不实际发送）
        HtmlEmail email = new HtmlEmail();
        email.setHostName(SMTP_HOST);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthentication(USERNAME, PASSWORD);
        email.setStartTLSEnabled(true);

        email.setFrom(FROM_EMAIL);
        email.addTo(TO_EMAIL);
        email.setSubject("HTML 测试邮件");

        // 设置 HTML 内容
        String htmlContent = "<html>" +
                "<body>" +
                "<h1>HTML 邮件测试</h1>" +
                "<p style='color: blue;'>这是一封 <b>HTML</b> 格式的邮件。</p>" +
                "<ul><li>列表项 1</li><li>列表项 2</li></ul>" +
                "</body></html>";

        email.setHtmlMsg(htmlContent);
        // 设置纯文本备用内容（不支持 HTML 的客户端会显示这个）
        email.setTextMsg("您的邮件客户端不支持 HTML 格式");

        System.out.println("HTML 邮件构建成功");
        System.out.println("主题: " + email.getSubject());
    }

    @Test
    void testEmailWithAttachment_build() throws EmailException {
        // 带附件的邮件构建示例
        HtmlEmail email = new HtmlEmail();
        email.setHostName(SMTP_HOST);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthentication(USERNAME, PASSWORD);

        email.setFrom(FROM_EMAIL);
        email.addTo(TO_EMAIL);
        email.setSubject("带附件的邮件");
        email.setMsg("请查收附件。");

        // 添加附件（实际使用时取消注释）
        // email.attach(new File("/path/to/file.pdf"));
        // email.attach(new URL("https://example.com/file.pdf"), "file.pdf", "文件描述");

        System.out.println("带附件邮件构建成功");
    }

    @Test
    void testEmailWithMultipleRecipients() throws EmailException {
        // 多收件人、抄送、密送
        SimpleEmail email = new SimpleEmail();
        email.setHostName(SMTP_HOST);

        email.setFrom(FROM_EMAIL);
        email.addTo("user1@example.com");
        email.addTo("user2@example.com");
        email.addCc("cc@example.com");      // 抄送
        email.addBcc("bcc@example.com");    // 密送
        email.setSubject("群发邮件测试");
        email.setMsg("这是一封发给多人的测试邮件。");

        System.out.println("收件人: " + email.getToAddresses());
        System.out.println("抄送: " + email.getCcAddresses());
        System.out.println("密送: " + email.getBccAddresses());
    }
}
