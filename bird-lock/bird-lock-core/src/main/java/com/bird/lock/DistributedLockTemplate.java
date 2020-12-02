package com.bird.lock;

import com.bird.lock.reject.ExceptionRejectStrategy;
import com.bird.lock.reject.RejectStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * @author liuxx
 * @since 2020/12/2
 */
public class DistributedLockTemplate {

    private final IDistributedLock lock;

    public DistributedLockTemplate(IDistributedLock lock) {
        this.lock = lock;
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier) {
        return this.withLock(lockKey, supplier, new ExceptionRejectStrategy<>());
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier, RejectStrategy<T> rejectStrategy) {
        return this.withLock(lockKey, supplier, () -> lock.tryLock(lockKey), rejectStrategy);
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier, long keyExpire) {
        return this.withLock(lockKey, supplier, keyExpire, new ExceptionRejectStrategy<>());
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier, long keyExpire, RejectStrategy<T> rejectStrategy) {
        return this.withLock(lockKey, supplier, () -> lock.tryLock(lockKey, keyExpire), rejectStrategy);
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier, long keyExpire, long retryExpire) {
        return this.withLock(lockKey, supplier, keyExpire, retryExpire, new ExceptionRejectStrategy<>());
    }

    public <T> T withLock(String lockKey, Supplier<T> supplier, long keyExpire, long retryExpire, RejectStrategy<T> rejectStrategy) {
        return this.withLock(lockKey, supplier, () -> lock.tryLock(lockKey, keyExpire, retryExpire), rejectStrategy);
    }


    public <T> T withLock(String lockKey, Supplier<T> supplier, long keyExpire, long retryInterval, long retryExpire, RejectStrategy<T> rejectStrategy) {
        return this.withLock(lockKey, supplier, () -> lock.tryLock(lockKey, keyExpire, retryInterval, retryExpire), rejectStrategy);
    }

    private <T> T withLock(String lockKey, Supplier<T> supplier, Supplier<Boolean> lockSupplier, RejectStrategy<T> rejectStrategy) {
        if (StringUtils.isBlank(lockKey)) {
            throw new RuntimeException("lock key cannot be empty");
        }
        if (lockSupplier.get()) {
            try {
                return supplier.get();
            } finally {
                lock.unLock(lockKey);
            }
        } else {
            // 未获取到锁，执行拒绝策略
            return rejectStrategy.reject(lockKey);
        }
    }

    public <T> T withUnique(String lockKey, Supplier<T> query, Supplier<T> createFunc) {
        // 先尝试获取一次
        T value = query.get();
        if (value != null) {
            // 获取成功, 快速返回
            return value;
        }
        if (StringUtils.isBlank(lockKey)) {
            throw new RuntimeException("lock key cannot be empty");
        }
        // 加锁
        return withLock(lockKey, () -> {
            // 双重检查
            T val = query.get();
            if (val != null) {
                return val;
            }
            // 最终创建
            return createFunc.get();
        });
    }
}
