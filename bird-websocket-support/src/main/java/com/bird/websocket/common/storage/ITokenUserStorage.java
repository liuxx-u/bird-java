package com.bird.websocket.common.storage;

/**
 * Token - 用户 映射
 *
 * @author yuanjian
 */
public interface ITokenUserStorage {

    /**
     * 通过token获取全部user信息
     *
     * @param token token
     * @return 用户标识信息
     */
    String get(String token);

    /**
     * 添加 Token - userId 映射信息
     *
     * @param token  Token
     * @param userId userId
     * @return 添加成功结果
     */
    boolean put(String token, String userId);

    /**
     * 移除 对应token的所有user信息
     *
     * @param token token
     * @return 如果存在，返回对应的user信息，否则返回null
     */
    String remove(String token);

    /**
     * 是否包含 token的数据
     *
     * @param token token
     * @return 判断结果
     */
    boolean contain(String token);
}
