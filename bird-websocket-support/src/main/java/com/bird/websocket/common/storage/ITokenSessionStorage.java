package com.bird.websocket.common.storage;

import javax.websocket.Session;
import java.util.List;

/**
 * Token - Session 映射
 *
 * @author yuanjian
 */
public interface ITokenSessionStorage {

    /**
     * 通过token获取 通讯Session
     *
     * @param token token
     * @return 通讯Session信息
     */
    Session get(String token);

    /**
     * 添加 Token - userId 映射信息
     *
     * @param token   Token
     * @param session 通讯Session
     * @return 添加成功结果
     */
    boolean put(String token, Session session);

    /**
     * 移除 对应userId的所有Session信息
     *
     * @param token Token
     */
    void remove(String token);

    /**
     * 是否包含 token的数据
     *
     * @param token userId
     * @return 判断结果
     */
    boolean contain(String token);

    /**
     * 获取所有session
     *
     * @return Session集合
     */
    List<Session> getAll();
}
