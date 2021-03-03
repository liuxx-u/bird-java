package com.bird.web.sso.client.interceptor;

import com.bird.core.session.BirdSession;
import com.bird.core.session.SessionContext;
import com.bird.web.sso.SsoAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuxx
 * @since 2020/8/31
 */
public abstract class AbstractAuthorizeInterceptor extends HandlerInterceptorAdapter {

    /**
     * 从请求中解析Session信息
     * @param request 请求
     * @return session
     */
    protected abstract BirdSession getSession(HttpServletRequest request);

    /**
     * 校验权限
     * @param userId userId
     * @param permissions 权限集合
     * @param roles 角色集合
     * @param checkAll 是否检查全部
     * @return 是否检查通过
     */
    protected abstract boolean checkPermissions(String userId, List<String> permissions, List<String> roles, boolean checkAll);

    /**
     * 拦截器处理方法
     *
     * @param request  request
     * @param response response
     * @param handler  跨域第一次OPTIONS请求时handler为AbstractHandlerMapping.PreFlightHandler，不拦截
     * @return 是否进行下一步处理
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 根据请求获取session信息并存入ThreadLocal
        BirdSession session = this.getSession(request);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        SsoAuthorize methodAuthorize = handlerMethod.getMethodAnnotation(SsoAuthorize.class);
        if (methodAuthorize != null && methodAuthorize.anonymous()) {
            return true;
        }

        Class clazz = handlerMethod.getBeanType();
        SsoAuthorize typeAuthorize = (SsoAuthorize) clazz.getDeclaredAnnotation(SsoAuthorize.class);
        if (typeAuthorize != null && typeAuthorize.anonymous()) {
            return true;
        }

        if (methodAuthorize != null || typeAuthorize != null) {
            if (session == null || session.getUserId() == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户信息已失效");
                return false;
            }
            if (methodAuthorize == null || methodAuthorize.permissions().length == 0) {
                return true;
            }

            List<String> permissions = new ArrayList<>();
            if (typeAuthorize == null || typeAuthorize.permissions().length == 0) {
                permissions = Arrays.asList(methodAuthorize.permissions());
            } else {
                for (String typePermission : typeAuthorize.permissions()) {
                    for (String methodPermission : methodAuthorize.permissions()) {
                        permissions.add(typePermission + methodPermission);
                    }
                }
            }

            boolean isCheckAll = methodAuthorize.isCheckAll();
            if (!checkPermissions(session.getUserId().toString(), permissions, Arrays.asList(methodAuthorize.roles()), isCheckAll)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "用户没有当前操作的权限");
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //移除线程中session信息，防止数据返回后线程未清掉session信息，线程复用时拿到无效的session信息
        SessionContext.removeSession();
    }
}
