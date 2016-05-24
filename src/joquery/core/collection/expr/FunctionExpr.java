package joquery.core.collection.expr;

import joquery.core.QueryException;
import joquery.core.QueryMode;

import java.util.function.Function;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class FunctionExpr<T> implements IExpr<T>
{
    private Function<T,?> function;

    public FunctionExpr(Function<T,?> function)
    {
        this.function = function;
    }

    @Override
    public boolean supportsMode(QueryMode mode)
    {
        return mode == QueryMode.SELECT
                || mode == QueryMode.WHERE
                || mode == QueryMode.SORT;
    }

    @Override
    public boolean add(IExpr<T> expr)
    {
        return false;
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
	    try
	    {
		    return function.apply(t);
	    }
	    catch (Exception ex)
	    {
		    throw new QueryException(String.format("Unable retrieve value for %s",function));
	    }

    }
}
