package com.axinger.notification.domain;

import com.axinger.notification.NotificationModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 通知仓储接口
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationModule.Notification, NotificationModule.NotificationId> {

    /**
     * 根据接收者查找通知
     */
    List<NotificationModule.Notification> findByRecipient(String recipient);

    /**
     * 根据接收者和状态查找通知
     */
    List<NotificationModule.Notification> findByRecipientAndStatus(
            String recipient,
            NotificationModule.NotificationStatus status
    );

    /**
     * 根据类型查找通知
     */
    List<NotificationModule.Notification> findByType(NotificationModule.NotificationType type);

    /**
     * 根据状态查找通知
     */
    List<NotificationModule.Notification> findByStatus(NotificationModule.NotificationStatus status);

    /**
     * 根据优先级查找通知
     */
    List<NotificationModule.Notification> findByPriority(NotificationModule.NotificationPriority priority);

    /**
     * 统计接收者的特定状态通知数量
     */
    long countByRecipientAndStatus(String recipient, NotificationModule.NotificationStatus status);

    /**
     * 统计接收者的所有通知数量
     */
    long countByRecipient(String recipient);

    /**
     * 查找指定时间范围内创建的通知
     */
    List<NotificationModule.Notification> findByCreatedAtBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    /**
     * 查找超时未发送的通知
     */
    @Query("SELECT n FROM NotificationModule.Notification n WHERE n.status = :status AND n.createdAt < :timeoutTime")
    List<NotificationModule.Notification> findTimeoutNotifications(
            @Param("status") NotificationModule.NotificationStatus status,
            @Param("timeoutTime") LocalDateTime timeoutTime
    );

    /**
     * 查找需要重试的失败通知
     */
    List<NotificationModule.Notification> findByStatusAndType(
            NotificationModule.NotificationStatus status,
            NotificationModule.NotificationType type
    );

    /**
     * 查找用户的未读通知数量（应用内通知）
     */
    @Query("SELECT COUNT(n) FROM NotificationModule.Notification n WHERE n.recipient = :recipient AND n.status = :status AND n.type = :type")
    long countUnreadInAppNotifications(
            @Param("recipient") String recipient,
            @Param("status") NotificationModule.NotificationStatus status,
            @Param("type") NotificationModule.NotificationType type
    );

    /**
     * 删除指定时间之前的已读通知
     */
    long deleteByStatusAndCreatedAtBefore(
            NotificationModule.NotificationStatus status,
            LocalDateTime beforeDate
    );

    /**
     * 检查接收者是否有特定类型的通知
     */
    boolean existsByRecipientAndTypeAndStatus(
            String recipient,
            NotificationModule.NotificationType type,
            NotificationModule.NotificationStatus status
    );
}