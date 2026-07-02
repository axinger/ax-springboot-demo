package com.github.axinger.controller;

import com.github.axinger.service.DataStructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分布式数据结构 Controller — Redisson 新特性
 *
 * <pre>
 *  基础:    RBucket, RAtomicLong, RBitSet
 *  集合:    RMap, RList, RSet, RScoredSortedSet, RQueue
 *  高级:    RBloomFilter, RRateLimiter, RCountDownLatch
 *  地理:    RGeo
 *  统计:    RHyperLogLog
 *  本地缓存: RLocalCachedMap
 * </pre>
 */
@Tag(name = "数据结构Demo", description = "Redisson 分布式数据结构新特性")
@RestController
@RequestMapping("/ds")
public class DataStructureController {

    @Resource
    private DataStructureService ds;

    // ==================== 基础类型 ====================

    @Operation(summary = "RBucket — 简单键值")
    @GetMapping("/bucket")
    public Map<String, Object> bucket(@RequestParam(defaultValue = "demo:bucket") String key,
                                       @RequestParam(defaultValue = "Hello Redisson") String value) {
        return Map.of("result", ds.bucketDemo(key, value));
    }

    @Operation(summary = "RAtomicLong — 原子计数器")
    @GetMapping("/atomic-long")
    public Map<String, Object> atomicLong(@RequestParam(defaultValue = "demo:atomic") String key) {
        return Map.of("result", ds.atomicLongDemo(key));
    }

    @Operation(summary = "RBitSet — 位图签到")
    @GetMapping("/bitset")
    public Map<String, Object> bitSet(@RequestParam(defaultValue = "demo:bitset") String name) {
        return ds.bitSetDemo(name);
    }

    // ==================== 集合类型 ====================

    @Operation(summary = "RMap — 分布式HashMap")
    @GetMapping("/map")
    public Map<String, Object> map(@RequestParam(defaultValue = "demo:map") String name) {
        return ds.mapDemo(name);
    }

    @Operation(summary = "RList — 分布式List")
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "demo:list") String name) {
        return ds.listDemo(name);
    }

    @Operation(summary = "RSet — 去重集合")
    @GetMapping("/set")
    public Map<String, Object> set(@RequestParam(defaultValue = "demo:set") String name) {
        return ds.setDemo(name);
    }

    @Operation(summary = "RScoredSortedSet — 排行榜")
    @GetMapping("/scored-sorted-set")
    public Map<String, Object> scoredSortedSet(@RequestParam(defaultValue = "demo:score") String name) {
        return ds.scoredSortedSetDemo(name);
    }

    @Operation(summary = "RQueue — FIFO队列")
    @GetMapping("/queue")
    public Map<String, Object> queue(@RequestParam(defaultValue = "demo:queue") String name) {
        return ds.queueDemo(name);
    }

    // ==================== 高级类型 ====================

    @Operation(summary = "RBloomFilter — 布隆过滤器", description = "判断元素是否可能存在, 用于防止缓存穿透")
    @GetMapping("/bloom-filter")
    public Map<String, Object> bloomFilter(@RequestParam(defaultValue = "demo:bloom") String name) {
        return ds.bloomFilterDemo(name);
    }

    @Operation(summary = "RRateLimiter — 限流器", description = "控制请求速率, 保护下游服务")
    @GetMapping("/rate-limiter")
    public Map<String, Object> rateLimiter(@RequestParam(defaultValue = "demo:limiter") String name) {
        return ds.rateLimiterDemo(name);
    }

    @Operation(summary = "RCountDownLatch — 分布式倒计时")
    @GetMapping("/countdown/init")
    public Map<String, Object> countdownInit(@RequestParam(defaultValue = "3") int count) {
        return Map.of("result", ds.countDownLatchInit(count));
    }

    @Operation(summary = "RCountDownLatch — 倒计时-1")
    @GetMapping("/countdown/count")
    public Map<String, Object> countdownCount() {
        return Map.of("result", ds.countDownLatchCountDown());
    }

    // ==================== 地理/统计 ====================

    @Operation(summary = "RGeo — 地理位置", description = "计算距离、查找附近")
    @GetMapping("/geo")
    public Map<String, Object> geo(@RequestParam(defaultValue = "demo:geo") String name) {
        return ds.geoDemo(name);
    }

    @Operation(summary = "RHyperLogLog — UV统计", description = "极低内存统计海量数据基数, 误差<1%")
    @GetMapping("/hyper-log-log")
    public Map<String, Object> hyperLogLog(@RequestParam(defaultValue = "demo:hll") String name) {
        return ds.hyperLogLogDemo(name);
    }

    // ==================== 本地缓存 ====================

    @Operation(summary = "RLocalCachedMap — 本地缓存Map", description = "读多写少场景, 本地缓存减少Redis网络开销")
    @GetMapping("/local-cached-map")
    public Map<String, Object> localCachedMap(@RequestParam(defaultValue = "demo:localMap") String name) {
        return ds.localCachedMapDemo(name);
    }

    // ==================== 概览 ====================

    @Operation(summary = "数据结构概览", description = "Redisson 所有分布式数据结构一览")
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", "Redisson 分布式数据结构大全");
        result.put("all", ds.overview());

        result.put("scenarios", Map.of(
                "计数器/ID生成", "RAtomicLong",
                "缓存", "RBucket, RMap, RLocalCachedMap",
                "排行榜", "RScoredSortedSet",
                "消息队列", "RQueue, RDeque, RTopic, RStream",
                "去重", "RSet, RBloomFilter, RHyperLogLog",
                "限流", "RRateLimiter",
                "签到/在线", "RBitSet",
                "附近的人/门店", "RGeo",
                "分布式协调", "RCountDownLatch, RSemaphore, RLock",
                "UV/PV统计", "RHyperLogLog"
        ));

        return result;
    }
}
