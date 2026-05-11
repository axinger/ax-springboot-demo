package com.axinger.notification;

import org.springframework.modulith.ApplicationModule;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 通知模块 - Spring Modulith 应用模块
 */
@ApplicationModule(
    displayName = "通知模块"
)
public class NotificationModule {

    /**
     * 通知聚合根
     */
    public record Notification(
            NotificationId id,
            String recipient,
            String subject,
            String content,
            NotificationType type,
            NotificationPriority priority,
            NotificationStatus status,
            LocalDateTime createdAt,
            LocalDateTime sentAt
    ) {

        public Notification withStatus(NotificationStatus newStatus) {
            return new Notification(
                    this.id,
                    this.recipient,
                    this.subject,
                    this.content,
                    this.type,
                    this.priority,
                    newStatus,
                    this.createdAt,
                    newStatus == NotificationStatus.SENT ? LocalDateTime.now() : this.sentAt
            );
        }

        public boolean isHighPriority() {
            return this.priority == NotificationPriority.HIGH || this.priority == NotificationPriority.URGENT;
        }

        public boolean canBeSent() {
            return this.status == NotificationStatus.PENDING;
        }

        public boolean shouldBeRetried() {
            return this.status == NotificationStatus.FAILED &&
                   this.priority != NotificationPriority.LOW;
        }
    }

    /**
     * 通知ID值对象
     */
    public record NotificationId(String value) {

        public NotificationId {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("通知ID不能为空");
            }
        }

        public static NotificationId create() {
            return new NotificationId("NOTIFY_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000));
        }

        public static NotificationId fromString(String id) {
            return new NotificationId(id);
        }
    }

    /**
     * 通知内容值对象
     */
    public record NotificationContent(
            String subject,
            String body,
            String templateId,
            java.util.Map<String, Object> templateVariables
    ) {

        public NotificationContent {
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("通知主题不能为空");
            }
            if (body == null || body.trim().isEmpty()) {
                throw new IllegalArgumentException("通知内容不能为空");
            }
        }

        public String getFormattedBody() {
            if (templateVariables == null || templateVariables.isEmpty()) {
                return body;
            }

            String formattedBody = body;
            for (var entry : templateVariables.entrySet()) {
                formattedBody = formattedBody.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }
            return formattedBody;
        }
    }

    /**
     * 邮件通知实体
     */
    public record EmailNotification(
            NotificationId notificationId,
            String from,
            String to,
            String cc,
            String bcc,
            String subject,
            String htmlContent,
            boolean isHtml
    ) {

        public EmailNotification {
            if (from == null || from.trim().isEmpty()) {
                throw new IllegalArgumentException("发件人不能为空");
            }
            if (to == null || to.trim().isEmpty()) {
                throw new IllegalArgumentException("收件人不能为空");
            }
        }

        public boolean hasCc() {
            return cc != null && !cc.trim().isEmpty();
        }

        public boolean hasBcc() {
            return bcc != null && !bcc.trim().isEmpty();
        }
    }

    /**
     * 短信通知实体
     */
    public record SmsNotification(
            NotificationId notificationId,
            String phoneNumber,
            String content,
            String signature
    ) {

        public SmsNotification {
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("手机号不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("短信内容不能为空");
            }
        }

        public String getFullContent() {
            return (signature != null && !signature.trim().isEmpty()) ?
                   signature + "：" + content : content;
        }
    }

    public enum NotificationType {
        EMAIL,          // 邮件
        SMS,            // 短信
        PUSH,           // 推送
        IN_APP,         // 应用内通知
        WECHAT,         // 微信
        DINGTALK        // 钉钉
    }

    public enum NotificationPriority {
        LOW,            // 低优先级
        NORMAL,         // 普通
        HIGH,           // 高优先级
        URGENT          // 紧急
    }

    public enum NotificationStatus {
        PENDING,        // 待发送
        PROCESSING,     // 处理中
        SENT,           // 已发送
        DELIVERED,      // 已送达
        READ,           // 已读
        FAILED,         // 发送失败
        CANCELLED       // 已取消
    }
}