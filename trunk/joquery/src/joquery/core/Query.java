package joquery.core;

import joquery.IExec;
import joquery.IQuery;
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
public class Query<T> implements IQuery<T>
{
    private Iterable<T> items;
    private QueryMode queryMode;
    private IExpr<T> condition;

    @Override
    public IQuery<T> from(Iterable<T> list)
    {
        items = list;
        return this;
    }

    @Override
    public IQuery<T> where()
    {
        queryMode = QueryMode.WHERE;
        return this;
    }

    @Override
    public IQuery<T> exec(IExec<T> exec) throws QueryException
    {
        return addExpr(new ExecExpr<>(exec));
    }

    @Override
    public IQuery<T> property(String property) throws QueryException
    {
        return addExpr(new ReflectionExpr<T>(property));
    }

    @Override
    public IQuery<T> value(Object value) throws QueryException
    {
        return addExpr(new ConstExpr<T>(value));
    }

    @Override
    public IQuery<T> eq() throws QueryException
    {
        return addExpr(new EqCmpExpr<T>());
    }

    @Override
    public IQuery<T> lt() throws QueryException
    {
        return addExpr(new LtCmpExpr<T>());
    }

    @Override
    public IQuery<T> le() throws QueryException
    {
        return addExpr(new LeCmpExpr<T>());
    }

    @Override
    public IQuery<T> gt() throws QueryException
    {
        return addExpr(new GtCmpExpr<T>());
    }

    @Override
    public IQuery<T> ge() throws QueryException
    {
        return addExpr(new GeCmpExpr<T>());
    }

    @Override
    public IQuery<T> in() throws QueryException
    {
        return addExpr(new InCmpExpr<T>());
    }

    @Override
    public IQuery<T> between() throws QueryException
    {
        return addExpr(new BetweenCmpExpr<T>());
    }

    private IQuery<T> addExpr(IExpr<T> expr) throws QueryException
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
    public Collection<T> execute() throws QueryException
    {
        Collection<T> retVal = new ArrayList<>();
        for (T t : items)
        {
            Object result = condition.evaluate(t);
            if (result instanceof Boolean)
            {
                boolean boolResult = (Boolean)result;
                if (boolResult)
                    retVal.add(t);
            }
            else
            {
                throw new QueryException(String.format("Evaluation resulted in unexpected result %s should have been a boolean",result));
            }
        }
        return retVal;
    }
}
