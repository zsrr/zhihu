package com.stephen.zhihu.base;

// Redis实现的分布式计数信号量

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ZParams;

import java.util.List;
import java.util.UUID;

// 基于Redis做的信号量

public class RedisSemaphore {

    public static String acquireSemaphore(Jedis connection, String semaName, long timeout, int limit) {

        String lockIdentifier = RedisLock.acquireLockWithTimeout(connection, "lock-semaphore", -1, 20 * 1000);

        String setName = semaName + ":owner";
        String cName = semaName + ":counter";
        String uuid = UUID.randomUUID().toString();

        Pipeline pipeline = connection.pipelined();
        pipeline.multi();
        pipeline.zremrangeByScore(semaName, "-inf", "" + (System.currentTimeMillis() - timeout));
        pipeline.zinterstore(setName, new ZParams().weightsByDouble(1.0, 0.0), setName, semaName);
        pipeline.exec();

        pipeline.incr(cName);
        Long counter = (Long) (pipeline.exec().get().get(0));

        pipeline.multi();
        pipeline.zadd(semaName, System.currentTimeMillis(), uuid);
        pipeline.zadd(setName, counter, uuid);
        pipeline.zrank(setName, uuid);

        List<Object> results = pipeline.exec().get();

        if (Integer.parseInt(results.get(results.size() - 1).toString()) < limit) {
            RedisLock.releaseLock(connection, "lock-semaphore", lockIdentifier);
            return uuid;
        }

        RedisLock.releaseLock(connection, "lock-semaphore", lockIdentifier);
        return null;
    }

    public static void releaseSemaphore(Jedis jedis, String semaName, String identifier) {
        String setName = semaName + ":owner";
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        pipeline.zrem(semaName, identifier);
        pipeline.zrem(setName, identifier);
    }

}
