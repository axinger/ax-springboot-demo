package com.axinger.notification.web;

import com.axinger.notification.NotificationModule;
import com.axinger.notification.application.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通知控制器 - REST API 端点
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 发送邮件通知
     */
    @PostMapping("/email")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse sendEmail(@RequestBody SendEmailRequest request) {
        NotificationModule.Notification notification = notificationService.sendEmailNotification(
                request.recipient(),
                request.subject(),
                request.content(),
                NotificationModule.NotificationPriority.valueOf(request.priority())
        );

        return toNotificationResponse(notification);
    }

    /**
     * 发送短信通知
     */
    @PostMapping("/sms")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse sendSms(@RequestBody SendSmsRequest request) {
        NotificationModule.Notification notification = notificationService.sendSmsNotification(
                request.phoneNumber(),
                request.content(),
                NotificationModule.NotificationPriority.valueOf(request.priority())
        );

        return toNotificationResponse(notification);
    }

    /**
     * 发送应用内通知
     */
    @PostMapping("/in-app")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse sendInApp(@RequestBody SendInAppRequest request) {
        NotificationModule.Notification notification = notificationService.sendInAppNotification(
                request.userId(),
                request.title(),
                request.content(),
                NotificationModule.NotificationPriority.valueOf(request.priority())
        );

        return toNotificationResponse(notification);
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/{notificationId}/mark-read")
    public NotificationResponse markAsRead(@PathVariable String notificationId) {
        NotificationModule.NotificationId id = NotificationModule.NotificationId.fromString(notificationId);
        NotificationModule.Notification notification = notificationService.markAsRead(id);

        return toNotificationResponse(notification);
    }

    /**
     * 获取通知详情
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable String notificationId) {
        NotificationModule.NotificationId id = NotificationModule.NotificationId.fromString(notificationId);
        Optional<NotificationModule.Notification> notification = notificationService.findNotificationById(id);

//        return notification.map(n -> ResponseEntity.ok(toNotificationResponse(n)))
//                .orElse(ResponseEntity.notFound().build());
        return null;
    }

    /**
     * 获取用户通知列表
     */
    @GetMapping("/user/{recipient}")
    public NotificationListResponse getUserNotifications(
            @PathVariable String recipient,
            @RequestParam(required = false) String status) {

        NotificationModule.NotificationStatus filterStatus = null;
        if (status != null && !status.isEmpty()) {
            filterStatus = NotificationModule.NotificationStatus.valueOf(status);
        }

        List<NotificationModule.Notification> notifications = notificationService.getUserNotifications(recipient, filterStatus);

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());

        return new NotificationListResponse(notificationResponses, notificationResponses.size());
    }

    /**
     * 获取用户未读通知数量
     */
    @GetMapping("/user/{recipient}/unread-count")
    public UnreadCountResponse getUnreadCount(@PathVariable String recipient) {
        long count = notificationService.getUnreadNotificationCount(recipient);
        return new UnreadCountResponse(recipient, count);
    }

    // 响应对象转换方法
    private NotificationResponse toNotificationResponse(NotificationModule.Notification notification) {
        return new NotificationResponse(
                notification.id().toString(),
                notification.recipient(),
                notification.subject(),
                notification.content(),
                notification.type().name(),
                notification.priority().name(),
                notification.status().name(),
                notification.createdAt(),
                notification.sentAt()
        );
    }

    // 请求/响应对象
    public record SendEmailRequest(
            String recipient,
            String subject,
            String content,
            String priority
    ) {}

    public record SendSmsRequest(
            String phoneNumber,
            String content,
            String priority
    ) {}

    public record SendInAppRequest(
            String userId,
            String title,
            String content,
            String priority
    ) {}

    public record NotificationResponse(
            String notificationId,
            String recipient,
            String subject,
            String content,
            String type,
            String priority,
            String status,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime sentAt
    ) {}

    public record NotificationListResponse(
            List<NotificationResponse> notifications,
            int totalCount
    ) {}

    public record UnreadCountResponse(
            String recipient,
            long unreadCount
    ) {}
}