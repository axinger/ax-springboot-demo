package com.axinger.modulith.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 事件监控服务（简化版）
 * 
 * 通过直接查询 EVENT_PUBLICATION 表来监控事件状态
 * 不依赖 Spring Modulith 内部 API，更加稳定
 */
@Slf4j
@Service
public class EventMonitoringService {

    private final JdbcTemplate jdbcTemplate;

    public EventMonitoringService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 每5分钟检查一次未完成的事件
     */
    @Scheduled(fixedRate = 300000) // 5分钟
    public void checkIncompleteEvents() {
        try {
            // 查询待处理事件数量
            String sql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            
            if (count != null && count > 0) {
                log.warn("发现 {} 个未完成的事件", count);
                
                // 查询超时事件（超过1小时）
                String overdueSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION " +
                                  "WHERE completion_date IS NULL " +
                                  "AND publication_date < ?";
                LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
                Integer overdueCount = jdbcTemplate.queryForObject(overdueSql, Integer.class, oneHourAgo);
                
                if (overdueCount != null && overdueCount > 0) {
                    log.error("发现 {} 个超时事件（超过1小时未处理）", overdueCount);
                    
                    // 查询具体的超时事件类型
                    String eventTypesSql = "SELECT event_type, COUNT(*) as count " +
                                         "FROM EVENT_PUBLICATION " +
                                         "WHERE completion_date IS NULL " +
                                         "AND publication_date < ? " +
                                         "GROUP BY event_type";
                    
                    jdbcTemplate.queryForList(eventTypesSql, oneHourAgo)
                        .forEach(row -> {
                            log.error("  - {}: {} 个", row.get("event_type"), row.get("count"));
                        });
                }
            } else {
                log.debug("所有事件已处理完成");
            }
        } catch (Exception e) {
            log.error("检查事件状态失败", e);
        }
    }

    /**
     * 每天凌晨1点生成事件统计报告
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateDailyReport() {
        try {
            // 总事件数
            String totalSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION";
            Integer totalCount = jdbcTemplate.queryForObject(totalSql, Integer.class);
            
            // 已完成事件数
            String completedSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NOT NULL";
            Integer completedCount = jdbcTemplate.queryForObject(completedSql, Integer.class);
            
            // 待处理事件数
            String pendingSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL";
            Integer pendingCount = jdbcTemplate.queryForObject(pendingSql, Integer.class);
            
            log.info("=== 每日事件统计报告 ===");
            log.info("总事件数: {}", totalCount);
            log.info("已完成: {}", completedCount);
            log.info("待处理: {}", pendingCount);
            log.info("报告生成时间: {}", LocalDateTime.now());
            log.info("========================");
        } catch (Exception e) {
            log.error("生成事件统计报告失败", e);
        }
    }

    /**
     * 获取事件统计信息
     */
    public EventStatistics getEventStatistics() {
        try {
            String pendingSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL";
            Integer pendingCount = jdbcTemplate.queryForObject(pendingSql, Integer.class);
            
            String overdueSql = "SELECT COUNT(*) FROM EVENT_PUBLICATION " +
                              "WHERE completion_date IS NULL " +
                              "AND publication_date < ?";
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            Integer overdueCount = jdbcTemplate.queryForObject(overdueSql, Integer.class, oneHourAgo);
            
            return new EventStatistics(
                pendingCount != null ? pendingCount : 0,
                overdueCount != null ? overdueCount : 0,
                LocalDateTime.now()
            );
        } catch (Exception e) {
            log.error("获取事件统计信息失败", e);
            return new EventStatistics(0, 0, LocalDateTime.now());
        }
    }

    /**
     * 事件统计信息
     */
    public record EventStatistics(
        int pendingEvents,      // 待处理事件数
        int overdueEvents,      // 超时事件数
        LocalDateTime reportTime // 报告时间
    ) {
        public boolean hasOverdueEvents() {
            return overdueEvents > 0;
        }
    }
}
