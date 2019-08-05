package com.git.hui.boot.redis.site.core;

import com.git.hui.boot.redis.site.util.DateUtil;
import com.git.hui.boot.redis.utils.RedisOperator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * Created by @author yihui in 16:28 19/5/12.
 */
@Component
public class VisitService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    private final static Long PV_URI_TAG_EXPIRE_TIME = 604800L; // pv每天标记数据过期时间为七天

    /**
     * 获取pv
     *
     * pv存储结果为hash，一个应用一个key; field 为uri； value为pv
     *
     * @return null表示首次有人访问；这个时候需要+1
     */
    public Long getPv(String key, String uri) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] ans = connection.hGet(key.getBytes(), uri.getBytes());
                if (ans == null || ans.length == 0) {
                    return null;
                }

                return Long.parseLong(new String(ans));
            }
        });
    }

    /**
     * 获取uri对应的uv，以及当前访问ip的历史访问排名
     * 使用zset来存储，key为uri唯一标识；value为ip；score为访问的排名
     *
     * @param key : 由app与URI来生成，即一个uri维护一个uv集
     * @param ip: 访问者ip
     * @return 返回uv/rank, 如果对应的值为0，表示没有访问过
     */
    public ImmutablePair</** uv */Long, /** rank */Long> getUv(String key, String ip) {
        // 获取总uv数，也就是最大的score
        Long uv = redisTemplate.execute((RedisCallback<Long>) connection -> {
            Set<RedisZSetCommands.Tuple> set = connection.zRangeWithScores(key.getBytes(), -1, -1);
            if (CollectionUtils.isEmpty(set)) {
                return 0L;
            }

            Double score = set.stream().findFirst().get().getScore();
            return score.longValue();
        });

        if (uv == null || uv == 0L) {
            // 表示还没有人访问过
            return ImmutablePair.of(0L, 0L);
        }

        // 获取ip对应的访问排名
        Long rank = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Double score = connection.zScore(key.getBytes(), ip.getBytes());
                return score == null ? 0L : score.longValue();
            }
        });

        return ImmutablePair.of(uv, rank);
    }


    /**
     * 判断ip今天是否访问过
     * 采用bitset来判断ip是否有访问，key由app与uri唯一确定
     *
     * @return true 表示今天访问过/ false 表示今天没有访问过
     */
    public boolean visitToday(String key, String ip) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hGet(key.getBytes(),ip.getBytes()) != null ? true : false;
            }
        });
    }


    /**
     * pv 次数+1
     *
     * @param key
     * @param uri
     */
    public void addPv(String key, String uri) {
        redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hIncrBy(key.getBytes(), uri.getBytes(), 1);
                return null;
            }
        });
    }

    /**
     * uv +1
     *
     * @param key
     * @param ip
     * @param rank
     */
    public void addUv(String key, String ip, Long rank) {
        redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zAdd(key.getBytes(), rank, ip.getBytes());
                return null;
            }
        });
    }

    /**
     * 标记ip访问过这个key
     *
     * @param key
     * @param ip
     */
    public void tagVisit(String key, String ip) {
        redisTemplate.execute((RedisConnection connection) ->{
            connection.hSet(key.getBytes(),ip.getBytes(), DateUtil.getNowDateTime().getBytes());
            connection.expire(key.getBytes(),PV_URI_TAG_EXPIRE_TIME);
            return null;
        });
    }

    /**
     * 热度，每访问一次，计数都+1
     *
     * @param key
     * @param uri
     * @return
     */
    public Long addHot(String key, String uri) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hIncrBy(key.getBytes(), uri.getBytes(), 1);
            }
        });
    }
}
