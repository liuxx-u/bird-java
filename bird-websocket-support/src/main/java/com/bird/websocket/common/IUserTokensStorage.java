package com.bird.websocket.common;

import java.util.Set;

/**
 * 用户 - Tokens 映射
 *
 * @author yuanjian
 */
public interface IUserTokensStorage {

    /**
     * 通过user获取全部Token信息
     *
     * @param userId userId
     * @return 全部Token信息
     */
    Set<String> get(String userId);

    /**
     * 添加 userId - Token 映射信息
     *
     * @param userId userId
     * @param token  Token
     * @return 添加结果
     */
    boolean add(String userId, String token);

    /**
     * 添加 userId - Tokens 映射信息
     *
     * @param userId userId
     * @param tokens tokens
     * @return 添加结果
     */
    boolean put(String userId, Set<String> tokens);

    /**
     * 移除 对应userId的所有Token信息
     *
     * @param userId userId
     */
    void remove(String userId);

    /**
     * 是否包含 userId的数据
     *
     * @param userId userId
     * @return 判断结果
     */
    boolean contain(String userId);
}
