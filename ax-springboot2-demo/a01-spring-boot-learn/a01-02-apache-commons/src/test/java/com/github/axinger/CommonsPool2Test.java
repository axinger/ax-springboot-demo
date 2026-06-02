package com.github.axinger;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Apache Commons Pool2 对象池示例
 */
class CommonsPool2Test {

    /**
     * 模拟一个需要池化的资源（如数据库连接）
     */
    static class Connection {
        private final int id;
        private boolean active;

        public Connection(int id) {
            this.id = id;
            this.active = true;
            System.out.println("创建连接 #" + id);
        }

        public void execute(String sql) {
            if (!active) {
                throw new IllegalStateException("连接已关闭");
            }
            System.out.println("连接 #" + id + " 执行: " + sql);
        }

        public void close() {
            active = false;
            System.out.println("连接 #" + id + " 已关闭");
        }

        public boolean isActive() {
            return active;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 连接工厂：定义如何创建、包装和销毁对象
     */
    static class ConnectionFactory extends BasePooledObjectFactory<Connection> {
        private final AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Connection create() {
            return new Connection(counter.incrementAndGet());
        }

        @Override
        public PooledObject<Connection> wrap(Connection connection) {
            return new DefaultPooledObject<>(connection);
        }

        @Override
        public void destroyObject(PooledObject<Connection> p) {
            p.getObject().close();
        }

        @Override
        public boolean validateObject(PooledObject<Connection> p) {
            return p.getObject().isActive();
        }

        @Override
        public void activateObject(PooledObject<Connection> p) {
            System.out.println("激活连接 #" + p.getObject().getId());
        }

        @Override
        public void passivateObject(PooledObject<Connection> p) {
            System.out.println("钝化连接 #" + p.getObject().getId());
        }
    }

    @Test
    void testBasicPool() throws Exception {
        // 创建对象池
        GenericObjectPool<Connection> pool = new GenericObjectPool<>(new ConnectionFactory());

        // 从池中获取对象
        Connection conn1 = pool.borrowObject();
        conn1.execute("SELECT * FROM users");

        // 归还对象到池中
        pool.returnObject(conn1);

        // 再次获取（可能复用同一个对象）
        Connection conn2 = pool.borrowObject();
        conn2.execute("SELECT * FROM orders");
        pool.returnObject(conn2);

        // 关闭池
        pool.close();
    }

    @Test
    void testPoolConfig() throws Exception {
        // 配置对象池
        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(5);              // 最大对象数
        config.setMaxIdle(3);               // 最大空闲数
        config.setMinIdle(1);               // 最小空闲数
        config.setMaxWaitMillis(3000);      // 获取对象最大等待时间（毫秒）
        config.setTestOnBorrow(true);       // 借用时验证对象
        config.setTestOnReturn(true);       // 归还时验证对象
        config.setTimeBetweenEvictionRunsMillis(60000); // 空闲检测周期

        GenericObjectPool<Connection> pool = new GenericObjectPool<>(new ConnectionFactory(), config);

        // 借用多个对象
        Connection conn1 = pool.borrowObject();
        Connection conn2 = pool.borrowObject();
        Connection conn3 = pool.borrowObject();

        System.out.println("活跃对象数: " + pool.getNumActive());   // 3
        System.out.println("空闲对象数: " + pool.getNumIdle());     // 0
        System.out.println("总对象数: " + pool.getNumWaiters());    // 等待线程数

        // 归还
        pool.returnObject(conn1);
        pool.returnObject(conn2);
        pool.returnObject(conn3);

        System.out.println("归还后活跃: " + pool.getNumActive());   // 0
        System.out.println("归还后空闲: " + pool.getNumIdle());     // 3

        pool.close();
    }

    @Test
    void testPoolExhausted() {
        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(2);              // 最多2个对象
        config.setMaxWaitMillis(1000);      // 等待1秒
        config.setBlockWhenExhausted(true); // 耗尽时阻塞等待

        GenericObjectPool<Connection> pool = new GenericObjectPool<>(new ConnectionFactory(), config);

        try {
            Connection conn1 = pool.borrowObject();
            Connection conn2 = pool.borrowObject();
            System.out.println("已借用2个对象");

            // 尝试借第3个（会超时抛异常）
            System.out.println("尝试借第3个对象...");
            Connection conn3 = pool.borrowObject();
            System.out.println("借到第3个: " + conn3.getId());
        } catch (Exception e) {
            System.out.println("获取对象失败: " + e.getMessage());
        } finally {
            pool.close();
        }
    }

    @Test
    void testPoolStats() throws Exception {
        GenericObjectPoolConfig<Connection> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMaxIdle(5);

        GenericObjectPool<Connection> pool = new GenericObjectPool<>(new ConnectionFactory(), config);

        // 获取并查看统计信息
        Connection conn = pool.borrowObject();
        System.out.println("活跃数: " + pool.getNumActive());
        System.out.println("空闲数: " + pool.getNumIdle());
        System.out.println("最大容量: " + pool.getMaxTotal());
        System.out.println("借出次数: " + pool.getBorrowedCount());

        pool.returnObject(conn);
        pool.close();
    }
}
