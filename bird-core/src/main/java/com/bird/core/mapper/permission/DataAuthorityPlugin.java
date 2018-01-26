package com.bird.core.mapper.permission;

import com.baomidou.mybatisplus.toolkit.PluginUtils;
import com.bird.core.Constants;
import com.bird.core.mapper.PagedQueryParam;
import com.bird.core.service.query.PagedListQueryDTO;
import com.bird.core.utils.SpringContextHolder;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * @author liuxx
 * @date 2018/1/25
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})})
public class DataAuthorityPlugin implements Interceptor {

    /**
     * 拦截方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler handler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject statementHandler = SystemMetaObject.forObject(handler);
        MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) return invocation.proceed();

        BoundSql boundSql = handler.getBoundSql();
        Object param = boundSql.getParameterObject();

        if (param instanceof MapperMethod.ParamMap || param instanceof PagedQueryParam) {
            DataAuthority authority = null;
            //取值DataAuthority
            if (param instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap map = (MapperMethod.ParamMap) param;
                //如果参数不包括数据权限相关的数据，返回
                if (!map.containsKey(Constants.DataAuthority.AUTHORITY_PARAM_NAME)) return invocation.proceed();

                Object authorityObj = map.get(Constants.DataAuthority.AUTHORITY_PARAM_NAME);
                if (!DataAuthority.class.isInstance(authority)) return invocation.proceed();
                authority = (DataAuthority) authorityObj;
            } else {
                PagedListQueryDTO dto = ((PagedQueryParam) param).getQuery();
                if (dto == null || dto.getAuthority() == null) return invocation.proceed();
                authority = dto.getAuthority();
            }


            DataAuthorityProvider provider = SpringContextHolder.getBean(DataAuthorityProvider.class);
            if (provider == null) return invocation.proceed();

            Long userId = authority.getUserId();
            String field = authority.getField();

            Set<Long> userIds = provider.getSubordinateUserIds(userId);
            if (userIds == null || userIds.size() == 0) return invocation.proceed();

            String sql = boundSql.getSql();
            sql = sql.replace("where", "WHERE");

            String[] spans = sql.split("WHERE");

            int lastIndex = spans.length - 1;
            if (lastIndex < 0) return invocation.proceed();//TODO：处理没有where子句的sql
            spans[lastIndex] = " " + field + " in (" + join(userIds) + ") and " + spans[lastIndex];

            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = spans.length; i < len; i++) {
                sb.append(spans[i]);
                if (i < len - 1) {
                    sb.append("WHERE");
                }
            }
            sql = sb.toString();
            statementHandler.setValue("delegate.boundSql.sql", sql);
            return invocation.proceed();
        }
        return invocation.proceed();
    }

    /**
     * 拦截目标
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String join(Collection collection) {
        //调用前判断了null与size，此处可不用判断

        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (Object obj : collection) {
            sb.append(obj.toString());
            if (++index < collection.size()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
