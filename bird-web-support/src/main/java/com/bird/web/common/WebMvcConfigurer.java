package com.bird.web.common;

import com.bird.web.common.version.ApiVersionRequestHandlerMapping;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.Servlet;
import java.time.Duration;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/9/8
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class })
@AutoConfigureAfter(name = {"com.bird.web.common.configuration.WebAutoConfiguration","com.bird.web.sso.client.configuration.SsoClientAutoConfiguration","com.bird.trace.skywalking.configuration.SkywalkingTraceAutoConfiguration"})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    private static final String[] SERVLET_LOCATIONS = {"/"};

    private final WebMvcProperties mvcProperties;
    private final ResourceProperties resourceProperties;
    private final ListableBeanFactory beanFactory;
    private final ObjectProvider<HttpMessageConverters> messageConvertersProvider;
    private final ObjectProvider<List<HandlerInterceptorAdapter>> interceptorsProvider;

    public WebMvcConfigurer(ResourceProperties resourceProperties, WebMvcProperties mvcProperties
            , ListableBeanFactory beanFactory
            , ObjectProvider<HttpMessageConverters> messageConvertersProvider
            , ObjectProvider<List<HandlerInterceptorAdapter>> interceptorsProvider) {

        this.mvcProperties = mvcProperties;
        this.resourceProperties = resourceProperties;
        this.beanFactory = beanFactory;
        this.messageConvertersProvider = messageConvertersProvider;
        this.interceptorsProvider = interceptorsProvider;
    }

    static String[] getResourceLocations(String[] staticLocations) {
        String[] locations = new String[staticLocations.length + SERVLET_LOCATIONS.length];
        System.arraycopy(staticLocations, 0, locations, 0, staticLocations.length);
        System.arraycopy(SERVLET_LOCATIONS, 0, locations, staticLocations.length, SERVLET_LOCATIONS.length);
        return locations;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.messageConvertersProvider.ifAvailable(customConverters -> converters.addAll(customConverters.getConverters()));
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        if (this.beanFactory.containsBean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)) {
            Object taskExecutor = this.beanFactory.getBean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME);
            if (taskExecutor instanceof AsyncTaskExecutor) {
                configurer.setTaskExecutor(((AsyncTaskExecutor) taskExecutor));
            }
        }
        Duration timeout = this.mvcProperties.getAsync().getRequestTimeout();
        if (timeout != null) {
            configurer.setDefaultTimeout(timeout.toMillis());
        }
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        if (this.mvcProperties.getMessageCodesResolverFormat() != null) {
            DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
            resolver.setMessageCodeFormatter(this.mvcProperties.getMessageCodesResolverFormat());
            return resolver;
        }
        return null;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.addBeans(registry, this.beanFactory);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!this.resourceProperties.isAddMappings()) {
            return;
        }
        Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
        CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/")
                    .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl);
        }
        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (!registry.hasMappingForPattern(staticPathPattern)) {
            registry.addResourceHandler(staticPathPattern)
                    .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
                    .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl);
        }
    }

    /**
     * 自定义版本号RequestMapping
     *
     * @return ApiVersionRequestHandlerMapping
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new ApiVersionRequestHandlerMapping();
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.interceptorsProvider.ifAvailable(interceptors ->
                interceptors.forEach(interceptor ->
                        registry.addInterceptor(interceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns("/*.ico", "/*.htm", "/*.html", "/*.css", "/*.js", "/*.txt", "/*.json", "/*/api-docs", "/swagger**", "/webjars/**", "/configuration/**")));
    }

    private Integer getSeconds(Duration cachePeriod) {
        return (cachePeriod != null) ? (int) cachePeriod.getSeconds() : null;
    }
}
