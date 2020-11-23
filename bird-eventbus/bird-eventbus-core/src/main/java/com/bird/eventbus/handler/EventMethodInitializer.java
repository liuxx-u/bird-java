package com.bird.eventbus.handler;

import com.bird.eventbus.arg.IEventArg;
import com.bird.eventbus.registry.IEventRegistry;
import com.bird.eventbus.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liuxx
 * @since 2020/11/19
 */
@Slf4j
public class EventMethodInitializer {

    private boolean initialized = false;
    private final EventHandlerProperties handlerProperties;
    private final IEventRegistry eventRegistry;
    private final IEventMethodInitializeListener initializeListener;

    public EventMethodInitializer(EventHandlerProperties handlerProperties, IEventRegistry eventRegistry, IEventMethodInitializeListener initializeListener) {
        this.handlerProperties = handlerProperties;
        this.eventRegistry = eventRegistry;
        this.initializeListener = initializeListener;
    }

    public void initialize() {
        if(!this.initialized){
            return;
        }
        String scanPackages = this.handlerProperties.getScanPackages();
        if (StringUtils.isBlank(scanPackages)) {
            log.warn("事件处理方法扫码包路径不存在");
            return;
        }

        List<EventMethodDefinition> definitions = new ArrayList<>();
        Set<Class<?>> classes = ClassUtils.getClasses(scanPackages);
        if (classes != null) {
            for (Class<?> clazz : classes) {
                for (Method method : clazz.getDeclaredMethods()) {
                    EventHandler eventAnnotation = method.getAnnotation(EventHandler.class);
                    if (eventAnnotation == null) {
                        continue;
                    }

                    //被@EventHandler注解标注的方法只接受一个参数，且参数必须是IEventArg的子类
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1 || !IEventArg.class.isAssignableFrom(parameterTypes[0])) {
                        continue;
                    }

                    String argClassName = parameterTypes[0].getName();
                    this.eventRegistry.add(argClassName, method);

                    EventMethodDefinition definition = new EventMethodDefinition();
                    definition.setClazz(clazz.getName());
                    definition.setMethod(method.getName());
                    definition.setEvent(argClassName);
                    definition.setGroup(this.handlerProperties.getGroup());
                    definitions.add(definition);
                }
            }
        }
        if (initializeListener != null && !CollectionUtils.isEmpty(definitions)) {
            initializeListener.initialize(definitions);
        }
        this.initialized = true;
    }
}
