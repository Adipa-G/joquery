package joquery.core;

import joquery.Exec;
import joquery.Query;
import joquery.ResultTransformer;
import joquery.core.expr.ConstExpr;
import joquery.core.expr.ExecExpr;
import joquery.core.expr.IExpr;
import joquery.core.expr.ReflectionExpr;
import joquery.core.expr.cmp.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class QueryImpl<T,U,W extends Query> implements Query<T, W>
{
    private QueryMode queryMode;

    protected Iterable<T> items;
    protected IExpr<T> condition;

    @Override
    public W from(Iterable<T> list)
    {
        items = list;
        //noinspection unchecked
        return (W)this;
    }

    @Override
    public W where()
    {
        queryMode = QueryMode.WHERE;
        //noinspection unchecked
        return (W)this;
    }

    @Override
    public W exec(Exec<T> exec) throws QueryException
    {
        return addExpr(new ExecExpr<>(exec));
    }

    @Override
    public W property(String property) throws QueryException
    {
        return addExpr(new ReflectionExpr<T>(property));
    }

    @Override
    public W value(Object value) throws QueryException
    {
        return addExpr(new ConstExpr<T>(value));
    }

    @Override
    public W and() throws QueryException
    {
        return addExpr(new AndCndExpr<T>());
    }

    @Override
    public W or() throws QueryException
    {
        return addExpr(new OrCndExpr<T>());
    }

    @Override
    public W eq() throws QueryException
    {
        return addExpr(new EqCndExpr<T>());
    }

    @Override
    public W lt() throws QueryException
    {
        return addExpr(new LtCndExpr<T>());
    }

    @Override
    public W le() throws QueryException
    {
        return addExpr(new LeCndExpr<T>());
    }

    @Override
    public W gt() throws QueryException
    {
        return addExpr(new GtCndExpr<T>());
    }

    @Override
    public W ge() throws QueryException
    {
        return addExpr(new GeCndExpr<T>());
    }

    @Override
    public W in() throws QueryException
    {
        return addExpr(new InCndExpr<T>());
    }

    @Override
    public W between() throws QueryException
    {
        return addExpr(new BetweenCndExpr<T>());
    }

    private W addExpr(IExpr<T> expr) throws QueryException
    {
        boolean added;

        switch (queryMode)
        {
            case WHERE:
                added = addWhereExpr(expr);
                break;
            case GROUP:
                added = false;
                break;
            default:
                added = false;
        }

        if (!added)
            throw new QueryException("failed while adding expression to tree " + expr);

        //noinspection unchecked
        return (W)this;
    }

    private boolean addWhereExpr(IExpr<T> expr)
    {
        boolean added;
        if (condition != null)
        {
            added = condition.add(expr);
            if (!added)
            {
                added = expr.add(condition);
                condition = expr;
            }
        }
        else
        {
            added = true;
            condition = expr;
        }
        return added;
    }

    protected Collection<U> executeSelect(ResultTransformer<T, U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        for (T t : items)
        {
            Object result = condition != null ? condition.evaluate(t) : true;
            if (result instanceof Boolean)
            {
                boolean boolResult = (Boolean)result;
                if (boolResult)
                    retVal.add(transformer.transform(t));
            }
            else
            {
                throw new QueryException(String.format("Evaluation resulted in unexpected result %s should have been a boolean",result));
            }
        }
        return retVal;
    }
}
