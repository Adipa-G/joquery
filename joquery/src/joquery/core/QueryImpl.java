package joquery.core;

import joquery.Exec;
import joquery.Query;
import joquery.ResultTransformer;
import joquery.core.expr.ConstExpr;
import joquery.core.expr.ExecExpr;
import joquery.core.expr.IExpr;
import joquery.core.expr.ReflectionExpr;
import joquery.core.expr.cmp.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class QueryImpl<T,U> implements Query<T, U>
{
    private Iterable<T> items;
    private QueryMode queryMode;
    private IExpr<T> condition;

    @Override
    public Query<T, U> from(Iterable<T> list)
    {
        items = list;
        return this;
    }

    @Override
    public Query<T, U> where()
    {
        queryMode = QueryMode.WHERE;
        return this;
    }

    @Override
    public Query<T, U> exec(Exec<T> exec) throws QueryException
    {
        return addExpr(new ExecExpr<>(exec));
    }

    @Override
    public Query<T, U> property(String property) throws QueryException
    {
        return addExpr(new ReflectionExpr<T>(property));
    }

    @Override
    public Query<T, U> value(Object value) throws QueryException
    {
        return addExpr(new ConstExpr<T>(value));
    }

    @Override
    public Query<T, U> eq() throws QueryException
    {
        return addExpr(new EqCmpExpr<T>());
    }

    @Override
    public Query<T, U> lt() throws QueryException
    {
        return addExpr(new LtCmpExpr<T>());
    }

    @Override
    public Query<T, U> le() throws QueryException
    {
        return addExpr(new LeCmpExpr<T>());
    }

    @Override
    public Query<T, U> gt() throws QueryException
    {
        return addExpr(new GtCmpExpr<T>());
    }

    @Override
    public Query<T, U> ge() throws QueryException
    {
        return addExpr(new GeCmpExpr<T>());
    }

    @Override
    public Query<T, U> in() throws QueryException
    {
        return addExpr(new InCmpExpr<T>());
    }

    @Override
    public Query<T, U> between() throws QueryException
    {
        return addExpr(new BetweenCmpExpr<T>());
    }

    private Query<T, U> addExpr(IExpr<T> expr) throws QueryException
    {
        boolean added;

        if (condition != null)
        {
            added = expr.add(condition);
            if (added)
            {
                condition = expr;
            }
            else
            {
                added = condition.add(expr);
            }
        }
        else
        {
            added = true;
            condition = expr;
        }

        if (!added)
            throw new QueryException("failed while adding expression to tree " + expr);

        return this;
    }

    @Override
    public Collection<U> execute() throws QueryException
    {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class tClass = type.getActualTypeArguments()[0].getClass();
        Class uClass = type.getActualTypeArguments()[1].getClass();

        if (tClass != uClass)
            throw new QueryException(String.format("The result type of %s cannot be directly transformed from type %s." +
                                                           " Use a resultTransformer to achieve this"
                    ,tClass.getCanonicalName(),uClass.getCanonicalName()));

        ResultTransformer<T,U> resultTransformer = new ResultTransformer<T, U>()
        {
            @Override
            public U transform(T t)
            {
                //noinspection unchecked
                return (U)t;
            }
        };
        return execute(resultTransformer);
    }

    @Override
    public Collection<U> execute(ResultTransformer<T,U> transformer) throws QueryException
    {
        Collection<U> retVal = new ArrayList<>();
        for (T t : items)
        {
            Object result = condition.evaluate(t);
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
