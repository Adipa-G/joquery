package joquery.core.expr;

import joquery.core.QueryException;

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
