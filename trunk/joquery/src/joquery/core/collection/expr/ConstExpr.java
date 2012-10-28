package joquery.core.collection.expr;

import joquery.core.QueryException;
import joquery.core.QueryMode;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class ConstExpr<T> implements IExpr<T>
{
    private Object constVal;

    public ConstExpr(Object constVal)
    {
        this.constVal = constVal;
    }

    @Override
    public boolean supportsMode(QueryMode mode)
    {
        return true;
    }

    @Override
    public boolean add(IExpr<T> expr)
    {
        return false;
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
        return constVal;
    }
}
