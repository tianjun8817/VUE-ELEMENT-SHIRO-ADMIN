package com.admin.config.redis;

import org.springframework.data.redis.connection.RedisStringCommands;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 庞绪
 * @since 2018/11/5
 */
public interface BaseRepository<T> {

    /**
     * 保存
     *
     * @param key   key
     * @param value 值
     * @return boolean
     */
    Boolean save(String key, T value);

    /**
     * 保存（带过期时间）
     *
     * @param key     key
     * @param value   值
     * @param seconds 秒
     * @return boolean
     */
    Boolean save(String key, T value, long seconds);

    /**
     * 保存（带过期时间）
     *
     * @param key     key
     * @param value   值
     * @param seconds 秒
     * @param option  写参数，XX 存在则写入，NX 不存在则写入
     * @return boolean
     */
    Boolean save(String key, T value, long seconds, RedisStringCommands.SetOption option);

    /**
     * 保存（如果键存在则不创建并返回false）
     *
     * @param key   key
     * @param value 值
     * @return boolean
     */
    Boolean setNX(String key, T value);

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param seconds 秒
     * @return boolean
     */
    Boolean expire(String key, long seconds);

    /**
     * 查看KEY的过期时间
     *
     * @param key key
     * @return 秒
     */
    Long ttl(String key);

    /**
     * 读取
     *
     * @param key key
     * @return t
     */
    T get(String key);

    /**
     * 删除
     *
     * @param key key
     */
    Long delete(String key);

    /**
     * 检查给定 key 是否存在
     *
     * @param key key
     * @return Boolean
     */
    Boolean exists(Object key);

    boolean hMSet(String key, Map<String, Object> value, long seconds);

    void hMSet(String key, Map<String, Object> value);

    String getLeaderPrefix();

    boolean hSet(String key, String hk, T hv);

    String hGet(String key, String hk);

    Map<String, String> hGet(String key);

    List<String> hMGet(String key, String... hks);

    boolean hSetObj(String key, String hk, T hv);

    T hGetObject(String key, String hk);

    List<T> getList(List key);
}
