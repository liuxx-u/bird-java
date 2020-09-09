package com.bird.web.common.version;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuxx
 * @since 2020/6/11
 */
public class ApiVersionRequestHandlerMapping extends RequestMappingHandlerMapping {

    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();


    /**
     * Look up the best-matching handler method for the current request.
     * If multiple matches are found, the best match is selected.
     *
     * @param lookupPath mapping lookup path within the current servlet mapping
     * @param request    the current request
     * @return the best-matching handler method, or {@code null} if no match
     * @see #handleMatch(Object, String, HttpServletRequest)
     * @see #handleNoMatch(Set, String, HttpServletRequest)
     */
    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        return super.lookupHandlerMethod(lookupPath, request);
    }

    /**
     * Uses method and type-level @{@link RequestMapping} annotations to create
     * the RequestMappingInfo.
     *
     * @return the created RequestMappingInfo, or {@code null} if the method
     * does not have a {@code @RequestMapping} annotation.
     * @see #getCustomMethodCondition(Method)
     * @see #getCustomTypeCondition(Class)
     */
    @Override
    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        ApiVersion apiVersion = handlerType.getAnnotation(ApiVersion.class);
        ApiVersion methodApi = method.getAnnotation(ApiVersion.class);
        if (methodApi != null) {
            apiVersion = methodApi;
        }

        RequestMappingInfo info = createRequestMappingInfo(method, null);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType, apiVersion);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    /**
     * Delegates to {@link #createRequestMappingInfo(RequestMapping, RequestCondition)},
     * supplying the appropriate custom {@link RequestCondition} depending on whether
     * the supplied {@code annotatedElement} is a class or method.
     *
     * @see #getCustomTypeCondition(Class)
     * @see #getCustomMethodCondition(Method)
     */
    @Nullable
    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element, ApiVersion apiVersion) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        if (requestMapping == null && apiVersion == null) {
            return null;
        }

        RequestCondition<?> condition = (element instanceof Class ? getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return createRequestMappingInfo(requestMapping, condition, apiVersion);
    }

    /**
     * Create a {@link RequestMappingInfo} from the supplied
     * {@link RequestMapping @RequestMapping} annotation, which is either
     * a directly declared annotation, a meta-annotation, or the synthesized
     * result of merging annotation attributes within an annotation hierarchy.
     */
    private RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, @Nullable RequestCondition<?> customCondition, ApiVersion apiVersion) {

        if(requestMapping == null){
            return createRequestMappingInfo(customCondition,apiVersion);
        }

        String prefix = this.getVersionPrefix(apiVersion);
        List<String> paths = Arrays.stream(requestMapping.path()).map(path -> this.ensureStart(prefix) + this.ensureStart(path)).collect(Collectors.toList());

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(paths.toArray(new String[]{})))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name());
        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(this.config).build();
    }

    private RequestMappingInfo createRequestMappingInfo(@Nullable RequestCondition<?> customCondition, ApiVersion apiVersion) {

        String prefix = this.getVersionPrefix(apiVersion);

        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(new String[]{this.ensureStart(prefix)}));
        if (customCondition != null) {
            builder.customCondition(customCondition);
        }
        return builder.options(this.config).build();
    }


    private String getVersionPrefix(ApiVersion apiVersion) {
        if (apiVersion == null) {
            return null;
        }
        return apiVersion.value();
    }

    private String ensureStart(String str) {
        if (StringUtils.isBlank(str)) {
            return StringUtils.EMPTY;
        }

        return str.startsWith("/") ? str : "/" + str;
    }
}
