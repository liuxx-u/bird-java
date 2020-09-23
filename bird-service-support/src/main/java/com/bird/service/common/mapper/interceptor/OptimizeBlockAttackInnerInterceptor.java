package com.bird.service.common.mapper.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.Connection;

/**
 * 阻止全表更新、删除操作的拦截器
 *
 * @author liuxx
 * @since 2020/9/22
 */
public class OptimizeBlockAttackInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private final String logicDeleteField;

    public OptimizeBlockAttackInnerInterceptor(String logicDeleteField) {
        this.logicDeleteField = logicDeleteField;
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler handler = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = handler.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if (InterceptorIgnoreHelper.willIgnoreBlockAttack(ms.getId())) {
                return;
            }
            BoundSql boundSql = handler.boundSql();
            parserMulti(boundSql.getSql(), null);
        }
    }

    @Override
    protected void processDelete(Delete delete, int index, Object obj) {
        this.checkWhere(delete.getWhere(), "Prohibition of full table deletion");
    }

    @Override
    protected void processUpdate(Update update, int index, Object obj) {
        this.checkWhere(update.getWhere(), "Prohibition of table update operation");
    }


    protected void checkWhere(Expression where, String ex) {
        Assert.isFalse(this.fullMatch(where), ex);
    }

    private boolean fullMatch(Expression where) {
        if (where == null) {
            return true;
        }
        if (StringUtils.isNotBlank(this.logicDeleteField) && (where instanceof BinaryExpression)) {
            if (StringUtils.equals(((BinaryExpression) where).getLeftExpression().toString(), this.logicDeleteField)) {
                return true;
            }
        }
        if (where instanceof EqualsTo) {
            // example: 1=1
            EqualsTo equalsTo = (EqualsTo) where;

            return StringUtils.equals(equalsTo.getLeftExpression().toString(), equalsTo.getRightExpression().toString());
        } else if (where instanceof NotEqualsTo) {
            // example: 1 != 2
            NotEqualsTo notEqualsTo = (NotEqualsTo) where;
            return !StringUtils.equals(notEqualsTo.getLeftExpression().toString(), notEqualsTo.getRightExpression().toString());
        } else if (where instanceof OrExpression) {
            OrExpression orExpression = (OrExpression) where;

            Expression left = orExpression.getLeftExpression();
            Expression right = orExpression.getRightExpression();
            return fullMatch(left) || fullMatch(right);
        } else if (where instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) where;

            Expression left = andExpression.getLeftExpression();
            Expression right = andExpression.getRightExpression();
            return fullMatch(left) && fullMatch(right);
        } else if (where instanceof Parenthesis) {
            // example: (1 = 1)
            Parenthesis parenthesis = (Parenthesis) where;
            return fullMatch(parenthesis.getExpression());
        }

        return false;
    }
}
