package com.stephen.zhihu.base;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.*;

public class RedisAccessCounter implements Runnable {
    private Jedis jedis;
    private volatile boolean quit = false;
    private Thread cleanThread;

    public RedisAccessCounter(String host, int port) {
        jedis = new Jedis(host, port);
    }

    // 精度
    private static final int[] PRECISIONS = new int[]{5, 60, 300, 3600, 18000, 86400};
    // 要保存的记录数量
    private static final int COUNT = 120;

    public void updateCounter() {
        long now = System.currentTimeMillis() / 1000;
        // 减少和服务器的直接通信，这里使用非事务型流水线来进行通信
        Pipeline pipeline = jedis.pipelined();

        for (int precision : PRECISIONS) {
            int pnow = ((int) now / precision) * precision;
            String hashKey = precision + ":hits";
            pipeline.zadd("known:", 0.0, hashKey);
            pipeline.hincrBy("count:" + hashKey, pnow + "", 1);
        }

        pipeline.sync();
    }

    public Map<Long, Long> getCounter(int precision) {
        // 检查参数的合法性
        checkPrecision(precision);
        Map<String, String> data = jedis.hgetAll("count:" + precision + ":hits");
        return transformData(data);
    }

    private static Map<Long, Long> transformData(Map<String, String> data) {
        Map<Long, Long> result = new TreeMap<>(Long::compareTo);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            result.put(Long.parseLong(entry.getKey()), Long.parseLong(entry.getValue()));
        }
        return result;
    }

    private static void checkPrecision(int precision) {
        for (int p : PRECISIONS) {
            if (p == precision)
                return;
        }
        throw new IllegalArgumentException();
    }

    public void stopCleaning() {
        quit = true;
    }

    public void clean() {
        cleanThread = new Thread(this);
        cleanThread.setDaemon(true);
        cleanThread.start();
    }

    private static Set<Long> transformToLong(Set<String> keys) {
        Set<Long> result = new TreeSet<>(Long::compareTo);
        for (String key : keys) {
            result.add(Long.parseLong(key));
        }

        return result;
    }

    public void close() {
        stopCleaning();
        if (jedis != null) {
            jedis.close();
        }
    }

    @Override
    public void run() {
        Pipeline pipeline = jedis.pipelined();
        int passes = 0;
        while (!quit) {
            long start = System.currentTimeMillis() / 1000;
            int index = 0;
            while (index < jedis.zcard("known:") && !quit) {
                Set<String> hashes = jedis.zrange("known", index, index);
                index += 1;
                // 此时没有计数器
                if (hashes == null || hashes.isEmpty()) {
                    break;
                }
                String hash = (String) hashes.toArray()[0];
                // 获取精度
                int prec = Integer.parseInt(hash.split(":")[0]);
                // 看此时是否需要进行清理
                int bprec = prec / 60 == 0 ? 1 : prec / 60;
                // 此时不需要进行清理，遍历下一个
                if (passes % bprec != 0) {
                    continue;
                }

                // 用来看哪些数据需要删除，在此时间之前的统统删除，最多保留COUNT个记录
                long cutoff = System.currentTimeMillis() / 1000 - COUNT * prec;
                String hkey = "count:" + hash;
                Set<String> keys = jedis.hkeys(hkey);
                Set<Long> times = transformToLong(keys);
                // 需要删除的
                Set<Long> removals = ((SortedSet<Long>) times).headSet(cutoff);

                if (!removals.isEmpty()) {
                    for (Long removal : removals) {
                        pipeline.hdel(hkey, removal.toString());
                    }
                    pipeline.sync();
                }
            }
            passes += 1;
            long duration = System.currentTimeMillis() / 1000 - start + 1;
            try {
                Thread.sleep(duration > 60 ? 1000 : (60 - duration) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
