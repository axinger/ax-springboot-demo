package com.axinger.notification.application;

import com.axinger.notification.NotificationModule;
import com.axinger.notification.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 通知应用服务 - 处理通知业务逻辑
 */
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationService(
            NotificationRepository notificationRepository,
            ApplicationEventPublisher eventPublisher,
            JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
        this.mailSender = mailSender;
    }

    /**
     * 发送邮件通知
     */
    public NotificationModule.Notification sendEmailNotification(
            String recipientEmail,
            String subject,
            String content,
            NotificationModule.NotificationPriority priority) {

        NotificationModule.Notification notification = createNotification(
                recipientEmail,
                subject,
                content,
                NotificationModule.NotificationType.EMAIL,
                priority
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);

            // 标记为已发送
            NotificationModule.Notification sentNotification = notification.withStatus(
                    NotificationModule.NotificationStatus.SENT
            );

            NotificationModule.Notification savedNotification = notificationRepository.save(sentNotification);

            // 发布通知发送事件
            eventPublisher.publishEvent(new NotificationSentEvent(
                    savedNotification.id(),
                    recipientEmail,
                    NotificationModule.NotificationType.EMAIL
            ));

            return savedNotification;

        } catch (Exception e) {
            // 标记为发送失败
            NotificationModule.Notification failedNotification = notification.withStatus(
                    NotificationModule.NotificationStatus.FAILED
            );

            notificationRepository.save(failedNotification);

            // 发布通知失败事件
            eventPublisher.publishEvent(new NotificationFailedEvent(
                    notification.id(),
                    recipientEmail,
                    e.getMessage()
            ));

            throw new NotificationSendException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送短信通知（模拟实现）
     */
    public NotificationModule.Notification sendSmsNotification(
            String phoneNumber,
            String content,
            NotificationModule.NotificationPriority priority) {

        NotificationModule.Notification notification = createNotification(
                phoneNumber,
                "短信通知",
                content,
                NotificationModule.NotificationType.SMS,
                priority
        );

        try {
            // 模拟短信发送
            simulateSmsSending(phoneNumber, content);

            // 标记为已发送
            NotificationModule.Notification sentNotification = notification.withStatus(
                    NotificationModule.NotificationStatus.SENT
            );

            NotificationModule.Notification savedNotification = notificationRepository.save(sentNotification);

            // 发布通知发送事件
            eventPublisher.publishEvent(new NotificationSentEvent(
                    savedNotification.id(),
                    phoneNumber,
                    NotificationModule.NotificationType.SMS
            ));

            return savedNotification;

        } catch (Exception e) {
            // 标记为发送失败
            NotificationModule.Notification failedNotification = notification.withStatus(
                    NotificationModule.NotificationStatus.FAILED
            );

            notificationRepository.save(failedNotification);

            // 发布通知失败事件
            eventPublisher.publishEvent(new NotificationFailedEvent(
                    notification.id(),
                    phoneNumber,
                    e.getMessage()
            ));

            throw new NotificationSendException("短信发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送应用内通知
     */
    public NotificationModule.Notification sendInAppNotification(
            String userId,
            String title,
            String content,
            NotificationModule.NotificationPriority priority) {

        NotificationModule.Notification notification = createNotification(
                userId,
                title,
                content,
                NotificationModule.NotificationType.IN_APP,
                priority
        );

        NotificationModule.Notification savedNotification = notificationRepository.save(notification);

        // 发布通知发送事件
        eventPublisher.publishEvent(new NotificationSentEvent(
                savedNotification.id(),
                userId,
                NotificationModule.NotificationType.IN_APP
        ));

        return savedNotification;
    }

    /**
     * 标记通知为已读
     */
    public NotificationModule.Notification markAsRead(NotificationModule.NotificationId notificationId) {
        NotificationModule.Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("通知未找到: " + notificationId));

        if (notification.type() != NotificationModule.NotificationType.IN_APP) {
            throw new IllegalArgumentException("只有应用内通知可以标记为已读");
        }

        NotificationModule.Notification readNotification = notification.withStatus(
                NotificationModule.NotificationStatus.READ
        );

        return notificationRepository.save(readNotification);
    }

    /**
     * 获取用户未读通知数量
     */
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(String userId) {
        return notificationRepository.countByRecipientAndStatus(
                userId,
                NotificationModule.NotificationStatus.SENT
        );
    }

    /**
     * 获取用户的通知列表
     */
    @Transactional(readOnly = true)
    public java.util.List<NotificationModule.Notification> getUserNotifications(
            String userId,
            NotificationModule.NotificationStatus status) {

        if (status != null) {
            return notificationRepository.findByRecipientAndStatus(userId, status);
        } else {
            return notificationRepository.findByRecipient(userId);
        }
    }

    /**
     * 根据ID查找通知
     */
    @Transactional(readOnly = true)
    public Optional<NotificationModule.Notification> findNotificationById(NotificationModule.NotificationId notificationId) {
        return notificationRepository.findById(notificationId);
    }

    // 创建通知对象
    private NotificationModule.Notification createNotification(
            String recipient,
            String subject,
            String content,
            NotificationModule.NotificationType type,
            NotificationModule.NotificationPriority priority) {

        return new NotificationModule.Notification(
                NotificationModule.NotificationId.create(),
                recipient,
                subject,
                content,
                type,
                priority,
                NotificationModule.NotificationStatus.PENDING,
                LocalDateTime.now(),
                null
        );
    }

    // 模拟短信发送
    private void simulateSmsSending(String phoneNumber, String content) {
        // 模拟发送延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 模拟10%的失败率
        if (Math.random() < 0.1) {
            throw new RuntimeException("短信网关暂时不可用");
        }
    }

    // 领域事件定义
    public record NotificationSentEvent(
            NotificationModule.NotificationId notificationId,
            String recipient,
            NotificationModule.NotificationType type
    ) {}

    public record NotificationFailedEvent(
            NotificationModule.NotificationId notificationId,
            String recipient,
            String reason
    ) {}

    // 异常类
    public static class NotificationNotFoundException extends RuntimeException {
        public NotificationNotFoundException(String message) {
            super(message);
        }
    }

    public static class NotificationSendException extends RuntimeException {
        public NotificationSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}