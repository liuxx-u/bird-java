package com.bird.websocket.common.server;

import javax.websocket.Session;
import java.util.List;

/**
 * socket session字典服务
 *
 * @author liuxx
 * @since 2020/12/30
 */
public interface ISessionDirectory {

    /**
     * 添加socket连接
     *
     * @param token   token
     * @param session session
     * @return 是否添加成功
     */
    boolean add(String token, Session session);

    /**
     * 移除socket连接
     *
     * @param token token
     */
    void remove(String token);

    /**
     * 根据userId获取所有socket连接
     *
     * @param userId userId
     * @return session集合
     */
    List<Session> getUserSessions(String userId);

    /**
     * 根据token获取session
     *
     * @param token token
     * @return session
     */
    Session getSession(String token);

    /**
     * 获取所有Session
     *
     * @return session集合
     */
    List<Session> getAllSession();
}
