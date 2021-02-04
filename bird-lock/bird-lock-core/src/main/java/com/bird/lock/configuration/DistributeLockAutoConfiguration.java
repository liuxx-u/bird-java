package com.bird.lock.configuration;

import com.bird.lock.DistributedLockTemplate;
import com.bird.lock.IDistributedLock;
import com.bird.lock.aspect.DistributedLockAspect;
import com.bird.lock.expression.IKeyVariableProvider;
import com.bird.lock.expression.ILockKeyParser;
import com.bird.lock.expression.SessionVariableProvider;
import com.bird.lock.expression.SpelLockKeyParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxx
 * @since 2020/12/2
 */
@Configuration
@ConditionalOnBean(IDistributedLock.class)
public class DistributeLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IKeyVariableProvider.class)
    public IKeyVariableProvider keyVariableProvider() {
        return new SessionVariableProvider();
    }

    @Bean
    @ConditionalOnMissingBean(ILockKeyParser.class)
    public ILockKeyParser lockKeyParser(IKeyVariableProvider keyVariableProvider) {
        return new SpelLockKeyParser(keyVariableProvider);
    }

    @Bean
    @ConditionalOnBean(IDistributedLock.class)
    @ConditionalOnMissingBean(DistributedLockTemplate.class)
    public DistributedLockTemplate distributedLockTemplate(IDistributedLock distributedLock) {
        return new DistributedLockTemplate(distributedLock);
    }

    @Bean
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    public DistributedLockAspect distributedLockAspect(IDistributedLock distributedLock, ILockKeyParser lockKeyParser) {
        return new DistributedLockAspect(distributedLock, lockKeyParser);
    }
}
