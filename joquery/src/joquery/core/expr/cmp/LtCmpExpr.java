package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class LtCmpExpr<T> extends CmpExpr<T>
{
    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        Object leftVal = left.evaluate(t);
        Object rightVal = right.evaluate(t);
        if (leftVal == rightVal)
            return false;
        if (leftVal == null)
            return true;
        if (!(leftVal instanceof Comparable))
            return false;
        Comparable<Object> comparable = (Comparable<Object>) leftVal;
        return comparable.compareTo(rightVal) < 0;
    }
}
