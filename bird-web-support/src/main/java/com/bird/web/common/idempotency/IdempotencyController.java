package com.bird.web.common.idempotency;

import com.bird.core.Result;
import com.bird.core.exception.UserFriendlyException;
import com.bird.web.common.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 幂等性接口
 *
 * @author liuxx
 * @since 2020/7/27
 */
@RestController
@RequestMapping("/vb/idempotency")
public class IdempotencyController {

    @Autowired(required = false)
    RedisTemplate<String, Object> redisTemplate;

    private final IdempotencyProperties idempotencyProperties;

    public IdempotencyController(IdempotencyProperties idempotencyProperties) {
        this.idempotencyProperties = idempotencyProperties;
    }

    @GetMapping("/token")
    public Result<String> token() {
        if (redisTemplate == null) {
            throw new UserFriendlyException("Redis未配置，幂等功能未开启");
        }
        String token = UUID.randomUUID().toString();
        String cacheKey = WebConstant.Cache.IDEMPOTENCY + token;

        redisTemplate.opsForValue().set(cacheKey, 1, this.idempotencyProperties.getExpire(), TimeUnit.MINUTES);
        return Result.success("获取成功", token);
    }
}
