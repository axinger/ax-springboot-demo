package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式数据结构 — 新特性演示
 * <pre>
 *  基础类型: RBucket, RAtomicLong, RBitSet
 *  集合类型: RMap, RList, RSet, RScoredSortedSet, RQueue, RDeque
 *  高级类型: RBloomFilter, RRateLimiter, RCountDownLatch
 *  地理/统计: RGeo, RHyperLogLog
 *  本地缓存: RLocalCachedMap
 * </pre>
 */
@Slf4j
@Service
public class DataStructureService {

    @Resource
    private RedissonClient redissonClient;

    // ==================== RBucket — 简单键值 ====================
    public String bucketDemo(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, 30, TimeUnit.SECONDS);
        String got = bucket.get();
        log.info("[RBucket] set {}={}, get={}", key, value, got);
        return "RBucket: " + key + "=" + got + " (TTL=30s)";
    }

    // ==================== RAtomicLong — 原子计数器 ====================
    public String atomicLongDemo(String key) {
        RAtomicLong atomic = redissonClient.getAtomicLong(key);
        long val = atomic.incrementAndGet();
        long current = atomic.get();
        log.info("[RAtomicLong] {} → {}", key, current);
        return "RAtomicLong: " + key + "=" + current;
    }

    // ==================== RMap — 分布式 HashMap ====================
    public Map<String, Object> mapDemo(String mapName) {
        RMap<String, String> map = redissonClient.getMap(mapName);
        map.put("name", "张三");
        map.put("age", "25");
        map.put("city", "北京");
        map.expire(60, TimeUnit.SECONDS);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RMap");
        result.put("size", map.size());
        result.put("all", new HashMap<>(map));
        result.put("name", map.get("name"));
        return result;
    }

    // ==================== RList — 分布式 List ====================
    public Map<String, Object> listDemo(String listName) {
        RList<String> list = redissonClient.getList(listName);
        list.delete();
        list.addAll(Arrays.asList("Java", "Spring", "Redis", "Redisson"));
        list.add(1, "Kafka");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RList");
        result.put("size", list.size());
        result.put("items", new ArrayList<>(list));
        result.put("get(0)", list.get(0));
        return result;
    }

    // ==================== RSet — 分布式 Set ====================
    public Map<String, Object> setDemo(String setName) {
        RSet<String> set = redissonClient.getSet(setName);
        set.delete();
        set.addAll(Arrays.asList("A", "B", "C", "A")); // 去重
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RSet");
        result.put("size", set.size());
        result.put("contains('A')", set.contains("A"));
        result.put("members", new ArrayList<>(set));
        return result;
    }

    // ==================== RScoredSortedSet — 带分数的有序集合 ====================
    public Map<String, Object> scoredSortedSetDemo(String setName) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(setName);
        set.delete();
        set.add(95.5, "张三");
        set.add(88.0, "李四");
        set.add(72.5, "王五");
        set.add(90.0, "赵六");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RScoredSortedSet (成绩排名)");
        result.put("rank_from_high", set.valueRangeReversed(0, -1));
        result.put("top3", set.valueRangeReversed(0, 2));
        result.put("score('张三')", set.getScore("张三"));
        return result;
    }

    // ==================== RQueue / RDeque — 队列/双端队列 ====================
    public Map<String, Object> queueDemo(String queueName) {
        RQueue<String> queue = redissonClient.getQueue(queueName);
        queue.delete();
        queue.add("任务1");
        queue.add("任务2");
        queue.add("任务3");

        String polled = queue.poll();
        String peeked = queue.peek();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RQueue (FIFO)");
        result.put("polled", polled);
        result.put("peeked", peeked);
        result.put("remaining", new ArrayList<>(queue));
        return result;
    }

    // ==================== RBloomFilter — 布隆过滤器 ====================
    public Map<String, Object> bloomFilterDemo(String name) {
        RBloomFilter<String> bloom = redissonClient.getBloomFilter(name);
        bloom.tryInit(10000L, 0.01); // 预计1万条, 容错率1%
        bloom.add("user:1001");
        bloom.add("user:1002");
        bloom.add("user:1003");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RBloomFilter");
        result.put("expectedInsertions", 10000);
        result.put("falseProbability", 0.01);
        result.put("contains('user:1001')", bloom.contains("user:1001"));
        result.put("contains('user:9999')", bloom.contains("user:9999")); // 大概率 false
        result.put("count", bloom.count());
        result.put("desc", "布隆过滤器: 不存在的一定不存在, 存在的可能误判");
        return result;
    }

    // ==================== RRateLimiter — 限流器 ====================
    public Map<String, Object> rateLimiterDemo(String name) {
        RRateLimiter limiter = redissonClient.getRateLimiter(name);
        // 每秒最多 5 个请求
        limiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        int pass = 0;
        int reject = 0;
        for (int i = 0; i < 10; i++) {
            if (limiter.tryAcquire()) {
                pass++;
            } else {
                reject++;
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RRateLimiter");
        result.put("rate", "5 req/s (OVERALL)");
        result.put("totalAttempts", 10);
        result.put("passed", pass);
        result.put("rejected", reject);
        result.put("desc", "10次请求中通过" + pass + "次, 限流" + reject + "次");
        return result;
    }

    // ==================== RCountDownLatch — 分布式倒计时 ====================
    private static final String LATCH_KEY = "demo:latch";

    public String countDownLatchInit(int count) {
        RCountDownLatch latch = redissonClient.getCountDownLatch(LATCH_KEY);
        latch.trySetCount(count);
        log.info("[CountDownLatch] 初始化: count={}", count);
        return "倒计时初始化: " + count;
    }

    public String countDownLatchCountDown() {
        RCountDownLatch latch = redissonClient.getCountDownLatch(LATCH_KEY);
        latch.countDown();
        long remaining = latch.getCount();
        log.info("[CountDownLatch] 倒计时 -1, 剩余: {}", remaining);
        return remaining == 0 ? "倒计时完成! 所有任务可以继续" : "倒计时 -1, 剩余: " + remaining;
    }

    // ==================== RBitSet — 位图 ====================
    public Map<String, Object> bitSetDemo(String name) {
        RBitSet bitSet = redissonClient.getBitSet(name);
        bitSet.delete();
        // 签到: 第1,3,7天签到
        bitSet.set(1);
        bitSet.set(3);
        bitSet.set(7);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RBitSet (签到场景)");
        result.put("day1_signed", bitSet.get(1));
        result.put("day3_signed", bitSet.get(3));
        result.put("day5_signed", bitSet.get(5));
        result.put("day7_signed", bitSet.get(7));
        result.put("cardinality", bitSet.cardinality()); // 签到天数
        result.put("desc", "位图签到: 共签到" + bitSet.cardinality() + "天");
        return result;
    }

    // ==================== RGeo — 地理位置 ====================
    public Map<String, Object> geoDemo(String name) {
        RGeo<String> geo = redissonClient.getGeo(name);
        geo.delete();
        // 添加位置
        geo.add(116.397128, 39.916527, "北京");
        geo.add(121.473701, 31.230416, "上海");
        geo.add(113.264385, 23.129112, "广州");
        geo.add(114.057868, 22.543099, "深圳");

        // 计算距离
        Double dist = geo.dist("北京", "上海", GeoUnit.KILOMETERS);

        // 北京 1000km 范围内的城市
        List<String> nearby = geo.radius("北京", 1500, GeoUnit.KILOMETERS);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RGeo");
        result.put("beijing_shanghai_km", String.format("%.0f km", dist));
        result.put("near_beijing_1500km", nearby);
        result.put("all_cities", geo.pos("北京", "上海", "广州", "深圳"));
        return result;
    }

    // ==================== RHyperLogLog — 基数统计 ====================
    public Map<String, Object> hyperLogLogDemo(String name) {
        RHyperLogLog<String> hll = redissonClient.getHyperLogLog(name);
        hll.delete();
        // 模拟 UV 统计: 10w 个用户访问
        for (int i = 0; i < 100000; i++) {
            hll.add("user:" + i);
        }
        // 重复添加 — HLL 自动去重
        for (int i = 0; i < 50000; i++) {
            hll.add("user:" + i);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RHyperLogLog (UV统计)");
        result.put("actualVisitors", 100000);
        result.put("hllCount", hll.count());
        result.put("memory", "仅占用 ~12KB");
        result.put("desc", "HyperLogLog: 10万UV仅用12KB内存, 误差<1%");
        return result;
    }

    // ==================== RLocalCachedMap — 本地缓存 Map ====================
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> localCachedMapDemo(String name) {
        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
                .cacheSize(1000)
                .timeToLive(30, TimeUnit.SECONDS)
                .maxIdle(20, TimeUnit.SECONDS);
        RLocalCachedMap<String, String> map = redissonClient.getLocalCachedMap(name, options);

        map.put("key1", "value1");
        map.put("key2", "value2");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", "RLocalCachedMap");
        result.put("desc", "读多写少场景 — 本地缓存减少 Redis 网络调用");
        result.put("ttl", "30s");
        result.put("key1", map.get("key1"));
        result.put("key2", map.get("key2"));
        return result;
    }

    // ==================== 概览 ====================
    public List<Map<String, String>> overview() {
        return Arrays.asList(
                Map.of("类型", "RBucket", "场景", "简单键值存储", "类比", "Redis String"),
                Map.of("类型", "RAtomicLong", "场景", "分布式计数器/ID生成", "类比", "AtomicLong"),
                Map.of("类型", "RMap", "场景", "分布式HashMap", "类比", "Redis Hash"),
                Map.of("类型", "RList", "场景", "分布式列表/消息队列", "类比", "Redis List"),
                Map.of("类型", "RSet", "场景", "去重集合", "类比", "Redis Set"),
                Map.of("类型", "RScoredSortedSet", "场景", "排行榜", "类比", "Redis ZSet"),
                Map.of("类型", "RQueue/RDeque", "场景", "FIFO队列/双端队列", "类比", "Redis List"),
                Map.of("类型", "RBloomFilter", "场景", "缓存穿透防护/去重", "类比", "Guava BloomFilter"),
                Map.of("类型", "RRateLimiter", "场景", "接口限流", "类比", "Guava RateLimiter"),
                Map.of("类型", "RCountDownLatch", "场景", "分布式协调", "类比", "CountDownLatch"),
                Map.of("类型", "RBitSet", "场景", "签到/在线状态", "类比", "Redis Bitmap"),
                Map.of("类型", "RGeo", "场景", "附近的人/门店", "类比", "Redis Geo"),
                Map.of("类型", "RHyperLogLog", "场景", "UV统计/基数统计", "类比", "Redis HyperLogLog"),
                Map.of("类型", "RLocalCachedMap", "场景", "读多写少本地缓存", "类比", "Caffeine + Redis")
        );
    }
}
