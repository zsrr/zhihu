package com.stephen.zhihu.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.util.UUID;

// redis实现 分布式锁服务

public class RedisLock {

    public static String acquireLockWithTimeout(Jedis connection, String lockName, long requestTimeout, long lockTimeout) {
        String identifier = UUID.randomUUID().toString();
        long endTime = System.currentTimeMillis() + requestTimeout;
        int lockTimeInSeconds = (int) (lockTimeout / 1000L);
        while (requestTimeout < 0 || System.currentTimeMillis() < endTime) {
            if (connection.setnx(lockName, identifier) == 1) {
                connection.expire(lockName, lockTimeInSeconds);
                return identifier;
            } else if (connection.ttl(lockName) < 0) {
                connection.expire(lockName, lockTimeInSeconds);
            }
        }
        return null;
    }

    public static boolean releaseLock(Jedis connection, String lockName, String identifier) {
        connection.watch(lockName);
        while (true) {
            try {
                if (identifier.equals(connection.get(lockName))) {
                    Pipeline pipeline = connection.pipelined();
                    pipeline.multi();
                    /*// 立即向服务端发送
                    Transaction tx = connection.multi();
                    tx.del(lockName);
                    tx.exec();*/
                    pipeline.del(lockName);
                    pipeline.exec();
                    pipeline.sync();
                    return true;
                }
                connection.unwatch();
                break;
            } catch (Exception ignore) {
            }
        }

        return false;
    }

}
