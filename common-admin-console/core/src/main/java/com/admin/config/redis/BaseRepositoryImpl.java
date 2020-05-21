package com.admin.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 庞绪
 * @since 2018/11/5
 */
public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {

    @Autowired
    protected RedisTemplate<String, Serializable> redisTemplate;

    private String getLeader() {
        return "_";
    }

    protected String getPrefix() {
        return "";
    }

    protected byte[] getKeyByteArray(String key) {
        return (this.getLeader() + this.getPrefix() + key).getBytes();
    }

    protected List<byte[]> getKeyByteArray(String... key) {
        List<byte[]> bytes = Arrays.stream(key).map(k -> {
            return (this.getLeader() + this.getPrefix() + key).getBytes();
        }).collect(Collectors.toList());

        return bytes;
    }

    @SuppressWarnings("unchecked")
    protected RedisSerializer<T> getDefaultSerializer() {
        return (RedisSerializer<T>) redisTemplate.getDefaultSerializer();
    }

    public String getLeaderPrefix() {
        return this.getLeader() + this.getPrefix();
    }

    @Override
    public Boolean save(String key, T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn ->
                conn.set(this.getKeyByteArray(key), this.getDefaultSerializer().serialize(value))
        );
    }

    @Override
    public Boolean save(String key, T value, long seconds) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn ->
                conn.setEx(this.getKeyByteArray(key), seconds, this.getDefaultSerializer().serialize(value))
        );
    }

    @Override
    public Boolean save(String key, T value, long seconds, RedisStringCommands.SetOption option) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn ->
                conn.set(this.getKeyByteArray(key), this.getDefaultSerializer().serialize(value), Expiration.seconds(seconds), option)
        );
    }

    @Override
    public Boolean setNX(String key, T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn ->
                conn.setNX(this.getKeyByteArray(key), this.getDefaultSerializer().serialize(value))
        );
    }

    @Override
    public Boolean expire(String key, long seconds) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> conn.expire(this.getKeyByteArray(key), seconds));
    }

    @Override
    public Long ttl(String key) {
        return redisTemplate.execute((RedisCallback<Long>) conn -> conn.ttl(this.getKeyByteArray(key)));
    }

    @Override
    public T get(String key) {
        return redisTemplate.execute((RedisCallback<T>) conn -> {
            byte[] value = conn.get(this.getKeyByteArray(key));
            return this.getDefaultSerializer().deserialize(value);
        });
    }

    @Override
    public List<T> getList(List key) {
        return redisTemplate.opsForValue().multiGet(key);
    }

    @Override
    public Long delete(String key) {
        return redisTemplate.execute((RedisCallback<Long>) conn -> conn.del(this.getKeyByteArray(key)));
    }

    @Override
    public Boolean exists(Object key) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> conn.exists(this.getKeyByteArray(String.valueOf(key))));
    }

    @Override
    public boolean hMSet(String key, Map<String, Object> value, long seconds) {
        Map<byte[], byte[]> map = new HashMap<>();
        for (Map.Entry<String, Object> item : value.entrySet())
            map.put(item.getKey().getBytes(), item.getValue().toString().getBytes());
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            byte[] k = this.getKeyByteArray(key);
            conn.hMSet(k, map);
            return conn.expire(k, seconds);
        });
    }

    @Override
    public void hMSet(String key, Map<String, Object> value) {
        Map<byte[], byte[]> map = new HashMap<>();
        for (Map.Entry<String, Object> item : value.entrySet())
            map.put(item.getKey().getBytes(), item.getValue().toString().getBytes());
        redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            conn.hMSet(this.getKeyByteArray(key), map);
            return true;
        });
    }

    @Override
    public boolean hSet(String key, String hk, T hv) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            return conn.hSet(this.getKeyByteArray(key), hk.getBytes(), hv.toString().getBytes());
        });
    }

    @Override
    public String hGet(String key, String hk) {
        return redisTemplate.execute((RedisCallback<String>) conn -> {
            byte[] value = conn.hGet(this.getKeyByteArray(key), hk.getBytes());
            return value == null ? null : new String(value);
        });
    }

    @Override
    public Map<String, String> hGet(String key) {
        return redisTemplate.execute((RedisCallback<Map<String, String>>) conn -> {
            Map<byte[], byte[]> map = conn.hGetAll(this.getKeyByteArray(key));
            Map<String, String> result = new HashMap<>();
            for (Map.Entry<byte[], byte[]> item : map.entrySet())
                result.put(new String(item.getKey()), new String(item.getValue()));
            return result;
        });
    }

    @Override
    public List<String> hMGet(String key, String... hks) {
        return redisTemplate.execute((RedisCallback<List<String>>) conn -> {
            List<String> result = new ArrayList<>();
            byte[][] m = new byte[hks.length][];
            for (int i = 0; i < hks.length; i++)
                m[i] = hks[i].getBytes();
            List<byte[]> value = conn.hMGet(this.getKeyByteArray(key), m);
            for (byte[] v : value)
                if (v != null)
                    result.add(new String(v));
            return result;
        });
    }


    @Override
    public boolean hSetObj(String key, String hk, T hv) {
        return redisTemplate.execute((RedisCallback<Boolean>) conn -> {
            return conn.hSet(this.getKeyByteArray(key), hk.getBytes(), this.getDefaultSerializer().serialize(hv));
        });
    }

    @Override
    public T hGetObject(String key, String hk) {
        return redisTemplate.execute((RedisCallback<T>) conn -> {
            byte[] value = conn.hGet(this.getKeyByteArray(key), hk.getBytes());
            return value == null ? null : this.getDefaultSerializer().deserialize(value);
        });
    }

}
