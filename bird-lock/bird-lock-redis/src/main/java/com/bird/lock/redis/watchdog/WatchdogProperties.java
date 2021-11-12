package com.bird.lock.redis.watchdog;

import lombok.Data;

/**
 * @author kayn.liu
 * @date 2021/11/8
 */
@Data
public class WatchdogProperties {

    /**
     * 是否开启看门狗，默认：true
     */
    private Boolean enabled = true;
    /**
     * 看门狗线程池大小，默认：1
     */
    private Integer poolSize = 1;
    /**
     * 续期次数，默认：3次
     * 续期机制：锁超时时间达到一半时，开始续期，每次续期后重置有效期，三次续期后有效期总计 = 2.5 * 原有效期
     */
    private Integer renewCount = 3;
}
